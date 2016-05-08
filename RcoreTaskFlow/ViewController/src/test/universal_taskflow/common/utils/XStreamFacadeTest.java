package test.universal_taskflow.common.utils;


import com.rcore.global.Files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.universal_taskflow.common.utils.data.ClassForSerializableAliasAnotation;
import test.universal_taskflow.common.utils.data.ClassForSerializableEmpty;
import test.universal_taskflow.common.utils.data.ClassForSerialization;
import test.universal_taskflow.common.utils.data.TestEnum;
import test.universal_taskflow.common.utils.data.TestEvents;
//import test.universal_taskflow.common.utils.data.TestFromXml_NewField;

import universal_taskflow.common.utils.serialization.XStreamFacade;


public class XStreamFacadeTest
{
  private static final String OUT_FILE="c:/temp/Test.xml";
  private static final Class[] CLASSES =
  {
   ClassForSerializableEmpty.class,
   ClassForSerializableAliasAnotation.class,
   ClassForSerialization.class,
   //TestFromXml_NewField.class,
   TestEvents.class
  };
  //-------------------------------------------
  public XStreamFacadeTest()
  {
    super();
  }

  public static void main(String[] args)
  {
    String[] args2 =
    { XStreamFacadeTest.class.getName() };
    org.junit.runner.JUnitCore.main(args2);
  }

  @Before
  public void setUp()
    throws Exception
  {
  }

  @After
  public void tearDown()
    throws Exception
  {
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    for (Class cl : CLASSES) 
    {
      XStreamFacade.registerClass(cl);
    }
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    for (Class cl : CLASSES) 
    {
      XStreamFacade.unRegisterClass(cl);
    }
  }
  //=====================
  @Test
  public void testToXML_empty()
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    XStreamFacade.toXML(new ClassForSerializableEmpty(), OUT_FILE);
    assertEquals("<test.universal__taskflow.common.utils.data.ClassForSerializableEmpty serialVersionUID=\"1\"/>", getOutFile());
  }
  @Test
  public void testToXML_aliasAnotation()
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    XStreamFacade.toXML(new ClassForSerializableAliasAnotation(), OUT_FILE);
    assertEquals("<ClassForSerializableAliasAnotation serialVersionUID=\"1\"/>", getOutFile());
  }
  
  @Test
  public void testToXML()
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    ClassForSerialization obj = new ClassForSerialization();
    obj.getList().add(new ClassForSerialization());
    obj.setTs(new ClassForSerialization());
    XStreamFacade.toXML(obj, OUT_FILE);
    assertEquals("<ClassForSerialization serialVersionUID=\"2\" attr=\"attr\">\n" + 
    "  <simple>1</simple>\n" + 
    "  <rus>Цапля</rus>\n" + 
    "  <testEnum>TWO</testEnum>\n" + 
    "  <list>\n" + 
    "    <int>1</int>\n" + 
    "    <int>2</int>\n" + 
    "    <ClassForSerialization serialVersionUID=\"2\" attr=\"attr\">\n" + 
    "      <simple>1</simple>\n" + 
    "      <rus>Цапля</rus>\n" + 
    "      <testEnum>TWO</testEnum>\n" + 
    "      <list>\n" + 
    "        <int>1</int>\n" + 
    "        <int>2</int>\n" + 
    "      </list>\n" + 
    "    </ClassForSerialization>\n" + 
    "  </list>\n" + 
    "  <ts serialVersionUID=\"2\" attr=\"attr\">\n" + 
    "    <simple>1</simple>\n" + 
    "    <rus>Цапля</rus>\n" + 
    "    <testEnum>TWO</testEnum>\n" + 
    "    <list>\n" + 
    "      <int>1</int>\n" + 
    "      <int>2</int>\n" + 
    "    </list>\n" + 
    "  </ts>\n" + 
    "</ClassForSerialization>", 
                 getOutFile());
  }
//  @Test
//  public void testFromXML_simple()
//    throws UnsupportedEncodingException, FileNotFoundException, IOException
//  {
//    URL url = ClassForSerialization.class.getResource("TestUnserialization1.xml");
//    ClassForSerialization obj = (ClassForSerialization) XStreamFacade.fromXML(url);
//    List<Serializable> list = obj.getList();
//    assertEquals(1, list.get(0));
//    assertEquals(2, list.get(1));
//    assertEquals(ClassForSerialization.class, list.get(2).getClass());
//  }
  
//  @Test
//  public void testFromXML_newField()
//    throws UnsupportedEncodingException, FileNotFoundException, IOException
//  {
//    TestFromXml_NewField obj = (TestFromXml_NewField) XStreamFacade.fromXML(TestFromXml_NewField.class.getResource("TestFromXML_NewFields.xml"));
//    assertEquals(11, obj.getNewField1());
//    assertEquals(22, obj.getNewField2());
//  }
  @Test
  public void testEvent_xstreamBeforeMarshall_xstreamAfterUnMarshall()
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    TestEvents obj = new TestEvents();
    XStreamFacade.toXML(obj, OUT_FILE);
    obj = (TestEvents) XStreamFacade.fromXML(OUT_FILE);
    assertEquals("1", obj.getDymamic_attr());
  }
  
//  @Test
//  public void testFromXML_userRecord()
//    throws UnsupportedEncodingException, FileNotFoundException, IOException
//  {
//    Object obj = XStreamFacade.fromXML("/JDeveloper/mywork/11119/RcoreTaskFlow/RcoreTaskFlowImpl/Test/public_html/server_taskflow_repository/user_taskflow/portlet1/anonymous/127.0.0.1.xml");
//    System.out.println("@obj="+obj);
//  }
  @Test
  public void testEnum()
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    TestEnum te = TestEnum.B;
    te.setF1(12);
    XStreamFacade.toXML(te, OUT_FILE);
    te.setF1(13);
    te = (TestEnum) XStreamFacade.fromXML(OUT_FILE);
    assertEquals(new Integer(12), te.getF1());
  }
  //---------------
  private String getOutFile() 
  {
    return Files.getFile(OUT_FILE);
  }
}
