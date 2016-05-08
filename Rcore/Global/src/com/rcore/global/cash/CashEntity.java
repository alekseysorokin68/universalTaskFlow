package com.rcore.global.cash;

import java.io.Serializable;

public class CashEntity<V> implements Serializable
{
    @SuppressWarnings("compatibility:4027751834056633299")
    private static final long serialVersionUID = 1L;
  
    private transient final V value;
    private final long dieAt;

    public CashEntity(V value, long dieAt)
    {
      super();
      this.value = value;
      this.dieAt = dieAt;
    }

    public boolean isExpired()
    {
      return System.currentTimeMillis() > dieAt;
    }
    @Override 
    public String toString() 
    {
      return value+"";
    }

    V getValue()
    {
      return value;
    }
}
