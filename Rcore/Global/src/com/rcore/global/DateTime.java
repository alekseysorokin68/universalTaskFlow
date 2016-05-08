package com.rcore.global;
// ШМЯ

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Stack;
import java.util.TimeZone;


/**
 * Класс для работы с датой и временем.
 */
public final class DateTime
{
  private static final Locale russianLocale = new Locale("ru","RU");
  private static final TimeZone russianTimeZone = TimeZone.getTimeZone("Europe/Moscow");
  /**
   * Список месяцев в именительном падеже
   */
  public static String[] getMonthList()
  {
    String[] rc =
    { "Январь", 
      "Февраль", 
      "Март", 
      "Апрель", 
      "Май", 
      "Июнь", 
      "Июль", 
      "Август", 
      "Сентябрь", 
      "Октябрь", 
      "Ноярь", 
      "Декабрь" 
    };
    return rc;
  }

  /**
   * Список месяцев в родительном падеже
   */
  public static String[] getMonthListParentCase()
  {
    String[] rc =
    { 
      "Января", 
      "Февраля", 
      "Марта", 
      "Апреля", 
      "Мая", 
      "Июня", 
      "Июля", 
      "Августа", 
      "Сентября", 
      "Октября", 
      "Нояря", 
      "Декабря" 
    };
    return rc;
  }

  /**
   * Список дней недели в именительном падеже
   */
  public static String[] getDayList()
  {
    String[] rc =
    { "Воскресение", 
      "Понедельник", 
      "Вторник", 
      "Среда", 
      "Четверг", 
      "Пятница", 
      "Суббота" 
    };
    
    return rc;
  }
  
  /**
   * Список дней недели в родительном падеже
   */
  public static String[] getDayListParentCase()
  {
    String[] rc =
    { "Воскресения", 
      "Понедельника", 
      "Вторника", 
      "Среды", 
      "Четверга", 
      "Пятницы", 
      "Субботы" 
    };
    return rc;
  }
  
  /**
   * Количество дней в каждом месяце текущего года
   */
  public static int[] getDaysInMonth(int year) 
  {
    int[] rc = 
    {
      31,
      28,
      31,
      30,
      31,
      30,
      31,
      31,
      30,
      31,
      30,
      31
    };
    GregorianCalendar cal = new GregorianCalendar();
    if (cal.isLeapYear(year)) 
    {
      rc[1] = 29;
    }
    return rc;
  }

  /**
   * Название текущего месяца
   */
  public static String getStrMonth()
  {
    GregorianCalendar cal = new GregorianCalendar();
    int n = cal.get(Calendar.MONTH);
    String rc = getMonthList()[n];
    return rc;
  }

  /**
   * Название текущего дня недели
   */
  public static String getStrDayOfWeek()
  {
    GregorianCalendar cal = new GregorianCalendar();
    int n = cal.get(Calendar.DAY_OF_WEEK);
    String rc = getDayList()[n - 1];
    return rc;
  }

  /**
   * Возвращает текущую дату с разделителем, заданым параметром.
   *
   * @param sepDate - разделитель для даты
   * @return String - текущая дата в формате : DDRMMRYYYY где : DD - день MM -
   * месяц YYYY - год R - разделитель
   */
  public static String getDate(String sepDate)
  {
    GregorianCalendar cal = new GregorianCalendar();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    return StringFunc.strZero(day, 2) + sepDate + StringFunc.strZero(month + 1, 2) + sepDate + StringFunc.strZero(year, 4);
  }

  /**
   * Возвращает текущую дату с разделителем /
   *
   * @return String - текущая дата в формате : DD/MM/YYYY где : DD - день MM -
   * месяц YYYY - год
   */
  public static String getDate()
  {
    return getDate("/");
  }

