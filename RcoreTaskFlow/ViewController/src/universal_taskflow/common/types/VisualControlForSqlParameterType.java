package universal_taskflow.common.types;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Элементы управления для редактирования параметров запроса.
 */
@XStreamAlias("VisualControlForSqlParameterType")
public enum VisualControlForSqlParameterType
{
  DATE,
  INPUT,
  DROP_DOWN_LIST
  ;
  private static final long serialVersionUID = 1L;
  
  public String getName() 
  {
    return name();
  }
}
