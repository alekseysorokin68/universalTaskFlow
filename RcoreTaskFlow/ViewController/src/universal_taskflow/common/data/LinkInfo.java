package universal_taskflow.common.data;


import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import universal_taskflow.common.beans.BaseTaskFlowBean;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TaskFlowParametersBaseOnSql;
import universal_taskflow.common.types.LinkType;
import universal_taskflow.common.utils.CommonUtilsImpl;


public class LinkInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final String ATTRIBUTE = "attribute";
  
  private String target = null;
  private Set<String> portletsIdToRefresh = new HashSet<String>();
  private List<LinkParameter> parameters = new ArrayList<LinkParameter>();
  private Boolean isOpenTargetPageInCurrentWindow = true;
  private LinkType linkType = LinkType.LINK_SIMPLE;
  //======================================================================
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
    ;
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
    if (linkType == null) 
    {
      linkType = LinkType.LINK_SIMPLE;
    }
    if (isOpenTargetPageInCurrentWindow == null) 
    {
      isOpenTargetPageInCurrentWindow = true;
    } 
    if (portletsIdToRefresh == null) 
    {
      portletsIdToRefresh = new HashSet<String>();
    } 
    
  }
  //======================================================================

  public void setTarget(String target)
  {
    this.target = target;
  }

  /**
   * @return
   */
  public String getTarget()
  {
    return target;
  }

  /**
   * @param parameters
   */
  public void setParameters(List<LinkParameter> parameters)
  {
    this.parameters = parameters;
  }

  /**
   * @return
   */
  public List<LinkParameter> getParameters()
  {
    return parameters;
  }

  public String getFullTarget()
  {
    return getFullTarget(getTarget());
  }

  public static String getFullTarget(String target)
  {
    if (target != null &&
        (target.startsWith("http://") || (target.startsWith("https://"))))
    {
      return target;
    }
    String portalServerName = BaseTaskFlowBean.getPortalServerName();
    StringBuilder rc =
      (new StringBuilder(BaseTaskFlowBean.getScheme())).append("://").
      append(portalServerName).
      append(":").
      append(BaseTaskFlowBean.getPortalPort()).
      append(BaseTaskFlowBean.getPortalContext()).
      append("/faces").append(target);
    return rc.toString();
  }

  /**
   * @param portletsIdToRefresh
   */
  public void setPortletIdToRefresh(Set<String> portletsIdToRefresh)
  {
    this.portletsIdToRefresh = portletsIdToRefresh;
  }

  /**
   * @return
   */
  public Set<String> getPortletIdToRefresh()
  {
    if (portletsIdToRefresh == null) 
    {
      portletsIdToRefresh = new HashSet<String>();
    }
    return portletsIdToRefresh;
  }

//  /**
//   * Корректировка UserData в целевых портлетах.
//   */
//  public void processEvent(Map<String, Object> row, String userName)
//    throws Exception
//  {
//    Set<Long> targetPartletIds = this.getPortletIdToRefresh();
//    if (targetPartletIds == null || targetPartletIds.size() == 0)
//    {
//      return;
//    }
//    for (Long portletId: targetPartletIds)
//    {
//      processEventForOnePortlet(row, userName, portletId);
//    }
//  }

//  private void processEventForOnePortlet(Map<String, Object> row,
//                                         String userName, 
//                                         Long portletId)
//    throws Exception
//  {
//    PortletUsers portletUsersTarget = PortletUsers.getRecord(portletId, userName);
//    if (portletUsersTarget == null)
//    {
//      portletUsersTarget =  PortletUsers.createRecord(portletId, userName, new UserData());
//    }
//    UserData userDataTarget = (UserData) (portletUsersTarget.getUserData());
//    if (userDataTarget == null)
//    {
//      // Создадим целевую UserData:
//      portletUsersTarget =  PortletUsers.createRecord(portletId, userName, new UserData());
//      userDataTarget = (UserData) (portletUsersTarget.getUserData());
//    }
//    List<LinkParameter> linkParameters = getParameters();
//    for (LinkParameter linkParameter: linkParameters)
//    {
//      String paramName = linkParameter.getName();
//      String fieldName = linkParameter.getFieldName();
//      Serializable fieldValue = (Serializable) row.get(fieldName);
//      userDataTarget.setSqlParameterCurrentValue(paramName, fieldValue);
////      System.out.println("@@fieldValue = "+fieldValue);
////      if (fieldValue != null) {
////        System.out.println("@@fieldValue.class = "+fieldValue.getClass().getName());
////      }
//      // @@fieldValue = 13.03.2014
//      // @@fieldValue.class = java.lang.String
//      
//      //@@fieldValue = 2014-03-13 00:00:00.0
//      //@@fieldValue.class = java.sql.Timestamp
//
//
//    } // for
//    portletUsersTarget.update(); // сохраняем
//    //==================================================
//    UniversalPortlet1Bean.refreshUserDataHashByMainId(portletId, userName);
//  }

  /**
   * @param isOpenTargetPageInCurrentWindow
   */
  public void setIsOpenTargetPageInCurrentWindow(Boolean isOpenTargetPageInCurrentWindow)
  {
    //if (isOpenTargetPageInCurrentWindow == null) isOpenTargetPageInCurrentWindow = true;
    this.isOpenTargetPageInCurrentWindow = isOpenTargetPageInCurrentWindow;
  }

  /**
   * @return
   */
  public Boolean getIsOpenTargetPageInCurrentWindow()
  {
    //if (isOpenTargetPageInCurrentWindow == null)  isOpenTargetPageInCurrentWindow = true;
    return isOpenTargetPageInCurrentWindow;
  }

  /**
   * @return
   */
  public String getTargetFrame()
  {
    String rc = "_parent";
    if ((isOpenTargetPageInCurrentWindow != null) &&
        (!isOpenTargetPageInCurrentWindow))
    {
      rc = "_blank";
    }
    return rc;
  }

