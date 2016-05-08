package com.rcore.global.adf;


import com.rcore.global.DateTime;
import com.rcore.global.log.FacesLog;
import com.rcore.global.log.FacesLogImpl;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.DBTransactionImpl;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.internal.OracleResultSet;


public class AdfJdbcUtils extends AdfJdbcUtilsInt
{
  private int timeOut = 300;
  final static boolean IS_TRACE_SQL = false;
  private static AdfJdbcUtils instance = new AdfJdbcUtils();
  private static final FacesLog LOG = new FacesLogImpl(AdfJdbcUtils.class);

  private AdfJdbcUtils()
  {
    super();
  }

  public static AdfJdbcUtils getInstance()
  {
    return instance;
  }

  public ResultSet executeQuery(String sql, Connection con)
    throws SQLException
  {
    ResultSet rc = null;
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }
    Statement st = con.createStatement();
    st.setQueryTimeout(timeOut);
    try
    {
      rc = st.executeQuery(sql);
    }
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = \n" +
          sql, ex);
      throw ex;
    }
//    if (rc instanceof OracleResultSet)
//    {
//      ((OracleResultSet) rc).closeStatementOnClose();
//    }
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis() - traceTime;
      LOG.log("*** Время выполнения запроса = " + traceTime + "\n" +
          sql);
    }
    return rc;
  }

  public ResultSet executeQueryByTransaction(String sql,
                                             DBTransaction tran)
    throws SQLException
  {
    ResultSet rc = null;
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }
    Statement st = tran.createStatement(1);
    st.setQueryTimeout(timeOut);
    try
    {
      rc = st.executeQuery(sql);
    }
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = \n" +
          sql, ex);
      throw ex;
    }
//    if (rc instanceof OracleResultSet)
//    {
//      ((OracleResultSet) rc).closeStatementOnClose();
//    }
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis() - traceTime;
      LOG.log("*** Время выполнения запроса = " + traceTime + "\n" +
          sql);
    }
    return rc;
  }

  public List<DbRecord> executeQuery(String sql, DBTransaction dbTrans)
    throws SQLException
  {
    ResultSet rs = null;
    List<DbRecord> rc = null;
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }
    Statement st = dbTrans.createStatement(1);
    st.setQueryTimeout(timeOut);
    try
    {
      rs = st.executeQuery(sql);
      rc = resultSet2DbRecord(rs);
    }
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = \n" +
          sql, ex);
      throw ex;
    }
    finally
    {
      if (rs != null)
      {
        rs.close();
      }
    }
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis() - traceTime;
      LOG.log("*** Время выполнения запроса и конвертации = " + traceTime +
              "\n" +
          sql);
    }
    return rc;
  }

  public ResultSet executeQuery(String sql, Object[] params,
                                Connection con)
    throws SQLException
  {
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }
    PreparedStatement st = null;
    ResultSet rc = null;
    st = con.prepareStatement(sql);
    st.setQueryTimeout(timeOut);
    setParameters(st, params);
    try
    {
      rc = st.executeQuery();
    }
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = \n" +
          sql + "\n params = " + Arrays.toString(params), ex);
      throw ex;
    }
//    if (rc instanceof OracleResultSet)
//    {
//      ((OracleResultSet) rc).closeStatementOnClose();
//    }
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis() - traceTime;
      LOG.log(" *** Время выполнения запроса = " + traceTime + "\n" +
          sql);
      LOG.log(" *** params = " + Arrays.toString(params));
    }
    return rc;
  }

  public ResultSet executeQueryByTransaction(String sql, Object[] params,
                                             DBTransaction tran,
                                             int preparedRows)
    throws SQLException
  {
    //long traceTime = 0;
    //if (IS_TRACE_SQL)
    //{
    //  traceTime = System.currentTimeMillis();
    //}
    PreparedStatement st = null;
    ResultSet rc = null;
    st = tran.createPreparedStatement(sql, preparedRows);
    //st.setQueryTimeout(timeOut);
    setParameters(st, params);
    try
    {
      rc = st.executeQuery();
    }
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = \n" +
          sql + "\n params = " + Arrays.toString(params), ex);
      throw ex;
    }
