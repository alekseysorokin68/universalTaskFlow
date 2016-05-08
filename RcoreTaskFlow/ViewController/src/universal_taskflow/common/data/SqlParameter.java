package universal_taskflow.common.data;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.util.Map;
import java.util.Set;

import universal_taskflow.common.types.ParameterAttributeType;
import universal_taskflow.common.utils.CommonUtilsImpl;

import universal_taskflow.edit_defaults.beans.EditDefaultsBean;


/**
 * Класс описывающий параметр запроса.
 */
@XStreamAlias("SqlParameter")
public class SqlParameter implements Serializable,Cloneable
{
  private static final long serialVersionUID = 1L;
  //----
  private String name = null;
  private ParameterAttributeType type = ParameterAttributeType.CHAR;
  private Serializable defaultValue = null;
  private Boolean filterAble = false; // Участвует в фильтре
  //private VisualControlForSqlParameterType visualControlType = VisualControlForSqlParameterType.INPUT;
  private VisualControlForSqlParameterInfo visualControlInfo = new VisualControlForSqlParameterInfo();
  private String caption = null;   // Метка на форме
  private String shortDesc = null; // Всплывающая строка на форме
  private Set<String> linkToOtherPortlets = null; // Связь с одноименными параметрами в других портлетах
  //-----------------------
  // private TimeInfo time = null; // Имеет значение только для type = ParameterAttributeType.Timestamp
  //-----------------------
  //================================================
  public SqlParameter()
  {
    super();
  }
  public SqlParameter(String name)
  {
    super();
    this.name = name;
  }
  //------------------------
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
  }
  //------------------------

  public String getName()
  {
    return name;
  }
  public SqlParameter clone() throws CloneNotSupportedException
  {
    SqlParameter target = new SqlParameter();
    clone(target);
    return target;
  }

  public void clone(SqlParameter target)
  {
    target.setName(this.name);
    target.setCaption(this.caption);
    target.setType(this.type);
    target.setDefaultValue(this.defaultValue);
    target.setFilterAble(this.filterAble);
    target.setVisualControlInfo(this.getVisualControlInfo());
    target.setShortDesc(this.shortDesc);
    target.setLinkToOtherPortlets(this.getLinkToOtherPortlets());
    target.setVisualControlInfo(this.getVisualControlInfo());
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setCaption(String caption)
  {
    this.caption = caption;
  }

  public String getCaption()
  {
    return caption;
  }

  public void setType(ParameterAttributeType type)
  {
    boolean changed = (this.type != type);
    this.type = type;
    if (changed) 
    {
      defaultValue = null;
      ElHtmlKey key = new ElHtmlKey(this.getClass().getName(),"defaultValue",name);
      EditDefaultsBean.getInstance().getMainRecord().getElHtmlValues().remove(key);
    }
  }

  public ParameterAttributeType getType()
  {
    return type;
  }

  public void setDefaultValue(Serializable defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public Serializable getDefaultValue()
  {
    return defaultValue;
  }
  
  public Serializable getResolvedDefaultValue()
  {
    return CommonUtilsImpl.getInstance().getResolvedValue(this, "defaultValue", getName());
  }

  public void setFilterAble(Boolean filterAble)
  {
    this.filterAble = filterAble;
  }

  public Boolean getFilterAble()
  {
    return filterAble;
  }

  public void setShortDesc(String shortDesc)
  {
    this.shortDesc = shortDesc;
  }

  public String getShortDesc()
  {
    return shortDesc;
  }

  public void setLinkToOtherPortlets(Set<String> linkToOtherPortlets)
  {
    this.linkToOtherPortlets = linkToOtherPortlets;
  }

  public Set<String> getLinkToOtherPortlets()
  {
    return linkToOtherPortlets;
  }

  public void setVisualControlInfo(VisualControlForSqlParameterInfo visualControlInfo)
  {
    this.visualControlInfo = visualControlInfo;
  }

  public VisualControlForSqlParameterInfo getVisualControlInfo()
  {
    return visualControlInfo;
  }

  public String getFacetNameForSqlParameterDefaultValue()
  {
    String rc = "INPUT";
    if (type != null) 
    {
      if      (type.equals(ParameterAttributeType.DATE)) 
      {
        rc = "DATE";
      }
      else if (type.equals(ParameterAttributeType.TIME)) 
      {
        rc = "TIME";
      }
      else if (type.equals(ParameterAttributeType.TIMESTAMP)) 
      {
        rc = "TIMESTAMP";
      }
    }
    return rc;    
  }

  public Serializable getCurrentValue(UserRecord ur)
  {
    final Serializable value  = getResolvedDefaultValue();
    Serializable currentValue = ur.getSqlParameterCurrentValue(getName());
    if (currentValue == null)
    {
      currentValue = value;
    }
    // TODO ?
//    else
//    {
//      if (currentValue instanceof java.util.Date)
//      {
//        currentValue = adoptDateValue((java.util.Date) currentValue);
//      }
//    }
    return currentValue;
  }
  
//  public Serializable adoptDateValue(java.util.Date date)
//  {
//    Serializable rc = null;
//    if (!(ParameterAttributeType.TIMESTAMP.equals(type))) 
//    {
//      rc = DateTime.javaUtilDate2DomainDate(date);
//    }
//    else 
//    {
//      // TIMESTAMP!
//      Calendar calendar = Calendar.getInstance();
//      calendar.setTimeInMillis(date.getTime());
//      TimeInfo info = getTime();
//      calendar.set(Calendar.HOUR_OF_DAY,info.getHour());
//      calendar.set(Calendar.MINUTE,info.getMinute());
//      calendar.set(Calendar.SECOND,info.getSecond());
//      long newTime = calendar.getTimeInMillis();
//      rc = new oracle.jbo.domain.Timestamp(newTime);
//    }
//    return rc;
//  }
}

