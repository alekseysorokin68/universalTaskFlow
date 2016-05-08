package com.rcore.model.dynamic_list;


import com.rcore.global.StringFunc;

import java.io.IOException;
import java.io.InputStream;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import oracle.jbo.ApplicationModule;


public class DbDynamicListImpl0 extends DynamicListSignature<Map<String, Object>>
{
  @SuppressWarnings("compatibility")
  private static final long serialVersionUID = 1L;
  private String  sqlCount=null;
  private String  sqlBody=null;
  private int     rangeSize=25;
  private transient ConnectionInterface connectionInterface = null;
  
  private transient Map<Integer, Map<String, Object>> cacheRecords = new HashMap<Integer, Map<String, Object>>();
  private static final int MAX_SIZE = 500000;
  
  private int nSize = 0;
  
  public DbDynamicListImpl0( 
              String[] tableOrView,
              String[] idAttribute,
              String   captionAttribute,
              String[] idParentAttribute,
              String[] idParentValue,
              String   filter,
              int      rangeSize,
              ConnectionInterface connectionInterface,
              String extendedAttributes
          ) 
  {
    super();
    this.sqlCount = getSqlCount(tableOrView,idAttribute,idParentAttribute,idParentValue,filter);
    this.sqlBody  = getSqlBody(tableOrView,idAttribute,captionAttribute,idParentAttribute,idParentValue,filter,extendedAttributes);
    this.rangeSize = rangeSize;
    this.connectionInterface = connectionInterface;
    this.nSize = sizeImpl();
  }

  public DbDynamicListImpl0(
            String  sqlCount,
            String  sqlBody,
            int     rangeSize,
            ConnectionInterface connectionInterface
    )
  {
    super();
    this.sqlCount = sqlCount;
    this.sqlBody  = sqlBody;
    this.rangeSize = rangeSize;
    this.connectionInterface = connectionInterface;
    this.nSize = sizeImpl();
  }
  
  public DbDynamicListImpl0(
            String  sql,
            int     rangeSize,
            ConnectionInterface connectionInterface
    )
  {
    this(getSqlCount(sql),getSqlBody(sql),rangeSize,connectionInterface);
  }
  
  public DbDynamicListImpl0(
            String  sql,
            int     rangeSize,
            ApplicationModule appModule
    )
  {
    super();
    this.sqlCount = getSqlCount(sql);
    this.sqlBody  = getSqlBody(sql);
    this.rangeSize = rangeSize;
    this.connectionInterface = ConnectionFactory.getConnectionInterface(appModule);
    this.nSize = sizeImpl();
  }

