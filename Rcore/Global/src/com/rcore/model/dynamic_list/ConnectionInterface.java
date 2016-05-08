package com.rcore.model.dynamic_list;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionInterface extends Serializable
{
  Connection getConnection();
  void closeConnection(Connection con) throws SQLException;
}
