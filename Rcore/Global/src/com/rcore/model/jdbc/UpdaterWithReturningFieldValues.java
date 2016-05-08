package com.rcore.model.jdbc;


import com.rcore.global.DateTime;
import com.rcore.global.Token;
import com.rcore.global.adf.NullDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import oracle.jdbc.OracleDriver;


public final class UpdaterWithReturningFieldValues
{
  private UpdaterWithReturningFieldValues()
  {
    super();
  }
  //--------------------------------------------
  public static Map<String,Object> updateRow(
    Connection con,
    String sql,
    Map<String,Object> params,  // обязательный, != null
    String[] pkeys,             // обязательный, != null and length > 0
    String[] fieldsForReturn,
    String tableOrQuery
  ) throws SQLException
  {
    Map<String, Object> rc = null;
    NamedParameterStatement ps = null;
    ResultSet rs = null;
    try {
      ps = new NamedParameterStatement(con,sql);
      setParameters(ps, params);
      ps.executeUpdate();
      con.commit();
    }
    finally {
      if (ps != null) ps.close();
    }
    //----- Теперь прочтем --------
    Map<String,Object> keysParam = new HashMap<String, Object>();
    String fieldsList = Token.join(fieldsForReturn, ",");
    StringBuilder buf = new StringBuilder("select ").
                            append(fieldsList).
                            append(" from ").
                            append(tableOrQuery);
    if (pkeys != null && pkeys.length > 0) 
    {
      buf.append(" where ");
      for (String key : pkeys) 
      {
        buf.append(key).append(" = :").append(key).append(" and ");
        keysParam.put(key, params.get(key));
      }
      buf.setLength(buf.length() - 5);
    } // if
    try {
      ps = new NamedParameterStatement(con, buf.toString());
      setParameters(ps, keysParam);
      rs = ps.executeQuery();
      rc = new HashMap<String, Object>();
      if (rs.next()) 
      {
        for (String field : fieldsForReturn) 
        {
          rc.put(field, rs.getObject(field));
        }
      }
    }
    finally 
    {
      if (rs != null) rs.close();
      if (ps != null) ps.close();
    }
    //-----------
    return rc;
  }
  
