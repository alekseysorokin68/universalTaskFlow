package universal_taskflow.common.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@XStreamAlias("UserRecord")
public class UserRecord implements Serializable
{
  private static final long serialVersionUID = 1L;
  private transient String login = null;
  private transient String taskFlowId = null;
  //private UserData userData = null;
  private String userComment = null;
  private Date lastModification = null;
  private Map<String, Serializable> sqlParameterCurrentValues = new HashMap<String, Serializable>();
  //-----------------------------------------
  public void saveSqlParameters(Map<String, Serializable> params)
  {
    sqlParameterCurrentValues.putAll(params);
  }
  public void setLogin(String login)
  {
    this.login = login;
  }

  public String getLogin()
  {
    return login;
  }

  public void setTaskFlowId(String taskFlowId)
  {
    this.taskFlowId = taskFlowId;
  }

  public String getTaskFlowId()
  {
    return taskFlowId;
  }

  public void setUserComment(String userComment)
  {
    this.userComment = userComment;
  }

  public String getUserComment()
  {
    return userComment;
  }

  public void setLastModification(Date lastModification)
  {
    this.lastModification = lastModification;
  }

  public Date getLastModification()
  {
    return lastModification;
  }

  public Serializable getSqlParameterCurrentValue(String paramName)
  {
    if (sqlParameterCurrentValues == null)
    {
      return null;
    }
    return sqlParameterCurrentValues.get(paramName);
  }
}
