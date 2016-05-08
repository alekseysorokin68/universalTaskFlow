package com.rcore.utils.table;


import com.rcore.global.StringFunc;
import com.rcore.global.adf.AdfBaseBean;
import com.rcore.global.bcomponent.base.ViewObjectImplRcore;
import com.rcore.model.dynamic_list.DbDynamicListImpl2;

import java.io.Serializable;

import java.util.Collection;
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
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.jbo.AttributeDef;
import oracle.jbo.Key;
import oracle.jbo.Row;

import org.apache.myfaces.trinidad.component.UIXComponent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.event.SortEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.SortCriterion;


/**
 * @see TableModelesIntegrator
 */
public class TableModelesIntegratorImpl2 implements TableModelesIntegrator, Serializable
{
  @SuppressWarnings("compatibility:8239467227867902143")
  private static final long serialVersionUID = 1L;
  private ViewObjectImplRcore viewObject  = null;
  private Map<String,String>    filterAbleMapSortPropertyColumnName = new HashMap<String,String>();
  //private List<String>        filterAbleFields = null;
  //---
  private CollectionModel2           dataModel   = null;
  private FilterableQueryDescriptor2 filterModel = null;
  private List<SortCriterion>        sortModel   = null;
  private boolean                    autoSetCurrentRow = true;
  private boolean                    autoSetCurrentMap = false;
  private boolean                    fieldNameAsAttributeName=false;
  private int                        cache_max_size = 10000;
  private Object                     selectedRowData = null;


  public static TableModelesIntegratorImpl2 createTableModelesIntegratorImpl(
                                    ViewObjectImplRcore viewObject,
                                    UIXComponent parent,
                                    boolean fieldNameAsAttributeName,
                                    int cache_max_size
                )
  {
    return createTableModelesIntegratorImpl(viewObject,parent,true,false,fieldNameAsAttributeName,cache_max_size);
  }
  public static TableModelesIntegratorImpl2 createTableModelesIntegratorImpl(ViewObjectImplRcore viewObject,
                                                                             UIXComponent parent,
                                                                             boolean autoSetCurrentRow,
                                                                             boolean autoSetCurrentMap,
                                                                             boolean fieldNameAsAttributeName,
                                                                             int cache_max_size
                                                                             )
  {
    Map<String,String> filterAbleFields = getFilterAbleFields(parent, viewObject, fieldNameAsAttributeName);
    TableModelesIntegratorImpl2 rc =
      new TableModelesIntegratorImpl2(viewObject,
                                      filterAbleFields,
                                      autoSetCurrentRow,
                                      autoSetCurrentMap,
                                      fieldNameAsAttributeName,
                                      cache_max_size
                                      );
    return rc;
  }

  private static Map<String,String> getFilterAbleFields(
      UIXComponent parent, 
      ViewObjectImplRcore viewObject, 
      boolean fieldNameAsAttributeName)
  {
    Map<String,String> rc = new HashMap<String,String>();
    fillFilterAbleFields(parent, rc, viewObject, fieldNameAsAttributeName);
    return rc;
  }

//  private static void fillFilterAbleFields(
//      UIComponent comp, 
//      List<String> list, 
//      ViewObjectImplRcore viewObject, 
//      boolean fieldNameAsAttributeName)
//  {
//    if (comp instanceof RichColumn)
//    {
//      RichColumn column = (RichColumn) comp;
//      String sortProperty = column.getSortProperty();
//      if (sortProperty != null && sortProperty.trim().length() > 0)
//      {
//        boolean isFilterable = column.isFilterable();
//        if (isFilterable)
//        {
//          list.add(sortProperty);
//        }
//      }
//    }
//
//    List<UIComponent> childs = comp.getChildren();
//    for (UIComponent child : childs)
//    {
//      fillFilterAbleFields(child,list, viewObject, fieldNameAsAttributeName);
//    }
//  }
  
