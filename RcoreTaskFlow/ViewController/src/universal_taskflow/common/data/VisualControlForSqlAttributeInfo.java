package universal_taskflow.common.data;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

import universal_taskflow.common.types.VisualControlForSqlAttributeType;


@XStreamAlias("VisualControlForSqlAttributeInfo")
public class VisualControlForSqlAttributeInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  private VisualControlForSqlAttributeType type = null;
  // Количество строк в текстовом поле (актуально если: visualControl == VisualControlForAttributeSql.INPUT)
  private int rowsInInput = 1; // VisualControlForSqlAttributeType.INPUT
  private LinkInfo linkInfo = null; // VisualControlForSqlAttributeType.LINK
  private DropDownInfo dropDownInfo =  null; // Для VisualControlForSqlParameterType.DROP_DOWN_LIST
  private Serializable checkBoxTrueValue  = "1";
  private Serializable checkBoxFalseValue = "0";
  //=============================================
  //=============================================
  public VisualControlForSqlAttributeInfo(VisualControlForSqlAttributeType type)
  {
    super();
    this.type = type;
  }

  public void setRowsInInput(int rowsInInput)
  {
    this.rowsInInput = rowsInInput;
  }

  public int getRowsInInput()
  {
    return rowsInInput;
  }

  public void setLinkInfo(LinkInfo linkInfo)
  {
    this.linkInfo = linkInfo;
  }

  public LinkInfo getLinkInfo()
  {
    if (linkInfo == null) linkInfo = new LinkInfo();
    return linkInfo;
  }
  
  public VisualControlForSqlAttributeType getType()
  {
    return type;
  }

  public void setDropDownInfo(DropDownInfo dropDownInfo)
  {
    this.dropDownInfo = dropDownInfo;
  }

  public DropDownInfo getDropDownInfo()
  {
    if (dropDownInfo == null) dropDownInfo = new DropDownInfo();
    return dropDownInfo;
  }
  //=====================================
//  public class MapTypes extends HashMap<String,Boolean>
//  {
//    private static final long serialVersionUID = 1L;
//    @Override
//    public Boolean get(Object obj) 
//    {
//      Boolean rc = false;
//      String key = (String) obj;
//      rc = (type.name().equals(key));
//      return rc;
//    }
//    
//    @Override
//    public Boolean put(String key, Boolean value) 
//    {
//      if (value != null && value) 
//      {
//        type = VisualControlForSqlAttributeType.valueOf(key);
//      }
//      return value;
//    }
//  }

  public void setType(VisualControlForSqlAttributeType type)
  {
    this.type = type;
  }

  public void setCheckBoxTrueValue(Serializable checkBoxTrueValue)
  {
    this.checkBoxTrueValue = checkBoxTrueValue;
  }

  public Serializable getCheckBoxTrueValue()
  {
    return checkBoxTrueValue;
  }

  public void setCheckBoxFalseValue(Serializable checkBoxFalseValue)
  {
    this.checkBoxFalseValue = checkBoxFalseValue;
  }

  public Serializable getCheckBoxFalseValue()
  {
    return checkBoxFalseValue;
  }
}
