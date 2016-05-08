package com.rcore.global.cash;

public class CashEntityFrequency<V>
{
  private final V value;
  private final long dieAt;
  private long frequency = 0;

  public CashEntityFrequency(V value, long dieAt)
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
    return "("+value+","+frequency+")";
  }

  V getValue()
  {
    return value;
  }

  long getFrequency()
  {
    return frequency;
  }
  void increment() 
  {
    frequency++;
  }
  
}
