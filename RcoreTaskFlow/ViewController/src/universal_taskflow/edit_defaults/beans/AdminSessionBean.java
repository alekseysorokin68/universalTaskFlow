package universal_taskflow.edit_defaults.beans;

import com.rcore.global.jsf.JSFUtils;

import universal_taskflow.common.beans.BaseAdminBean;

public class AdminSessionBean extends BaseAdminBean
{
  private static final long serialVersionUID = 1L;

  public AdminSessionBean()
  {
    super();
  }
  public static AdminSessionBean getInstance() 
  {
    return (AdminSessionBean)(JSFUtils.resolveExpression("#{sessionScope.adminSessionBean}"));
  }
}
