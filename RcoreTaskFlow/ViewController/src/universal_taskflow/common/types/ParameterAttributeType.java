package universal_taskflow.common.types;

import java.sql.Types;


/**
 * Типы полей запроса
 */
public enum ParameterAttributeType
{
  BIT(Types.BIT),
  TINYINT(Types.TINYINT),
  SMALLINT(Types.SMALLINT),
  INTEGER(Types.INTEGER),
  BIGINT(Types.BIGINT),
  FLOAT(Types.FLOAT),
  REAL(Types.REAL),
  DOUBLE(Types.DOUBLE),
  NUMERIC(Types.NUMERIC),
  DECIMAL(Types.DECIMAL),
  CHAR(Types.CHAR),
  VARCHAR(Types.VARCHAR),
  LONGVARCHAR(Types.LONGNVARCHAR),
  DATE(Types.DATE),
  TIME(Types.TIME),
  TIMESTAMP(Types.TIMESTAMP),
  ARRAY(Types.ARRAY),
  BOOLEAN(Types.BOOLEAN),
  ROWID(Types.ROWID);
  private static final long serialVersionUID = 1L;

  private int sqlType = Types.VARCHAR;

  private ParameterAttributeType(int sqlType)
  {
    this.sqlType = sqlType;
  }

  /**
   * @return
   */
  public int getSqlType()
  {
    return sqlType;
  }
  
  public String getName() 
  {
    return name();
  }
}
