package universal_taskflow.common.data.taskflow_type_parameters.on_sql;


import com.rcore.global.MapWithOrderKeys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import universal_taskflow.common.data.SqlParameter;
import universal_taskflow.common.data.UserRecord;
import universal_taskflow.common.types.EventType;
import universal_taskflow.common.types.ReadFromDbType;
import universal_taskflow.common.types.TableContentDelivery;


public class TableAdfParameters extends TaskFlowParametersBaseOnSql
{
  private static final long serialVersionUID = 1L;
  

  public TableAdfParameters()
  {
    super();
  }
  //-----
  private String columnStreching = "multiple";

  // Интревал для перемены цвета строк
  private int intervalForChangeColourOfLines = 1;

  // Возможность изменения ширины колонок
  private Boolean possibilityChangeWidthColumns = false;

  // Возможность перестановки колонок. Не обслуживается визуально
  private Boolean possibilityShiftColumns = true;

  // Стиль таблицы (CSS)
  private String inlineStyle = null;
  // Стиль контейнера таблицы (CSS)
  private String containerInlineStyle = null;

  // Всплывающее описание
  private String shortDesc = null;

  // Ширина таблицы
  //private String width = "";

  // Высота таблицы
  //private String height = null;

  // Наличие "нашего" тулбар-а
  private Boolean toolBarExists = false;

  private Boolean sortAble = false;
  private Boolean filterAble = false;

  // Автоматическая кастомизация
  private Boolean autoPersonalizableAble = false;

  // Выделение текущей строки
  private Boolean rowSelectAble = false;
  
  // Текст при отсутствии данных
  private String emptyText = "Нет данных";
  
  // Отображать заголовки колонок
  private Boolean displayHeadersOfColumns = true;
  
  // Кол-во строк в буфере таблицы
  private Integer fetchSize = 50;
  
  // Стратегия извлечения данных из базы
  private ReadFromDbType readFromDbType = ReadFromDbType.READ_PORTIONS;
  private Integer recordCountForReadFromDb = 50;
  //private Integer fetchSizeDb = fetchSize; // Размер порции для извлечения из базы данных
  
  // Политика определения высоты таблицы
  // -1 - определяется системой и не зависит от кол-ва строк
  // 0 - автоматическое определение высоты в зависимости от числа строк в таблице, но не больше буфера
  // > 0 - заданное кол-во строк, но не больше буфера
  
  private Integer autoHeightRows = -1;
  
  // Блок параметров запроса раскрыт
  private Boolean parametersDisclosed = true;
  // Количество строк в фильтровой форме
  private Integer rowInFilterForm = 2;
  // Отступ кнопок слева (в пикселях) для формы поиска
  private int buttonsIndent = 0;
  
  private String contentDelivery = TableContentDelivery.getDefault().getValue();
  // Отступ до кнопок редактирования в пикселях private Integer indexToButtonsEdit = 4;
  
  //============================================================================
  private void xstreamAfterUnMarshall(Map<String, String> attributes) 
  {
    if (readFromDbType == null) 
    {
      readFromDbType = ReadFromDbType.READ_PORTIONS;
    }
    //---  
    if (recordCountForReadFromDb == null) 
    {
      if (ReadFromDbType.READ_PORTIONS.equals(readFromDbType)) 
      {
        recordCountForReadFromDb = 50;
      }
      else 
      {
        recordCountForReadFromDb = 1000;
      }
    }
    if (toolBarExists == null) 
    {
      toolBarExists = false; 
    }
    if (rowSelectAble == null) 
    {
      rowSelectAble = false;
    }
  }
  //============================================================================

  public String getEventChangeCurrentRowLabel() 
  {
    return EventType.CHANGE_CURRENT_ROW.getCaption();
  }
  public void setFilterAble(Boolean filterAble)
  {
    this.filterAble = filterAble;
  }

  /**
   * @return
   */
  public Boolean getFilterAble()
  {
    return filterAble;
  }

  /**
   * @param intervalForChangeColourOfLines
   */
  public void setIntervalForChangeColourOfLines(int intervalForChangeColourOfLines)
  {
    this.intervalForChangeColourOfLines = intervalForChangeColourOfLines;
  }

  /**
   * @return
   */
  public int getIntervalForChangeColourOfLines()
  {
    return intervalForChangeColourOfLines;
  }

  /**
   * @param possibilityChangeWidthColumns
   */
  public void setPossibilityChangeWidthColumns(Boolean possibilityChangeWidthColumns)
  {
    this.possibilityChangeWidthColumns = possibilityChangeWidthColumns;
  }

  /**
   * @return
   */
  public Boolean getPossibilityChangeWidthColumns()
  {
    return possibilityChangeWidthColumns;
  }

  /**
   * @param possibilityShiftColumns
   */
  public void setPossibilityShiftColumns(Boolean possibilityShiftColumns)
  {
    this.possibilityShiftColumns = possibilityShiftColumns;
  }

  /**
   * @return
   */
  public Boolean getPossibilityShiftColumns()
  {
    return possibilityShiftColumns;
  }

  /**
   * @param inlineStyle
   */
  public void setInlineStyle(String inlineStyle)
  {
    this.inlineStyle = inlineStyle;
  }

  /**
   * @return
   */
  public String getInlineStyle()
  {
    return inlineStyle;
  }

  /**
   * @param shortDesc
   */
  public void setShortDesc(String shortDesc)
  {
    this.shortDesc = shortDesc;
  }

  /**
   * @return
   */
  public String getShortDesc()
  {
    return shortDesc;
  }

  public void setToolBarExists(Boolean toolBarExists)
  {
    this.toolBarExists = toolBarExists;
  }