  /**
   * Возвращает текущую дату в формате : YYYY-MM-DD
   *
   * @return String - текущая дата в формате : YYYY-MM-DD где : DD - день MM -
   * месяц YYYY - год
   */
  public static String getAmericanDate()
  {
    GregorianCalendar cal = new GregorianCalendar();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    return StringFunc.strZero(year, 4) + "-" + StringFunc.strZero(month + 1, 2) + "-" + StringFunc.strZero(day, 2);
  } // public static String getAmericanDate()
  
  /**
   * Возвращает нужную дату в формате : YYYY-MM-DD
   */
  public static String getAmericanDate(Date date)
  {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    int year = cal.get(GregorianCalendar.YEAR);
    int month = cal.get(GregorianCalendar.MONTH);
    int day = cal.get(GregorianCalendar.DAY_OF_MONTH);
    return StringFunc.strZero(year, 4) + "-" + StringFunc.strZero(month + 1, 2) + "-" + StringFunc.strZero(day, 2);
  } // public static String getAmericanDate()

  /**
   * Возвращает текущее время в формате : HH:MM:SS
   *
   * @return String - текущая дата в формате : HH:MM:SS где : HH - часы MM -
   * минуты SS - секунды
   */
  public static String getTime()
  {
    GregorianCalendar cal = new GregorianCalendar();
    int hour = cal.get(Calendar.HOUR_OF_DAY)+1;
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);
    return StringFunc.strZero(hour, 2) + ":" + StringFunc.strZero(min, 2) + 
      ":" + StringFunc.strZero(sec, 2);
  } // public static String getTime()
  
  /**
   * Возвращает текущее время в виде массива {HH, MM, SS}
   */
  public static int[] getTimeArray()
  {
    String time = getTime();
    String[] tok = time.split(":");
    int[] rc = {Integer.parseInt(tok[0]),Integer.parseInt(tok[1]),Integer.parseInt(tok[2])};
    return rc;
  } // public static String getTime()
  
  /**
   * Возвращает текущую дату и время в формате : DD/MM/YYYY hh:mm:ss
   * 
   * @return текущая дата и время в формате : DD/MM/YYYY hh:mm:ss где : DD -
   *         день MM - месяц YYYY - год hh - часы mm - минуты ss - секунды
   */
  public static String getDateTime()
  {
    return getDate() + " " + getTime();
  }

  /**
   * Возвращает текущую дату и время в формате : DDRMMRYYYY hh:mm:ss (R-разделитель)
   * 
   * @param sepDate - разделитель для даты
   * @return текущая дата и время в формате : DDRMMRYYYY hh:mm:ss где : R -
   *         разделитель, заданный параметром DD - день MM - месяц YYYY - год hh -
   *         часы mm - минуты ss - секунды
   */
  public static String getDateTime(String sepDate)
  {
    return getDate(sepDate) + " " + getTime();
  }

  /**
   * Возвращает текущую дату и время
   * 
   * @return текущая дата и время в формате : YYYY-MM-DD hh:mm:ss где : DD -
   *         день MM - месяц YYYY - год hh - часы mm - минуты ss - секунды
   */
  public static String getAmericanDateTime()
  {
    return getAmericanDate() + " " + getTime();
  }

  // Chronometer :
  private static boolean timeProcessActive = true;

  private static Stack<Long> timeStack = new Stack<Long>();

  //@SuppressWarnings("unchecked")
  /**
   * Начало хронометража
   */
  public static void timeStart()
  {
    if (!timeProcessActive)
      return;
    long start = System.currentTimeMillis();
    timeStack.push(new Long(start));
  }

   /**
    * Конец хронометража
    */
  public static long timeEnd()
  {
    return timeEnd(null);
  }

  /**
   * Конец хронометража
   * @param prefix - префикс для печати
   */
  public static long timeEnd(String prefix)
  {
    final String CHRONOMETRAJ_ID = "@ch > ";
    if (!timeProcessActive)
      return 0;
    Long oStart = (timeStack.pop());
    long start = oStart.longValue();
    long end = System.currentTimeMillis();
    long rc = end - start;
    if (prefix != null)
    {
      if (prefix.length() == 0)
      {
        prefix = "Time process";
      }
      System.out.println(CHRONOMETRAJ_ID+prefix + " = " + rc + " Millisec");
    }
    return rc;
  } // public static long timeEnd(String prefix)

  /*
   * Function getNormalDateTime(String sDateTime,String pattern) return String
   * By pattern Sample 1 sDateTime - "Tue Feb 27 02:08:41 PST 2001" pattern -
   * "dd/MM/yyyy" return - "27/02/2001" Sample 2 sDateTime - "Tue Feb 27
   * 02:08:41 PST 2001" pattern - "dd/MM/yyyy HH:MM:SS" return - "27/02/2001
   * 02:08:41" Sample 3 sDateTime - "Tue Feb 27 02:08:41 PST 2001" pattern -
   * "HH:MM:SS" return - "02:08:41"
   */

  /**
   * Переформатирование даты по шаблону
   */
  public static String getNormalDateTime(String sDateTime, String pattern)
  {
    Date date = null;
    try
    {
      date = DateFormat.getDateInstance().parse(sDateTime);
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
    } // catch
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    return format.format(date);
  }

  /**
   * Преоразовать дату, заданную в формате YYYY-MM-DD [hh:mm:ss], в объект класса java.util.Date
   */
  public static Date getJavaUtilDate(String sDate)
  {
    String tok2[] = sDate.split(" ");
    String[] tokDate = tok2[0].split("-");
    String[] tokTime = {"00","00","00"};
    try {
      tokTime = tok2[1].split(":");
    }
    catch(Exception ex) 
    {
      ;
    }
    int year = Integer.parseInt(tokDate[0]);
    int month = Integer.parseInt(tokDate[1]) - 1;
    int day = Integer.parseInt(tokDate[2]);
    int hour = Integer.parseInt(tokTime[0]);
    int min = Integer.parseInt(tokTime[1]);
    if (tokTime[2] != null || tokTime[2].contains(".")) 
    {
      tokTime[2] = tokTime[2].substring(0,tokTime[2].indexOf("."));
    }
    int sec = Integer.parseInt(tokTime[2]);
    Calendar cal = Calendar.getInstance();
    cal.set(year, month, day, hour, min, sec);
    Date rc = cal.getTime();
    return rc;
  }


  /**
   * Преобразует заданную дату в формат: 12 декабря 2007
   */
  public static String getDateByDayCmonthYear(java.sql.Date date)
  {
    // Возвращает дату в формате: 12 декабря 2007
    return getDateByDayCmonthYear(date," ");
  }

  /**
   * Преобразует заданную дату в формат: 12{разделитель}декабря{разделитель}2007
   * @param date  - заданная дата
   * @param split - заданный разделитель
   */
  public static String getDateByDayCmonthYear(java.sql.Date date, String split)
  {
    String strDate = date.toString(); // 2007-12-25
    String[] tok = Token.aToken(strDate, "-/\\");
    int month = Integer.parseInt(tok[1]);
    String cMonth = getMonthListParentCase()[month - 1];
    StringBuffer rc = new StringBuffer();
    rc.append(tok[2]).append(split).append(cMonth).append(split).append(tok[0]);
    return rc.toString();
  }
  
  /**
   * Преобразует заданную дату в формат: DD.MM.YYYY
   * @param date  - заданная дата
   */
  public static String getDateByDayCmonthYear(Date date)
  {
    StringBuffer rc = new StringBuffer();
    GregorianCalendar cal = new GregorianCalendar();
    //cal.setGregorianChange(date);
    cal.setTime(date);
    int day = cal.get(GregorianCalendar.DAY_OF_MONTH);
    int month = cal.get(GregorianCalendar.MONTH);
    int year = cal.get(GregorianCalendar.YEAR);
    rc.append(day).append(".").append(month+1).append(".").append(year);
    return rc.toString();
  }

