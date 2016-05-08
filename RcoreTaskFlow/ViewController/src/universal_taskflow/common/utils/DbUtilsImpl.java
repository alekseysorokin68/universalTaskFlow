package universal_taskflow.common.utils;

import com.rcore.global.DateTime;
import com.rcore.global.adf.NullDb;
import com.rcore.model.jdbc.NamedParameterStatement;
import com.rcore.model.jdbc.ResultSetStatementClose;

import java.io.Serializable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import universal_taskflow.common.data.SqlAttribute;
import universal_taskflow.common.types.VisualControlForSqlAttributeType;


public class DbUtilsImpl implements DbUtils
{
  private static DbUtilsImpl instance = new DbUtilsImpl();

  private DbUtilsImpl()
  {
    super();
  }
  public static DbUtilsImpl getInstance() 
  {
    return instance;
  }

  public List<Map<String, Object>> query2List(String sql, Connection con) throws SQLException
  {
    return query2List(sql, -1, con);
  }
  
  public String getInsertSql(List<SqlAttribute> sqlAttributes,
                              String objectName,
                              Map<String, Object> attributeValues,
                              Map<String, Object> returnParameters)
  throws Exception
  {
    List<SqlAttribute> insertList = getInsertList(sqlAttributes);
    if (insertList == null || insertList.isEmpty()) 
    {
      throw new Exception("Нет полей допустимых для вставки (insert)");
    }
    StringBuilder rc = new StringBuilder("insert into ").append(objectName).append(" \n(");
    StringBuilder sValues = new StringBuilder("(");
    for (SqlAttribute attribute : insertList)
    {
       rc.append(attribute.getName()).append(",");
       sValues.append(":").append(attribute.getName()).append(",");
      //---
      Object value = attributeValues.get(attribute.getName());
      boolean isCheckBox = (
          attribute.getVisualControlTypeOnFormInfo() != null && 
          attribute.getVisualControlTypeOnFormInfo().getType() != null &&
          attribute.getVisualControlTypeOnFormInfo().getType().equals(VisualControlForSqlAttributeType.CHECK_BOX));
      if (isCheckBox) 
      {
        if (value instanceof Boolean) 
        {
          if (value != null && (Boolean)value) 
          {
            value = attribute.getVisualControlTypeOnFormInfo().getCheckBoxTrueValue();
          }
          else 
          {
            value = attribute.getVisualControlTypeOnFormInfo().getCheckBoxFalseValue();
          }
        }
      }
      //---------------
      if (value != null) 
      {
        if (value instanceof java.util.Date)
        {
          java.util.Date date = (java.util.Date)value;
          java.sql.Date sqlDate = new java.sql.Date(date.getTime());
          returnParameters.put(attribute.getName(), sqlDate);
        }
        else
        {
          returnParameters.put(attribute.getName(), value);
        }
      }
      else 
      {
        returnParameters.put(attribute.getName(), null);
      }
    }
    sValues.setLength(sValues.length() - 1);
    sValues.append(")");
    rc.setLength(rc.length() - 1);
    rc.append(") values\n").append(sValues);
    return rc.toString();
  }
  
  public List<SqlAttribute> getInsertList(List<SqlAttribute> sqlAttributes)
  {
    List<SqlAttribute> rc = new ArrayList<SqlAttribute>();
    for (SqlAttribute att : sqlAttributes) 
    {
      if (att.isInInsertSql()) 
      {
        rc.add(att);
      }
    }
    return rc;
  }
  
  public List<SqlAttribute> getUpdateList(List<SqlAttribute> sqlAttributes)
  {
    List<SqlAttribute> rc = new ArrayList<SqlAttribute>();
    for (SqlAttribute att : sqlAttributes) 
    {
      if (att.isInUpdateSql()) 
      {
        rc.add(att);
      }
    }
    return rc;
  }
  
  public List<Map<String, Object>> query2List(String sql, 
                                              int max,
                                              Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    try
    {
      ResultSetMetaData md = rs.getMetaData();
      if (max == -1)
      {
        while (rs.next())
        {
          Map<String, Object> rec = resultSet2Record(rs, md);
          rc.add(rec);
        }
      }
      else
      {
        while (rs.next())
        {
          Map<String, Object> rec = resultSet2Record(rs, md);
          rc.add(rec);
          if (rc.size() == max)
          {
            break;
          }
        }
      }
    }
    catch (SQLException ex)
    {
      throw ex;
    }
    finally
    {
      rs.close();
      st.close();
    }
    rs = null;
    st = null;
    return rc;
  }

  public List<Map<String, Object>> query2List(String sqlQuery,
                                              Object[] params,
                                              Connection con)
    throws SQLException
  {
    return query2List(sqlQuery, params, -1, con);
  }

