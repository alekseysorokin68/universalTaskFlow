package universal_taskflow.common.data;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import javax.servlet.ServletRequest;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.types.DebugMode;
import universal_taskflow.common.utils.CommonUtilsImpl;

/**
 * Класс - идентификатор пользователя
 */
public class KeyUsers implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String userName = null;
  private String ip = null;
  public KeyUsers(String userName, String ip)
  {
    super();
    this.userName = userName;
    this.ip = ip;
  }
  public static KeyUsers getCurrentKey()
  {
    if (TaskFlowSystemProperties.getInstance().getDebugMode() != DebugMode.DEBUG_OUT_TASKFLOW_OUT_WEB) 
    {
      ServletRequest req = (ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
      return new KeyUsers(
          CommonUtilsImpl.getInstance().getUserName(),
          req.getRemoteAddr());  
    }
    else 
    {
      return new KeyUsers("debug_user","debug_ip");
    }
    
  }

  public String getUserName()
  {
    return userName;
  }

  public String getIp()
  {
    return ip;
  }
  //------------------
  @Override 
  public String toString() 
  {
    return (new StringBuilder(userName).append(";").append(ip)).toString();
  }
  @Override
  public boolean equals(Object obj)
  {
    boolean rc = false;
    if (obj != null && (obj instanceof KeyUsers)) 
    {
      KeyUsers oKey = (KeyUsers) obj;
      rc = toString().equals(oKey.toString());
    }
    return rc;
  }
  @Override 
  public int hashCode() 
  {
    return toString().hashCode();
  }
}
