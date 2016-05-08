package universal_taskflow.common.data;


import com.rcore.global.MapWithOrderKeys;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.trinidad.model.DateListProvider;

import universal_taskflow.common.types.ValidatorType;


@XStreamAlias("ValidatorInfo")
public class ValidatorInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  private ValidatorType type = ValidatorType.validateEmpty;
  private MapWithOrderKeys<String,Serializable> mapAttributeValues = new MapWithOrderKeys<String,Serializable>();
  private transient List<InvalidDayRow> invalidDaysTableModel = null;
  private transient List<InvalidDaysOfWeekRow> invalidDaysOfWeekTableModel = null;
  private transient List<InvalidMonthsRow> invalidMonthsTableModel = null;
  private transient Date invalidDayToAdd = null;
  public ValidatorInfo()
  {
    super();
  }
  
  //===================================
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
    if (type == null) 
    {
      type = ValidatorType.validateEmpty;
    }
  }
  //===================================

  public void setType(ValidatorType type)
  {
    if (this.type != type) 
    {
      if (type != null) 
      {
        type.initMapDefaults(getMapAttributeValues());
      }
      invalidDaysTableModel = null;
      invalidDaysOfWeekTableModel = null;
      invalidMonthsTableModel = null;
    }
    this.type = type;
  }

  public ValidatorType getType()
  {
    if (type == null) 
    {
      type = ValidatorType.validateEmpty;
    }
    return type;
  }

  public void setMapAttributeValues(MapWithOrderKeys<String, Serializable> mapAttributeValues)
  {
    this.mapAttributeValues = mapAttributeValues;
  }

  public MapWithOrderKeys<String, Serializable> getMapAttributeValues()
  {
    if (mapAttributeValues == null) 
    {
      mapAttributeValues = new MapWithOrderKeys<String,Serializable>();
    }
    return mapAttributeValues;
  }

  public List<InvalidDayRow> getInvalidDaysTableModel()
  {
    if (invalidDaysTableModel == null) 
    {
      invalidDaysTableModel = new ArrayList<InvalidDayRow>();
      List<Date> listOfDate = (List<Date>)(mapAttributeValues.get("invalidDays"));
      if (listOfDate != null) 
      {
        for (Date item : listOfDate) 
        {
          invalidDaysTableModel.add(new InvalidDayRow(item));
        }
      }
    }
    return invalidDaysTableModel;
  }
  
  public List<InvalidDaysOfWeekRow> getInvalidDaysOfWeekTableModel() 
  {
    if (invalidDaysOfWeekTableModel == null)
    {
      Object value = mapAttributeValues.get("invalidDaysOfWeek");
      String[] invalidDaysOfWeek = (String[])value;
      if (invalidDaysOfWeek == null) 
      {
        invalidDaysOfWeek = new String[0];
      }
      List<String> invalidDaysOfWeekList = Arrays.asList(invalidDaysOfWeek);
      invalidDaysOfWeekTableModel = new ArrayList<InvalidDaysOfWeekRow>();
      invalidDaysOfWeekTableModel.add(new InvalidDaysOfWeekRow("sun","воскресенье",invalidDaysOfWeekList.contains("sun")));
      invalidDaysOfWeekTableModel.add(new InvalidDaysOfWeekRow("mon","понедельник",invalidDaysOfWeekList.contains("mon")));
      invalidDaysOfWeekTableModel.add(new InvalidDaysOfWeekRow("tue","вторник",invalidDaysOfWeekList.contains("tue")));
      invalidDaysOfWeekTableModel.add(new InvalidDaysOfWeekRow("wed","среда",invalidDaysOfWeekList.contains("wed")));
      invalidDaysOfWeekTableModel.add(new InvalidDaysOfWeekRow("thu","четверг",invalidDaysOfWeekList.contains("thu")));
      invalidDaysOfWeekTableModel.add(new InvalidDaysOfWeekRow("fri","пятница",invalidDaysOfWeekList.contains("fri")));
      invalidDaysOfWeekTableModel.add(new InvalidDaysOfWeekRow("sat","суббота",invalidDaysOfWeekList.contains("sat")));
    }
    return invalidDaysOfWeekTableModel;
  }
  
  public List<InvalidMonthsRow> getInvalidMonthsTableModel()
  {
    if (invalidMonthsTableModel == null)
    {
      Object value = mapAttributeValues.get("invalidMonths");
      String[] invalidMonths = (String[])value;
      if (invalidMonths == null) 
      {
        invalidMonths = new String[0];
      }
      List<String> invalidMonthsList = Arrays.asList(invalidMonths);
      invalidMonthsTableModel = new ArrayList<InvalidMonthsRow>();
      invalidMonthsTableModel.add(new InvalidMonthsRow("jan","Январь",invalidMonthsList.contains("jan")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("feb","Февраль",invalidMonthsList.contains("feb")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("mar","Март",invalidMonthsList.contains("mar")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("apr","Апрель",invalidMonthsList.contains("apr")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("may","Май",invalidMonthsList.contains("may")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("jun","Июнь",invalidMonthsList.contains("jun")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("jul","Июль",invalidMonthsList.contains("jul")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("aug","Август",invalidMonthsList.contains("aug")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("sep","Сентябрь",invalidMonthsList.contains("sep")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("oct","Октябрь",invalidMonthsList.contains("oct")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("nov","Ноябрь",invalidMonthsList.contains("nov")));
      invalidMonthsTableModel.add(new InvalidMonthsRow("dec","Декабрь",invalidMonthsList.contains("dec")));
    }
    return invalidMonthsTableModel;
  }
  
  private void invalidDaysOfWeekTableModel2invalidDaysOfWeek()
  {
    List<String> invalidDaysOfWeekList = new ArrayList<String>();
    for (InvalidDaysOfWeekRow item : invalidDaysOfWeekTableModel) 
    {
      if (item.isSelected()) 
      {
        invalidDaysOfWeekList.add(item.getDay());
      }
    }
    //---
    String[] rc = invalidDaysOfWeekList.toArray(new String[invalidDaysOfWeekList.size()]);
    mapAttributeValues.put("invalidDaysOfWeek",rc);
  }
  
  private void invalidMonthsTableModel2invalidMonths() 
  {
    List<String> invalidMonthsList = new ArrayList<String>();
    for (InvalidMonthsRow item : invalidMonthsTableModel) 
    {
      if (item.isSelected()) 
      {
        invalidMonthsList.add(item.getMonth());
      }
    }
    //---
    String[] rc = invalidMonthsList.toArray(new String[invalidMonthsList.size()]);
    mapAttributeValues.put("invalidMonths",rc);
  }
    
  private void invalidDaysTableModel2InvalidDays()
  {
    List<Date> rc = new ArrayList<Date>();
    for (InvalidDayRow item : invalidDaysTableModel) 
    {
      Date date = item.getDate();
      if (date != null) 
      {
        if (!rc.contains(date)) 
        {
          rc.add(date);
        }
      }
    }
    mapAttributeValues.put("invalidDays", (Serializable) rc);
    return;
  }

  public void setInvalidDayToAdd(Date invalidDayToAdd)
  {
    this.invalidDayToAdd = invalidDayToAdd;
  }

  public Date getInvalidDayToAdd()
  {
    return invalidDayToAdd;
  }
  public String addInvalidDayAction() 
  {
    if (invalidDayToAdd != null) 
    {
      InvalidDayRow newObj = new InvalidDayRow(invalidDayToAdd);
      if (!invalidDaysTableModel.contains(newObj))
      {
        invalidDaysTableModel.add(newObj);
        invalidDaysTableModel2InvalidDays();
      }
    }
    return null;
  }
  public void invalidDayDelete(ActionEvent event) 
  {
    InvalidDayRow row = (ValidatorInfo.InvalidDayRow) event.getComponent().getAttributes().get("row");
    invalidDaysTableModel.remove(row);
    invalidDaysTableModel2InvalidDays();
  }
  public DateListProvider getInvalidDaysDateListProvider() 
  {
    DateListProvider rc = new DateListProvider() 
    {

      public List<Date> getDateList(FacesContext facesContext, Calendar calendar, Date date, Date date1)
      {
        return (List<Date>) mapAttributeValues.get("invalidDays");
      }
    };
    return rc;
  }
  //============================
  //
  //============================
  public class InvalidDayRow
  {
    private Date date = null;

    private InvalidDayRow(Date date) 
    {
      super();
      this.date = date;
    }

    public void setDate(Date date)
    {
      this.date = date;
      // 
      invalidDaysTableModel2InvalidDays();
    }

    public Date getDate()
    {
      return date;
    }
    @Override
    public boolean equals(Object obj) 
    {
      boolean rc = false;
      if (obj != null && obj instanceof InvalidDayRow) 
      {
        InvalidDayRow objRow = (ValidatorInfo.InvalidDayRow) obj;
        if (date != null) 
        {
          rc = (date.equals(objRow.date));  
        }
        else 
        {
          rc = (objRow.date == null);
        }
      }
      return rc;
    }
    @Override
    public int hashCode() 
    {
      int rc = 0;
      if (date != null) 
      {
        rc = date.hashCode();
      }
      return rc;
    }
  }

  public class InvalidDaysOfWeekRow
  {
    private String day = null;
    private String label = null;
    private boolean selected = false;
    private InvalidDaysOfWeekRow(String day,String label, boolean selected) 
    {
      super();
      this.day = day;
      this.label = label;
      this.selected = selected;
    }

    public void setSelected(boolean selected)
    {
      this.selected = selected;
      invalidDaysOfWeekTableModel2invalidDaysOfWeek();
    }

    public boolean isSelected()
    {
      return selected;
    }

    public String getLabel()
    {
      return label;
    }

    public String getDay()
    {
      return day;
    }
  }
  
  public class InvalidMonthsRow
  {
    private String month = null;
    private String label = null;
    private boolean selected = false;
    private InvalidMonthsRow(String month, String label, boolean selected) 
    {
      super();
      this.month = month;
      this.label = label;
      this.selected = selected;
    }

    public void setSelected(boolean selected)
    {
      this.selected = selected;
      invalidMonthsTableModel2invalidMonths();
    }

    public boolean isSelected()
    {
      return selected;
    }

    public String getLabel()
    {
      return label;
    }

    public String getMonth()
    {
      return month;
    }
  }
}
