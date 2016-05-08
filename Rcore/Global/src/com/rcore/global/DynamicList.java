package com.rcore.global;
// ШМЯ


import java.util.ArrayList;


public abstract class DynamicList<E> extends ArrayList<E>
{
  private static final long serialVersionUID = 1L;

  public DynamicList()
  {
    super();
  }
  //-----------------------------------
  @Override
  public abstract int size();
  @Override
  public abstract E get(int i);
  //-----------------------------------
}
