package com.rcore.global.adf;
// ШМЯ

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AdfJdbcUtilsInt
{
  public abstract void setTimeOut(int timeOut);
  //-----
  public abstract ResultSet executeQuery(String sql,Connection con) throws SQLException;
  public abstract ResultSet executeQuery(String sql, Object[] params, Connection con) throws SQLException;
  //-------------
  public abstract int executeUpdateWithOutCommit(String sql, Connection con) throws SQLException;
  public abstract int executeUpdateWithOutCommit(String sql,Object[] params,Connection con) throws SQLException;
}
