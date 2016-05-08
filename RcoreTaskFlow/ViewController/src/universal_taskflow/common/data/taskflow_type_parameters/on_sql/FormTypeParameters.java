package universal_taskflow.common.data.taskflow_type_parameters.on_sql;

import universal_taskflow.common.types.EventType;
import universal_taskflow.common.types.LabelAlign;


public class FormTypeParameters extends TaskFlowParametersBaseOnSql
{
  private static final long serialVersionUID = 1L;
  public FormTypeParameters()
  {
    super();
  }
  //-----------------------------------------------
  // Ширина полей
  private String widthAttributes = null;

  // Ширина меток
  private String widthLabels = null;

  // Кол-во строк
  private Integer rows = null;

  // Максимальное количество колонок
  private Integer maxColumns = null;

  // Расположение меток
  private LabelAlign labelAlign = LabelAlign.LEFT;

  private Boolean isOpenFormInCurrentWindow = true;
  //----------------------------------------------
  // Блок параметров запроса раскрыт
  private Boolean parametersDisclosed = true;
  // Количество строк в фильтровой форме
  private Integer rowInFilterForm = 2;
  // Отступ кнопок слева (в пикселях) для формы поиска
  private int buttonsIndent = 0;
  //---
  private Boolean isNavigationPanel = true;
  //--------------- События и связи -------
  //private boolean eventChangeCurrentRowActive = false;
  //private LinkInfo linkByChangeCurrentRow = null;
  //private transient List<MainRecord> mainRecordTableModelForLinkPortletsByChangeCurrentRow = null;
  //==============================================

  /**
   * @param widthAttributes
   */
  public void setWidthAttributes(String widthAttributes)
  {
    this.widthAttributes = widthAttributes;
  }

  /**
   * @return
   */
  public String getWidthAttributes()
  {
    return widthAttributes;
  }

  /**
   * @param widthLabels
   */
  public void setWidthLabels(String widthLabels)
  {
    this.widthLabels = widthLabels;
  }

  /**
   * @return
   */
  public String getWidthLabels()
  {
    return widthLabels;
  }

  /**
   * @param rows
   */
  public void setRows(Integer rows)
  {
    this.rows = rows;
  }

  /**
   * @return
   */
  public Integer getRows()
  {
    return rows;
  }

  /**
   * @param maxColumns
   */
  public void setMaxColumns(Integer maxColumns)
  {
    this.maxColumns = maxColumns;
  }

  /**
   * @return
   */
  public Integer getMaxColumns()
  {
    return maxColumns;
  }

  /**
   * @param labelAlign
   */
  public void setLabelAlign(LabelAlign labelAlign)
  {
    this.labelAlign = labelAlign;
  }

  /**
   * @return
   */
  public LabelAlign getLabelAlign()
  {
    return labelAlign;
  }

  /**
   * @param isOpenFormInCurrentWindow
   */
  public void setIsOpenFormInCurrentWindow(Boolean isOpenFormInCurrentWindow)
  {
    if (isOpenFormInCurrentWindow == null)
      isOpenFormInCurrentWindow = true;
    this.isOpenFormInCurrentWindow = isOpenFormInCurrentWindow;
  }

  /**
   * @return
   */
  public Boolean getIsOpenFormInCurrentWindow()
  {
    if (isOpenFormInCurrentWindow == null)
      isOpenFormInCurrentWindow = true;
    return isOpenFormInCurrentWindow;
  }
  //-----------------------------------------------
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

  public void setIsNavigationPanel(Boolean isNavigationPanel)
  {
    this.isNavigationPanel = isNavigationPanel;
  }

  public Boolean getIsNavigationPanel()
  {
    return isNavigationPanel;
  }

  public String getEventChangeCurrentRowLabel() 
  {
    return EventType.CHANGE_CURRENT_ROW.getCaption();
  }
}
