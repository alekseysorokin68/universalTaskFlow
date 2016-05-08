package com.rcore.utils.table;

import java.util.List;
import java.util.Map;

import oracle.adf.view.rich.model.FilterableQueryDescriptor;


public interface TableModelesSimpleIntegrator
{
  List<Map<String,Object>> getDataModel();
  FilterableQueryDescriptor getFilterModel();
  
//  void sortListener(SortEvent sortEvent);
//  void selectionListener(SelectionEvent selectionEvent);
//  void makeCurrent(Object rowKey);
//  Object getSelectedRowData();
}
