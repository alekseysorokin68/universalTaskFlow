package universal_taskflow.common.data;

import java.io.Serializable;

public class TimeInfo implements Serializable,Cloneable
{
  @SuppressWarnings("compatibility:-4279142713467003100")
  private static final long serialVersionUID = 1L;
  
  private int hour = 0;
  private int minute = 0;
  private int second = 0;

  public void setHour(int hour)
  {
    this.hour = hour;
  }

  public int getHour()
  {
    return hour;
  }

  public void setMinute(int minute)
  {
    this.minute = minute;
  }

  public int getMinute()
  {
    return minute;
  }

  public void setSecond(int second)
  {
    this.second = second;
  }

  public int getSecond()
  {
    return second;
  }
  //------------------
  public String actionSetMinimum() 
  {
    hour = 0;
    minute = 0;
    second = 0;
    return null;
  }
  public String actionSetMaximum() 
  {
    hour = 23;
    minute = 59;
    second = 59;
    return null;
  }
}