//  public String getVisualFacetName() 
//  {
//    String rc = "LINK";
//    String linkType = null;
//    if (this.linkType != null) {
//      linkType = this.linkType.name();
//    }
//    String linkUrl  = getFullTarget();
//    if (linkType == null) 
//    {
//      linkType = "LINK_TO_PORTAL_RESOURCE";
//    }
//    if (linkUrl != null) 
//    {
//      if      (linkType.equals("LINK_TO_PORTAL_RESOURCE")) 
//      {
//        boolean isAccess = GoLinkOnly.isRenderedByAccess(linkUrl);
//        if (!isAccess) 
//        {
//          rc = "TEXT";
//        }
//      }
//      else if (linkType.equals("LINK_SIMPLE")) 
//      {
//        rc = "LINK";
//      }
//    }
//    return rc;
//  }
  //====================

  public void setLinkType(LinkType linkType)
  {
    this.linkType = linkType;
  }

  public LinkType getLinkType()
  {
    return linkType;
  }
  
  public SelectItem[] getLinkTypeSelectItems() 
  {
    return LinkType.getSelectItems();
  }
  
  public int getTargetPortletsToRefreshCount()
  {
    int rc = 0;
    try
    {
      rc = getPortletIdToRefresh().size();
    }
    catch (Exception e)
    {
      ;
    }
    return rc;
  }
  
  public List<SelectItem> getSelectItemsParameterNamesInTargetPortlet()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    Set<String> set = null;
    try
    {
      set = getPortletIdToRefresh();
    }
    catch (Exception ex)
    {
      System.err.println("Err8 : "+ex.getMessage());
    }
    if (set != null && set.size() > 0)
    {
      String targetPortletMainId = (String) (set.toArray())[0];
      DataInfo dataInfoTarget = null;
      List<SqlParameter> targetSqlParameters = null;
      try
      {
        dataInfoTarget = new DataInfo(targetPortletMainId);
        if (dataInfoTarget != null)
        {
          TaskFlowParametersBaseOnSql parameters = dataInfoTarget.getMainRecord().getTaskFlowParameters(TaskFlowParametersBaseOnSql.class);
          targetSqlParameters = parameters.getSqlParametersList();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      if (targetSqlParameters != null)
      {
        for (SqlParameter item: targetSqlParameters)
        {
          rc.add(new SelectItem(item.getName(), item.getName(),
                                item.getName()));
        } // for
      }
    }
    return rc;
  }
  
  public void deleteLinkParameterActionListener(ActionEvent actionEvent)
  {
    LinkParameter row = (LinkParameter) (actionEvent.getComponent().getAttributes().get("row"));
    if (row != null)
    {
      getParameters().remove(row);
    }
  }
  
  public List<Map<String, SqlAttribute>> getListMapSqlAttributes()
  {
    List<Map<String, SqlAttribute>> rc =  new ArrayList<Map<String, SqlAttribute>>();
    List<SqlAttribute> list = null;
    try
    {
      list = CommonUtilsImpl.getInstance().getMainRecord().getTaskFlowParameters(TaskFlowParametersBaseOnSql.class).
          getSqlAttributesList();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (list == null)
    {
      list = new ArrayList<SqlAttribute>();
    }

    for (SqlAttribute field: list)
    {
      Map<String, SqlAttribute> map = new HashMap<String, SqlAttribute>();
      map.put(ATTRIBUTE, field);
      rc.add(map);
    }
    return rc;
  }
  
  public String newLinkParameterAction()
  {
    List<LinkParameter> parameters = getParameters();
    if (parameters == null)
    {
      parameters = new ArrayList<LinkParameter>();
      setParameters(parameters);
    }
    LinkParameter newLinkParam = new LinkParameter();
    newLinkParam.setName("* Новый параметр *");
    List<Map<String, SqlAttribute>> list = getListMapSqlAttributes();
    newLinkParam.setFieldName(list.get(0).get(ATTRIBUTE).getName());

    parameters.add(newLinkParam);
    return null;
  }
}