  public List<Map<String, Object>> query2List(String sqlQuery,
                                              Object[] params,
                                              int max,
                                              Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    PreparedStatement st = con.prepareStatement(sqlQuery);
    setParameters(st, params);
    ResultSet rs = st.executeQuery();
    try
    {
      ResultSetMetaData md = rs.getMetaData();
      if (max == -1)
      {
        while (rs.next())
        {
          Map<String, Object> rec = resultSet2Record(rs, md);
          rc.add(rec);
        }
      }
      else
      {
        while (rs.next())
        {
          Map<String, Object> rec = resultSet2Record(rs, md);
          rc.add(rec);
          if (rc.size() == max)
          {
            break;
          }
        }
      }
    }
    catch (SQLException ex)
    {
      throw ex;
    }
    finally
    {
      rs.close();
      st.close();
    }
    rs = null;
    st = null;
    //--------
    return rc;
  }
  
  public List<Map<String, Serializable>> query2ListSerializable(
                                              String sqlQuery,
                                              Object[] params,
                                              int max,
                                              Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Serializable>> rc = new ArrayList<Map<String, Serializable>>();
    PreparedStatement st = con.prepareStatement(sqlQuery);
    setParameters(st, params);
    ResultSet rs = st.executeQuery();
    try
    {
      ResultSetMetaData md = rs.getMetaData();
      if (max == -1)
      {
        while (rs.next())
        {
          Map<String, Serializable> rec = resultSet2RecordSerializable(rs, md);
          rc.add(rec);
        }
      }
      else
      {
        while (rs.next())
        {
          Map<String, Serializable> rec = resultSet2RecordSerializable(rs, md);
          rc.add(rec);
          if (rc.size() == max)
          {
            break;
          }
        }
      }
    }
    catch (SQLException ex)
    {
      throw ex;
    }
    finally
    {
      rs.close();
      st.close();
    }
    rs = null;
    st = null;
    //--------
    return rc;
  }

  //------------------------------------------------------------

  public ResultSet query2ResultSet(String sql, Connection con)
    throws SQLException
  {

    ResultSet rs = null;
    try
    {
      Statement st = con.createStatement();
      rs = st.executeQuery(sql);
    }
    catch (SQLException ex)
    {
      throw ex;
    }
    return new ResultSetStatementClose(rs);
  }

  public ResultSet query2ResultSet(String sqlQuery, 
                                   Object[] params,
                                   Connection con)
    throws SQLException
  {
    ResultSet rs = null;
    PreparedStatement st = con.prepareStatement(sqlQuery);
    setParameters(st, params);
    rs = st.executeQuery();
    return new ResultSetStatementClose(rs);
  }

  public synchronized int executeUpdate(String sql, Connection con)
    throws SQLException
  {
    int rc = 0;
    Statement st = con.createStatement();
    rc = st.executeUpdate(sql);
    st.close();
    return rc;
  }

  public synchronized int executeUpdate(String sql, Object[] params,
                                               Connection con)
    throws SQLException
  {
    int rc = 0;
    PreparedStatement st = con.prepareStatement(sql);
    setParameters(st, params);
    rc = st.executeUpdate();
    st.close();
    return rc;
  }
  
  //===================================================================
  public Map<String, Object> resultSet2Record(ResultSet rs, ResultSetMetaData md)
    throws SQLException
  {
    Map<String, Object> rc = new HashMap<String, Object>();
    int count = md.getColumnCount();
    for (int i = 1; i <= count; i++)
    {
      String columnName = md.getColumnName(i); // 1,2,...
      rc.put(columnName, rs.getObject(columnName));
    }
    return rc;
  }
  
  public Map<String, Serializable> resultSet2RecordSerializable(ResultSet rs, ResultSetMetaData md)
    throws SQLException
  {
    Map<String, Serializable> rc = new HashMap<String, Serializable>();
    int count = md.getColumnCount();
    for (int i = 1; i <= count; i++)
    {
      String columnName = md.getColumnName(i); // 1,2,...
      rc.put(columnName, (Serializable) rs.getObject(columnName));
    }
    return rc;
  }
  
//  protected Map<String, Serializable> resultSet2RecordSerializable(ResultSet rs, ResultSetMetaData md)
//    throws SQLException
//  {
//    Map<String, Serializable> rc = new HashMap<String, Serializable>();
//    int count = md.getColumnCount();
//    for (int i = 1; i <= count; i++)
//    {
//      String columnName = md.getColumnName(i); // 1,2,...
//      rc.put(columnName, (Serializable)(rs.getObject(columnName)));
//    }
//    return rc;
//  }
  //--------------------------------------------------------------------------------------

