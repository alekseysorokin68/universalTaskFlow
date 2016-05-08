package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum VerticalAlign
{
  TOP("Вверх"),
  MIDDLE("Середина"),
  BOTTOM("Низ");
  private static final long serialVersionUID = 1L;


  private String caption = null;
  private VerticalAlign(String caption) 
  {
    this.caption = caption;
  }
  
  public static List<SelectItem> getSelectItems() 
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (VerticalAlign value : VerticalAlign.values()) 
    {
      rc.add(new SelectItem(value,value.getCaption(),value.getCaption()));
    }
    return rc;
  }

  public String getCaption()
  {
    return caption;
  }
}