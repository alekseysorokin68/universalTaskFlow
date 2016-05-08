package universal_taskflow.common.types;

import javax.faces.model.SelectItem;

public enum LinkType
{
  LINK_TO_PORTAL_RESOURCE("Ссылка на ресурс портала","Будет обычной ссылкой, если ресурс доступен. Иначе - будет простым текстом"),
  LINK_SIMPLE("Обычная ссылка","Это обычная ссылка")
  ;
  //--------
  @SuppressWarnings("compatibility:-3580817278869051201")
  private static final long serialVersionUID = 1L;
  //----------------------------------------------
  private String caption;
  private String schortDesc;
  private LinkType(String caption, String schortDesc)
  {
    this.caption = caption;
    this.schortDesc = schortDesc;
  }

  public String getCaption()
  {
    return caption;
  }
  
  public String getSchortDesc()
  {
    return schortDesc;
  }
  
  public static SelectItem[] getSelectItems() 
  {
    LinkType[] values = LinkType.values();
    SelectItem[] rc = new SelectItem[values.length];
    for (int i=0; i < values.length; i++) 
    {
      LinkType value = values[i];
      rc[i] = new SelectItem(value,value.getCaption(),value.getSchortDesc());
    } // for
    return rc;
  }
  
  public String getName() 
  {
    return name();
  }
}
