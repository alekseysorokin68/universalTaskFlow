package com.rcore.model.jdbc;

import com.rcore.global.Reflection;

import java.io.InputStream;
import java.io.Reader;

import java.math.BigDecimal;

import java.net.URL;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
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

import java.util.Calendar;
import java.util.Map;


public class ResultSetStatementClose implements ResultSet
{

  private ResultSet rs = null;

  public ResultSetStatementClose(ResultSet resultSet)
  {
    this.rs = resultSet;
  }


  @Override
  public int findColumn(String columnName)
    throws SQLException
  {
    return rs.findColumn(columnName);
  }

  @Override
  public Array getArray(String colName)
    throws SQLException
  {
    return rs.getArray(findColumn(colName));
  }

  @Override
  public void updateArray(String columnName, Array x)
    throws SQLException
  {
    rs.updateArray(findColumn(columnName), x);
  }

  @Override
  public InputStream getAsciiStream(String columnName)
    throws SQLException
  {
    return rs.getAsciiStream(findColumn(columnName));
  }

  @Override
  public void updateAsciiStream(String columnName, InputStream x,
                                int length)
    throws SQLException
  {
    rs.updateAsciiStream(findColumn(columnName), x, length);
  }

  @Override
  public BigDecimal getBigDecimal(String columnName)
    throws SQLException
  {
    return rs.getBigDecimal(findColumn(columnName));
  }

  @Override
  public BigDecimal getBigDecimal(String columnName, int scale)
    throws SQLException
  {
    BigDecimal rc =
      (BigDecimal) Reflection.runInvokeMethod(rs, "getBigDecimal",
                                                new Object[]{columnName,scale},
                                                new Class[]{String.class,Integer.class}
                                                );
    return rc;
    //return rs.getBigDecimal(findColumn(columnName), scale);
  }

  @Override
  public void updateBigDecimal(String columnName, BigDecimal x)
    throws SQLException
  {
    rs.updateBigDecimal(findColumn(columnName), x);
  }

  @Override
  public InputStream getBinaryStream(String columnName)
    throws SQLException
  {
    return rs.getBinaryStream(findColumn(columnName));
  }

  @Override
  public void updateBinaryStream(String columnName, InputStream x,
                                 int length)
    throws SQLException
  {
    rs.updateBinaryStream(findColumn(columnName), x, length);
  }

  @Override
  public Blob getBlob(String columnName)
    throws SQLException
  {
    return rs.getBlob(findColumn(columnName));
  }

  @Override
  public void updateBlob(String columnName, Blob x)
    throws SQLException
  {
    rs.updateBlob(findColumn(columnName), x);
  }

  @Override
  public boolean getBoolean(String columnName)
    throws SQLException
  {
    return rs.getBoolean(findColumn(columnName));
  }

  @Override
  public void updateBoolean(String columnName, boolean x)
    throws SQLException
  {
    rs.updateBoolean(findColumn(columnName), x);
  }

  @Override
  public byte getByte(String columnName)
    throws SQLException
  {
    return rs.getByte(findColumn(columnName));
  }

  @Override
  public void updateByte(String columnName, byte x)
    throws SQLException
  {
    rs.updateByte(findColumn(columnName), x);
  }

  @Override
  public byte[] getBytes(String columnName)
    throws SQLException
  {
    return rs.getBytes(findColumn(columnName));
  }

  @Override
  public void updateBytes(String columnName, byte[] x)
    throws SQLException
  {
    rs.updateBytes(findColumn(columnName), x);
  }

  @Override
  public Reader getCharacterStream(String columnName)
    throws SQLException
  {
    return rs.getCharacterStream(findColumn(columnName));
  }

  @Override
  public void updateCharacterStream(String columnName, Reader x,
                                    int length)
    throws SQLException
  {
    rs.updateCharacterStream(findColumn(columnName), x, length);
  }

  @Override
  public Clob getClob(String columnName)
    throws SQLException
  {
    return rs.getClob(findColumn(columnName));
  }

