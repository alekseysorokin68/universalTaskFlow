package universal_taskflow.common.beans;


import com.rcore.global.adf.AdfBaseBean;
import com.rcore.global.cash.CashManagerSimpleImpl;
import com.rcore.global.jsf.JSFUtils;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import oracle.adf.share.ADFContext;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.DataInfo;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.UserRecord;
import universal_taskflow.common.utils.CommonUtilsImpl;
import universal_taskflow.common.utils.serialization.drivers.SerializationDriverBase;

import universal_taskflow.edit_defaults.beans.AdminApplicationBean;

import universal_taskflow.view_types.beans.ViewApplicationBean;


/**
 * Базовый бин для всех бинов
 */
public class BaseTaskFlowBean extends AdfBaseBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  public BaseTaskFlowBean()
  {
    super();
  }

  public boolean isEditDefaultsMode()
  {
    return CommonUtilsImpl.getInstance().isEditDefaultsMode();
  }

  public String getTaskFlowParameterId()
  {
    return CommonUtilsImpl.getInstance().getTaskFlowParameterId();
  }

  public Double getTaskFlowParameterVer()
  {
    return CommonUtilsImpl.getInstance().getTaskFlowParameterVer();
  }

  public DataInfo getDataInfo()
  {
    return CommonUtilsImpl.getInstance().getDataInfo();
  }

  public MainRecord getMainRecord()
  {
    return CommonUtilsImpl.getInstance().getMainRecord();
  }

  public UserRecord getUserRecord()
  {
    return CommonUtilsImpl.getInstance().getUserRecord();
  }

  public boolean isModelReadOnly()
  {
    return CommonUtilsImpl.getInstance().isModelReadOnly();
  }

  public void refreshData()
  {
    CommonUtilsImpl.getInstance().refreshData();
  }

  public String getUserName()
  {
    return CommonUtilsImpl.getInstance().getUserName();
  }

  public boolean isTaskFlowActivated()
  {
    return CommonUtilsImpl.getInstance().isTaskFlowActivated();
  }

  public void setTaskFlowActivated(boolean activated)
  {
    CommonUtilsImpl.getInstance().setTaskFlowActivated(activated);
  }

  public void setTextNotActivated(String textNotActivated)
  {
    CommonUtilsImpl.getInstance().setTextNotActivated(textNotActivated);
  }

  public String getTextNotActivated()
  {
    return CommonUtilsImpl.getInstance().getTextNotActivated();
  }

  public Map<String, String> getTaskFlowParametersFromRequest()
  {
    return CommonUtilsImpl.getInstance().getTaskFlowParametersFromRequest();
  }

  public void update()
  {
    CommonUtilsImpl.getInstance().update();
  }

  public void updateUserData()
  {
    CommonUtilsImpl.getInstance().updateUserData();
  }

//  public boolean isFirstTime()
//  {
//    return CommonUtilsImpl.getInstance().isFirstTime();
//  }

  public void saveSqlParametersToUserData(Map<String, Serializable> params)
  {
    CommonUtilsImpl.getInstance().saveSqlParametersToUserData(params);
  }
  
  public TaskFlowSystemProperties getSystemProperties() 
  {
    return TaskFlowSystemProperties.getInstance();
  }
  //------------------------------------------------
  public static String getPortalServerName()
  {
    String rc = null;
    Object request =  FacesContext.getCurrentInstance().getExternalContext().getRequest();
    if (request instanceof HttpServletRequest)
    {
      HttpServletRequest req = (HttpServletRequest) request;
      rc = req.getServerName();
    }
    else
    {
      System.err.println("@error getPortalServerName ");
    }
    return rc;
  }
  public static String getScheme()
  {
    String rc = null;
    Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
    if (request instanceof ServletRequest)
    {
      ServletRequest req = (ServletRequest) request;
      rc = req.getScheme();
    }
    return rc;
  }
  
  public static String getPortalPort()
  {
    String rc = null;
    Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
    if (request instanceof HttpServletRequest)
    {
      HttpServletRequest req = (HttpServletRequest) request;
      rc = req.getServerPort() + "";
    }
    return rc;
  }
  
  public static String getPortalContext()
  {
    //return "/cb-portal";
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    return request.getContextPath();
  }
  
  public List<String> getIdList() 
  {
    return SerializationDriverBase.getInstance().getIdList();
  }
  
  public List<MainRecord> getMainRecordList() 
  {
    return SerializationDriverBase.getInstance().getMainRecordList();
  }
  //======================================
  private CashManagerSimpleImpl<String,DataInfo> viewHash = 
    new CashManagerSimpleImpl<String,DataInfo>(TaskFlowSystemProperties.getInstance().getDefaultLifeTimeInCashView());
  //---------------------------------------
  public void refreshDataImpl() 
  {
    viewHash.remove(CommonUtilsImpl.getInstance().getTaskFlowParameterId());
  }
  public DataInfo getDataInfoFromViewHash() 
  {
    return getDataInfoFromViewHash(CommonUtilsImpl.getInstance().getTaskFlowParameterId());
  }
  public DataInfo getDataInfoFromViewHash(String key) 
  {
    return viewHash.get(key);
  }
  public void setDataInfoToCashView(String key, DataInfo info) 
  {
    viewHash.put(key,info);
  }
  
  public static BaseTaskFlowBean getInstance() 
  {
    BaseTaskFlowBean rc = null;
    if (CommonUtilsImpl.getInstance().isEditDefaultsMode()) 
    {
      rc = AdminApplicationBean.getInstance();
    }
    else 
    {
      Map<String,Object> map = ADFContext.getCurrent().getApplicationScope();
      rc = (BaseTaskFlowBean) map.get("viewApplicationBean");
      if (rc == null) 
      {
        rc = new ViewApplicationBean();
        map.put("viewApplicationBean", rc);
      }
    }
    return rc;
  }
  
  public Map<String, Object> getPageFlowScope() 
  {
    return (Map<String, Object>) (JSFUtils.resolveExpression("#{pageFlowScope}"));
  }
}
