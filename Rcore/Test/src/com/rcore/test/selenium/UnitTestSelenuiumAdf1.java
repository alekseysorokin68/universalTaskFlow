package com.rcore.test.selenium;


import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class UnitTestSelenuiumAdf1
{
  private static final String baseUrl = "http://127.0.0.1:7101/PPRForm-ViewController-context-root/";
  private static final String url = "faces/new_ver_1.1.1/testSeleniumAdf1.jspx";
  private static SeleniumAdf selenium = null;
  public UnitTestSelenuiumAdf1()
  {
    super();
  }

  @Before
  public void setUp() throws Exception
  {
    //selenium.start();
  }

  @After
  public void tearDown() throws Exception
  {
    //selenium.stop();
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    //selenium = new SeleniumAdf("localhost", 4444, "*firefox", baseUrl);
    selenium = new SeleniumAdf(baseUrl);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    //selenium.close();
  }
  //=======================================================
  @Test
  public void test11() throws InterruptedException
  {
    selenium.open(url);
    selenium.waitForPageToLoad("1000");
    //-------------------------------
    String id = null;
    WebElement element = selenium.findElementByAdfIdUsingConvertToJsId("text_field");
    element.sendKeys(new String[]{"Sample Text Field 1"});
    assertEquals( element.getAttribute("value") , "Sample Text Field 1" );
    //---
    element = selenium.findElementByAdfIdUsingConvertToJsId("text_area");
    element.sendKeys(new String[]{"Sample Text Area 1"});
    assertEquals( element.getAttribute("value") , "Sample Text Area 1" );
    element.clear();
    
    element = selenium.findElementByAdfIdUsingConvertToJsId("radio_2");
    element.sendKeys(new String[]{" "});
    assertEquals( element.getAttribute("selected") , "true" );
    
    element = selenium.findElementByAdfIdUsingConvertToJsId("check_box");
    element.sendKeys(new String[]{" "});
    assertEquals( element.getAttribute("selected") , "true" );
    
    element = selenium.findElementByAdfIdUsingConvertToJsId("check_box");
    element.sendKeys(new String[]{" "});
    String selected = element.getAttribute("selected");
    assertTrue(selected == null || selected.equals("false"));
    
    element = selenium.getElementById("ev_check_box");
    element.sendKeys(new String[]{" "});
    assertEquals( element.getAttribute("selected") , "true" );
    
    element = selenium.getElementById("deactivated_btn");  
    assertEquals( element.getAttribute("disabled") , "false" );
    
    element = selenium.getElementById("deactivated_btn");
    element.sendKeys(new String[]{" "});
    id = selenium.getIdByPartId("clicked_text","input");
    assertTrue( selenium.getValue(id).equals("Deactivated") );
    
    
    element = selenium.getElementById("btn_1");
    element.sendKeys(new String[]{" "});
    id = selenium.getIdByPartId("clicked_text","input");
    assertTrue( selenium.getValue(id).equals("First") );
    
    element = selenium.getElementById("btn_2");
    element.sendKeys(new String[]{" "});
    id = selenium.getIdByPartId("clicked_text","input");
    assertTrue( selenium.getValue(id).equals("Second") );
    
    // Работа со списками (select - ми)
    WebElement single_list = selenium.findElementByAdfIdUsingConvertToJsId("single_list");
    Select select = new Select(single_list);
    select.selectByIndex(1);
    assertEquals(selenium.getSelectedValue(single_list.getAttribute("id")),"1");
    assertEquals(selenium.getSelectedLabel( single_list.getAttribute("id") ),"Option 2");
  
    // MultiSelect:
    //String multiSelectId = selenium.adfId2JsId("multi_list");
    WebElement multiSelect = selenium.findElementByAdfIdUsingConvertToJsId("multi_list");
    assertTrue(multiSelect != null);
    // <input type="checkbox" tabindex="-1" class="x12z" value="1" 
    // name="multi_list"/>
    WebElement check1 = selenium.getWrappedDriver().findElement(By.xpath("//input[@type='checkbox' and @name='multi_list' and @value=1]"));
    assertTrue(check1 != null);
    check1.sendKeys(new String[]{" "});
    assertTrue(check1.isSelected());
    
    WebElement check2 = selenium.getWrappedDriver().findElement(By.xpath("//input[@type='checkbox' and @name='multi_list' and @value=2]"));
    assertTrue(check2 != null);
    check2.sendKeys(new String[]{" "});
    assertTrue(check2.isSelected());
  }
  
  
  @Test
  @Ignore
  public void test2() 
  {
    selenium.open(url);
    selenium.waitForPageToLoad("1000");
    
//    WebElement el = selenium.getElementById("alert_btn");
//    el.sendKeys(new String[]{" "});
//    selenium.waitForPageToLoad("1000");
//    //assertTrue( selenium.isAlertPresent() );
//    //assertEquals("This is Alert", selenium.getAlert());
//    System.out.println(selenium.getAllWindowNames().length);
    //selenium.runScript("alert(AdfPage.PAGE.findComponentByAbsoluteId('text_field').setValue('12345'))");
    selenium.setValueAdf("text_field", "09876");
    assertEquals(selenium.getValueAdf("text_field"), "09876");
  }
}
