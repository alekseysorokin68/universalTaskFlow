package com.rcore.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Набор статических методов для разбиения строк на подстроки
 */
public class Token
{
  /**
   * Разделить строку на подстроки в соответствии с разделителями заданными
   * 2-м параметром
   * 
   * @param str - Исходная строка
   * @param delim - Список разделителей
   * @return - массив подстрок
   */
  public static String[] aToken(String str, String delim)
  {
    if (StringFunc.isEmpty(str))
    {
      return new String[0];
    }
    StringTokenizer strtok = new StringTokenizer(str, delim, false);
    int count = strtok.countTokens();
    String result[];
    result = new String[count];
    for (int i = 1; i <= count; i++)
      result[i - 1] = strtok.nextToken();
    return result;
  }

  /**
   * Разделить строку на подстроки в соответствии с разделителями заданными
   * 2-м параметром
   * 
   * @param str - Исходная строка
   * @param delim - Список разделителей
   * @return - ArrayList(String) - список подстрок
   */
  public static ArrayList<String> aTokenList(String str, String delim)
  {
    StringTokenizer strtok = new StringTokenizer(str, delim, false);
    int count = strtok.countTokens();
    ArrayList<String> result = new ArrayList<String>();
    for (int i = 1; i <= count; i++)
    {
      result.add(strtok.nextToken());
    }
    return result;
  }

  public static List<Integer> aTokenIntList(String str, String delim)
  {
    StringTokenizer strtok = new StringTokenizer(str, delim, false);
    int count = strtok.countTokens();
    ArrayList<Integer> result = new ArrayList<Integer>(count);
    for (int i = 1; i <= count; i++)
    {
      int n = Integer.parseInt(strtok.nextToken());
      result.add(n);
    }
    return result;
  }

  public static Set<Integer> aTokenIntSet(String str, String delim)
  {
    StringTokenizer strtok = new StringTokenizer(str, delim, false);
    int count = strtok.countTokens();
    Set<Integer> result = new HashSet<Integer>(count);
    for (int i = 1; i <= count; i++)
    {
      int n = Integer.parseInt(strtok.nextToken());
      result.add(n);
    }
    return result;
  }

  /**
   * Разделить строку на подстроки в соответствии с разделителями заданными
   * 2-м параметром. Сами разделители тоже будут входить в выходной массив.
   * 
   * @param str - Исходная строка
   * @param delim - Список разделителей
   * @return - массив подстрок
   */
  public static String[] aTokenDelim(String str, String delim)
  {
    StringTokenizer strtok = new StringTokenizer(str, delim, true);
    int count = strtok.countTokens();
    String result[];
    result = new String[count];
    for (int i = 1; i <= count; i++)
      result[i - 1] = strtok.nextToken();
    return result;
  }

  /**
   * Разделить строку на подстроки в соответствии с разделителями заданными
   * 2-м параметром. Сами разделители тоже будут входить в выходной массив.
   * 
   * @param str - Исходная строка
   * @param delim - Список разделителей
   * @return - список подстрок
   */
  public static ArrayList<String> aTokenDelimList(String str, String delim)
  {
    StringTokenizer strtok = new StringTokenizer(str, delim, true);
    int count = strtok.countTokens();
    ArrayList<String> result = new ArrayList<String>();
    for (int i = 1; i <= count; i++)
      result.add(strtok.nextToken());
    return result;
  }

  /**
   * Разделить строку вида : a=b c=d на HasMap вида : a-->b c-->d
   * 
   * @param src - Исходня строка
   * @param delim1 - Разделители первого уровня
   * @param delim2 - Разделители второго уровня
   * @return - HashMap - разобранная строка
   */
  public static HashMap<String, String> aToken2(String src, String delim1, 
                                                String delim2)
  {
    HashMap<String, String> rc = new HashMap<String, String>();
    if (StringFunc.isEmpty(src))
    {
      return rc;
    } // if isEmpty(src)
    String[] atok1 = Token.aToken(src, delim1);

    for (int i = 0; i < atok1.length; i++)
    {
      String s = atok1[i];
      String[] atok2 = Token.aToken(s, delim2);
      if (atok2.length < 2) {
        continue;
      }
      rc.put(atok2[0], atok2[1]);
    } // for
    return rc;
  }

  public static String join(String[] tokens, String seps)
  {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < tokens.length; i++)
    {
      String tok = tokens[i];
      if (StringFunc.isEmpty(tok))
      {
        continue;
      }
      buf.append(tok).append(seps);
    } // for
    String rc = buf.toString();
    if (rc.endsWith(seps))
    {
      rc = rc.substring(0, rc.length() - seps.length());
    }
    return rc;
  }

  public static String join(List<Integer> tokens, String seps)
  {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < tokens.size(); i++)
    {
      String tok = tokens.get(i) + "";
      if (StringFunc.isEmpty(tok))
      {
        continue;
      }
      buf.append(tok).append(seps);
    } // for
    String rc = buf.toString();
    if (rc.endsWith(seps))
    {
      rc = rc.substring(0, rc.length() - seps.length());
    }
    return rc;
  }
  
  public static String joinListOfString(List<String> tokens, String seps)
  {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < tokens.size(); i++)
    {
      String tok = tokens.get(i) + "";
      if (StringFunc.isEmpty(tok))
      {
        continue;
      }
      buf.append(tok).append(seps);
    } // for
    String rc = buf.toString();
    if (rc.endsWith(seps))
    {
      rc = rc.substring(0, rc.length() - seps.length());
    }
    return rc;
  }

  public static <E extends Number> String join(Set<E> tokens, String seps)
  {
    StringBuffer buf = new StringBuffer();
    for (Number item: tokens)
    {
      buf.append(item).append(seps);
    } // for
    String rc = buf.toString();
    if (rc.endsWith(seps))
    {
      rc = rc.substring(0, rc.length() - seps.length());
    }
    return rc;
  }
}
