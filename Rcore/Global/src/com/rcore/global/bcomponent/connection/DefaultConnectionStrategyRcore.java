package com.rcore.global.bcomponent.connection;

import java.util.Properties;

import oracle.jbo.ApplicationModule;
import oracle.jbo.ApplicationPoolSvcMsgContext;
import oracle.jbo.common.ampool.DefaultConnectionStrategy;
import oracle.jbo.common.ampool.EnvInfoProvider;
import oracle.jbo.common.ampool.SessionCookie;


// Referenced classes of package oracle.jbo.common.ampool:
//            ConnectionStrategy, SessionCookie, EnvInfoProvider

public class DefaultConnectionStrategyRcore extends DefaultConnectionStrategy
{

  public DefaultConnectionStrategyRcore()
  {
    super();
  }
  
  /*
  public void connect(ApplicationModule applicationModule, SessionCookie cookie, EnvInfoProvider envInfo)
  {
      ApplicationPoolSvcMsgContext ctx = cookie.getMessageContext();
      ctx.setApplicationModuleConnected(true);
      if(!applicationModule.getTransaction().isConnected())
      {
          ctx.setApplicationModuleConnected(false);
          try
          {
              if(ctx.getJDBCConnectionType() == 0)
                  applicationModule.getTransaction().connectToDataSource(null, ctx.getJDBCDataSourceName(), ctx.getJDBCDataSourceUser(), ctx.getJDBCDataSourcePassword(), false);
              else
              if(ctx.getJDBCConnectionType() == 1)
                  applicationModule.getTransaction().connect(ctx.getJDBCConnectString(), ctx.getJDBCProperties());
              ctx.setApplicationModuleConnected(true);
          }
          catch(Throwable t)
          {
              ctx.setApplicationModuleConnected(false);
              ctx.setStatus(2);
              ctx.setJDBCConnectException(t);
          }
      }
  }
  */

  public void connect(ApplicationModule applicationModule, SessionCookie cookie, EnvInfoProvider envInfo)
  {
    ApplicationPoolSvcMsgContext ctx = cookie.getMessageContext();
    ctx.setApplicationModuleConnected(true);
    if (!applicationModule.getTransaction().isConnected())
    {
      ctx.setApplicationModuleConnected(false);
      try
      {
        if (ctx.getJDBCConnectionType() == 0) {
          connectToDataSource(applicationModule,
                              ctx.getJDBCDataSourceName(),
                              ctx.getJDBCDataSourceUser(),
                              ctx.getJDBCDataSourcePassword()
                              );
          
        }
        else if (ctx.getJDBCConnectionType() == 1) {
          connect(applicationModule, ctx.getJDBCConnectString(), ctx.getJDBCProperties());
        }
        ctx.setApplicationModuleConnected(true);
      }
      catch (Throwable t)
      {
        ctx.setApplicationModuleConnected(false);
        ctx.setStatus(2);
        ctx.setJDBCConnectException(t);
      }
    }
  }
  
  
  /**
   * Следующие protected - методы - для перегрузки
   * Реализация в этом классе - оригинальная
   */
  protected void connectToDataSource(ApplicationModule applicationModule, 
                                     String jdbcDataSourceName,
                                     String jdbcDataSourceUser,
                                     String jdbcDataSourcePassword
                                     )
  {
    applicationModule.getTransaction().connectToDataSource(null, 
                                                           jdbcDataSourceName,
                                                           jdbcDataSourceUser,
                                                           jdbcDataSourcePassword, 
                                                           false);
  }
  
  protected void connect(ApplicationModule applicationModule,
                         String jdbcConnectString, 
                         Properties jdbcProperties) 
  {
    applicationModule.getTransaction().connect(jdbcConnectString, jdbcProperties);
  }
  

// Закоменторован настоящий работающий код декомпилированного класса
//  public void reconnect(ApplicationModule applicationModule, SessionCookie cookie, EnvInfoProvider envInfo)
//  {
//    ApplicationPoolSvcMsgContext ctx = cookie.getMessageContext();
//    if (ctx.isApplicationModuleConnected())
//      return;
//    if (applicationModule.getTransaction().isConnected())
//    {
//      ctx.setApplicationModuleConnected(true);
//      return;
//    }
//    try
//    {
//      if (ctx.getJDBCConnectionType() != 2)
//      {
//        Diagnostic.println("DefaultConnectionStrategy is re-establishing an application module connection");
//        try
//        {
//          applicationModule.getTransaction().reconnect();
//          ctx.setApplicationModuleConnected(true);
//        }
//        catch (NotConnectedException ncex)
//        {
//          connect(applicationModule, cookie, envInfo);
//        }
//      }
//    }
//    catch (JboException ex)
//    {
//      if ("25224".equals(ex.getErrorCode()))
//      {
//        boolean activationRequired = false;
//        int psId = -1;
//        if (ctx.isRetainState())
//        {
//          Diagnostic.println("Passivating the application module state.");
//          psId = applicationModule.passivateState(null, 0);
//          activationRequired = true;
//        }
//        applicationModule.getTransaction().disconnect();
//        ctx.setApplicationModuleConnected(false);
//        try
//        {
//          applicationModule.getTransaction().reconnect();
//          ctx.setApplicationModuleConnected(true);
//        }
//        catch (NotConnectedException ncex)
//        {
//          connect(applicationModule, cookie, envInfo);
//        }
//        if (activationRequired)
//        {
//          applicationModule.activateState(psId, null, 2);
//          ctx.addRtnEvent(6);
//        }
//      }
//      else
//      {
//        throw ex;
//      }
//    }
//  }
//
//  public void disconnect(ApplicationModule applicationModule, boolean retainState, SessionCookie cookie)
//  {
//    ApplicationPoolSvcMsgContext ctx = cookie.getMessageContext();
//    if (ctx.getJDBCConnectionType() == 2)
//    {
//      if (!retainState)
//        applicationModule.resetState(3);
//      return;
//    }
//    try
//    {
//      if (retainState)
//        try
//        {
//          applicationModule.getTransaction().disconnect(true);
//        }
//        catch (JboException ex)
//        {
//          if (ex.getErrorCode() != null && ex.getErrorCode().equals("25224"))
//          {
//            Diagnostic.println("Database state was detected while disconnecting the application module's connection");
//            doHandleDatabaseState(applicationModule, ctx);
//            try
//            {
//              applicationModule.getTransaction().disconnect(true);
//            }
//            catch (NotConnectedException ncex)
//            {
//              ;
//            }
//          }
//          else
//          {
//            throw ex;
//          }
//        }
//      else
//        applicationModule.getTransaction().disconnect();
//    }
//    catch (NotConnectedException ncex)
//    {
//      ;
//    }
//  }
}
