package universal_taskflow.common.data;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Класс - ключ для таблицы НТML и EL значений (EL_HTML_value)
 */
@XStreamAlias("ElHtmlKey")
public class ElHtmlKey implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String propertyClassName = null;
  private String propertyName = null;
  private Serializable objectId = null;
  
  public ElHtmlKey(String propertyClassName, String propertyName, Serializable objectId) 
  {
    super();
    this.propertyClassName = propertyClassName;
    this.propertyName = propertyName;
    this.objectId = objectId;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public String getPropertyName()
  {
    return propertyName;
  }
  //--------------------
  @Override
  public int hashCode()
  {
    return (propertyClassName+"_"+propertyName+"_"+objectId).hashCode();
  }
  @Override
  public boolean equals(Object obj)
  {
    boolean rc = false;
    if (obj != null && (obj instanceof ElHtmlKey))
    {
      ElHtmlKey objElHtml = (ElHtmlKey) obj;
      if ((propertyClassName + "_" + propertyName+"_"+objectId).equals(
              objElHtml.propertyClassName+"_"+objElHtml.propertyName+"_"+objElHtml.objectId))
      {
        rc = true;
      }
    }
    return rc;
  }

  public void setPropertyClassName(String propertyClassName)
  {
    this.propertyClassName = propertyClassName;
  }

  public String getPropertyClassName()
  {
    return propertyClassName;
  }

  public void setObjectId(Serializable objectId)
  {
    this.objectId = objectId;
  }

  public Serializable getObjectId()
  {
    return objectId;
  }
}
