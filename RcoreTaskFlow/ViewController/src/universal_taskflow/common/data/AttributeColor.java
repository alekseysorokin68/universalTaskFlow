package universal_taskflow.common.data;

import java.awt.Color;

import java.io.Serializable;

import java.util.HashMap;

public class AttributeColor extends HashMap<Object, Object> implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String nameAttribute = null;
  private Color colorAttribute = null;

  @Override
  public Object get(Object key)
  {
    if ("name".equals(key))
    {
      return nameAttribute;
    }
    else if ("color".equals(key))
    {
      return colorAttribute;
    }
    else
    {
      return null;
    }
  }

  @Override
  public Object put(Object key, Object value)
  {
    //colorAttribute = color;
    //return color;
    if ("name".equals(key))
    {
      nameAttribute = (String) value;
    }
    else if ("color".equals(key))
    {
      colorAttribute = (Color) value;
    }
    return value;
  }
  //------------------------------

  @Override
  public int size()
  {
    return 2;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof AttributeColor))
    {
      return false;
    }
    AttributeColor color = (AttributeColor) obj;
    boolean rc = false;
    //--------------------------------------
    String id1 = nameAttribute + "." + colorAttribute;
    String id2 = color.nameAttribute + "." + color.colorAttribute;
    rc = id1.equals(id2);
    return rc;
  }

  @Override
  public int hashCode()
  {
    String id = nameAttribute + "." + colorAttribute;
    return (id.hashCode());
  }
  //==============================


  public void setNameAttribute(String nameAttribute)
  {
    this.nameAttribute = nameAttribute;
  }

  public String getNameAttribute()
  {
    return nameAttribute;
  }

  public void setColorAttribute(Color colorAttribute)
  {
    this.colorAttribute = colorAttribute;
  }

  public Color getColorAttribute()
  {
    return colorAttribute;
  }
  //-------------------------------

  public static void main(String[] args)
  {
    Color col = Color.darkGray;
    System.out.println(col);
  }
}
