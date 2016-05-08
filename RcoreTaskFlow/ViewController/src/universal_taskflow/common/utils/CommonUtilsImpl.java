package universal_taskflow.common.utils;


import com.rcore.global.jsf.JSFUtils;

import java.awt.Color;

import java.io.Serializable;

import java.util.Collections;
import java.util.Map;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowContext;
import oracle.adf.controller.TaskFlowId;
import oracle.adf.controller.ViewPortContext;
import oracle.adf.share.ADFContext;

import org.apache.commons.beanutils.PropertyUtils;

import universal_taskflow.common.beans.BaseTaskFlowBean;
import universal_taskflow.common.beans.BaseViewBean;
import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.DataInfo;
import universal_taskflow.common.data.ElHtmlKey;
import universal_taskflow.common.data.ElHtmlValue;
import universal_taskflow.common.data.KeyUsers;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.UserRecord;
import universal_taskflow.common.types.DebugMode;
import universal_taskflow.common.utils.serialization.drivers.SerializationDriverBase;

import universal_taskflow.edit_defaults.beans.AdminPageFlowBean;


public class CommonUtilsImpl implements CommonUtils
{
  private static CommonUtilsImpl instance = new CommonUtilsImpl();

  private CommonUtilsImpl()
  {
    super();
  }
  public static CommonUtilsImpl getInstance() 
  {
    return instance;
  }
  //=========================================
  @Override 
  public boolean isEditDefaultsMode()
  {
    ControllerContext controllerContext = ControllerContext.getInstance();
    ViewPortContext   currentViewPort = controllerContext.getCurrentViewPort();
    TaskFlowContext   taskFlowContext = currentViewPort.getTaskFlowContext();
    TaskFlowId        taskFlowId      = taskFlowContext.getTaskFlowId();
    String docName = taskFlowId.getDocumentName();
    boolean rc = ("/WEB-INF/universal_taskflow/view_types/administration/content/edit_defaults-task-flow-definition.xml".equals(docName));
    return rc;
  }

  @Override 
  public String getTaskFlowParameterId()
  {
    if (TaskFlowSystemProperties.getInstance().getDebugMode() != DebugMode.DEBUG_OUT_TASKFLOW_OUT_WEB)
    {
      return (String) JSFUtils.resolveExpression("#{pageFlowScope.id}");
    }
    else 
    {
      return "debug_taskflow_1";
    }
  }

  @Override 
  public Double getTaskFlowParameterVer()
  {
    if (TaskFlowSystemProperties.getInstance().getDebugMode() != DebugMode.DEBUG_OUT_TASKFLOW_OUT_WEB)
    {
      return (Double) JSFUtils.resolveExpression("#{pageFlowScope.ver}");
    }
    else 
    {
      return 1.11;
    }
  }

  @Override 
  public DataInfo getDataInfo()
  {
    DataInfo rc = null;
    if (!isEditDefaultsMode()) 
    {
      rc = getDataInfoFromViewHash();
    }
    else 
    {
      rc = getDataInfoFromAdminHash();
    }
    return rc;
  }

  @Override 
  public MainRecord getMainRecord()
  {
    MainRecord rc = null;
    DataInfo info = getDataInfo();
    if (info != null) 
    {
      rc = info.getMainRecord();
    }
    return rc;
  }

  @Override 
  public UserRecord getUserRecord()
  {
    UserRecord rc = null;
    DataInfo info = getDataInfo();
    if (info != null) 
    {
      rc = info.getUserRecord();
    }
    return rc;
  }

  @Override 
  public boolean isModelReadOnly()
  {
    boolean rc = getMainRecord().getTaskFlowParametersAsOnSql().getModelReadOnly();
    return rc;
  }

  @Override 
  public void refreshData()
  {
    if (!isEditDefaultsMode()) 
    {
      BaseTaskFlowBean bean = BaseTaskFlowBean.getInstance();
      bean.refreshDataImpl();
    }
    else 
    {
      AdminPageFlowBean bean = AdminPageFlowBean.getInstance();
      bean.setDataInfo(null);
    }
  }

