package com.rcore.global;
// ШМЯ

import java.util.HashMap;

/**
 * Класс для создания информационного "почтового" ящика приложения
 * 
 * @author mark
 */
public final class InfoBox
{
  private static HashMap<Object, Object> table = 
    new HashMap<Object, Object>();

  /**
   * Положить объект в почтовый ящик
   * 
   * @param key - ключ
   * @param value - значение
   */
  public static void putToBox(Object key, Object value)
  {
    table.put(key, value);
  }

  /**
   * Положить объект в почтовый ящик, если его там не было
   * 
   * @param key - ключ
   * @param value - значение
   */
  public static void putIfNotExist(Object key, Object value)
  {
    Object obj = table.get(key);
    if (obj == null)
      table.put(key, value);
  }

  /**
   * Получить объект из почтового ящика
   * 
   * @param key - ключ объекта
   */
  public static Object getFromBox(Object key)
  {
    return table.get(key);
  }

  /**
   * Сравнить объекты в почтовом ящике. С разными ключами
   * 
   * @param key1
   * @param key2
   * @return - true - если key1 эквивалентен key2 false - иначе
   */
  public static boolean equalsInBox(Object key1, Object key2)
  {
    Object obj1 = getFromBox(key1);
    Object obj2 = getFromBox(key2);
    return (obj1.equals(obj2));
  }

  /**
   * Удалить объект из почтового ящика
   * 
   * @param key - ключ
   * @return Object - объект из почтового ящика
   */
  public static Object removeFromBox(Object key)
  {
    return table.remove(key);
  }

  /**
   * Полностью очистить почтовый ящик
   */
  public static void clearBox()
  {
    table.clear();
  }

} // public final class InfoBox
