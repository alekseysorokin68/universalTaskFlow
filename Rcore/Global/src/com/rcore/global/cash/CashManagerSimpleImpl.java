package com.rcore.global.cash;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CashManagerSimpleImpl<K, V>  implements CashManagerSimple<K, V>, Serializable
{
  @SuppressWarnings("compatibility:-2986006744361002107")
  private static final long serialVersionUID = 1L;
  //---------------------------------------------
  private transient Map<K, CashEntity<V>> storage = new ConcurrentHashMap<K, CashEntity<V>>();
  private long defaultLifeTime = 10000L;
  //-----
  public CashManagerSimpleImpl(long defaultLifeTime) 
  {
    super();
    if (defaultLifeTime <= 0) 
    {
      defaultLifeTime = Long.MAX_VALUE/2;
    }
    this.defaultLifeTime = defaultLifeTime;
  }
  //---------------------------------------
  @Override 
  public V get(K key)
  {
    CashEntity<V> e = storage.get(key);
    if (e != null)
    {
      if (!e.isExpired())
      {
        return e.getValue();
      }
    }
    return null;
  }

  @Override 
  public void put(K key, V value)
  {
    storage.put(key,
                 new CashEntity<V>(value, System.currentTimeMillis() + defaultLifeTime));
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
      storage = new ConcurrentHashMap<K, CashEntity<V>>();
    }
  }
//========================================================
  public static void main(String[] args) throws Exception
  {
    test1();
  }
  private static void test1() throws Exception
  {
    //Map<String,Integer> m;  m.put(1, 1);
    
    CashManagerSimple<String,Integer> obj = new CashManagerSimpleImpl<String,Integer>(3000);
    obj.put("1",1);
    obj.put("2",2);
    obj.put("3",3);
    System.out.println(obj.get("1"));
    System.out.println(obj.get("2"));
    System.out.println(obj.get("3"));
    Thread.currentThread().sleep(3000);
    System.out.println(obj.get("1"));
    System.out.println(obj.get("2"));
    System.out.println(obj.get("3"));
    //---------
    obj.put("1",1);
    Thread.currentThread().sleep(2000); // null
    obj.put("2",2);
    Thread.currentThread().sleep(2000); // null 
    obj.put("3",3);
    Thread.currentThread().sleep(2000);  // 3
    obj.removeExpiredObjects();
    System.out.println(obj.toString());
  }
//========================================
}