  @Override
  public void updateClob(String columnName, Clob x)
    throws SQLException
  {
    rs.updateClob(findColumn(columnName), x);
  }

  @Override
  public Date getDate(String columnName)
    throws SQLException
  {
    return rs.getDate(findColumn(columnName));
  }

  @Override
  public Date getDate(String columnName, Calendar cal)
    throws SQLException
  {
    return rs.getDate(findColumn(columnName), cal);
  }

  @Override
  public void updateDate(String columnName, Date x)
    throws SQLException
  {
    rs.updateDate(findColumn(columnName), x);
  }

  @Override
  public double getDouble(String columnName)
    throws SQLException
  {
    return rs.getDouble(findColumn(columnName));
  }

  @Override
  public void updateDouble(String columnName, double x)
    throws SQLException
  {
    rs.updateDouble(findColumn(columnName), x);
  }

  @Override
  public float getFloat(String columnName)
    throws SQLException
  {
    return rs.getFloat(findColumn(columnName));
  }

  @Override
  public void updateFloat(String columnName, float x)
    throws SQLException
  {
    rs.updateFloat(findColumn(columnName), x);
  }

  @Override
  public int getInt(String columnName)
    throws SQLException
  {
    return rs.getInt(findColumn(columnName));
  }

  @Override
  public void updateInt(String columnName, int x)
    throws SQLException
  {
    rs.updateInt(findColumn(columnName), x);
  }

  @Override
  public long getLong(String columnName)
    throws SQLException
  {
    return rs.getLong(findColumn(columnName));
  }

  @Override
  public void updateLong(String columnName, long x)
    throws SQLException
  {
    rs.updateLong(findColumn(columnName), x);
  }

  @Override
  public Object getObject(String columnName)
    throws SQLException
  {
    return rs.getObject(findColumn(columnName));
  }

  @Override
  public Object getObject(String columnName, Map map)
    throws SQLException
  {
    Object rc =
      Reflection.runInvokeMethod(rs, "getObject",
                                      new Object[]{columnName,map},
                                      new Class[]{String.class,Map.class}
                                );
    return rc;
    //return rs.getObject(findColumn(columnName), map);
  }

  @Override
  public void updateObject(String columnName, Object x)
    throws SQLException
  {
    rs.updateObject(findColumn(columnName), x);
  }

  @Override
  public void updateObject(String columnName, Object x, int scale)
    throws SQLException
  {
    rs.updateObject(findColumn(columnName), x, scale);
  }

  @Override
  public Ref getRef(String columnName)
    throws SQLException
  {
    return rs.getRef(findColumn(columnName));
  }

  @Override
  public void updateRef(String columnName, Ref x)
    throws SQLException
  {
    rs.updateRef(findColumn(columnName), x);
  }

  @Override
  public short getShort(String columnName)
    throws SQLException
  {
    return rs.getShort(findColumn(columnName));
  }

  @Override
  public void updateShort(String columnName, short x)
    throws SQLException
  {
    rs.updateShort(findColumn(columnName), x);
  }

  @Override
  public String getString(String columnName)
    throws SQLException
  {
    return rs.getString(findColumn(columnName));
  }

  @Override
  public void updateString(String columnName, String x)
    throws SQLException
  {
    rs.updateString(findColumn(columnName), x);
  }

  @Override
  public Time getTime(String columnName)
    throws SQLException
  {
    return rs.getTime(findColumn(columnName));
  }

  @Override
  public Time getTime(String columnName, Calendar cal)
    throws SQLException
  {
    return rs.getTime(findColumn(columnName), cal);
  }

  @Override
  public void updateTime(String columnName, Time x)
    throws SQLException
  {
    rs.updateTime(findColumn(columnName), x);
  }

  @Override
  public Timestamp getTimestamp(String columnName)
    throws SQLException
  {
    return rs.getTimestamp(findColumn(columnName));
  }

