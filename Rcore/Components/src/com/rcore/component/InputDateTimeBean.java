package com.rcore.component;

import com.rcore.component.base.BaseComponentBean;
import com.rcore.global.jsf.JSFUtils;

public class InputDateTimeBean extends BaseComponentBean
{
  private String layout = null;
  private String space = null;
  public InputDateTimeBean()
  {
    super();
  }

  @Override
  public void initDelegateBean()
  {
    layout = (String)(JSFUtils.resolveExpression("#{attrs.layout}"));
    space = (String)(JSFUtils.resolveExpression("#{attrs.space}"));
  }

  @Override
  public void destroyDelegateBean()
  {
    ;
  }

  public String getWidthSeparator()
  {
    if ("horizontal".equals(layout)) 
    {
      return space;
    }
    else 
    {
      return "0";
    }
  }

  public String getHeightSeparator()
  {
    if ("horizontal".equals(layout)) 
    {
      return "0";      
    }
    else 
    {
      return space;
    }
  }
}
