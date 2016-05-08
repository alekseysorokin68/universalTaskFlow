package com.rcore.global.utils;
// ШМЯ

import com.rcore.global.Resource;

import java.io.IOException;
import java.io.InputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import oracle.jbo.server.DBTransaction;


public final class MyUtils {
    private static Properties applicationProps = new Properties();
    static 
    {
      InputStream is = Resource.getResourceInputStream(Resource.class,"/com/rdtex/rcore/model/Application.xml");

      try {
        applicationProps.loadFromXML(is);
        is.close();
      }
      catch (InvalidPropertiesFormatException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    } 
    
    public static String getApplicationProperty(String key) 
    {
      return applicationProps.getProperty(key);
    }
    
    public static Object getValueOfBindVariable(String bindVar,
                                                Object[] params) {
        // in params each element is Object[]
        // in that array expected 2 elements, String - name and Object - value
        if (bindVar == null) {
            return null;
        }
        for (Object o : params) {
            if (o instanceof Object[] && ((Object[])o).length == 2) {
                Object[] param = (Object[])o;
                if (bindVar.equals(param[0])) {
                    return param[1];
                }
            }
        }
        return null;
    }
    
  public static long getNextSeq(DBTransaction trans, String seq)
    throws SQLException
  {
    long rc = 1;
    String sql = "SELECT " + seq + ".NEXTVAL val FROM dual";
    Statement st = trans.createStatement(1);
    ResultSet rs = st.executeQuery(sql);
    while (rs.next())
    {
      rc = rs.getInt("val");
      break;
    }
    return rc;
  }
}
