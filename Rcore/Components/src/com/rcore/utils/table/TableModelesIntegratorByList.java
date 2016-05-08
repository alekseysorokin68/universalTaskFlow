package com.rcore.utils.table;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.adf.view.rich.model.FilterableQueryDescriptor;


public class TableModelesIntegratorByList
   implements TableModelesSimpleIntegrator, Serializable
{
  @SuppressWarnings("compatibility:-7650776646261201015")
  private static final long serialVersionUID = 1L;
  private transient List<Map<String, Object>> sourceList = null;
  private transient List<Map<String, Object>> currentList = null;
  //private List<String> filterAbleFields = null;
  private transient EmptyFilterableQueryDescriptor filterModel = null;
  //private int rangeSize = 25;

  public TableModelesIntegratorByList(
                    List<Map<String,Object>> sourceList
                    //,List<String> filterAbleFields
                    //,int rangeSize
  ) 
  {
    super();
    this.sourceList = sourceList;
    currentList = cloneSourceList();
//    this.filterAbleFields = filterAbleFields;
//    if (filterAbleFields != null && (!filterAbleFields.isEmpty())) 
//    {
//      filterModel = new CustomFilterableQueryDescriptor(filterAbleFields);
//    }
    //this.rangeSize = rangeSize;
  }
  // ----------------------------------

  private List<Map<String, Object>> cloneSourceList()
  {
    List<Map<String, Object>> rc = new ArrayList<Map<String, Object>>();
    for (Map<String,Object> item : sourceList) 
    {
      rc.add( (new HashMap<String,Object>(item)));
    }
    return rc;
  }
  //================================================

  public List<Map<String, Object>> getDataModel()
  {
    return currentList;
  }

  public FilterableQueryDescriptor getFilterModel()
  {
    return filterModel;
  }

//  public void sortListener(SortEvent sortEvent)
//  {
//    // TODO
//    System.out.println("@sortlestener");
//  }
//
//  public void selectionListener(SelectionEvent selectionEvent)
//  {
//    // TODO
//  }
//
//  public void makeCurrent(Object rowKey)
//  {
//    // TODO
//  }
//
//  public Object getSelectedRowData()
//  {
//    // TODO
//    return null;
//  }
}