  @Override 
  public String getUserName()
  {
    String rc = ADFContext.getCurrent().getSecurityContext().getUserName();
    return rc;
  }

  @Override 
  public boolean isTaskFlowActivated()
  {
    // TODO
    return false;
  }

  @Override 
  public void setTaskFlowActivated(boolean activated)
  {
    // TODO
  }

  @Override 
  public void setTextNotActivated(String textNotActivated)
  {
    // TODO
  }

  @Override 
  public String getTextNotActivated()
  {
    // TODO
    return null;
  }

  @Override 
  public Map<String, String> getTaskFlowParametersFromRequest()
  {
    // TODO
    return Collections.emptyMap();
  }

  @Override 
  public void update()
  {
    DataInfo info = getDataInfo();
    if (info != null) 
    {
      writeMainRecord(info.getMainRecord());
      UserRecord record = info.getUserRecord();
      if (record != null) 
      {
        writeUserRecord(KeyUsers.getCurrentKey(),record);
      }
    } // if
  }

  @Override 
  public void updateUserData()
  {
    DataInfo info = getDataInfo();
    if (info != null) {
      UserRecord record = info.getUserRecord();
      if (record != null) 
      {
        writeUserRecord(KeyUsers.getCurrentKey(),record);
      }
    } // if
  }

//  @Override 
//  public boolean isFirstTime()
//  {
//    return BasePageFlowBean.getInstance().isFirstTime();
//  }

