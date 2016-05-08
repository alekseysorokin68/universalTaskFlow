package com.rcore.view;

import com.rcore.global.jsf.MapDynamicProperty;

import com.rcore.global.jsf.ResourcePropertyString;

import java.io.Serializable;

import java.util.Map;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.fragment.RichDeclarativeComponent;


public class TestInputTimeBean implements Serializable
{
  @SuppressWarnings("compatibility:3857421931874426271")
  private static final long serialVersionUID = 1L;
  //----------------------------------------------------
  private String hh="01";
  private String mm="02";
  private String ss="03";
  private transient RichDeclarativeComponent binding = null;;
  private Map label = null;
  private Map resource = new ResourcePropertyString();

  public TestInputTimeBean()
  {
    super();
    label = new MyHashMap();
  }

  public void setResource(Map resource)
  {
    this.resource = resource;
  }

  public Map getResource()
  {
    return resource;
  }

  public static class MyHashMap extends MapDynamicProperty 
  {
    @SuppressWarnings("compatibility:1035548090622034013")
    private static final long serialVersionUID = 1L;

    @Override
    protected Object getImpl(Object key)
    {
      return "_3_"+key+"_3";
    }
  }
  
  public Object get(Object key) 
  {
    return "_2_"+key+"_TT2";
  }

  public void valueChangeListener(ValueChangeEvent actionEvent)
  {
    System.out.println("valueChangeListener newValue : "+actionEvent.getNewValue());
    System.out.println("timeType : "+actionEvent.getComponent().getAttributes().get("timeType"));
  }
  

  public void setHh(String hh)
  {
    this.hh = hh;
  }

  public String getHh()
  {
    return hh;
  }

  public void setMm(String mm)
  {
    this.mm = mm;
  }

  public String getMm()
  {
    return mm;
  }

  public void setSs(String ss)
  {
    this.ss = ss;
  }

  public String getSs()
  {
    return ss;
  }

//  public Object get(Object property) 
//  {
//    if ("binding".equals(property)) 
//    {
//      return getBindingImpl();
//    }
//    return  null;
//  }
//  public void set(Object property,Object bind) 
//  {
//    if ("binding".equals(property)) 
//    {
//      setBindingImpl((RichDeclarativeComponent)bind);
//    }
//  }
//  public void put(Object property,Object bind) 
//  {
//    if ("binding".equals(property)) 
//    {
//      setBindingImpl((RichDeclarativeComponent)bind);
//    }
//  }
//  
//  public Object getProperty(Object property) 
//  {
//    if ("binding".equals(property)) 
//    {
//      return getBindingImpl();
//    }
//    return  null;
//  }
//  public void setProperty(Object property,Object bind) 
//  {
//    if ("binding".equals(property)) 
//    {
//      setBindingImpl((RichDeclarativeComponent)bind);
//    }
//  }
  

  private void setBindingImpl(RichDeclarativeComponent binding)
  {
    this.binding = binding;
    if (binding != null) System.out.println("@set bind="+binding.getClass().getName());
  }

  private RichDeclarativeComponent getBindingImpl()
  {
    if (binding != null) System.out.println("@get bind="+binding.getClass().getName());
    return binding;
  }

  public void setLabel(Map label)
  {
    this.label = label;
  }

  public Map getLabel()
  {
    return label;
  }
}
