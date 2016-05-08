package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum FilterType
{
  INPUT_TEXT("Текст"),
  INPUT_DATE("Дата"),
  INPUT_TIME("Время"),
  INPUT_DATE_TIME("Дата и время")
  ;
  private static final long serialVersionUID = 1L;
  private String caption;

  private FilterType(String caption) 
  {
    this.caption = caption;
  }

  public String getCaption()
  {
    return caption;
  }
  public static List<SelectItem> getSelectItems() 
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (FilterType item : FilterType.values()) 
    {
      rc.add(new SelectItem(item.name(),item.getCaption(),item.getCaption()));
    }
    return rc;
  }
  public String getName() 
  {
    return name();
  }
}
