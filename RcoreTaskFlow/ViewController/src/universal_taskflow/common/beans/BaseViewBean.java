package universal_taskflow.common.beans;


import com.rcore.global.adf.AdfBaseBean;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.nav.RichGoImageLink;

import org.apache.myfaces.trinidad.event.PollEvent;

import universal_taskflow.common.config.Accessibility;
import universal_taskflow.common.config.AccessibilityDefault;
import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TaskFlowParametersBaseOnSql;
import universal_taskflow.common.types.ContainerType;
import universal_taskflow.common.types.TaskFlowTypeImpl;
import universal_taskflow.common.utils.CommonUtilsImpl;

import universal_taskflow.edit_defaults.beans.AdminPageFlowBean;

import universal_taskflow.view_types.beans.ViewPageFlowBean;


/**
 * Базовый бин для View
 */
public class BaseViewBean extends BaseTaskFlowBean
{
  private static final long serialVersionUID = 1L;
  private Set<String> errorMessages = new HashSet<String>();
  //---
  private transient RichPopup popup = null;
  private transient RichGoImageLink adminLink = null;

  public BaseViewBean()
  {
    super();
  }
  
  public void initTaskFlow()
  {
    CommonUtilsImpl.getInstance().getDataInfo();
  }

//  public void setFirstTime(boolean firstTime)
//  {
//    this.firstTime = firstTime;
//  }
  
  public void registerErrorMessage(String mes) 
  {
    errorMessages.add(mes);
  }

//  @Override 
//  public boolean isFirstTime()
//  {
//    return firstTime;
//  }
  public static BaseViewBean getInstance() 
  {
    BaseViewBean rc = null;
    if (CommonUtilsImpl.getInstance().isEditDefaultsMode()) 
    {
      rc = AdminPageFlowBean.getInstance();
    }
    else 
    {
      Map<String,Object> map = ADFContext.getCurrent().getApplicationScope();
      rc = (BaseViewBean) map.get("viewPageFlowBean");
      if (rc == null) 
      {
        rc = new ViewPageFlowBean();
        map.put("viewPageFlowBean", rc);
      }
    }
    return rc;
  }

  public Set<String> getErrorMessages()
  {
    return errorMessages;
  }
  
  public TaskFlowSystemProperties getSystemProperties() 
  {
    return TaskFlowSystemProperties.getInstance();
  }
  //=========================================
  /**
   * Метод возвращающий нужную акцию
   */
  public String methodCallStartView() 
  {
    String rc = "notActive";
    MainRecord mainRecord = getMainRecord();
    if (mainRecord.isActivated()) 
    {
      TaskFlowTypeImpl type = mainRecord.getTaskFlowType();
      rc = type.getStartViewDeclarativeComponent();
    }
    return rc;
  }
  
  public String getRootDC() 
  {
    String rc = (new StringBuilder("/universal_taskflow/pages/view_types/_viewRootDC/").
      append(getRootContainer().name().toLowerCase()).
      append(".jsff")
      ).toString();
    return rc;
  }
  
  public TaskFlowParametersBaseOnSql getTaskFlowParametersAsOnSql()
  {
    return getMainRecord().getTaskFlowParametersAsOnSql();
  }
  
