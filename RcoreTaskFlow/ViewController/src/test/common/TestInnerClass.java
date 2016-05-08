package test.common;

import com.thoughtworks.xstream.XStream;

import java.io.Serializable;

import universal_taskflow.common.utils.serialization.XStreamFacade;

public class TestInnerClass implements Serializable
{
  private static final long serialVersionUID = 1L;
  private InnerClass ic = new InnerClass();

  public TestInnerClass()
  {
    super();
  }
  //---
  public static void main(String[] args)
  {
    TestInnerClass obj = new TestInnerClass();
    XStream xstream = XStreamFacade.getXStream();
    String str = xstream.toXML(obj);
    System.out.println(str);
  }
  //---
  public class InnerClass implements Serializable
  {
    private static final long serialVersionUID = 1L;
  }
}
