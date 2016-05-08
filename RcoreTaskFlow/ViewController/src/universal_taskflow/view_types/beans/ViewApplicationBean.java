package universal_taskflow.view_types.beans;


import com.rcore.global.jsf.JSFUtils;
import universal_taskflow.common.beans.BaseTaskFlowBean;


public class ViewApplicationBean extends BaseTaskFlowBean
{
  private static final long serialVersionUID = 1L;

  public ViewApplicationBean()
  {
    super();
  }
  public static ViewApplicationBean getInstance() 
  {
    return (ViewApplicationBean)(JSFUtils.resolveExpression("#{applicationScope.viewApplicationBean}"));
  }
}
