package com.rcore.global.log;

import java.util.List;
// ШМЯ

public interface FacesLog
{
  void log(String mess, Throwable ex);
  void log(Throwable ex, String mess);
  void log(String mess);
  void log(Throwable ex);
  
  void log(LogLevel level,String mess, Throwable ex);
  void log(LogLevel level,Throwable ex, String mess);
  void log(LogLevel level,String mess);
  void log(LogLevel level,Throwable ex);
  
  boolean isLevelEnabled(LogLevel level);
  void setEnabledList(List<LogLevel> list);
  
  void info(String mess);
  void error(String mess);
  void debug(String mess);
  
}