//  public static String getDateByDayCmonthYear1(java.sql.Date date, String split)
//  {
//    String strDate = date.toString(); // 2007-12-25
//    String[] tok = Token.aToken(strDate, "-/\\");
//    int month = Integer.parseInt(tok[1]);
//    String cMonth = getMonthListParentCase()[month - 1];
//    StringBuffer rc = new StringBuffer();
//    StringBuffer dayOfWeek = new StringBuffer(getStrDayOfWeek());
//    dayOfWeek.append(", ").append(rc.append(tok[2]).append(' ').append(cMonth).append(split));
//    return rc.toString();
//  }

  /**
   * Преобразование даты в формате YYYY-MM-DD [...] в формат DD.MM.YYYY
   */
  public static String convertDateToDateInCalendar(String value)
  {
    if (StringFunc.isEmpty(value)) 
    {
      return value;
    }
    value = value.split(" ")[0];
    if (value.contains("-")) 
    {
      String tok[] = value.split("-");
      StringBuffer rc = new StringBuffer(
                               tok[2]).append('.').
                        append(tok[1]).append('.').
                        append(tok[0]);
      value = rc.toString();
   }
    return value;
  }

  /**
   * Преобразование java.util.Date в java.sql.Date
   */
  public static java.sql.Date java_util_date2java_sql_date(java.util.Date date)
  {
    if (date == null) 
    {
      return null;
    }
    java.sql.Date rc = new java.sql.Date(date.getTime());
    return rc;
  }

  /**
   * Преобразование даты формата YYYY-MM-DD в java.util.Date
   */
  public static Date dateFromDb2Java_util_date(String date)
  {
    if (StringFunc.isEmpty(date)) 
    {
      return null;
    }
    date = date.split(" ")[0];
    String[] tok = date.split("-");
    String year = tok[0];
    String month = tok[1];
    String day = tok[2];
    //Date rc = new Date();
    Calendar cal = Calendar.getInstance(Locale.getDefault());
    cal.set(Integer.parseInt(year),
            Integer.parseInt(month)-1,
            Integer.parseInt(day));
    Date rc = cal.getTime();
    return rc;
  }


  /**
   * Преобразует дату в формат - TO_DATE('...','YYYY-MM-DD')
   * @param date - дата для фильтрации
   * @return String - TO_DATE('...','YYYY-MM-DD')
   */
  public static String date2StringForOracleWhere(Date date) 
  {
    String cDate = getAmericanDate(date);
    String cFormat = "YYYY-MM-DD";
    StringBuffer rc = new StringBuffer("TO_DATE('").
                      append(cDate).append("','").
                      append(cFormat).append("')");
    return rc.toString();
  }
  /**
   * Получить дату по: году, месяцу, дню
   * @param y - год
   * @param m - месяц
   * @param d - день
   * @return Дата
   */
  public static java.util.Date getJavaUtilDate(int y, int m, int d)
  {
    GregorianCalendar cal = new GregorianCalendar(y, m-1, d);
    java.util.Date rc = cal.getTime();
    return rc;
  }
  
  public static int[] getYearMonthDateFromJavaUtilDate(Date date) 
  {
    int[] rc = {0,0,0};
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    rc[0] = cal.get(GregorianCalendar.YEAR);
    rc[1] = cal.get(GregorianCalendar.MONTH)+1;
    rc[2] = cal.get(GregorianCalendar.DAY_OF_MONTH);
    return rc;
  }
  
  /**
   * Получить дату в формате YY.MM.DD
   */
  public static String javaUtilDate2RusianDate(java.util.Date date) 
  {
    int[] yearMonthDate = getYearMonthDateFromJavaUtilDate(date);
    StringBuffer rc = new StringBuffer();
    rc.append(StringFunc.strZero(yearMonthDate[2],2)).
       append(".").
       append(StringFunc.strZero(yearMonthDate[1],2)).
       append(".").
       append(yearMonthDate[0]);
    return rc.toString();
  }

  /**
   * Текущий год
   */
  public static int getCurrentYear()
  {
    int[] rc = getYearMonthDateFromJavaUtilDate(new Date());
    return rc[0];
  }

  /**
   * Текущий месяц года
   */
  public static int getCurrentMonth()
  {
    int[] rc = getYearMonthDateFromJavaUtilDate(new Date());
    return rc[1];
  }
  
  /**
   * Текущий день месяца
   */
  public static int getCurrentDay()
  {
    int[] rc = getYearMonthDateFromJavaUtilDate(new Date());
    return rc[2];
  }

  /**
   * Дата последнего дня месяца
   */

  public static Date getLastDayOfMonth(Date date)
  {
    if (date == null) 
    {
      return null;
    }
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    int[] d = getYearMonthDateFromJavaUtilDate(date);
    int year  = d[0];
    int month = d[1];
    int[] days = getDaysInMonth(year);
    int lastDay = days[month-1];
    Date rc = getJavaUtilDate(year,month,lastDay);
    return rc;
  }

  /**
   * Преобразование даты в формат DD.MM.YYYY
   */
  public static String toDateInCalendar(Date date)
  {
    int[] ymd = getYearMonthDateFromJavaUtilDate(date);
    StringBuffer rc = new StringBuffer();
    rc.append(StringFunc.strZero(ymd[2],2)).append(".").
       append(StringFunc.strZero(ymd[1],2)).append(".").
       append(ymd[0]);
    return rc.toString();
  }
  public static Date domainDate2javaUtilDate(oracle.jbo.domain.Date domainDate) 
  {
    if (domainDate == null) 
    {
      return null;
    }
    return domainDate.getValue();
  }
  public static oracle.jbo.domain.Date javaUtilDate2DomainDate(java.util.Date date) 
  {
    if (date == null) 
    {
      return null;
    }
    return new oracle.jbo.domain.Date(new java.sql.Date(date.getTime()));
  }

  public static Locale getRussianLocale()
  {
    return russianLocale;
  }

  public static TimeZone getRussianTimeZone()
  {
    return russianTimeZone;
  }
  
