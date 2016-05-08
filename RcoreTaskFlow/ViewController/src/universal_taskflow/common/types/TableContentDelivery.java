package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum TableContentDelivery
{
  immediate("Таблица заполняется данными сразу при построении"),
  lazy("Таблица заполняется данными при первом запросе после ее построения"),
  whenAvailable("Таблица заполняется данными, когда модель показывает, что все данные доступны")
  ;
  private static final long serialVersionUID = 1L;
  
  //private String caption;
  private String description;
  
  private TableContentDelivery(String description) 
  {
    this.description = description;
  }
  public static TableContentDelivery getDefault() 
  {
    return immediate;
  }
  public String getValue()
  {
    return name();
  }
  
  public static List<SelectItem> getSelectItems() 
  {
    TableContentDelivery[] items = values();
    List<SelectItem> rc = new ArrayList<SelectItem>(items.length);
    TableContentDelivery defaultItem = getDefault();
    rc.add(new SelectItem(defaultItem.getValue(),defaultItem.getValue(),defaultItem.getDescription()));
    for (TableContentDelivery item : items) 
    {
      if (!item.equals(defaultItem)) 
      {
        rc.add(new SelectItem(item.getValue(),item.getValue(),item.getDescription()));
      }
    }
    return rc;
  }

  public String getDescription()
  {
    return description;
  }
}