  @Override
  public void updateTimestamp(String columnName, Timestamp x)
    throws SQLException
  {
    rs.updateTimestamp(findColumn(columnName), x);
  }

  @Override
  public Timestamp getTimestamp(String columnName, Calendar cal)
    throws SQLException
  {
    return rs.getTimestamp(findColumn(columnName), cal);
  }

  @Override
  public InputStream getUnicodeStream(String columnName)
    throws SQLException
  {
    InputStream rc =
      (InputStream) Reflection.runInvokeMethod(rs, "getUnicodeStream",
                                                new Object[]{columnName},
                                                new Class[]{String.class}
                                                );
    return rc;
    //return rs.getUnicodeStream(columnName);
    //return rs.getCharacterStream(columnName).
  }

  @Override
  public URL getURL(String columnName)
    throws SQLException
  {
    return rs.getURL(findColumn(columnName));
  }

  @Override
  public void updateNull(String columnName)
    throws SQLException
  {
    rs.updateNull(findColumn(columnName));
  }


  // ResultSet impl (delegated) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  @Override
  public int getConcurrency()
    throws SQLException
  {
    return rs.getConcurrency();
  }

  @Override
  public int getFetchDirection()
    throws SQLException
  {
    return rs.getFetchDirection();
  }

  @Override
  public int getFetchSize()
    throws SQLException
  {
    return rs.getFetchSize();
  }

  @Override
  public int getRow()
    throws SQLException
  {
    return rs.getRow();
  }

  @Override
  public int getType()
    throws SQLException
  {
    return rs.getType();
  }

  @Override
  public void afterLast()
    throws SQLException
  {
    rs.afterLast();
  }

  @Override
  public void beforeFirst()
    throws SQLException
  {
    rs.beforeFirst();
  }

  @Override
  public void cancelRowUpdates()
    throws SQLException
  {
    rs.cancelRowUpdates();
  }

  @Override
  public void clearWarnings()
    throws SQLException
  {
    rs.clearWarnings();
  }

  /**
   *  Именно для пеереопределения следующего метода этот класс
   */
  @Override
  public void close() throws SQLException
  {
    if (!rs.getStatement().isClosed()) 
    {
      rs.getStatement().close();
    }
    rs.close();
  }

  @Override
  public void deleteRow()
    throws SQLException
  {
    rs.deleteRow();
  }

  @Override
  public void insertRow()
    throws SQLException
  {
    rs.insertRow();
  }

  @Override
  public void moveToCurrentRow()
    throws SQLException
  {
    rs.moveToCurrentRow();
  }

  @Override
  public void moveToInsertRow()
    throws SQLException
  {
    rs.moveToInsertRow();
  }

  @Override
  public void refreshRow()
    throws SQLException
  {
    rs.refreshRow();
  }

  @Override
  public void updateRow()
    throws SQLException
  {
    rs.updateRow();
  }

  @Override
  public boolean first()
    throws SQLException
  {
    return rs.first();
  }

  @Override
  public boolean isAfterLast()
    throws SQLException
  {
    return rs.isAfterLast();
  }

  @Override
  public boolean isBeforeFirst()
    throws SQLException
  {
    return rs.isBeforeFirst();
  }

  @Override
  public boolean isFirst()
    throws SQLException
  {
    return rs.isFirst();
  }

  @Override
  public boolean isLast()
    throws SQLException
  {
    return rs.isLast();
  }

  @Override
  public boolean last()
    throws SQLException
  {
    return rs.last();
  }

  @Override
  public boolean next()
    throws SQLException
  {
    return rs.next();
  }

  @Override
  public boolean previous()
    throws SQLException
  {
    return rs.previous();
  }

  @Override
  public boolean rowDeleted()
    throws SQLException
  {
    return rs.rowDeleted();
  }

  @Override
  public boolean rowInserted()
    throws SQLException
  {
    return rs.rowInserted();
  }

  @Override
  public boolean rowUpdated()
    throws SQLException
  {
    return rs.rowUpdated();
  }

