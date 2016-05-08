package test.universal_taskflow.common.utils;

import java.io.Serializable;


public abstract class TestEnumBase implements Serializable
{
  private static final long serialVersionUID = 1L;
  public abstract Enum getDelegateEnum();
  public String toString()
  {
    return name();
  }
  public final boolean equals(Object other)
  {
    boolean rc = false;
    if (other != null && (other instanceof TestEnumBase))
    {
      TestEnumBase otherBase = (TestEnumBase) other;
      rc = (this.getClass().equals(other.getClass())) &&
           getDelegateEnum().equals(otherBase.getDelegateEnum());
        ;
    }
    return rc;
  }
  public final int hashCode()
  {
    StringBuilder buf = new StringBuilder(this.getClass().getName());
    buf.append(ordinal());
    return buf.toString().hashCode();
  }
  public String getName()
  {
    return name();
  }

  public String name()
  {
    return getDelegateEnum().name();
  }

  public int getOrdinal()
  {
    return ordinal();
  }
  public int ordinal()
  {
    return getDelegateEnum().ordinal();
  }
//  public <T extends TestEnumBase> T valueOf(
//                Class<T> enumType, String name) 
//  {
//    // TODO
//    return getDelegateEnum().valueOf(enumType, name);
//  }
//  private String name = null;
//  private int ordinal = -1;
//
//  public TestEnumBase(String name,int ordinal)
//  {
//    super();
//    this.name = name;
//    this.ordinal = ordinal;
//  }
//
//  public String toString()
//  {
//    return name;
//  }
//
//  public int compareTo(TestEnumBase o)
//  {
//    TestEnumBase other = o;
//    TestEnumBase self = this;
//    if (self.getClass() != other.getClass()
//          && self.getDeclaringClass() != other.getDeclaringClass()
//       )
//       throw new ClassCastException();
//    return self.ordinal - other.ordinal;
//  }
//
//  public final Class getDeclaringClass()
//  {
//    Class clazz = getClass();
//    Class zuper = clazz.getSuperclass();
//    return (zuper == TestEnumBase.class) ? clazz : zuper;
//  }
//
//  public final boolean equals(Object other)
//  {
//    boolean rc = false;
//    if (other != null && (other instanceof TestEnumBase))
//    {
//      TestEnumBase otherBase = (TestEnumBase) other;
//      rc = (this.getClass().equals(other.getClass())) &&
//           (this.ordinal == otherBase.ordinal) &&
//           (this.name == otherBase.name)
//        ;
//    }
//    return rc;
//  }
//  protected final Object clone() throws CloneNotSupportedException 
//  {
//    throw new CloneNotSupportedException();
//  }
//  
////  public static <T extends TestEnumBase> T valueOf(
////                Class<T> enumType, String name) 
////  {
////    T rc = null;
////    Field field = null;
////    try
////    {
////      field = enumType.getDeclaredField(name);
////      field.setAccessible(true);
////      field.get(obj)
////    }
////    catch (Exception e)
////    {
////      e.printStackTrace();
////    }
////    return rc;
////  }
//
//
//  /**
//   * Returns a hash code for this enum constant.
//   *
//   * @return a hash code for this enum constant.
//   */
//  public final int hashCode()
//  {
//    StringBuilder buf = new StringBuilder(this.getClass().getName());
//    buf.append(name).append(ordinal);
//    return buf.toString().hashCode();
//  }
//
//  public String getName()
//  {
//    return name;
//  }
//
//  public String name()
//  {
//    return name;
//  }
//
//  public int getOrdinal()
//  {
//    return ordinal;
//  }
//  public int ordinal()
//  {
//    return ordinal;
//  }
}
