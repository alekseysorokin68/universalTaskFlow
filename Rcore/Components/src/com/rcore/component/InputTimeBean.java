package com.rcore.component;


import com.rcore.component.base.BaseComponentBean;
import com.rcore.global.DateTime;
import com.rcore.global.jsf.JSFUtils;

import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.render.ClientEvent;


public class InputTimeBean extends BaseComponentBean
{
  private Boolean initCurrentTime = null;
  private Boolean isAutoRefresh = null;
  private RichInputNumberSpinbox hhEditor;
  private RichInputNumberSpinbox mmEditor;
  private RichInputNumberSpinbox ssEditor;
  public InputTimeBean()
  {
    super();
  }
  
  @Override
  public void initDelegateBean() 
  {
    initCurrentTime = (Boolean)(JSFUtils.resolveExpression("#{attrs.initCurrentTime}"));
    if (initCurrentTime == null) 
    {
      initCurrentTime = false;
    }
    if (initCurrentTime) 
    {
      setCurrentTime();
    }
    isAutoRefresh = (Boolean)(JSFUtils.resolveExpression("#{attrs.autoRefresh}"));
    if (isAutoRefresh == null) 
    {
      isAutoRefresh = false;
    }
  }
  @Override
  public void destroyDelegateBean() 
  {
    ;
  }
  
  public String getScript()
  {
    String rc = 
      "function refreshImpl_{attrs.id}() \n" + 
      "{\n" + 
      "   itToServer();\n" + 
      "}\n" + 
      "window.setInterval(refreshImpl_{attrs.id},{attrs.refreshInterval});";
    rc = rc.replace("{attrs.id}",              JSFUtils.resolveExpression("#{attrs.id}")+"");
    rc = rc.replace("{attrs.refreshInterval}", JSFUtils.resolveExpression("#{attrs.refreshInterval}")+"");
    return rc;
  }
  
  
  
  public void autoRefresh(ClientEvent clientEvent)
  {
    setCurrentTime();
    addPartialTarget(hhEditor);
    addPartialTarget(mmEditor);
    addPartialTarget(ssEditor);
  }

  private void setCurrentTime()
  {
    int[] time = DateTime.getTimeArray();
    JSFUtils.setExpressionValue("#{attrs.hh}", time[0]+"");
    JSFUtils.setExpressionValue("#{attrs.mm}", time[1]+"");
    JSFUtils.setExpressionValue("#{attrs.ss}", time[2]+"");
  }
  
  public void setHhEditor(RichInputNumberSpinbox hhEditor)
  {
    this.hhEditor = hhEditor;
  }

  public RichInputNumberSpinbox getHhEditor()
  {
    return hhEditor;
  }

  public void setMmEditor(RichInputNumberSpinbox mmEditor)
  {
    this.mmEditor = mmEditor;
  }

  public RichInputNumberSpinbox getMmEditor()
  {
    return mmEditor;
  }

  public void setSsEditor(RichInputNumberSpinbox ssEditor)
  {
    this.ssEditor = ssEditor;
  }

  public RichInputNumberSpinbox getSsEditor()
  {
    return ssEditor;
  }
  
  public static void main(String[] args)
  {
    int[] time = DateTime.getTimeArray();
    System.out.println(time[0]);
    System.out.println(time[1]);
    System.out.println(time[2]);
  }
}
