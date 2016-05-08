package universal_taskflow.common.data.taskflow_type_parameters.on_sql;


import com.rcore.global.DefaultCompare;
import com.rcore.global.MapWithOrderKeys;
import com.rcore.global.StringFunc;
import com.rcore.model.jdbc.NamedParameterStatement;
import com.rcore.model.jdbc.SqlParser;
import com.rcore.utils.table.CustomFilterModelWithoutSql;
import com.rcore.utils.table.GetTableInterface;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneListbox;

import oracle.jbo.Row;

import org.apache.commons.lang.mutable.MutableObject;
import org.apache.myfaces.trinidad.event.SelectionEvent;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.CommunicationWithOtherPortlets;
import universal_taskflow.common.data.LinkInfo;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.SqlAttribute;
import universal_taskflow.common.data.SqlParameter;
import universal_taskflow.common.data.VisualControlForSqlParameterInfo;
import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.types.DataChangeType;
import universal_taskflow.common.types.FilterType;
import universal_taskflow.common.types.HorizontalAlign;
import universal_taskflow.common.types.LabelAlign;
import universal_taskflow.common.types.ModelType;
import universal_taskflow.common.types.ParameterAttributeType;
import universal_taskflow.common.types.VisualControlForSqlAttributeType;
import universal_taskflow.common.types.VisualControlForSqlParameterType;
import universal_taskflow.common.utils.CommonUtilsImpl;
import universal_taskflow.common.utils.DataSourceInfo;
import universal_taskflow.common.utils.DbUtilsImpl;
import universal_taskflow.common.utils.serialization.drivers.SerializationDriverBase;

import universal_taskflow.edit_defaults.beans.AdminPageFlowBean;
import universal_taskflow.edit_defaults.beans.MessageType;


public abstract class TaskFlowParametersBaseOnSql extends TaskFlowParametersBase
{
  private static final long serialVersionUID = 1L;
  private transient RichTable tableSqlParameters = null;
  private transient RichTable tableSqlAttributes = null;
  private transient RichPopup popupSelectCategory = null;
  private transient RichSelectOneListbox comboboxCategory = null;
  private transient RichInputText inputNewCategory = null;
  private SqlParameter currentSqlParameter = null;
  private SqlAttribute currentSqlAttribute = null;
  private MapWithOrderKeys<Integer,String> custErrorSqlMessages = null;
  private String custErrorSqlDefaultMessage = null;
  //--------------- События и связи -------
  private boolean eventChangeCurrentRowActive = false;
  private LinkInfo linkByChangeCurrentRow = null;
  private transient List<MainRecord> mainRecordTableModelForLinkPortletsByChangeCurrentRow = null;
  //----------------------------------------
  private transient Map<String,Boolean> visualControlMap = new VisualControlMap();
  private transient RichPopup mainRecordPopup = null;
  private transient List<MainRecord> mainRecordTableModelForSelectPortletsInHiperLinkInTable = null;
  private transient Object mainRecordFilterModel = null;
  private transient Object mainRecordFilterModel2 = null;
  private transient RichTable mainRecordTable = null;
  private transient RichTable mainRecordTable2 = null;
  private transient long resetCacheCounter = 0;
  private transient List<Map<String,Object>> customizationErrorSqlModel = null;
  //------------------------------------------
  public TaskFlowParametersBaseOnSql()
  {
    super();
  }
  //----------------------------------------
  private Boolean personalizationOn = false;
  //private Boolean refreshButtonOn = true;
  //private Boolean isZoomButtonOn = false;
  //-------
  private ModelType modelType = ModelType.READ_ONLY;
  // Период обновления в сек. Актуально только для modelType == ModelType.READ_ONLY
  private int autoRefreshPeriod = 0; //60*2;

  // Актуально только для modelType = ModelType.READ_WRITE
  private Set<DataChangeType> dataChangeTypes = new HashSet<DataChangeType>();
  // Источник данных
  private DataSourceInfo dataSourceCurrent = null;
  // Запрос
  private String sql = null;
  // Объект базы данных для корректировки
  private String objectName = null;
  //==== Только для разработчиков. Альтернативный запрос:
  private static final String DEV_PSW_ETA = " / ' ] . ; [ =".replace(" ","");
  private transient String dev_psw = "";
  private transient int insertToAlternateTableCount = 2;
  private transient String statusFd2 = null;
  private transient Boolean alternateSqlOk = null;
  private String alternateSql = null;
  private String createAlternateTable = null;
  private Boolean alternateSqlActivated = false; // Альтернативный запрос включен
  //============================================================================
  // Параметры запроса
  private MapWithOrderKeys<String, SqlParameter> sqlParameters = null;
  // Поля запроса
  private MapWithOrderKeys<String,SqlAttribute> sqlAttributes = null;

