package com.rcore.global.bcomponent.base;


import com.rcore.global.DateTime;

import com.rcore.global.bcomponent.RowByMap;

import java.io.IOException;
import java.io.Serializable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jbo.ApplicationModule;
import oracle.jbo.AttributeDef;
import oracle.jbo.AttributeList;
import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.XMLInterface;
import oracle.jbo.client.Configuration;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.ViewDefImpl;
import oracle.jbo.server.ViewObjectImpl;

import oracle.jdbc.OraclePreparedStatement;

import oracle.xml.parser.v2.XMLDocument;

import org.w3c.dom.Node;


/** Замечания:
 * 1) writeXml:
 * public Document getDeptXml() {
 *   HashMap h = new HashMap();
 *   h.put("nl.amis.empdept.model.DeptView", // Имя view объекта (не класса)
 *   new String[]{"Deptno", // массив аттрибутов
 *                "Dname",
 *                "Loc",
 *                "EmpView"});
 *   h.put("nl.amis.empdept.model.EmpView",
 *   new String[]{"Empno",
 *                "Ename",
 *                "Sal",
 *                "Job",
 *                "Hiredate"});
 *   Node n = writeXML(XMLInterface.XML_OPT_ALL_ROWS,h);
 *   Document d = n.getOwnerDocument();
 *   n = ((XMLDocument)d).adoptNode(n);
 *   d.appendChild(n);
 *   return (Document)d;
 * }
 *
 * 2) writeXml:
 * package test;
 * import oracle.jbo.ApplicationModule;
 * import oracle.jbo.ViewObject;
 * import oracle.jbo.XMLInterface;
 * import oracle.jbo.client.Configuration;
 * import org.w3c.dom.Node;
 * import java.io.PrintWriter;
 * import com.sun.java.util.collections.HashMap;
 * import oracle.xml.parser.v2.XMLNode;
 * public class TestClient  {
 *  private static final String   AMDEF     = "test.TestModule";
 *  private static final String   CONFIG    = "TestModuleLocal";
 *  private static final String   DEPTVODEF = "test.DeptView";
 *  private static final String   EMPVODEF  = "test.EmpView";
 *  private static final String[] DEPTATTRS = {"Deptno","Dname","Employees"};
 *  private static final String[] EMPATTRS  = {"Empno","Ename","Sal"};
 *  public static void main(String[] args) throws Throwable {
 *    ApplicationModule am = Configuration.createRootApplicationModule(AMDEF,CONFIG);
 *    ViewObject vo = am.findViewObject("Departments");
 *    vo.executeQuery();
 *    HashMap attrMap = new HashMap(2);
 *    attrMap.put(DEPTVODEF,DEPTATTRS);
 *    attrMap.put(EMPVODEF,EMPATTRS);
 *    Node n = vo.writeXML(XMLInterface.XML_OPT_ALL_ROWS,attrMap);
 * // Make this PrintWriter wrap a FileWriter to print the
 * // XML out to a file instead
 *    PrintWriter pw = new PrintWriter(System.out);
 *    ((XMLNode)n).print(pw);
 *    Configuration.releaseRootApplicationModule(am,true);
 * }
 * }
 * Running the program against a standard SCOTT schema produces results like the following:
 *
 * <Departments>
 *    <Department>
 *    <Deptno>10</Deptno>
 *    <Dname>ACCOUNTING</Dname>
 *    <Employees>
 *    <Employee>
 *    <Empno>7782</Empno>
 *    <Ename>CLARK</Ename>
 *    <Sal>2450</Sal>
 *    </Employee>
 *    <Employee>
 *    <Empno>7839</Empno>
 *    <Ename>KING</Ename>
 *    <Sal>5000</Sal>
 *    </Employee>
 *    <Employee>
 *    <Empno>7934</Empno>
 *    <Ename>MILLER</Ename>
 *    <Sal>1300</Sal>
 *    </Employee>
 *    </Employees>
 *    </Department>
 * ...
 * </Departments>
 */
