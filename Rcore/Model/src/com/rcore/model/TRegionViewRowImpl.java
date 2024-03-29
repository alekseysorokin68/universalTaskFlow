package com.rcore.model;

import com.rcore.global.bcomponent.base.EntityImplRcore;
import com.rcore.global.bcomponent.base.ViewRowImplRcore;

import oracle.jbo.RowIterator;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Dec 23 10:28:58 MSK 2011
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class TRegionViewRowImpl
  extends ViewRowImplRcore
{
  /**
   * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
   */
  public enum AttributesEnum
  {
    RegionId
    {
      public Object get(TRegionViewRowImpl obj)
      {
        return obj.getRegionId();
      }

      public void put(TRegionViewRowImpl obj, Object value)
      {
        obj.setRegionId((Number)value);
      }
    }
    ,
    CountryId
    {
      public Object get(TRegionViewRowImpl obj)
      {
        return obj.getCountryId();
      }

      public void put(TRegionViewRowImpl obj, Object value)
      {
        obj.setCountryId((Number)value);
      }
    }
    ,
    Sname
    {
      public Object get(TRegionViewRowImpl obj)
      {
        return obj.getSname();
      }

      public void put(TRegionViewRowImpl obj, Object value)
      {
        obj.setSname((String)value);
      }
    }
    ,
    TCityView
    {
      public Object get(TRegionViewRowImpl obj)
      {
        return obj.getTCityView();
      }

      public void put(TRegionViewRowImpl obj, Object value)
      {
        obj.setAttributeInternal(index(), value);
      }
    }
    ;
    private static AttributesEnum[] vals = null;
    private static int firstIndex = 0;

    public abstract Object get(TRegionViewRowImpl object);

    public abstract void put(TRegionViewRowImpl object, Object value);

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
  public static final int REGIONID = AttributesEnum.RegionId.index();
  public static final int COUNTRYID = AttributesEnum.CountryId.index();
  public static final int SNAME = AttributesEnum.Sname.index();
  public static final int TCITYVIEW = AttributesEnum.TCityView.index();

  /**
   * This is the default constructor (do not remove).
   */
  public TRegionViewRowImpl()
  {
  }

  /**
   * Gets TRegion entity object.
   * @return the TRegion
   */
  public EntityImplRcore getTRegion()
  {
    return (EntityImplRcore) getEntity(0);
  }

  /**
   * Gets the attribute value for REGION_ID using the alias name RegionId.
   * @return the REGION_ID
   */
  public Number getRegionId()
  {
    return (Number) getAttributeInternal(REGIONID);
  }

  /**
   * Sets <code>value</code> as attribute value for REGION_ID using the alias name RegionId.
   * @param value value to set the REGION_ID
   */
  public void setRegionId(Number value)
  {
    setAttributeInternal(REGIONID, value);
  }

  /**
   * Gets the attribute value for COUNTRY_ID using the alias name CountryId.
   * @return the COUNTRY_ID
   */
  public Number getCountryId()
  {
    return (Number) getAttributeInternal(COUNTRYID);
  }

  /**
   * Sets <code>value</code> as attribute value for COUNTRY_ID using the alias name CountryId.
   * @param value value to set the COUNTRY_ID
   */
  public void setCountryId(Number value)
  {
    setAttributeInternal(COUNTRYID, value);
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
   * Gets the associated <code>RowIterator</code> using master-detail link TCityView.
   */
  public RowIterator getTCityView()
  {
    return (RowIterator) getAttributeInternal(TCITYVIEW);
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
