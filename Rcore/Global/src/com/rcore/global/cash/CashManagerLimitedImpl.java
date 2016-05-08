package com.rcore.global.cash;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class CashManagerLimitedImpl<K, V>  implements CashManagerSimple<K, V>, Serializable
{
  @SuppressWarnings("compatibility:-1364199566512810645")
  private static final long serialVersionUID = 1L;
  //---------------------------------------------
  private transient Map<K, CashEntityFrequency<V>> storage = new ConcurrentHashMap<K, CashEntityFrequency<V>>();
  private long defaultLifeTime = 10000L;
  private int  maxElements = Integer.MAX_VALUE;
  private boolean isCleanedByReachingMaximum = false;
  //-----
  public CashManagerLimitedImpl(long defaultLifeTime,
                                int maxElements,
                                boolean isCleanedByReachingMaximum) 
  {
    super();
    if (defaultLifeTime <= 0) 
    {
      defaultLifeTime = Long.MAX_VALUE/2;
    }
    this.defaultLifeTime = defaultLifeTime;
    if (maxElements > 0) 
    {
      this.maxElements = maxElements;
    }
    this.isCleanedByReachingMaximum = isCleanedByReachingMaximum;
  }
  //---------------------------------------
  @Override 
  public V get(K key)
  {
    CashEntityFrequency<V> e = storage.get(key);
    if (e != null)
    {
      if (!e.isExpired())
      {
        e.increment();
        return e.getValue();
      }
    }
    return null;
  }

  @Override 
  public void put(K key, V value)
  {
    if (storage.size() >= maxElements) 
    {
      if (isCleanedByReachingMaximum)
      {
        storage.clear();
      }
      else 
      {
        removeElementWithMinimumFrequency();  
      }
      
    }
    storage.put(key,
                 new CashEntityFrequency<V>(value, System.currentTimeMillis() + defaultLifeTime));
  }

  @Override 
  public void remove(K key)
  {
    storage.remove(key);
  }

  @Override 
  public void clear()
  {
    storage.clear();
  }

  @Override
  public void removeExpiredObjects()
  {
    for (K key: storage.keySet())
    {
      if (storage.get(key).isExpired())
      {
        storage.remove(key);
      }
    }
  }
  //----
  private void removeElementWithMinimumFrequency()
  {
    long frequency = Long.MAX_VALUE;
    K key =  null;
    boolean isRemove = false;
    Set<Map.Entry<K, CashEntityFrequency<V>>> entries =  storage.entrySet();
    for (Map.Entry<K, CashEntityFrequency<V>> entry : entries) 
    {
      K                      kItem = entry.getKey();
      CashEntityFrequency<V> vItem = entry.getValue();
      if (vItem.isExpired()) 
      {
        storage.remove(kItem);
        isRemove = true;
      }
      long value = vItem.getFrequency();
      if (value < frequency) 
      {
        frequency = value;
        key = kItem;
      }
    } // for
    //---------------------------
    if (!isRemove && key != null) 
    {
      storage.remove(key);
    }
  }
  //----
  @Override 
  public String toString() 
  {
    return storage+"";
  }
  
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
  {
    ois.defaultReadObject();
    if (storage == null) 
    {
      storage = new ConcurrentHashMap<K, CashEntityFrequency<V>>();
    }
  }
//=======================================================
  public static void main(String[] args) throws Exception
  {
    test1();
  }
  private static void test1() throws Exception
  {
    CashManagerLimitedImpl<String,Integer> obj = 
        new CashManagerLimitedImpl<String,Integer>(1000,4,false);
    obj.put("1",1);
    obj.put("2",2);
    obj.put("3",3);
    Thread.currentThread().sleep(1001);
    obj.put("4",4);
    obj.put("5",5);
    obj.put("6",6);
    obj.put("7",7);
    obj.get("4");
    obj.put("8",8);
    System.out.println(obj.storage);
  }
}
