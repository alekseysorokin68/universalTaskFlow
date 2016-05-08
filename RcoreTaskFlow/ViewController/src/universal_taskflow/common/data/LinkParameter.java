package universal_taskflow.common.data;

import java.io.Serializable;

import java.util.HashMap;

public class LinkParameter extends HashMap<Object, Object>  implements Serializable
{
  private static final long serialVersionUID = 4L;
  private String name = null;      // Имя Sql - параметра
  private String fieldName = null; // Имя поля
  //==============================

  /**
   * @param key
   * @return
   */
  @Override
  public Object get(Object key)
  {
    String rc = null;
    if ("name".equals(key))
    {
      rc = name;
    }
    else if ("fieldName".equals(key))
    {
      rc = fieldName;
    }
    return rc;
  }

  /**
   * @param key
   * @param value
   * @return
   */
  @Override
  public Object put(Object key, Object value)
  {
    if ("name".equals(key))
    {
      name = (String) value;
    }
    else if ("fieldName".equals(key))
    {
      fieldName = (String) value;
    }
    return value;
  }

  /**
   * @return
   */
  @Override
  public int size()
  {
    return 2;
  }

  /**
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof LinkParameter))
    {
      return false;
    }
    LinkParameter par = (LinkParameter) obj;
    boolean rc = false;
    //--------------------------------------
    String id1 = name + "." + fieldName;
    String id2 = par.name + "." + par.fieldName;
    rc = id1.equals(id2);
    return rc;
  }

  /**
   * @return
   */
  @Override
  public int hashCode()
  {
    String id = name + "." + fieldName;
    return (id.hashCode());
  }
  //==============================

  /**
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param value
   */
  public void setFieldName(String value)
  {
    this.fieldName = value;
  }

  /**
   * @return
   */
  public String getFieldName()
  {
    return fieldName;
  }

  /**
   * @return
   */
  @Override
  public String toString()
  {
    StringBuilder rc = new StringBuilder();
    rc.append("name='").append(name).append("'  ").append("fieldName = '").append(fieldName).append("'");
    return rc.toString();
  }
}

