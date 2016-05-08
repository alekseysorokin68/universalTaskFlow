package com.rcore.utils.table;

import java.util.Map;

import oracle.adf.view.rich.component.rich.data.RichTable;

public interface FilterEventListener
{
  void filterEventExecute(RichTable table, Map<String,Object> filterCriteria);
}
