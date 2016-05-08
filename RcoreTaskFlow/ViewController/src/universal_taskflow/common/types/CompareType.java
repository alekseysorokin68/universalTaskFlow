package universal_taskflow.common.types;

public enum CompareType
{
  DELETED("Удален (есть в файле, но нет в базе)"),
  CHANGED("Изменен"),
  NEW("Новый (есть в базе, но нет в файле)");
  
  private static final long serialVersionUID = 1L;
  
  private String caption;
  private CompareType(String caption) 
  {
    this.caption = caption;
  }
  public String getCaption() 
  {
    return caption;  
  }
}
