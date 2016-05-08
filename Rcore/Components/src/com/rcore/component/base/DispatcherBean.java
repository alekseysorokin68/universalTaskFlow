package com.rcore.component.base;

import javax.annotation.PreDestroy;

public interface DispatcherBean
{
  void initDispatcherBean();
  void destroyDispatcherBean();
  DelegateBean getDelegate(); 
}
