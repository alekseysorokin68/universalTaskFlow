package com.rcore.utils.table;

import java.io.Serializable;

import java.util.Collections;
import java.util.Map;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;
import oracle.adf.view.rich.model.QueryDescriptor;


public class FilterableQueryDescriptor2 
    extends FilterableQueryDescriptor implements Serializable
  {
    @SuppressWarnings("compatibility:9006133379352097997")
    private static final long serialVersionUID = 1L;
    private transient MapChangeIndicator map = null;
    private transient RichTable richTable = null;

    public FilterableQueryDescriptor2(MapChangeIndicator map) 
    {
      super();
      this.map = map;
      
    }
    @Override
    public Map<String, Object> getFilterCriteria()
    {
      return map;
    }
    
    public MapChangeIndicator getMap()
    {
      return map;
    }
    
    @Override
    public void setFilterCriteria(Map<String, Object> map)
    {
      if (map == null) 
      {
        this.map = null;
      }
      if (map instanceof MapChangeIndicator) 
      {
        this.map = (MapChangeIndicator) map;
      }
      this.map = new MapChangeIndicator();
      this.map.putAll(map);
    }
    
    @Override
    public void addCriterion(String string)
    {
      ;
    }

    @Override
    public void changeMode(QueryDescriptor.QueryMode queryMode)
    {
      ;
    }

    @Override
    public ConjunctionCriterion getConjunctionCriterion()
    {
      return null;
    }

    @Override
    public String getName()
    {
      return null;
    }

    @Override
    public Map<String, Object> getUIHints()
    {
      return Collections.emptyMap();
    }

    @Override
    public void removeCriterion(oracle.adf.view.rich.model.Criterion criterion)
    {
      ;
    }

    @Override
    public AttributeCriterion getCurrentCriterion()
    {
      return null;
    }

    @Override
    public void setCurrentCriterion(AttributeCriterion attributeCriterion)
    {
      ;
    }

    public void invalidateRichTable()
    {
      RichTable table = getRichTable();
      if (table != null) 
      {
        AdfFacesContext.getCurrentInstance().addPartialTarget(table);
      }
      
    }

  public RichTable getRichTable()
  {
    return richTable;  
  }

  public void setRichTable(RichTable richTable)
  {
    this.richTable = richTable;
  }
}
