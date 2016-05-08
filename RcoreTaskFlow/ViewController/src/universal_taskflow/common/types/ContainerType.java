package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

// panel_group_layout_scroll
public enum ContainerType
{
  DEFAULT("По умолчанию (решение принимает система - рекомендуется)"),
  PANEL_STRECH_LAYOUT("Растягивающая панель (af:panelStretchLayout)"),
  TABLE_LAYOUT("HTML таблица (trh:tableLayout)"),
  PANEL_GROUP_LAYOUT_VERTICAL("Вертикальное расположение (af:panelGroupLayout layout=\"vertical\")"),
  PANEL_GROUP_LAYOUT_SCROLL("Вертикальное расположение со скроллингом (af:panelGroupLayout layout=\"scroll\")")
  //PANEL_GROUP_LAYOUT_HORIZONTAL("Горизонтальное расположение (af:panelGroupLayout layout=\"horizontal\")"),
  //PANEL_GROUP_LAYOUT_DEFAULT("Выбор расположения определяет ADF (af:panelGroupLayout layout=\"default\")"),
  ;
  private static final long serialVersionUID = 1L;
  private String caption = null;

  private ContainerType(String caption) 
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
    for (ContainerType item : ContainerType.values()) 
    {
      rc.add(new SelectItem(item,item.getCaption(),item.getCaption()));
    }
    return rc;
  }
}
