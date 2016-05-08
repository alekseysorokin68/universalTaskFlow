package com.rcore.component.base;

import com.rcore.global.adf.AdfBaseBean;
import com.rcore.global.jsf.JSFUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class BaseComponentBean extends AdfBaseBean 
             implements DispatcherBean,DelegateBean
{
  protected DispatcherBean dispatcher = null;
  private Map<String,DelegateBean> mapBeanById = new HashMap<String,DelegateBean>();
  public BaseComponentBean()
  {
    super();
  }
  public boolean isDispatcher() 
  {
    return (dispatcher == null);
  }
  @PostConstruct
  public void initDispatcherBean() 
  {
    ;
  }
  @PreDestroy
  public void destroyDispatcherBean() 
  {
    Collection<DelegateBean> beans = mapBeanById.values();
    for(DelegateBean bean : beans) 
    {
      bean.destroyDelegateBean();
    } // for
    mapBeanById.clear();
  }
  //---------------------------
  public abstract void initDelegateBean();
  public abstract void destroyDelegateBean();
  //---------------------------------
  protected DelegateBean getNewInstanceDelegateBean() 
  {
    try
    {
      DelegateBean rc = (DelegateBean)(this.getClass().newInstance());
      return rc;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      new RuntimeException(e.getMessage());
    }
    return null;
  }
  //----------------------------------
  @Override
  public DelegateBean getDelegate() 
  {
    String id = getId();
    if (id == null) 
    {
      return null;
    }
    DelegateBean delegateBean = mapBeanById.get(id);
    if (delegateBean == null) 
    {
      delegateBean = getNewInstanceDelegateBean();
      if (delegateBean instanceof BaseComponentBean) {
        ((BaseComponentBean)delegateBean).dispatcher = this;
      }
      mapBeanById.put(id, delegateBean);
      delegateBean.initDelegateBean();
    }
    return delegateBean;
  }
  public DelegateBean getDelegate(String id) 
  {
    return mapBeanById.get(id);
  }
  protected String getId() 
  {
    String rc = null;
    try 
    {
      rc = (String)JSFUtils.resolveExpression("#{attrs.id}");
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    return rc;
  }
}
