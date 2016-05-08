package com.rcore.model.dynamic_list;


import com.rcore.global.StringFunc;
import com.rcore.global.adf.AdfJdbcUtils;

import java.io.IOException;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import oracle.jbo.ApplicationModule;

import oracle.jdbc.OraclePreparedStatement;


public class DbDynamicListImpl extends DynamicListSignature<Map<String, Object>>
{
  private static final long serialVersionUID = 1L;
  private String sqlCount = null;
  private String sqlBody = null;
  private transient Map<String, Object> sqlParams = null;
  private int rangeSize = 25;
  private transient ConnectionInterface connectionInterface = null;
  private boolean isInited = false;
  
  private transient Map<Integer, Map<String, Object>> cacheRecords = null;  //new HashMap<Integer, Map<String, Object>>();
  
  private int nSize = 0;
  
  public DbDynamicListImpl(
            String  sqlCount,
            String  sqlBody,
            Map<String, Object> sqlParams,
            int     rangeSize,
            ConnectionInterface connectionInterface
    )
  {
    super();
    cacheRecords = new HashMap<Integer, Map<String, Object>>(rangeSize);
    this.sqlCount = sqlCount;
    this.sqlBody  = sqlBody;
    this.sqlParams = sqlParams;
    this.rangeSize = rangeSize;
    this.connectionInterface = connectionInterface;
  }
  
  public DbDynamicListImpl(
            String  sql,
            Map<String, Object> sqlParams,
            int     rangeSize,
            ConnectionInterface connectionInterface
    )
  {
    this(getSqlCount(sql),
         getSqlBody(sql),
         sqlParams,
         rangeSize,
         connectionInterface);
  }
  
  public DbDynamicListImpl(
            String  sql,
            Map<String, Object> sqlParams,
            int     rangeSize,
            ApplicationModule appModule
    )
  {
    super();
    cacheRecords = new HashMap<Integer, Map<String, Object>>(rangeSize);
    this.sqlCount = getSqlCount(sql);
    this.sqlBody  = getSqlBody(sql);
    this.sqlParams = sqlParams;
    this.rangeSize = rangeSize;
    this.connectionInterface = ConnectionFactory.getConnectionInterface(appModule);
  }
  //=======================

  @Override 
  public int size()
  {
    init();
    return nSize;
  }
  
  private int sizeImpl() 
  {
    int rc = 0;
    if (cacheRecords.size() < rangeSize) 
    {
      rc = cacheRecords.size();  // Весь запрос вместился в кэш
    }
    else 
    {
      rc = dbSizeImpl();
    }
    return rc;
  }
  
  private int dbSizeImpl() 
  {
    //System.out.println("@dbSizeImpl");
    //------------------------------------
    Connection conn = connectionInterface.getConnection();
    int rc = 0;
    try 
    {
      OraclePreparedStatement st = (OraclePreparedStatement) conn.prepareStatement(sqlCount);
      AdfJdbcUtils.setParameters(st, sqlParams);
      
      st.setFetchSize(1); // NEW
      ResultSet rs = st.executeQuery();
      
      if (rs.next()) 
      {
        rc = rs.getInt(1);
      }
      rs.close();
      st.close();
    }
    catch (Exception e) 
    {
      System.err.println("*** Sql Error : "+sqlCount );
      e.printStackTrace();
    }
    finally 
    {
      if (conn != null) 
      {
        try {
          connectionInterface.closeConnection(conn);
        }
        catch(Exception ex1) 
        {
          ex1.printStackTrace();
        }
      }
    }
    return rc;
  }

  @Override
  public Map<String, Object> get(int i)
  {
    init();
    Map<String, Object> rc = null;
    try 
    {
      rc = getImpl(i);
    }
    catch (Exception e) 
    {
      e.printStackTrace();
      rc = null;
    }
    return rc;
  }
  
