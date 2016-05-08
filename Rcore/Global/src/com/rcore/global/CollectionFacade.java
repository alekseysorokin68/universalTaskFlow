package com.rcore.global;

import java.util.HashSet;
import java.util.Set;

/**
 * Фасад для работы с массивами и коллекциями
 */
public class CollectionFacade
{
  public static boolean contains(Object[] list, Object find) 
  {
    return (indexOf(list, find) >= 0);
  }
  public static int indexOf(Object[] list, Object find)
  {
    int rc = -1;
    if (find != null)
    {
      for (int i = 0; i < list.length; i++)
      {
        if (find.equals(list[i]))
        {
          rc = i;
          break;
        }
      } // for
    }
    else
    {
      for (int i = 0; i < list.length; i++)
      {
        if (list[i] == null)
        {
          rc = i;
          break;
        }
      } // for
    }
    return rc;
  }
  public static <T> Set<T> array2Set(T[] arr) 
  {
    Set<T> rc = null;
    if (arr == null) 
    {
      return rc;
    }
    rc = new HashSet<T>();
    for (T item : arr) 
    {
      rc.add(item);
    }
    return rc;
  }
  
  public static <T> Object[] set2Array(Set<T> set)
  {
    Object[] rc = null;
    if (set == null) 
    {
      return rc;
    }
    rc = set.toArray();
    return rc;
  }
  public static <T> String array2String(T[] arr) 
  {
    return array2String(arr,",");
  }
  public static <T> String array2String(T[] arr, String separator) 
  {
    StringBuilder rc = new StringBuilder();
    for (T item : arr) 
    {
      rc.append(item + "").append(separator);
    }
    if (rc.length() > 0) 
    {
      rc.setLength(rc.length() - separator.length());
    }
    return rc.toString();
  }
  
  //========================================
  public static void main(String[] args)
  {
    Set<Integer> set = new HashSet<Integer>();
    set.add(1);
    set.add(3);
    Object[] arr = set2Array(set);
    System.out.println(arr[0].getClass().getName());
  }
}