  private ContainerType getRootContainer() 
  {
    ContainerType type = ContainerType.DEFAULT;
    try
    {
      type = getMainRecord().getTaskFlowParameters().getContainerType();
      if (ContainerType.DEFAULT.equals(type)) 
      {
        type = getMainRecord().getTaskFlowType().getRootDefaultContainerResolver().
          resolveContainer(getMainRecord().getTaskFlowParameters());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return type;
  }
  
  public String getJsRootContextMenu()
  {
    StringBuilder rc = new StringBuilder("function _rootContextMenu(event) { \n");
    rc.append("event.cancel();\n");
    rc.append("var id = null;\n");
    rc.append("var id_comp_for_hint_align = null;\n");
    rc.append("try {\n");
    rc.append("var src = event.getSource();\n");
    rc.append("id = '").append(popup.getClientId(FacesContext.getCurrentInstance())).append("';\n");
    rc.append("id_comp_for_hint_align = '").append(adminLink.getClientId(FacesContext.getCurrentInstance())).append("';\n");
    rc.append("var popup = AdfPage.PAGE.findComponent(id);\n");
    rc.append("var hints = {};\n");
    rc.append("hints[AdfRichPopup.HINT_LAUNCH_ID] = src.getClientId();\n");
    rc.append("if (id_comp_for_hint_align) \n");
    rc.append("   hints[AdfRichPopup.HINT_ALIGN_ID]  = id_comp_for_hint_align;\n");
    rc.append("hints[AdfRichPopup.HINT_ALIGN]     = AdfRichPopup.ALIGN_AFTER_END;\n");
    rc.append("popup.show(hints);\n");
    rc.append("}\n catch(e) {alert('Ошибка : '+e.message+' ; '+id+' ; '+id_comp_for_hint_align);}\n");
    rc.append("}\n");
    //System.out.println("@rc = \n"+rc);
    return rc.toString();
  }

  public void setPopup(RichPopup popup)
  {
    this.popup = popup;
  }

  public RichPopup getPopup()
  {
    return popup;
  }

  public void setAdminLink(RichGoImageLink adminLink)
  {
    this.adminLink = adminLink;
  }

  public RichGoImageLink getAdminLink()
  {
    return adminLink;
  }
  
  public boolean isAdminLinkRendered() 
  {
    Accessibility accessibility = TaskFlowSystemProperties.getInstance().getAccessibility();
    if (accessibility == null) 
    {
      accessibility = new AccessibilityDefault();
    }
    return accessibility.isAccessToAdminInterface();
  }
  
  public String getAdminURL() 
  {
    StringBuilder rc = new StringBuilder(AdfBaseBean.getBasePathStatic());
    rc.append("faces/universal_taskflow/pages/edit_defaults/edit_defaults.jspx?id=");
    rc.append(ADFContext.getCurrent().getPageFlowScope().get("id"));
    rc.append("&ver=");
    rc.append(ADFContext.getCurrent().getPageFlowScope().get("ver"));
    return rc.toString();
  }
  
  public void pollListener(PollEvent event) 
  {
    try
    {
      refresh();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
                                              
  
  protected void refresh() throws Exception
  {
//    UIComponent comp = event.getComponent();
//    if (comp != null)
//    {
//      RichRegion region = (RichRegion) findComponentParentByClassName(comp, RichRegion.class.getName());
//      if (region != null)
//      {
//        addPartialTarget(region);
//      }
//      else
//      {
//        addPartialTarget(comp);
//      }
//    }
    refreshData();
  }
  
  public void refreshActionListener(ActionEvent event)
  {
    try
    {
      TaskFlowTypeImpl taskFlowType = getMainRecord().getTaskFlowType();
      ViewPageFlowBean bean = taskFlowType.getViewBean();
      bean.refresh();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    refreshFullPage();
  }
  
//  public void refreshActionListener(ActionEvent event)
//  {
//    TaskFlowTypeImpl taskFlowType = getMainRecord().getTaskFlowType();
//    ViewPageFlowBean bean = taskFlowType.getViewBean();
//    String categorySign = "";
//    try
//    {
//      if (bean != null)
//      {
//        if (bean instanceof BaseTableBean)
//        {
//          categorySign = bean.getCategoriesSignature();
//        }
//        bean.refresh();
//      }
//      //-------------------------------------
//      UIComponent comp = event.getComponent();
//      if (comp != null)
//      {
//        RichRegion region = (RichRegion) findComponentParentByClassName(comp, RichRegion.class.getName());
//        if (region != null)
//        {
//          addPartialTarget(region);
//        }
//        else
//        {
//          addPartialTarget(comp);
//        }
//      }
//      refreshData();
//      //---------------
//      if (bean != null)
//      {
//        if (bean instanceof BaseTableBean)
//        {
//          String newSing = bean.getCategoriesSignature();
//          if (!newSing.equals(categorySign))
//          {
//            refreshFullPage();
//          }
//        }
//      }
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
  
  public String refreshFullPage()
  {
    javaScriptEvaluate("_reloadPageSpecial();");
    return null;
  }
}
