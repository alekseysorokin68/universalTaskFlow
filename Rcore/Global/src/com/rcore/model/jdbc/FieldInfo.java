package com.rcore.model.jdbc;

import java.io.Serializable;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class FieldInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int columnIndex = 0;
  private String columnName = null;
  private int columnType = 0;
  private String columnClassName = null;
  private int    columnDisplaySize =  0;
  private String columnLabel = null;
  private String columnTypeName = null;
  private int    scale = 0;
  private int    precision = 0;
  private boolean nullable = false;
  private String tableName = null;
  private boolean readOnly = false;
  private boolean writable = false;
  private boolean autoIncrement = false;
  private boolean caseSensitive = false;
  private boolean currency = false;
  private boolean definitelyWritable = false;
  private boolean searchable = false;
  private boolean signed = false;
  //------------------------------------------------
  public FieldInfo(ResultSetMetaData meta, int index)
    throws SQLException
  {
    super();
    columnIndex = index;
    this.columnName = meta.getColumnName(index);
    this.columnType = meta.getColumnType(index);
    this.columnClassName = meta.getColumnClassName(index);
    this.columnDisplaySize = meta.getColumnDisplaySize(index);
    this.columnLabel = meta.getColumnLabel(index);
    this.columnTypeName = meta.getColumnTypeName(index);
    this.precision = meta.getPrecision(index);
    this.scale = meta.getScale(index);
    this.tableName = meta.getTableName(index);
    this.autoIncrement = meta.isAutoIncrement(index);
    this.caseSensitive = meta.isCaseSensitive(index);
    this.currency = meta.isCurrency(index);
    this.definitelyWritable = meta.isDefinitelyWritable(index);
    this.nullable = (meta.isNullable(index) == ResultSetMetaData.columnNullable);
    this.readOnly = meta.isReadOnly(index);
    this.searchable = meta.isSearchable(index);
    this.signed = meta.isSigned(index);
    this.writable = meta.isWritable(index);
  }
  //-------------------------
  public int getColumnIndex()
  {
    return columnIndex;
  }

  public String getColumnName()
  {
    return columnName;
  }

  public int getColumnType()
  {
    return columnType;
  }

  public String getColumnClassName()
  {
    return columnClassName;
  }

  public int getColumnDisplaySize()
  {
    return columnDisplaySize;
  }

  public String getColumnLabel()
  {
    return columnLabel;
  }

  public String getColumnTypeName()
  {
    return columnTypeName;
  }

  public int getScale()
  {
    return scale;
  }

  public int getPrecision()
  {
    return precision;
  }

  public boolean isNullable()
  {
    return nullable;
  }

  public String getTableName()
  {
    return tableName;
  }

  public boolean isReadOnly()
  {
    return readOnly;
  }

  public boolean isWritable()
  {
    return writable;
  }

  public boolean isAutoIncrement()
  {
    return autoIncrement;
  }

  public boolean isCaseSensitive()
  {
    return caseSensitive;
  }

  public boolean isCurrency()
  {
    return currency;
  }

  public boolean isDefinitelyWritable()
  {
    return definitelyWritable;
  }

  public boolean isSearchable()
  {
    return searchable;
  }

  public boolean isSigned()
  {
    return signed;
  }
}
