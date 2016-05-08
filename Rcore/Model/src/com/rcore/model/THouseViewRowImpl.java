package com.rcore.model;

import com.rcore.global.bcomponent.base.EntityImplRcore;
import com.rcore.global.bcomponent.base.ViewRowImplRcore;

import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Dec 23 10:28:46 MSK 2011
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class THouseViewRowImpl
  extends ViewRowImplRcore
{
  /**
   * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
   */
  public enum AttributesEnum
  {
    HouseId
    {
      public Object get(THouseViewRowImpl obj)
      {
        return obj.getHouseId();
      }

      public void put(THouseViewRowImpl obj, Object value)
      {
        obj.setHouseId((Number)value);
      }
    }
    ,
    StreetId
    {
      public Object get(THouseViewRowImpl obj)
      {
        return obj.getStreetId();
      }

      public void put(THouseViewRowImpl obj, Object value)
      {
        obj.setStreetId((Number)value);
      }
    }
    ,
    Sname
    {
      public Object get(THouseViewRowImpl obj)
      {
        return obj.getSname();
      }

      public void put(THouseViewRowImpl obj, Object value)
      {
        obj.setSname((String)value);
      }
    }
    ;
    private static AttributesEnum[] vals = null;
    private static int firstIndex = 0;

    public abstract Object get(THouseViewRowImpl object);

    public abstract void put(THouseViewRowImpl object, Object value);

    public int index()
    {
      return AttributesEnum.firstIndex() + ordinal();
    }

    public static int firstIndex()
    {
      return firstIndex;
    }

    public static int count()
    {
      return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
    }

    public static AttributesEnum[] staticValues()
    {
      if (vals == null)
      {
        vals = AttributesEnum.values();
      }
      return vals;
    }
  }
  public static final int HOUSEID = AttributesEnum.HouseId.index();
  public static final int STREETID = AttributesEnum.StreetId.index();
  public static final int SNAME = AttributesEnum.Sname.index();

  /**
   * This is the default constructor (do not remove).
   */
  public THouseViewRowImpl()
  {
  }

  /**
   * Gets THouse entity object.
   * @return the THouse
   */
  public EntityImplRcore getTHouse()
  {
    return (EntityImplRcore) getEntity(0);
  }

  /**
   * Gets the attribute value for HOUSE_ID using the alias name HouseId.
   * @return the HOUSE_ID
   */
  public Number getHouseId()
  {
    return (Number) getAttributeInternal(HOUSEID);
  }

  /**
   * Sets <code>value</code> as attribute value for HOUSE_ID using the alias name HouseId.
   * @param value value to set the HOUSE_ID
   */
  public void setHouseId(Number value)
  {
    setAttributeInternal(HOUSEID, value);
  }

  /**
   * Gets the attribute value for STREET_ID using the alias name StreetId.
   * @return the STREET_ID
   */
  public Number getStreetId()
  {
    return (Number) getAttributeInternal(STREETID);
  }

  /**
   * Sets <code>value</code> as attribute value for STREET_ID using the alias name StreetId.
   * @param value value to set the STREET_ID
   */
  public void setStreetId(Number value)
  {
    setAttributeInternal(STREETID, value);
  }

  /**
   * Gets the attribute value for SNAME using the alias name Sname.
   * @return the SNAME
   */
  public String getSname()
  {
    return (String) getAttributeInternal(SNAME);
  }

  /**
   * Sets <code>value</code> as attribute value for SNAME using the alias name Sname.
   * @param value value to set the SNAME
   */
  public void setSname(String value)
  {
    setAttributeInternal(SNAME, value);
  }

  /**
   * getAttrInvokeAccessor: generated method. Do not modify.
   * @param index the index identifying the attribute
   * @param attrDef the attribute

   * @return the attribute value
   * @throws Exception
   */
  protected Object getAttrInvokeAccessor(int index,
                                         AttributeDefImpl attrDef)
    throws Exception
  {
    if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count()))
    {
      return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
    }
    return super.getAttrInvokeAccessor(index, attrDef);
  }

  /**
   * setAttrInvokeAccessor: generated method. Do not modify.
   * @param index the index identifying the attribute
   * @param value the value to assign to the attribute
   * @param attrDef the attribute

   * @throws Exception
   */
  protected void setAttrInvokeAccessor(int index, Object value,
                                       AttributeDefImpl attrDef)
    throws Exception
  {
    if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count()))
    {
      AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
      return;
    }
    super.setAttrInvokeAccessor(index, value, attrDef);
  }
}