  private Map<String, Object> getImpl(int i)  throws Exception
  {
    i++;
    Map<String, Object> mapItem = cacheRecords.get(i);
    if (mapItem != null)
    {
      return mapItem;
    }
    Connection conn = connectionInterface.getConnection();
    OraclePreparedStatement ps = (OraclePreparedStatement) (conn.prepareStatement(sqlBody));
    ResultSet rs = null;
    Map<String, Object> newParams = new HashMap<String, Object>();
    try
    {
      if (sqlParams != null)
      {
        newParams.putAll(sqlParams);
      }
      int min = Math.max(i - rangeSize, 1);
      int max = Math.min(i + rangeSize, size());
      newParams.put("row_min", min);
      newParams.put("row_max", max);
      AdfJdbcUtils.setParameters(ps, newParams);
      
      ps.setFetchSize(Math.min(rangeSize,1000)); // NEW
      rs = ps.executeQuery();
      
      ResultSetMetaData md = rs.getMetaData();
      if (cacheRecords.size() > rangeSize)
      {
        cacheRecords.clear();
      }
      int n = min;
      
      while (rs.next())
      {
        mapItem = new HashMap<String, Object>(md.getColumnCount());
        int count = md.getColumnCount();
        for (int j = 1; j <= count; j++)
        {
          String columnName = md.getColumnName(j); // 1,2,...
          Object result = rs.getObject(columnName);
          if      (result != null && result instanceof Clob)
          {
            result = getClobValue((Clob) result);
          }
//          else if (result != null && result instanceof Blob) 
//          {
//            result = getBlobValue((Blob) result);
//          }
          mapItem.put(columnName, result);
          //mapItem.put(columnName, rs.getObject(columnName));
        } // for
        cacheRecords.put(n, mapItem);
        n++;
      } // while
      mapItem = cacheRecords.get(i);
    }
    catch (Exception ex)
    {
      System.err.println("Sql Error(sqlBody) : " + sqlBody);
      System.err.println("params = " + newParams);
      throw ex;
    }
    finally {
      if (conn != null) 
      {
        try 
        {
          if (rs != null) 
          {
            rs.close();
          }
          if (ps != null) 
          {
            ps.close();
          }
        }
        catch(Exception ex2) {ex2.printStackTrace();}
        
        connectionInterface.closeConnection(conn);
      }
    }
    return mapItem;
  }
  
  @Override
  public void clear()
  {
    init();
    cacheRecords.clear();
  }
  @Override
  public boolean isEmpty()
  {
    init();
    return cacheRecords.isEmpty();
  }
  
  //--------------------------------------
  private void init()  // throws Exception
  {
    if (isInited) return;
    Connection conn = null;
    OraclePreparedStatement ps = null;
    ResultSet rs = null;
    Map<String, Object> newParams = new HashMap<String, Object>();
    try
    {
      conn = connectionInterface.getConnection();
      ps = (OraclePreparedStatement) (conn.prepareStatement(sqlBody));
      if (sqlParams != null)
      {
        newParams.putAll(sqlParams);
      }
      int min = 1;
      int max = rangeSize;
      newParams.put("row_min", min);
      newParams.put("row_max", max);
      AdfJdbcUtils.setParameters(ps, newParams);
      
      ps.setFetchSize(Math.min(rangeSize,1000)); // NEW
      rs = ps.executeQuery();
      
      ResultSetMetaData md = rs.getMetaData();
      int n = min;
      
      while (rs.next())
      {
        Map<String, Object> mapItem = new HashMap<String, Object>(md.getColumnCount());
        int count = md.getColumnCount();
        for (int j = 1; j <= count; j++)
        {
          String columnName = md.getColumnName(j); // 1,2,...
          Object result = rs.getObject(columnName);
          if      (result != null && result instanceof Clob)
          {
            result = getClobValue((Clob) result);
          }
          else if (result != null && result instanceof Blob) 
          {
            result = getBlobValue((Blob) result);
          }
          mapItem.put(columnName, result);
        } // for
        cacheRecords.put(n, mapItem);
        n++;
      } // while
      //=============
      nSize = sizeImpl();
      isInited = true;
    }
    catch (Exception ex)
    {
      System.err.println("Sql Error(sqlBody) 2 : " + sqlBody);
      System.err.println("params 2 = " + newParams);
      //throw ex;
      ex.printStackTrace();
    }
    finally 
    {
      if (conn != null) 
      {
        if (rs != null) 
        {
          try {rs.close();} catch(Exception e1) {e1.printStackTrace();}
        }
        if (ps != null) 
        {
          try {ps.close();} catch(Exception e1) {e1.printStackTrace();}
        }
        try {connectionInterface.closeConnection(conn);} catch(Exception e1) {e1.printStackTrace();}
      }
    }
    return;
  }
  
  
  
