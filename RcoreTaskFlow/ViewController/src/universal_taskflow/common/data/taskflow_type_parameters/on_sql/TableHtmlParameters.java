package universal_taskflow.common.data.taskflow_type_parameters.on_sql;


public class TableHtmlParameters extends TaskFlowParametersBaseOnSql
{
  private static final long serialVersionUID = 1L;
  // Отображать заголовки колонок
  private Boolean displayHeadersOfColumns = true;
  // Всплывающее описание
  private String shortDesc = null;
  // Стиль таблицы (CSS)
  private String inlineStyle = null;
  // Стиль контейнера таблицы (CSS)
  private String containerInlineStyle = null;
  
  private String emptyText = "Нет данных";
  
  // Максимальное кол-во отображаемых записей
  private int maxRecordCount = 1000; // Проверено
  
  private String width = "100%"; // Проверено
  
  // Наличие границы
  private int borderWidth = 0;
  // Интревал для перемены цвета строк
  private Integer intervalForChangeColourOfLines = 1; // 0-нет перемены цвета
  
  // Блок параметров запроса раскрыт
  private Boolean parametersDisclosed = false;
  // Количество строк в фильтровой форме   hhh
  private Integer rowInFilterForm = 2;
  // Отступ кнопок слева (в пикселях) для формы поиска 
  private int buttonsIndent = 0;
  //---------------------------------------------------------------------
  public void setDisplayHeadersOfColumns(Boolean displayHeadersOfColumns)
  {
    this.displayHeadersOfColumns = displayHeadersOfColumns;
  }

  public Boolean getDisplayHeadersOfColumns()
  {
    return displayHeadersOfColumns;
  }

  public void setShortDesc(String shortDesc)
  {
    this.shortDesc = shortDesc;
  }

  public String getShortDesc()
  {
    return shortDesc;
  }

  public void setInlineStyle(String inlineStyle)
  {
    this.inlineStyle = inlineStyle;
  }

  public String getInlineStyle()
  {
    return inlineStyle;
  }

  public void setContainerInlineStyle(String containerInlineStyle)
  {
    this.containerInlineStyle = containerInlineStyle;
  }

  public String getContainerInlineStyle()
  {
    return containerInlineStyle;
  }

  public void setEmptyText(String emptyText)
  {
    this.emptyText = emptyText;
  }

  public String getEmptyText()
  {
    return emptyText;
  }

  public void setMaxRecordCount(int maxRecordCount)
  {
    this.maxRecordCount = maxRecordCount;
  }

  public int getMaxRecordCount()
  {
    return maxRecordCount;
  }

  public void setWidth(String width)
  {
    this.width = width;
  }

  public String getWidth()
  {
    return width;
  }

  public void setIntervalForChangeColourOfLines(Integer intervalForChangeColourOfLines)
  {
    this.intervalForChangeColourOfLines = intervalForChangeColourOfLines;
  }

  public Integer getIntervalForChangeColourOfLines()
  {
    return intervalForChangeColourOfLines;
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

  public void setButtonsIndent(int buttonsIndent)
  {
    this.buttonsIndent = buttonsIndent;
  }

  public int getButtonsIndent()
  {
    return buttonsIndent;
  }

  public void setBorderWidth(int borderWidth)
  {
    this.borderWidth = borderWidth;
  }

  public int getBorderWidth()
  {
    return borderWidth;
  }
}
