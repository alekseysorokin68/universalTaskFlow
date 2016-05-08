package com.rcore.global.config;

public interface RcoreBundle
{
  void putAll(String resourceName); // .properties или .xml
  String getProperty(String key);
  Object get(String key);
}
