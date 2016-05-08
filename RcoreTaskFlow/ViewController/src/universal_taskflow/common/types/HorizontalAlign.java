package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * Горизонтальное выравнивание данных
 */
public enum HorizontalAlign
{
  LEFT("Слева"),
  CENTER("Центр"),
  RIGHT("Справа");
  
  @SuppressWarnings("compatibility:524132777116903712")
  private static final long serialVersionUID = 1L;

  private String caption = null;
  private HorizontalAlign(String caption) 
  {
    this.caption = caption;
  }
  
  public static List<SelectItem> getSelectItems() 
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (HorizontalAlign value : HorizontalAlign.values()) 
    {
      rc.add(new SelectItem(value,value.getCaption(),value.getCaption()));
    }
    return rc;
  }

  public String getCaption()
  {
    return caption;
  }
  public String getLowerCase() 
  {
    return this.toString().toLowerCase();
  }
}
