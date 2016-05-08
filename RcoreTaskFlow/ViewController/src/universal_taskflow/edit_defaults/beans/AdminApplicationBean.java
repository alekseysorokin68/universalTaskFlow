package universal_taskflow.edit_defaults.beans;

import com.rcore.global.jsf.JSFUtils;

import universal_taskflow.common.beans.BaseTaskFlowBean;


public class AdminApplicationBean extends BaseTaskFlowBean
{
  private static final long serialVersionUID = 1L;

  public AdminApplicationBean()
  {
    super();
  }
  public static AdminApplicationBean getInstance() 
  {
    return (AdminApplicationBean)(JSFUtils.resolveExpression("#{applicationScope.adminApplicationBean}"));
  }
}
