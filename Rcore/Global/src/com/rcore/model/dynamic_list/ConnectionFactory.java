package com.rcore.model.dynamic_list;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import oracle.jbo.ApplicationModule;
import oracle.jbo.ViewObject;
import oracle.jbo.server.DBTransactionImpl;
import oracle.jbo.server.DataSourceContextFactory;


public final class ConnectionFactory
{
  public static ConnectionInterface getConnectionInterface(Connection conn) 
  {
    return new ConnectionInterfaceByConnection(conn);
  }
  public static ConnectionInterface getConnectionInterface(DBTransactionImpl tran) 
  {
    Connection conn = tran.getPersistManagerConnection();
    return new ConnectionInterfaceByConnection(conn);
  }
  public static ConnectionInterface getConnectionInterface(ApplicationModule appModule) 
  {
    return getConnectionInterface((DBTransactionImpl)appModule.getTransaction());
  }
  public static ConnectionInterface getConnectionInterface(String dsName) throws NamingException,
                                                                                 SQLException
  {
    return new ConnectionInterfaceByDatasourceName(dsName);
  }
  
  public static ConnectionInterface getConnectionInterface(ViewObject viewObject) 
  {
    return new ConnectionInterfaceByViewObject(viewObject);
  }
  
  public static Connection getConnection(String dsName) throws NamingException,
                                                        SQLException
  {
    Connection rc = null;
    final Hashtable<String, String> tableJndiEnv = new Hashtable<String, String>();
    tableJndiEnv.put("java.naming.factory.initial",
                     DataSourceContextFactory.class.getName());
    final Context initContext = new InitialContext(tableJndiEnv);
    final DataSource pool = (DataSource)initContext.lookup(dsName);
    rc = pool.getConnection();
    rc.setAutoCommit(false);
    return rc;
  }

  public static class ConnectionInterfaceByConnection implements ConnectionInterface
  {
    @SuppressWarnings("compatibility:-2372138181890180365")
    private static final long serialVersionUID = 1L;
    
    private transient Connection conn = null;
    private ConnectionInterfaceByConnection(Connection conn) 
    {
      super();
      this.conn = conn;
    }

    public Connection getConnection()
    {
      return this.conn;
    }

    public void closeConnection(Connection con)
    {
      ;
    }
  } // public static class ConnectionInterfaceByConnection implements ConnectionInterface
  
  public static class ConnectionInterfaceByDatasourceName implements ConnectionInterface
  {
    @SuppressWarnings("compatibility:-8166850460004209183")
    private static final long serialVersionUID = 1L;
    
    private String dsName = null;
    private ConnectionInterfaceByDatasourceName(String dsName) 
    {
      super();
      this.dsName = dsName;
    }

    public Connection getConnection()
    {
       Connection  rc = null;
      try {
        rc = ConnectionFactory.getConnection(dsName);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      return rc;
    }

    public void closeConnection(Connection con)
    {
      try {
        con.close();
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
  } // public static class ConnectionInterfaceByConnection implements ConnectionInterface
  
  public static class ConnectionInterfaceByViewObject implements ConnectionInterface 
  {
    @SuppressWarnings("compatibility:-2356093358242570499")
    private static final long serialVersionUID = 1L;
    private transient Connection conn = null;

    public ConnectionInterfaceByViewObject(ViewObject vo) 
    {
      super();
      ApplicationModule appMod = vo.getApplicationModule();
      DBTransactionImpl tran = (DBTransactionImpl)(appMod.getTransaction());
      conn = tran.getPersistManagerConnection();
    }
    public Connection getConnection()
    {
      return conn;
    }

    public void closeConnection(Connection con)
    {
      ;
    }
  }
  
  public static void main(String[] args) throws Exception
  {
    Connection con = getConnection("java:comp/env/jdbc/AddressDbDS");
    System.out.println("@="+con);
  }
  
}
