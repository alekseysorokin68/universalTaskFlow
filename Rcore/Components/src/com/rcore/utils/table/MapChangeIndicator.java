package com.rcore.utils.table;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

public class MapChangeIndicator extends HashMap<String, Object>
  implements Serializable
{
  @SuppressWarnings("compatibility:4698635480648364052")
  private static final long serialVersionUID = 1L;
  private boolean changed = false;

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
    changed = true;
  }

  @Override
  public Object remove(java.lang.Object key)
  {
    if (containsKey(key))
    {
      changed = true;
    }
    Object rc = super.remove(key);
    return rc;
  }

  @Override
  public void clear()
  {
    if (size() > 0)
    {
      changed = true;
    }
    super.clear();
  }

  private void setChanged(Object oldValue, Object value)
  {
    if (oldValue == null)
    {
      if (value != null)
      {
        changed = true;
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
        if (!changed) //
        {
          changed = !(value.equals(oldValue));
        }
      }
      else
      {
        changed = true;
      }
    }
  }

  public boolean isChanged()
  {
    return changed;
  }

  public void setChanged(boolean changed)
  {
    this.changed = changed;
  }
}
