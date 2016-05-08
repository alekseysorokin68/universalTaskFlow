package com.rcore.global.bcomponent.transaction;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import oracle.jbo.server.DBTransactionImpl2;


public class TransactionFacade
{
  private TransactionFacade()
  {
    super();
  }
  //---------------------------
  public static void startTransaction(DBTransactionImpl2 tran) 
  {
    tran.commit();
    return;
  }
  public static boolean isDirty(DBTransactionImpl2 tran) 
  {
    return tran.isDirty();
  }
  public static enum LockMode 
  {
    LOCK_NONE(DBTransactionImpl2.LOCK_NONE),
    LOCK_PESSIMISTIC(DBTransactionImpl2.LOCK_PESSIMISTIC),
    LOCK_OPTIMISTIC(DBTransactionImpl2.LOCK_OPTIMISTIC),
    LOCK_OPTUPDATE(DBTransactionImpl2.LOCK_OPTUPDATE)
    ;
    private int value = DBTransactionImpl2.LOCK_NONE;

    private LockMode(int value) 
    {
      this.value = value;
    }
    public int getValue() 
    {
      return value;
    }
  }
  
  public static void setLockMode(DBTransactionImpl2 tran, LockMode mode) 
  {
    tran.setLockingMode(mode.getValue());
  }
  public static LockMode getLockMode(DBTransactionImpl2 tran) 
  {
    int lock = tran.getLockingMode();
    LockMode[] values = LockMode.values();
    LockMode rc = LockMode.LOCK_NONE;
    for (LockMode item : values) 
    {
      if (item.getValue() == lock) 
      {
        rc = item;
        break;
      }
    } // for
    return rc;
  }
  //============================================================================================
  /*
   * Закрывать полученный ResultSet следует так:
   * rs.getStatement().close(); // Закроется и Statement и ResultSet
   */
  public static ResultSet executeQuery(String sql, DBTransactionImpl2 tran, int noRowsPrefetch) throws SQLException
  {
    Statement st = tran.createStatement(noRowsPrefetch);
    ResultSet rc = st.executeQuery(sql);
    return rc;
  }
  public static int executeUpdate(String sql, DBTransactionImpl2 tran) throws SQLException
  {
    int rc = tran.executeCommand(sql);
    return rc;
  }
  /*
   * Закрывать полученный ResultSet следует так:
   * rs.getStatement().close(); // Закроется и Statement и ResultSet
   */
  public static ResultSet executeQuery(String sql, DBTransactionImpl2 tran, int noRowsPrefetch, Object[] params) throws SQLException
  {
    PreparedStatement st = tran.createPreparedStatement(sql, noRowsPrefetch);
    setParameters(st, params);
    ResultSet rc = st.executeQuery();
    return rc;
  }
  public static int executeUpdate(String sql, DBTransactionImpl2 tran, Object[] params) throws SQLException
  {
    PreparedStatement st = tran.createPreparedStatement(sql, 1);
    setParameters(st, params);
    int rc = st.executeUpdate();
    st.close();
    return rc;
  }
  // --------------------------------------------------------------------
  public static void setParameters(PreparedStatement st, Object[] params)
    throws SQLException
  {
    int i = 0;
    Object param = null;
    try
    {
      for (i = 0; i < params.length; i++)
      {
        param = params[i];
        if (param == null)
        {
          st.setNull(i + 1, Types.VARCHAR);
        }
        else if (param instanceof String || param instanceof Integer || 
                 param instanceof Character || param instanceof Byte || 
                 param instanceof Long
                )
        {
          st.setString(i + 1, param.toString());
        }
        else if (param instanceof java.sql.Date)
        {
          st.setDate(i + 1, (java.sql.Date) param);
        }
        
        else if (param instanceof java.util.Date)
        {
          java.util.Date du = (java.util.Date)param;
          java.sql.Date  ds = new java.sql.Date(du.getTime());
          st.setDate(i + 1, ds);
        }
        
        
        else if (param instanceof NullDb)
        {
          st.setNull(i + 1, ((NullDb) param).type);
        }
        else
        {
          st.setObject(i + 1, param);
        }
      } // for
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  public static class NullDb
  {
    int type = Types.VARCHAR;
    public NullDb(int type)
    {
      super();
      this.type = type;
    }
  }

  
}
