package com.rcore.model.dynamic_list;

import java.util.HashMap;

import oracle.jbo.AttributeDef;
import oracle.jbo.server.RowImpl;
import oracle.jbo.server.ViewObjectImpl;


public final class RowWrap extends HashMap<String,Object>
{
  public RowWrap(RowImpl row, ViewObjectImpl vo,
                 boolean fieldNameAsAttributeName)
  {
    super();
    //--------------------
    if (row != null)
    {
      AttributeDef[] attrKeys = vo.getAttributeDefs();
      if (!fieldNameAsAttributeName)
      {
        // 1 Наполняем по columnName
        for (int i = 0; i < attrKeys.length; i++)
        {
          AttributeDef attrDef = attrKeys[i];
          String name = attrDef.getName();
          String columnName = attrDef.getColumnName();
          Object value = row.getAttribute(name);
          if (value != null)
          {
            put(columnName, value);
          }
        } // for
      }
      else
      {
        // 2 Наполняем по name
        for (int i = 0; i < attrKeys.length; i++)
        {
          AttributeDef attrDef = attrKeys[i];
          String name = attrDef.getName();
          Object value = row.getAttribute(name);
          if (value != null)
          {
            put(name, value);
          }
        } // for
      }
    }
  }
}
