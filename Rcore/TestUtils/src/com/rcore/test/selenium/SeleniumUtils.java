package com.rcore.test.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class SeleniumUtils
{
  public static WebElement findElementByPartId(WebDriver driver,String partId,String tagName,TypeFind typeFind )
  {
    WebElement rc = null;
    if (tagName == null) 
    {
      tagName = "*";
    }
    if (typeFind == null) 
    {
      typeFind = TypeFind.CONTAINS;
    }
    StringBuilder xPath = new StringBuilder("//").append(tagName);
    if (typeFind.equals(TypeFind.CONTAINS)) 
    {
      xPath.append("[contains(@id,'").append(partId).append("')]");
      rc = driver.findElement(By.xpath(xPath.toString()));
    }
    else if (typeFind.equals(TypeFind.STARTS_WITH)) 
    {
      xPath.append("[starts-with(@id,'").append(partId).append("')]");
      rc = driver.findElement(By.xpath(xPath.toString()));
    }
    else if (typeFind.equals(TypeFind.ENDS_WITH)) 
    {
      List<WebElement> list = driver.findElements(By.xpath(xPath.toString()));
      for(WebElement item : list) 
      {
        String id = item.getAttribute("id");
        if (id != null && id.endsWith(partId)) 
        {
          rc = item;
          break;
        }
      } // for
      
    }
    return rc;
  }
  
  public static String adfId2JsId(String adfId) 
  {
    return adfId+"::content";
  }
}
