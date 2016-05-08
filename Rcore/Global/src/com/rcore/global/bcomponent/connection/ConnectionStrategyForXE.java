package com.rcore.global.bcomponent.connection;

import java.util.Locale;
import java.util.Properties;

import oracle.jbo.ApplicationModule;
import oracle.jbo.common.ampool.DefaultConnectionStrategy;
import oracle.jbo.common.ampool.EnvInfoProvider;
import oracle.jbo.common.ampool.SessionCookie;

// com.rcore.global.bcomponent.connection.ConnectionStrategyForXE

public class ConnectionStrategyForXE extends DefaultConnectionStrategy
{
  public ConnectionStrategyForXE() 
  {
    super();
  }
  public void connect(ApplicationModule applicationModule, SessionCookie cookie, EnvInfoProvider envInfo) 
  {
    Locale current = Locale.getDefault();
    try
    {
      Locale.setDefault(Locale.ENGLISH);
      super.connect(applicationModule, cookie, envInfo);
    }
    finally
    {
      Locale.setDefault(current);
    }
  }
  public void reconnect(oracle.jbo.ApplicationModule p1, oracle.jbo.common.ampool.SessionCookie p2, oracle.jbo.common.ampool.EnvInfoProvider p3) { 
    Locale current = Locale.getDefault();
    try
    {
      Locale.setDefault(Locale.ENGLISH);
      super.reconnect(p1, p2, p3);
    }
    finally
    {
      Locale.setDefault(current);
    }
  }
}
