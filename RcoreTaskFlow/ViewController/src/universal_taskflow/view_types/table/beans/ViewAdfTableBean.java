package universal_taskflow.view_types.table.beans;


import com.rcore.global.jsf.JSFUtils;
import com.rcore.model.dynamic_list.ConnectionInterface;
import com.rcore.utils.table.TableModelesIntegratorImpl;
import com.rcore.utils.table.TableModelesIntegratorSimple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodInfo;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.layout.RichToolbar;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;
import oracle.adf.view.rich.event.ClientListenerSet;
import oracle.adf.view.rich.render.ClientEvent;

import org.apache.myfaces.trinidad.event.PollEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.event.SelectionListener;
import org.apache.myfaces.trinidad.event.SortEvent;
import org.apache.myfaces.trinidad.event.SortListener;
import org.apache.myfaces.trinidad.model.CollectionModel;

import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.SqlAttribute;
import universal_taskflow.common.data.UserRecord;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableAdfParameters;
import universal_taskflow.common.types.ReadFromDbType;
import universal_taskflow.common.types.SelectionSortFilterListener;
import universal_taskflow.common.utils.DataSourceInfo;


public class ViewAdfTableBean extends BaseTableBean 
        implements SelectionListener,SortListener
{
  private static final long serialVersionUID = 1L;
  //----------------------------------------------
  private TableModelesIntegratorImpl tableModeles = null;
  private FilterValueClass filterValue = new FilterValueClass();
  //private transient Map<String, RichColumn> mapColumns = null;
  //private transient RichTable table = null;
  private transient RichTable tableNoCategories = null;
  private transient RichTable tableCategories;
  private MyMethodExpression myMethodExpression = new MyMethodExpression(this);
  private transient RichPanelCollection panelCollection = null;
  //----------------------------------------------
  public ViewAdfTableBean()
  {
    super();
  }

  public FilterValueClass getFilterValue()
  {
    if (filterValue == null)
    {
      filterValue = new FilterValueClass();
    }
    return filterValue;
  }

  public TableAdfParameters getTaskFlowParametersAsTableAdfParameters()
    throws Exception
  {
    return getMainRecord().getTaskFlowParametersAsTableAdfParameters();
  }
  //---------------------

  public void myselectionLsnr(SelectionEvent selectionEvent)
  {
    // TODO
    getTableModeles().selectionListener(selectionEvent);
//    getUserData().setTableBean(this);
//    selectionSortAndFilterProcess(selectionEvent, null, false);
  }

  public void sortListener(SortEvent sortEvent)
  {
    if (tableModeles != null)
    {
      tableModeles.sortListener(sortEvent);
      if (getTable() != null) 
      {
        tableModeles.setSelectedRowData(getTable().getSelectedRowData());
      }
      selectionSortAndFilterProcess(null, sortEvent, false);
    }
  }

  private void selectionSortAndFilterProcess(SelectionEvent selectionEvent,
                                             SortEvent sortEvent,
                                             boolean isFilter
                                            )
  {
    // TODO
    if (tableModeles != null)
    {
      if (isFilter)
      {
        tableModeles.refreshDataModel();
        addPartialTarget(getTable());
      }
      else if (sortEvent != null)
      {
        tableModeles.refreshDataModel();
        addPartialTarget(getTable());
      }
    }
    try
    {
      internalListenerProcess(selectionEvent, sortEvent);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    try
    {
      SelectionSortFilterListener listener =
        (SelectionSortFilterListener) getPageFlowScope().get("selectionSortFilterListener");
      if (listener != null)
      {
        Map<String,Object> rowMap = (Map<String, Object>) getCurrentRecord();
        if (rowMap != null)
        {

        listener.processEvent(getTaskFlowParameterId(),
                              selectionEvent,
                              sortEvent,
                              rowMap);
       }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public Object getCurrentRecord() 
  {
    Object rc = null;
    if (tableModeles != null) 
    {
      rc = tableModeles.getSelectedRowData();
    }
    return rc;
  }
  
  private void internalListenerProcess(SelectionEvent selectionEvent,
                                       SortEvent sortEvent)
    throws Exception
  {
    // TODO
//    AdminData ad = getAdminData();
//    CommunicationWithOtherPortlets info = ad.getCommunicationWithOtherPortlets();
//    Set<Long> portlets = info.getPortletsIdToRefresh(); // info != null
//    if (portlets == null || portlets.size() == 0)
//    {
//      return;
//    }
//    List<LinkParameter> parameters = info.getParameters();
//    for (Long id: portlets)
//    {
//      internalListenerProcessForOnePortlet(id, 
//                                           parameters, 
//                                           selectionEvent,
//                                           sortEvent);
//    }
  }

  public TableModelesIntegratorImpl getTableModeles()
  {
    if (tableModeles == null)
    {
      try
      {
        tableModeles = buildTableModeles();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return tableModeles;
  }

  private TableModelesIntegratorImpl buildTableModeles()
    throws Exception
  {
    MainRecord mr = getMainRecord();
    TableAdfParameters tableTypeParameters = mr.getTaskFlowParametersAsTableAdfParameters();
      TableModelesIntegratorImpl rc = null;
      ReadFromDbType readFromDbType = tableTypeParameters.getReadFromDbType();
      TableAdfParameters parameters = getTaskFlowParametersAsTableAdfParameters();
      ConnectionInterface connectionInterface = getConnectionInterface();
      String sql = parameters.getSql();
      UserRecord ur = getUserRecord();
      Map<String, Object> mapSqlParameters = parameters.getSqlParametersCurrentValues(ur);
      List<String> visibleFilterAbleAttributes =  parameters.getVisibleFilterAbleFieldsInTable(parameters.getSqlAttributesList());
      Map<String,Integer> sqlAttributeTypes = parameters.getSqlAttributeTypes();

      if (ReadFromDbType.JUST_READ_ALL.equals(readFromDbType))
      {
        rc = new TableModelesIntegratorSimple
        (
          connectionInterface,
          sql,
          mapSqlParameters,
          visibleFilterAbleAttributes,
          sqlAttributeTypes,
          tableTypeParameters.getRecordCountForReadFromDb()
        );
      }
      else
      {
        rc = new TableModelesIntegratorImpl
        (
          connectionInterface,
          sql,
          mapSqlParameters,
          visibleFilterAbleAttributes,
          parameters.getFetchSize(),
          sqlAttributeTypes
        );
      }
    CollectionModel model = rc.getDataModel();
    if (model.getRowCount() == 1)
    {
      Object rowData = model.getRowData(0);
      rc.setSelectedRowData(rowData);
    }
    return rc;
  }

  private ConnectionInterface getConnectionInterface() throws Exception
  {
    DataSourceInfo info = getTaskFlowParametersAsTableAdfParameters().getDataSourceCurrent();
    return info;
  }
  
  public RichTable getTable() 
  {
    RichTable rc = tableCategories;
    if (rc == null) 
    {
      rc = tableNoCategories;
    }
    return rc;
  }

  public void doCustomEventForColumn(ClientEvent event)
  {
    try {
      //String propertyName = event.getParameters().get("propertyName");
      UIComponent src = event.getComponent();
      if (src instanceof RichColumn)
      {
        RichColumn col = (RichColumn)src;
        String w = null;
        w = event.getParameters().get("width").toString();
        if (w != null)
        {
          int index = w.lastIndexOf(".");
          if (index != -1)
          {
            w = w.substring(0,index);
          }
        }
        col.setWidth(w);
      }
      saveOrderAndWidthColumns();
    }
    catch(Exception ex)
    {
      System.err.println("BaseTableComponent.doCustomEventForColumn Error 1");
      ex.printStackTrace();
    }
  }

  private void saveOrderAndWidthColumns()
  {
    // TODO
  }
  public void clickLinkActionListener(ActionEvent event)
  {
    // TODO
//    // Публикуем событие для портлетов, которые с нами связаны:
//    Map<String, Object> row       = (Map<String, Object>) event.getComponent().getAttributes().get("row");
//    SqlAttribute        attribute = (SqlAttribute) event.getComponent().getAttributes().get("attribute");
//    // ---- Преобразуем данные в row ----
//    row = transformRow(row);
//    //-----------------------------------
//
//    LinkInfo info = attribute.getLinkInfo();
//    if (info.getPortletIdToRefresh() == null) // Он должен быть готов к этому времени
//    {
//      info.setPortletIdToRefresh(new HashSet<Long>());
//    }
//
//    try
//    {
//      info.processEvent(row, getUserName());
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    // Открываем в текущем, или новом окне
//    String targetFrame = attribute.getLinkInfo().getTargetFrame();
//    String target = attribute.getLinkInfo().getTarget();
//    Boolean pprnav = (Boolean) event.getComponent().getAttributes().get("pprnav");
//    if (pprnav == null)
//    {
//      pprnav = false;
//    }
//    if ("_blank".equals(targetFrame))
//    {
//      evalJScript(targetFrame, target);
//    }
//    else
//    {
//      if (pprnav)
//      {
//        Object node = GoLink.getResourceObjectByPath(target);
//        if (node != null)
//        {
//          event.getComponent().getAttributes().put("node", node);
//          System.out.println("@GoLink.processActionStatic");
//
//          GoLink.processActionStatic(event);
//        }
//        else
//        {
//          evalJScript(targetFrame, target);
//        }
//      }
//      else
//      {
//        evalJScript(targetFrame, target);
//      }
//    }
//    //-------------------
  }

//  public Map<String, RichColumn> getMapColumns()
//  {
//    if (mapColumns == null)
//    {
//      mapColumns = new MapColumns();
//    }
//    return mapColumns;
//  }

  private void setClientAndServerListemers(RichColumn value)
  {
    ClientListenerSet clientListenerSet = new ClientListenerSet();
    String type = "propertyChange";
    String funcName = "window.universalTaskFlow._columnPropChange";
    clientListenerSet.addListener(type, funcName); // ClientListener
    clientListenerSet.addCustomServerListener(     // ServerListener
        "_myCustomEvent",
        myMethodExpression
    );
    value.setClientListeners(clientListenerSet);
    //-------
  }

  public void setTableNoCategories(RichTable tableNoCategories)
  {
    this.tableNoCategories = tableNoCategories;
//    }
  }
  
  public Map<String,SqlAttribute> getColums() 
  {
    Map<String,SqlAttribute> rc = null;
    try
    {
      rc = getTaskFlowParametersAsTableAdfParameters().getSqlAttributes();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }

  public RichTable getTableNoCategories()
  {
    return tableNoCategories;
  }

  public void setTableCategories(RichTable tableCategories)
  {
    this.tableCategories = tableCategories;
  }

  public RichTable getTableCategories()
  {
    return tableCategories;
  }

  //--------- REFRESH ---------------------
  @Override
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
    RichTable table = getTable();
    if (table != null)
    {
      addPartialTarget(table);
    }

  }
  @Override
  protected void refresh() throws Exception
  {
    //-----------------------
    if (tableModeles != null)
    {
      tableModeles.dispose();
    }
    tableModeles = null;

    //table = null;
    setNotCategoryChanged();
    //-----------------------
    refreshData();
  }

  public void processSelection(SelectionEvent selectionEvent)
  {
    myselectionLsnr(selectionEvent);
  }
  
  public void processSort(SortEvent sortEvent)
  {
    sortListener(sortEvent);
  }

  public void setPanelCollection(RichPanelCollection panelCollection)
  {
    this.panelCollection = panelCollection;
    initPanelCollection(panelCollection);
  }

  public RichPanelCollection getPanelCollection()
  {
    return panelCollection;
  }

  private void initPanelCollection(RichPanelCollection panelCollection)
  {
    Boolean inited = (Boolean) panelCollection.getAttributes().get("inited");
    if (inited != null) 
    {
      return;
    }
    try 
    {
      FacesContext context = FacesContext.getCurrentInstance();
      Application application = context.getApplication();
      
      Set set = new HashSet();
      set.add("statusBar");
      set.add("detach");
      panelCollection.setFeaturesOff(set);
      panelCollection.setStyleClass("AFStretchWidth");
      
      // -------- Toolbar
      RichToolbar toolbar = (RichToolbar) application.createComponent(RichToolbar.COMPONENT_TYPE);
      toolbar.setId("_toolbar_pan_col");
      toolbar.setRendered(getTaskFlowParametersAsOnSql().isTaskFlowInEditMode());
      
      RichCommandToolbarButton commandToolbarButton = (RichCommandToolbarButton) application.createComponent(RichCommandToolbarButton.COMPONENT_TYPE);
      commandToolbarButton.setId("_InsertDb");
      commandToolbarButton.setText("Добавить");
      commandToolbarButton.setRendered(getTaskFlowParametersAsOnSql().isTaskFlowInsertAble());
      commandToolbarButton.getAttributes().put("action", "insert");
      commandToolbarButton.addActionListener(JSFUtils.createActionListenerByElExp(
            "#{pageFlowScope.viewAdfTableBean.editActionListener}"));
      toolbar.getChildren().add(commandToolbarButton);
      commandToolbarButton.setParent(toolbar);
      
      commandToolbarButton = (RichCommandToolbarButton) application.createComponent(RichCommandToolbarButton.COMPONENT_TYPE);
      commandToolbarButton.setId("_UpdateDb");
      commandToolbarButton.setText("Изменить");
      commandToolbarButton.setRendered(getTaskFlowParametersAsOnSql().isTaskFlowUpdateAble());
      commandToolbarButton.getAttributes().put("action", "update");
      commandToolbarButton.addActionListener(JSFUtils.createActionListenerByElExp(
            "#{pageFlowScope.viewAdfTableBean.editActionListener}"));
      toolbar.getChildren().add(commandToolbarButton);
      commandToolbarButton.setParent(toolbar);
      
      commandToolbarButton = (RichCommandToolbarButton) application.createComponent(RichCommandToolbarButton.COMPONENT_TYPE);
      commandToolbarButton.setId("_DeleteDb");
      commandToolbarButton.setText("Удалить");
      commandToolbarButton.setRendered(getTaskFlowParametersAsOnSql().isTaskFlowDeleteAble());
      commandToolbarButton.getAttributes().put("action", "delete");
      commandToolbarButton.addActionListener(JSFUtils.createActionListenerByElExp(
            "#{pageFlowScope.viewAdfTableBean.editActionListener}"));
      toolbar.getChildren().add(commandToolbarButton);
      commandToolbarButton.setParent(toolbar);
      // ----
      panelCollection.getFacets().put("toolbar", toolbar);
      //--------------------------------------------------
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    finally 
    {
      panelCollection.getAttributes().put("inited",true);
    }
  }
  
  public void editActionListener(ActionEvent event) 
  {
    System.out.println("@editActionListener");
    String action = (String) event.getComponent().getAttributes().get("action");
    // TODO
  }

  //============================================================================

  public class MyMethodExpression extends MethodExpression
  {
    private static final long serialVersionUID = 1L;
    private final transient MethodInfo methodInfo =
      new MethodInfo("doCustomEventForColumn", void.class, new Class[]
        { ClientEvent.class });
    private transient ViewAdfTableBean parent = null;
    private MyMethodExpression(ViewAdfTableBean parent)
    {
      super();
      this.parent = parent;
    }
    public MethodInfo getMethodInfo(ELContext elContext)
    {
      return methodInfo;
    }

    public Object invoke(ELContext elContext, Object[] objects)
    {
      doCustomEventForColumn((ClientEvent)objects[0]);
      return null;
    }

    public String getExpressionString()
    {
      return "#{component.doCustomEventForColumn}";
    }

    public boolean isLiteralText()
    {
      return true;
    }

    public boolean equals(java.lang.Object p1) {
      if (p1 == null)
      {
        return false;
      }
      if (p1 == this)
      {
        return true;
      }
      if ((p1 instanceof MyMethodExpression))
      {
        return false;
      }
      return p1.equals(this);
    }

    public int hashCode() {
      return parent.hashCode();
    }
  }

  public class FilterValueClass extends HashMap<String,Object>
  {
    private static final long serialVersionUID = 1L;

    @Override
    public Object get(Object obj)
    {
      String field = (String) obj;
      StringBuilder el = new StringBuilder("#{");
      RichTable table = getTable();
      String varStatus = "varStatus";
      if (table != null)
      {
        varStatus = table.getVarStatus();
      }
      el.append(varStatus).append(".filterCriteria.").append(field).append("}");
      String elStr = el.toString();
      Object rc = JSFUtils.resolveExpression(elStr);
      return rc;
    }

    @Override
    public Object put(String field,Object value)
    {
      StringBuilder el = new StringBuilder("#{");
      RichTable table = getTable();
      String varStatus = "varStatus";
      if (table != null)
      {
        varStatus = table.getVarStatus();
      }
      el.append(varStatus).append(".filterCriteria.").append(field).append("}");
      String elStr = el.toString();
      JSFUtils.setExpressionValue(elStr, value);
      if (table != null)
      {
        tableModeles.setSelectedRowData(table.getSelectedRowData());
        SelectionEvent selectionEvent = new SelectionEvent(table,
                                                           table.getSelectedRowKeys(),
                                                           table.getSelectedRowKeys()
                                                          );
        selectionSortAndFilterProcess(selectionEvent, null, true);

        // После этого могло произойти рассогласование таблицы и модели, поэтому:
        // Mark 12.11.2015
        Object selRowData = getTable().getSelectedRowData();
        tableModeles.setSelectedRowData(selRowData);
      }
      return value;
    }
  }

//  public class MapColumns extends HashMap<String,RichColumn>
//  {
//    private static final long serialVersionUID = 1L;
//    @Override
//    public RichColumn put(String columnName, RichColumn value)
//    {
//      Boolean inited = (Boolean) value.getAttributes().get("inited");
//      if (inited == null)
//      {
//        try
//        {
//          initRichColumn(columnName, value);
//        }
//        catch (Exception e)
//        {
//          e.printStackTrace();
//        }
//        finally
//        {
//          value.getAttributes().put("inited",true);
//        }
//      }
//      return super.put(columnName, value);
//    }
//  }
}