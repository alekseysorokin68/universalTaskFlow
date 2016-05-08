package universal_taskflow.common.types;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@XStreamAlias("ValidatorType")
public enum ValidatorType
{
  // validateByteLength,       // Проверяет длину в байтах строки при кодировании
 validateDoubleRange("Валидатор по диапазону значений чисел",
                     new String[]
                     {
                      "maximum", // double
                      "minimum"  // double
                     },
                     new Serializable[]
                     {
                       null,
                       null
                     }
        ),  // Диапазон для double  
 
 validateLength("Валидатор по длине поля",
                new String[]
                {
                  "maximum", // int
                  "minimum"  // int
                },
                new Serializable[]
                {
                  null,
                  null
                }
      ),           // Проверяет длину строки в символах
// validateLongRange("maximum", // long
//                   "minimum"  // long
//                  ),          // Диапазон для long

 validateDateTimeRange("Валидатор по диапазону значений дат",
                      new String[]
                      {
                       "maximum", // java.util.Date
                       "minimum"  // java.util.Date
                      },
                      new Serializable[]
                      {
                        null,
                        null
                      }
        ),    // Диапазон для даты-время 
 
 validateDateRestriction("Валидатор исключающий некоторые даты",
                        new String[]
                        {
                          "invalidDays",       // List<Date>
                          "invalidDaysOfWeek", // String[] (sun, mon, tue, wed, thu, fri, sat)
                          "invalidMonths"      // String[] месяца, которые недопустимы 
                                               // (jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec)
                        },
                        new Serializable[]
                        {
                          new ArrayList<Date>(),
                          new String[0],
                          new String[0]
                        } 
            ),  // Ограничения на дату
 
 validateEmpty("Валидатор не используется",
               null,
               null
              )
 

  // ,validateRegExp         //  
  ;
  private static final long serialVersionUID = 1L;
  private String title = null;
  private String[] attrList = null;
  private Serializable[] defaultValues = null;

  private ValidatorType(String title, String[] attrList, Serializable[] defaultValues) 
  {
    this.title = title;
    if (attrList == null)       attrList = new String[0];
    if (defaultValues == null)  defaultValues = new Serializable[0];
    this.attrList = attrList;
    this.defaultValues = defaultValues;
  }

  public String getTitle()
  {
    return title;
  }

  public String[] getAttrList()
  {
    return attrList;
  }
  
  public String getName() 
  {
    return name();
  }
  
  public void initMapDefaults(Map<String,Serializable> map) 
  {
    map.clear();
    for (int i=0; i < attrList.length; i++) 
    {
      String attribute = attrList[i];
      Serializable value = defaultValues[i];
      map.put(attribute, value);
    } // for
  }
}
