package com.rcore.global.bcomponent;

import java.io.Serializable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import oracle.jbo.server.ViewRowImpl;


public class RowByMap 
      extends ViewRowImpl 
      //implements Row
      implements Serializable,Map<String,Object>
{
  @SuppressWarnings("compatibility:202497962210054855")
  private static final long serialVersionUID = 1L;
  private transient Map<String, Object> map = null;

  public RowByMap(Map<String,Object> map)
  {
    super();
    this.map = map;
  }

  public int size()
  {
    return map.size();
  }

  public boolean isEmpty()
  {
    return map.isEmpty();
  }

  public boolean containsKey(Object key)
  {
    return map.containsKey(key);
  }

  public boolean containsValue(Object value)
  {
    return map.containsValue(value);
  }

  public Object get(Object key)
  {
    return map.get(key);
  }

  public Object put(String key, Object value)
  {
    return map.put(key, value);
  }

  public Object remove(Object key)
  {
    return map.remove(key);
  }

  public void putAll(Map<? extends String, ? extends Object> m)
  {
    map.putAll(m);
  }

  public void clear()
  {
    map.clear();
  }

  public Set<String> keySet()
  {
    return map.keySet();
  }

  public Collection<Object> values()
  {
    return map.values();
  }

  public Set<Map.Entry<String, Object>> entrySet()
  {
    return map.entrySet();
  }
}