//  public static Date addMonthsToDate(Date date,int months) 
//  {
//    Calendar cal = Calendar.getInstance();
//    cal.setTime(date);
//    if (months > 0) 
//    {
//      for (int i=1; i <= months; i++) 
//      {
//        addOneMonthToDate(cal);
//      }
//    }
//    else if (months < 0) 
//    {
//      for (int i=1; i <= Math.abs(months); i++) 
//      {
//        minusOneMonthFromDate(cal);
//      }
//    }
//    else 
//    {
//      ;
//    }
//    return cal.getTime();
//  }

//  private static void addOneMonthToDate(Calendar cal)
//  {
//    int month = cal.get(Calendar.MONTH);
//    int year = cal.get(Calendar.YEAR);
//    month++;
//    if (month > 11) 
//    {
//      month = 0;
//      year++;
//    }
//    cal.set(Calendar.YEAR, year);
//    cal.set(Calendar.MONTH, month);
//  }
//
//  private static void minusOneMonthFromDate(Calendar cal)
//  {
//    int month = cal.get(Calendar.MONTH);
//    int year = cal.get(Calendar.YEAR);
//    month--;
//    if (month < 0) 
//    {
//      month = 11;
//      year--;
//    }
//    cal.set(Calendar.YEAR, year);
//    cal.set(Calendar.MONTH, month);
//  }
  //========================================
  public static void main(String[] args) throws Exception
  {
//    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//    Date date = format.parse("06.10.2000");
//    
////    Calendar c = Calendar.getInstance();
////    c.setTime(date);
////    System.out.println("@"+c.get(Calendar.MONTH));
//    
//    Date date2 = addMonthsToDate(date, +1);
//    System.out.println(format.format(date2));
  } // main
} // public final class DateTime
