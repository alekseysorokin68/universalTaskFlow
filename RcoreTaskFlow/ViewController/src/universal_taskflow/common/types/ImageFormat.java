package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public enum ImageFormat
{
  AUTO,
  FLASH,
  HTML5,
  PNG,
  PNG_STAMPED;
  
  private static final long serialVersionUID = 1L;

  public String getName() 
  {
    return this.name();
  }
  public static List<SelectItem> getImageFormatSelectItems() 
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (ImageFormat item : ImageFormat.values()) 
    {
      rc.add(new SelectItem(item,item.name(),item.name()));
    }
    return rc;
  }
}