  public List<Map<String, Object>> resultSet2List(ResultSet rs)
    throws SQLException
  {
    List<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    ResultSetMetaData md = rs.getMetaData();
    while (rs.next())
    {
      rc.add(resultSet2Record(rs, md));
    }
    return rc;
  }

  /**
   *  Проверено, работает
   * @param con
   * @param sqlInsert
   * @param sqlParams
   * @param table
   * @return
   * @throws SQLException
   */
  public Map<String, Object> insertAndReturnNewRecord(Connection con,
                                                      String sqlInsert,
                                                      Object[] sqlParams,
                                                      String table // может быть null
                                                      )
    throws SQLException
  {
    CallableStatement cs = null;
    Map<String, Object> rc = null;
    try
    {
      String rowId = null;
      String newSql = "begin " + sqlInsert + " RETURNING rowid into ?;end;";
      cs = con.prepareCall(newSql);
      int indexRet = 1;
      if (sqlParams != null && sqlParams.length > 0)
      {
        indexRet = sqlParams.length + 1;
        for (int i = 0; i < sqlParams.length; i++)
        {
          cs.setObject(i + 1, sqlParams[i]);
        } // for
      }
      cs.registerOutParameter(indexRet, Types.VARCHAR);
      cs.execute();
      rowId = cs.getString(indexRet);

      String sqlSelectNew = "select * from " + table + " where rowid = ?";
      List<Map<String, Object>> dbList =
        query2List(sqlSelectNew, new Object[]
          { rowId }, con);
      rc = dbList.get(0);
      con.commit();
    }
    catch (SQLException ex)
    {
      con.rollback();
      throw ex;
    }
    finally
    {
      if (cs != null)
      {
        cs.close();
      }
    }
    return rc;
  }
  
  public Map<String, Object> insertAndReturnNewRecord(Connection con,
                                                      String sqlInsert,
                                                      Object[] sqlParams,
                                                      String table, // может быть null
                                                      String fieldReturn
                                                      )
    throws SQLException
  {
    CallableStatement cs = null;
    Map<String, Object> rc = null;
    try
    {
      String fieldReturnValue = null;
      String newSql = "begin " + sqlInsert + " RETURNING "+fieldReturn+" into ?;end;";
      cs = con.prepareCall(newSql);
      int indexRet = 1;
      if (sqlParams != null && sqlParams.length > 0)
      {
        indexRet = sqlParams.length + 1;
        for (int i = 0; i < sqlParams.length; i++)
        {
          try {
            Object param = sqlParams[i];
            if (param != null && param instanceof java.util.Date) 
            {
              param = DateTime.java_util_date2java_sql_date((java.util.Date)param);
            }
            cs.setObject(i + 1, param);
          }
          catch(Exception ex) 
          {
            System.err.println("@insertAndReturnNewRecord.error1 "+(i+1)+" ; "+ex.getMessage());
          }
        } // for
      }
      cs.registerOutParameter(indexRet, Types.VARCHAR);
      cs.execute();
      fieldReturnValue = cs.getString(indexRet);

      String sqlSelectNew = "select * from " + table + " where "+fieldReturn+"  = ?";
      List<Map<String, Object>> dbList =
        query2List(sqlSelectNew, new Object[]
          { fieldReturnValue }, con);
      rc = dbList.get(0);
      con.commit();
    }
    catch (SQLException ex)
    {
      con.rollback();
      throw ex;
    }
    finally
    {
      if (cs != null)
      {
        cs.close();
      }
    }
    return rc;
  }
  
  public void setParameters(PreparedStatement st, Object[] params)
    throws SQLException
  {
    Object param = null;
    try
    {
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
            param =
                DateTime.java_util_date2java_sql_date((java.util.Date) param);
          }
          st.setObject(i + 1, param);
        }
      } // for
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

  public void executeUpdateList(Connection con, List<String> list)
    throws SQLException
  {
    for (String sql: list)
    {
      executeUpdate(sql, con);
    }
  }
  //===================================================================

