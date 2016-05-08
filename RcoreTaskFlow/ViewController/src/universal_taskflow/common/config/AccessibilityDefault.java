package universal_taskflow.common.config;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;


public class AccessibilityDefault implements Accessibility
{
  private static final long serialVersionUID = 1L;

  public AccessibilityDefault()
  {
    super();
  }

  public boolean isAccessToAdminInterface()
  {
    SecurityContext context = ADFContext.getCurrent().getSecurityContext();
    String userName = context.getUserName();
    boolean rc = 
      "anonymous".equals(userName.toLowerCase()) ||
      context.isUserInRole("Administrator") ||
      context.isUserInRole("aib") ||
      context.isUserInRole("AIB") 
    ;
    return rc;  
  }
}