  @Override
  public boolean wasNull()
    throws SQLException
  {
    return rs.wasNull();
  }

  @Override
  public byte getByte(int columnIndex)
    throws SQLException
  {
    return rs.getByte(columnIndex);
  }

  @Override
  public double getDouble(int columnIndex)
    throws SQLException
  {
    return rs.getDouble(columnIndex);
  }

  @Override
  public float getFloat(int columnIndex)
    throws SQLException
  {
    return rs.getFloat(columnIndex);
  }

  @Override
  public int getInt(int columnIndex)
    throws SQLException
  {
    return rs.getInt(columnIndex);
  }

  @Override
  public long getLong(int columnIndex)
    throws SQLException
  {
    return rs.getLong(columnIndex);
  }

  @Override
  public short getShort(int columnIndex)
    throws SQLException
  {
    return rs.getShort(columnIndex);
  }

  @Override
  public void setFetchDirection(int direction)
    throws SQLException
  {
    rs.setFetchDirection(direction);
  }

  @Override
  public void setFetchSize(int rows)
    throws SQLException
  {
    rs.setFetchSize(rows);
  }

  @Override
  public void updateNull(int columnIndex)
    throws SQLException
  {
    rs.updateNull(columnIndex);
  }

  @Override
  public boolean absolute(int row)
    throws SQLException
  {
    return rs.absolute(row);
  }

  @Override
  public boolean getBoolean(int columnIndex)
    throws SQLException
  {
    return rs.getBoolean(columnIndex);
  }

  @Override
  public boolean relative(int rows)
    throws SQLException
  {
    return rs.relative(rows);
  }

  @Override
  public byte[] getBytes(int columnIndex)
    throws SQLException
  {
    return rs.getBytes(columnIndex);
  }

  @Override
  public void updateByte(int columnIndex, byte x)
    throws SQLException
  {
    rs.updateByte(columnIndex, x);
  }

  @Override
  public void updateDouble(int columnIndex, double x)
    throws SQLException
  {
    rs.updateDouble(columnIndex, x);
  }

  @Override
  public void updateFloat(int columnIndex, float x)
    throws SQLException
  {
    rs.updateFloat(columnIndex, x);
  }

  @Override
  public void updateInt(int columnIndex, int x)
    throws SQLException
  {
    rs.updateInt(columnIndex, x);
  }

  @Override
  public void updateLong(int columnIndex, long x)
    throws SQLException
  {
    rs.updateLong(columnIndex, x);
  }

  @Override
  public void updateShort(int columnIndex, short x)
    throws SQLException
  {
    rs.updateShort(columnIndex, x);
  }

  @Override
  public void updateBoolean(int columnIndex, boolean x)
    throws SQLException
  {
    rs.updateBoolean(columnIndex, x);
  }

  @Override
  public void updateBytes(int columnIndex, byte[] x)
    throws SQLException
  {
    rs.updateBytes(columnIndex, x);
  }

  @Override
  public InputStream getAsciiStream(int columnIndex)
    throws SQLException
  {
    return rs.getAsciiStream(columnIndex);
  }

  @Override
  public InputStream getBinaryStream(int columnIndex)
    throws SQLException
  {
    return rs.getBinaryStream(columnIndex);
  }

  @Override
  public InputStream getUnicodeStream(int columnIndex)
    throws SQLException
  {
    InputStream rc =
      (InputStream) Reflection.runInvokeMethod(rs, "getUnicodeStream",
                                                new Object[]{columnIndex},
                                                new Class[]{Integer.class}
                                                );
    return rc;
    //return rs.getUnicodeStream(columnIndex);
  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x, int length)
    throws SQLException
  {
    rs.updateAsciiStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x,
                                 int length)
    throws SQLException
  {
    rs.updateBinaryStream(columnIndex, x, length);
  }

