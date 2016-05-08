package universal_taskflow.common.data;

import com.rcore.global.MapWithOrderKeys;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

import universal_taskflow.common.types.ConvertorType;

@XStreamAlias("ConvertorInfo")
public class ConvertorInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  private ConvertorType type = ConvertorType.convertEmpty;
  private MapWithOrderKeys<String,Serializable> mapAttributeValues = new MapWithOrderKeys<String,Serializable>();
  public ConvertorInfo()
  {
    super();
  }

  public void setType(ConvertorType type)
  {
    if (this.type != type) 
    {
      if (type != null) 
      {
        type.initMapDefaults(getMapAttributeValues());
      }
    }
    this.type = type;
  }

  public ConvertorType getType()
  {
    if (type == null) 
    {
      type = ConvertorType.convertEmpty;
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
}
