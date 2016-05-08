package com.rcore.global.config;
// ШМЯ

import com.rcore.global.Resource;

import java.io.IOException;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


public class GlobalResourceBundle extends ResourceBundle implements RcoreBundle
{
  private Properties properties = null;
  private static final String COMMON_RESOURCE_BUNDLE_DEFAULT = "/com/rcore/global/config/common.properties.xml";
  private static final String LANG_RESOURCE_BUNDLE_DEFAULT   = "/com/rcore/global/config/lang/default.properties.xml";
  
  protected String getCommonBundleResource() 
  {
    return COMMON_RESOURCE_BUNDLE_DEFAULT;
  }
  protected String getLangBundleResource() 
  {
    return LANG_RESOURCE_BUNDLE_DEFAULT;
  }

  private static volatile GlobalResourceBundle lastInstance = null;  
  
  public static synchronized  GlobalResourceBundle getInstance() 
  {
    if (lastInstance == null) 
    {
      lastInstance = new GlobalResourceBundle();
    }
    return lastInstance;
  }
  
  public GlobalResourceBundle()
  {
    this(Locale.getDefault());
  }
  
  public GlobalResourceBundle(Locale locale) 
  {
    super();
    properties = getPropertiesFromResource(getCommonBundleResource());
    String currentLocale = properties.getProperty("current.locale","byJVM");
    //---------------------------------------------------
    String targetResource = null;
    if ("byJVM".equals(currentLocale) || currentLocale == null || "".equals(currentLocale))
    {
      targetResource = getResourceByLocale(locale);
    }
    else if ("default".equals(currentLocale))
    {
      targetResource = getLangBundleResource();
    }
    else 
    {
      String[] tok = currentLocale.split(",");
      Locale targetLocale = null;
      if (tok.length == 1) 
      {
        targetLocale = new Locale(tok[0]);
      }
      if (tok.length > 1) 
      {
        targetLocale = new Locale(tok[0],tok[1]);
      }
      targetResource = getResourceByLocale(targetLocale);
    }
    //-----------------------------------------
    Properties langProps = getPropertiesFromResource(targetResource);
    properties.putAll(langProps);
    //---------------------------
    lastInstance = this;
  }
  
  private String getResourceByLocale(Locale locale) 
  {
    String targetResource = null;
    String language = locale.getLanguage();
    String country = locale.getCountry();
    targetResource = "/global/config/lang/${language}_${country}.properties.xml";
    targetResource = targetResource.replace("${language}", language);
    targetResource = targetResource.replace("${country}", country);
    if (!Resource.isResourceExists(GlobalResourceBundle.class, targetResource))
    {
      targetResource = "/global/config/lang/${language}.properties.xml";
      targetResource = targetResource.replace("${language}", language);
      if (!Resource.isResourceExists(GlobalResourceBundle.class, targetResource))
      {
        targetResource = getLangBundleResource();
      }
    }
    return targetResource;
  }
  

  private static Properties getPropertiesFromResource(String resourceBundle) 
  {
    Properties applicationProps = new Properties();
    InputStream is = Resource.getResourceInputStream(Resource.class,resourceBundle);
    try {
      if (resourceBundle.toLowerCase().endsWith(".xml")) {
        applicationProps.loadFromXML(is);
      }
      else 
      {
        applicationProps.load(is);
      }
      is.close();
    }
    catch (InvalidPropertiesFormatException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return applicationProps;
  }

  @Override
  protected Object handleGetObject(String key)
  {
    return properties.get(key);
  }

  @Override
  public Enumeration getKeys()
  {
    return properties.keys();
  }
  
  @Override
  public String getProperty(final String key) {
    return (String)(handleGetObject(key));
  }
  
  @Override
  public Object get(final String key) {
    return handleGetObject(key);
  }
  
  
//  public Properties getProperties() 
//  {
//    return properties;
//  }

  @Override
  public void putAll(String resourceName)
  {
    Properties props = getPropertiesFromResource(resourceName);
    properties.putAll(props);
  }
  //---------------------------------
  public static void main(String[] args)
  {
    GlobalResourceBundle b = new GlobalResourceBundle();
    System.out.println(b.properties);
  }
}
