package com.rcore.utils.table;


import com.rcore.global.DateTime;
import com.rcore.global.StringFunc;
import com.rcore.model.dynamic_list.ConnectionInterface;
import com.rcore.model.dynamic_list.DbDynamicListImpl;
import com.rcore.model.dynamic_list.DynamicListSignature;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.layout.RichToolbar;
import oracle.adf.view.rich.component.rich.nav.RichTrain;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;
import oracle.adf.view.rich.model.QueryDescriptor;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.myfaces.trinidad.component.UIXComponent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.event.SortEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.SortCriterion;


/**
 * @see TableModelesIntegrator
 */
public class TableModelesIntegratorImpl implements TableModelesIntegrator, Serializable
{
  private static final long serialVersionUID = 1L;

  protected ConnectionInterface connectionInterface = null;
  protected String sql = null;
  protected transient Map<String, Object> sqlParams = null;
  private List<String> filterAbleFields = null;
  private int rangeSize = 25;
  //---
  private CollectionModel2 dataModel = null;
  protected MyFilterableQueryDescriptor filterModel = null;
  protected List<SortCriterion> sortModel = null;
  private transient Object selectedRowData = null;
  
  protected Map<String,Integer> sqlTypesFileds = new HashMap<String,Integer>();
  private transient RichTable table = null;


  public TableModelesIntegratorImpl(ConnectionInterface connectionInterface,
                                    String sql,
                                    Map<String, Object> sqlParams, // new
                                    List<String> filterAbleFields, 
                                    int rangeSize)
  {
    this(connectionInterface,sql,sqlParams,filterAbleFields,rangeSize,null,null);
  }

  public TableModelesIntegratorImpl(ConnectionInterface connectionInterface,
                                    String sql,
                                    Map<String, Object> sqlParams, // new
                                    List<String> filterAbleFields, 
                                    int rangeSize,
                                    Map<String,Integer> sqlTypesFileds
                                   )
  {
    this(connectionInterface,sql,sqlParams,filterAbleFields,rangeSize,sqlTypesFileds,null);
  }

