package com.rcore.model.dynamic_list;


import com.rcore.global.DynamicList;

public abstract class DynamicListSignature<E> extends DynamicList<E>
{
  private static final long serialVersionUID = 4855694447199653133L;
  private String signature = "";
  public DynamicListSignature()
  {
    super();
  }
  //-----------------------------------
  public void setSignature(String signature)
  {
    this.signature = signature;
  }
  public String getSignature()
  {
    return signature;
  }
  
  abstract public void refresh(); // Переопределяется в дочерних
  //abstract public void dispose(); // Переопределяется в дочерних
}
