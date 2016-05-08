package universal_taskflow.common.data.taskflow_type_parameters.on_sql;

import java.util.List;

import javax.faces.model.SelectItem;

import universal_taskflow.common.types.EventType;
import universal_taskflow.common.types.TableContentDelivery;


public class TreeTypeParameters extends TaskFlowParametersBaseOnSql
{
  private static final long serialVersionUID = 1L;
  public TreeTypeParameters()
  {
    super();
  }
  //--------------------------
  // Поле - указатель на parent
  private String parentField = null;
  
  private boolean initiallyExpanded = false;
  //-------
  private int intervalForChangeColourOfLines = 1; // rowBandingInterval

  // Возможность изменения ширины колонок
  private Boolean possibilityChangeWidthColumns = false; // columnResizing

  // Возможность перестановки колонок. Не обслуживается визуально
  private Boolean possibilityShiftColumns = true; // disableColumnReordering

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

  // Выделение текущей строки
  private Boolean rowSelectAble = false; // rowSelection="single"
  
  // Текст при отсутствии данных
  private String emptyText = "Нет данных";
  
  // CSS для заголовка категории колонки 
  //private String cssHeaderColumnCategory = "font-size:12px;font-weight:bold;color:black;";
  
  // CSS для заголовка колонки 
  //private String cssHeaderColumn = "color:black;font-size:12px;font-weight:bold;";
  
  // Отображать заголовки колонок
  private Boolean displayHeadersOfColumns = true;
  
  // Кол-во строк в буфере
  //private Integer fetchSize = 25;
  private Integer fetchSize = 10000;
  
  // Блок параметров запроса раскрыт
  private Boolean parametersDisclosed = true;
  // Количество строк в фильтровой форме
  private Integer rowInFilterForm = 2;
  // Отступ кнопок слева (в пикселях) для формы поиска
  private int buttonsIndent = 0;
  
  private String contentDelivery = TableContentDelivery.getDefault().getValue();
  // Отступ до кнопок редактирования в пикселях
  private Integer indexToButtonsEdit = 4;
  
  //============================================================================

  public String getEventChangeCurrentRowLabel() 
  {
    return EventType.CHANGE_CURRENT_ROW.getCaption();
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

  /**
   * @param toolBarExists
   */
  public void setToolBarExists(Boolean toolBarExists)
  {
    this.toolBarExists = toolBarExists;
  }

  /**
   * @return
   */
  public Boolean getToolBarExists()
  {
    return toolBarExists;
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

  public void setIndexToButtonsEdit(Integer indexToButtonsEdit)
  {
    this.indexToButtonsEdit = indexToButtonsEdit;
  }

  public Integer getIndexToButtonsEdit()
  {
    return indexToButtonsEdit;
  }
  //  protected void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  //  {
  //    super.xstreamBeforeMarshall(writer);
  //  }
  //  protected void xstreamAfterUnMarshall(Map<String, String> attributes)
  //  {
  //    super.xstreamAfterUnMarshall(attributes);
  //  }
  //-----

  public void setParentField(String parentField)
  {
    this.parentField = parentField;
  }

  public String getParentField()
  {
    return parentField;
  }

  public void setInitiallyExpanded(boolean initiallyExpanded)
  {
    this.initiallyExpanded = initiallyExpanded;
  }

  public boolean isInitiallyExpanded()
  {
    return initiallyExpanded;
  }
}
