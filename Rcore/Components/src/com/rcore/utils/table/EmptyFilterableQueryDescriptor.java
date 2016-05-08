package com.rcore.utils.table;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;
import oracle.adf.view.rich.model.QueryDescriptor;

public class EmptyFilterableQueryDescriptor extends FilterableQueryDescriptor
{
  private Map<String,Object> filterCriteria = new HashMap<String,Object>();
  public EmptyFilterableQueryDescriptor()
  {
    super();
  }

  @Override public Map<String, Object> getFilterCriteria()
  {
    return filterCriteria;
  }

  @Override public void setFilterCriteria(Map<String, Object> map)
  {
    filterCriteria = map;
  }

  @Override public void addCriterion(String string)
  {
    ;
  }

  @Override public void changeMode(QueryDescriptor.QueryMode queryMode)
  {
    ;
  }

  @Override public ConjunctionCriterion getConjunctionCriterion()
  {
    return null;
  }

  @Override public String getName()
  {
    return null;
  }

  @Override public Map<String, Object> getUIHints()
  {
    return Collections.emptyMap();
  }

  @Override public void removeCriterion(oracle.adf.view.rich.model.Criterion criterion)
  {
    ;
  }

  @Override public AttributeCriterion getCurrentCriterion()
  {
    return null;
  }

  @Override public void setCurrentCriterion(AttributeCriterion attributeCriterion)
  {
    ;
  }
}
