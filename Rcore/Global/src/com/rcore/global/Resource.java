package com.rcore.global;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import java.util.Properties;


/**
 * Набор утилитных функций для работы с различными ресурсами
 */
public final class Resource
{
  public static final String ENCODING = "UTF-8";

  /**
   * Получить ресурс в виде строки
   *
   * @param c   - класс с помощью которого можно загрузить ресурс
   * @param map - адресс ресурса
   * @return - Строка - значение ресурса
   */
  public static String getResourceString(Class c, String map, String encoding)
  {
    String rc = null;
    byte[] arr = getResourceByteArray(c, map);

    if (arr != null)
    {
      try
      {
        rc = new String(arr, encoding);
      }
      catch (UnsupportedEncodingException e)
      {
        e.printStackTrace();
      }
    }
    return rc;
  }
  
  public static String getResourceString(Class c, String map) 
  {
    return getResourceString(c, map, ENCODING);
  }

  /**
   * Получить ресурс в виде объекта Properties
   * 
   * @param c   - класс с помощью которого можно загрузить ресурс
   * @param map - адресс ресурса
   * @return - java.util.Properties - значение ресурса
   */
  public static java.util.Properties getResourceProperties(Class c, 
                                                           String map)
  {
    java.util.Properties rc = null;
    InputStream is = getResourceInputStream(c, map);
    if (is != null)
    {
      try
      {
        rc = new java.util.Properties();
        rc.load(is);
      }
      catch (java.io.IOException ioe)
      {
        rc = null;
      }
    }
    return rc;
  }

  /**
   * Получить ресурс в виде объекта Properties, если в ресурсе есть текст на
   * русском языке
   * 
   * @param c - класс с помощью которого можно загрузить ресурс
   * @param map - адресс ресурса
   * @return - java.util.Properties - значение ресурса
   */
  public static java.util.Properties getResourcePropertiesExtend(Class c, 
                                                                 String map)
  {
    java.util.Properties rc = null;
    String str = getResourceString(c, map);
    if (str != null)
    {
      rc = new java.util.Properties();
      loadPropertiesFromString(rc, str);
    }
    return rc;
  }

  /**
   * Загрузить строку в Properties
   * 
   * @param p - Properties для загрузки строки
   * @param str - строка с данными
   */
  public static void loadPropertiesFromString(Properties p, String str)
  {
    str = str.replace("\r\n", "\n");
    str = str.replace("\n\r", "\n");
    str = str.replace("\\\n", "");
    String[] lines = str.split("\\n");
    for (int i = 0; i < lines.length; i++)
    {
      String line = lines[i];
      if (line == null)
        continue;
      String lineTrim = line.trim();
      if (lineTrim.length() == 0)
        continue;
      if (lineTrim.startsWith("#"))
        continue;
      // ------------------------------------
      String[] key_value = line.split("=");
      if (key_value.length < 2)
        continue;
      String key = key_value[0].trim();
      String value = key_value[1];
      p.setProperty(key, value);
    }
  }

  /**
   * Загрузить ресурс как последовательность байтов
   * 
   * @param c - класс с помощью которого можно загрузить ресурс
   * @param map - адресс ресурса
   * @return - последовательность байтов
   */
  public static byte[] getResourceByteArray(Class c, String map)
  {
    byte[] rc = null;

    InputStream is = getResourceInputStream(c, map);

    if (is != null)
    {
      rc = inputStreamToByteArray(is);
    }

    return rc;
  }

  /**
   * Загрузить ресурс как входной поток
   * 
   * @param c - Класс с помощью которого можно загрузить ресурс
   * @param map - Адресс ресурса
   * @return - InputStream - входной поток
   */
  public static InputStream getResourceInputStream(Class c, String map)
  {
    InputStream rc = null;
    if (map == null)
    {
      return rc;
    }

    if (c == null)
    {
      c = Resource.class;
    }
//    if (!map.startsWith("/"))
//    {
//      map = "/" + map;
//    }
    rc = c.getResourceAsStream(map);
    return rc;
  }

  /**
   * Проверяет наличие
   * 
   * @param c - класс с помощью которого можно загрузить ресурс
   * @param path - ключевая строка
   * @return - true - ресурс входит в classpath, false - не входит
   */
  public static boolean isResourceExists(Class c, String path) 
  {
    boolean rc = false;
    try 
    {
      rc = (c.getResource(path) != null);
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    return rc;
  }
  
  /**
   * Преобразование InputStream в String
   * 
   * @param is - InputStream - входной поток
   * @return - String - строка, полученная из входного потока
   */
  public static String inputStreamToString(InputStream is)
  {
    return inputStreamToString(is,ENCODING);
  }
  
  public static String inputStreamToString(InputStream is, String encoding)
  {
    byte[] buf = inputStreamToByteArray(is);
    String rc = null;
    try
    {
      rc = new String(buf, encoding);
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return rc;
  }
  
  public static String readerToString(Reader reader)
    throws IOException
  {
    char[] arr = new char[8 * 1024];
    StringBuilder buffer = new StringBuilder();
    int numCharsRead = 0;
    while (true)
    {
      numCharsRead = reader.read(arr, 0, arr.length);
      if (numCharsRead == -1) 
      {
        break;
      }
      buffer.append(arr, 0, numCharsRead);
    }
    //reader.close();
    String rc = buffer.toString();
    return rc;
  }

  /**
   * Преобразование InputStream в массив байтов
   * 
   * @param is - InputStream - входной поток
   * @return - byte[] - массив байтов
   */
  public static byte[] inputStreamToByteArray(InputStream is)
  {
    byte[] rc = null;
    if (is != null)
    {
      try
      {
        byte buf[] = new byte[4096];
        java.io.ByteArrayOutputStream baos = 
          new java.io.ByteArrayOutputStream();
        int read = 0;

        read = is.read(buf);
        while (read > 0)
        {
          baos.write(buf, 0, read);
          read = is.read(buf);
        }
        rc = baos.toByteArray();
      }
      catch (java.io.IOException ioe)
      {
        rc = null;
      }
    }
    return rc;
  }
  //========================
  public static void main(String[] args)
    throws Exception
  {
    Reader reader = new StringReader("Test1_Цапля");
    String str = readerToString(reader);
    reader.close();
    System.out.println(str);
  }
  
}