  @Override
  public Reader getCharacterStream(int columnIndex)
    throws SQLException
  {
    return rs.getCharacterStream(columnIndex);
  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x, int length)
    throws SQLException
  {
    rs.updateCharacterStream(columnIndex, x, length);
  }

  @Override
  public Object getObject(int columnIndex)
    throws SQLException
  {
    return rs.getObject(columnIndex);
  }

  @Override
  public void updateObject(int columnIndex, Object x)
    throws SQLException
  {
    rs.updateObject(columnIndex, x);
  }

  @Override
  public void updateObject(int columnIndex, Object x, int scale)
    throws SQLException
  {
    rs.updateObject(columnIndex, x, scale);
  }

  @Override
  public String getCursorName()
    throws SQLException
  {
    return rs.getCursorName();
  }

  @Override
  public String getString(int columnIndex)
    throws SQLException
  {
    return rs.getString(columnIndex);
  }

  @Override
  public void updateString(int columnIndex, String x)
    throws SQLException
  {
    rs.updateString(columnIndex, x);
  }

  @Override
  public BigDecimal getBigDecimal(int columnIndex)
    throws SQLException
  {
    return rs.getBigDecimal(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(int columnIndex, int scale)
    throws SQLException
  {
    BigDecimal rc =
      (BigDecimal) Reflection.runInvokeMethod(rs, "getBigDecimal",
                                                new Object[]{columnIndex,scale},
                                                new Class[]{Integer.class,Integer.class}
                                                );
    return rc;
    //return rs.getBigDecimal(columnIndex, scale);
  }

  @Override
  public void updateBigDecimal(int columnIndex, BigDecimal x)
    throws SQLException
  {
    rs.updateBigDecimal(columnIndex, x);
  }

  @Override
  public URL getURL(int columnIndex)
    throws SQLException
  {
    return rs.getURL(columnIndex);
  }

  @Override
  public Array getArray(int columnIndex)
    throws SQLException
  {
    return rs.getArray(columnIndex);
  }

  @Override
  public void updateArray(int columnIndex, Array x)
    throws SQLException
  {
    rs.updateArray(columnIndex, x);
  }

  @Override
  public Blob getBlob(int columnIndex)
    throws SQLException
  {
    return rs.getBlob(columnIndex);
  }

  @Override
  public void updateBlob(int columnIndex, Blob x)
    throws SQLException
  {
    rs.updateBlob(columnIndex, x);
  }

  @Override
  public Clob getClob(int columnIndex)
    throws SQLException
  {
    return rs.getClob(columnIndex);
  }

  @Override
  public void updateClob(int columnIndex, Clob x)
    throws SQLException
  {
    rs.updateClob(columnIndex, x);
  }

  @Override
  public Date getDate(int columnIndex)
    throws SQLException
  {
    return rs.getDate(columnIndex);
  }

  @Override
  public void updateDate(int columnIndex, Date x)
    throws SQLException
  {
    rs.updateDate(columnIndex, x);
  }

  @Override
  public Ref getRef(int columnIndex)
    throws SQLException
  {
    return rs.getRef(columnIndex);
  }

  @Override
  public void updateRef(int columnIndex, Ref x)
    throws SQLException
  {
    rs.updateRef(columnIndex, x);
  }

  @Override
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    return rs.getMetaData();
  }

  @Override
  public SQLWarning getWarnings()
    throws SQLException
  {
    return rs.getWarnings();
  }

  @Override
  public Statement getStatement()
    throws SQLException
  {
    return rs.getStatement();
  }

  @Override
  public Time getTime(int columnIndex)
    throws SQLException
  {
    return rs.getTime(columnIndex);
  }

  @Override
  public void updateTime(int columnIndex, Time x)
    throws SQLException
  {
    rs.updateTime(columnIndex, x);
  }

  @Override
  public Timestamp getTimestamp(int columnIndex)
    throws SQLException
  {
    return rs.getTimestamp(columnIndex);
  }

  @Override
  public void updateTimestamp(int columnIndex, Timestamp x)
    throws SQLException
  {
    rs.updateTimestamp(columnIndex, x);
  }

  @Override
  public Object getObject(int columnIndex, Map<String,Class<?>> map)
    throws SQLException
  {
    return rs.getObject(columnIndex, map);
  }

  @Override
  public Date getDate(int columnIndex, Calendar cal)
    throws SQLException
  {
    return rs.getDate(columnIndex, cal);
  }

  @Override
  public Time getTime(int columnIndex, Calendar cal)
    throws SQLException
  {
    return rs.getTime(columnIndex, cal);
  }

  @Override
  public Timestamp getTimestamp(int columnIndex, Calendar cal)
    throws SQLException
  {
    return rs.getTimestamp(columnIndex, cal);
  }


  // ---------------------------------------------

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x,
                                long length)
    throws SQLException
  {
    rs.updateAsciiStream(columnIndex, x, length);
  }

