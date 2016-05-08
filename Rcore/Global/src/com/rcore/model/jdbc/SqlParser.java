package com.rcore.model.jdbc;


import com.rcore.global.DateTime;
import com.rcore.global.MapWithOrderKeys;
import com.rcore.global.adf.NullDb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SqlParser
{
  private SqlParser()
  {
    super();
  }

  /**
   * Parses a query with named parameters.  The parameter-index mappings are
   * put into the map, and the
   * parsed query is returned.  DO NOT CALL FROM CLIENT CODE.  This
   * method is non-private so JUnit code can
   * test it.
   * @param query    query to parse
   * @param paramMap map to hold parameter-index mappings
   * @return the parsed query
   */
  public static String parseNamedParameters(String query,
                                            Map<String, int[]> paramMap)
  {
    // I was originally using regular expressions, but they didn't work well
    // for ignoring parameter-like strings inside quotes.
    MapWithOrderKeys<String, LinkedList<Integer>> paramMapAux = new MapWithOrderKeys<String, LinkedList<Integer>>();
    int length = query.length();
    StringBuffer parsedQuery = new StringBuffer(length);
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    int index = 1;

    for (int i = 0; i < length; i++)
    {
      char c = query.charAt(i);
      if (inSingleQuote)
      {
        if (c == '\'')
        {
          inSingleQuote = false;
        }
      }
      else if (inDoubleQuote)
      {
        if (c == '"')
        {
          inDoubleQuote = false;
        }
      }
      else
      {
        if (c == '\'')
        {
          inSingleQuote = true;
        }
        else if (c == '"')
        {
          inDoubleQuote = true;
        }
        else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1)))
        {
          int j = i + 2;
          while (j < length && Character.isJavaIdentifierPart(query.charAt(j)))
          {
            j++;
          }
          String name = query.substring(i + 1, j);
          c = '?'; // replace the parameter with a question mark
          i += name.length(); // skip past the end if the parameter

          LinkedList<Integer> indexList = paramMapAux.get(name);
          if (indexList == null)
          {
            indexList = new LinkedList<Integer>();
            paramMapAux.put(name, indexList);
          }
          indexList.add(index);

          index++;
        }
      }
      parsedQuery.append(c);
    }

    // replace the lists of Integer objects with arrays of ints
    //for (Map.Entry<String, List<Integer>> entry: paramMapAux.entrySet())
    for (String key: paramMapAux.getKeysList())
    {
      LinkedList<Integer> list = paramMapAux.get(key);
      int[] indexes = new int[list.size()];
      int i = 0;
      for (Integer x: list)
      {
        indexes[i++] = x;
      }
      paramMap.put(key, indexes);
    }

    return parsedQuery.toString();
  }

  public static List<String> getNamedParametersList(String query)
  {
    List<String> rc = new ArrayList<String>();
    int length = query.length();
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    //int index = 1;

    for (int i = 0; i < length; i++)
    {
      char c = query.charAt(i);
      if (inSingleQuote)
      {
        if (c == '\'')
        {
          inSingleQuote = false;
        }
      }
      else if (inDoubleQuote)
      {
        if (c == '"')
        {
          inDoubleQuote = false;
        }
      }
      else
      {
        if (c == '\'')
        {
          inSingleQuote = true;
        }
        else if (c == '"')
        {
          inDoubleQuote = true;
        }
        else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1)))
        {
          int j = i + 2;
          while (j < length && Character.isJavaIdentifierPart(query.charAt(j)))
          {
            j++;
          }
          String name = query.substring(i + 1, j);
          if (!rc.contains(name))  rc.add(name);
        }
      }
    }
    return rc;
  }

  public static FieldInfo[] getFieldsInfo(String query, Connection con) throws SQLException
  {
    FieldInfo[] rc = null;
    List<String> paramList = getNamedParametersList(query);
    NamedParameterStatement ps = new NamedParameterStatement(con,query);
    try
    {
      if (paramList != null && paramList.size() > 0)
      {
        for (int i = 0; i < paramList.size(); i++)
        {
          ps.setObject(paramList.get(i), null);
        }
      }
      ResultSetMetaData meta = ps.getMetaData();
      int columnCount = meta.getColumnCount();
      rc = new FieldInfo[columnCount];
      for (int i = 1; i <= columnCount; i++)
      {
        rc[i - 1] = new FieldInfo(meta,i);
      } // for
    }
    finally
    {
      if (ps != null)
      {
        ps.close();
      }
    }
    return rc;
  }

  public static long timeStude(String sql, Connection con, Map<String, Object> params, StringBuilder recordCount)
  {
    NamedParameterStatement ps = null;
    ResultSet rs = null;
    long rc = 0;
    try
    {
      ps = new NamedParameterStatement(con,sql);
      setParameters(ps,params);
      //----
      rc = System.currentTimeMillis();
      rs = ps.executeQuery();
      long maxRecordCount = Long.parseLong(recordCount.toString());
      long realRecordCount = 0;
      int colCount = rs.getMetaData().getColumnCount();
      while (rs.next())
      {
        for (int i = 1; i <= colCount; i++)
        {
          try
          {
            rs.getObject(i);
          }
          catch (Exception ex)
          {
            System.err.println("Err6 : " + ex.getMessage());
          }
        }
        maxRecordCount--;
        realRecordCount++;
        if (maxRecordCount <= 0)
        {
          break;
        }
      }
      rc = System.currentTimeMillis() - rc;
      recordCount.setLength(0);
      recordCount.append(realRecordCount);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (rs != null)
      {
        try
        {
          rs.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
      if (ps != null)
      {
        try
        {
          ps.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    }
    return rc;
  }

  public static String getErrorQuery(Connection con,
                                     String query,
                                     Map<String,Object> params // или null
                                     )
  {
    NamedParameterStatement ps = null;
    ResultSet rs = null;
    String rc = null;
    StringBuilder sql = new StringBuilder();
    sql.append("select * from (").append(query).append(") where rownum < 1");

    try
    {
      ps = new NamedParameterStatement(con,query);
      setParameters(ps, params);
      rs = ps.executeQuery();
      rc = null;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      rc = e.getMessage();
    }
    finally
    {
      if (rs != null)
      {
        try
        {
          rs.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
      if (ps != null)
      {
        try
        {
          ps.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    }
    return rc;
  }

  public static String getErrorQueryWithCheckNames(Connection con, String sql)
    throws SQLException
  {
    Map<String, int[]> map = new HashMap<String, int[]>();
    SqlParser.parseNamedParameters(sql, map);
    Map<String, Object> params = null;
    if (map.size() > 0)
    {
      params = new HashMap<String, Object>();
      Set<String> set = map.keySet();
      for (String key: set)
      {
        params.put(key, null);
      }
    }
    return getErrorQueryWithCheckNames(con,sql,params);
  }

  public static String getErrorQueryWithCheckNames(Connection con,
                                            String query,
                                            Map<String, Object> params // или null
                                            )
  {
    NamedParameterStatement ps = null;
    ResultSet rs = null;
    String rc = null;
    StringBuilder sql = new StringBuilder();
    sql.append("select * from (").append(query).append(") where rownum < 1");

    try
    {
      ps = new NamedParameterStatement(con,sql.toString());
      setParameters(ps, params);
      rs = ps.executeQuery();
      rc = null;
      // Теперь проверим имена (на соответствие соглашению об именах)
      ResultSetMetaData meta = rs.getMetaData();
      int colCount = meta.getColumnCount();
      for (int i = 1; i <= colCount; i++)
      {
        String colName = meta.getColumnName(i);
        rc = checkName(colName);
        if (rc != null)
        {
          break;
        }
      } // for
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      rc = e.getMessage();
    }
    finally
    {
      if (rs != null)
      {
        try
        {
          rs.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
      if (ps != null)
      {
        try
        {
          ps.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    }
    return rc;
  }

  public static String checkName(String name)
  {
    String rc = null;
    if (name == null || "".equals(name))
    {
      rc = "Пустое имя";
    }
    else if (name.length() > 30)
    {
      rc = "Длина имени '" + name + "' > 30";
    }
    else if (!Character.isJavaIdentifierStart(name.charAt(0)) || ('$' == name.charAt(0)))
    {
      rc = "Первый символ имени '" + name + "' не удовлетворяет соглашению об именах oracle";
    }
    else
    {
      for (int i = 1; i < name.length(); i++)
      {
        char ch = name.charAt(i);
        if (!Character.isJavaIdentifierPart(ch))
        {
          rc = (i + 1) + "- й символ имени '" + name + "' не удовлетворяет соглашению об именах (" + ch + ")";
          break;
        }
      } // for
    }
    return rc;
  }

  public static String getTableFromSql(String sql)
  {
    if (sql == null)
    {
      return null;
    }
    sql = sql.trim();
    if (sql.length() == 0)
    {
      return null;
    }
    sql = sql.toLowerCase();
    //----------------------------
    // 1 Заменяем все управляющие символы на пробел:
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < sql.length(); i++)
    {
      Character ch = sql.charAt(i);
      if (Character.isWhitespace(ch))
      {
        ch = ' ';
      }
      buf.append(ch);
    }
    sql = buf.toString();
    // Удаляем двойные пробелы:
    while (sql.contains("  "))
    {
      sql = sql.replace("  ", " ");
    }
    // Ищем ' from '
    final String FROM = " from ";
    int index = sql.indexOf(FROM);
    if (index < 0)
    {
      return null;
    }
    index += (FROM.length());
    int index2 = sql.indexOf(' ', index);
    if (index2 < index)
    {
      index2 = sql.length();
    }
    // Извлекаем:
    String rc = sql.substring(index, index2);
    // select a from b
    // 012345678911111
    //           01234
    index2 = rc.indexOf(',');
    if (index2 > 0)
    {
      rc = rc.substring(0, index2);
    }
    rc = rc.trim();
    return rc;
  }

  //-------------------------
  private static void setParameters(NamedParameterStatement st,
                                    Map<String, Object> params)
    throws SQLException
  {
    if (params != null)
    {
      Object param = null;
      try
      {
        Set<String> keys = params.keySet();
        for (String key : keys)
        {
          param = params.get(key);
          if (param instanceof NullDb)
          {
            st.setNull(key, ((NullDb) param).getType());
          }
          else
          {
            if (param instanceof java.util.Date)
            {
              param =
                  DateTime.java_util_date2java_sql_date((java.util.Date) param);
            }
            st.setObject(key, param);
          }
        } // for
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        if (ex instanceof SQLException)
        {
          throw (SQLException) ex;
        }
        else
        {
          throw new SQLException(ex.getMessage());
        }
      }
    }
  }
  //==================================================================================
  public static void main(String[] args)
  {
    //test1();
    //test2();
    test3();
  }

  static void test1()
  {
    String sql = "select :field f, :b bf from dual where bf=:b";
    Map<String, int[]> map = new HashMap<String, int[]>();
    String sql2 = parseNamedParameters(sql, map);
    System.out.println(sql2);
    System.out.println("**************");
    System.out.println(Arrays.toString(map.get("field")));
    System.out.println(Arrays.toString(map.get("b")));
  }

  static void test2()
  {
    new SqlParser();
    String sql = "select :field f, :b bf from dual where bf=:b";
    List<String> list = getNamedParametersList(sql);
    System.out.println(list);
  }

  private static void test3()
  {
    String sql = 
    " select * from \n" + 
    " (\n" + 
    "   select 1 a,   2 b from dual\n" + 
    " )\n" + 
    " where a=:a and b=:b\n";
    
    MapWithOrderKeys<String,int[]> indexMap = new MapWithOrderKeys<String, int[]>();
    parseNamedParameters(sql,indexMap);
    List<String> source = indexMap.getKeysList();
    List<String> rc = new ArrayList<String>(source.size());
    for(String item: source) rc.add(item);
    System.out.println("@="+indexMap.getKeysList());
  }
}
