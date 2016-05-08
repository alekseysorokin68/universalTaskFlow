package com.rcore.model.jdbc;


import com.rcore.global.MapWithOrderKeys;

import java.io.InputStream;
import java.io.Reader;

import java.math.BigDecimal;

import java.net.URL;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * This class wraps around a and allows the
 * programmer to set parameters by name instead
 * of by index.  This eliminates any confusion as to which parameter index
 * represents what.  This also means that
 * rearranging the SQL statement or adding a parameter doesn't involve
 * renumbering your indices.
 * Code such as this:
 *
 * <PRE>
 * Connection con=getConnection();
 * String query="select * from my_table where name=? or address=?";
 * PreparedStatement p=con.prepareStatement(query);
 * p.setString(1, "bob");
 * p.setString(2, "123 terrace ct");
 * ResultSet rs=p.executeQuery();
 * </PRE>
 *
 * can be replaced with:
 *
 * <PRE>
 * Connection con=getConnection();
 * String query="select * from my_table where name=:name or address=:address";
 * NamedParameterStatement p=new NamedParameterStatement(con, query);
 * p.setString("name", "bob");
 * p.setString("address", "123 terrace ct");
 * ResultSet rs=p.executeQuery();
 * </PRE>
 *
 */
public class NamedParameterStatement implements Statement
    //implements PreparedStatement
{
  /** The statement this object is wrapping. */
  private final PreparedStatement statement;

  /** Maps parameter names to arrays of ints which are the parameter indices. */
  private final MapWithOrderKeys<String, int[]> indexMap;


  /**
   * Creates a NamedParameterStatement.  Wraps a call to
   * prepareStatement}.
   * @param connection the database connection
   * @param query      the parameterized query
   * @throws SQLException if the statement could not be created
   */
  public NamedParameterStatement(Connection connection, String query) throws SQLException
  {
    indexMap = new MapWithOrderKeys<String, int[]>();
    String parsedQuery = SqlParser.parseNamedParameters(query, indexMap);
    statement = connection.prepareStatement(parsedQuery);
  }
  
  public List<String> getNamedParametersList()
  {
    List<String> source = indexMap.getKeysList();
    List<String> rc = new ArrayList<String>(source.size());
    for(String item: source) rc.add(item);
    return rc;
  }
  
  public static void main(String[] args)
    throws CloneNotSupportedException
  {
    MapWithOrderKeys<String, int[]> indexMap = new MapWithOrderKeys<String, int[]>();
    indexMap.put("key1", new int[]{3,1});
    indexMap.put("key2", new int[]{3,1});
    List<String> rc = Arrays.asList(indexMap.getKeysList().toArray(new String[indexMap.size()]));
    System.out.println("@rc= "+rc);
//    Map<String, int[]> cloned = (Map<String, int[]>) indexMap.clone();
//    System.out.println(cloned.get("key1").length);
//    System.out.println(cloned.get("key1")[0]);
//    System.out.println(cloned.get("key1")[1]);
  }

  public NamedParameterStatement(Connection connection, String query, String[] fieldsForReturn)  
  throws SQLException
  {
    indexMap = new MapWithOrderKeys<String, int[]>();
    String parsedQuery = SqlParser.parseNamedParameters(query, indexMap);
    statement = connection.prepareStatement(parsedQuery,fieldsForReturn);
  }

  /**
   * Returns the indexes for a parameter.
   * @param name parameter name
   * @return parameter indexes
   * @throws IllegalArgumentException if the parameter does not exist
   */
  private int[] getIndexes(String name)
  {
    int[] indexes = indexMap.get(name);
    if (indexes == null)
    {
      throw new IllegalArgumentException("Parameter not found: " + name);
    }
    return indexes;
  }


  /**
   * Sets a parameter.
   * @param name  parameter name
   * @param value parameter value
   * @throws SQLException if an error occurred
   * @throws IllegalArgumentException if the parameter does not exist
   * @see PreparedStatement#setObject(int, java.lang.Object)
   */
  public void setObject(String name, Object value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setObject(indexes[i], value);
    }
  }


  /**
   * Sets a parameter.
   * @param name  parameter name
   * @param value parameter value
   * @throws SQLException if an error occurred
   * @throws IllegalArgumentException if the parameter does not exist
   * @see PreparedStatement#setString(int, java.lang.String)
   */
  public void setString(String name, String value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setString(indexes[i], value);
    }
  }


  /**
   * Sets a parameter.
   * @param name  parameter name
   * @param value parameter value
   * @throws SQLException if an error occurred
   * @throws IllegalArgumentException if the parameter does not exist
   * @see PreparedStatement#setInt(int, int)
   */
  public void setInt(String name, int value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setInt(indexes[i], value);
    }
  }


  /**
   * Sets a parameter.
   * @param name  parameter name
   * @param value parameter value
   * @throws SQLException if an error occurred
   * @throws IllegalArgumentException if the parameter does not exist
   * @see PreparedStatement#setInt(int, int)
   */
  public void setLong(String name, long value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setLong(indexes[i], value);
    }
  }


  /**
   * Sets a parameter.
   * @param name  parameter name
   * @param value parameter value
   * @throws SQLException if an error occurred
   * @throws IllegalArgumentException if the parameter does not exist
   * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
   */
  public void setTimestamp(String name, Timestamp value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setTimestamp(indexes[i], value);
      // TODO Возможно для Oracle нужно:
      //Date date = new Date(value.getTime());
      //statement.setDate(indexes[i], date);
    }
  }
  
  public void setDouble(String name, double value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setDouble(indexes[i], value);
    }
  }
  
  public void setFloat(String name, float value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setFloat(indexes[i], value);
    }
  }


  /**
   * Returns the underlying statement.
   * @return the statement
   */
  public PreparedStatement getStatement()
  {
    return statement;
  }


  /**
   * Executes the statement.
   * @return true if the first result is a {@link ResultSet}
   * @throws SQLException if an error occurred
   * @see PreparedStatement#execute()
   */
  public boolean execute()
    throws SQLException
  {
    return statement.execute();
  }


  /**
   * Executes the statement, which must be a query.
   * @return the query results
   * @throws SQLException if an error occurred
   * @see PreparedStatement#executeQuery()
   */
  public ResultSet executeQuery()
    throws SQLException
  {
    statement.setFetchSize(1000);
    return statement.executeQuery();
  }


  /**
   * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE statement;
   * or an SQL statement that returns nothing, such as a DDL statement.
   * @return number of rows affected
   * @throws SQLException if an error occurred
   * @see PreparedStatement#executeUpdate()
   */
  public int executeUpdate()
    throws SQLException
  {
    return statement.executeUpdate();
  }


  /**
   * Closes the statement.
   * @throws SQLException if an error occurred
   */
  public void close() throws SQLException
  {
    statement.close();
  }


  /**
   * Adds the current set of parameters as a batch entry.
   * @throws SQLException if something went wrong
   */
  public void addBatch() throws SQLException
  {
    statement.addBatch();
  }


  /**
   * Executes all of the batched statements.
   *
   * @return update counts for each statement
   * @throws SQLException if something went wrong
   */
  public int[] executeBatch() throws SQLException
  {
    return statement.executeBatch();
  }
  //=============================================


  public void clearParameters() throws SQLException
  {
    statement.clearParameters();
  }

  public ResultSetMetaData getMetaData() throws SQLException
  {
    return statement.getMetaData();
  }

  public ParameterMetaData getParameterMetaData() throws SQLException
  {
    return statement.getParameterMetaData();
  }

  public ResultSet executeQuery(String sql) throws SQLException
  {
    return statement.executeQuery(sql);
  }

  public int executeUpdate(String sql)  throws SQLException
  {
    return statement.executeUpdate(sql);
  }

  public int executeUpdate(String sql, int autoGeneratedKeys)
    throws SQLException
  {
    return statement.executeUpdate(sql,autoGeneratedKeys);
  }

  public int executeUpdate(String sql, int[] columnIndexes)
    throws SQLException
  {
    return statement.executeUpdate(sql,columnIndexes);
  }

  public int executeUpdate(String sql, String[] columnNames)
    throws SQLException
  {
    return statement.executeUpdate(sql,columnNames);
  }

  public int getMaxFieldSize()
    throws SQLException
  {
    return statement.getMaxFieldSize();
  }

  public void setMaxFieldSize(int max) throws SQLException
  {
    statement.setMaxFieldSize(max);
  }

  public int getMaxRows()
    throws SQLException
  {
    return statement.getMaxRows();
  }

  public void setMaxRows(int max)  throws SQLException
  {
    statement.setMaxRows(max);
  }

  public void setEscapeProcessing(boolean enable)
    throws SQLException
  {
    statement.setEscapeProcessing(enable);
  }

  public int getQueryTimeout()
    throws SQLException
  {
    return statement.getQueryTimeout();
  }

  public void setQueryTimeout(int seconds)
    throws SQLException
  {
    statement.setQueryTimeout(seconds);
  }

  public void cancel() throws SQLException
  {
    statement.cancel();
  }

  public SQLWarning getWarnings()
    throws SQLException
  {
    return statement.getWarnings();
  }

  public void clearWarnings()
    throws SQLException
  {
    statement.clearWarnings();
  }

  public void setCursorName(String name) throws SQLException
  {
    statement.setCursorName(name);
  }

  public boolean execute(String sql)
    throws SQLException
  {
    return statement.execute(sql);
  }

  public boolean execute(String sql, int autoGeneratedKeys)
    throws SQLException
  {
    return statement.execute(sql,autoGeneratedKeys);
  }

  public boolean execute(String sql, int[] columnIndexes)
    throws SQLException
  {
    return statement.execute(sql,columnIndexes);
  }

  public boolean execute(String sql, String[] columnNames)
    throws SQLException
  {
    return statement.execute(sql,columnNames);
  }

  public ResultSet getResultSet()
    throws SQLException
  {
    return statement.getResultSet();
  }

  public int getUpdateCount()
    throws SQLException
  {
    return statement.getUpdateCount();
  }

  public boolean getMoreResults()
    throws SQLException
  {
    return statement.getMoreResults();
  }

  public boolean getMoreResults(int current)
    throws SQLException
  {
    return statement.getMoreResults(current);
  }

  public void setFetchDirection(int direction)
    throws SQLException
  {
    statement.setFetchDirection(direction);
  }

  public int getFetchDirection()
    throws SQLException
  {
    return statement.getFetchDirection();
  }

  public void setFetchSize(int rows)
    throws SQLException
  {
    statement.setFetchSize(rows);
  }

  public int getFetchSize()
    throws SQLException
  {
    return statement.getFetchSize();
  }

  public int getResultSetConcurrency()
    throws SQLException
  {
    return statement.getResultSetConcurrency();
  }

  public int getResultSetType() throws SQLException
  {
    return statement.getResultSetType();
  }

  public void addBatch(String sql)  throws SQLException
  {
    statement.addBatch(sql);
  }

  public void clearBatch() throws SQLException
  {
    statement.clearBatch();
  }

  public Connection getConnection()
    throws SQLException
  {
    return statement.getConnection();
  }

  public ResultSet getGeneratedKeys()
    throws SQLException
  {
    return statement.getGeneratedKeys();
  }

  public int getResultSetHoldability()
    throws SQLException
  {
    return statement.getResultSetHoldability();
  }

  public boolean isClosed()
    throws SQLException
  {
    return statement.isClosed();
  }

  public void setPoolable(boolean poolable)
    throws SQLException
  {
    statement.setPoolable(poolable);
  }

  public boolean isPoolable()
    throws SQLException
  {
    return statement.isPoolable();
  }

  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return statement.unwrap(iface);
  }

  public boolean isWrapperFor(Class<?> iface)
    throws SQLException
  {
    return statement.isWrapperFor(iface);
  }

  public void setNull(String name, int sqlType)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNull(indexes[i], sqlType);
    }
  }

  public void setNull(String name, int sqlType, String typeName)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNull(indexes[i], sqlType, typeName);
    }
  }

  public void setBoolean(String name, boolean x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBoolean(indexes[i], x);
    }
  }

  public void setByte(String name, byte x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setByte(indexes[i], x);
    }
  }

  public void setShort(String name, short x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setShort(indexes[i], x);
    }
  }

  public void setBigDecimal(String name, BigDecimal x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBigDecimal(indexes[i], x);
    }
  }

  public void setBytes(String name, byte[] x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBytes(indexes[i], x);
    }
  }

  public void setDate(String name, Date x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setDate(indexes[i], x);
    }
  }

  public void setDate(String name, Date x, Calendar cal)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setDate(indexes[i], x, cal);
    }
  }

  public void setTime(String name, Time x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setTime(indexes[i], x);
    }
  }

  public void setTime(String name, Time x, Calendar cal)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setTime(indexes[i], x, cal);
    }
  }

  public void setTimestamp(String name, Timestamp x, Calendar cal)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setTimestamp(indexes[i], x, cal);
    }
  }

  public void setAsciiStream(String name, InputStream x, int length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setAsciiStream(indexes[i], x, length);
    }
  }

  public void setAsciiStream(String name, InputStream x,
                             long length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setAsciiStream(indexes[i], x, length);
    }
  }

  public void setAsciiStream(String name, InputStream x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setAsciiStream(indexes[i], x);
    }
  }

