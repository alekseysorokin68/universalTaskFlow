package universal_taskflow.common.types;

public enum FieldValueType
{
  LITERAL,
  HTML,
  EL
  ;
  private static final long serialVersionUID = 1L;
  
  public String getName() 
  {
    return this.name();
  }
}
