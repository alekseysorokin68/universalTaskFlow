package universal_taskflow.common.data;


import com.rcore.global.CollectionFacade;
import com.rcore.global.StringFunc;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import universal_taskflow.common.types.ConvertorType;
import universal_taskflow.common.types.DataChangeType;
import universal_taskflow.common.types.DisplayFormat;
import universal_taskflow.common.types.FilterType;
import universal_taskflow.common.types.HorizontalAlign;
import universal_taskflow.common.types.LabelAlign;
import universal_taskflow.common.types.ModelType;
import universal_taskflow.common.types.ValidatorType;
import universal_taskflow.common.types.VisualControlForSqlAttributeType;
import universal_taskflow.common.utils.CommonUtilsImpl;


@XStreamAlias("SqlAttribute")
public class SqlAttribute implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  //---
  private String  name        = null;
  private int     sqlType     = Types.VARCHAR; // java.sql.Types
  private String  sqlTypeName = "CHAR";
  private String  className   = null;
  private String  tableName   = null;
  private Boolean nullable    = true;
  private Boolean primaryKey  = false; //*
  private Integer length      = null;
  private Integer precision   = null;
  //-------------------------------------
  
  // Блок для генерации SQL
  //private Boolean readOnly = true;
  private boolean editableInInsert = false;
  private boolean editableInUpdate = false;
  
  private Serializable defaultValue = null; //*     Автоматическое значение для insert
  private Serializable updateValue = null;  // NEW! Автоматическое значение для корректируемой записи
  private boolean      inInsertSql = false; // NEW! Участие в insert
  private boolean      inUpdateSql = false; // NEW! Участие в update
  
  private Boolean visibleInTable = true; //*
  private Boolean visibleInForm = true; //*
  private String caption = null; //*
  //private transient boolean printCaptionInHeaderTable = true; // Чисто рабочее поле
  private String schortDesc = null; //*
  private DisplayFormat displayFormat = DisplayFormat.STRING; //*
  //---
  private VisualControlForSqlAttributeInfo visualControlTypeOnTableInfo = new VisualControlForSqlAttributeInfo(VisualControlForSqlAttributeType.OUTPUT_FORMATTED);
  private VisualControlForSqlAttributeInfo visualControlTypeOnFormInfo  = new VisualControlForSqlAttributeInfo(VisualControlForSqlAttributeType.INPUT);
  //----
  private String widthInTable = null; // Ширина в таблице
  private String heightInTable = null; //*
  private String widthInForm = null; // Ширина в форме

  // Расположение заголовка в таблице
  private HorizontalAlign headerAlign = HorizontalAlign.CENTER; //*
  // Выравнивание данных по полю в таблице
  private HorizontalAlign dataAlign = HorizontalAlign.LEFT; //*

  // Расположение метки на форме
  private LabelAlign labelAlign = LabelAlign.LEFT; //*

  // Перенос заголовка по словам
  private Boolean wrapEnabled = true; //*
  // Перенос данных по словам
  private Boolean wrapEnabledData = true; //*

  private Boolean filterAble = true; //*
  private Boolean sortAble = true; //*

  // Признак "замороженного" поля
  private Boolean frozen = false; //*
  
  private ValidatorInfo validatorInfo = new ValidatorInfo();
  private ConvertorInfo convertorInfoForTable = new ConvertorInfo();
  private ConvertorInfo convertorInfoForForm = new ConvertorInfo();

  // Цвет шрифта
  // private Color color = Color.decode("#000000"); // Черный цвет
  // Цвет фона
  //  private Color background = Color.decode("#FFFFFF"); // Белый цвет

  // МАРК, СДЕЛАЛ ТАК  ПОСКОЛЬКУ С ДЕФОЛТНЫМИ ЦВЕТАМИ  ТАБЛИЦА ВЫГЛЯДИТ УБОГО
  //private Color color = Color.decode("#000000");
  //private Color background = null;

  // Цвет в зависимости от условий
  // El-выражение вида:
  // #{row.<имя поля> == <литерал>}
  //private ValueByExpression colorByExpression = null;
  private String category = null;
  private String inlineStyleForHeaderInTable = "color:#000000;font-weight:bold";
  private String inlineStyleForDataInTable = "color:#000000";
  //=================================================
  //private transient RichPopup popupColorByExpression;
  //private transient RichTable popupTableForSetColorFont;
  //private transient List<Map<String, Object>> tableModelForColorFontByExpression = new ArrayList<Map<String, Object>>();
  
  private DropDownInfo dropDownInfo =  null;
  //=================================================
  private String sortProperty   = null;   // null - текущее поле
  private String filterProperty = null; // null - текущее поле
  private String filterType = null; 
  //=================================================
  private transient Map<String,Boolean> mapTypesForTable = new MapTypesForTable();
  private transient Map<String,Boolean> mapTypesForForm = new MapTypesForForm();
  //------
  private transient Map<Object,Integer> mapRowsInTable = null;
  
  private Integer indexTable = null;
  private Integer indexForm = null;
  //=================================================================
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
  }
  //=================================================================
  public SqlAttribute(ResultSetMetaData meta, 
                      int index,
                      ModelType modelType,
                      Set<DataChangeType> dataChangeTypes
                      )
    throws SQLException
  {
    super();
    setName(meta.getColumnName(index));
    validateByDb(meta,index);
    //---------------------------------------
    if (ModelType.READ_ONLY.equals(modelType))
    {
      setEditableInInsert(false);
      setEditableInUpdate(false);
    }
    setVisualControlTypeOnTableInfo(new VisualControlForSqlAttributeInfo(getVisualControlDefault()));
    setupInInsert_InUpdate(modelType, dataChangeTypes);
    //---
    indexTable = index * 100;
    indexForm = index * 100;
  }
  //=================================================
  public String getName()
  {
    return name;
  }

  public int getSqlType()
  {
    return sqlType;
  }

  public String getSqlTypeName()
  {
    return sqlTypeName;
  }

  public String getClassName()
  {
    return className;
  }

  public String getTableName()
  {
    return tableName;
  }

  public Boolean getNullable()
  {
    return nullable;
  }

  public Boolean getPrimaryKey()
  {
    return primaryKey;
  }

  public Integer getLength()
  {
    return length;
  }

  public Integer getPrecision()
  {
    return precision;
  }

  public void setEditableInInsert(boolean editableInInsert)
  {
    this.editableInInsert = editableInInsert;
  }

  public boolean isEditableInInsert()
  {
    return editableInInsert;
  }

  public void setEditableInUpdate(boolean editableInUpdate)
  {
    this.editableInUpdate = editableInUpdate;
  }

  public boolean isEditableInUpdate()
  {
    return editableInUpdate;
  }

  public void setDefaultValue(Serializable defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public Serializable getDefaultValue()
  {
    return defaultValue;
  }

  public void setUpdateValue(Serializable updateValue)
  {
    this.updateValue = updateValue;
  }

  public Serializable getUpdateValue()
  {
    return updateValue;
  }

  public void setInInsertSql(boolean inInsertSql)
  {
    this.inInsertSql = inInsertSql;
  }

  public boolean isInInsertSql()
  {
    return inInsertSql;
  }

  public void setInUpdateSql(boolean inUpdateSql)
  {
    this.inUpdateSql = inUpdateSql;
  }

  public boolean isInUpdateSql()
  {
    return inUpdateSql;
  }

  public void setVisibleInTable(Boolean visibleInTable)
  {
    this.visibleInTable = visibleInTable;
  }

  public Boolean getVisibleInTable()
  {
    return visibleInTable;
  }

  public void setVisibleInForm(Boolean visibleInForm)
  {
    this.visibleInForm = visibleInForm;
  }

  public Boolean getVisibleInForm()
  {
    return visibleInForm;
  }

  public void setCaption(String caption)
  {
    this.caption = caption;
  }

  public String getCaption()
  {
    return caption;
  }
  
  public String getResolvedCaption()
  {
    String rc = (String) CommonUtilsImpl.getInstance().getResolvedValue(this, "caption", name);
    return rc;
  }

  public void setSchortDesc(String schortDesc)
  {
    this.schortDesc = schortDesc;
  }

  public String getSchortDesc()
  {
    return schortDesc;
  }

  public void setDisplayFormat(DisplayFormat displayFormat)
  {
    this.displayFormat = displayFormat;
  }

  public DisplayFormat getDisplayFormat()
  {
    return displayFormat;
  }

  public void setWidthInTable(String width)
  {
    this.widthInTable = width;
  }

  public String getWidthInTable()
  {
    return widthInTable;
  }
  
  public String getColumnTableResolvedWidth()
  {
    // TODO
    String rc = getWidthInTable();
//    try
//    {
//      AdfTableWithoutBcBean bean = (AdfTableWithoutBcBean) JSFUtils.resolveExpression("#{pageFlowScope.AdfTableWithoutBcBean}");
//      if (bean != null)
//      {
//        AdminData ad = bean.getAdminData();
//        TableTypeParameters tableParameters = ad.getTableTypeParameters();
//        Boolean autoPersonalizableAble = tableParameters.getAutoPersonalizableAble();
//        if (autoPersonalizableAble == null)
//        {
//          autoPersonalizableAble = false;
//        }
//        if (autoPersonalizableAble)
//        {
//          Map<String,Object> widthsColumn = bean.getWidthTableColumnsFromUserParameters();
//          if (widthsColumn != null)
//          {
//            if (widthsColumn.containsKey(getName()))
//            {
//              rc = widthsColumn.get(getName()).toString();
//            }
//          }
//        }
//      }
//    }
//    catch(Exception ex)
//    {
//      ex.printStackTrace();
//    }
    return rc;
  }

  public void setHeightInTable(String height)
  {
    this.heightInTable = height;
  }

  public String getHeightInTable()
  {
    return heightInTable;
  }

  public void setWidthInForm(String widthInForm)
  {
    this.widthInForm = widthInForm;
  }

  public String getWidthInForm()
  {
    return widthInForm;
  }

  public void setHeaderAlign(HorizontalAlign headerAlign)
  {
    this.headerAlign = headerAlign;
  }

  public HorizontalAlign getHeaderAlign()
  {
    return headerAlign;
  }
  
  public HorizontalAlign getResolvedHeaderAlign()
  {
    Object rc = CommonUtilsImpl.getInstance().getResolvedValue(this, "headerAlign", name);
    if (rc != null) 
    {
      if (rc instanceof String) 
      {
        rc = HorizontalAlign.valueOf((String) rc);
      }
    }
    return (HorizontalAlign) rc;    
  }

  public void setLabelAlign(LabelAlign labelAlign)
  {
    this.labelAlign = labelAlign;
  }

  public LabelAlign getLabelAlign()
  {
    return labelAlign;
  }

  public void setWrapEnabled(Boolean wrapEnabled)
  {
    this.wrapEnabled = wrapEnabled;
  }

  public Boolean getWrapEnabled()
  {
    return wrapEnabled;
  }
  
  public Boolean getResolvedWrapEnabled()
  {
    Object rc = CommonUtilsImpl.getInstance().getResolvedValue(this, "wrapEnabled", name);
    if (rc == null) 
    {
      rc = false;
    }
    if (rc instanceof String) 
    {
      rc = Boolean.valueOf((String) rc);
    }
    return (Boolean) rc;
  }

  public void setWrapEnabledData(Boolean wrapEnabledData)
  {
    this.wrapEnabledData = wrapEnabledData;
  }

  public Boolean getWrapEnabledData()
  {
    return wrapEnabledData;
  }
  
  public Boolean getResolvedWrapEnabledData()
  {
    Object rc = CommonUtilsImpl.getInstance().getResolvedValue(this, "wrapEnabledData", name);
    if (rc == null) 
    {
      rc = false;
    }
    if (rc instanceof String) 
    {
      rc = Boolean.valueOf((String) rc);
    }
    return (Boolean) rc;
  }

  public void setDataAlign(HorizontalAlign dataAlign)
  {
    this.dataAlign = dataAlign;
  }

  public HorizontalAlign getDataAlign()
  {
    return dataAlign;
  }
  
  public HorizontalAlign getResolvedDataAlign()
  {
    Object rc = ( CommonUtilsImpl.getInstance().getResolvedValue(this, "dataAlign", name) );
    if (rc != null) 
    {
      if (rc instanceof String) 
      {
        rc = HorizontalAlign.valueOf((String) rc);
      }
    }
    return (HorizontalAlign) rc;    
  }

  public void setFilterAble(Boolean filterAble)
  {
    this.filterAble = filterAble;
  }

  public Boolean getFilterAble()
  {
    return filterAble;
  }
  
  public Boolean getResolvedFilterAble()
  {
    Object rc = CommonUtilsImpl.getInstance().getResolvedValue(this, "filterAble", name);
    if (rc == null) 
    {
      rc = false;
    }
    if (rc instanceof String) 
    {
      rc = Boolean.valueOf((String) rc);
    }
    return (Boolean) rc;
  }

  public void setSortAble(Boolean sortAble)
  {
    this.sortAble = sortAble;
  }

  public Boolean getSortAble()
  {
    return sortAble;
  }
  
  public Boolean getResolvedSortAble()
  {
    Object rc = CommonUtilsImpl.getInstance().getResolvedValue(this, "sortAble", name);
    if (rc == null) 
    {
      rc = false;
    }
    if (rc instanceof String) 
    {
      rc = Boolean.valueOf((String) rc);
    }
    return (Boolean) rc;
  }

  public void setFrozen(Boolean frozen)
  {
    this.frozen = frozen;
  }

  public Boolean getFrozen()
  {
    return frozen;
  }
  
  public Boolean getResolvedFrozen()
  {
    Object rc = CommonUtilsImpl.getInstance().getResolvedValue(this, "frozen", name);
    if (rc == null) 
    {
      rc = false;
    }
    if (rc instanceof String) 
    {
      rc = Boolean.valueOf((String) rc);
    }
    return (Boolean) rc;
  }

//  public void setColor(Color color)
//  {
//    this.color = color;
//  }
//
//  public Color getColor()
//  {
//    return color;
//  }
//
//  public void setBackground(Color background)
//  {
//    this.background = background;
//  }
//
//  public Color getBackground()
//  {
//    return background;
//  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public String getCategory()
  {
    return category;
  }
  
  public String getResolvedCategory()
  {
    String rc = (String)( CommonUtilsImpl.getInstance().getResolvedValue(this, "category", name) );
    return rc;
  }
  
  public String getVisualCategory()
  {
    String rc = category;
    if (StringFunc.isEmpty(rc))
    {
      rc = "<пусто>";
    }
    return rc;
  }

  public void setInlineStyleForDataInTable(String inlineStyleForDataInTable)
  {
    this.inlineStyleForDataInTable = inlineStyleForDataInTable;
  }

  public String getInlineStyleForDataInTable()
  {
    return inlineStyleForDataInTable;
  }
  
  public String getResolvedInlineStyleForDataInTable()
  {
    String rc = (String) CommonUtilsImpl.getInstance().getResolvedValue(this, "inlineStyleForDataInTable", name);
    return rc;
  }
  
  public Map<Map<String,Object>,Object> getResolvedMapInlineStyleForDataInTable() 
  {
    ResolvedMapByRow rc = new ResolvedMapByRow("inlineStyleForDataInTable");
    return rc;
  }
  
  public void setDropDownInfo(DropDownInfo dropDownInfo)
  {
    this.dropDownInfo = dropDownInfo;
  }

  public DropDownInfo getDropDownInfo()
  {
    return dropDownInfo;
  }

  public void setSortProperty(String sortProperty)
  {
    this.sortProperty = sortProperty;
  }

  public String getSortProperty()
  {
    return sortProperty;
  }
  
  public String getResolvedSortProperty()
  {
    String rc = (String) CommonUtilsImpl.getInstance().getResolvedValue(this, "sortProperty", name);
    return rc;
  }
  
  public String getNotNullSortProperty()
  {
    String rc = getResolvedSortProperty();
    if (rc == null)
    {
      rc = getName();
    }
    return rc;
  }
  
  public String getNotNullFilterProperty()
  {
    String rc = null;
    if (filterProperty == null)
    {
      rc = getName();
    }
    else
    {
      rc = filterProperty;
    }
    return rc;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setSqlType(int sqlType)
  {
    this.sqlType = sqlType;
  }

  public void setSqlTypeName(String sqlTypeName)
  {
    this.sqlTypeName = sqlTypeName;
  }

  public void setClassName(String className)
  {
    this.className = className;
  }

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

  public void setNullable(Boolean nullable)
  {
    this.nullable = nullable;
  }

  public void setPrimaryKey(Boolean primaryKey)
  {
    this.primaryKey = primaryKey;
  }

  public void setLength(Integer length)
  {
    this.length = length;
  }

  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }
  
  /**
   * Визуальный элемент управления по умолчанию,
   * в зависимости от типа.
   * @return
   */
  public VisualControlForSqlAttributeType getVisualControlDefault()
  {
    VisualControlForSqlAttributeType rc = VisualControlForSqlAttributeType.OUTPUT_FORMATTED;
    if (sqlType == Types.BIGINT)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.DECIMAL)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.DOUBLE)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.FLOAT)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.INTEGER)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.SMALLINT)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.NUMERIC)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.TINYINT)
    {
      rc = VisualControlForSqlAttributeType.INPUT_NUMBER;
    }
    else if (sqlType == Types.BOOLEAN)
    {
      rc = VisualControlForSqlAttributeType.CHECK_BOX;
    }
    else if (sqlType == Types.CHAR)
    {
      ;
    }
    else if (sqlType == Types.CLOB)
    {
      ;
    }
    else if (sqlType == Types.DATE)
    {
      rc = VisualControlForSqlAttributeType.DATE;
    }
    else if (sqlType == Types.TIME)
    {
      rc = VisualControlForSqlAttributeType.DATE;
    }
    else if (sqlType == Types.TIMESTAMP)
    {
      rc = VisualControlForSqlAttributeType.DATE;
    }
    else if (sqlType == Types.VARCHAR)
    {
      ;
    }
    else
    {
      ;
    }
    return rc;
  }
  public void setupInInsert_InUpdate(ModelType mt, Set<DataChangeType> dataChangeTypes)
  {
    //ModelType mt = ad.getModelType();
    //Set<DataChangeType> dataChangeTypes = ad.getDataChangeTypes();
    if (mt == null)
    {
      mt = ModelType.READ_ONLY;
    }
    if (dataChangeTypes == null)
    {
      dataChangeTypes = new HashSet<DataChangeType>();
    }
    //-----------------------------
    if (ModelType.READ_ONLY.equals(mt))
    {
      inInsertSql = false;
      inUpdateSql = false;
    }
    else
    {
      // INSERT:
      boolean isPortletInsertable =
        dataChangeTypes.contains(DataChangeType.INSERT);
      boolean isInsertAutoValue =
        (!StringFunc.isEmpty((String) defaultValue));
      if (!editableInInsert && isPortletInsertable && isInsertAutoValue)
      {
        inInsertSql = true;
      }
      else if (!editableInInsert && isPortletInsertable && isInsertAutoValue)
      {
        inInsertSql = true;
      }
      else if (!editableInInsert && !isPortletInsertable && isInsertAutoValue)
      {
        inInsertSql = true;
      }
      else if (editableInInsert && !isPortletInsertable && isInsertAutoValue)
      {
        inInsertSql = true;
      }
      //---
      else if (!editableInInsert && isPortletInsertable && !isInsertAutoValue)
      {
        inInsertSql = false;
      }
      else if (editableInInsert && isPortletInsertable && !isInsertAutoValue)
      {
        inInsertSql = true;
      }
      else if (!editableInInsert && !isPortletInsertable && !isInsertAutoValue)
      {
        inInsertSql = false;
      }
      else if (editableInInsert && !isPortletInsertable && !isInsertAutoValue)
      {
        inInsertSql = false;
      }

      // UPDATE:
      boolean isPortletUpdateAble =
        dataChangeTypes.contains(DataChangeType.UPDATE);
      boolean isUpdateAutoValue =
        (!StringFunc.isEmpty((String) updateValue));
      if (!editableInUpdate && isPortletUpdateAble && isUpdateAutoValue)
      {
        inUpdateSql = true;
      }
      else if (editableInUpdate && isPortletUpdateAble && isUpdateAutoValue)
      {
        inUpdateSql = true;
      }
      else if (!editableInUpdate && !isPortletUpdateAble && isUpdateAutoValue)
      {
        inUpdateSql = true;
      }
      else if (editableInUpdate && !isPortletUpdateAble && isUpdateAutoValue)
      {
        inUpdateSql = true;
      }
      //---
      else if (!editableInUpdate && isPortletUpdateAble && !isUpdateAutoValue)
      {
        inUpdateSql = false;
      }
      else if (editableInUpdate && isPortletUpdateAble && !isUpdateAutoValue)
      {
        inUpdateSql = true;
      }
      else if (!editableInUpdate && !isPortletUpdateAble && !isUpdateAutoValue)
      {
        inUpdateSql = false;
      }
      else if (editableInUpdate && !isPortletUpdateAble && !isUpdateAutoValue)
      {
        inUpdateSql = false;
      }
    }
    return;
  }

  public void validateByDb(ResultSetMetaData meta)
    throws SQLException
  {
    int index = getIndex(meta);
    validateByDb(meta, index);
  }
  
  private void validateByDb(ResultSetMetaData meta, int index)
    throws SQLException
  {
    setSqlType(meta.getColumnType(index));
    setSqlTypeName(meta.getColumnTypeName(index));
    setClassName(meta.getColumnClassName(index));
    setTableName(meta.getTableName(index));
    setNullable((meta.isNullable(index) == ResultSetMetaData.columnNullable));
    setEditableInInsert(!meta.isReadOnly(index));
    setEditableInUpdate(!meta.isReadOnly(index));
    int columnSize = meta.getColumnDisplaySize(index);
    setLength(columnSize);
    setPrecision(meta.getPrecision(index));
  }

  private int getIndex(ResultSetMetaData meta)
    throws SQLException
  {
    int index = -1;
    int len = meta.getColumnCount();
    for (int i = 1; i <= len; i++) 
    {
      if (meta.getColumnName(i).equals(getName())) 
      {
        index = i;
        break;
      }
    }
    return index;
  }

  public void setVisualControlTypeOnTableInfo(VisualControlForSqlAttributeInfo visualControlTypeOnTableInfo)
  {
    this.visualControlTypeOnTableInfo = visualControlTypeOnTableInfo;
  }

  public VisualControlForSqlAttributeInfo getVisualControlTypeOnTableInfo()
  {
    return visualControlTypeOnTableInfo;
  }

  public void setVisualControlTypeOnFormInfo(VisualControlForSqlAttributeInfo visualControlTypeOnFormInfo)
  {
    this.visualControlTypeOnFormInfo = visualControlTypeOnFormInfo;
  }

  public VisualControlForSqlAttributeInfo getVisualControlTypeOnFormInfo()
  {
    if (visualControlTypeOnFormInfo == null) 
    {
      visualControlTypeOnFormInfo = new VisualControlForSqlAttributeInfo(VisualControlForSqlAttributeType.INPUT);
    }
    return visualControlTypeOnFormInfo;
  }
  //=====================================
  public void setInlineStyleForHeaderInTable(String inlineStyleForHeaderInTable)
  {
    this.inlineStyleForHeaderInTable = inlineStyleForHeaderInTable;
  }

  public String getInlineStyleForHeaderInTable()
  {
    return inlineStyleForHeaderInTable;
  }
  
  public String getResolvedInlineStyleForHeaderInTable()
  {
    String rc = (String) CommonUtilsImpl.getInstance().getResolvedValue(this, "inlineStyleForHeaderInTable", name);
    return rc;
  }

  public void setValidatorInfo(ValidatorInfo validatorInfo)
  {
    this.validatorInfo = validatorInfo;
  }

  public ValidatorInfo getValidatorInfo()
  {
    if (validatorInfo == null) 
    {
      validatorInfo = new ValidatorInfo();
    }
    return validatorInfo;
  }

  public void setConvertorInfoForTable(ConvertorInfo convertorInfoForTable)
  {
    this.convertorInfoForTable = convertorInfoForTable;
  }

  public ConvertorInfo getConvertorInfoForTable()
  {
    if (convertorInfoForTable == null) 
    {
      convertorInfoForTable = new ConvertorInfo();
    }
    return convertorInfoForTable;
  }

  public void setConvertorInfoForForm(ConvertorInfo convertorInfoForForm)
  {
    this.convertorInfoForForm = convertorInfoForForm;
  }

  public ConvertorInfo getConvertorInfoForForm()
  {
    if (convertorInfoForForm == null) 
    {
      convertorInfoForForm = new ConvertorInfo();
    }
    return convertorInfoForForm;
  }

  public List<SelectItem> getConvertorSelectItemsInTable()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    ConvertorType[] convertorTypes = visualControlTypeOnTableInfo.getType().getConvertors();
    //ConvertorType currentConvertor = getConvertorInfoForTable().getType();
    // TODO
    rc.add(new SelectItem(ConvertorType.convertEmpty,
                          ConvertorType.convertEmpty.getTitle(),
                          ConvertorType.convertEmpty.getTitle()
                         )
          );
    for (int i=0; i < convertorTypes.length; i++) 
    {
      ConvertorType itemConvertorType = convertorTypes[i];
      if (itemConvertorType != null)
      {
        rc.add(new SelectItem(itemConvertorType,
                              itemConvertorType.getTitle(),
                              itemConvertorType.getTitle()
                             )
              );
      }
    }
    return rc;
  }
  
  public List<SelectItem> getConvertorSelectItemsInForm()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    ConvertorType[] convertorTypes = visualControlTypeOnFormInfo.getType().getConvertors();
    rc.add(new SelectItem(ConvertorType.convertEmpty,
                          ConvertorType.convertEmpty.getTitle(),
                          ConvertorType.convertEmpty.getTitle()
                         )
          );
    for (int i=0; i < convertorTypes.length; i++) 
    {
      ConvertorType itemConvertorType = convertorTypes[i];
      if (itemConvertorType != null) 
      {
        rc.add(new SelectItem(itemConvertorType,
                              itemConvertorType.getTitle(),
                              itemConvertorType.getTitle()
                             )
              );
      }
    }
    return rc;
  }
  
  public List<SelectItem> getValidatorSelectItems() 
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    ValidatorType[] validatorTypes = visualControlTypeOnFormInfo.getType().getValidators();
    rc.add(new SelectItem(ValidatorType.validateEmpty,
                          ValidatorType.validateEmpty.getTitle(),
                          ValidatorType.validateEmpty.getTitle()
                         )
          );
    for (int i=0; i < validatorTypes.length; i++) 
    {
      ValidatorType itemValidatorType = validatorTypes[i];
      if (itemValidatorType != null) 
      {
        rc.add(new SelectItem(itemValidatorType,
                              itemValidatorType.getTitle(),
                              itemValidatorType.getTitle()
                             )
              );
      }
    }
    return rc;
  }
  //================================
  public Map<String, Boolean> getMapTypesForTable()
  {
    if (mapTypesForTable == null) mapTypesForTable = new MapTypesForTable();
    return mapTypesForTable;
  }

  public Map<String, Boolean> getMapTypesForForm()
  {
    if (mapTypesForForm == null) mapTypesForForm = new MapTypesForForm();
    return mapTypesForForm;
  }

  public void setFilterProperty(String filterProperty)
  {
    this.filterProperty = filterProperty;
  }

  public String getFilterProperty()
  {
    return filterProperty;
  }

  public void setFilterType(String filterType)
  {
    this.filterType = filterType;
  }

  public String getFilterType()
  {
    if (filterType == null) 
    {
      filterType = FilterType.INPUT_TEXT.name();
      if       ( sqlType == Types.DATE) {filterType = FilterType.INPUT_DATE.name();}
      else if  ( sqlType == Types.TIME) {filterType = FilterType.INPUT_TIME.name();}
      else if  ( sqlType == Types.TIMESTAMP) {filterType = FilterType.INPUT_DATE_TIME.name();}
    }
    return filterType;
  }
  
  public Map<Object,Integer> getRowsInTable()
  {
    if (mapRowsInTable == null)
    {
      mapRowsInTable = new MapRowsInTable();
    }
    return mapRowsInTable;
  }

  public void setIndexTable(Integer indexTable)
  {
    this.indexTable = indexTable;
  }

  public Integer getIndexTable()
  {
    return indexTable;
  }

  public void setIndexForm(Integer indexForm)
  {
    this.indexForm = indexForm;
  }

  public Integer getIndexForm()
  {
    return indexForm;
  }

  //============================================================================
  public class MapRowsInTable extends HashMap<Object,Integer>
  {
    private static final long serialVersionUID = 1L;
    @Override
    public Integer get(Object key)
    {
      int rc = 1;
      if (key != null)
      {
        String str = key.toString();
        rc = StringFunc.countLines(str);
        //---------------
        if (rc < 1)
        {
          rc = 1;
        }
      }
      return rc;
    }
  }
  
  public class ResolvedMapByRow extends HashMap<Map<String,Object>,Object> 
  {
    private static final long serialVersionUID = 1L;
    private String propertyName = null;

    private ResolvedMapByRow(String propertyName) 
    {
      super();
      this.propertyName = propertyName;
    }
    @Override
    public Object get(Object rowObj) 
    {
      String rc = (String) CommonUtilsImpl.getInstance().getResolvedValue(
          SqlAttribute.this, 
          SqlAttribute.class, 
          propertyName, 
          name, 
          (Map<String,Object>)rowObj);
      return rc; 
    }
  }
  
  public class MapTypesForTable extends HashMap<String,Boolean>
  {
    private static final long serialVersionUID = 1L;
    @Override
    public Boolean get(Object obj) 
    {
      Boolean rc = false;
      String key = (String) obj;
      rc = (getVisualControlTypeOnTableInfo().getType().name().equals(key));
      return rc;
    }
    
    @Override
    public Boolean put(String key, Boolean value) 
    {
      if (value != null && value) 
      {
        getVisualControlTypeOnTableInfo().setType(VisualControlForSqlAttributeType.valueOf(key));
        // А теперь возможно нужно изменить текущий конвертор, если он не входит в список допустимых
        // для нового элемента управления
        ConvertorType currentConvertor = getConvertorInfoForTable().getType();
        ConvertorType[] avalableConvertors = getVisualControlTypeOnTableInfo().getType().getConvertors();
        if (
            (avalableConvertors != null) && (avalableConvertors.length > 0) &&
            (!CollectionFacade.contains(avalableConvertors, currentConvertor)) 
           ) 
        {
          getConvertorInfoForTable().setType(avalableConvertors[0]);
        }
        
      }
      return value;
    }
  }
  
  public class MapTypesForForm extends HashMap<String,Boolean>
  {
    private static final long serialVersionUID = 1L;
    @Override
    public Boolean get(Object obj) 
    {
      Boolean rc = false;
      String key = (String) obj;
      rc = (getVisualControlTypeOnFormInfo().getType().name().equals(key));
      return rc;
    }
    
    @Override
    public Boolean put(String key, Boolean value) 
    {
      if (value != null && value) 
      {
        getVisualControlTypeOnFormInfo().setType(VisualControlForSqlAttributeType.valueOf(key));
        // А теперь возможно нужно изменить текущий конвертор, если он не входит в список допустимых
        // для нового элемента управления
        ConvertorType currentConvertor = getConvertorInfoForForm().getType();
        ConvertorType[] avalableConvertors = getVisualControlTypeOnFormInfo().getType().getConvertors();
        if (
            (avalableConvertors != null) && (avalableConvertors.length > 0) &&
            (!CollectionFacade.contains(avalableConvertors, currentConvertor)) 
           ) 
        {
          getConvertorInfoForForm().setType(avalableConvertors[0]);
        }
        
      }
      return value;
    }
  }
  
}
