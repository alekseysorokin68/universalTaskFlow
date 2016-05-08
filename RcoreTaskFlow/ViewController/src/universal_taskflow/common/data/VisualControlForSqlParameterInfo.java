package universal_taskflow.common.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

import java.util.Map;

import universal_taskflow.common.types.VisualControlForSqlParameterType;

@XStreamAlias("VisualControlForSqlParameterData")
public class VisualControlForSqlParameterInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  private VisualControlForSqlParameterType type = VisualControlForSqlParameterType.INPUT;
  //private DateType dateType = DateType.DATE; // Для даты
  private String widthInput = null; // Ширина поля ввода - VisualControlForSqlParameterType.INPUT
  private int    rowsInput = 1;     // Кол-во строк в поле для ввода - VisualControlForSqlParameterType.INPUT
  private DropDownInfo dropDownInfo =  null; // Для VisualControlForSqlParameterType.DROP_DOWN_LIST
  //-------------------------------------
  public VisualControlForSqlParameterInfo()
  {
    super();
  }
  //-------------------------------------
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
    if (type == null) 
    {
      type = VisualControlForSqlParameterType.INPUT;
    }
  }
  //-------------------------------------
  public void setDropDownInfo(DropDownInfo dropDownInfo)
  {
    this.dropDownInfo = dropDownInfo;
  }

  public DropDownInfo getDropDownInfo()
  {
    if (dropDownInfo == null) 
    {
      dropDownInfo = new DropDownInfo();
    }
    return dropDownInfo;
  }

  public void setWidthInput(String widthInput)
  {
    this.widthInput = widthInput;
  }

  public String getWidthInput()
  {
    return widthInput;
  }

  public void setRowsInput(int rowsInput)
  {
    this.rowsInput = rowsInput;
  }

  public int getRowsInput()
  {
    return rowsInput;
  }

  public void setType(VisualControlForSqlParameterType type)
  {
    this.type = type;
  }

  public VisualControlForSqlParameterType getType()
  {
    return type;
  }

//  public void setDateType(DateType dateType)
//  {
//    this.dateType = dateType;
//  }
//
//  public DateType getDateType()
//  {
//    return dateType;
//  }
//  //------
//  public Boolean getDateTypeDate() 
//  {
//    return DateType.DATE.equals(dateType);
//  }
//  public void setDateTypeDate(Boolean value) 
//  {
//    if (value != null && value) 
//    {
//      dateType = DateType.DATE;
//    }
//  }
//  
//  public Boolean getDateTypeTime() 
//  {
//    return DateType.TIME.equals(dateType);
//  }
//  public void setDateTypeTime(Boolean value) 
//  {
//    if (value != null && value) 
//    {
//      dateType = DateType.TIME;
//    }
//  }
//  
//  public Boolean getDateTypeBoth() 
//  {
//    return DateType.BOTH.equals(dateType);
//  }
//  public void setDateTypeBoth(Boolean value) 
//  {
//    if (value != null && value) 
//    {
//      dateType = DateType.BOTH;
//    }
//  }
}
