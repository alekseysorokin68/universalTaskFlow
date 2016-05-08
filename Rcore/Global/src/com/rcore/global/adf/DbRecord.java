package com.rcore.global.adf;
// ШМЯ

import com.rcore.global.DateTime;

import java.io.Serializable;

import java.sql.SQLException;

import java.util.Date;
import java.util.HashMap;

public final class DbRecord extends HashMap<String, String> 
  implements Serializable
{
  @SuppressWarnings("compatibility:-5182741990714084563")
  private static final long serialVersionUID = 1L;

   public int getInt(String field) {
      int rc = 0;
      String value = get(field);
      if (value == null) {
         return rc;
      }
      try {
         rc = Integer.parseInt(value);
      }
      catch (NumberFormatException e) {
         System.out.println("field = '"+value+"'" );
         throw e;
      }
      return rc;
   }
   
   public Date getDate(String field)
   {
    Date rc = null;
    String value = get(field);
    if (value == null)
    {
      return rc;
    }
    try
    {
      rc = DateTime.getJavaUtilDate(value);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("field = '" + value + "'");
    }
    return rc;
   }
   
   public void put(Object key,Object value) 
   {
     if (key != null) 
     {
       key = ((String)key).toUpperCase();
     }
     super.put((String) key, (String) value);
   } // public void put(Object key,Object value)

   public String get(Object key) {
     if (key != null) 
     {
       key = ((String)key).toUpperCase();
     }
     String rc = super.get(key);
     return rc;
   }

   public char getChar(String key) {
      String rc = get(key);
      if (rc == null) {
         return 0;
      }
      if (rc.equals("")) {
         return 0;
      }
      return rc.charAt(0);
   }
   
   public void dispose() {
     this.clear();
   }

  public long getLong(String field) {
    long rc = 0;
    String value = get(field);
    if (value == null) {
       return rc;
    }
    try {
       rc = Long.parseLong(value);
    }
    catch (NumberFormatException e) {
       System.out.println("field = '"+value+"'" );
       throw e;
    }
    return rc;
  }
  
  public oracle.jbo.domain.Number getNumber(String field) {
    oracle.jbo.domain.Number rc = null;
    String s = get(field);
    if (s != null) {
      try {
        rc = new oracle.jbo.domain.Number(s);
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return rc;
  }
   
}