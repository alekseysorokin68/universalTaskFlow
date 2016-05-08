package com.rcore.model.dynamic_list;


import com.rcore.global.adf.AdfJdbcUtils;

import java.io.Serializable;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OraclePreparedStatement;


public class DbListSimple extends DynamicListSignature<Map<String, Object>> implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private transient List<Map<String, Object>> cacheRecords = null;
  private String sql;
  private transient Map<String, Object> sqlParams;
  private transient ConnectionInterface connectionInterface;
  private int maxRows = 1000;

  public DbListSimple(
         String  sql,
         Map<String, Object> sqlParams,
         ConnectionInterface connectionInterface,
         int maxRows
    )
  {
    super();
    this.sql = sql;
    this.sqlParams = sqlParams;
    this.connectionInterface = connectionInterface;
    this.maxRows = maxRows;
  }
  
  private void readFromDb() 
  {
    try 
    {
      cacheRecords = readFromDbImpl(sql,sqlParams,connectionInterface,maxRows);
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
      cacheRecords = new ArrayList<Map<String,Object>>();
    }     
  }
  
  private static List<Map<String, Object>> query2List(String sqlQuery,
                                              Map<String, Object> params,
                                              int max,
                                              Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    OraclePreparedStatement st = (OraclePreparedStatement) (con.prepareStatement(sqlQuery));
    
    st.setMaxRows(max);
    st.setFetchSize(Math.min(max,1000)); // NEW
    
    AdfJdbcUtils.setParameters(st, params);
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
    //--------
    return rc;
  }
  
  private static List<Map<String, Object>> readFromDbImpl(
        String sql, 
        Map<String, Object> sqlParams,
        ConnectionInterface connectionInterface,
        int maxRows
        )
    throws SQLException
  {
    List<Map<String, Object>> rc = null;
    Connection con = connectionInterface.getConnection();
    try 
    {
      if (sqlParams == null || sqlParams.size() == 0) 
      {
        rc = query2List(sql, maxRows, con);
      }
      else 
      {
        rc = query2List(sql, sqlParams, maxRows, con);
      }
    }
    finally 
    {
      if (con != null) 
      {
        con.close();
      }
    }
    return rc;
  }
  
  private static List<Map<String, Object>> query2List(String sql, 
                                              int max,
                                              Connection con)
    throws SQLException
  {
    ArrayList<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    Statement st = con.createStatement();
    
    st.setMaxRows(max);
    st.setFetchSize(Math.min(max,1000)); // NEW
    
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
    return rc;
  }
  
  private static Map<String, Object> resultSet2Record(ResultSet rs, ResultSetMetaData md)
    throws SQLException
  {
    int count = md.getColumnCount();
    Map<String, Object> rc = new HashMap<String, Object>(count);
    for (int i = 1; i <= count; i++)
    {
      String columnName = md.getColumnName(i); // 1,2,...
      Object result = rs.getObject(columnName);
      if      (result != null && result instanceof Clob)
      {
        result = DbDynamicListImpl.getClobValue((Clob) result);
      }
      rc.put(columnName, result);
    }
    return rc;
  }
  
  @Override 
  public int size()
  {
    if (cacheRecords == null) readFromDb();
    return cacheRecords.size();
  }
  
  @Override
  public Map<String, Object> get(int i)
  {
    if (cacheRecords == null) readFromDb();
    return cacheRecords.get(i);
  }
  
  @Override
  public void clear()
  {
    if (cacheRecords == null) readFromDb();
    cacheRecords.clear();
  }
  @Override
  public boolean isEmpty()
  {
    if (cacheRecords == null) readFromDb();
    return cacheRecords.isEmpty();
  }
  // -----------------
  public void refresh()
  {
    if (cacheRecords != null) cacheRecords.clear();
    cacheRecords = null;
  }

  public List<Map<String, Object>> getCacheRecords()
  {
    return cacheRecords;
  }
}