//  public synchronized Connection getConnection(final String dataSourceName)
//    throws NamingException, SQLException
//  {
//    Connection rc = null;
//    //------------------
//    final Context initContext = new InitialContext();
//    final DataSource pool =
//      (DataSource) initContext.lookup(dataSourceName);
//    rc = pool.getConnection();
//    if (rc == null)
//    {
//      //System.err.println("@Connection not received from server ("+dataSourceName+")");
//      throw new RuntimeException("Connection for '" + dataSourceName +
//                                 "' not received!");
//    }
//    //-----------------
//    return rc;
//  }
  //============================================================
  public synchronized int getNextSeq(String seq, Connection con)
  {
    String sql = "SELECT " + seq + ".NEXTVAL val FROM dual";
    int rc = 1;
    Statement st = null;
    ResultSet rs = null;
    try
    {
      st = con.createStatement();
      rs = st.executeQuery(sql);
      while (rs.next())
      {
        rc = rs.getInt("val");
        break;
      }
      rs.close();
      st.close();
      rs = null;
      st = null;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (rs != null)
      {
        try
        {
          rs.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        rs = null;
      }
      if (st != null)
      {
        try
        {
          st.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        st = null;
      }
    }
    return rc;
  }
  //------------------------------------------
  public List<Map<String, Object>> query2List(String sqlQuery,
                                              Map<String, Object> params,
                                              Connection con)
    throws SQLException
  {
    return query2List(sqlQuery, params, -1, con);
  }

  public List<Map<String, Object>> query2List(String sqlQuery, Map<String, Object> params, int max, Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    NamedParameterStatement st = new NamedParameterStatement(con, sqlQuery);
    setParameters(st, params);
    ResultSet rs = st.executeQuery();
    try
    {
      ResultSetMetaData md = rs.getMetaData();
      if (max == -1)
      {
        while (rs.next())
        {
          Map<String, Object> rec = resultSet2Record(rs, md);
          rc.add(rec);
        }
      }
      else
      {
        while (rs.next())
        {
          Map<String, Object> rec = resultSet2Record(rs, md);
          rc.add(rec);
          if (rc.size() == max)
          {
            break;
          }
        }
      }
    }
    catch (SQLException ex)
    {
      throw ex;
    }
    finally
    {
      rs.close();
      st.close();
    }
    rs = null;
    st = null;
    //--------
    return rc;
  }

  public List<Map<String, Serializable>> query2ListSerializable(
            String sqlQuery,
            Map<String, Object> params,
            int max,
            Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Serializable>> rc = new ArrayList<Map<String, Serializable>>();
    NamedParameterStatement st = new NamedParameterStatement(con, sqlQuery);
    setParameters(st, params);
    ResultSet rs = st.executeQuery();
    try
    {
      ResultSetMetaData md = rs.getMetaData();
      if (max == -1)
      {
        while (rs.next())
        {
          Map<String, Serializable> rec =
            resultSet2RecordSerializable(rs, md);
          rc.add(rec);
        }
      }
      else
      {
        while (rs.next())
        {
          Map<String, Serializable> rec =
            resultSet2RecordSerializable(rs, md);
          rc.add(rec);
          if (rc.size() == max)
          {
            break;
          }
        }
      }
    }
    catch (SQLException ex)
    {
      throw ex;
    }
    finally
    {
      rs.close();
      st.close();
    }
    rs = null;
    st = null;
    //--------
    return rc;
  }

  public int executeUpdate(String sql, Map<String, Object> params, Connection con)
    throws SQLException
  {
    int rc = 0;
    NamedParameterStatement st = new NamedParameterStatement(con, sql);
    setParameters(st, params);
    rc = st.executeUpdate();
    st.close();
    return rc;
  }

  public int executeUpdateSerializable(String sql,
                                       Map<String, Serializable> params,
                                       Connection con)
    throws SQLException
  {
    int rc = 0;
    NamedParameterStatement st = new NamedParameterStatement(con, sql);
    setParametersSerializable(st, params);
    rc = st.executeUpdate();
    st.close();
    return rc;
  }

  public void setParameters(NamedParameterStatement st,
                            Map<String, Object> params)
    throws SQLException
  {
    Object param = null;
    try
    {
      Set<String> keys = params.keySet();
      for (String key : keys)
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
            param =
                DateTime.java_util_date2java_sql_date((java.util.Date) param);
          }
          st.setObject(key, param);
        }
      } // for
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

  public void setParametersSerializable(NamedParameterStatement st,
                                        Map<String, Serializable> params)
    throws SQLException
  {
    Object param = null;
    try
    {
      Set<String> keys = params.keySet();
      for (String key : keys)
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
            param =
                DateTime.java_util_date2java_sql_date((java.util.Date) param);
          }
          st.setObject(key, param);
        }
      } // for
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

  public Connection getConnection(final String jndiName)
    throws Exception
  {
    Connection result = null;
    Context initialContext = new InitialContext();
    if (initialContext == null)
    {
      throw new Exception("JNDI problem. Cannot get InitialContext.");
    }
    DataSource datasource = (DataSource) initialContext.lookup(jndiName);
    if (datasource != null)
    {
      result = datasource.getConnection();
    }
    else
    {
      throw new Exception("Failed to lookup datasource = "+jndiName);
    }
    return result;
  }

}
