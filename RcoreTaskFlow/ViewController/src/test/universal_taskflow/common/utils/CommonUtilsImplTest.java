package test.universal_taskflow.common.utils;

import java.net.URL;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.types.DebugMode;
import universal_taskflow.common.utils.CommonUtilsImpl;

public class CommonUtilsImplTest
{
  public CommonUtilsImplTest()
  {
    super();
  }

  public static void main(String[] args)
  {
    String[] args2 =
    { CommonUtilsImplTest.class.getName() };
    JUnitCore.main(args2);
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }
  
  private static DebugMode mode = TaskFlowSystemProperties.getInstance().getDebugMode();
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    TaskFlowSystemProperties.getInstance().setDebugMode(DebugMode.DEBUG_OUT_TASKFLOW_OUT_WEB);
  }

  @AfterClass
  public static void tearDownAfterClass()  throws Exception
  {
    TaskFlowSystemProperties.getInstance().setDebugMode(mode);
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#getDataInfo()
   */
  @Test
  public void testGetDataInfo()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#getMainRecord()
   */
  @Test
  public void testGetMainRecord()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#getUserRecord()
   */
  @Test
  public void testGetUserRecord()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#getUserData()
   */
  @Test
  public void testGetUserData()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#isModelReadOnly()
   */
  @Test
  public void testIsModelReadOnly()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#refreshData()
   */
  @Test
  public void testRefreshData()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#getUserName()
   */
  @Test
  public void testGetUserName()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#isTaskFlowActivated()
   */
  @Test
  public void testIsTaskFlowActivated()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#setTaskFlowActivated(boolean)
   */
  @Test
  public void testSetTaskFlowActivated()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#setTextNotActivated(String)
   */
  @Test
  public void testSetTextNotActivated()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#getTextNotActivated()
   */
  @Test
  public void testGetTextNotActivated()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#getTaskFlowParametersFromRequest()
   */
  @Test
  public void testGetTaskFlowParametersFromRequest()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#update()
   */
  @Test
  public void testUpdate()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#updateUserData()
   */
  @Test
  public void testUpdateUserData()
  {
    // TODO
  }

  /**
   * universal_taskflow.common.utils.CommonUtilsImpl#saveSqlParametersToUserData(java.util.Map<java.lang.String, java.io.Serializable>)
   */
  @Test
  public void testSaveSqlParametersToUserData()
  {
    // TODO
  }

  /**
   * @see universal_taskflow.common.utils.CommonUtilsImpl#registerError(Exception)
   */
  @Test
  public void testRegisterError()
  {
    // TODO
  }
  //=================================
//  @Test
//  public void testGetFullUrlToUserRecord()
//  {
//    URL url = CommonUtilsImpl.getInstance().getFullUrlToUserRecord();
//    assertEquals(url.getFile(), 
//"/JDeveloper/mywork/11119/RcoreTaskFlow/RcoreTaskFlowImpl/Test/public_html/"+
//"server_taskflow_repository/"+
//"user_taskflow/debug_taskflow_1/anonymous/debug_ip.xml");
//  }
//  @Test
//  public void testGetFullUrlToMainRecord()
//  {
//    URL url = CommonUtilsImpl.getInstance().getFullUrlToMainRecord();
//    String check = 
//      "/JDeveloper/mywork/11119/RcoreTaskFlow/RcoreTaskFlowImpl/Test/public_html/"+
//      "server_taskflow_repository/main_taskflow/debug_taskflow_1.xml";
//    assertEquals(url.getFile(), check);
//  }


}
