package com.rcore.global.jsf;

import com.rcore.global.Resource;

public class ResourcePropertyString
  extends MapDynamicProperty
{
  @SuppressWarnings("compatibility:8527289127092994468")
  private static final long serialVersionUID = 1L;
  private String  prefix = "";

  public ResourcePropertyString(boolean isHashValues)
  {
    super(isHashValues);
  }

  public ResourcePropertyString()
  {
    super();
  }
  public ResourcePropertyString(String prefix)
  {
    super();
    this.prefix = prefix;
  }
  public ResourcePropertyString(String prefix, boolean isHashValues)
  {
    super(isHashValues);
    this.prefix = prefix;
  }

  @Override
  protected Object getImpl(Object key)
  {
    String resource = prefix+key.toString();
    String rc = Resource.getResourceString(Resource.class, resource);
    return rc;
  }

  protected String getPrefix()
  {
    return prefix;
  }
}
