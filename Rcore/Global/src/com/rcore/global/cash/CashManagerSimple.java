package com.rcore.global.cash;

public interface CashManagerSimple<K, V>
{
  V get(K key);
  void put(K key, V value);
  void remove(K key);
  void clear();
  void removeExpiredObjects();
}
