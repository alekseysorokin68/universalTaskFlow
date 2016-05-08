package universal_taskflow.common.utils;


import com.rcore.model.jdbc.NamedParameterStatement;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;


public interface DbUtils
{
  int getNextSeq(String seq, Connection con);
  //----------------------------------------------
  List<Map<String, Object>> query2List(String sql, Connection con) 
    throws SQLException;
  List<Map<String, Object>> query2List(String sql, int max, Connection con)  
    throws SQLException;
  List<Map<String, Object>> query2List(String sqlQuery,
                                        Map<String, Object> params,
                                        Connection con) 
                                        throws SQLException;
  List<Map<String, Object>> query2List(String sqlQuery,
                                                Object[] params,
                                                Connection con) 
                                                throws SQLException;
  List<Map<String, Object>> query2List(String sqlQuery,
                                       Object[] params,
                                       int max,
                                       Connection con)
  throws SQLException;
  List<Map<String, Serializable>> query2ListSerializable(
                                                String sqlQuery,
                                                Object[] params,
                                                int max,
                                                Connection con)
  throws SQLException;
  List<Map<String, Object>> query2List(String sqlQuery,
                                                Map<String, Object> params,
                                                int max,
                                                Connection con)
      throws SQLException;
  List<Map<String, Serializable>> query2ListSerializable(
                                                String sqlQuery,
                                                Map<String, 
                                                Object> params,
                                                int max,
                                                Connection con)
      throws SQLException;
  
  ResultSet query2ResultSet(String sql, Connection con)
      throws SQLException;
  ResultSet query2ResultSet(String sqlQuery, Object[] params,
                                            Connection con)
      throws SQLException;;
  
  int executeUpdate(String sql, Connection con)
      throws SQLException;
  
  int executeUpdate(String sql, 
                                 Object[] params,
                                 Connection con)
      throws SQLException;
  
  int executeUpdate(String sql, 
                                 Map<String,Object> params, 
                                 Connection con)
      throws SQLException;
  
  int executeUpdateSerializable(String sql, Map<String,Serializable> params, Connection con)
      throws SQLException;
  Map<String, Object> resultSet2Record(ResultSet rs, ResultSetMetaData md)
      throws SQLException;
  Map<String, Serializable> resultSet2RecordSerializable(ResultSet rs, ResultSetMetaData md)
      throws SQLException;
  List<Map<String, Object>> resultSet2List(ResultSet rs)
      throws SQLException;
  Map<String, Object> insertAndReturnNewRecord(Connection con,
                                                        String sqlInsert,
                                                        Object[] sqlParams,
                                                        String table // может быть null
                                                        )
  throws SQLException;
  
  Map<String, Object> insertAndReturnNewRecord(Connection con,
                                                        String sqlInsert,
                                                        Object[] sqlParams,
                                                        String table, // может быть null
                                                        String fieldReturn
                                                        )
  throws SQLException;
  
  void setParameters(NamedParameterStatement st, Map<String, Object> params)
      throws SQLException;
  void setParametersSerializable(NamedParameterStatement st, Map<String, Serializable> params)
      throws SQLException;
  void setParameters(PreparedStatement st, Object[] params)
      throws SQLException;
  void executeUpdateList(Connection con, List<String> list)
      throws SQLException;
  Connection getConnection(final String dataSourceName)
      throws Exception;
}