    private static void fillFilterAbleFields(
      UIComponent comp, 
      Map<String,String> map, 
      ViewObjectImplRcore viewObject, 
      boolean fieldNameAsAttributeName)
  {
    if (comp instanceof RichColumn && comp.isRendered())
    {
      RichColumn column = (RichColumn) comp;
      String columnName = (String)(column.getAttributes().get("columnName"));
      if (columnName == null) 
      {
        columnName = column.getSortProperty();
      }
      if (columnName != null && columnName.trim().length() > 0)
      {
        boolean isFilterable = column.isFilterable();
        if (isFilterable)
        {
//          if (!list.contains(columnName)) {
//            list.add(columnName);
//          }
          map.put(column.getSortProperty(), columnName);
        }
      }
    }

    List<UIComponent> childs = comp.getChildren();
    for (UIComponent child : childs)
    {
      if (child.isRendered()) {
        fillFilterAbleFields(child,map, viewObject, fieldNameAsAttributeName);
      }
    }
  }
  
  public TableModelesIntegratorImpl2(
                                      ViewObjectImplRcore viewObject,
                                      Map<String,String>  filterAbleFields,
                                      boolean autoSetCurrentRow,
                                      boolean autoSetCurrentMap,
                                      boolean fieldNameAsAttributeName,
                                      int cache_max_size
                                   )
  {
    super();
    this.viewObject = viewObject;
    this.filterAbleMapSortPropertyColumnName = filterAbleFields;
    if (this.filterAbleMapSortPropertyColumnName == null) 
    {
      this.filterAbleMapSortPropertyColumnName = new HashMap<String,String>();
    }
    this.autoSetCurrentRow = autoSetCurrentRow;
    this.autoSetCurrentMap = autoSetCurrentMap;
    this.fieldNameAsAttributeName = fieldNameAsAttributeName;
    this.cache_max_size = cache_max_size;
    createNewDataModel(null);
  }

  public Map<String,String> getFilterAbleMapSortPropertyColumnName()
  {
    return filterAbleMapSortPropertyColumnName;
  }


  @Override
  public void sortListener(SortEvent sortEvent)
  {
    setSortModel(sortEvent.getSortCriteria());
  }

  @Override
  public CollectionModel getValidateDataModel()
  {
    validate(null);
    return getDataModel();
  }

  public void refreshDataModel()
  {
    if (viewObject != null)
    {
      viewObject.setOriginalQuery(null);
    }
    if (dataModel != null)
    {
      dataModel.clearLocalCache();
      dataModel = null;
    }
    validate(null);
  }

