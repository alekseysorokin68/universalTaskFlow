package com.rcore.global.adf;
// ШМЯ

import java.io.Serializable;

import java.sql.Types;

public class NullDb implements Serializable
{
  @SuppressWarnings("compatibility:-268309434531893873")
  private static final long serialVersionUID = 1L;
  
  int type = Types.VARCHAR;

  public NullDb(int type)
  {
    super();
    this.type = type;
  }

  public int getType()
  {
    return type;
  }
}