  @Override
  public void updateAsciiStream(String columnLabel, InputStream x,
                                long length)
    throws SQLException
  {
    rs.updateAsciiStream(columnLabel, x, length);
  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x)
    throws SQLException
  {
    rs.updateAsciiStream(columnIndex, x);
  }

  @Override
  public void updateAsciiStream(String columnLabel, InputStream x)
    throws SQLException
  {
    rs.updateAsciiStream(columnLabel, x);
  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x,
                                 long length)
    throws SQLException
  {
    rs.updateBinaryStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(String columnLabel, InputStream x,
                                 long length)
    throws SQLException
  {
    rs.updateBinaryStream(columnLabel, x, length);
  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x)
    throws SQLException
  {
    rs.updateBinaryStream(columnIndex, x);
  }

  @Override
  public void updateBinaryStream(String columnLabel, InputStream x)
    throws SQLException
  {
    rs.updateBinaryStream(columnLabel, x);
  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x, long length)
    throws SQLException
  {
    rs.updateCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateCharacterStream(String columnLabel, Reader reader,
                                    long length)
    throws SQLException
  {
    rs.updateCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x)
    throws SQLException
  {
    rs.updateCharacterStream(columnIndex, x);
  }

  @Override
  public void updateCharacterStream(String columnLabel, Reader reader)
    throws SQLException
  {
    rs.updateCharacterStream(columnLabel, reader);
  }

  @Override
  public void updateBlob(int columnIndex, InputStream inputStream,
                         long length)
    throws SQLException
  {
    rs.updateBlob(columnIndex, inputStream, length);
  }

  @Override
  public void updateBlob(String columnLabel, InputStream inputStream,
                         long length)
    throws SQLException
  {
    rs.updateBlob(columnLabel, inputStream, length);
  }

  @Override
  public void updateBlob(int columnIndex, InputStream inputStream)
    throws SQLException
  {
    rs.updateBlob(columnIndex, inputStream);
  }

  @Override
  public void updateBlob(String columnLabel, InputStream inputStream)
    throws SQLException
  {
    rs.updateBlob(columnLabel, inputStream);
  }

  @Override
  public void updateClob(int columnIndex, Reader reader, long length)
    throws SQLException
  {
    rs.updateClob(columnIndex, reader, length);
  }

  @Override
  public void updateClob(String columnLabel, Reader reader, long length)
    throws SQLException
  {
    rs.updateClob(columnLabel, reader, length);
  }

  @Override
  public void updateClob(int columnIndex, Reader reader)
    throws SQLException
  {
    rs.updateClob(columnIndex, reader);
  }

  @Override
  public void updateClob(String columnLabel, Reader reader)
    throws SQLException
  {
    rs.updateClob(columnLabel, reader);
  }

  @Override
  public RowId getRowId(int columnIndex)
  {
    return null;
  }

  @Override
  public RowId getRowId(String columnLabel)
    throws SQLException
  {
    return rs.getRowId(columnLabel);
  }

  @Override
  public void updateRowId(int columnIndex, RowId x)
    throws SQLException
  {
    rs.updateRowId(columnIndex, x);
  }

  @Override
  public void updateRowId(String columnLabel, RowId x)
    throws SQLException
  {
    rs.updateRowId(columnLabel, x);
  }

  @Override
  public int getHoldability()
    throws SQLException
  {
    return rs.getHoldability();
  }

  @Override
  public boolean isClosed()
    throws SQLException
  {
    return rs.isClosed();
  }

  @Override
  public void updateNString(int columnIndex, String nString)
    throws SQLException
  {
    rs.updateNString(columnIndex, nString);
  }

  @Override
  public void updateNString(String columnLabel, String nString)
    throws SQLException
  {
    rs.updateNString(columnLabel, nString);
  }

  @Override
  public void updateNClob(int columnIndex, NClob nClob)
    throws SQLException
  {
    rs.updateNClob(columnIndex, nClob);
  }

  @Override
  public void updateNClob(String columnLabel, NClob nClob)
    throws SQLException
  {
    rs.updateNClob(columnLabel, nClob);
  }

  @Override
  public void updateNClob(int columnIndex, Reader reader, long length)
    throws SQLException
  {
    rs.updateNClob(columnIndex, reader, length);
  }

  @Override
  public void updateNClob(String columnLabel, Reader reader, long length)
    throws SQLException
  {
    rs.updateNClob(columnLabel, reader, length);
  }

  @Override
  public void updateNClob(int columnIndex, Reader reader)
    throws SQLException
  {
    rs.updateNClob(columnIndex, reader);
  }

  @Override
  public void updateNClob(String columnLabel, Reader reader)
    throws SQLException
  {
    rs.updateNClob(columnLabel, reader);
  }

  @Override
  public NClob getNClob(int columnIndex)
    throws SQLException
  {
    return rs.getNClob(columnIndex);
  }

  @Override
  public NClob getNClob(String columnLabel)
    throws SQLException
  {
    return rs.getNClob(columnLabel);
  }

  @Override
  public SQLXML getSQLXML(int columnIndex)
    throws SQLException
  {
    return rs.getSQLXML(columnIndex);
  }

  @Override
  public SQLXML getSQLXML(String columnLabel)
    throws SQLException
  {
    return rs.getSQLXML(columnLabel);
  }

  @Override
  public void updateSQLXML(int columnIndex, SQLXML xmlObject)
    throws SQLException
  {
    rs.updateSQLXML(columnIndex, xmlObject);
  }

  @Override
  public void updateSQLXML(String columnLabel, SQLXML xmlObject)
    throws SQLException
  {
    rs.updateSQLXML(columnLabel, xmlObject);
  }

  @Override
  public String getNString(int columnIndex)
    throws SQLException
  {
    return rs.getNString(columnIndex);
  }

  @Override
  public String getNString(String columnLabel)
    throws SQLException
  {
    return rs.getNString(columnLabel);
  }

  @Override
  public Reader getNCharacterStream(int columnIndex)
    throws SQLException
  {
    return rs.getNCharacterStream(columnIndex);
  }

  @Override
  public Reader getNCharacterStream(String columnLabel)
    throws SQLException
  {
    return rs.getNCharacterStream(columnLabel);
  }

  @Override
  public void updateNCharacterStream(int columnIndex, Reader x,
                                     long length)
    throws SQLException
  {
    rs.updateNCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateNCharacterStream(String columnLabel, Reader reader,
                                     long length)
    throws SQLException
  {
    rs.updateNCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateNCharacterStream(int columnIndex, Reader x)
    throws SQLException
  {
    rs.updateNCharacterStream(columnIndex, x);
  }

  @Override
  public void updateNCharacterStream(String columnLabel, Reader reader)
    throws SQLException
  {
    rs.updateNCharacterStream(columnLabel, reader);
  }

  @Override
  public <T> T unwrap(Class<T> iface)
    throws SQLException
  {
    return rs.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface)
    throws SQLException
  {
    return rs.isWrapperFor(iface);
  }
}

