package universal_taskflow.common.types;

public enum EventType
{
  CHANGE_CURRENT_ROW("Смена текущей записи")
  ;
  private static final long serialVersionUID = 1L;
  private String caption = null;

  private EventType(String caption) 
  {
    this.caption = caption;
  }

  public String getCaption()
  {
    return caption;
  }
  public String getName() 
  {
    return name();
  }
}