  public TableModelesIntegratorImpl(ConnectionInterface connectionInterface,
                                    String sql,
                                    Map<String, Object> sqlParams, // new
                                    List<String> filterAbleFields, 
                                    int rangeSize,
                                    Map<String,Integer> sqlTypesFileds,
                                    RichTable table
                                   )
  {
    super();
    this.connectionInterface = connectionInterface;
    this.sql = sql;
    this.sqlParams = sqlParams;
    this.filterAbleFields = filterAbleFields;
    this.rangeSize = rangeSize;
    this.table = table;
    createNewDataModel(null);
    //-----------------------
    try
    {
      if (sqlTypesFileds != null && sqlTypesFileds.size() > 0) {
        this.sqlTypesFileds = sqlTypesFileds;        
      }
      else 
      {
        setSqlTypesFields();        
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public TableModelesIntegratorImpl(ConnectionInterface connectionInterface,
                                    String sql,
                                    Map<String, Object> sqlParams, // new
                                    int rangeSize,
                                    Map<String,Integer> sqlTypesFileds,
                                    RichTable table
                                   )
  {
    super();
    this.connectionInterface = connectionInterface;
    this.sql = sql;
    this.sqlParams = sqlParams;
    this.filterAbleFields = getFilterAbleFields(table);
    this.rangeSize = rangeSize;
    this.table = table;
    createNewDataModel(null);
    //-----------------------
    try
    {
      if (sqlTypesFileds != null && sqlTypesFileds.size() > 0) {
        this.sqlTypesFileds = sqlTypesFileds;        
      }
      else 
      {
        setSqlTypesFields();        
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  //===========================================================================
  
  private void setSqlTypesFields() throws Exception 
  {
    Connection con = connectionInterface.getConnection();
    OraclePreparedStatement ps = null;
    ResultSet rs = null;
    try 
    {
      StringBuilder newSql = new StringBuilder();
      newSql.append("select * from (").append(sql).append(") where rownum < 1");
      ps = (OraclePreparedStatement)(con.prepareStatement(sql.toString()));
      if (sqlParams != null) 
      {
        for (String key : sqlParams.keySet()) 
        {
          ps.setObjectAtName(key, null);
        }
      }
      rs = ps.executeQuery();
      ResultSetMetaData meta = rs.getMetaData();
      int columnCount = meta.getColumnCount();
      for (int i=1; i<= columnCount; i++) 
      {
        int colType = meta.getColumnType(i);
        sqlTypesFileds.put(meta.getColumnName(i), colType);
      }
    }
    finally 
    {
        if (con != null) 
        {
          con.close();
        }
        if (ps != null) 
        {
          ps.close();
        }
        if (rs != null) 
        {
          rs.close();
        }
    }
  }

  private static List<String> getFilterAbleFields(UIXComponent parent)
  {
    List<String> rc = new ArrayList<String>();
    fillFilterAbleFields(parent, rc);
    return rc;
  }

  private static void fillFilterAbleFields(UIComponent comp,
                                           List<String> list)
  {
    if (comp instanceof RichColumn)
    {
      RichColumn column = (RichColumn) comp;
      String sortProperty = column.getSortProperty();
      if (sortProperty != null && sortProperty.trim().length() > 0)
      {
        boolean isFilterable = column.isFilterable();
        if (isFilterable)
        {
          list.add(sortProperty);
        }
      }
    }

    List<UIComponent> childs = comp.getChildren();
    for (UIComponent child: childs)
    {
      fillFilterAbleFields(child, list);
    }
  }

  public List<String> getFilterAbleFields()
  {
    return filterAbleFields;
  }

  @Override
  public void sortListener(SortEvent sortEvent)
  {
    setSortModel(sortEvent.getSortCriteria());
    if (table != null) 
    {
      setSelectedRowData(table.getSelectedRowData());
    }
  }

  public CollectionModel getFilterAbleDataModel()
  {
    if (filterModel != null)
    {
      if (filterModel.map.isChanged)
      {
        createNewDataModel(null);
        filterModel.invalidateRichTable();
        filterModel.map.isChanged = false;
      }
    }
    return getDataModel();
  }
  
  @Override
  public CollectionModel getValidateDataModel()
  {
    validate2(null);
    return getDataModel();
  }

  public void refreshDataModel()
  {
    validate(null);
  }
  public void dispose() 
  {
    if (dataModel != null) 
    {
      dataModel.dispose();
    }
    connectionInterface = null;
    sql = null;
    sqlParams = null;
    filterAbleFields = null;
    //---
    dataModel = null;
    filterModel = null;
    sortModel = null;
    sqlTypesFileds.clear();
  }

  @Override
  public void selectionListener(SelectionEvent selectionEvent)
  {
    Object source = selectionEvent.getSource();
    RowKeySet rowKeySet = null;
    if (source instanceof RichTable)
    {
      rowKeySet = ((RichTable) source).getSelectedRowKeys();
    }
    else if (source instanceof RichTree)
    {
      rowKeySet = ((RichTree) source).getSelectedRowKeys();
    }
    else if (source instanceof RichTreeTable)
    {
      rowKeySet = ((RichTree) source).getSelectedRowKeys();
    }
    else if (source instanceof RichTrain)
    {
      rowKeySet = ((RichTree) source).getSelectedRowKeys();
    }
    else if (source instanceof RichToolbar)
    {
      rowKeySet = ((RichTree) source).getSelectedRowKeys();
    }
    else
    {
      System.err.println("Error. Object " + source + " not supported");
      return;
    }
    if (rowKeySet != null)
    {
      Object[] arr = rowKeySet.toArray(new Object[1]);
      if (arr != null)
      {
        if (arr.length > 0)
        {
          makeCurrent(arr[0]);
        }
      }
    }
  }

  @Override
  public void makeCurrent(Object rowKey)
  {
    selectedRowData = null;
    dataModel.setRowKey(rowKey);
    selectedRowData = dataModel.getRowData();
    System.out.println("@makeCurrent : current record = "+selectedRowData);
  }

  public Object getSelectedRowData()
  {
    return selectedRowData;
  }

  public void setSelectedRowData(Object selectedRowData)
  {
    this.selectedRowData = selectedRowData;
  }

  public void setTable(RichTable table)
  {
    this.table = table;
  }

  public RichTable getTable()
  {
    return table;
  }

  public int getRangeSize()
  {
    return rangeSize;
  }
  //------------- Classes begin ----------------------
  public class MyMap<String, Object>  extends HashMap<String, Object> implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private boolean isChanged = false;

    @Override
    public Object put(String key, Object value)
    {
      Object oldValue = get(key);
      Object rc = super.put(key, value);
      setChanged(oldValue, value);
      return rc;
    }

    @Override
    public void putAll(Map value)
    {
      super.putAll(value);
      isChanged = true;
    }

    @Override
    public Object remove(java.lang.Object key)
    {
      if (containsKey(key))
      {
        isChanged = true;
      }
      Object rc = super.remove(key);
      return rc;
    }

    @Override
    public void clear()
    {
      if (size() > 0)
      {
        isChanged = true;
      }
      super.clear();
    }

    private void setChanged(Object oldValue, Object value)
    {
      if (oldValue == null)
      {
        if (value != null)
        {
          if (value instanceof java.lang.String)
          {
            java.lang.String tmp1 = (java.lang.String)value;
            if (tmp1.trim().length() == 0) 
            {
              ;
            }
          }
          else 
          {
            isChanged = true;  
            //System.out.println("@isChanged = true 1");
          }
        }
        else
        {
          ;
        }
      }
      else
      {
        if (value != null)
        {
          if (!isChanged) //
          {
            isChanged = !(value.equals(oldValue));
            //if (isChanged)  System.out.println("@isChanged = true 2");
          }
        }
        else
        {
          isChanged = true;
          //System.out.println("@isChanged = true 2");
        }
      }
    }
  }

  public class MyFilterableQueryDescriptor  extends FilterableQueryDescriptor implements Serializable
  {
    private static final long serialVersionUID = 1L;

    private transient MyMap<String, Object> map = null;
    //private transient RichTable richTable = null;

    private MyFilterableQueryDescriptor(MyMap<String, Object> map)
    {
      super();
      this.map = map;

    }

    @Override
    public Map<String, Object> getFilterCriteria()
    {
      return map;
    }

    @Override
    public void setFilterCriteria(Map<String, Object> map)
    {
      // TODO
//      if (map == null)
//      {
//        this.map = null;
//      }
//      if (map instanceof MyMap)
//      {
//        this.map = (MyMap<String, Object>) map;
//      }
//      else 
//      {
//        this.map = new MyMap<String, Object>();
//        this.map.putAll(map);
//        this.map.isChanged = false; //?
//      }
//      
//      if (this.map == null)
//      {
//        this.map = new MyMap<String, Object>();
//      }
      
      // Было только :
      this.map = new MyMap<String, Object>();
      //..this.map.putAll(map);
    }

    @Override
    public void addCriterion(String string)
    {
      ;
    }

    @Override
    public void changeMode(QueryDescriptor.QueryMode queryMode)
    {
      ;
    }

    @Override
    public ConjunctionCriterion getConjunctionCriterion()
    {
      return null;
    }

    @Override
    public String getName()
    {
      return null;
    }

    @Override
    public Map<String, Object> getUIHints()
    {
      return Collections.emptyMap();
    }

    @Override
    public void removeCriterion(oracle.adf.view.rich.model.Criterion criterion)
    {
      ;
    }

    @Override
    public AttributeCriterion getCurrentCriterion()
    {
      return null;
    }

    @Override
    public void setCurrentCriterion(AttributeCriterion attributeCriterion)
    {
      ;
    }

    private void invalidateRichTable()
    {
      if (table != null)
      {
        AdfFacesContext.getCurrentInstance().addPartialTarget(table);
      }
    }
  }
  //------------- Classes end ---------

  @Override
  public CollectionModel getDataModel()
  {
    return dataModel;
  }
  
  @Override
  public void setSortModel(List<SortCriterion> value)
  {
    this.sortModel = value;
    createNewDataModel(null);
  }

  @Override
  public List<SortCriterion> getSortModel()
  {
    return sortModel;
  }

  @Override
  public FilterableQueryDescriptor getFilterModel()
  {
    return filterModel;
  }

//  public FilterableQueryDescriptor getFilterModel(RichTable richTable)
//  {
//    filterModel.richTable = richTable;
//    return filterModel;
//  }

  @Override
  public String getDataSignature()
  {
    return dataModel.getDynamicListSignature().getSignature();
  }

  @Override
  public void setDataSignature(String value)
  {
    dataModel.getDynamicListSignature().setSignature(value);
  }

  @Override
  public void validate(String newDataSignature) //,List<SortCriterion> sortModel
  {
    if (dataModel == null)
    {
      createNewDataModel(newDataSignature);
    }
    else
    {
      dataModel.getDynamicListSignature().refresh();
      if (newDataSignature == null && filterModel == null)
      {
        ;
      }
      else if (newDataSignature != null && filterModel == null)
      {
        if (!newDataSignature.equals(dataModel.getDynamicListSignature().getSignature()))
        {
          createNewDataModel(newDataSignature);
        }
        else
        {
          ;
        }
      }
      else if (newDataSignature == null && filterModel != null)
      {
        if (filterModel.map.isChanged)
        {
          createNewDataModel(newDataSignature);
          filterModel.invalidateRichTable();
          filterModel.map.isChanged = false;
        }
      }
      else if (newDataSignature != null && filterModel != null)
      {
        if (!newDataSignature.equals(dataModel.getDynamicListSignature().getSignature()))
        {
          createNewDataModel(newDataSignature);
        }
        else
        {
          if (filterModel.map.isChanged)
          {
            createNewDataModel(newDataSignature);
            filterModel.invalidateRichTable();
            //-----------
//            if (table != null)
//            {
//              SelectionEvent event = null;
//              event = new SelectionEvent( table,
//                                          table.getSelectedRowKeys(),
//                                          table.getSelectedRowKeys()
//                                        );
//              selectionListener(event);
//            }
            //-----------
            filterModel.map.isChanged = false;
          }
        }
      }
    }
  }
  
  public void validate2(String newDataSignature) //,List<SortCriterion> sortModel
  {
    if (dataModel == null)
    {
      createNewDataModel(newDataSignature);
    }
    else
    {
      //dataModel.getDynamicListSignature().refresh();
      if (newDataSignature == null && filterModel == null)
      {
        ;
      }
      else if (newDataSignature != null && filterModel == null)
      {
        if (!newDataSignature.equals(dataModel.getDynamicListSignature().getSignature()))
        {
          createNewDataModel(newDataSignature);
        }
        else
        {
          ;
        }
      }
      else if (newDataSignature == null && filterModel != null)
      {
        if (filterModel.map.isChanged)
        {
          createNewDataModel(newDataSignature);
          filterModel.invalidateRichTable();
          filterModel.map.isChanged = false;
        }
      }
      else if (newDataSignature != null && filterModel != null)
      {
        if (!newDataSignature.equals(dataModel.getDynamicListSignature().getSignature()))
        {
          createNewDataModel(newDataSignature);
        }
        else
        {
          if (filterModel.map.isChanged)
          {
            createNewDataModel(newDataSignature);
            filterModel.invalidateRichTable();
            filterModel.map.isChanged = false;
          }
        }
      }
    }
  }

  private void createNewDataModel(String dataSignature)
  {
    if (filterModel == null)
    {
      if (filterAbleFields != null)
      {
        filterModel = new MyFilterableQueryDescriptor(getMapFilters());
      }
    }
    //-----------------------------
    DynamicListSignature<Map<String, Object>> dbList = buildDynamicListSignature(dataSignature);
    dataModel = new CollectionModel2(dbList);
  }
  
  protected DynamicListSignature<Map<String, Object>> 
                buildDynamicListSignature(String dataSignature) 
  {
    String newSql = calculateNewSql(sql, filterModel, sortModel, sqlTypesFileds);
    DynamicListSignature<Map<String, Object>> rc =
      new DbDynamicListImpl(newSql, sqlParams, rangeSize, connectionInterface);
    if (dataSignature != null)
    {
      rc.setSignature(dataSignature);
    }
    return rc;
  }

  private MyMap<String, Object> getMapFilters()
  {
    MyMap<String, Object> rc = null;
    if (filterAbleFields == null)
    {
      return rc;
    }
    rc = new MyMap<String, Object>();
    for (String field: filterAbleFields)
    {
      rc.put(field, "");
    }
    return rc;
  }

  protected static String calculateNewSql(String sql,
                                        MyFilterableQueryDescriptor filterModel,
                                        List<SortCriterion> sortModel,
                                        Map<String,Integer> sqlTypesFileds
                                        )
  {
    String rc = sql;
    if (filterModel == null && sortModel == null)
    {
      ;
    }
    else if (filterModel != null && sortModel == null)
    {
      rc = calculateNewSqlByFilters(sql, filterModel.map,sqlTypesFileds);
    }
    else if (filterModel == null && sortModel != null)
    {
      rc = calculateNewSqlBySortModel(sql, sortModel, true);
    }
    else if (filterModel != null && sortModel != null)
    {
      rc = calculateNewSqlByFilters(sql, filterModel.map,sqlTypesFileds);
      rc = calculateNewSqlBySortModel(rc, sortModel, true);
    }
    //System.out.println("@@New Sql = "+rc);
    return rc;
  }

  private static String calculateNewSqlByFilters(String sql,
                                                 Map<String, Object> filterValues,
                                                 Map<String,Integer> sqlTypesFileds
                                                 )
  {
    if (filterValues == null || filterValues.size() == 0)
    {
      return sql;
    }
    Collection<Object> values = filterValues.values();
    boolean isExistsNotEmptyElement = false;
    for (Object value: values)
    {
      if (value != null)
      {
        if (!StringFunc.isEmpty(value.toString()))
        {
          isExistsNotEmptyElement = true;
          break;
        }
      }
    } // for
    if (!isExistsNotEmptyElement)
    {
      return sql;
    }
    //----------------------------------------------
    String rc = "SELECT * FROM (${sql}) ${where}";
    rc = rc.replace("${sql}", sql);
    StringBuffer where = new StringBuffer();
    Set<String> keys = filterValues.keySet();
    for (String name: keys)
    {
      Object objValue = filterValues.get(name);
      int sqlType = sqlTypesFileds.get(name);
      if (objValue == null) 
      {
        continue;
      }
      String strValue = objValue.toString();
      if (StringFunc.isEmpty(strValue))
      {
        continue;
      }
      if (where.length() > 0)
      {
        where.append(" AND ");
      }
      
      if (sqlType == Types.DATE || sqlType == Types.TIMESTAMP) 
      {
        if (objValue instanceof java.util.Date) 
        {
          strValue = DateTime.javaUtilDate2RusianDate((java.util.Date)objValue);
          where.append("to_char(").append(name).append(",'dd/MM/yyyy') LIKE '").append(strValue).append("%'");
        }
        else if (objValue instanceof java.sql.Date) 
        {
          strValue = DateTime.javaUtilDate2RusianDate(new java.util.Date(((java.sql.Date)(objValue)).getTime()));
          where.append("to_char(").append(name).append(",'dd/MM/yyyy') LIKE '").append(strValue).append("%'");
        }
        else if (objValue instanceof oracle.jbo.domain.Date) 
        {
          strValue = DateTime.javaUtilDate2RusianDate(DateTime.domainDate2javaUtilDate((oracle.jbo.domain.Date) objValue));
          where.append("to_char(").append(name).append(",'dd/MM/yyyy') LIKE '").append(strValue).append("%'");
        }
        else 
        {
          where.append("to_char(").append(name).append(",'dd/MM/yyyy') LIKE '").append(strValue).append("%'");
        }
      }
      else 
      {
        where.append("Upper(").append(name).append(") LIKE '").append(strValue.toUpperCase()).append("%'");
      }
    } // for
    if (where.length() > 0)
    {
      where.insert(0, "WHERE ");
    }
    rc = rc.replace("${where}", where.toString());
    return rc;
  }

  private static String calculateNewSqlBySortModel(String sql,
                                                   List<SortCriterion> sortModel,
                                                   boolean isWrap)
  {
    if (sortModel == null || sortModel.size() == 0)
    {
      return sql;
    }
    StringBuffer rc = new StringBuffer();
    if (isWrap)
    {
      rc.append("SELECT * FROM (").append(sql).append(") ");
    }
    else
    {
      rc.append(sql).append(" ");
    }
    StringBuffer orderBy = new StringBuffer();
    for (SortCriterion criterion: sortModel)
    {
      String property = criterion.getProperty();
      boolean isAsc = criterion.isAscending();
      String asc = (isAsc? " ASC ": " DESC ");
      if (orderBy.length() > 0)
      {
        orderBy.append(",");
      }
      orderBy.append(property).append(asc);
    } // for
    if (orderBy.length() > 0)
    {
      orderBy.insert(0, "ORDER BY ");
    }
    rc.append(orderBy.toString());
    return rc.toString();
  }
  //==============================================================
  //*
  //==============================================================

  public static void main(String[] args)
  {
    
    java.sql.Date d = new java.sql.Date(System.currentTimeMillis());
    String s = DateTime.javaUtilDate2RusianDate(new Date(d.getTime()));
    System.out.println("@d="+s);
    
//    Date d = new Date();
//    String s = DateTime.javaUtilDate2RusianDate(d);
//    System.out.println("@d="+s);
    
    //testCalculateNewSqlByFilters();
    //testcalculateNewSqlBySortModel();
  }

  static void testCalculateNewSqlByFilters()
  {
//    String sql = "SELECT HOUSE_ID ID, SNAME FROM T_HOUSE";
//    Map<String, Object> filterValues = new HashMap<String, Object>();
//    filterValues.put("ID", "1");
//    filterValues.put("SNAME", "�");
//    System.out.println(calculateNewSqlByFilters(sql, filterValues));
  }

  private static void testcalculateNewSqlBySortModel()
  {
    String sql = "SELECT HOUSE_ID ID, SNAME FROM T_HOUSE";
    List<SortCriterion> sortModel = new ArrayList<SortCriterion>();
    SortCriterion item = null;
    item = new SortCriterion("ID", true);
    sortModel.add(item);
    item = new SortCriterion("SNAME", false);
    sortModel.add(item);
    boolean isWrap = false;
    System.out.println(calculateNewSqlBySortModel(sql, sortModel, isWrap));
  }
}