  @Override
  public void selectionListener(SelectionEvent selectionEvent)
  {
    Object source = selectionEvent.getSource();
    RowKeySet rowKeySet = null;
    if      (source instanceof RichTable)
    {
      rowKeySet = ((RichTable)source).getSelectedRowKeys();
    }
    else if (source instanceof RichTree)
    {
      rowKeySet = ((RichTree)source).getSelectedRowKeys();
    }
    else if (source instanceof RichTreeTable)
    {
      rowKeySet = ((RichTree)source).getSelectedRowKeys();
    }
    else if (source instanceof RichTrain)
    {
      rowKeySet = ((RichTree)source).getSelectedRowKeys();
    }
    else if (source instanceof RichToolbar)
    {
      rowKeySet = ((RichTree)source).getSelectedRowKeys();
    }
    else
    {
      System.err.println("Error. Object "+source+" not supported");
      return;
    }
    if (rowKeySet != null) {
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

  /**
   * Здесь мы синхронизируем также ViewObject если он задан
   */
  @Override
  public void makeCurrent(Object rowKey)
  {
    selectedRowData = null;
    if (autoSetCurrentRow) {
      try {
        dataModel.setRowKey(rowKey);
      }
      catch(Exception ex)
      {
        System.err.println("@rowKey (1)="+rowKey);
        System.err.println(ex.getMessage());
      }
      boolean isOk = makeCurrentByKeys(rowKey);
      if (!isOk) {
        isOk = makeCurrentByGetRowIndex(rowKey);
        if (isOk)
        {
          selectedRowData = dataModel.getRowData();
          System.out.println("TableModelesIntegratorImpl2: текущая запись установлена - "+selectedRowData);
        }
      }
      else
      {
        selectedRowData = AdfBaseBean.row2map(viewObject.getCurrentRow());
        System.out.println("TableModelesIntegratorImpl2: текущая запись установлена - "+selectedRowData);
      }
      //---------------------------------------
      if (!isOk)
      {
        System.err.println("TableModelesIntegratorImpl2: текущая запись не установлена" );
      }
    }
    if (autoSetCurrentMap) {
      try {
        dataModel.setRowKey(rowKey);
      }
      catch(Exception ex)
      {
        System.err.println("@rowKey (2)="+rowKey);
        System.err.println(ex.getMessage());
      }
      //----------------------------------------------------------------------------
      //Map<String,Object> rowData = (Map<String, Object>)(dataModel.getRowData(rowKey));
      selectedRowData = dataModel.getRowData(rowKey);
      //----------------------------------------------------------------------------
      if (selectedRowData != null) {
        viewObject.setCurrentMap((Map<String,Object>)selectedRowData);
        System.out.println("TableModelesIntegratorImpl2 - setCurrentMap : "+selectedRowData);
      }
    }
  }

  private boolean makeCurrentByKeys(Object rowKey)
  {
    boolean rc = false;
    try {
      if (viewObject != null) {
        AttributeDef[] attrKeys = viewObject.getKeyAttributeDefs();
        if (attrKeys == null || attrKeys.length == 0)
        {
          return rc;
        }
        Object[] keyValues = new Object[attrKeys.length];
        Map<String,Object> rowData = (Map<String, Object>) dataModel.getRowData();
        // Заполним keyValues
        for (int i=0 ; i < attrKeys.length; i++)
        {
          AttributeDef attrDef = attrKeys[i];
          String name = attrDef.getColumnName(); //attrDef.getColumnNameForQuery();
          keyValues[i] = rowData.get(name);
          if (keyValues[i] == null)
          {
            name = attrDef.getColumnNameForQuery();
            keyValues[i] = rowData.get(name);
            if (keyValues[i] == null)
            {
              name = attrDef.getName();
              keyValues[i] = rowData.get(name);
            }
          }
        }
        Key key = new Key(keyValues);
        Row[] rows = null;
        try {
          rows = viewObject.findByKey(key, 1);
        }
        catch(Exception ex)
        {
          System.err.println("Ошибка поиска по ключу "+key);
          ex.printStackTrace();
        }
        if (rows != null && rows.length > 0)
        {
          rc = viewObject.setCurrentRow(rows[0]);
        }
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    return rc;
  }

  private boolean makeCurrentByGetRowIndex(Object rowKey)
  {
    Row row = null;
    try {
      row = ViewObjectImplRcore.getRowByIndex((Integer)rowKey, viewObject);
    }
    catch(Exception ex)
    {
      System.err.println("@makeCurrentByGetRowIndex rowKey = "+rowKey);
      System.err.println("@makeCurrentByGetRowIndex viewObject = "+viewObject);
      if (rowKey != null) {
        ex.printStackTrace();
      }
    }
    boolean isOk = false;
    if (row != null) {
      isOk = viewObject.setCurrentRow(row);
    }
    return isOk;
  }
  //------------------------------------
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
  public FilterableQueryDescriptor getFilterModel(RichTable richTable)
  {
    filterModel.setRichTable(richTable);
    return filterModel;
  }

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
        if (filterModel.getMap().isChanged()) {
          createNewDataModel(newDataSignature);
          filterModel.invalidateRichTable();
          filterModel.getMap().setChanged(false);
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
          if (filterModel.getMap().isChanged()) {
            createNewDataModel(newDataSignature);
            filterModel.invalidateRichTable();
            filterModel.getMap().setChanged(false);
          }
        }
      }
    }
  }

  private void createNewDataModel(String dataSignature)
  {
    if (filterModel == null) {
      if (filterAbleMapSortPropertyColumnName != null) {
        filterModel = new FilterableQueryDescriptor2(getMapFilters());
      }
    }
    { // Новый тестируемый блок
      if (viewObject.getOriginalQuery() == null)
      {
        String sql = viewObject.getUserDefinedQuery();
        //String sql = viewObject.getQuery();
        viewObject.setOriginalQuery(sql);
      }
      String whereClause = viewObject.getWhereClause();
      viewObject.setWhereClause(null);
      viewObject.setOrderByClause(null);

      String newSql = calculateNewSql(viewObject.getOriginalQuery(),
                                      whereClause,
                                      this.filterModel,
                                      this.sortModel);
      //System.out.println("@newSql = "+newSql);
      
      viewObject.setQuery(newSql);
      viewObject.reset();
    }

    //--------------------------------
    if (autoSetCurrentRow) {
      viewObject.executeQuery();
      viewObject.first();
    }

    DbDynamicListImpl2 dbList = new DbDynamicListImpl2(viewObject,
                                                       this.fieldNameAsAttributeName
                                                       );
    if (dataSignature != null) {
      dbList.setSignature(dataSignature);
    }
    dataModel = new CollectionModel2(dbList);
    //Debug.timingFinish("createNewDataModel");
  }

  private MapChangeIndicator getMapFilters()
  {
    MapChangeIndicator rc = null;
    if (filterAbleMapSortPropertyColumnName == null)
    {
      return rc;
    }
    rc = new MapChangeIndicator();
    for(String field : filterAbleMapSortPropertyColumnName.keySet())
    {
      rc.put(field, "");
    }
    return rc;
  }

  private String calculateNewSql(
                                 String sql,
                                 String whereClause,
                                 FilterableQueryDescriptor2 filterModel,
                                 List<SortCriterion> sortModel)
  {
    String rc = sql;
    if (whereClause != null)
    {
      rc = "select * from ("+rc+") where "+whereClause;
    }
    //------------------------------------------------
    if      (filterModel == null && sortModel == null)
    {
      ;
    }
    else if (filterModel != null && sortModel == null)
    {
      rc = calculateNewSqlByFilters(rc,filterModel.getMap());
    }
    else if (filterModel == null && sortModel != null)
    {
      rc = calculateNewSqlBySortModel(rc,sortModel,true);
    }
    else if (filterModel != null && sortModel != null)
    {
      rc = calculateNewSqlByFilters(rc,filterModel.getMap());
      rc = calculateNewSqlBySortModel(rc,sortModel,true);
    }
    return rc;
  }

  private String calculateNewSqlByFilters(
      String sql,Map<String,Object> filterValues)
  {
    //System.out.println("@filterValues = "+filterValues);
    //System.out.println("@filterAbleFields = "+filterAbleMapSortPropertyColumnName);
    if (filterValues == null || filterValues.size() == 0)
    {
      return sql;
    }
    Collection<Object> values = filterValues.values();
    boolean isExistsNotEmptyElement = false;
    for(Object value : values)
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
    //System.out.println("@isExistsNotEmptyElement = "+isExistsNotEmptyElement);
    if (!isExistsNotEmptyElement)
    {
      return sql;
    }
    //----------------------------------------------
    String rc = "SELECT * FROM (${sql}) ${where}";
    rc = rc.replace("${sql}", sql);
    StringBuffer where = new StringBuffer();
    Set<String> keys = filterValues.keySet();
    for(String sortProperty : keys)
    {
      String value = filterValues.get(sortProperty).toString();
      if (StringFunc.isEmpty(value))
      {
        continue;
      }
      if (where.length() > 0)
      {
        where.append(" AND ");
      }
      String columnName = filterAbleMapSortPropertyColumnName.get(sortProperty);
      if (columnName == null) 
      {
        columnName = sortProperty;
      }
      
      boolean isDate = false;
      if (fieldNameAsAttributeName)
      {
        columnName = viewObject.getMapName_ColumnName().get(columnName);
        //--
        try {
          //int index = viewObject.getAttributeIndexOf(sortProperty);
          int index = viewObject.getAttributeIndexOf(columnName);
          AttributeDef def = viewObject.getAttributeDefs()[index];
          Class javaType = def.getJavaType();
          if (javaType != null)
          {
            isDate = (
              javaType.isInstance(new oracle.jbo.domain.Date()) ||
              javaType.isInstance(new java.util.Date())
                     );
          }
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
        //--
      }
      else
      {
        String name = viewObject.getMapColumnName_Name().get(columnName);
        try {
          int index = viewObject.getAttributeIndexOf(name);
          AttributeDef def = viewObject.getAttributeDefs()[index];
          Class javaType = def.getJavaType();
          if (javaType != null)
          {
            isDate = (
              javaType.isInstance(new oracle.jbo.domain.Date()) ||
              javaType.isInstance(new java.util.Date())
                     );
          }
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
        //--
      }
      if (!isDate) {
        where.append("Upper(").append(columnName).append(") LIKE '").append(value.toUpperCase()).append("%'");
      }
      else
      {
        String value2 = value;
        if (value2 != null) 
        {
          value2 = value2.replace(".", "/");
          value2 = value2.replace("-", "/");
        }
        where.append("to_char(").append(columnName).append(",'dd/MM/yyyy') LIKE '").append(value2).append("%'");
      }
    } // for
    if (where.length() > 0)
    {
      where.insert(0, "WHERE ");
    }
    rc = rc.replace("${where}", where.toString());
    return rc;
  }
  private String calculateNewSqlBySortModel(String sql, List<SortCriterion> sortModel,boolean isWrap)
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
    for(SortCriterion criterion : sortModel)
    {
      String sortProperty = criterion.getProperty();
      boolean isAsc   = criterion.isAscending();
      String asc = (isAsc ? " ASC ":" DESC ");
      if (orderBy.length() > 0)
      {
        orderBy.append(",");
      }
      if (fieldNameAsAttributeName)
      {
        sortProperty = viewObject.getMapName_ColumnName().get(sortProperty);
      }
      orderBy.append(sortProperty).append(asc);
    } // for
    if (orderBy.length() > 0)
    {
      orderBy.insert(0, "ORDER BY ");
    }
    rc.append(orderBy.toString());
    String rcc = rc.toString();
    return rcc;
  }
  //==============================================================
  //
  //==============================================================
//  public static Object getRowData(Object key)
//  {
//    System.out.println("getRowData(Object key)");
//    return "1";
//  }
//  public static Object getRowData()
//  {
//    System.out.println("getRowData()");
//    return "2";
//  }
//  public static Object getRowData(Object[] key)
//  {
//    System.out.println("getRowData(Object[] key)");
//    return "3";
//  }
  public static void main(String[] args)
    throws Exception
  {
    try
    {
      throw new RuntimeException("TEST");
    }
    catch(Throwable ex)
    {
      System.err.println("Error - "+ex.getMessage());
    }
//    Object ob1 = "1";
//    Object[] list = {ob1};
//    //getRowData(list);
//    try {
//      Method met = TableModelesIntegratorImpl2.class.getMethod(
//        "getRowData", new Class[]{Object[].class});
//      System.out.println(met);
//      met.invoke(null,new Object[]{ob1});
//
//      //System.out.println(ret);
//    }
//    catch(Exception ex)
//    {
//      ex.printStackTrace();
//    }
  }

  public void setSelectedRowData(Object selectedRowData)
  {
    this.selectedRowData = selectedRowData;
  }

  public Object getSelectedRowData()
  {
    return selectedRowData;
  }
}

//  static void testCalculateNewSqlByFilters()
//  {
//    String sql = "SELECT HOUSE_ID ID, SNAME FROM T_HOUSE";
//    Map<String,Object> filterValues = new HashMap<String,Object>();
//    filterValues.put("ID", "1");
//    filterValues.put("SNAME", "�");
//    System.out.println(calculateNewSqlByFilters(sql,filterValues));
//  }
//  private static void testcalculateNewSqlBySortModel()
//  {
//    String sql = "SELECT HOUSE_ID ID, SNAME FROM T_HOUSE";
//    List<SortCriterion> sortModel = new ArrayList<SortCriterion>();
//    SortCriterion item = null;
//    item = new SortCriterion("ID",true);
//    sortModel.add(item);
//    item = new SortCriterion("SNAME",false);
//    sortModel.add(item);
//    boolean isWrap = false;
//    System.out.println(calculateNewSqlBySortModel(sql, sortModel,isWrap));
//  }
  //----
//  private class MyConnectionInterface implements ConnectionInterface
//  {
//    private ViewObjectImpl viewObject = null;
//
//    private MyConnectionInterface(ViewObjectImpl viewObject)
//    {
//      super();
//      this.viewObject = viewObject;
//    }
//    public Connection getConnection()
//    {
//      DBTransactionImpl tran = (DBTransactionImpl) viewObject.getApplicationModule().getTransaction();
//      return tran.getPersistManagerConnection();
//    }
//
//    public void closeConnection(Connection connection)
//    {
//      ;
//    }
//  }