  @Override 
  public int size()
  {
    return nSize;
  }
  private int sizeImpl() 
  {
    Connection conn=connectionInterface.getConnection();
    int rc = 0;
    try {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(sqlCount);
      if (rs.next()) 
      {
        rc = rs.getInt(1);
      }
      rs.close();
    }
    catch (Exception e) {
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
    Map<String, Object> rc = null;
    Connection conn = connectionInterface.getConnection();
    try {
      rc = getImpl(i,conn);
    }
    catch (Exception e) {
      e.printStackTrace();
      rc = null;
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
  private Map<String, Object> getImpl(int i, Connection conn) throws Exception
  {
    i++;
    Map<String, Object> mapItem = cacheRecords.get(i);
    if (mapItem != null) 
    {
      return mapItem;
    }
    PreparedStatement ps = conn.prepareStatement(sqlBody);
    int min = Math.max(i-rangeSize, 1);
    int max = Math.min(i+rangeSize, size());
    ps.setInt(1, min);
    ps.setInt(2, max);
    ResultSet rs = null;
    try {
      rs = ps.executeQuery();
    }
    catch(Exception ex) 
    {
      System.err.println("Sql Error(sqlBody) : "+sqlBody);
      System.err.println("param1 = "+min+" ; param2 ="+max);  
      throw ex;
    }
    ResultSetMetaData md = rs.getMetaData();
    if (cacheRecords.size() > MAX_SIZE) {
      cacheRecords.clear();
    }
    int n = min;
    Map<String, Object> rc = null;
    
    while (rs.next()) {
      mapItem = new HashMap<String, Object>();
      int count = md.getColumnCount();
      for (int j = 1; j <= count; j++)
      {
        String columnName = md.getColumnName(j); // 1,2,...
        Object result = rs.getObject(columnName);
        if      (result != null && result instanceof Clob)
        {
          result = DbDynamicListImpl.getClobValue((Clob) result);
        }
        else if (result != null && result instanceof Blob) 
        {
          result = getBlobValue((Blob) result);
        }
        mapItem.put(columnName, result);
        //mapItem.put(columnName, rs.getObject(columnName));
      } // for
      cacheRecords.put(n, mapItem);
//      if (n == i) 
//      {
//        rc = mapItem;
//      }
      n++;
    } // while
    rc = cacheRecords.get(i);
    rs.close();
    ps.close();
    return rc;
  }
  
  private Object getBlobValue(Blob blob)  throws SQLException, IOException
  {
    return DbDynamicListImpl.getBlobValue(blob);
  }
  
  private static String readString(InputStream is, long size) throws IOException
  {
    byte unicodeBytes[] = new byte[(int) size];
    readBytes(is, unicodeBytes);
    return bytesToHexString(unicodeBytes);
  }
  
  private static String bytesToHexString(byte[] bytes) 
  {
      StringBuilder sb = new StringBuilder(bytes.length * 2);

      Formatter formatter = new Formatter(sb);
      for (byte b : bytes) {
          formatter.format("%02x", b);
      }

      return sb.toString().toUpperCase();
  }
  
  private static int readBytes(InputStream is, byte[] bytes) throws IOException
  {
    int left = bytes.length;
    do
    {
      if (left <= 0)
        break;
      int nread = is.read(bytes, bytes.length - left, left);
      if (nread == -1)
        break;
      left -= nread;
    }
    while (true);
    if (bytes.length == left)
      return -1;
    else
      return bytes.length - left;
  }
  
  private String getSqlCount(String[] tableOrView, 
                                    String[] idAttribute,
                                    String[] idParentAttribute,
                                    String[] idParentValue, 
                                    String filter)
  {
    String sql = "SELECT COUNT(1) FROM ${tableOrView} ${where}";
    sql = sql.replace("${tableOrView}", getFromStatement(tableOrView,idParentAttribute,idParentValue));
    sql = sql.replace("${where}", getSqlWhere(tableOrView, idAttribute, idParentAttribute, idParentValue, filter));
    return sql;
  }

  private String getSqlBody(String[] tableOrView, 
                                   String[] idAttribute,
                                   String   captionAttribute, 
                                   String[] idParentAttribute,
                                   String[] idParentValue, 
                                   String filter,
                                   String extendedAttributes
                            )
  {
    String rc = 
      "SELECT * FROM \n"
     +" (SELECT TT.*,ROWNUM ROWN FROM  \n"
     +"    ((SELECT ${TABLE_ORDER_BY}.${ID} ID, ${TABLE_ORDER_BY}.${SNAME} SNAME ${EXTENDED_ATTRIBUTES} FROM ${TABLE} ${WHERE} ORDER BY ${TABLE_ORDER_BY}.${SNAME}) ) TT)" 
     +" WHERE ROWN >= ? AND ROWN <= ?"; 
    if (StringFunc.isEmpty(extendedAttributes)) 
    {
      rc = rc.replace("${EXTENDED_ATTRIBUTES}", "");
    }
    else 
    {
      rc = rc.replace("${EXTENDED_ATTRIBUTES}", ","+extendedAttributes);
    }
    rc = rc.replace("${ID}", idAttribute[0]);
    rc = rc.replace("${SNAME}", captionAttribute);
    rc = rc.replace("${TABLE}", getFromStatement(tableOrView, idParentAttribute, idParentValue));
    rc = rc.replace("${WHERE}", getSqlWhere(tableOrView, idAttribute, idParentAttribute, idParentValue, filter));
    rc = rc.replace("${TABLE_ORDER_BY}", getAlias(tableOrView[0]));
    return rc;
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
      "SELECT * FROM \n"
     +" (SELECT t.*,ROWNUM ROWN FROM  \n"
     +"    (${Sql}) t )" 
     +" WHERE ROWN >= ? AND ROWN <= ?"; 
    rc = rc.replace("${Sql}", sql);
    return rc;
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
    nSize = sizeImpl();
  }
  
//  @Override
//  public void dispose() 
//  {
//     cacheRecords.clear();
//     nSize = 0;
//  }
  //-----------------------------------------------
  public static void main(String[] args)
  {
    String sqlCount = getSqlCount("SELECT * FROM T_HOUSE");
    String sqlBody  = getSqlBody("SELECT * FROM T_HOUSE");
    System.out.println("@@1="+sqlCount);
    System.out.println("@@2="+sqlBody);
  }
}
