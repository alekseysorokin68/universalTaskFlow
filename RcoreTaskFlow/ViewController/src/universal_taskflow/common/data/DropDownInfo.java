package universal_taskflow.common.data;


import com.rcore.global.MapWithOrderKeys;
import com.rcore.global.StringFunc;
import com.rcore.model.jdbc.FieldInfo;
import com.rcore.model.jdbc.SqlParser;
import com.rcore.utils.table.TableModelesIntegrator;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.types.ParameterAttributeType;
import universal_taskflow.common.utils.DataSourceInfo;


public class DropDownInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  //----
  private boolean combobox = true; // Признак комбобокса или таблицы
  private DataSourceInfo dataSourceInfo = null; // Понятно
  private String sql = null; // Понятно
  private MapWithOrderKeys<String,ParameterInfo> parameters = null; // Параметры запроса
  // Для таблицы:
  private MapWithOrderKeys<String,String> fieldCaptions = new MapWithOrderKeys<String,String>();

  private String fieldValue = null;    // поле - значение
  private String fieldCaption = null;  // поле - для отображения
  //---
  //private transient DataSourceType oldDataSourceType = null;
  private transient DataSourceInfo oldDataSourceInfo = null;
  private transient String         oldSql            = null;
  private transient Boolean        oldValidateResult = null;
  private transient String messageTestType = null;
  private transient String messageTestMessage = null;
  private transient Map<ParameterAttributeType,String> facetNameForParameterValue = new FacetNameForParameterValueMap();
  //=============== Дальше все для view : ==================================
  // Данные из базы по запросу
  private transient Map<Object,List<SelectItem>> selectItemsByMapData = null;
  // Мапировка полей и их значений на форме (параметров или при редактировании)
  private transient Map<Object,Object> mapAttributesValue = null;
  // Следующая конструкция для класса (от HashMap), которому в get передаются все атрибуты,
  // а он, строит TableModelesIntegrator
  private transient Map<Map<String,Object>,TableModelesIntegrator> mapAttrsTableModelesIntegrator = null;
  //===============================================================
  public DropDownInfo()
  {
    super();
  }
  //----------------------------------------
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
    ;
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
    //..System.out.println("@xstreamAfterUnMarshall.fieldCaption="+fieldCaption);
  }
  //----------------------------------------

  public void setCombobox(boolean combobox)
  {
    this.combobox = combobox;
  }

  public boolean isCombobox()
  {
    return combobox;
  }

  public List<RowField> getTableModelFields()
  {
    List<RowField> rc = new ArrayList<RowField>();
    if (fieldCaptions == null || fieldCaptions.isEmpty())
    {
      return rc;
    }
    List<String> names = fieldCaptions.getKeysList();
    for (String name: names)
    {
      RowField rowField = new RowField(name,this.fieldCaptions);
      rc.add(rowField);
    }
    return rc;
  }

  public List<SelectItem> getSelectItemsFields()
  {
    List<SelectItem> rc = null;
    if (fieldCaptions == null || fieldCaptions.isEmpty())
    {
      return rc;
    }
    List<String> listNames = fieldCaptions.getKeysList();
    rc = new ArrayList<SelectItem>();
    for (String name : listNames)
    {
      rc.add(new SelectItem(name,name));
    }
    return rc;
  }

  public List<RowParameter> getTableModelParameters()
  {
    List<RowParameter> rc = new ArrayList<RowParameter>();
    if (!isParametersFound())
    {
      return rc;
    }
    List<String> names = parameters.getKeysList();
    for (String name: names)
    {
      RowParameter rowParameters = new RowParameter(name,this.parameters);
      rc.add(rowParameters);
    }
    return rc;
  }

  public void setMessageTestType(String messageTestType)
  {
    this.messageTestType = messageTestType;
  }

  public String getMessageTestType()
  {
    return messageTestType;
  }

  public void setMessageTestMessage(String messageTestMessage)
  {
    this.messageTestMessage = messageTestMessage;
  }

  public String getMessageTestMessage()
  {
    return messageTestMessage;
  }

  public boolean isTabFieldsDisabled()
  {
    boolean rc = false;
    rc = !validate();
    if (!rc)
    {
      return (fieldCaptions == null || fieldCaptions.isEmpty());
    }
    return rc;
  }

  public boolean isTabParametersDisabled()
  {
    boolean rc = false;
    rc = !validate();
    if (!rc)
    {
      rc = (parameters == null || parameters.isEmpty());
    }
    return rc;
  }

  public boolean isParametersFound()
  {
    return (parameters != null && parameters.size() > 0);
  }

  public void setDataSourceInfo(DataSourceInfo dataSourceInfo)
  {
    this.dataSourceInfo = dataSourceInfo;
  }

  public DataSourceInfo getDataSourceInfo()
  {
    if (dataSourceInfo == null)
    {
      List<DataSourceInfo> list = TaskFlowSystemProperties.getInstance().getListDatasource();
      dataSourceInfo = list.get(0);
    }
    return dataSourceInfo;
  }

  public void setParameters(MapWithOrderKeys<String, DropDownInfo.ParameterInfo> parameters)
  {
    this.parameters = parameters;
  }

  public MapWithOrderKeys<String, DropDownInfo.ParameterInfo> getParameters()
  {
    return parameters;
  }

  public void setFieldCaptions(MapWithOrderKeys<String, String> fieldCaptions)
  {
    this.fieldCaptions = fieldCaptions;
  }

  public MapWithOrderKeys<String, String> getFieldCaptions()
  {
    return fieldCaptions;
  }

  public void setFieldValue(String fieldValue)
  {
    this.fieldValue = fieldValue;
  }

  public String getFieldValue()
  {
    return fieldValue;
  }

  public void setFieldCaption(String fieldCaption)
  {
    this.fieldCaption = fieldCaption;
  }

  public String getFieldCaption()
  {
    return fieldCaption;
  }

  public void setTable(boolean table)
  {
    this.combobox = !table;
  }

  public boolean isTable()
  {
    return !combobox;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
    validate();
  }

  public String getSql()
  {
    return sql;
  }

  public String testSqlAction()
  {
    validate();
    return null;
  }

  private boolean validate()
  {
    boolean rc = false;
    if (oldValidateResult == null)
    {
      rc = validateImpl();
    }
    else
    {
      if (oldDataSourceInfo == null || oldSql == null)
      {
        rc = validateImpl();
      }
      else // oldValidateResult != null && oldDataSourceType != null && oldSql != null
      {
        if (oldDataSourceInfo.equals(dataSourceInfo) && oldSql.equals(sql))
        {
          rc = oldValidateResult;
        }
        else
        {
          rc = validateImpl();
        }
      }
    }
    //---
    oldDataSourceInfo = dataSourceInfo;
    oldSql = sql;
    oldValidateResult = rc;
    return rc;
  }

  private String testSql(String sql)
  {
    String rc = null;
    if (StringFunc.isEmpty(sql))
    {
      rc = "Ошибка - Запрос пустой";
    }
    else
    {
      DataSourceInfo type = getDataSourceInfo();
      Connection con = type.getConnection();
      try
      {
        rc = SqlParser.getErrorQueryWithCheckNames(con,sql);
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
            con.close();
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

  private boolean validateImpl()
  {
    boolean rc = false;
    String err = testSql(sql);
    if (err == null)
    {
      rc = true;
      messageTestMessage = "Запрос корректный";
      messageTestType = "info";
      try
      {
        validateGoodRequest();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      rc = false;
      messageTestMessage = err;
      messageTestType = "error";
    }
    return rc;
  }

  private void validateGoodRequest() throws SQLException, CloneNotSupportedException
  {
    Connection con = dataSourceInfo.getConnection();
    FieldInfo fieldInfoList[] = null;
    try
    {
      fieldInfoList = SqlParser.getFieldsInfo(sql,con);
    }
    finally
    {
      con.close();
    }
    if (indexOf(fieldInfoList, fieldValue) < 0)
    {
      fieldValue = null;
      if (fieldInfoList != null && fieldInfoList.length > 0)
      {
        fieldValue = fieldInfoList[0].getColumnName();
      }
    }
    if (indexOf(fieldInfoList, fieldCaption) < 0)
    {
      fieldCaption = null;
      if (fieldInfoList != null && fieldInfoList.length > 0)
      {
        fieldCaption = fieldInfoList[0].getColumnName();
      }
    }

    HashMap<String,String> oldFieldCaptions = fieldCaptions;
    if (oldFieldCaptions == null)
    {
      oldFieldCaptions = new HashMap<String,String>();
    }
    else
    {
      oldFieldCaptions = (HashMap<String,String>) oldFieldCaptions.clone();
    }
    fieldCaptions.clear();
    for (FieldInfo item : fieldInfoList)
    {
      String value = oldFieldCaptions.get(item.getColumnName());
      if (value == null)
      {
        value = item.getColumnName();
      }
      fieldCaptions.put(item.getColumnName(), value);
    }
    //----------------
    List<String> params = SqlParser.getNamedParametersList(sql);
    Map<String,ParameterInfo> oldParameters = this.parameters;
  //    if (oldParameters != null)
  //    {
  //      oldParameters = (Map<String, Serializable>)(oldParameters.clone());
  //    }
    parameters = new MapWithOrderKeys<String,ParameterInfo>();
    for (String param : params)
    {
      ParameterInfo value = new ParameterInfo(null);
      if (oldParameters != null)
      {
        if (oldParameters.containsKey(param))
        {
          value = oldParameters.get(param);
        }
      }
      parameters.put(param, value);
    }
  }

  private static int indexOf(FieldInfo[] fields, String fieldName)
  {
    int rc = -1;
    if (fieldName != null) 
    {
      fieldName = fieldName.toUpperCase();
      for (int i=0; i < fields.length; i++)
      {
        FieldInfo item = fields[i];
        if (item.getColumnName().toUpperCase().equals(fieldName))
        {
          rc = i;
          break;
        }
      }
    }
    return rc;
  }

  public Map<ParameterAttributeType, String> getFacetNameForParameterValue()
  {
    if (facetNameForParameterValue == null) facetNameForParameterValue = new FacetNameForParameterValueMap();
    return facetNameForParameterValue;
  }

  /*
  public void setDataSourceInfo(DataSourceInfo dataSourceInfo)
  {
    this.dataSourceInfo = dataSourceInfo;
  }
  private boolean validate()
    {
      boolean rc = false;
      if (oldValidateResult == null)
      {
        rc = validateImpl();
      }
      else
      {
        if (oldDataSourceInfo == null || oldSql == null)
        {
          rc = validateImpl();
        }
        else // oldValidateResult != null && oldDataSourceType != null && oldSql != null
        {
          if (oldDataSourceInfo.equals(dataSourceInfo) && oldSql.equals(sql))
          {
            rc = oldValidateResult;
          }
          else
          {
            rc = validateImpl();
          }
        }
      }
      //--------------------------------
      oldDataSourceInfo = dataSourceInfo;
      oldSql = sql;
      oldValidateResult = rc;
      return rc;
    }

    private boolean validateImpl()
    {
      boolean rc = false;
      String err = testSql(sql);
      if (err == null)
      {
        rc = true;
        messageTestMessage = "Запрос корректный";
        messageTestType = "info";
        try
        {
          validateGoodRequest();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      else
      {
        rc = false;
        messageTestMessage = err;
        messageTestType = "error";
      }
      return rc;
    }

  private void validateGoodRequest() throws SQLException, CloneNotSupportedException
  {
    Connection con = dataSourceInfo.getConnection();
    FieldInfo[] fieldInfoList = null;
    try
    {
      fieldInfoList = SqlParser.getFieldsInfo(sql,con);
    }
    finally
    {
      con.close();
    }
    List<String> fieldNames = new ArrayList<String>();
    for (String item : fieldInfoList)
    {
      fieldNames.add(item);
    }
    if (!fieldNames.contains(fieldValue))
    {
      fieldValue = null;
      if (fieldNames.size() > 0)
      {
        fieldValue = fieldNames.get(0);
      }
    }

    if (!fieldNames.contains(fieldCaption))
    {
      fieldCaption = null;
      if (fieldNames.size() > 0)
      {
        fieldCaption = fieldNames.get(0);
      }
    }

    HashMap<String,String> oldFieldCaptions = fieldCaptions;
    if (oldFieldCaptions == null)
    {
      oldFieldCaptions = new HashMap<String,String>();
    }
    else
    {
      oldFieldCaptions = (HashMap<String,String>) oldFieldCaptions.clone();
    }
    fieldCaptions.clear();
    for (String name : fieldNames)
    {
      String value = oldFieldCaptions.get(name);
      if (value == null)
      {
        value = name;
      }
      fieldCaptions.put(name, value);
    }
    //----------------
    List<String> params = SqlParser.getNamedParametersList(sql);
    Map<String,ParameterInfo> oldParameters = this.parameters;
    parameters = new MapWithOrderKeys<String,ParameterInfo>();
    for (String param : params)
    {
      ParameterInfo value = new ParameterInfo(null);
      if (oldParameters != null)
      {
        if (oldParameters.containsKey(param))
        {
          value = oldParameters.get(param);
        }
      }
      parameters.put(param, value);
    }
  }

  private String testSql(String sql)
  {
    String rc = null;
    if (StringFunc.isEmpty(sql))
    {
      rc = "Ошибка - Запрос пустой";
    }
    else
    {
      DataSourceInfo type = getDataSourceInfo();
      Connection con = type.getConnection();
      try
      {
        List<String> parameterList = SqlParser.getNamedParametersList(sql);
        Map<String,Object> mapParams = new HashMap<String,Object>();
        for (String item : parameterList)
        {
          mapParams.put(item, null);
        }
        rc = SqlParser.getErrorQueryWithCheckNames(con, sql, mapParams);
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
            con.close();
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

  public void setCombobox(boolean combobox)
  {
    this.combobox = combobox;
  }

  public boolean isCombobox()
  {
    return combobox;
  }


  // Вычисление значения параметров:
  public Serializable resolveParameterValue(String paramName,
                                            Map<String,Object> mapRow,
                                            Map<String, SqlParameter> parameters,
                                            Map<String, Serializable> sqlParameterCurrentValues)
  {
    ParameterInfo value = this.parameters.get(paramName);
    if (value == null)
    {
      System.err.println("@1="+this.parameters);
      System.err.println("@2="+value);
    }
    return resolveValue(value.getValue(), value.getType(), mapRow, parameters, sqlParameterCurrentValues);
  }

  public static Serializable resolveValue(String value,
                                          ParameterAttributeType type,
                                          Map<String,Object> mapRow,
                                          Map<String, SqlParameter> parameters,
                                          Map<String, Serializable> sqlParameterCurrentValues
                                          )
  {
    Serializable rc = null;
    if (value == null)
    {
      ;
    }
    else if (value.startsWith("@{"))
    {
      value = value.substring(2, value.length()-1);
      //rc = SqlAttribute.resolveSpecialValue(value, mapRow, parameters,sqlParameterCurrentValues);
      // TODO
    }
    else if (value.startsWith("#{"))
    {
      //rc = (Serializable)(JSFUtils.resolveExpression(value));
      // TODO
    }
    else
    {
      if (!ParameterAttributeType.DATE.equals(type))
      {
        rc = value;
      }
      else {
        //rc = SqlParameter.getJavaUtilDate(value);
        // TODO
      }
    }
    return rc;
  }
  */
  //========================================
  //*
  //========================================
  public static class RowParameter
  {
    private String name;
    private MapWithOrderKeys<String,ParameterInfo> parameters;

    private RowParameter(String name, MapWithOrderKeys<String,ParameterInfo> parameters)
    {
      super();
      this.name = name;
      this.parameters = parameters;
    }

    public String getName()
    {
      return name;
    }

    public void setValue(Serializable value)
    {
      ParameterInfo info = parameters.get(name);
      info.setValue(value);
    }

    public Serializable getValue()
    {
      ParameterInfo info = parameters.get(name);
      return info.getValue();
    }

    public ParameterAttributeType getType()
    {
      ParameterInfo info = parameters.get(name);
      return info.getType();
    }

    public void setType(ParameterAttributeType type)
    {
      ParameterInfo info = parameters.get(name);
      info.setType(type);
    }


  }

  public static class RowField
  {
    private String name;
    private MapWithOrderKeys<String, String> fieldCaptions;

    private RowField(String name, 
                     MapWithOrderKeys<String,String> fieldCaptions)
    {
      super();
      this.name = name;
      this.fieldCaptions = fieldCaptions;
    }

    public String getName()
    {
      return name;
    }

    public void setCaption(String caption)
    {
      fieldCaptions.put(name, caption);
    }

    public String getCaption()
    {
      return fieldCaptions.get(name);
    }
  }
  
  public static class ParameterInfo implements Serializable
  {
    @SuppressWarnings("compatibility:4929239047012199806")
    private static final long serialVersionUID = 1L;

    //private String value = null;
    private Serializable value = null;
    private ParameterAttributeType type = ParameterAttributeType.VARCHAR;
    private ParameterInfo(Serializable value)
    {
      super();
      this.value = value;
    }

    public void setValue(Serializable value)
    {
      this.value = value;
    }

    public Serializable getValue()
    {
      return value;
    }

    public void setType(ParameterAttributeType type)
    {
      this.type = type;
    }

    public ParameterAttributeType getType()
    {
      return type;
    }
  }

  private class FacetNameForParameterValueMap extends HashMap<ParameterAttributeType,String>
  {
    @Override
    public String get(Object objKey) 
    {
      ParameterAttributeType type = (ParameterAttributeType) objKey;
      String rc = "INPUT";
      if (type != null) 
      {
        if      (type.equals(ParameterAttributeType.DATE)) 
        {
          rc = "DATE";
        }
        else if (type.equals(ParameterAttributeType.TIME)) 
        {
          rc = "TIME";
        }
        else if (type.equals(ParameterAttributeType.TIMESTAMP)) 
        {
          rc = "TIMESTAMP";
        }
      }
      return rc;    
    }
    private static final long serialVersionUID = 1L;
  }
}