  public static Object getBlobValue(Blob blob)  throws SQLException, IOException
  {
    return blob;
  }
  
//  private static int readBytes(InputStream is, byte[] bytes) throws IOException
//  {
//    int left = bytes.length;
//    do
//    {
//      if (left <= 0)
//        break;
//      int nread = is.read(bytes, bytes.length - left, left);
//      if (nread == -1)
//        break;
//      left -= nread;
//    }
//    while (true);
//    if (bytes.length == left)
//      return -1;
//    else
//      return bytes.length - left;
//  }
  
  public static String getClobValue(Clob clob) throws SQLException
  {
    String rc = null;
    if (clob != null) 
    {
      int len = (int) clob.length();
      if (len > 0) 
      {
        rc = clob.getSubString(1, len);
      }
    }
    return rc;
  }

  private String getSqlCount(String[] tableOrView, 
                                    String[] idAttribute,
                                    String[] idParentAttribute,
                                    String[] idParentValue, 
                                    String filter)
  {
    StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM ${tableOrView} ${where}");
    String sSql = sql.toString().replace("${tableOrView}", getFromStatement(tableOrView,idParentAttribute,idParentValue));
    sSql = sSql.replace("${where}", getSqlWhere(tableOrView, idAttribute, idParentAttribute, idParentValue, filter));
    return sSql;
  }

  private String getFilterByParents(
        String[] sqlTableOrView,
        String[] idAttribute,
        String[] idParentAttribute,
        String[] idParentValue) 
  {
    StringBuffer rc = new StringBuffer();
    final String AND = " AND ";
    int len = Math.min(sqlTableOrView.length, idParentAttribute.length);
    len = Math.min(len, idParentValue.length);
    for(int i =0; i < len; i++) 
    {
      String sqlTableOrViewItem       = sqlTableOrView[i];
      String idParentAttributeItem = idParentAttribute[i];
      String idParentValueItem     = idParentValue[i];
      if (StringFunc.isEmpty(sqlTableOrViewItem))        sqlTableOrViewItem = null;
      if (StringFunc.isEmpty(idParentAttributeItem))  idParentAttributeItem = null;
      if (StringFunc.isEmpty(idParentValueItem))      idParentValueItem = null;
      //---------------------------------------------------------------------------
      if (sqlTableOrViewItem != null)
      {
        int nAppend = 0;
        if (idParentValueItem != null) {
          if (idParentAttributeItem != null) {
            rc.append(" ").append(getAlias(sqlTableOrViewItem)).append(".").append(idParentAttributeItem).append(" = '").
              append(idParentValueItem).append("'");
            nAppend++;
          }
        }
        if (i > 0 && sqlTableOrView[i-1] != null && idParentAttribute[i-1] != null) {
          if (nAppend > 0) 
          {
            rc.append(AND);  
          }
          String parentTable = sqlTableOrView[i-1];
          String idAttributeParentTable = idParentAttribute[i-1]; // ��� ���������!
          String child = sqlTableOrViewItem;
          String idAttributeChild = idAttribute[i]; //��� ���������! //idParentAttribute[i-1]; 
          rc.append(getAlias(child)).append(".").
              append(idAttributeChild). // append(idParentAttributeItem)
              append(" = ").
              append(getAlias(parentTable)).append(".").append(idAttributeParentTable);
          nAppend++;
        }
        //rc.append(")");
        if (nAppend > 0) {
          rc.append(AND);
        }
      }
    } // for
    if (rc.length() > 0) 
    {
      rc.setLength(rc.length() - AND.length());
    }
    return rc.toString();
  }
  