public class ViewObjectImplRcore
  extends ViewObjectImpl implements Serializable
{
  @SuppressWarnings("compatibility:8334613384751507098")
  private static final long serialVersionUID = 1L;
  private String initWhereClause = null;
  private String originalQuery = null;
  private transient Map<String, Object> currentMap = null;
  /**
   * Пользовательский объект
   */
  private Serializable userObject = null;
  //------------------------------------------------------
  private transient JdbcReader jdbcReader = null;

  public ViewObjectImplRcore(String string, ViewDefImpl viewDefImpl)
  {
    super(string, viewDefImpl);
  }

  public ViewObjectImplRcore()
  {
    super();
  }
  //------------------------------------------------------

  @Override
  public void setWhereClause(String filter)
  {
    if (filter != null)
    {
      if (initWhereClause != null)
      {
        super.setWhereClause(filter + " AND " + initWhereClause);
      }
      else
      {
        super.setWhereClause(filter);
      }
    }
    else
    {
      super.setWhereClause(initWhereClause);
    }
  }

  @Override
  public void setQuery(String query)
  {
    super.setQuery(query);
  }

  @Override
  public void reset()
  {
    try {
      super.reset();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void setOriginalQuery(String value)
  {
    originalQuery = value;
  }

  public String getOriginalQuery()
  {
    return originalQuery;
  }

  public void restoreOriginalQueryIfExists()
  {
    if (originalQuery != null)
    {
      super.setQuery(originalQuery);
    }
  }

  public boolean resetQuery()
  {
    boolean rc = false;
    if (originalQuery != null)
    {
      super.setQuery(originalQuery);
      rc = true;
    }
    setWhereClause(null);
    setOrderByClause(null);
    return rc;
  }

  public void print()
  {
    System.out.println("*** Print View Object " + getClass().getName() +
                       " *** (begin)");
    Row row = first();
    if (row != null)
    {
      setCurrentRow(row);
      System.out.println(printRow(row));
      while (hasNext())
      {
        row = next();
        System.out.println(printRow(row));
      }
    }
    System.out.println("*** Print View Object " + getClass().getName() +
                       " *** (end)");
  }

  public String printRow(Row row)
  {
    StringBuilder rc = new StringBuilder();
    String[] names = row.getAttributeNames();
    Object[] values = row.getAttributeValues();

    for (int i = 0; i < names.length; i++)
    {
      String name = names[i];
      Object value = values[i];
      if (value != null && value instanceof RowSet)
      {
        continue;
      }
      rc.append(name).append("=").append(value).append("; ");
    }
    return rc.toString();
  }

  public void findAndSetCurrentRowByKey(oracle.jbo.Key key)
  {
    super.findAndSetCurrentRowByKey(key, 0);
  }

  public String buildSelectClause()
  {
    String rc = super.buildSelectClause();
    return rc;
  }

  public String buildFromClause()
  {
    String rc = super.buildFromClause();
    return rc;
  }

  public String buildSelectSqlWithoutWhere()
  {
    StringBuilder rc = new StringBuilder("select ");
    rc.append(buildSelectClause()).append(" from ").append(buildFromClause());
    return rc.toString();
  }

  public boolean buildWhereClause(StringBuffer sqlBuffer, int noUserParams)
  {
    boolean rc = super.buildWhereClause(sqlBuffer, noUserParams);
    return rc;
  }

  public XMLDocument toXMLDocument()
    throws IOException
  {
    String viewName = getViewName();
    AttributeDef[] defs = getAttributeDefs();
    List<String> listAttNames = new ArrayList<String>();
    for (AttributeDef item: defs)
    {
      if (item.getSQLType() != -1)
      {
        listAttNames.add(item.getName());
      }
    }
    HashMap<String, String[]> h = new HashMap<String, String[]>();
    String[] attrList =
      listAttNames.toArray(new String[listAttNames.size()]);
    h.put(viewName, attrList);

    Node n = writeXML(XMLInterface.XML_OPT_ALL_ROWS, h);

    XMLDocument doc = (XMLDocument) n.getOwnerDocument();
    n = doc.adoptNode(n);
    doc.appendChild(n);
    return doc;
  }

  public String getViewName()
  {
    final Class classZ = getClass();
    final String packageName = classZ.getPackage().getName();
    String rc = null;
    if (packageName != null)
    {
      rc = packageName + "." + getDef().getName();
    }
    else
    {
      rc = getDef().getName();
    }
    return rc;
  }

  public void resetWhereClause()
  {
    setWhereClause(null);
    clearCache();
  }

  /**
   * Получить запись по 'глобальному' индексу
   * Проверенно
   * @param rowIndex
   * @param vo
   * @return
   */
//  public static Row getRowByIndex(int rowIndex, ViewObjectImpl vo)
//  {
//    return getRowByIndex((long)rowIndex, vo);
//  }
  public static Row getRowByIndex(int rowIndex, ViewObjectImpl vo)
  {
    boolean execute = false;
    int start = vo.getRangeStart();
    if (rowIndex < start)
    {
      execute = true;
    }
    else if (rowIndex > start)
    {
      int size = vo.getRangeSize();
      if (size > 0 && start + size <= rowIndex)
        execute = true;
    }
    if (execute)
    {
      vo.setRangeStart(rowIndex); // Здесь катастрофа по времени!
      start = vo.getRangeStart();
    }
    int rangeIndex = rowIndex - start;
    Row row = vo.getRowAtRangeIndex(rangeIndex);
    return row;
  }

  public Row getRowByIndex(int rowIndex)
  {
    return getRowByIndex(rowIndex, this);
  }


  /**
   * Некоторая замена методу getRowCount, который работает очень долго (в первый раз)
   * @param vo
   * @return
   */
  public static int getRowCountByQuery(ViewObjectImplRcore vo)
  {
    StringBuilder s = new StringBuilder();
    //s.append("SELECT count(1) FROM (").append(vo.getQuery()).append(")");
    s.append("SELECT count(1) FROM (").append(vo.getUserDefinedQuery()).append(")");
    String sqlCount = s.toString();
    //-----------------------------
    int rc = 0;
    OraclePreparedStatement st = null;
    ResultSet rs = null;
    try
    {
      DBTransaction tran = (DBTransaction) vo.getApplicationModule().getTransaction();
      st = (OraclePreparedStatement) tran.createPreparedStatement(sqlCount,1);
      setupPreparedStatementByViewObject(st, vo);
      rs = st.executeQuery();
      if (rs.next())
      {
        rc = rs.getInt(1);
      }
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      System.err.println("*** Sql Error : '" + sqlCount + "'");
      //e.printStackTrace();
    }
    finally
    {
      try {
        if (rs != null) rs.close();
      }
      catch(Exception e1)
      {
        e1.printStackTrace();
      }
      try {
        if (st != null) st.close();
      }
      catch(Exception e2)
      {
        e2.printStackTrace();
      }
    }
    return rc;
  }

  public int getRowCountByQuery()
  {
    return getRowCountByQuery(this);
  }

  public static void setupPreparedStatementByViewObject(OraclePreparedStatement st,
                                                        ViewObjectImpl vo)
    throws SQLException
  {
    AttributeList list = vo.getNamedWhereClauseParams();
    if (list == null)
    {
      return;
    }
    String[] names = list.getAttributeNames();
    if (names == null || names.length == 0)
    {
      return;
    }
    Object[] values = list.getAttributeValues();
    for (int i = 0; i < names.length; i++)
    {
      String name = names[i];
      Object value = values[i];
      st.setObjectAtName(name, value);
    }
  }
  private static Object convertValueFromOracleJboDomainToJavaSql(Object value)
  {
    if (value != null)
    {
      if (value instanceof oracle.jbo.domain.Number)
      {
        oracle.jbo.domain.Number val = (oracle.jbo.domain.Number)value;
        value = val.getValue();
      }
      if (value instanceof oracle.jbo.domain.Date)
      {
        oracle.jbo.domain.Date val = (oracle.jbo.domain.Date)value;
        java.util.Date utilDate = val.getValue();
        java.sql.Date sqlDate = DateTime.java_util_date2java_sql_date(utilDate);
        value = sqlDate;
      }
    }
    return  value;
  }
  //---------------------------------------------------------------
  private Map<String, String> mapColumnName_Name = null;
  private Map<String, String> mapColumnNameForQuery_Name = null;
  private Map<String, String> mapName_ColumnName = null;
  private String[] columnNames = null;
  private String[] columnNamesForQuery = null;

  public Map<String, String> getMapColumnName_Name()
  {
    if (mapColumnName_Name == null)
    {
      initExtendInfo();
    }
    return mapColumnName_Name;
  }

  public Map<String, String> getMapColumnNameForQuery_Name()
  {
    if (mapColumnNameForQuery_Name == null)
    {
      initExtendInfo();
    }
    return mapColumnNameForQuery_Name;
  }


  public String[] getColumnNames()
  {
    if (columnNames == null)
    {
      initExtendInfo();
    }
    return columnNames;
  }

  public String[] getColumnNamesForQuery()
  {
    if (columnNamesForQuery == null)
    {
      initExtendInfo();
    }
    return columnNamesForQuery;
  }

  public Map<String, String> getMapName_ColumnName()
  {
    if (mapName_ColumnName == null)
    {
      initExtendInfo();
    }
    return mapName_ColumnName;
  }
  //--

  private void initExtendInfo()
  {
    AttributeDef[] attrKeys = getAttributeDefs();
    mapColumnName_Name = new HashMap<String, String>();
    mapColumnNameForQuery_Name = new HashMap<String, String>();
    mapName_ColumnName = new HashMap<String, String>();
    columnNames = new String[attrKeys.length];
    columnNamesForQuery = new String[attrKeys.length];
    //--
    for (int i = 0; i < attrKeys.length; i++)
    {
      AttributeDef def = attrKeys[i];
      mapColumnName_Name.put(def.getColumnName(), def.getName());
      mapColumnNameForQuery_Name.put(def.getColumnNameForQuery(),
                                     def.getName());
      mapName_ColumnName.put(def.getName(), def.getColumnName());
      columnNames[i] = def.getColumnName();
      columnNamesForQuery[i] = def.getColumnNameForQuery();
    } // for
  }


  public void setInitWhereClause(String initWhereClause)
  {
    this.initWhereClause = initWhereClause;
  }

  public String getInitWhereClause()
  {
    return initWhereClause;
  }

  public static Map<String, Object> row2map(Row row) {
    Map<String, Object> rc = new HashMap<String, Object>();
    if (row == null) {
      return rc;
    }
    String[] attrs = row.getAttributeNames();
    for (String attr : attrs) {
      Object value = row.getAttribute(attr);
      rc.put(attr, value);
    }
    return rc;
  }

  public static Row map2row(Map<String, Object> map) {
    Row rc = null;
    if (map == null)
    {
      return rc;
    }
    rc = new RowByMap(map);
    return rc;
  }

  //========================================================

  public static void main(String[] args)
  {
    ApplicationModule mod =
      Configuration.createRootApplicationModule("com.rcore.model.AppModule",
                                                "AppModuleLocal");
    ViewObjectImpl vo = (ViewObjectImpl) mod.findViewObject("ViewObjTest1");
    long t = System.currentTimeMillis();
    Row row = ViewObjectImplRcore.getRowByIndex(39999, vo);
    t = System.currentTimeMillis() - t;
    System.out.println("@t = " + t);

    //    t = System.currentTimeMillis();
    //    row = ViewObjectImplRcore.getRowByIndex(1, vo);
    //    t = System.currentTimeMillis() - t;
    //    System.out.println("@t = "+t);
    //
    //    t = System.currentTimeMillis();
    //    row = ViewObjectImplRcore.getRowByIndex(3999, vo);
    //    t = System.currentTimeMillis() - t;
    //    System.out.println("@t = "+t);

    System.out.println(row);
    //---------------------------------------------------
    Configuration.releaseRootApplicationModule(mod, true);
  }
  //-------------------------------------
  //  private Row currentRow = null;
  //  @Override
  //  public boolean setCurrentRow(Row row) {
  //    boolean rc = false;
  //    rc = super.setCurrentRow(row);
  //    return rc;
  //  }
  //  @Override
  //  public Row getCurrentRow() {
  //    return super.getCurrentRow();
  //  }

//  @Override
//  public Class getRowClass()
//  {
//    return ViewRowImplRcore.class;
//  }

  public void setCurrentMap(Map<String, Object> currentMap)
  {
    this.currentMap = currentMap;
  }

  public Map<String, Object> getCurrentMap()
  {
    return currentMap;
  }
  @Override
  public String getQuery()
  {
//    if(mSelReqBindVarStateMap == null)
//        resetAffectedVCs(true);
//    return buildQuery(getDefaultRowSet().getWhereClauseParams().length, false);

    String rc = null;
    try
    {
      rc = super.getQuery();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      //rc = buildQuery(getDefaultRowSet().getWhereClauseParams().length, false);
      rc = mUserDefinedQuery;
    }
    return rc;
  }

  public String getUserDefinedQuery()
  {
    if (mUserDefinedQuery != null) {
      return mUserDefinedQuery;
    }
    else
    {
      return getQuery();
    }
  }

  public String buildQuery()
  {
    return buildQuery(getDefaultRowSet().getWhereClauseParams().length, false);
  }
  public String buildQueryForRowCount()
  {
    return buildQuery(getDefaultRowSet().getWhereClauseParams().length, true);
  }
  @Override
  public Row getCurrentRow()
  {
    Row rc = null;
    try {
      rc = super.getCurrentRow();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    return rc;
  }

  //-----------------------------------------------
  @Override
  public void remove() {
    refreshJdbcReader();
    super.remove();
  }

  protected void finalize () throws Throwable
  {
    refreshJdbcReader();
    super.finalize();
  }
//  private void destroyMapJdbcReadersByQuery()
//  {
//    try {
//      if (mapJdbcReadersByQuery != null)
//      {
//          mapJdbcReadersByQuery.destroy();
//      }
//    }
//    catch(Exception ex)
//    {
//      ex.printStackTrace();
//    }
//  }
  //-----------------------------------------------
  public Map<String, Object> getRecordByJdbc(int i,
                                             int allRecords,
                                             boolean fieldNameAsAttributeName)
  {
    if (jdbcReader == null)
    {
      jdbcReader = new JdbcReader();
    }
    return jdbcReader.get(i, allRecords, fieldNameAsAttributeName);
  }

  public void refreshJdbcReader()
  {
    if (jdbcReader != null)
    {
      jdbcReader.refresh();
      jdbcReader = null;
    }
  }

  public void setUserObject(Serializable userObject)
  {
    this.userObject = userObject;
  }

  public Serializable getUserObject()
  {
    return userObject;
  }

  private class JdbcReader //implements Serializable
  {
    String currentSql = null;
    Map<Integer, Map<String, Object>> cacheRecords = new HashMap<Integer, Map<String, Object>>();
    OraclePreparedStatement ps = null;

    JdbcReader()
    {
      super();
      currentSql = getUserDefinedQuery();
      try
      {
        DBTransaction tran = (DBTransaction)getApplicationModule().getTransaction();
        ps = (OraclePreparedStatement) tran.createPreparedStatement(getSqlRange(currentSql), getFetchSize());
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
    }

    private Map<String, Object> get(int index,
                                    int allRecords,
                                    boolean fieldNameAsAttributeName
                                )
    {
      Map<String, Object> rc = null;
      try {
        index++;
        String sqlNew = getUserDefinedQuery();
        String sqlRange = getSqlRange(sqlNew);
        if ((!sqlNew.equals(currentSql)) || (ps == null))
        {
          // Сброс кэша
          refresh();
          DBTransaction tran = (DBTransaction)getApplicationModule().getTransaction();
          ps = (OraclePreparedStatement) tran.createPreparedStatement(sqlRange, getFetchSize());
          currentSql = sqlNew;
        }

        Map<String, Object> mapItem = cacheRecords.get(index);
        if (mapItem != null)
        {
          return mapItem;
        }
        ViewObjectImplRcore.setupPreparedStatementByViewObject(ps,ViewObjectImplRcore.this);
        int rangeSize = getFetchSize();
        int min = Math.max(index-rangeSize, 1);
        int max = Math.min(index+rangeSize, allRecords);
        ps.setIntAtName("row_min", min); //ps.setInt(1, min);
        ps.setIntAtName("row_max", max); //ps.setInt(2, max);
        ResultSet rs = null;
        try {
          rs = ps.executeQuery();
        }
        catch(Exception ex)
        {
          System.err.println(ex.getMessage());
          System.err.println("Sql Error(sqlBody) : "+sqlRange);
          System.err.println("param1 = "+min+" ; param2 ="+max);
          //ex.printStackTrace();
          return rc;
          //throw ex;
        }
        ResultSetMetaData md = rs.getMetaData();
        int size = getFetchSize();
        if (cacheRecords.size() > size) {
          cacheRecords.clear();
        }
        int n = min;

        while (rs.next()) {
          mapItem = new HashMap<String, Object>();
          int count = md.getColumnCount();
          for (int j = 1; j <= count; j++)
          {
            String columnName = md.getColumnName(j); // 1,2,...
            String key = columnName;
            // new
            if (fieldNameAsAttributeName)
            {
              key = ViewObjectImplRcore.this.getMapColumnName_Name().get(columnName);
            }

            mapItem.put(key, rs.getObject(columnName));
          } // for
          cacheRecords.put(n, mapItem);
//          if (n == index)
//          {
//            rc = mapItem;
//          }
          n++;
        } // while
        rc = cacheRecords.get(index);
        rs.close();
      }
      catch(SQLException ex)
      {
        ex.printStackTrace();
      }
      return rc;
    }
    private String getSqlRange(String sql)
    {
      StringBuilder rc = new StringBuilder("SELECT * FROM \n");
      rc.append(" (SELECT t.*,ROWNUM ROWN FROM  \n(").
      append(sql).append(") t )").
      append(" WHERE ROWN >= :row_min AND ROWN <= :row_max");
      String sRc = rc.toString();
      return sRc;
    }

    private void refresh()
    {
      if (cacheRecords != null) {
        cacheRecords.clear();
      }
      if (ps != null)
      {
        try
        {
          ps.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        ps = null;
      }
    }
  }
}