  @Override 
  public void saveSqlParametersToUserData(Map<String, Serializable> params)
  {
    UserRecord ur = getUserRecord();
    if (ur != null)
    {
      ur.saveSqlParameters(params);
      updateUserData();
    }
  }
  //---------------------------------------
  private DataInfo getDataInfoFromViewHash()
  {
    BaseTaskFlowBean bean = BaseTaskFlowBean.getInstance();
    DataInfo info = bean.getDataInfoFromViewHash();
    if (info == null) 
    {
      String id = getTaskFlowParameterId();
      // Читаем mainRecord
      MainRecord mainRecord = null;
      try
      {
        mainRecord = readCurrentMainRecordFromRepository();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      info = new DataInfo(mainRecord);
      bean.setDataInfoToCashView(id, info);
      UserRecord userRecord = null;
      try
      {
        userRecord = readCurrentUserRecordFromRepository();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      info.getTaskFlowUsersMap().put(KeyUsers.getCurrentKey(),userRecord);
    }
    return info;
  }

  private DataInfo getDataInfoFromAdminHash()
  {
    AdminPageFlowBean bean = AdminPageFlowBean.getInstance();
    DataInfo info = bean.getDataInfo();
    if (info == null) 
    {
      // Читаем mainRecord
      MainRecord mainRecord = null;
      try
      {
        mainRecord = readCurrentMainRecordFromRepository();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      info = new DataInfo(mainRecord);
      UserRecord userRecord = null;
      try
      {
        userRecord = readCurrentUserRecordFromRepository();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      info.getTaskFlowUsersMap().put(KeyUsers.getCurrentKey(),userRecord);
      bean.setDataInfo(info);
    }
    return info;
  }

  private MainRecord readCurrentMainRecordFromRepository() throws Exception
  {
    return SerializationDriverBase.getInstance().fullReadMainRecordFromRepositories();
//    MainRecord rc = null;
//    SerializationDriverBase driver = SerializationDriverBase.getInstance();
//    rc = driver.readServerMainRecord();
//    if (rc == null) 
//    {
//      rc = driver.readLocalMainRecord();
//    }
//    if (rc == null) 
//    {
//      rc = new MainRecord(getTaskFlowParameterId());
//      writeMainRecord(rc);
//    }
//    return rc;
  }

  private UserRecord readCurrentUserRecordFromRepository() throws Exception
  {
    SerializationDriverBase driver = SerializationDriverBase.getInstance();
    UserRecord  rc = driver.readUserRecord();
    rc = new UserRecord();
    writeUserRecord(KeyUsers.getCurrentKey(), rc);
    return rc;
  }

  private void writeMainRecord(MainRecord mainRecord)
  {
    try
    {
      SerializationDriverBase.getInstance().writeMainRecord(mainRecord);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void writeUserRecord(KeyUsers key, UserRecord record)
  {
    try
    {
      SerializationDriverBase.getInstance().writeUserRecord(key,record);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  //==========================================================
  public void registerError(Exception e)
  {
    registerError(e.getMessage());
  }
  public void registerError(String mes)
  {
    BaseViewBean.getInstance().registerErrorMessage(mes);
  }
  //==================================
  public Serializable getResolvedValue(Object obj, String propertyName) 
  {
    return getResolvedValue(obj, obj.getClass(), propertyName, null);
  }
  
  public Serializable getResolvedValue(Object obj, String propertyName, Serializable objectId) 
  {
    return getResolvedValue(obj, obj.getClass(), propertyName, objectId);
  }
                                                                         
//  public Serializable getResolvedValue(Object obj, Class cl, String propertyName, Serializable objectId) 
//  {
//    Serializable rc = null;
//    ElHtmlKey key = new ElHtmlKey(cl.getName(), propertyName, objectId);
//    ElHtmlValue value = getMainRecord().getElHtmlValues().get(key);
//    if (value == null || isEditDefaultsMode()) 
//    {
//      try
//      {
//        rc = (Serializable) PropertyUtils.getProperty(obj, propertyName);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//      }
//    }
//    else 
//    {
//      rc = value.getValue();
//    }
//    //-------
//    return rc;
//  }
  
  public Serializable getResolvedValue(Object obj, Class cl, String propertyName, Serializable objectId) 
  {
    Serializable rc = null;
    try
    {
      ElHtmlKey key = new ElHtmlKey(cl.getName(), propertyName, objectId);
      ElHtmlValue value = getMainRecord().getElHtmlValues().get(key);
      if (value == null) 
      {
        rc = (Serializable) PropertyUtils.getProperty(obj, propertyName);          
      }
      else 
      {
        rc = (Serializable) value.getResolvedValue();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }  
    //-------
    return rc;
  }
  
  public Serializable getResolvedValue(Object obj, 
                                       Class cl, 
                                       String propertyName, 
                                       Serializable objectId,
                                       Map<String,Object> row
                                       ) 
  {
    Serializable rc = null;
    try
    {
      ElHtmlKey key = new ElHtmlKey(cl.getName(), propertyName, objectId);
      ElHtmlValue value = getMainRecord().getElHtmlValues().get(key);
      if (value == null) 
      {
        rc = (Serializable) PropertyUtils.getProperty(obj, propertyName);          
      }
      else 
      {
        rc = (Serializable) value.getResolvedValue(row);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }  
    //-------
    return rc;
  }
  
  /**
   * Солдатов
   * Преобразует цвет в строку шестнадцатиричного представление RGB.
   *
   * @param c
   * @return
   */
  public static String color2HexString(Color c)
  {
    if (c == null)
    {
      return "FFFFFF";
    }
    int r = c.getRed();
    int g = c.getGreen();
    int b = c.getBlue();
    String rh =
      r < 16? "0" + Integer.toHexString(r): Integer.toHexString(r);
    ;
    String gh =
      g < 16? "0" + Integer.toHexString(g): Integer.toHexString(g);
    ;
    String bh =
      b < 16? "0" + Integer.toHexString(b): Integer.toHexString(b);
    ;

    return rh + gh + bh;
  }
  
  //====================================
  public static void main(String[] args)
  {
    //getInstance().getCurrentMainRecordFromRepository();
  }
}
