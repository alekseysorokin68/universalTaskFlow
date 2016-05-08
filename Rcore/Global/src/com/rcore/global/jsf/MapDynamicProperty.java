package com.rcore.global.jsf;

import java.util.HashMap;

/**
 * Для создания динамических свойств в бинах
 */

public abstract class MapDynamicProperty extends HashMap<Object,Object>
{
  @SuppressWarnings("compatibility:3368324545918261369")
  private static final long serialVersionUID = 1L;
  private boolean isHashValues = true;

  public MapDynamicProperty()
  {
    super();
  }
  public MapDynamicProperty(boolean isHashValues)
  {
    super();
    this.isHashValues = isHashValues;
  }
  @Override
  public Object get(Object key) 
  {
    Object rc = super.get(key);
    if (rc == null) 
    {
      rc = getImpl(key);
      if (rc != null) 
      {
        if (isHashValues) {
          super.put(key, rc);
        }
      }
    }
    return rc;
  }
  protected abstract Object getImpl(Object key);
}
