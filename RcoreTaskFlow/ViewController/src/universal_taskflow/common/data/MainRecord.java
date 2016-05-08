package universal_taskflow.common.data;


import com.rcore.global.jsf.JSFUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableAdfParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TaskFlowParametersBaseOnSql;
import universal_taskflow.common.types.TaskFlowTypeImpl;
import universal_taskflow.common.utils.CommonUtilsImpl;


/**
 * Главная хранимая запись - taskFlow
 * Ключом является имя файла (без расширения (xml)).
 */
@XStreamAlias("MainRecord")
public class MainRecord implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final String ATT_FOR_LIB_VER      = "versionLib";
  private static final String ATT_FOR_TASKFLOW_VER = "versionTaskFlow";
  //---------------------------------------------
  private String id = null; // Первичный ключ (имя файла, для файловой системы)
  private String header = null;
  private Boolean headerExists = true;
  private String descriptionTaskFlow = null;
  private String adminComment = null;
  private SecurityData securityData = null;
  //private String taskFlowTitle = null;
  private boolean activated = false;
  private String  textNotActivated = "Эта область в разработке";
  private boolean saveAllTaskFlowTypes = true;
  //----
  private String user = null;
  private Date   date = null;
  private TaskFlowTypeImpl taskFlowType = TaskFlowTypeImpl.TABLE_ADF;
  //private TaskFlowParametersBase taskFlowParameters = new TableAdfParameters();
  private Map<TaskFlowTypeImpl,TaskFlowParametersBase> taskFlowParametersTable =
      new HashMap<TaskFlowTypeImpl,TaskFlowParametersBase>();
  private Map<ElHtmlKey,ElHtmlValue> elHtmlValues = new HashMap<ElHtmlKey,ElHtmlValue>();
  //=========================================================
  private transient boolean currentLibVerChanged = false;
  private transient boolean currentTaskFlowVerChanged = false;
  private transient boolean newInstance = true;
  //-----------------------------------------------------------
  private transient boolean selected = false;
  //-----------------------------------------------------------
  public MainRecord(String id)
  {
    super();
    this.id = id;
  }
  //----
  public String clearHistoryTypes()
  {
    if (taskFlowParametersTable.size() > 0)
    {
      if (taskFlowType != null)
      {
        TaskFlowParametersBase value = taskFlowParametersTable.get(taskFlowType);
        taskFlowParametersTable.clear();
        taskFlowParametersTable.put(taskFlowType,value);
      }
    }
    return null;
  }
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
    writer.addAttribute(ATT_FOR_LIB_VER, TaskFlowSystemProperties.getInstance().getVersionLib());
    String ver = getTaskFlowVer();
    writer.addAttribute(ATT_FOR_TASKFLOW_VER, ver);
    user = ADFContext.getCurrent().getSecurityContext().getUserName();
    date = new Date(System.currentTimeMillis());
    //--- Очистка taskFlowParametersTable ----
    if (!saveAllTaskFlowTypes)
    {
      clearHistoryTypes();
    }
    //-----------------------------------------
    //CustomWriter wr = (CustomWriter) writer;
    //wr.setFieldsAsCDATA("header","descriptionTaskFlow","adminComment","textNotActivated");
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
    if (FacesContext.getCurrentInstance() == null)
    {
      return;
    }
    String currentLibVer = TaskFlowSystemProperties.getInstance().getVersionLib();
    String libVerFromXml = attributes.get(ATT_FOR_LIB_VER);
    if (!currentLibVer.equals(libVerFromXml))
    {
      currentLibVerChanged = true;
    }

    String currentTaskFlowVer = getTaskFlowVer();
    String taskFlowVerFromXml = attributes.get(ATT_FOR_LIB_VER);
    if (!currentTaskFlowVer.equals(taskFlowVerFromXml))
    {
      currentTaskFlowVerChanged = true;
    }
    newInstance = false;
    selected = false;
  }
  //----
  public void setAdminComment(String adminComment)
  {
    this.adminComment = adminComment;
  }

  public String getAdminComment()
  {
    return adminComment;
  }

  public void setDescriptionTaskFlow(String descriptionForAis)
  {
    this.descriptionTaskFlow = descriptionForAis;
  }

  public String getDescriptionTaskFlow()
  {
    return descriptionTaskFlow;
  }

  public void setSecurityData(SecurityData securityData)
  {
    this.securityData = securityData;
  }

  public SecurityData getSecurityData()
  {
    return securityData;
  }

  public void setActivated(boolean activated)
  {
    this.activated = activated;
  }

  public boolean isActivated()
  {
    return activated;
  }

  public void setTextNotActivated(String textNotActivated)
  {
    this.textNotActivated = textNotActivated;
  }

  public String getTextNotActivated()
  {
    return textNotActivated;
  }

  public String getTaskFlowId()
  {
    return (String) JSFUtils.resolveExpression("#{pageFlowScope.id}");
  }

  public String getTaskFlowVer()
  {
    String rc = null;
    rc = (String) JSFUtils.resolveExpression("#{pageFlowScope.ver}");
    return rc;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  public String getUser()
  {
    return user;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public Date getDate()
  {
    return date;
  }

  public boolean isCurrentLibVerChanged()
  {
    return currentLibVerChanged;
  }

  public boolean isCurrentTaskFlowVerChanged()
  {
    return currentTaskFlowVerChanged;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getId()
  {
    return id;
  }

  public boolean isNewInstance()
  {
    return newInstance;
  }

  public void setTaskFlowType(TaskFlowTypeImpl taskFlowType)
  {
    this.taskFlowType = taskFlowType;
  }

  public TaskFlowTypeImpl getTaskFlowType()
  {
    return taskFlowType;
  }

//  public void setTaskFlowParameters(TaskFlowParametersBase taskFlowParameters)
//  {
//    this.taskFlowParameters = taskFlowParameters;
//  }

  public <V extends TaskFlowParametersBase> V getTaskFlowParameters(Class<V> cl)
    throws Exception
  {
    TaskFlowParametersBase rc = getTaskFlowParameters();
    return (V)rc;
  }

  public TaskFlowParametersBase getTaskFlowParameters()
    throws Exception
  {
    TaskFlowParametersBase rc = null;
    if (taskFlowType != null)
    {
      rc = taskFlowParametersTable.get(taskFlowType);
      if (rc == null)
      {
        rc = taskFlowType.getViewTypeParameters().newInstance();
        taskFlowParametersTable.put(taskFlowType,rc);
      }
    }
    else
    {
      rc = new TableAdfParameters();
      taskFlowParametersTable.put(TaskFlowTypeImpl.TABLE_ADF,rc);
    }
    return rc;
  }
  //  --- get...As... ----
  public TaskFlowParametersBaseOnSql getTaskFlowParametersAsOnSql()
  {
    TaskFlowParametersBaseOnSql rc = null;
    try
    {
      rc = (TaskFlowParametersBaseOnSql)getTaskFlowParameters();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }

  public TableAdfParameters getTaskFlowParametersAsTableAdfParameters()
    throws Exception
  {
    return (TableAdfParameters)getTaskFlowParameters();
  }

  public void setHeader(String header)
  {
    this.header = header;
  }

  public String getHeader()
  {
    return header;
  }

  public String getResolvedHeader()
  {
    String rc = (String)( CommonUtilsImpl.getInstance().getResolvedValue(this, "header") );
    return rc;
  }

  public Map<ElHtmlKey, ElHtmlValue> getElHtmlValues()
  {
    return elHtmlValues;
  }
  //===========================
  public void setSelected(boolean selected)
  {
    this.selected = selected;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSaveAllTaskFlowTypes(boolean saveAllTaskFlowTypes)
  {
    this.saveAllTaskFlowTypes = saveAllTaskFlowTypes;
  }

  public boolean isSaveAllTaskFlowTypes()
  {
    return saveAllTaskFlowTypes;
  }

  public void setHeaderExists(Boolean headerExists)
  {
    this.headerExists = headerExists;
  }

  public Boolean getHeaderExists()
  {
    if (headerExists == null)
    {
      headerExists = true;
    }
    return headerExists;
  }

  public Boolean getResolvedHeaderExists()
  {
    Object rc = CommonUtilsImpl.getInstance().getResolvedValue(this, "headerExists");
    if (rc == null) rc = true;
    if (rc instanceof String) 
    {
      rc = Boolean.valueOf((String) rc);
    }
    return (Boolean) rc;
  }
}
