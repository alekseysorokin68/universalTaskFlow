package com.rcore.global;
// ШМЯ

import java.io.File;
import java.io.IOException;

import java.text.NumberFormat;

import java.util.HashMap;
import java.util.Map;

public final class Debug
{
  private static LocalInfoStatus debugStatus = com.rcore.global.Debug.LocalInfoStatus.UNDEFINED;
  private static LocalInfoStatus localStatus = com.rcore.global.Debug.LocalInfoStatus.UNDEFINED;
  public static final String RUNTIME_LOCAL_INFO_PATH = "C:/JDeveloper/comp_id/";

  private Debug()
  {
    super();
  }

  //---------------------------------
  private static boolean timingActive = true;
  private static Map<String,Long> timingMap = new HashMap<String,Long>();
  public static void timingStart(String timingId) 
  {
    if (!timingActive) return;
    timingMap.put(timingId, System.currentTimeMillis());
  }
  public static void timingFinish(String timingId) 
  {
    timingFinish(timingId, false);
  }
  public static void timingFinish(String timingId, boolean noPrintIfZero) 
  {
    final String CHRONOMETRAJ_ID = "@timing > ";
    if (!timingActive) return;
    Long startTime = timingMap.remove(timingId);
    if (startTime == null) 
    {
      System.err.println(CHRONOMETRAJ_ID+"Нет отметки начала хронометража");
      return;
    }
    long time = System.currentTimeMillis() - startTime;
    if (time == 0L && noPrintIfZero) 
    {
      return;
    }
    String sTime = formatTime(time);
    System.out.println(CHRONOMETRAJ_ID+"Хронометраж события '"+timingId+"' - '"+sTime+"' миллисек.");
  }
  public static void setTimingActive(boolean timingActive)
  {
    Debug.timingActive = timingActive;
  }

  public static boolean isTimingActive()
  {
    return timingActive;
  }

  private static String formatTime(long time)
  {
    NumberFormat format = NumberFormat.getNumberInstance();
    String rc = format.format(time);
    return rc;
  }
  //----------------------------------

  private enum LocalInfoStatus
  {
    UNDEFINED,
    ON,
    OFF;
  }

  public static boolean isDebug()
  {
    if (debugStatus.equals(com.rcore.global.Debug.LocalInfoStatus.UNDEFINED)) {
      final String file = RUNTIME_LOCAL_INFO_PATH + "debug_on_off.txt";
      if (!Files.isFileExists(file)) {
        debugStatus = com.rcore.global.Debug.LocalInfoStatus.OFF;
      }
      else {
        String content = Files.getFile(file);
        if (content == null) {
          debugStatus = com.rcore.global.Debug.LocalInfoStatus.OFF;
        }
        if (content.toLowerCase().equals("on")) {
          debugStatus = com.rcore.global.Debug.LocalInfoStatus.ON;
        }
        else 
        {
          debugStatus = com.rcore.global.Debug.LocalInfoStatus.OFF;
        }
      }
    }
    return (debugStatus.equals(com.rcore.global.Debug.LocalInfoStatus.ON));
  }
  
  public static boolean isLocalComputer() 
  {
    if (localStatus.equals(com.rcore.global.Debug.LocalInfoStatus.UNDEFINED)) {
      final String file = RUNTIME_LOCAL_INFO_PATH + "local_id_on_off.txt";
      if (!Files.isFileExists(file)) {
        localStatus = com.rcore.global.Debug.LocalInfoStatus.OFF;
      }
      else {
        String content = Files.getFile(file);
        if (content == null) {
          localStatus = com.rcore.global.Debug.LocalInfoStatus.OFF;
        }
        if (content.toLowerCase().equals("on")) {
          localStatus = com.rcore.global.Debug.LocalInfoStatus.ON;
        }
        else 
        {
          localStatus = com.rcore.global.Debug.LocalInfoStatus.OFF;
        }
      }
    }
    return (localStatus.equals(com.rcore.global.Debug.LocalInfoStatus.ON));
  }
  
  public static void saveInfo(String fileName, String info) 
  {
    File file = new File(RUNTIME_LOCAL_INFO_PATH+"info");
    file.mkdirs();
    try {
      Files.putFile(RUNTIME_LOCAL_INFO_PATH+"info/"+fileName, info);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  //---------------------------
  public static void main(String[] args)  throws Exception
  {
    //saveInfo("test2", "test3") ;
    //System.out.println(formatTime(System.currentTimeMillis()));
    //System.out.println(formatTime(1456));
    timingStart("test1");
    Thread.currentThread().sleep(1500);
    timingFinish("test1");
  }

}