//  public void setUnicodeStream(String name, InputStream x,
//                               int length)
//    throws SQLException
//  {
//    int[] indexes = getIndexes(name);
//    for (int i = 0; i < indexes.length; i++)
//    {
//      statement.setUnicodeStream(indexes[i], x, length);
//    }
//  }

  public void setBinaryStream(String name, InputStream x,
                              int length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBinaryStream(indexes[i], x, length);
    }
  }

  public void setBinaryStream(String name, InputStream x,
                              long length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBinaryStream(indexes[i], x, length);
    }
  }

  public void setBinaryStream(String name, InputStream x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBinaryStream(indexes[i], x);
    }
  }

  public void setObject(String name, Object x, int targetSqlType)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setObject(indexes[i], x, targetSqlType);
    }
  }

  public void setObject(String name, 
                        Object x, 
                        int targetSqlType,
                        int scaleOrLength)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setObject(indexes[i], x, targetSqlType,scaleOrLength);
    }
  }

  public void setCharacterStream(String name, Reader reader,
                                 int length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setCharacterStream(indexes[i], reader, length);
    }
  }

  public void setCharacterStream(String name, Reader reader,
                                 long length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setCharacterStream(indexes[i], reader, length);
    }
  }

  public void setCharacterStream(String name, Reader reader)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setCharacterStream(indexes[i], reader);
    }
  }

  public void setRef(String name, Ref x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setRef(indexes[i], x);
    }
  }

  public void setBlob(String name, Blob x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBlob(indexes[i], x);
    }
  }

  public void setBlob(String name, InputStream inputStream,
                      long length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBlob(indexes[i], inputStream, length);
    }
  }

  public void setBlob(String name, InputStream inputStream)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setBlob(indexes[i], inputStream);
    }
  }

  public void setClob(String name, Clob x)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setClob(indexes[i], x);
    }
  }

  public void setClob(String name, Reader value, long length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setClob(indexes[i], value,length);
    }
  }

  public void setClob(String name, Reader value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setClob(indexes[i], value);
    }
  }

  public void setArray(String name, Array value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setArray(indexes[i], value);
    }
  }

  public void setURL(String name, URL value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setURL(indexes[i], value);
    }
  }

  public void setRowId(String name, RowId value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setRowId(indexes[i], value);
    }
  }

  public void setNString(String name, String value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNString(indexes[i], value);
    }
  }

  public void setNCharacterStream(String name, 
                                  Reader value,
                                  long length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNCharacterStream(indexes[i], value, length);
    }
  }

  public void setNCharacterStream(String name, Reader value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNCharacterStream(indexes[i], value);
    }
  }

  public void setNClob(String name, NClob value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNClob(indexes[i], value);
    }
  }

  public void setNClob(String name, Reader value, long length)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNClob(indexes[i], value, length);
    }
  }

  public void setNClob(String name, Reader value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setNClob(indexes[i], value);
    }
  }

  public void setSQLXML(String name, SQLXML value)
    throws SQLException
  {
    int[] indexes = getIndexes(name);
    for (int i = 0; i < indexes.length; i++)
    {
      statement.setSQLXML(indexes[i], value);
    }
  }

  @Override
  public void closeOnCompletion() throws SQLException
  {
    if (isCloseOnCompletion()) close();
  }

  @Override
  public boolean isCloseOnCompletion() throws SQLException
  {
    return true;
  }
}

