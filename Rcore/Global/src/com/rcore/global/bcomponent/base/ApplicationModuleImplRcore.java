package com.rcore.global.bcomponent.base;

import java.sql.Connection;

import oracle.jbo.ApplicationModuleHandle;
import oracle.jbo.client.Configuration;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.DBTransactionImpl;
import oracle.jbo.server.DBTransactionImpl2;
import oracle.jbo.server.SequenceImpl;


public class ApplicationModuleImplRcore extends ApplicationModuleImpl
{
  public ApplicationModuleImplRcore()
  {
    super();
  }
  public oracle.jbo.domain.Number getNextSequence(String seqName) 
  {
    SequenceImpl s = new SequenceImpl(seqName,getDBTransaction());
    return s.getSequenceNumber();
  }
  
  public DBTransactionImpl2 getDBTransactionImpl() 
  {
    DBTransactionImpl2 rc = null;
    DBTransaction tran = super.getDBTransaction();
    if (tran instanceof DBTransactionImpl2) 
    {
      rc = (DBTransactionImpl2)tran;
    }
    return rc;
  }
  
  public Connection getPersistManagerConnection() 
  {
    Connection rc = null;
    DBTransactionImpl tran = getDBTransactionImpl();
    rc = tran.getPersistManagerConnection();
    return rc;
  }
  
  /**
   *  Примеры:
   *  ApplicationModuleHandle handle = createRootApplicationModuleHandle("com.rcore.model.AppModule",
   *                                                                     "AppModuleShared");
   *  ApplicationModule sharedAM = handle.useApplicationModule();
   *  ...
   *  releaseRootApplicationModuleHandle(handle, false);
   *  
   *  Замечание:
   *  handle.getEnvironment() - содержит много полезной информации о соединении и даже пароль в открытом виде !!!
   *  String pass = handle.getEnvironment().get("password");
   *  Структура таблицы handle.getEnvironment() :
   *  {user=SCOTT, 
   *   password=tiger,
   *   jbo.jdbc.connectstring=jdbc:oracle:thin:@b2b-sibur-sso.vm-p.rdtex.ru:1521:orcl,
   *   jbo.project=com.rcore.model.Model, 
   *   AppModuleJndiName=com.rcore.model.AppModule, 
   *   DeployPlatform=LOCAL, 
   *   jbo.applicationmoduleclassname=com.rcore.model.AppModule, 
   *   jbo.jdbc.username=SCOTT, 
   *   jbo.ampool.initpoolsize=0, 
   *   jbo.ampool.maxpoolsize=1, 
   *   JDBCName=AddressDb, 
   *   name=AppModuleShared, 
   *   ApplicationName=com.rcore.model.AppModule, 
   *   jbo.locking.mode=optimistic, 
   *   jbo.ampool.sessioncookiefactoryclass=oracle.jbo.common.ampool.DefaultSessionCookieFactory, 
   *   jbo.ampool.isuseexclusive=false, 
   *   java.naming.factory.initial=oracle.jbo.common.JboInitialContextFactory, 
   *   jbo.jdbc.connectstring=jdbc:oracle:thin:@b2b-sibur-sso.vm-p.rdtex.ru:1521:orcl, 
   *   DBconnection=jdbc:oracle:thin:@b2b-sibur-sso.vm-p.rdtex.ru:1521:orcl, 
   *   jbo.ejb.useampool=false, 
   *   jbo.jdbc.password=tiger}
   *
   *  @param amDefName  - Имя Аррlication Module, вместе с пакетом
   *  @param configName - Имя конфигурации
   *  @return ApplicationModuleImpl
   */
  public static ApplicationModuleHandle createRootApplicationModuleHandle(String amDefName, String configName) 
  {
    ApplicationModuleHandle rc = Configuration.createRootApplicationModuleHandle(amDefName, configName);
    return rc;
  }
  public static void releaseRootApplicationModuleHandle(ApplicationModuleHandle handle, boolean isAppRemove) 
  {
    Configuration.releaseRootApplicationModuleHandle(handle, isAppRemove);
  }
  //----------------------------------------------------------------------

  
}
