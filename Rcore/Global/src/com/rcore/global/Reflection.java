package com.rcore.global;
// ШМЯ

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;


public class Reflection 
{
  public Reflection() 
  {
      super();
  }
    
  /**
   * Выполнение метода,доступа к которому нет обычным способом
   * @param obj
   * @param methodName
   * @param types
   * @param params
   * @return
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static Object runPrivateMethod(Object obj, 
                                        String methodName, 
                                        Class[] types, 
                                        Object[] params) 
  throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    Object rc = null;
    Class cl = obj.getClass();
    Method met = cl.getDeclaredMethod(methodName,types);
    if (met != null) 
    {
      met.setAccessible(true);
      rc = met.invoke(obj, params);
    }
    return rc;
  }
  /**
   * Выполнениe метода через Java Reflection.
   * При этом знать сигнатуту элементов заранее не обязательно.
   */
  public static Object runInvokeMethod(Object object, 
                                       String methodName,
                                       Object[] params)
  {
    Object result = null;
    Class classObject = object.getClass();
    Class<?>[] parameterTypes = new Class<?>[params.length];
    for (int i = 0; i < params.length; i++)
    {
      parameterTypes[i] = params[i].getClass();
    }
    try
    {
      result =
          classObject.getMethod(methodName, parameterTypes).invoke(object,
                                                                   params);
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  public static Object runInvokeMethod(Object object, 
                                       String methodName,
                                       Object[] params,
                                       Class[] parameterTypes
                                       )
  {
    Object result = null;
    Class classObject = object.getClass();
    try
    {
      result =
          classObject.getMethod(methodName, parameterTypes).invoke(object,
                                                                   params);
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  public static Object getProperty(Object object, String propertyName)
    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    return PropertyUtils.getProperty(object, propertyName);
  }
  //======
  //Method getter = new PropertyDescriptor(key, row.getClass()).getReadMethod();
  public static void main(String[] args) throws Exception
  {
    test1();
    test2();
  }

  private static void test2() throws Exception
  {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("key1", new Integer(11));
        Object value = PropertyUtils.getProperty(map, "key1");
        System.out.println("@value = "+value);
  }

  public static class Person 
  {
    private String name = "kaka";

    public void setName(String name)
    {
      this.name = name;
    }

    public String getName()
    {
      return name;
    }
  }
  private static void test1()  throws Exception
  {
//    Map<String,Object> map = new HashMap<String,Object>();
//    map.put("key1", new Integer(11));
//    Method getter = new PropertyDescriptor("key1", 
//                                           map.getClass(),
//                                           "get",
//                                           "put"
//                                           ).
//                          getReadMethod();
//    Object obj = getter.invoke(map);
//    System.out.println("@obj="+obj);
    Person person = new Person();
    Object value = PropertyUtils.getProperty(person, "name");
    System.out.println("@value = "+value);
  }
}
