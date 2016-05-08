package com.rcore.utils.table;


import com.rcore.model.dynamic_list.DynamicListSignature;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.myfaces.trinidad.model.SortCriterion;
import org.apache.myfaces.trinidad.model.SortableModel;


public class CollectionModel2 extends SortableModel implements Serializable
{
  @SuppressWarnings("compatibility:328890142283886156")
  private static final long serialVersionUID = 1L;
  private transient DynamicListSignature<Map<String, Object>> dynamicListSignature = null;

  public CollectionModel2(DynamicListSignature<Map<String, Object>> dynamicListSignature)
  {
    super(dynamicListSignature);
    this.dynamicListSignature = dynamicListSignature;
  }

  @Override
  public boolean isSortable(java.lang.String propName)
  {
    return false;
  }

  @Override
  public List<SortCriterion> getSortCriteria()
  {
    return Collections.emptyList();
  }

  @Override
  public void setSortCriteria(java.util.List p1)
  {
    ;
  }

  @Override
  public void clearLocalCache()
  {
    // TODO
    super.clearLocalCache();
    if (dynamicListSignature != null) {
      dynamicListSignature.refresh();
      dynamicListSignature.clear();
    }
  }
  
  @Override
  public Object getRowData(Object p1) { 
    Object rc = null;
    try {
      rc = super.getRowData(p1);
    }
    catch(Throwable ex) 
    {
      ex.printStackTrace();
    }
    return rc;
  }
  
  @Override
  public Object getRowData() { 
    Object rc = null;
    try {
      rc = super.getRowData();
    }
    catch(Throwable ex) 
    {
      ex.printStackTrace();
    }
    return rc;
  }
  
  /**
   * Кто - то требует наличие следующего метода ?
   * NoSuchMethodError - getRowData(Ljava/lang/Object)Ljava/lang/Object
   */
  public Object[] getRowData(Object[] key) 
  {
    Object[] rc = null;
    StringBuilder message = new StringBuilder("*** Вызов метода getRowData(Object[]) c параметрами = ");
    if (key != null) 
    {
      message.append(Arrays.toString(key));
    }
    else 
    {
      message.append(" null");
    }
    Exception ex = new Exception(message.toString());
    ex.printStackTrace();
    return rc;
  }
  

  public DynamicListSignature<Map<String, Object>> getDynamicListSignature()
  {
    return dynamicListSignature;
  }

  public void dispose()
  {
//    DynamicListSignature<Map<String, Object>> dynamicList = getDynamicListSignature();
//    if (dynamicList != null) 
//    {
//      dynamicList.dispose();
//    }
    clearLocalCache();
  }
}