//    if (rc instanceof OracleResultSet)
//    {
//      ((OracleResultSet) rc).closeStatementOnClose();
//    }
    /*
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis() - traceTime;
      FacesLog log = FacesLogImpl.getInstance();
      log.log(" *** Время выполнения запроса = " + traceTime + "\n" + sql);
      log.log(" *** params = " + Arrays.toString(params));
    }
    */
    return rc;
  }

  public List<DbRecord> executeQuery(String sql, Object[] params,
                                     DBTransaction dbTrans)
    throws SQLException
  {
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }
    PreparedStatement st = null;
    ResultSet rs = null;
    List<DbRecord> rc = null;
    st = dbTrans.createPreparedStatement(sql, 1);
    st.setQueryTimeout(timeOut);
    setParameters(st, params);
    try
    {
      rs = st.executeQuery();
      rc = resultSet2DbRecord(rs);
    }
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = \n" +
          sql + "\n params = " + Arrays.toString(params), ex);
      throw ex;
    }
    finally
    {
      if (rs != null)
      {
        rs.close();
      }
    }

    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis() - traceTime;
      LOG.log(" *** Время выполнения запроса = " + traceTime + "\n" +
          sql);
      LOG.log(" *** params = " + Arrays.toString(params));
    }
    return rc;
  }

  public void setParameters(PreparedStatement st, Object[] params)
    throws SQLException
  {
    int i = 0;
    Object param = null;
    try
    {
      for (i = 0; i < params.length; i++)
      {
        param = params[i];
        if (param == null)
        {
          st.setNull(i + 1, Types.VARCHAR);
        }
        else if (param instanceof String || param instanceof Integer ||
                 param instanceof Character || param instanceof Byte ||
                 param instanceof Long)
        {
          st.setString(i + 1, param.toString());
        }
        else if (param instanceof Timestamp)
        {
          st.setTimestamp(i + 1, (Timestamp) param);
        }
        else if (param instanceof java.sql.Date)
        {
          st.setDate(i + 1, (java.sql.Date) param);
        }

        else if (param instanceof java.util.Date)
        {
          java.util.Date du = (java.util.Date) param;
          java.sql.Date ds = new java.sql.Date(du.getTime());
          st.setDate(i + 1, ds);
        }


        else if (param instanceof NullDb)
        {
          st.setNull(i + 1, ((NullDb) param).type);
        }
        else
        {
          st.setObject(i + 1, param);
        }
      } // for
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }


  public int executeUpdateWithOutCommit(String sql, Connection con)
    throws SQLException
  {
    int rc = 0;
    Statement st = null;
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }

    try
    {
      st = con.createStatement();
      rc = st.executeUpdate(sql);
    } // try
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = " + sql);
      throw ex;
    } // catch
    finally
    {
      if (IS_TRACE_SQL)
      {
        traceTime = System.currentTimeMillis() - traceTime;
        LOG.log(" *** Время выполнения запроса = " + traceTime + "\n" +
            sql + "\n(return=" + rc + ")");
      }
      if (st != null)
      {
        st.close();
      }
      //if (con != null) {AdfConnectionPool.getInstance().returnConnection(con);}
    } // finally
    return rc;
  }

  public int executeUpdateWithOutCommit(String sql, DBTransaction tran)
    throws SQLException
  {
    int rc = 0;
    Statement st = null;
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }

    try
    {
      st = tran.createStatement(1);
      rc = st.executeUpdate(sql);
    } // try
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = " + sql);
      throw ex;
    } // catch
    finally
    {
      if (IS_TRACE_SQL)
      {
        traceTime = System.currentTimeMillis() - traceTime;
        LOG.log(" *** Время выполнения запроса = " + traceTime + "\n" +
            sql + "\n(return=" + rc + ")");
      }
      if (st != null)
      {
        st.close();
      }
      //if (con != null) {AdfConnectionPool.getInstance().returnConnection(con);}
    } // finally
    return rc;
  }

  public int executeUpdateWithOutCommit(String sql, Object[] params,
                                        Connection con)
    throws SQLException
  {
    int rc = 0;
    PreparedStatement st = null;
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }

    try
    {
      st = con.prepareStatement(sql);
      setParameters(st, params);
      rc = st.executeUpdate();
    } // try
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = " + sql + "\n params = " +
              Arrays.toString(params) + "\n (return = " + rc + ")", ex);
      throw ex;
    } // catch
    finally
    {
      if (IS_TRACE_SQL)
      {
        traceTime = System.currentTimeMillis() - traceTime;
        LOG.log(" *** Время выполнения запроса = " + traceTime + "\n" +
            sql + "\n(return=" + rc + ")");
      }
      if (st != null)
      {
        st.close();
      }
      //if (con != null) {AdfConnectionPool.getInstance().returnConnection(con);}
    } // finally
    return rc;
  }

  public int executeUpdateWithOutCommit(String sql, Object[] params,
                                        DBTransaction tran)
    throws SQLException
  {
    int rc = 0;
    PreparedStatement st = null;
    long traceTime = 0;
    if (IS_TRACE_SQL)
    {
      traceTime = System.currentTimeMillis();
    }

    try
    {
      st = tran.createPreparedStatement(sql, 1);
      setParameters(st, params);
      rc = st.executeUpdate();
    } // try
    catch (SQLException ex)
    {
      LOG.log("Ошибка при выполнении sql = " + sql + "\n params = " +
              Arrays.toString(params) + "\n (return = " + rc + ")", ex);
      throw ex;
    } // catch
    finally
    {
      if (IS_TRACE_SQL)
      {
        traceTime = System.currentTimeMillis() - traceTime;
        LOG.log(" *** Время выполнения запроса = " + traceTime + "\n" +
            sql + "\n(return=" + rc + ")");
      }
      if (st != null)
      {
        st.close();
      }
      //if (con != null) {AdfConnectionPool.getInstance().returnConnection(con);}
    } // finally
    return rc;
  }

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

  public synchronized int getNextSeq(String seq, DBTransactionImpl tran)
  {
    return getNextSeq(seq, tran.getPersistManagerConnection());
  }

  public void setTimeOut(int timeOut)
  {
    this.timeOut = timeOut;
  }

  public static List<DbRecord> resultSet2DbRecord(ResultSet rs)
    throws SQLException
  {
    ResultSetMetaData md = rs.getMetaData();
    List<DbRecord> rc = new ArrayList<DbRecord>();
    int count = md.getColumnCount();
    while (rs.next())
    {
      DbRecord rec = new DbRecord();
      rc.add(rec);
      for (int i = 1; i <= count; i++)
      {
        String columnName = md.getColumnName(i); // 1,2,...
        rec.put(columnName, rs.getString(columnName));
      } // for
    }
    return rc;
  }
  //---------------------------------------------
  public static void setParameters(OraclePreparedStatement st,
                                   Map<String, Object> params)
    throws SQLException
  {
    if (params == null) 
    {
      return;
    }
    try
    {
      Set<String> keys = params.keySet();
      for (String key: keys)
      {
        Object value = params.get(key);
        if (value instanceof NullDb)
        {
          st.setNullAtName(key, ((NullDb) value).getType());
        }
        else
        {
          if (value != null && value instanceof Timestamp) 
          {
            st.setTimestampAtName(key, (Timestamp) value);
          }
          else if (value != null && value instanceof java.util.Date)
          {
            value = DateTime.java_util_date2java_sql_date((java.util.Date) value);
            st.setObjectAtName(key, value);
          }
          else 
          {
            st.setObjectAtName(key, value);  
          }
          
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
  //=====================================================================================
  public List<Map<String, Object>> query2List(String sql, Connection con) throws SQLException
  {
    return query2List(sql, -1, con);
  }

  public List<Map<String, Object>> query2List(String sql, 
                                              int max,
                                              Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    Statement st = con.createStatement();
    if (max > -1) 
    {
      st.setMaxRows(max);
    }
    ResultSet rs = st.executeQuery(sql);
    try
    {
      ResultSetMetaData md = rs.getMetaData();
      while (rs.next())
      {
        Map<String, Object> rec = resultSet2Record(rs, md);
        rc.add(rec);
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
  
  protected Map<String, Serializable> resultSet2RecordSerializable(ResultSet rs, ResultSetMetaData md)
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
  //--------------------------------------------------------------------------------------

  public List<Map<String, Object>> resultSet2List(ResultSet rs) throws SQLException
  {
    List<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    ResultSetMetaData md = rs.getMetaData();
    while (rs.next())
    {
      rc.add(resultSet2Record(rs, md));
    }
    return rc;
  }
  public List<Map<String, Object>> query2List(String sqlQuery,
                                              Map<String, Object> params,
                                              Connection con)
    throws SQLException
  {
    return query2List(sqlQuery, params, -1, con);
  }
  public List<Map<String, Object>> query2List(String sqlQuery,
                                              Map<String, Object> params,
                                              int max,
                                              Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    OraclePreparedStatement st = (OraclePreparedStatement) (con.prepareStatement(sqlQuery));
    if (max > -1) 
    {
      st.setMaxRows(max);
    }
    setParameters(st, params);
    ResultSet rs = st.executeQuery();
    try
    {
      ResultSetMetaData md = rs.getMetaData();
      while (rs.next())
      {
        Map<String, Object> rec = resultSet2Record(rs, md);
        rc.add(rec);
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
}
