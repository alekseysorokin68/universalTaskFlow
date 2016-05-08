package universal_taskflow.common.types;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

import java.util.Map;

@XStreamAlias("ConvertorType")
public enum ConvertorType
{
  convertEmpty("Конвертор не используется",null,null),
  convertNumber("Конвертор для чисел,денег,процентов",
                new String[]
                {
                  "type",         // String ("number"(def), 
                                  //         "currency", // пока не реализуем
                                  //         "percent"   // пока не реализуем
                                  //         )
                  "groupingUsed", // Boolean
                  "pattern"       // String (как  в java.text.DecimalFormat)
                },
                new Serializable[] 
                {
                  "number",
                  "false",
                  ""
                }
               ),
  
  convertDateTime("Конвертор для дат",
                  new String[]
                  {
                    // "locale",  // java.util.Locale 
                    "type",       // String (date (def), "time", "both"
                    "pattern"     // String
                  },
                  new Serializable[] 
                  {
                    "date",
                    "",
                  }
          )
  
  ;


  private static final long serialVersionUID = 1L;
  private String title = null;
  private String[] attrList = null;
  private Serializable[] defaultValues = null;
  //,convertColor // на будующее

  private ConvertorType(String title, String[] attrList, Serializable[] defaultValues) 
  {
    this.title = title; 
    if (attrList == null) 
    {
      attrList = new String[0];
    }
    if (defaultValues == null) 
    {
      defaultValues = new Serializable[0];
    }
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
