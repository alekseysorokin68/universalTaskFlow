package com.rcore.global;
// ШМЯ

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Класс для работы с множествами
 */
public final class Abundance
{
  /**
   * Пересечение множеств
   */
  public static <E> List<E> crossing(List<E> list1, List<E> list2)
  {
    List<E> rc = new ArrayList<E>();
    if (list1 == null) return rc;
    if (list2 == null) return rc;
    int n = list1.size();
    for (int i = 0; i < n; i++)
    {
      E o1 = list1.get(i);
      if (list2.contains(o1))
      {
        if (!rc.contains(o1))
        {
          rc.add(o1);
        }
      }
    } // for (i)
    return rc;
  }

  /**
   * Пересечение множеств
   */
  public static <E> List<E> crossing(E[] list1, E[] list2)
  {
    List<E> rc = new ArrayList<E>();
    if (list1 != null && list2 != null) 
    {
      rc = crossing(Arrays.asList(list1), Arrays.asList(list2));
    }
    return rc;
  }

  /**
   * Пересечение множеств
   */
  public static <E> List<E> crossing(E[] list1, List<E> list2)
  {
    List<E> rc = new ArrayList<E>();
    if (list1 != null && list2 != null) 
    {
      rc = crossing(Arrays.asList(list1), list2);
    }
    return rc;
  }

  // ==============================
  /**
   * Объединение множеств
   */
  public static <E> List<E> amount(List<E> list1, List<E> list2)
  {
    List<E> rc = new ArrayList<E>();
    if (list1 == null) list1 = new ArrayList<E>();
    if (list2 == null) list2 = new ArrayList<E>();
    for (int i = 0; i < list1.size(); i++)
    {
      E o = list1.get(i);
      if (!rc.contains(o))  rc.add(o);
    } // for (i)
    
    for (int i = 0; i < list2.size(); i++)
    {
      E o = list2.get(i);
      if (!rc.contains(o))  rc.add(o);
    } // for (i)
    return rc;
  }

  /**
   * Объединение множеств
   */
  public static <E> List<E> amount(E[] list1, List<E> list2)
  {
    List<E> rc = new ArrayList<E>();
    if (list1 == null) 
    {
      rc = amount((List<E>)null, list2);
    }
    else 
    {
      rc = amount(Arrays.asList(list1), list2);
    }
    return rc;
  }

  /**
   * Объединение множеств
   */
  public static <E> List<E> amount(E[] list1, E[] list2)
  {
    List<E> p1 = null;
    List<E> p2 = null;
    if (list1 != null) p1 = Arrays.asList(list1);
    if (list2 != null) p2 = Arrays.asList(list2);
    return amount(p1,p2);
  }
  
  public static <E> Set<E> amount(Set<E> set1, Set<E> set2) 
  {
    List<E> p1 = null;
    List<E> p2 = null;
    if (set1 != null) 
    {
      p1 = new ArrayList<E>();
      for (E item : set1) 
      {
        p1.add(item);
      }
    }
    if (set2 != null) 
    {
      p2 = new ArrayList<E>();
      for (E item : set2) 
      {
        p2.add(item);
      }
    }
    List<E> list = amount(p1,p2);
    Set<E> rc = new HashSet<E>();
    if (list != null) 
    {
      rc.addAll(list);  
    }
    return rc;
  }

  // ==============================================

  /**
   * Разность множеств
   */
  public static <E> List<E> setDifference(List<E> list1, List<E> list2)
  {
    if (list1 == null) list1 = new ArrayList<E>();
    if (list2 == null) list2 = new ArrayList<E>();
    List<E> rc = new ArrayList<E>();
    for (E item : list1) 
    {
      if (!list2.contains(item)) 
      {
        rc.add(item);
      }
    }
    return rc;
  }

  /**
   * Объединение множеств
   */
  public static <E> List<E> setDifference(E[] list1, List<E> list2)
  {
    List<E> p1 = null;
    if (list1 != null) p1 = Arrays.asList(list1);
    if (list2 == null) list2 = new ArrayList<E>();
    return setDifference(p1, list2);
  }

  /**
   * Разность множеств
   */
  public static <E> List<E> setDifference(List<E> list1, E[] list2)
  {
    List<E> p2 = null;
    if (list1 == null) list1 = new ArrayList<E>();
    if (list2 != null) p2 = Arrays.asList(list2);
    return setDifference(list1, p2);
  }

  /**
   * Разность множеств
   */
  public static <E> List<E> setDifference(E[] list1, E[] list2)
  {
    List<E> p1=null,p2=null;
    if (list1 != null) p1 = Arrays.asList(list1);
    if (list2 != null) p2 = Arrays.asList(list2);
    return setDifference(p1,p2);
  }
  // ================= Compare Elements
//  public static void main(String[] args)
//  {
//    List<String> list1 = new ArrayList<String>();
//    List<String> list2 = new ArrayList<String>();
//    list1.add("1");
//    list1.add("2");
//    list1.add("3");
//    list2.add("6");
//    list2.add("4");
//    list2.add("5");
//    List<String> a = amount(list1, list2);
//    System.out.println(a + "");
//  }

} // public final class Abundance