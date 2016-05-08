package com.rcore.utils.table;


import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;
import oracle.adf.view.rich.model.QueryDescriptor;

import org.apache.commons.beanutils.PropertyUtils;


public class CustomFilterModelWithoutSql  extends FilterableQueryDescriptor
{
  private CustomMap filterCriteria = new CustomMap();
  private GetTableInterface getTableInterface = null;
  protected FilterEventListener filterEventListener = null;
  protected List<Object> startTableModel = null;

  public CustomFilterModelWithoutSql(String tableId)
  {
    super();
    this.getTableInterface = new CallBackTableByTableId(tableId);
    this.filterEventListener = new CustomFilterEventDefault();
  }
  public CustomFilterModelWithoutSql(String tableId, FilterEventListener customFilterEvent)
  {
    super();
    this.getTableInterface = new CallBackTableByTableId(tableId);
    this.filterEventListener = customFilterEvent;
  }
  
  public CustomFilterModelWithoutSql(GetTableInterface callBackTable)
  {
    super();
    this.getTableInterface = callBackTable;
    this.filterEventListener = new CustomFilterEventDefault();
  }
  
  public CustomFilterModelWithoutSql(GetTableInterface callBackTable, FilterEventListener customFilterEvent)
  {
    super();
    this.getTableInterface = callBackTable;
    this.filterEventListener = customFilterEvent;
  }
  //====================================================================
  private class CustomFilterEventDefault implements  FilterEventListener
  {
    public void filterEventExecute(RichTable table, Map<String, Object> filterCriteria)
    {
      try
      {
        CustomFilterModelWithoutSql.this.defaultFilterEventExecute();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  @Override public Map<String, Object> getFilterCriteria()
  {
    return filterCriteria;
  }

  @Override public void setFilterCriteria(Map<String, Object> map)
  {
    if (map == null)
    {
      this.filterCriteria = null;
    }
    else if (map instanceof CustomMap)
    {
      this.filterCriteria = (CustomMap) map;
    }
    else
    {
      this.filterCriteria = new CustomMap();
      this.filterCriteria.putAll(map);
    }
  }
  //------------
  @Override public void addCriterion(String string) {;}
  @Override public void changeMode(QueryDescriptor.QueryMode queryMode) {;}
  @Override public ConjunctionCriterion getConjunctionCriterion() {return null;}
  @Override public String getName() { return null;}
  @Override public Map<String, Object> getUIHints() { return Collections.emptyMap();}
  @Override public void removeCriterion(oracle.adf.view.rich.model.Criterion criterion) {;}
  @Override public AttributeCriterion getCurrentCriterion() {return null;}
  @Override public void setCurrentCriterion(AttributeCriterion attributeCriterion) {;}
  //------------
  private boolean isEmptyValue(Object value) 
  {
    return (value == null || value.toString().trim().length()==0);
  }
  private boolean isEmptyFilterCriteria() 
  {
    boolean rc = true;
    Set<String> keys = filterCriteria.keySet();
    for (String key : keys) 
    {
      if (!isEmptyValue(filterCriteria.get(key))) 
      {
        rc = false;
        break;
      }
    }
    return rc;
  }
  
  private void defaultFilterEventExecute()
    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    RichTable table = getTableInterface.getTable();
    if (table == null) return;
    List<Object> startTableModel = getStartTableModel();
    //---
    boolean isEmptyFilterCriteria = isEmptyFilterCriteria();
    if (isEmptyFilterCriteria) 
    {
      table.setValue(startTableModel);
      AdfFacesContext.getCurrentInstance().addPartialTarget(table);
      return;
    }
    StringBuilder sKey = new StringBuilder();
    Object value = getFirstNotEmptyValue(sKey);
    String key = sKey.toString();
    clearFilterCriteria(key);
    //--------------------------
    List<Object> newTableModel = executeFilter(key,value);
    
    table.setValue(newTableModel);
    AdfFacesContext.getCurrentInstance().addPartialTarget(table);
  }
  //---------------------------------------------------------------------
  protected List<Object> getStartTableModel()
  {
    if (startTableModel == null) 
    {
      startTableModel = (List<Object>) getTableInterface.getTable().getValue();
    }
    return startTableModel;
  }
  
  protected List<Object> executeFilter(String key, Object value)
    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    List<Object> newTableModel = new ArrayList<Object>();
    List<Object> startTableModel = getStartTableModel();
    //System.out.println("@startTableModel = "+startTableModel);
    for (Object row : startTableModel)
    {
      //Object rowValue = row.get(key);
      Object rowValue = PropertyUtils.getProperty(row, key);
      if (rowValue == null && value == null)
      {
        newTableModel.add(row);
      }
      else if (rowValue != null && value == null)
      {
        ;
      }
      else if (rowValue == null && value != null)
      {
        ;
      }
      else if (rowValue != null && value != null)
      {
        if (rowValue.toString().toUpperCase().startsWith(value.toString().toUpperCase()))
        {
          newTableModel.add(row);
        }
      }
    } // for
    return newTableModel;
  }
  private int getFilterColumnCount() 
  {
    RichTable table = getTableInterface.getTable();
    Counter counter = new Counter();
    findColumns(table,counter);
    return counter.count;
  }

  private Object getFirstNotEmptyValue(StringBuilder sKey)
  {
    Set<String> keys = filterCriteria.keySet();
    for (String key : keys) 
    {
      Object value = filterCriteria.get(key);
      if (value != null && value.toString().length() > 0) 
      {
        sKey.append(key);
        return value;
      }
    }
    return null;
  }

  private void clearFilterCriteria(String key)
  {
    Set<String> keys = filterCriteria.keySet();
    String[] aKeys = keys.toArray(new String[keys.size()]);
    for (int i=0; i < aKeys.length; i++) 
    {
      String item = aKeys[i];
      if (!key.equals(item)) 
      {
        filterCriteria.remove(item);
      }
    }
  }

  private void findColumns(UIComponent parent, 
                           Counter counter)
  {
    if (parent == null) return;
    if (parent instanceof RichColumn) 
    {
      RichColumn column = (RichColumn) parent;
      if (column.isRendered() && column.isFilterable() && (!column.isRowHeader())) 
      {
        counter.count++;
      }
    }
    Iterator<UIComponent> iter = parent.getChildren().iterator();
    while (iter.hasNext()) 
    {
      UIComponent child = iter.next();
      findColumns(child, counter);
    }
    return;
  }
  //==============================================================================
  public class CustomMap extends HashMap<String,Object>
  {
    private int putCount = 0;
    private static final long serialVersionUID = 1L;
    @Override
    public Object put(String key, Object value)
    {
      Object rc = super.put(key, value);
      putCount++;
      if (putCount == getFilterColumnCount()) 
      {
        filterEventListener.filterEventExecute(
              getTableInterface.getTable(), 
              filterCriteria);
        putCount = 0;
      }
      return rc;
    }
  }
  private static class CallBackTableByTableId implements GetTableInterface 
  {
    private String tableId = null;

    private CallBackTableByTableId(String tableId) 
    {
      super();
      this.tableId = tableId;
    }

    public RichTable getTable()
    {
      RichTable rc = null;
      FacesContext context = FacesContext.getCurrentInstance();
      rc = (RichTable) context.getViewRoot().findComponent(tableId);
      return rc;
    }
  }
  private static class Counter 
  {
    private int count = 0;
  }
}