  public Boolean getToolBarExists()
  {
    return toolBarExists;
  }
  
  public boolean isAutoHeightRowsEqMin1() 
  {
    return (autoHeightRows != null && autoHeightRows == -1);
  }


  /**
   * @param autoPersonalizableAble
   */
  public void setAutoPersonalizableAble(Boolean autoPersonalizableAble)
  {
    this.autoPersonalizableAble = autoPersonalizableAble;
  }

  /**
   * @return
   */
  public Boolean getAutoPersonalizableAble()
  {
    return autoPersonalizableAble;
  }

  /**
   * @param rowSelectAble
   */
  public void setRowSelectAble(Boolean rowSelectAble)
  {
    this.rowSelectAble = rowSelectAble;
  }

  /**
   * @return
   */
  public Boolean getRowSelectAble()
  {
    return rowSelectAble;
  }

  /**
   * @param columnStreching
   */
  public void setColumnStreching(String columnStreching)
  {
    this.columnStreching = columnStreching;
  }

  /**
   * @return
   */
  public String getColumnStreching()
  {
    return columnStreching;
  }

  /**
   * getColumnStrechingForTableAttribute - возвращает значение columnStreching
   * для соответствующего атрибута в таблице readOnlyDynamicTable или readOnlyDynamicTableOnly.
   * @return
   */
  public String getColumnStrechingForTableAttribute()
  {
    return columnStreching;
  }

  public void setEmptyText(String emptyText)
  {
    this.emptyText = emptyText;
  }

  public String getEmptyText()
  {
    if (emptyText == null) 
    {
      emptyText = "Нет данных";
    }
    return emptyText;
  }

  public String getCssHeaderColumnCategory()
  {
    return "font-size:12px;font-weight:bold;color:black;font-family: Tahoma,Verdana,Helvetica,sans-serif";
    
  }

  public String getCssHeaderColumn()
  {
    return "font-size:12px;font-weight:bold;color:black;font-family: Arial,Helvetica";
  }

  public void setDisplayHeadersOfColumns(Boolean displayHeadersOfColumns)
  {
    this.displayHeadersOfColumns = displayHeadersOfColumns;
  }

  public Boolean getDisplayHeadersOfColumns()
  {
    return displayHeadersOfColumns;
  }

  public void setFetchSize(Integer fetchSize)
  {
    this.fetchSize = fetchSize;
  }

  public Integer getFetchSize()
  {
    return fetchSize;
  }

  public void setAutoHeightRows(Integer autoHeightRows)
  {
    this.autoHeightRows = autoHeightRows;
  }

  public Integer getAutoHeightRows()
  {
    return autoHeightRows;
  }

  public void setParametersDisclosed(Boolean parametersDisclosed)
  {
    this.parametersDisclosed = parametersDisclosed;
  }

  public Boolean getParametersDisclosed()
  {
    return parametersDisclosed;
  }

  public void setRowInFilterForm(Integer rowInFilterForm)
  {
    this.rowInFilterForm = rowInFilterForm;
  }

  public Integer getRowInFilterForm()
  {
    return rowInFilterForm;
  }

  public void setContentDelivery(String contentDelivery)
  {
    this.contentDelivery = contentDelivery;
  }

  public String getContentDelivery()
  {
    return contentDelivery;
  }
  
  public List<SelectItem> getContentDeliverySelectItems() 
  {
    return TableContentDelivery.getSelectItems();
  }

  public void setButtonsIndent(int buttonsIndent)
  {
    this.buttonsIndent = buttonsIndent;
  }

  public int getButtonsIndent()
  {
    return buttonsIndent;
  }

  public void setContainerInlineStyle(String containerInlineStyle)
  {
    this.containerInlineStyle = containerInlineStyle;
  }

  public String getContainerInlineStyle()
  {
    return containerInlineStyle;
  }
  
  public String getContainerInlineStyleView()
  {
    String rc = containerInlineStyle;
    if (rc == null) 
    {
      rc = inlineStyle;
    }
    return rc;
  }

  //-----
  //Параметры кеширования данных
  //Параметры представления данных
  //Параметры формы фильтра

//  protected void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
//  {
//    super.xstreamBeforeMarshall(writer);
//  }
//  protected void xstreamAfterUnMarshall(Map<String, String> attributes)
//  {
//    super.xstreamAfterUnMarshall(attributes);
//  }
  //-----

  public void setSortAble(Boolean sortAble)
  {
    this.sortAble = sortAble;
  }

  public Boolean getSortAble()
  {
    return sortAble;
  }

  public void setReadFromDbType(ReadFromDbType readFromDbType)
  {
    this.readFromDbType = readFromDbType;
  }

  public ReadFromDbType getReadFromDbType()
  {
    return readFromDbType;
  }

  public void setRecordCountForReadFromDb(Integer recordCountForReadFromDb)
  {
    this.recordCountForReadFromDb = recordCountForReadFromDb;
  }

  public Integer getRecordCountForReadFromDb()
  {
    return recordCountForReadFromDb;
  }
  //--------
  public SelectItem[] getSelectItemsForReadFromDbType() 
  {
    return ReadFromDbType.getSelectItemsForReadFromDbType();
  }

  public Map<String, Object> getSqlParametersCurrentValues(UserRecord ur)
  {
    Map<String, Object> rc = new HashMap<String, Object>();
    MapWithOrderKeys<String, SqlParameter> sqlParameters = getSqlParameters();
    if (sqlParameters == null) 
    {
      return rc;
    }
    for (String name : sqlParameters.keySet())
    {
      SqlParameter item = sqlParameters.get(name);
      rc.put(name, item.getCurrentValue(ur));
    }
    return rc;
  }
}
