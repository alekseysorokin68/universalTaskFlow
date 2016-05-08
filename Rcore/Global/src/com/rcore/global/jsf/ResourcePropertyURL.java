package com.rcore.global.jsf;

import com.rcore.global.Resource;

import java.net.URL;

public class ResourcePropertyURL
  extends ResourcePropertyString
{
  @SuppressWarnings("compatibility:2963062495582241637")
  private static final long serialVersionUID = 1L;

  public ResourcePropertyURL(String string, boolean isHashValues)
  {
    super(string, isHashValues);
  }

  public ResourcePropertyURL(String string)
  {
    super(string);
  }

  public ResourcePropertyURL()
  {
    super();
  }

  public ResourcePropertyURL(boolean b)
  {
    super(b);
  }
  @Override
  protected Object getImpl(Object key)
  {
    String resource = getPrefix()+key.toString();
    URL url = Resource.class.getResource(resource);
    String rc = url.toString();
    return rc;
  }
}