  private String getFromStatement(String[] sqlOrTableOrView,
                                         String[] idParentAttribute,
                                         String[] idParentValue)
  {
    StringBuffer rc = new StringBuffer();
    boolean isSql0 = isSqlQuery(sqlOrTableOrView[0]);
    if (sqlOrTableOrView.length > 0 && !StringFunc.isEmpty(sqlOrTableOrView[0])) 
    {
      if (!isSql0) {
        rc.append(sqlOrTableOrView[0]).append(",");
      }
      else 
      {
        rc.append("(").append(sqlOrTableOrView[0]).append(") ").append(getAlias(sqlOrTableOrView[0])).append(",");
      }
    }
    int len = Math.min(sqlOrTableOrView.length, idParentAttribute.length);
    len = Math.min(len, idParentValue.length);
    for(int i = 1; i < len; i++) 
    {
      String sqlTableOrViewItem       = sqlOrTableOrView[i];
      String idParentAttributeItem = idParentAttribute[i];
      String idParentValueItem     = idParentValue[i];
      if (StringFunc.isEmpty(sqlTableOrViewItem)) sqlTableOrViewItem = null;
      if (StringFunc.isEmpty(idParentAttributeItem)) idParentAttributeItem = null;
      if (StringFunc.isEmpty(idParentValueItem)) idParentValueItem = null;
      boolean isSqlItem = isSqlQuery(sqlTableOrViewItem);
      if (sqlTableOrViewItem != null)
      {
        if (!isSqlItem) {
          rc.append(sqlTableOrViewItem).append(",");
        }
        else 
        {
          rc.append("(").append(sqlTableOrViewItem).append(") ").append(getAlias(sqlTableOrViewItem)).append(",");
        }
      }
    } // for
    if (rc.length() > 0) 
    {
      rc.setLength(rc.length() - 1);
    }
    return rc.toString();
  }
  
  private String getSqlWhere( String[] sqlTableOrView,
                              String[] idAttribute,
                              String[] idParentAttribute,
                              String[] idParentValue, 
                              String   filter) 
  {
    StringBuffer rc = new StringBuffer();
    //---- filter by parents
    rc.append(getFilterByParents(sqlTableOrView,idAttribute,idParentAttribute,idParentValue));
    //--- filter
    if (filter != null) 
    {
      if (rc.length() > 0) 
      {
        rc.append(" AND ");
      }
      rc.append(filter);
    }
    if (rc.length() > 0) 
    {
      rc.insert(0, " WHERE ");
    }
    return rc.toString();
  }
  //-----------------------------------------------
  private static String getSqlCount(String sql)
  {
    StringBuffer rc = new StringBuffer();
    rc.append("SELECT count(1) FROM (").append(sql).append(")");
    return rc.toString();
  }
  private static String getSqlBody(String sql)
  {
    String rc = 
    " SELECT * FROM (  \n" + 
    "   SELECT t.*, ROWNUM ROWN FROM (${sql}) t \n" + 
    "          WHERE ROWNUM <= :row_max  \n" + 
    "               ) WHERE ROWN >= :row_min";
    
    rc = rc.replace("${sql}", sql);  
    return rc;
    
//    StringBuilder rc = new StringBuilder(
//             "SELECT * FROM \n")
//     .append(" (SELECT t.*,ROWNUM ROWN FROM  \n")
//     .append("    (${sql}) t ) \n")
//     .append(" WHERE ROWN >= :row_min AND ROWN <= :row_max"); 
//    String sRc = rc.toString().replace("${sql}", sql);
//    return sRc;
  }
  //-----------------------------------------------
  private static boolean isSqlQuery(String sqlOrTableOrView) 
  {
    if (StringFunc.isEmpty(sqlOrTableOrView)) 
    {
      return false;
    }
    sqlOrTableOrView = sqlOrTableOrView.trim().toUpperCase();
    boolean rc = sqlOrTableOrView.startsWith("SELECT ");
    return rc;
  }
  private long aliasCount = 0;
  private Map<String,String> aliases = new HashMap<String,String>();
  private static final String ALIAS_PREFIX = "ALIAS_";
  private String getAlias(String sqlOrTableOrView) 
  {
    if (!isSqlQuery(sqlOrTableOrView)) 
    {
      return sqlOrTableOrView;
    }
    sqlOrTableOrView = sqlOrTableOrView.trim().toUpperCase();
    String alias = aliases.get(sqlOrTableOrView);
    if (alias == null) 
    {
      alias = ALIAS_PREFIX+(++aliasCount);
      aliases.put(sqlOrTableOrView, alias);
    }
    return alias;
  }
  
