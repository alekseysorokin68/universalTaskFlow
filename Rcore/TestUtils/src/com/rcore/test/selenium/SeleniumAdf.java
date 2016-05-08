package com.rcore.test.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class SeleniumAdf extends WebDriverBackedSelenium //DefaultSelenium
{
  public SeleniumAdf(com.google.common.base.Supplier<WebDriver> maker, String baseUrl)
  {
    super(maker, baseUrl);
  }
  
  public SeleniumAdf(WebDriver baseDriver, String baseUrl) 
  {
    super(baseDriver, baseUrl);
  }
  //----------------------------------------------------------------------
    public SeleniumAdf(String baseUrl) 
    {
      super(new FirefoxDriver(), baseUrl);
    }
  //----------------------------------------------------------------------
  public WebElement findElementByPartId(String partId) 
  {
    return findElementByPartId(partId,null,null);
  }
  
  public WebElement findElementByPartId(String partId,String tagName) 
  {
    return findElementByPartId(partId,tagName,null);
  }
  
  public WebElement findElementByPartId(String partId,String tagName,TypeFind typeFind )
  {
    return SeleniumUtils.findElementByPartId(getWrappedDriver(), partId, tagName, typeFind);
  }
  
  public String getIdByPartId(String partId) 
  {
    return getIdByPartId(partId,null,null);
  }
  public String getIdByPartId(String partId,String tagName) 
  {
    return getIdByPartId(partId,tagName,null);
  }
  public String getIdByPartId(String partId,String tagName,TypeFind typeFind ) 
  {
    WebElement element = findElementByPartId(partId, tagName, typeFind);
    String id = element.getAttribute("id");
    return id;
  }
  
  public WebElement getElementById(String id) 
  {
    return getWrappedDriver().findElement(By.id(id));
  }
  
  //------------Using ::content ------------------------
  public String adfId2JsId(String adfId) 
  {
    return SeleniumUtils.adfId2JsId(adfId);
  }
  public WebElement findElementByAdfIdUsingConvertToJsId(String adfId) 
  {
    String jsId = adfId2JsId(adfId);
    return getWrappedDriver().findElement(By.id(jsId));
  }
  // ---- JavaScript ----
  public void setValueAdf(String id, String value)
  {
    String js = "var comp=AdfPage.PAGE.findComponentByAbsoluteId('"+id+"'); comp.setValue('"+value+"');";
    runScript(js);
  }
  public String getValueAdf(String id) 
  {
    String js = "var comp=AdfPage.PAGE.findComponentByAbsoluteId('"+id+"'); comp.getValue();";
    return getEval(js);
  }
  //------ Отметка методов которые плохо работают: ------------------
  @Deprecated
  @Override  
  public void type(String id, String value)
  {
    super.type(id, value);
  }
  
  @Deprecated
  @Override  
  public void select(String id, String value)
  {
    super.select(id, value);
  }
}
