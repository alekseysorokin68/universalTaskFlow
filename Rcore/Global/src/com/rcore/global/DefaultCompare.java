package com.rcore.global;
// ШМЯ


import java.sql.Time;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Базовый класс для сравнения объектов при сортировке
 */
public class DefaultCompare implements Comparator
{
  /**
   * Константа для условия "меньше"
   */
  public static final int LT = -1;

  /**
   * Константа для условия "равно"
   */
  public static final int EQ = 0;

  /**
   * Константа для условия "больше"
   */
  public static final int GT = 1;

  /**
   * метод сравнения объектов
   *
   * @param obj1 - первый объект
   * @param obj2 - второй объект
   * @param isAscending -
   *            true - сортировка по возрастанию, 
   *            false - сортировка по убыванию.
   * @return результат сравнения - т.е. LT,GT,EQ
   */
  public static int compareNormal(Object obj1, Object obj2,
                                  boolean isAscending)
  {
    if (isAscending)
    {
      return compareNormal(obj1, obj2);
    }
    else
    {
      return compareDescending(obj1, obj2);
    }
  }

  /**
   * метод сравнения объектов
   *
   * @param obj1 - первый объект
   * @param obj2 - второй объект
   * @return результат сравнения - т.е. LT,GT,EQ
   */
  public static int compareNormal(Object obj1, Object obj2)
  {
    if      (obj1 == null && obj2 == null) 
    {
      return EQ;
    }
    else if (obj1 != null && obj2 == null) 
    {
      return GT;
    }
    else if (obj1 == null && obj2 != null) 
    {
      return LT;
    }
    //-----------------------------
    // Оба значения не null:
    //-----------------------------
    int result = EQ;
    if ((obj1 instanceof Number) && (obj2 instanceof Number))
    {
      if (((Number) obj1).doubleValue() < ((Number) obj2).doubleValue())
      {
        result = LT;
      }
      else if (((Number) obj1).doubleValue() >
               ((Number) obj2).doubleValue())
      {
        result = GT;
      }
    }
    else if ((obj1 instanceof String) && (obj2 instanceof String))
    {
      if (((String) obj1).compareTo((String) obj2) < 0)
      {
        result = LT;
      }
      else if (((String) obj1).compareTo((String) obj2) > 0)
      {
        result = GT;
      }
    }
    else if ((obj1 instanceof Date) && (obj2 instanceof Date))
    {
      long value1 = ((Date)obj1).getTime();
      long value2 = ((Date)obj2).getTime();
      if (value1 < value2) 
      {
        result = LT;
      }
      else if (value1 > value2) 
      {
        result = GT;
      }
    }
    else if ((obj1 instanceof java.sql.Date) && (obj2 instanceof java.sql.Date))
    {
      long value1 = ((java.sql.Date)obj1).getTime();
      long value2 = ((java.sql.Date)obj2).getTime();
      if (value1 < value2) 
      {
        result = LT;
      }
      else if (value1 > value2) 
      {
        result = GT;
      }
    }
    else if ((obj1 instanceof Timestamp) && (obj2 instanceof Timestamp))
    {
      long value1 = ((Timestamp)obj1).getTime();
      long value2 = ((Timestamp)obj2).getTime();
      if (value1 < value2) 
      {
        result = LT;
      }
      else if (value1 > value2) 
      {
        result = GT;
      }
    }
    else if ((obj1 instanceof Time) && (obj2 instanceof Time))
    {
      long value1 = ((Time)obj1).getTime();
      long value2 = ((Time)obj2).getTime();
      if (value1 < value2) 
      {
        result = LT;
      }
      else if (value1 > value2) 
      {
        result = GT;
      }
    }

    return result;
  } // compareNormal

  /**
   * метод сравнения объектов для сортировки по убыванию
   *
   * @param obj1 - первый объект
   * @param obj2 - второй объект
   * @return результат сравнения - т.е. LT,GT,EQ
   */
  public static int compareDescending(Object obj1, Object obj2)
  {
    int rc = compareNormal(obj2, obj1);
    return rc;
  } // public static int compareDescending(Object obj1,Object obj2)

  /**
   * метод сравнения объектов для сортировки по возрастанию
   *
   * @param obj1 - первый объект
   * @param obj2 - второй объект
   * @return результат сравнения - т.е. LT,GT,EQ
   */
  @Override
  public int compare(Object obj1, Object obj2)
  {
    return compareNormal(obj1, obj2);
  }

  // ------------------------------------

  @SuppressWarnings("unchecked")
  public static void sort(List data, Comparator<Object> comparator)
  {
    Object[] arr = data.toArray();
    Arrays.sort(arr, comparator);
    data.clear();
    for (int i = 0; i < arr.length; i++)
    {
      data.add(arr[i]);
    }
  }
  public static <T> void sortTyped(List<T> data, Comparator<T> comparator) 
  {
    Comparator<Object> c = new WrapComparator(comparator);
    Object[] arr = data.toArray();
    Arrays.sort(arr, c);
    data.clear();
    for (int i = 0; i < arr.length; i++)
    {
      data.add((T)arr[i]);
    }
  }
  
//  public static <T> void sortTyped(Class cl, List<T> data, Comparator<T> comparator) 
//  {
//    T[] arr = (T[]) Array.newInstance(cl, data.size());
//    for ( : )
//    Arrays.sort(arr, comparator);
//    data.clear();
//    for (int i = 0; i < arr.length; i++)
//    {
//      data.add(arr[i]);
//    }
//  }
  
  private static class WrapComparator<T> implements Comparator<T> 
  {
    private Comparator<T> comparator = null;

    private WrapComparator(Comparator<T> comparator) 
    {
      super();
      this.comparator = comparator;
    }
    public int compare(Object o1, Object o2)
    {
      return comparator.compare((T)o1, (T)o2);
    }
  }

  /**
   * Метод сравнения объектов, анализирующий их возможные значения null
   */
  public static boolean equals(Object o1, Object o2)
  {
    if      (o1 == null && o2 == null)
    {
      return true;
    }
    else if (o1 != null && o2 == null)
    {
      return false;
    }
    else if (o1 == null && o2 != null)
    {
      return false;
    }
    else
    {
      return o1.equals(o2);
    }
  }
  //=============
  public static void main(String[] args)
  {
    List<Integer> list = new ArrayList<Integer>();
    list.add(5);
    list.add(1);
    list.add(2);
    Comparator<Integer> c = new Comparator<Integer>() 
    {
      public int compare(Integer o1, Integer o2)
      {
        return compareDescending(o1, o2);
      }
    };
    sortTyped(list, c);  
    System.out.println(list);
  }
}