  //private TaskFlowParametersBase taskFlowParameters = null;
  private CommunicationWithOtherPortlets communicationWithOtherPortlets = null;
  private FormTypeParameters formTypeParameters = null; //new FormTypeParameters();
  //----------------------------------------------------------------------------
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
    visualControlMap = new VisualControlMap();
  }
  //----------------------------------------------------------------------------

  public List<Map<String,Object>> getCustomizationErrorSqlModel() 
  {
    if (customizationErrorSqlModel == null)
    {
      customizationErrorSqlModel = new ArrayList<Map<String,Object>>();
      List<Integer> keyList = getCustErrorSqlMessages().getKeysList();
      for (Integer errCode : keyList) 
      {
        Map<String,Object> row = new HashMap<String,Object>();
        row.put("f1", errCode);
        row.put("f2", getCustErrorSqlMessages().get(errCode));
        row.put("f3", false);
        customizationErrorSqlModel.add(row);
      } // for
    }
    return customizationErrorSqlModel;
  }
  
  public String newErrorMessage() 
  {
    Map<String,Object> row = new HashMap<String,Object>();
    row.put("f1", new Integer(0));
    row.put("f2", "");
    row.put("f3", false);
    customizationErrorSqlModel.add(row);
    return null;
  }
  
  public String saveErrorMessages() 
  {
    getCustErrorSqlMessages().clear();
    for (Map<String,Object> row : customizationErrorSqlModel) 
    {
      Boolean isDelete = (Boolean) row.get("f3");
      if (isDelete != null && !isDelete) 
      {
        Object f1 = row.get("f1");
        if (f1 == null) f1 = 0;
        getCustErrorSqlMessages().put(
          Integer.parseInt(f1.toString()),
          (String) row.get("f2"));
      }
    }
    customizationErrorSqlModel = null;
    return null;
  }
  
  public void setDataSourceCurrent(DataSourceInfo dataSourceCurrent)
  {
    this.dataSourceCurrent = dataSourceCurrent;
  }
  
  public void setEventChangeCurrentRowActive(boolean eventChangeCurrentRowActive)
  {
    this.eventChangeCurrentRowActive = eventChangeCurrentRowActive;
  }

  public boolean isEventChangeCurrentRowActive()
  {
    return eventChangeCurrentRowActive;
  }

  public DataSourceInfo getDataSourceCurrent()
  {
    if (dataSourceCurrent == null)
    {
      List<DataSourceInfo> list = TaskFlowSystemProperties.getInstance().getListDatasource();
      dataSourceCurrent = list.get(0);
    }
    return dataSourceCurrent;
  }
  public List<SelectItem> getDataSourceSelectItems()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    List<DataSourceInfo> list = TaskFlowSystemProperties.getInstance().getListDatasource();
    for (DataSourceInfo item : list)
    {
      rc.add(new SelectItem(item,item.getTitle(),item.getJndiName()));
    } // for
    return rc;
  }

  public int getSizeListDataSources()
  {
    List<DataSourceInfo> list = TaskFlowSystemProperties.getInstance().getListDatasource();
    int rc = Math.min(4,list.size());
    return rc;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
    MutableObject mObj = new MutableObject();
    testSqlActionImpl(mObj,false);
    String err = (String) mObj.getValue();
    if (err == null) 
    {
      StringBuilder messageBuf = new StringBuilder("");
      //--- Параметры
      BuildSqlParametersStatus status1 = buildSqlParameters();
      String mess1 = status1.getMessage();
      if (mess1 != null)
      {
        messageBuf.append(mess1).append(" ");
      }
      
      //--- Поля 
      BuildSqlAttributesStatus status2 = null;
      try 
      {
        status2 = buildSqlAttributes();
      }
      catch (SQLException e)
      {
        AdminPageFlowBean.getInstance().setStatusMessage(MessageType.error, e.getMessage());
        e.printStackTrace();
        return;
      }
      String mess2 = status2.getMessage();
      if (mess2 != null)
      {
        messageBuf.append(mess2);
      }
      String mess = messageBuf.toString().trim();
      if (mess.length() > 0) {
        AdminPageFlowBean.getInstance().setStatusMessage(MessageType.info, mess);
      }
      
      // ---- setObjectName ----
//      if (sqlAttributes != null && !(sqlAttributes.isEmpty())) 
//      {
//        setObjectName(sqlAttributes.get(sqlAttributes.getKeysList().get(0)).getTableName());
//      }
//      else 
//      {
//        setObjectName(SqlParser.getTableFromSql(sql));
//      }
    }
  }

  public String getSql()
  {
    return sql;
  }
  
  public void setModelType(ModelType modelType)
  {
    this.modelType = modelType;
  }

  public ModelType getModelType()
  {
    return modelType;
  }
  public Boolean getModelReadOnly()
  {
    boolean rc = ModelType.READ_ONLY.equals(modelType);
    return rc;
  }
  public void setModelReadOnly(Boolean value)
  {
    if (value != null && value)
    {
      modelType = ModelType.READ_ONLY;
    }
    else
    {
      modelType = ModelType.READ_WRITE;
    }
    return;
  }
  public Boolean getModelReadWrite()
  {
    boolean rc = true;
    rc = ModelType.READ_WRITE.equals(modelType);
    return rc;
  }
  public void setModelReadWrite(Boolean value)
  {
    if (value != null && value)
    {
      modelType = ModelType.READ_WRITE;
    }
    else
    {
      modelType = ModelType.READ_ONLY;
    }
  }

  public void setAutoRefreshPeriod(int autoRefreshPeriod)
  {
    this.autoRefreshPeriod = autoRefreshPeriod;
  }

  public int getAutoRefreshPeriod()
  {
    return autoRefreshPeriod;
  }
  public Boolean getUpdateAble()
  {
    Boolean rc = false;
    Set<DataChangeType> set = getDataChangeTypes();
    if (set != null)
    {
      rc = set.contains(DataChangeType.UPDATE);
    }
    return rc;
  }

  public void setUpdateAble(Boolean value)
  {
    Set<DataChangeType> set = getDataChangeTypes();
    if (set == null)
    {
      set = new HashSet<DataChangeType>();
    }
    if (value != null && value)
    {
      set.add(DataChangeType.UPDATE);
    }
    else
    {
      set.remove(DataChangeType.UPDATE);
    }
    setDataChangeTypes(set);
    return;
  }

  public Boolean getInsertAble()
  {
    Boolean rc = false;
    Set<DataChangeType> set = getDataChangeTypes();
    if (set != null)
    {
      rc = set.contains(DataChangeType.INSERT);
    }
    return rc;
  }

  /**
   * @param value
   */
  public void setInsertAble(Boolean value)
  {
    Set<DataChangeType> set = getDataChangeTypes();
    if (set == null)
    {
      set = new HashSet<DataChangeType>();
    }
    if (value != null && value)
    {
      set.add(DataChangeType.INSERT);
    }
    else
    {
      set.remove(DataChangeType.INSERT);
    }
    setDataChangeTypes(set);
    return;
  }

  public Boolean getDeleteAble()
  {
    Boolean rc = false;
    Set<DataChangeType> set = getDataChangeTypes();
    if (set != null)
    {
      rc = set.contains(DataChangeType.DELETE);
    }
    return rc;
  }

  public void setDeleteAble(Boolean value)
  {
    Set<DataChangeType> set = getDataChangeTypes();
    if (set == null)
    {
      set = new HashSet<DataChangeType>();
    }
    if (value != null && value)
    {
      set.add(DataChangeType.DELETE);
    }
    else
    {
      set.remove(DataChangeType.DELETE);
    }
    setDataChangeTypes(set);
    return;
  }

  public void setDataChangeTypes(Set<DataChangeType> dataChangeTypes)
  {
    this.dataChangeTypes = dataChangeTypes;
  }

  public Set<DataChangeType> getDataChangeTypes()
  {
    return dataChangeTypes;
  }

  public void setObjectName(String objectName)
  {
    this.objectName = objectName;
  }

  public String getObjectName()
  {
    String sql = getSql();
    if (
        (objectName == null || objectName.trim().length() == 0) &&
        (sql != null && sql.trim().length() > 0)
       )
    {
      objectName = SqlParser.getTableFromSql(sql);
    }
    return objectName;
  }
  
  public String timeStudyAction()
  {
    String err = testSql();
    if (err != null)
    {
      AdminPageFlowBean.getInstance().setStatusMessage(MessageType.error,err);
      return null;
    }

    Connection con = getDataSourceCurrent().getConnection();
    try
    {
      String sql = getSql();
      Map<String, SqlParameter> params = getSqlParameters();
      Map<String, Object> paramsObj = null;
      if (params != null)
      {
        paramsObj = new HashMap<String, Object>();
        Set<String> keys = params.keySet();
        for (String key: keys)
        {
          Serializable value = params.get(key).getResolvedDefaultValue();
          paramsObj.put(key, value);
        }
      }
      StringBuilder recCount = new StringBuilder("25");
      long time = SqlParser.timeStude(sql, con, paramsObj, recCount);
      double dTime = ((double) time / (double) 1000);
      
      AdminPageFlowBean.getInstance().setStatusMessage(MessageType.info,
                      recCount + " записей выбрано за " + dTime + " сек."
      );
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      AdminPageFlowBean.getInstance().setStatusMessage(MessageType.error,"Ошибка - " + ex.getMessage());
    }
    finally
    {
      if (con != null)
      {
        try
        {
          con.close();
        }
        catch (Exception e2)
        {
          e2.printStackTrace();
        }
      }
    }
    return null;
  }
  
  public String synchronizeWithDb()
  {
    try
    {
      String message = null;
      String oldSql = getSql();
      if (oldSql != null && (oldSql.trim().length() > 0 ))
      {
        try {
          sql = getSqlToResetCache(oldSql);
          //setSql(newSql);
          message = checkAndValidate(sql);
        }
        finally
        {
          //setSql(oldSql);
          sql = oldSql;
        }
        if (message == null)
        {
          AdminPageFlowBean.getInstance().setStatusMessage(MessageType.info, "Синхронизация прошла успешно");
        }
        else
        {
          AdminPageFlowBean.getInstance().setStatusMessage(MessageType.error, message);
        }
      }
      else
      {
        AdminPageFlowBean.getInstance().setStatusMessage(MessageType.error, "Запрос пуст");
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
      AdminPageFlowBean.getInstance().setStatusMessage(MessageType.error, e.getMessage());
    }
    return null;
  }
  
  private String checkAndValidate(String value)
  {
    String rc = testSql(value);
    if (rc == null)
    {
      try
      {
        //BuildSqlAttributesStatus status = buildSqlAttributes();
        buildSqlAttributes();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return rc;
  }
  
  /**
   * Получить модифицированный SQL для сброса кэша
   * @param sql
   * @return модифицированный SQL
   */
  public String getSqlToResetCache(final String sql)
  {
    resetCacheCounter++;
    if (resetCacheCounter > 255)
    {
      resetCacheCounter = 1;
    }
    StringBuilder rc = null;
    if (sql != null)
    {
      rc = new StringBuilder(sql);
      for (int i = 1; i <= resetCacheCounter; i++ )
      {
        rc.append('\t');
      }
    }
    String sRc = null;
    if (rc != null)
    {
      sRc = rc.toString();
    }
    return sRc;
  }
  
  public String testSqlAction()
  {
    return testSqlActionImpl(new MutableObject(),true);
  }
  
  private String testSqlActionImpl(MutableObject error, boolean displayOk)
  {
    String err = testSql();
    error.setValue(err);
    if (err == null)
    {
      if (displayOk)
      {
        AdminPageFlowBean.getInstance().
          setStatusMessage(MessageType.info,"Запрос корректный");
      }
    }
    else
    {
      AdminPageFlowBean.getInstance().
        setStatusMessage(MessageType.error,err);
    }
    return null;
  }
  
  private String testSql()
  {
    String sql = getSql();
    return testSql(sql);
  }

  public static String testSql(String sql, DataSourceInfo info)
  {
    String rc = null;
    if (StringFunc.isEmpty(sql))
    {
      rc = "Ошибка - Запрос пустой";
    }
    else
    {
      Connection con = info.getConnection();
      try
      {
        rc = SqlParser.getErrorQueryWithCheckNames(con,sql);
        if (rc != null && rc.trim().startsWith("ORA-01007:"))
        {
          // ORA-01007: переменной нет в списке выборки команды - ошибка связанная с кешированием Oracle
          // Повторим:
          rc = SqlParser.getErrorQueryWithCheckNames(con,sql);
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        rc = "Ошибка - " + ex.getMessage();
      }
      finally
      {
        if (con != null)
        {
          try
          {
            info.closeConnection(con);
          }
          catch (Exception e2)
          {
            e2.printStackTrace();
          }
        }
      }
    }
    return rc;
  }

  public String testSql(String sql)
  {
    return testSql(sql,getDataSourceCurrent());
  }
  
  private static MapWithOrderKeys<String, SqlAttribute> buildSqlAttributesOnly(
          String sql,
          DataSourceInfo info,
          ModelType modelType,
          Set<DataChangeType> dataChangeTypes
  )
    throws SQLException
  {
    MapWithOrderKeys<String, SqlAttribute> rc = new MapWithOrderKeys<String, SqlAttribute>();
    Connection con = null;
    NamedParameterStatement ps = null;
    try 
    {
      con = info.getConnection();
      ps = new NamedParameterStatement(con,sql);
      List<String> paramList = ps.getNamedParametersList();
      if (paramList != null && paramList.size() > 0)
      {
        for (int i = 0; i < paramList.size(); i++)
        {
          ps.setObject(paramList.get(i), null);
        }
      }
      ResultSetMetaData meta = ps.getMetaData();
      int columnCount = meta.getColumnCount();
      for (int i = 1; i <= columnCount; i++)
      {
        SqlAttribute attrItem = new SqlAttribute(meta, i, modelType, dataChangeTypes);
        rc.put(attrItem.getName(),attrItem);
      } // for
    }
    finally 
    {
      if (ps != null) ps.close();
      if (con != null) info.closeConnection(con);
    }
    //---
    return rc;
  }
  
  private static MapWithOrderKeys<String, SqlAttribute> mergeSqlAttributes(
      String sql,
      MapWithOrderKeys<String, SqlAttribute> current,
      MapWithOrderKeys<String, SqlAttribute> old,
      DataSourceInfo info,
      BuildSqlAttributesStatus status
  )
    throws SQLException
  {
    MapWithOrderKeys<String, SqlAttribute> rc = new MapWithOrderKeys<String, SqlAttribute>();
    if (current == null)
    {
      return rc;
    }
    else if (old == null)
    {
      return current;
    }
    Connection con = null;
    NamedParameterStatement ps = null;
    ResultSetMetaData rs = null;
    try 
    {
      con = info.getConnection();
      ps = new NamedParameterStatement(con,sql);
      rs = ps.getMetaData();
      for (String itemKey : current.getKeysList())
      {
        SqlAttribute oldValue = old.get(itemKey);
        if (oldValue != null)
        {
          oldValue.validateByDb(rs);
          rc.put(itemKey, oldValue);
        }
        else
        {
          if (status != null) 
          {
            status.setCount(status.getCount()+1);  
          }
          SqlAttribute newValue = current.get(itemKey);
          rc.put(itemKey, newValue);
        }
      }
    }
    finally 
    {
      if (ps != null) ps.close();
      if (con != null) info.closeConnection(con);
    }
    return rc;
  }

  private static MapWithOrderKeys<String, SqlParameter> buildSqlParametersOnly(String sql)
  {
    MapWithOrderKeys<String, SqlParameter> rc = new MapWithOrderKeys<String, SqlParameter>();
    Map<String, int[]> paramMap = new HashMap<String, int[]>();
    SqlParser.parseNamedParameters(sql, paramMap);
    if (paramMap.size() > 0)
    {
      List<NameAndIndex> list = new ArrayList<NameAndIndex>();
      Set<String> keys = paramMap.keySet();
      for (String item: keys)
      {
        int index = minimum(paramMap.get(item));
        NameAndIndex nameAndIndex = new NameAndIndex(item, index);
        list.add(nameAndIndex);
      } // for
      DefaultCompare.sortTyped(list, new Comparator<NameAndIndex>()
        {
          public int compare(NameAndIndex o1, NameAndIndex o2)
          {
            return DefaultCompare.compareNormal(o1.index, o2.index);
          }
        });
      for (NameAndIndex item2: list)
      {
        rc.put(item2.name, new SqlParameter(item2.name));
      } // for
    } // if
    return rc;
  }
  private static MapWithOrderKeys<String, SqlParameter> mergeSqlParameters(
      MapWithOrderKeys<String, SqlParameter> current,
      MapWithOrderKeys<String, SqlParameter> old,
      BuildSqlParametersStatus status
  )
  {
    MapWithOrderKeys<String, SqlParameter> rc = new MapWithOrderKeys<String, SqlParameter>();
    if (current == null)
    {
      return rc;
    }
    else if (old == null)
    {
      return current;
    }
    for (String itemKey : current.getKeysList())
    {
      SqlParameter oldValue = old.get(itemKey);
      if (oldValue != null)
      {
        rc.put(itemKey, oldValue);
      }
      else
      {
        if (status != null) 
        {
          status.setCount(status.getCount()+1);  
        }
        rc.put(itemKey, current.get(itemKey));
      }
    }
    return rc;
  }

  private static int minimum(int[] indexes)
  {
    int rc = Integer.MAX_VALUE;
    for (int item : indexes)
    {
      rc = Math.min(rc,item);
    }
    return rc;
  }

  private BuildSqlParametersStatus buildSqlParameters()
  {
    BuildSqlParametersStatus rc = BuildSqlParametersStatus.SQL_NOT_VALID;

    MapWithOrderKeys<String, SqlParameter> oldParameters = sqlParameters;
    sqlParameters = buildSqlParametersOnly(getSql());
    if (sqlParameters.size() == 0)
    {
      rc = BuildSqlParametersStatus.NO_PARAMETERS;
      sqlParameters = null;
    }
    else
    {
      rc = BuildSqlParametersStatus.FOUND_NEW_PARAMETERS;
      rc.setCount(0);
      sqlParameters = mergeSqlParameters(sqlParameters, oldParameters, rc);
    }
    return rc;
  }
  
  private BuildSqlAttributesStatus buildSqlAttributes()
    throws SQLException
  {
    BuildSqlAttributesStatus rc = BuildSqlAttributesStatus.SQL_NOT_VALID;

    MapWithOrderKeys<String, SqlAttribute> oldAttributes = sqlAttributes;
    sqlAttributes = buildSqlAttributesOnly(getSql(), 
                                           dataSourceCurrent, 
                                           modelType, 
                                           dataChangeTypes);
    if (sqlAttributes.size() == 0)
    {
      rc = BuildSqlAttributesStatus.NO_PARAMETERS;
      sqlAttributes = null;
    }
    else
    {
      rc = BuildSqlAttributesStatus.FOUND_NEW_PARAMETERS;
      rc.setCount(0);
      sqlAttributes = mergeSqlAttributes(getSql(),sqlAttributes, oldAttributes, dataSourceCurrent, rc);
    }
    return rc;
  }

  public MapWithOrderKeys<String, SqlParameter> getSqlParameters()
  {
    return sqlParameters;
  }

  public void setSqlParameters(MapWithOrderKeys<String, SqlParameter> sqlParameters)
  {
    this.sqlParameters = sqlParameters;
  }

  public List<SqlParameter> getSqlParametersList()
  {
    List<SqlParameter> rc = sqlParameters.getValuesList();
    return rc;
  }
  
  public List<SqlAttribute> getSqlAttributesList() 
  {
    List<SqlAttribute> rc = sqlAttributes.getValuesList();
    return rc;
  }

  public void sqlParamSelectionListener(SelectionEvent selectionEvent)
  {
    //---------------
    RichTable table = (RichTable) selectionEvent.getComponent();
    SqlParameter row = (SqlParameter) table.getSelectedRowData();
    if (row != null)
    {
      currentSqlParameter = row;
    }
  }
  
  public void sqlAttributeSelectionListener(SelectionEvent selectionEvent)
  {
    RichTable table = (RichTable) selectionEvent.getComponent();
    SqlAttribute row = (SqlAttribute) table.getSelectedRowData();
    if (row != null)
    {
      currentSqlAttribute = row;
    }
  }

  public List<SelectItem> getParameterTypeSqlSelectItems()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    ParameterAttributeType[] values = ParameterAttributeType.values();
    for (ParameterAttributeType type: values)
    {
      rc.add(new SelectItem(type, type.toString()));
    }
    return rc;
  }

  //====================================================
  public static void main(String[] args)
  {
//    String sql = "select * from \n" +
//    "(\n" +
//    "   select 1 a, 2 b from dual\n" +
//    ")\n" +
//    "where a=:a and b=:b\n";
//    MapWithOrderKeys<String, SqlParameter> map = buildSqlParametersOnly(sql);
//    System.out.println("@@1="+map.getKeysList());
//    System.out.println("@@21="+map.getValuesList().get(0).getName());
//    System.out.println("@@22="+map.getValuesList().get(1).getName());

//    MutableInt i = new MutableInt(-3);
//    i.setValue(5);
//    System.out.println(i.getValue());
  }

  public void setTableSqlAttributes(RichTable tableSqlAttributes)
  {
    this.tableSqlAttributes = tableSqlAttributes;
    currentSqlAttribute = null;
    try 
    {
      if (tableSqlAttributes != null)
      {
        currentSqlAttribute = (SqlAttribute) tableSqlAttributes.getSelectedRowData();
      }
    }
    catch(Exception ex)
    {
      if (!"null".equals(ex.getMessage()+"")) 
      {
        System.err.println("Ошибка 2a : "+ex.getMessage());        
      }
    }
  }

  public void setTableSqlParameters(RichTable tableSqlParameters)
  {
    this.tableSqlParameters = tableSqlParameters;
    currentSqlParameter = null;
    try 
    {
      if (tableSqlParameters != null)
      {
        currentSqlParameter = (SqlParameter) tableSqlParameters.getSelectedRowData();
      }
    }
    catch(Exception ex)
    {
      if (!"null".equals(ex.getMessage()+"")) 
      {
        System.err.println("Ошибка 2 : "+ex.getMessage());        
      }
    }
  }

  public RichTable getTableSqlParameters()
  {
    return tableSqlParameters;
  }

  public SqlParameter getCurrentSqlParameter()
  {
    return currentSqlParameter;
  }

  public Map<String,Boolean> getVisualControlMap()
  {
    return visualControlMap;
  }

  public void setSqlAttributes(MapWithOrderKeys<String, SqlAttribute> sqlAttributes)
  {
    this.sqlAttributes = sqlAttributes;
  }

  public MapWithOrderKeys<String, SqlAttribute> getSqlAttributes()
  {
    return sqlAttributes;
  }
  
  public Map<String,Integer> getSqlAttributeTypes()
  {
    Map<String,Integer> rc = new HashMap<String,Integer>();
    for (SqlAttribute item : sqlAttributes.getValuesList()) 
    {
      rc.put(item.getName(), item.getSqlType());
    }
    return rc;
  }

  public void setPersonalizationOn(Boolean personalizationOn)
  {
    this.personalizationOn = personalizationOn;
  }

  public Boolean getPersonalizationOn()
  {
    return personalizationOn;
  }

  public void setCommunicationWithOtherPortlets(CommunicationWithOtherPortlets communicationWithOtherPortlets)
  {
    this.communicationWithOtherPortlets = communicationWithOtherPortlets;
  }

  public CommunicationWithOtherPortlets getCommunicationWithOtherPortlets()
  {
    return communicationWithOtherPortlets;
  }

  public RichTable getTableSqlAttributes()
  {
    return tableSqlAttributes;
  }

  public void setCurrentSqlAttribute(SqlAttribute currentSqlAttribute)
  {
    this.currentSqlAttribute = currentSqlAttribute;
  }

  public SqlAttribute getCurrentSqlAttribute()
  {
    return currentSqlAttribute;
  }
  
  public List<SelectItem> getSelectItemsSqlAttributesWithNull()
  {
    List<SelectItem> rc = getSelectItemsSqlAttributes();
    rc.add(0, new SelectItem(null, "", "По умолчанию - данное поле"));
    return rc;
  }
  
  public List<SelectItem> getSelectItemsFilterType()
  {
    List<SelectItem> rc = FilterType.getSelectItems();
    return rc;
  }
  
  public List<SelectItem> getSelectItemsSqlAttributes()
  {
    List<SelectItem> rc = null;
    List<SqlAttribute> listAttrs = getSqlAttributesList();
    if (listAttrs != null)
    {
      rc = new ArrayList<SelectItem>();
      for (SqlAttribute field: listAttrs)
      {
        rc.add(new SelectItem(field.getName(), 
                              field.getName(),
                              field.getName()));
      } // for
    }
    return rc;
  }

  public void setPopupSelectCategory(RichPopup popupSelectCategory)
  {
    this.popupSelectCategory = popupSelectCategory;
  }

  public RichPopup getPopupSelectCategory()
  {
    return popupSelectCategory;
  }

  public void setComboboxCategory(RichSelectOneListbox comboboxCategory)
  {
    this.comboboxCategory = comboboxCategory;
  }

  public RichSelectOneListbox getComboboxCategory()
  {
    return comboboxCategory;
  }
  
  public List<SelectItem> getCategoryOfAttributes()
  {
    Set<String> categotySet = new HashSet<String>();
    categotySet.add(null);
    List<SqlAttribute> list = getSqlAttributesList();
    for (SqlAttribute item: list)
    {
      String category = item.getCategory();
      if (StringFunc.isEmpty(category))
      {
        category = null;
      }
      categotySet.add(category);
    } // for
    //--------------------
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (String item: categotySet)
    {
      rc.add(new SelectItem(item, (item == null? "": item),
                            (item == null? "": item)));
    }
    return rc;
  }

  public void setInputNewCategory(RichInputText inputNewCategory)
  {
    this.inputNewCategory = inputNewCategory;
  }

  public RichInputText getInputNewCategory()
  {
    return inputNewCategory;
  }
  
  public String selectCategoryAction()
  {
    String value = (String) (comboboxCategory.getValue());
    if (currentSqlAttribute != null)
      currentSqlAttribute.setCategory(value);
    popupSelectCategory.hide();
    return null;
  }
  
  public String newCategoryAction()
  {
    String value = (String) (inputNewCategory.getValue());
    if (currentSqlAttribute != null)
      currentSqlAttribute.setCategory(value);
    inputNewCategory.setValue(null);
    popupSelectCategory.hide();
    return null;
  }
  
  public String cancelCategoryAction()
  {
    popupSelectCategory.hide();
    return null;
  }
  
  public List<SelectItem> getHorizontalAlignSelectItems()
  {
    return HorizontalAlign.getSelectItems();
  }
  
  public List<SelectItem> getLabelAlignSelectItems()
  {
    return LabelAlign.getSelectItems();
  }
  
  public void setMainRecordPopup(RichPopup mainRecordPopup)
  {
    this.mainRecordPopup = mainRecordPopup;
  }

  public RichPopup getMainRecordPopup()
  {
    return mainRecordPopup;
  }
  
  public void valueChangeSelectedPortletListener(ValueChangeEvent event) 
  {
    LinkInfo linkInfo = (LinkInfo) event.getComponent().getAttributes().get("linkInfo");
    String portletId = (String) event.getComponent().getAttributes().get("portletId");
    Boolean newValue = (Boolean) event.getNewValue();
    
    if (newValue != null && newValue) 
    {
      linkInfo.getPortletIdToRefresh().add(portletId);  
    }
    else 
    {
      linkInfo.getPortletIdToRefresh().remove(portletId);
    }
  }
  
  public List<MainRecord> getMainRecordTableModelForSelectPortletsInHiperLinkInTable() 
  {
    if (mainRecordTableModelForSelectPortletsInHiperLinkInTable == null)
    {
      if (currentSqlAttribute != null) 
      {
        String currentId = CommonUtilsImpl.getInstance().getTaskFlowParameterId();
        MainRecord current = null;
        mainRecordTableModelForSelectPortletsInHiperLinkInTable = SerializationDriverBase.getInstance().getMainRecordList();
        for (MainRecord item : mainRecordTableModelForSelectPortletsInHiperLinkInTable) 
        {
          if (currentId.equals(item.getId())) 
          {
            current = item;
          }
          else 
          {
            item.setSelected(false);
            try 
            {
              Set<String> portletsIdToRefresh = currentSqlAttribute.getVisualControlTypeOnTableInfo().getLinkInfo().getPortletIdToRefresh();
              item.setSelected(portletsIdToRefresh.contains(item.getId()));  
            }
            catch(Exception ex) 
            {
              System.err.println("@Error getMainRecordTableModel 0 : "+ex.getMessage());
            }
          }
        } // for
        if (current != null) 
        {
          mainRecordTableModelForSelectPortletsInHiperLinkInTable.remove(current);
        }
      }
    }
    return mainRecordTableModelForSelectPortletsInHiperLinkInTable;
  }
  
  public void closeMainRecordPopup(ActionEvent event) 
  {
    LinkInfo linkInfo = (LinkInfo) event.getComponent().getAttributes().get("linkInfo");
    if (linkInfo != null) 
    {
      // Перегрузка выбранных в mainRecordTableModel в portletIdToRefresh
      Set<String> linkPortlets = linkInfo.getPortletIdToRefresh();
      linkPortlets.clear();
      for (MainRecord item : mainRecordTableModelForSelectPortletsInHiperLinkInTable) 
      {
        if (item.isSelected()) 
        {
          linkPortlets.add(item.getId());
        }
      } // for
    }
    //--------------------------
    if (mainRecordPopup != null) 
    {
      mainRecordPopup.hide();  
    }
    return;
  }
  
  public Object getMainRecordFilterModel()
  {
    if (mainRecordFilterModel == null) 
    {
      mainRecordFilterModel = new CustomFilterModelWithoutSql(
                                  new GetTableInterface() 
                                  {
                                    public RichTable getTable()
                                    {
                                      return mainRecordTable;
                                    }
                                  }
                              ); 
    } // if
    return mainRecordFilterModel;
  }
  
  public Object getMainRecordFilterModel2()
  {
    if (mainRecordFilterModel2 == null) 
    {
      mainRecordFilterModel2 = new CustomFilterModelWithoutSql(
                                  new GetTableInterface() 
                                  {
                                    public RichTable getTable()
                                    {
                                      return mainRecordTable2;
                                    }
                                  }
                              ); 
    } // if
    return mainRecordFilterModel2;
  }
  
  public void setMainRecordTable(RichTable mainRecordTable)
  {
    this.mainRecordTable = mainRecordTable;
  }

  public RichTable getMainRecordTable()
  {
    return mainRecordTable;
  }
  
  public String setFieldsWidthEvenlyAction()
  {
    setFieldsWidthEvenly(getSqlAttributes().getValuesList());
    return null;
  }
  
  public String clearFieldsWidthInTable()
  {
    List<SqlAttribute> list = getSqlAttributes().getValuesList();
    if (list != null)
    {
      for (SqlAttribute item: list)
      {
        item.setWidthInTable(null);
      }
    }
    return null;
  }
  
  public String getSqlInsertAction()
  {
    String sql = null;
    FacesMessage.Severity type = FacesMessage.SEVERITY_INFO;
    Map<String,Object> returnParameters = null;
    try
    {
      List<SqlAttribute> sqlAttributes = getSqlAttributes().getValuesList();
      String objectName = getObjectName();
      Map<String,Object> mapAttributeValues = new HashMap<String, Object>();
      returnParameters = new HashMap<String, Object>();
      sql = DbUtilsImpl.getInstance().getInsertSql(sqlAttributes,
                                                   objectName,
                                                   mapAttributeValues,
                                                   returnParameters                 
            );
      //sql = "<html>"+sql+" <br/><br/> Parameters = "+returnParameters+"</html>";
    }
    catch (Exception e)
    {
      sql = "Ошибка : "+e.getMessage();
      type = FacesMessage.SEVERITY_ERROR;
    }
    addMessage(sql, type, null);
    //JSFUtils.addFacesInformationMessage(sql);
    //----
    return null;
  }
  
  public String getSqlUpdateAction()
  {
    Map<String,Object> returnParameters = new HashMap<String,Object>();
    String sql = null;
    FacesMessage.Severity type = FacesMessage.SEVERITY_INFO;
    try
    {
      sql = getUpdateSql(null, null, returnParameters);
      //sql = sql+"\n\nParameters = "+returnParameters;
    }
    catch (Exception e)
    {
      sql = "Ошибка : "+e.getMessage();
      type = FacesMessage.SEVERITY_ERROR;
    }
    //JSFUtils.addFacesInformationMessage(sql);
    addMessage(sql, type, null);
    return null;
  }
  
  public String getSqlDeleteAction()
  {
    Map<String,Object> returnParameters = new HashMap<String,Object>();
    String sql = null;
    FacesMessage.Severity type = FacesMessage.SEVERITY_INFO;
    try
    {
      sql = getDeleteSql(null, returnParameters);
      //sql = sql+"\n\nParameters = "+returnParameters;
    }
    catch (Exception e)
    {
      sql = "Ошибка : "+e.getMessage();
      type = FacesMessage.SEVERITY_ERROR;
    }
    addMessage(sql, type, null);
    //JSFUtils.addFacesInformationMessage(sql);
    return null;
  }
  
  public String getDeleteSql(Row row, Map<String,Object> returnParameters) throws Exception
  {
    String rc = null;
    WhereInfo info = null;
    if (row != null) 
    {
      info = new WhereInfo(row,getSqlAttributes().getValuesList());  
    }
    else 
    {
      info = new WhereInfo(new HashMap<String,Object>(),getSqlAttributes().getValuesList());
    }
    //-------    
    String where = info.getWhere();
    if (where != null && where.length() > 0)
    {
      rc = "delete from " + getObjectName() + " where " + where;
      if (returnParameters == null) 
      {
        returnParameters = new HashMap<String,Object>();
      }
      returnParameters.putAll(info.getParams());
    }
    else
    {
      throw new Exception("У таблицы не указаны ключевые поля,удаление невозможно");
    }
    return rc;
  }
  
  public String getUpdateSql(
                             Map<String, Object> rowMap,
                             Map<String, Object> values,
                             Map<String, Object> returnParameters)
  throws Exception
  {
    List<SqlAttribute> sqlAttributes = getSqlAttributes().getValuesList();
    if (rowMap == null) 
    {
      rowMap = new HashMap<String, Object>();
    }
    if (values == null) 
    {
      values = new HashMap<String, Object>();
    }
    if (returnParameters == null) 
    {
      returnParameters = new HashMap<String, Object>();
    }
    return getUpdateSql(sqlAttributes, getObjectName(), rowMap, values, returnParameters);
  }
  
  protected String getUpdateSql(List<SqlAttribute> sqlAttributes,
                                String objectName,
                                Map<String, Object> rowMap,
                                Map<String, Object> values,
                                Map<String, Object> returnParameters)
  throws Exception
  {
    List<SqlAttribute> updateList = DbUtilsImpl.getInstance().getUpdateList(sqlAttributes);
    if (updateList == null || updateList.isEmpty()) 
    {
      throw new Exception("Нет полей допустимых для корректировки (update)");
    }
    WhereInfo info = new WhereInfo(rowMap,sqlAttributes);
    String where = info.getWhere();
    if (where == null || where.trim().length() == 0 ) 
    {
      throw new Exception("Не указаны ключевые поля, корректировка невозможна");
    }
    
    StringBuilder rc = new StringBuilder("update ").append(objectName).append(" set \n");
    for (SqlAttribute attribute : updateList)
    {
       rc.append(attribute.getName()).append(" = :").append(attribute.getName()).append(",\n");
      //---
      Object value = values.get(attribute.getName());
      boolean isCheckBox = (attribute.getVisualControlTypeOnFormInfo() != null && 
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
    rc.setLength(rc.length() - 2);
    rc.append(" \nwhere ").append(where);
    returnParameters.putAll(info.getParams());
    return rc.toString();
  }
  
  public static void addMessage(String text, FacesMessage.Severity type, String clientId)
  {
    FacesMessage message = new FacesMessage(text);
    message.setSeverity(type);
    FacesContext fc = FacesContext.getCurrentInstance();
    fc.addMessage(clientId, message);
  }
  //--------------
  private void setFieldsWidthEvenly(List<SqlAttribute> sqlSqlAttributes)
  {
    List<SqlAttribute> visibleAttributes = getVisibleFieldsInTable(sqlSqlAttributes);
    int count = visibleAttributes.size();
    if (count == 0)
    {
      return;
    }
    int percent = Math.round(100 / count);
    int sum = 0;
    for (int i = 0; i < count; i++)
    {
      SqlAttribute att = visibleAttributes.get(i);
      if (i < (count - 1))
      {
        att.setWidthInTable(percent + "%");
        sum += percent;
      }
      else
      {
        att.setWidthInTable((100 - sum) + "%");
      }
    } // for
  }
  
  public List<SqlAttribute> getVisibleFieldsInTable() 
  {
    return getVisibleFieldsInTable(getSqlAttributes().getValuesList());
  }
  
  public Map<String,SqlAttribute> getVisibleFieldsInTableAsMap() 
  {
    return getVisibleFieldsInTableAsMap(getSqlAttributes().getValuesList());
  }
  
  public List<SqlAttribute> getVisibleFieldsInForm() 
  {
    return getVisibleFieldsInForm(getSqlAttributes().getValuesList());
  }
  
  private List<SqlAttribute> getVisibleFieldsInTable(List<SqlAttribute> sqlSqlAttributes)
  {
    List<SqlAttribute> rc = new ArrayList<SqlAttribute>();
    for (SqlAttribute item: sqlSqlAttributes)
    {
      if (item.getVisibleInTable())
      {
        rc.add(item);
      }
    }
    if (isOrderOnTableExists(rc)) 
    {
      DefaultCompare.sortTyped(rc, new ComparatorOnTable());
    }
    return rc;
  }
  
  private Map<String,SqlAttribute> getVisibleFieldsInTableAsMap(List<SqlAttribute> sqlSqlAttributes)
  {
    Map<String,SqlAttribute> rc = new HashMap<String,SqlAttribute>();
    for (SqlAttribute item: sqlSqlAttributes)
    {
      if (item.getVisibleInTable())
      {
        rc.put(item.getName(),item);
      }
    }
    return rc;
  }
  
  public List<String> getVisibleFilterAbleFieldsInTable(List<SqlAttribute> sqlSqlAttributes)
  {
    List<String> rc = new ArrayList<String>();
    for (SqlAttribute item: sqlSqlAttributes)
    {
      if (item.getVisibleInTable())
      {
        Boolean isFilterAble = item.getFilterAble();
        if (isFilterAble == null) isFilterAble = false;
        if (isFilterAble) 
        {
          rc.add(item.getName());  
        }
      }
    }
    return rc;
  }
  
  private List<SqlAttribute> getVisibleFieldsInForm(List<SqlAttribute> sqlSqlAttributes)
  {
    List<SqlAttribute> rc = new ArrayList<SqlAttribute>();
    for (SqlAttribute item: sqlSqlAttributes)
    {
      if (item.getVisibleInForm())
      {
        rc.add(item);
      }
    }
    
    if (isOrderOnFormExists(rc)) 
    {
      DefaultCompare.sortTyped(rc, new ComparatorOnForm());
    }
    
    return rc;
  }
  
  public List<SqlAttribute> getVisibleFields() 
  {
    return getVisibleFields(getSqlAttributes().getValuesList());
  }
  
  public List<SqlAttribute> getVisibleFields(List<SqlAttribute> sqlSqlAttributes)
  {
    List<SqlAttribute> rc = new ArrayList<SqlAttribute>();
    for (SqlAttribute item: sqlSqlAttributes)
    {
      if (item.getVisibleInTable() || item.getVisibleInForm())
      {
        rc.add(item);
      }
    }
    return rc;
  }

  public void setFormTypeParameters(FormTypeParameters formTypeParameters)
  {
    this.formTypeParameters = formTypeParameters;
  }

  public FormTypeParameters getFormTypeParameters()
  {
    if (formTypeParameters == null) 
    {
      formTypeParameters = new FormTypeParameters();
    }
    return formTypeParameters;
  }
  
  public List<MainRecord> getMainRecordTableModelForLinkPortletsByChangeCurrentRow()
  {
    if (mainRecordTableModelForLinkPortletsByChangeCurrentRow == null) 
    {
      String currentId = CommonUtilsImpl.getInstance().getTaskFlowParameterId();
      MainRecord current = null;
      mainRecordTableModelForLinkPortletsByChangeCurrentRow = SerializationDriverBase.getInstance().getMainRecordList();
      for (MainRecord item : mainRecordTableModelForLinkPortletsByChangeCurrentRow) 
      {
        if (currentId.equals(item.getId())) 
        {
          current = item;
        }
        else 
        {
          item.setSelected(false);
          try 
          {
            Set<String> portletsIdToRefresh = getLinkByChangeCurrentRow().getPortletIdToRefresh();
            item.setSelected(portletsIdToRefresh.contains(item.getId()));  
          }
          catch(Exception ex) 
          {
            System.err.println("@Error getMainRecordTableModel : "+ex.getMessage());
          }
        }
      } // for
      if (current != null) 
      {
        mainRecordTableModelForLinkPortletsByChangeCurrentRow.remove(current);
      }
    }
    return mainRecordTableModelForLinkPortletsByChangeCurrentRow;
  }
  
  public LinkInfo getLinkByChangeCurrentRow()
  {
    if (linkByChangeCurrentRow == null) 
    {
      linkByChangeCurrentRow = new LinkInfo();
    }
    return linkByChangeCurrentRow;
  }
  public void setLinkByChangeCurrentRow(LinkInfo linkByChangeCurrentRow)
  {
    this.linkByChangeCurrentRow = linkByChangeCurrentRow;
  }

  public void setMainRecordTable2(RichTable mainRecordTable2)
  {
    this.mainRecordTable2 = mainRecordTable2;
  }

  public RichTable getMainRecordTable2()
  {
    return mainRecordTable2;
  }

  public void setCustErrorSqlMessages(MapWithOrderKeys<Integer, String> custErrorSqlMessages)
  {
    this.custErrorSqlMessages = custErrorSqlMessages;
  }

  public MapWithOrderKeys<Integer, String> getCustErrorSqlMessages()
  {
    if (custErrorSqlMessages == null) 
    {
      custErrorSqlMessages = new MapWithOrderKeys<Integer,String>();
    }
    return custErrorSqlMessages;
  }

  public void setCustErrorSqlDefaultMessage(String custErrorSqlDefaultMessage)
  {
    this.custErrorSqlDefaultMessage = custErrorSqlDefaultMessage;
  }

  public String getCustErrorSqlDefaultMessage()
  {
//    if (custErrorSqlDefaultMessage == null) 
//    {
//      custErrorSqlDefaultMessage = "Ошибка обращения к базе данных";
//    }
    return custErrorSqlDefaultMessage;
  }
  
  private boolean isOrderOnTableExists(List<SqlAttribute> list)
  {
    boolean rc = true;
    for (SqlAttribute item : list) 
    {
      if (item.getIndexTable() == null) 
      {
        rc = false;
        break;
      }
    }
    
    return rc;
  }
  
  private boolean isOrderOnFormExists(List<SqlAttribute> list)
  {
    boolean rc = true;
    for (SqlAttribute item : list) 
    {
      if (item.getIndexForm() == null) 
      {
        rc = false;
        break;
      }
    }
    
    return rc;
  }
  
  public boolean isTaskFlowInEditMode() 
  {
    boolean rc = false;
    rc = (ModelType.READ_WRITE.equals(getModelType()) && getDataChangeTypes() != null &&
         getDataChangeTypes().size() > 0);
    return rc;
  }
  
  public boolean isTaskFlowInsertAble() 
  {
    boolean rc = false;
    rc = (ModelType.READ_WRITE.equals(getModelType()) && getDataChangeTypes() != null &&
         getDataChangeTypes().contains(DataChangeType.INSERT));
    return rc;
  }
  
  public boolean isTaskFlowUpdateAble() 
  {
    boolean rc = false;
    rc = (ModelType.READ_WRITE.equals(getModelType()) && getDataChangeTypes() != null &&
         getDataChangeTypes().contains(DataChangeType.UPDATE));
    return rc;
  }
  public boolean isTaskFlowDeleteAble() 
  {
    boolean rc = false;
    rc = (ModelType.READ_WRITE.equals(getModelType()) && getDataChangeTypes() != null &&
         getDataChangeTypes().contains(DataChangeType.UPDATE));
    return rc;
  }
  
  public boolean isTaskFlowReadOnly() 
  {
    return (!isTaskFlowInEditMode());
  }
  
  //====================================================
  public class VisualControlMap extends HashMap<String,Boolean>
  {
    private static final long serialVersionUID = 1L;
    @Override
    public Boolean get(Object objName)
    {
      boolean rc = false;
      String name = (String) objName;
      if (currentSqlParameter != null)
      {
        try
        {
          rc = currentSqlParameter.getVisualControlInfo().getType().name().equals(name);
        }
        catch(Exception ex)
        {
          System.err.println("Ошибка 3 : "+ex.getMessage());
        }
      }
      return rc;
    }
    public Boolean put(String name, Boolean value)
    {
      try
      {
        if (value)
        {
          VisualControlForSqlParameterInfo info = currentSqlParameter.getVisualControlInfo();
          info.setType(VisualControlForSqlParameterType.valueOf(name));
        }
      }
      catch(Exception ex)
      {
        System.err.println("Ошибка 4 : "+ex.getMessage());
      }
      return value;
    }
  }
  private static class NameAndIndex
  {
    private String name = null;
    private int index = 0;
    NameAndIndex (String name, int index)
    {
      super();
      this.name = name;
      this.index = index;
    }
  }
  private static enum BuildSqlParametersStatus
  {
    SQL_NOT_VALID(null),
    NO_PARAMETERS(null),
    FOUND_NEW_PARAMETERS("Обнаружено ${count} новых параметров запроса. Настройте их.");
    private static final long serialVersionUID = 1L;
    private String message = null;
    private Integer count = null;


    private BuildSqlParametersStatus(String message)
    {
      this.message = message;
    }

    public String getMessage()
    {
      String rc = message;
      if (this.equals(FOUND_NEW_PARAMETERS))
      {
        if (count != null && count > 0)
        {
          rc = rc.replace("${count}", count+"");
        }
        else 
        {
          rc = null;
        }
      }
      return rc;
    }

    public void setCount(Integer count)
    {
      this.count = count;
    }

    public Integer getCount()
    {
      return count;
    }
  }
 
  private static enum BuildSqlAttributesStatus 
  {
    SQL_NOT_VALID(null),
    NO_PARAMETERS(null),
    FOUND_NEW_PARAMETERS("Обнаружено ${count} новых полей запроса. Настройте их.");
    private static final long serialVersionUID = 1L;
    private String message = null;
    private Integer count = null;


    private BuildSqlAttributesStatus(String message)
    {
      this.message = message;
    }

    public String getMessage()
    {
      String rc = message;
      if (this.equals(FOUND_NEW_PARAMETERS))
      {
        if (count != null && count > 0)
        {
          rc = rc.replace("${count}", count+"");
        }
        else 
        {
          rc = null;
        }
      }
      return rc;
    }

    public void setCount(Integer count)
    {
      this.count = count;
    }

    public Integer getCount()
    {
      return count;
    }
  }

  public class ComparatorOnTable implements Comparator<SqlAttribute>
  {
    public int compare(SqlAttribute a1, SqlAttribute a2)
    {
      return DefaultCompare.compareNormal(a1.getIndexTable(), a2.getIndexTable());
    }
  }
  
  public class ComparatorOnForm implements Comparator<SqlAttribute>
  {
    public int compare(SqlAttribute a1, SqlAttribute a2)
    {
      return DefaultCompare.compareNormal(a1.getIndexForm(), a2.getIndexForm());
    }
  }
}

