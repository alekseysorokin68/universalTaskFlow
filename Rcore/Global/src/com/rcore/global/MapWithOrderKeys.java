package com.rcore.global;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapWithOrderKeys<K extends Serializable, V extends Serializable> extends HashMap<K,V>
{
  private static final long serialVersionUID = 1;
  
  private List<K> keysList = new ArrayList<K>(); // Это нормально, что поле возможно не сериализуемо


  public MapWithOrderKeys(java.util.Map<K, V> map)
  {
    super(map);
  }

  public MapWithOrderKeys()
  {
    super();
  }

  public MapWithOrderKeys(int i)
  {
    super(i);
  }

  public MapWithOrderKeys(int i, float f)
  {
    super(i, f);
  }
  //----------------------

  @Override
  public V put(K key, V value)
  {
    if (!containsKey(key))
    {
      keysList.add(key);
    }
    return super.put(key, value);
  }

  @Override
  public V remove(Object key)
  {
    keysList.remove(key);
    return super.remove(key);
  }
  
  @Override 
  public void clear() 
  {
    keysList.clear();
    super.clear();
  }

  @Override
  public void putAll(Map m)
  {
    //super.putAll(m);
    throw new UnsupportedOperationException("Not Supported");
  }
  //--------- Новые методы --------------

  public List<K> getKeysList()
  {
    return keysList;
  }
  public List<V> getValuesList()
  {
    List<V> rc = null;
    if (keysList == null) 
    {
      return rc;
    }
    rc = new ArrayList<V>();
    for (K item : keysList) 
    {
      rc.add(get(item));
    }
    return rc;
  }
  //==========================
  public static void main(String[] args)
    throws ClassNotFoundException
  {
    //    Set<String> set = new HashSet<String>();
    //    set.add("9999");
    //    set.add("0");
    //    System.out.println(Arrays.toString(set.toArray()));

    //    MapWithOrderKeys obj = new MapWithOrderKeys();
    //    obj.put("9999", 0);
    //    obj.put("0", 1);
    //    System.out.println(Arrays.toString(obj.keySet().toArray()));
    //    System.out.println(Arrays.toString(obj.getKeysList().toArray()));

    MapWithOrderKeys obj = new MapWithOrderKeys();
    obj.put("9999", 0);
    obj.put("0", 1);

    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    String filename = "c:/temp/ts1.ser";
    try
    {
      fos = new FileOutputStream(filename);
      out = new ObjectOutputStream(fos);
      out.writeObject(obj);
      out.close();
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    //-----------------------------------------
    FileInputStream fis = null;
    ObjectInputStream in = null;
    try
    {
      fis = new FileInputStream(filename);
      in = new ObjectInputStream(fis);
      obj = (MapWithOrderKeys) in.readObject();
      in.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    //---------------------------------------
    System.out.println(Arrays.toString(obj.keySet().toArray()));
    System.out.println(Arrays.toString(obj.getKeysList().toArray()));
  }
}
