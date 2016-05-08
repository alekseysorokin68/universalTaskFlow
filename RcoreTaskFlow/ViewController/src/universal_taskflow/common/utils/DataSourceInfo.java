package universal_taskflow.common.utils;

import com.rcore.model.dynamic_list.ConnectionInterface;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * Описатель источника данных
 */
public class DataSourceInfo implements Serializable,ConnectionInterface
{
  @SuppressWarnings("compatibility:-8761888949583884222")
  private static final long serialVersionUID = 1L;
  private String title = null;
  private String jndiName = null;
  //---------------------

  public DataSourceInfo(String title, String jndiName)
  {
    super();
    this.title = title;
    this.jndiName = jndiName;
  }
  //---------------------
  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getTitle()
  {
    return title;
  }

  public void setJndiName(String jndiName)
  {
    this.jndiName = jndiName;
  }

  public String getJndiName()
  {
    return jndiName;
  }
  //=========================================
  @Override 
  public Connection getConnection()
  {
    Connection rc = null;
    try
    {
      rc = DbUtilsImpl.getInstance().getConnection(jndiName);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }

  @Override 
  public void closeConnection(Connection connection) throws SQLException
  {
    connection.close();
  }
  //=========================
  @Override
  public boolean equals(Object obj) 
  {
    boolean rc = false;
    if (obj != null) 
    {
      if (obj instanceof DataSourceInfo) 
      {
        DataSourceInfo objInfo = (DataSourceInfo) obj;
        rc = jndiName.equals(objInfo.jndiName);
      }
    }
    return rc;
  }
  @Override 
  public int hashCode() 
  {
    return jndiName.hashCode();
  }
}
