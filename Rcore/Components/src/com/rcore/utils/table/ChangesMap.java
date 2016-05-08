package com.rcore.utils.table;

import java.util.HashMap;
import java.util.Map;

public class ChangesMap extends HashMap<String,Object>
{
  @SuppressWarnings("compatibility:5493704977659954090")
  private static final long serialVersionUID = 1L;

  public ChangesMap()
  {
    super();
  }
  
  private boolean isChanged = false;

  @Override
  public Object put(String key, Object value)
  {
    Object oldValue = get(key);
    Object rc = super.put(key, value);
    setChanged(oldValue, value);
    return rc;
  }

  @Override
  public void putAll(Map value)
  {
    super.putAll(value);
    isChanged = true;
  }

  @Override
  public Object remove(java.lang.Object key)
  {
    if (containsKey(key))
    {
      isChanged = true;
    }
    Object rc = super.remove(key);
    return rc;
  }

  @Override
  public void clear()
  {
    if (size() > 0)
    {
      isChanged = true;
    }
    super.clear();
  }

  private void setChanged(Object oldValue, Object value)
  {
    if (oldValue == null)
    {
      if (value != null)
      {
        isChanged = true;
      }
      else
      {
        ;
      }
    }
    else
    {
      if (value != null)
      {
        if (!isChanged) //
        {
          isChanged = !(value.equals(oldValue));
        }
      }
      else
      {
        isChanged = true;
      }
    }
  }
}