  public static Map<String,Object> insertRow(
                                    Connection con,
                                    String sql,
                                    Map<String,Object> params,
                                    String[] fieldsForReturn
  ) throws SQLException
  {
    Map<String,Object> rc = null;
    NamedParameterStatement ps = null;
    ResultSet rs = null;
    try {
      ps = new NamedParameterStatement(con,sql,fieldsForReturn);
      setParameters(ps,params);
      ps.executeUpdate();
      con.commit();
      rs = ps.getGeneratedKeys(); //!
      //-------
      rc = new HashMap<String,Object>();
      if (rs != null && rs.next())
      {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++)
        {
          String name = meta.getColumnName(i);
          rc.put(name, rs.getObject(i));
        }
      }
    }
    finally 
    {
      if (rs != null) rs.close();
      if (ps != null) ps.close();
    }
    return rc;
  }

  public static Map<String, Object> insertRow(Connection con, 
                                              String sql, 
                                              Object[] params,
                                              String[] fieldsForReturn
                                              )
    throws SQLException
  {
    Map<String, Object> rc = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    rc = new HashMap<String, Object>();
    //------------------------------
    try {
      ps = con.prepareStatement(sql, fieldsForReturn);
      setParameters(ps, params);
      ps.executeUpdate();
      con.commit();
      rs = ps.getGeneratedKeys(); //!
      //-------
      if (rs != null && rs.next())
      {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++)
        {
          String name = meta.getColumnName(i);
          rc.put(name, rs.getObject(i));
        }
      }
    }
    finally 
    {
      if (rs != null) rs.close();
      if (ps != null) ps.close();
    }
    return rc;
  }
  //=====================================================================
  public static void setParameters(PreparedStatement st, Object[] params)
    throws SQLException
  {
    try
    {
      if (params != null)
      {
        Object param = null;
        for (int i = 0; i < params.length; i++)
        {
          param = params[i];
          if (param instanceof NullDb)
          {
            st.setNull(i + 1, ((NullDb) param).getType());
          }
          else
          {
            if (param instanceof java.util.Date)
            {
              param = DateTime.java_util_date2java_sql_date((java.util.Date) param);
            }
            st.setObject(i + 1, param);
          }
        } // for
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      if (ex instanceof SQLException)
      {
        throw (SQLException) ex;
      }
      else
      {
        throw new SQLException(ex.getMessage());
      }
    }
  }

  public static void setParameters(NamedParameterStatement st, Map<String, Object> params)
    throws SQLException
  {
    try
    {
      if (params != null)
      {
        Object param = null;
        Set<String> keys = params.keySet();
        for (String key: keys)
        {
          param = params.get(key);
          if (param instanceof NullDb)
          {
            st.setNull(key, ((NullDb) param).getType());
          }
          else
          {
            if (param instanceof java.util.Date)
            {
              param = DateTime.java_util_date2java_sql_date((java.util.Date) param);
            }
            st.setObject(key, param);
          }
        } // for
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      if (ex instanceof SQLException)
      {
        throw (SQLException) ex;
      }
      else
      {
        throw new SQLException(ex.getMessage());
      }
    }
  }
//================== TEST ===============
  private static Connection getConnection() throws SQLException
  {
    String username = "cb_test";
    String password = "cb_test";
    String thinConn = "jdbc:oracle:thin:@172.28.36.8:1521:sppr";
    DriverManager.registerDriver(new OracleDriver());
    Connection conn = DriverManager.getConnection(thinConn, username, password);
    conn.setAutoCommit(false);
    return conn;
  }
  public static void main(String[] args)
    throws Exception
  {
    //test1();
    //test2();
    //test3();
    test4();
  }

  static void test1()
    throws Exception
  {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      String query = " insert into t_sample_data\n" +
        "  (value, id_caption)\n" +
        "  values\n" +
        "  (?, ?)";
      //pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      pstmt = con.prepareStatement(query, new String[]
            { "ID" });
      pstmt.setString(1, "5");
      pstmt.setString(2, "55");
      pstmt.executeUpdate();
      con.commit();
      rs = pstmt.getGeneratedKeys(); //!!!
      //----------------------------------
      if (rs != null && rs.next())
      {
        //System.out.println("Generated Emp Id: " + rs.getObject(1));
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++)
        {
          String name = meta.getColumnName(i);
          System.out.println(name + " = " + rs.getObject(i));
        }
      }
    }
    finally
    {
      try
      {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (con != null) con.close();
      }
      catch (Exception ex)
      {
        ;
      }
    }
  }
  //================

  static void test2()  throws Exception
  {
    Connection con = getConnection();

    con = getConnection();
    String query = " insert into t_sample_data\n" +
      "  (VALUE, ID_CAPTION)\n" +
      "  values\n" +
      "  (?, ?)";
      //"  (:VALUE, :ID_CAPTION)";
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("VALUE", System.currentTimeMillis());
    params.put("ID_CAPTION", 77);
    Map<String,Object> map = insertRow(con,
                                          query,
                                          //params,
                                          new Object[]{11,22},
                                          new String[]{"ID","VALUE","ID_CAPTION"}
                                          );
    System.out.println("@="+map);
    //-------------------------
    con.close();
  }

  static void test3()  throws Exception
  {
    Connection con = getConnection();

    con = getConnection();
    String query = 
      "update t_sample_data " + 
      "   set VALUE      = :VALUE, " + 
      "       ID_CAPTION = :ID_CAPTION " + 
      " where ID         = :ID";
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("VALUE", 11);
    params.put("ID_CAPTION", 12);
    params.put("ID", 1);
    String[] pkeys = new String[] {"ID"};
    String[] fieldsForReturn = new String[] {"ID","VALUE","ID_CAPTION"};
    String tableOrQuery = "t_sample_data";
    Map<String, Object> map = updateRow(con, 
                                        query, 
                                        params, 
                                        pkeys, 
                                        fieldsForReturn, 
                                        tableOrQuery
                                        );
    System.out.println("@=" + map);
    //-------------------------
    con.close();
  }

  /**
   * Хороший способ изменения базы на основе sql = "select ..."
   * Вот как нужно
   * Проверено - работает
   * @throws Exception
   */
  private static void test4() throws Exception
  {
    Connection con = getConnection();
    Statement st = con.createStatement( // !!! В этом вся суть
        ResultSet.TYPE_SCROLL_INSENSITIVE, 
        ResultSet.CONCUR_UPDATABLE,
        ResultSet.HOLD_CURSORS_OVER_COMMIT
        );
    String sql = "select ID, VALUE, ID_CAPTION, 'kkk' P1 from t_sample_data where ID = 3";
    ResultSet rs = st.executeQuery(sql);
    if (rs.next()) 
    {
      // Вот как нужно делать update и перепрочитывать запись
      rs.updateInt(2, 104);
      rs.updateNull(3);
      rs.updateRow();
      con.commit();
      rs.refreshRow(); // Здесь допустимы виртуальные столбцы!
      System.out.println("@getInt(3)=" + rs.getInt(3)); //!!!
    }
    //----------------------------------
    rs.close();
    st.close();
    //---
    sql = "select ID, VALUE, ID_CAPTION from t_sample_data where ID = 3";
    st = con.createStatement( // !!! В этом вся суть
          ResultSet.TYPE_SCROLL_INSENSITIVE, 
          ResultSet.CONCUR_UPDATABLE, 
          ResultSet.HOLD_CURSORS_OVER_COMMIT);
    rs = st.executeQuery(sql);
    //----------------------------------
    rs.moveToInsertRow();
    rs.updateInt(2, 162);
    rs.updateInt(3, 164);
    rs.insertRow(); // виртуальный столбец здесь недопустим (ORA-01733)
    /**
     * rs.moveToInsertRow();
     * rs.updateInt("id",104);
     * rs.updateString("first","John");
     * //Commit row
     * rs.insertRow();
     */
    System.out.println("@@1=" + rs.getInt(1));
    con.commit();
    // rs.refreshRow(); // Здесь (при insert) неработает !
    System.out.println("@@2="+rs.getInt(1));
    con.close();
  }
}
