package test.universal_taskflow.common.utils;
  
public class TestEnumReal extends TestEnumBase //extends TestEnumBase  
{
  public static enum DELEGATE_ENUM 
  {
    A,
    B
    ;
    private static final long serialVersionUID = 1L;
  }
  private static final long serialVersionUID = 1L;
  private TestEnumReal.DELEGATE_ENUM delegateEnum;

  public TestEnumReal(DELEGATE_ENUM delEnum)
  {
    super();
    this.delegateEnum = delEnum;
  }
  @Override
  public DELEGATE_ENUM getDelegateEnum()
  {
    return delegateEnum;
  }
  //@Override
  public static TestEnumReal[] values()
  {
    DELEGATE_ENUM[] list = DELEGATE_ENUM.values();
    TestEnumReal[] rc = new TestEnumReal[list.length];
    for (int i=0; i < list.length; i++)  
      rc[i] = new TestEnumReal(list[i]);
    return rc;
  }
  
  public static TestEnumReal valuesOf(String name) 
  {
    return new TestEnumReal(DELEGATE_ENUM.valueOf(name));
  }

  //---------------------------------------
  //==================
  public static void main(String[] args)
  {
    TestEnumReal objA = new TestEnumReal(TestEnumReal.DELEGATE_ENUM.A);
    TestEnumReal objB = new TestEnumReal(TestEnumReal.DELEGATE_ENUM.B);
    boolean a = objA.getDelegateEnum().equals(TestEnumReal.DELEGATE_ENUM.A);
    boolean b = objB.getDelegateEnum().equals(TestEnumReal.DELEGATE_ENUM.A);
    System.out.println("@a="+objA.hashCode());
    System.out.println("@b="+objB.hashCode());
  }
}