  public void refresh() 
  {
    cacheRecords.clear();
    nSize = 0;
    isInited = false;
  }
  
//  @Override
//  public void dispose()
//  {
//    cacheRecords.clear();
//    nSize = -1;
//  }
  //-----------------------------------------------
  //* Test
  //-----------------------------------------------
  public static void main(String[] args)
    throws Exception
  {
    // test1();
    //test2();
    //test3();
  }
//  static void test1() 
//  {
//    String sqlCount = getSqlCount("SELECT * FROM T_HOUSE");
//    String sqlBody  = getSqlBody("SELECT * FROM T_HOUSE");
//    System.out.println("@@1="+sqlCount);
//    System.out.println("@@2="+sqlBody);
//  }
//
//  private static void test2()
//    throws Exception
//  {
//    String sqlBody = 
//      "SELECT * FROM \n" + 
//      " (SELECT t.*,ROWNUM ROWN FROM  \n" + 
//      "    (select ID ID, VALUE VALUE from TEST where rownum < 3 order by ID) t ) WHERE ROWN >= :row_min AND ROWN <= :row_max";
//    Connection con = getConnection();
//    ConnectionInterface connectionInterface = ConnectionFactory.getConnectionInterface(con);
//    //-----------------
//    DbDynamicListImpl obj = new DbDynamicListImpl();
//    obj.sqlBody = sqlBody;
//    obj.connectionInterface = connectionInterface;
//    obj.nSize = 2;
//    System.out.println("@result1="+obj.getImpl(0, con));
//    System.out.println("@result2="+obj.getImpl(1, con));
//  }
//
//  static Connection getConnection() throws SQLException
//  {
//    String username = "cb_test";
//    String password = "cb_test";
//    String thinConn = "jdbc:oracle:thin:@windows-xp-32-2:1521:ORCL";
//    DriverManager.registerDriver(new OracleDriver());
//    Connection conn = DriverManager.getConnection(thinConn, username, password);
//    conn.setAutoCommit(false);
//    return conn;
//  }
//
//  private static void test3()  throws Exception
//  {
//    String sql = "SELECT * FROM\n" + 
//    " (SELECT t.*,ROWNUM ROWN FROM  \n" + 
//    "    (select ID ID, VALUE VALUE from TEST where rownum < 3 order by ID) t ) \n" + 
//    " WHERE ROWN >= :row_min AND ROWN <= :row_max";
//    Connection con = getConnection();
//    //------------------
//    OraclePreparedStatement ps = (OraclePreparedStatement)(con.prepareStatement(sql));
//    Map<String,Object> newParams = new HashMap<String,Object>();
//    int min = 1;
//    int max = 2;
//    newParams.put("row_min",min);
//    newParams.put("row_max",max);
//    
//    AdfJdbcUtils.setParameters(ps, newParams);
//      
//    ResultSet rs = null;
//    rs = ps.executeQuery();
//    
//    ResultSetMetaData md = rs.getMetaData();
//    while (rs.next()) {
//      int count = md.getColumnCount();
//      for (int j = 1; j <= count; j++)
//      {
//        String columnName = md.getColumnName(j); // 1,2,...
//        System.out.println("@@ "+columnName+" = "+rs.getObject(columnName));
//      } // for
//    } // while
//    rs.close();
//    ps.close();
//    con.close();
//  }
}
