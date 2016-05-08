package com.rcore.global.log;
// ШМЯ


import com.rcore.global.config.GlobalResourceBundle;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


public class FacesLogImpl implements FacesLog
{
  private static final LogLevel DEFAULT_LEVEL = LogLevel.INFO;
  private static final String PREFIX = "rcore.log.";
  private String id = null;

  public FacesLogImpl(Class classId)
  {
    this(classId.getName());
  }
  public FacesLogImpl(String id)
  {
    super();
    this.id = id;
  }
  
  public FacesLogImpl(Class classId,LogLevel active)
  {
    this(classId.getName(),active);
  }
  public FacesLogImpl(String id, LogLevel active)
  {
    super();
    this.id = id;
    List<LogLevel> list = new ArrayList<LogLevel>();
    if (active != null) {
      list.add(active);
    }
    setEnabledList(list);
  }

  @Override
  public void log(String mess, Throwable ex)
  {
    log(DEFAULT_LEVEL,mess,ex);
  }

  @Override
  public void log(String mess)
  {
    log(DEFAULT_LEVEL,mess);
  }

  @Override
  public void log(Throwable ex)
  {
    log(DEFAULT_LEVEL,ex);
  }

  @Override
  public void log(Throwable ex, String mess)
  {
    log(DEFAULT_LEVEL,ex,mess);
  }

  @Override
  public void log(LogLevel level, String mess, Throwable ex)
  {
    if (!isLevelEnabled(level)) return;
    ExternalContext ext = getContext();
    if (ext != null)
    {
      ext.log(prepareForSystemLog(level,mess), ex);
    }
    if (level.ordinal() < LogLevel.ERROR.ordinal()) {
      System.out.println(prepareForConsole(level,mess));
    }
    else 
    {
      System.err.println(prepareForConsole(level,mess));
    }
    ex.printStackTrace();
  }

  @Override
  public void log(LogLevel level, Throwable ex, String mess)
  {
    log(level, mess, ex);
  }

  @Override
  public void log(LogLevel level, String mess)
  {
    if (!isLevelEnabled(level)) return;
    ExternalContext ext = getContext();
    if (ext != null)
    {
      ext.log(prepareForSystemLog(level,mess));
    }
    if (level.ordinal() < LogLevel.ERROR.ordinal()) {
      System.out.println(prepareForConsole(level,mess));
    }
    else 
    {
      System.err.println(prepareForConsole(level,mess));
    }
  }

  @Override
  public void log(LogLevel level, Throwable ex)
  {
    log(level,"", ex);
  }
  
  public void setEnabledList(List<LogLevel> list)
  {
    sList = new ArrayList<String>();
    if (list == null || list.size() == 0) 
    {
      return;
    }
    for(LogLevel level : list) 
    {
      sList.add(level.toString());
    }
    return;
  }

  private static List<String> sList = null;
  
  @Override
  public boolean isLevelEnabled(LogLevel level)
  {
    if (sList == null) {
      GlobalResourceBundle bundle = GlobalResourceBundle.getInstance();
      String list = bundle.getProperty("log.level.enabled");
      if (list == null) 
      {
        return true;
      }
      String[] tok = list.split(",");
      sList = new ArrayList<String>();
      for(int i=0; i < tok.length; i++) 
      {
        sList.add(tok[i].trim());
      }
    }
    boolean rc = sList.contains(level.toString());
    return rc;
  }
  //--------------------------------------------
  private ExternalContext getContext()
  {
    FacesContext context = FacesContext.getCurrentInstance();
    if (context == null)
    {
      return null;
    }
    ExternalContext ext = context.getExternalContext();
    if (ext == null)
    {
      return null;
    }
    return ext;
  }
  static private String getTime()
  {
    StringBuffer rc = new StringBuffer();
    GregorianCalendar cal = new GregorianCalendar();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);
    rc.append(day).append("/").append(month).append("/").append(year);
    rc.append(" ");
    rc.append(hour).append(":").append(min).append(":").append(sec);
    return rc.toString();
  }
  
  protected String prepareForConsole(LogLevel level, String mess)
  {
    StringBuilder rc = new StringBuilder();
//    Throwable t = new Throwable(); 
//    StackTraceElement trace[] = t.getStackTrace(); 
//    if (trace.length > 2) 
//    { 
//      StackTraceElement element = trace[2]; 
//      System.out.format("[%s] [%s, %d] %s", element.getClassName(), element.getMethodName(), element.getLineNumber(), mess); 
//    } 
//    else {
//      System.out.format("[no info] %s", mess); 
//    } 
    rc.append(PREFIX).append(level.toString()).append(" (") .append(this.id).
      append(") ") .append(getTime()).append(" > ");
    
    if (mess == null)
    {
      mess = "";
    }
    rc.append(mess);
    return rc.toString();
  }
  
  protected String prepareForSystemLog(LogLevel level, String mess)
  {
    StringBuilder rc = new StringBuilder();
    rc.append(PREFIX).append(level.toString()).append(" (").append(this.id).append(") ");
    if (mess == null)
    {
      mess = "";
    }
    rc.append(mess);
    return rc.toString();
  }
  //------------------

  @Override
  public void info(String mess)
  {
    log(LogLevel.INFO,mess);
  }

  @Override
  public void error(String mess)
  {
    log(LogLevel.ERROR,mess);
  }

  @Override
  public void debug(String mess)
  {
    log(LogLevel.DEBUG,mess);
  }
  //-----------------------------------------------------------
  public static String getPrintStackTrace(Throwable ex) 
  {
    return getPrintStackTrace(ex, "\n", -1);
  }
  public static String getPrintStackTrace(Throwable ex,String separator) 
  {
    return getPrintStackTrace(ex, separator, -1);
  }
  public static String getPrintStackTrace(Throwable ex,String separator,int maxStack) 
  {
    StackTraceElement[] list = ex.getStackTrace();
    StringBuilder rc = new StringBuilder(ex.getMessage());
    rc.append(separator);
    int index = 0;
    for (StackTraceElement element : list) 
    {
      if (maxStack != -1) {
        index++;
        if (index > maxStack) 
        {
          break;
        }
      }
      rc.append(element.toString()).append(separator);
    } // for;
    return rc.toString();
  }
  
  public static String getFullPrintStackTrace(Throwable ex) 
  { 
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    pw.close();
    return sw.toString();
  } 
  //-----------------------------------------------------------
  public static void main(String[] args)
  {
//    FacesLog log = new FacesLogImpl(FacesLogImpl.class,null);
//    log.log(LogLevel.DEBUG,"debug0");
    test1();
  }
  private static void test1()
  {
    Exception ex = new Exception("Error1");
    System.out.println(getFullPrintStackTrace(ex));
  }
  //==========================================
}
