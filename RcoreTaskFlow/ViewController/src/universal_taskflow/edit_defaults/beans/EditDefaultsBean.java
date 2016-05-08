package universal_taskflow.edit_defaults.beans;


import com.rcore.global.StringFunc;
import com.rcore.global.jsf.JSFUtils;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adfinternal.controller.train.MenuModelAdapter;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;

import universal_taskflow.common.beans.BaseTaskFlowBean;
import universal_taskflow.common.config.Accessibility;
import universal_taskflow.common.config.AccessibilityDefault;
import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.ElHtmlKey;
import universal_taskflow.common.data.ElHtmlValue;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.SqlAttribute;
import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.DiagramTypeParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.FormTypeParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableAdfParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableHtmlParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TaskFlowParametersBaseOnSql;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TreeTypeParameters;
import universal_taskflow.common.types.LabelAlign;


public class EditDefaultsBean extends BaseTaskFlowBean
{
  private static final long serialVersionUID = 1L;
  private final static Map<String, String> trainTextMap = new HashMap<String,String>();
  //private final transient Map<Object, Boolean> trainRenderedMap = new TrainRenderedMap();
  private transient MenuModelAdapter trainModel = null;
  private transient ChildPropertyTreeModel taskFlowTypeTreeModel = null;
  private transient Map<TaskFlowTypeNode,Boolean> typeNodeSelectedMap = new TypeNodeSelectedMap();
  //---------------------------------------------------
  private transient Map<Object,Boolean> mapIsContains = new MapIsContains(); //+
  private transient Map<String,String> mapElHtmlValuesByString = 
                                          new MapElHtmlValuesByString(); //+
  //..private transient ElHtmlKey elHtmlKeyCurrent = null;
  //..private transient ElHtmlValue elHtmlValueCurrent = null;
  private transient String selectedELValue = null;
  private transient RichPopup elHtmlPopup = null;
  
  private transient SqlAttribute[] selectedFieldsInTable = null;
  private transient SqlAttribute[] selectedFieldsInForm = null;
  
  static 
  {
    trainTextMap.put("step1", "Основные данные");
    trainTextMap.put("step2", "Тип потока задач");
    trainTextMap.put("step3", "Параметры потока задач");
    trainTextMap.put("step4", "Финиш");
  }

  public EditDefaultsBean()
  {
    super();
  }
  public static EditDefaultsBean getInstance() 
  {
    return (EditDefaultsBean)(JSFUtils.resolveExpression("#{backingBeanScope.editDefaultsBean}"));
  }
  //----
  public MenuModelAdapter getTrainModel() 
  {
    if (trainModel == null) 
    {
      trainModel = (MenuModelAdapter) JSFUtils.resolveExpression("#{controllerContext.currentViewPort.taskFlowContext.trainModel}");  
    }
    return trainModel;    
  }
  
  public Map<String, String> getTrainText()
  {
    return trainTextMap;
  }
  public boolean isNewInstancePortlet() 
  {
    MainRecord mainRecord = getMainRecord();
    return mainRecord.isNewInstance();
  }
  
  public Map<Object, Boolean> getMapIsContains()
  {
    //if (mapIsContains == null) mapIsContains = new MapIsContains();
    return mapIsContains;
  }
  
  public Map<String, String> getMapElHtmlValuesByString()
  {
    //if (mapElHtmlValuesByString == null) mapElHtmlValuesByString = new MapElHtmlValuesByString();
    return mapElHtmlValuesByString;
  }
  
  public void setElHtmlPopup(RichPopup elHtmlPopup)
  {
    this.elHtmlPopup = elHtmlPopup;
  }

  public RichPopup getElHtmlPopup()
  {
    return elHtmlPopup;
  }
  
  public void okActionListenerInPopupEL(ActionEvent event) 
  {
    Map<String,Object> attrs = event.getComponent().getAttributes();
    String propertyClassName = (String) attrs.get("propertyClassName");
    String propertyName = (String) attrs.get("propertyName");
    Serializable objectId = (Serializable) attrs.get("objectId"); 
    Boolean clearTargetProperty = (Boolean) attrs.get("clearTargetProperty");
    Object object = attrs.get("object");
    if (clearTargetProperty == null) 
    {
      clearTargetProperty = false;
    }
    //--------------
    ElHtmlKey elHtmlKeyCurrent = new ElHtmlKey(propertyClassName,propertyName,objectId);
    ElHtmlValue elHtmlValueCurrent = getMainRecord().getElHtmlValues().get(elHtmlKeyCurrent);
    if (elHtmlValueCurrent == null) 
    {
      elHtmlValueCurrent = new ElHtmlValue();
    }
    if (getExpressionTypeInput().equals(ExpressionTypeInput.EL))
    {
      String exp = getExpressionTypeInput().getElInputText();
      if (StringFunc.isEmpty(exp))
      {
        JSFUtils.addFacesErrorMessage("Значение EL пусто");
        return;
      }
      try
      {
        JSFUtils.resolveExpression(exp);
      }
      catch (Exception e)
      {
        JSFUtils.addFacesErrorMessage("Ошибка в EL выражении : " + e.getMessage());
        return ;
      }
      //----------------------------
      elHtmlValueCurrent.setEl(exp);
      elHtmlValueCurrent.setHtml(null);
      getMainRecord().getElHtmlValues().put(elHtmlKeyCurrent, elHtmlValueCurrent);
      clearProperty(clearTargetProperty,propertyClassName,propertyName,object);
    }
    else if (getExpressionTypeInput().equals(ExpressionTypeInput.HTML))
    {
      String exp = getExpressionTypeInput().getHtmlInputText();
      if (StringFunc.isEmpty(exp))
      {
        JSFUtils.addFacesErrorMessage("Значение HTML - пусто");
        return ;
      }
      elHtmlValueCurrent.setEl(null);
      elHtmlValueCurrent.setHtml(getExpressionTypeInput().getHtmlInputText());
      getMainRecord().getElHtmlValues().put(elHtmlKeyCurrent, elHtmlValueCurrent);
      clearProperty(clearTargetProperty,propertyClassName,propertyName,object);
    }
    else if (getExpressionTypeInput().equals(ExpressionTypeInput.LITERAL))
    {
      getMainRecord().getElHtmlValues().remove(elHtmlKeyCurrent);
    }
    elHtmlPopup.hide();
    //---
    
    return;
  }
  
  private static void clearProperty(boolean clearTargetProperty,
                             String propertyClassName,
                             String propertyName,
                             Object object
                             ) 
  {
    if (clearTargetProperty) 
    {
      Class cl = null;
      try
      {
        cl = Class.forName(propertyClassName);
        Field field = cl.getDeclaredField(propertyName);
        field.setAccessible(true);
        field.set(object, null);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void elHtmlActionListener(ActionEvent event)
  {
    String propertyClassName = (String) event.getComponent().getAttributes().get("propertyClassName");
    String propertyName = (String) event.getComponent().getAttributes().get("propertyName");
    Serializable objectId = (Serializable) event.getComponent().getAttributes().get("objectId");
    //----
    ElHtmlKey elHtmlKeyCurrent = new ElHtmlKey(propertyClassName,propertyName,objectId);
    ElHtmlValue elHtmlValueCurrent = getMainRecord().getElHtmlValues().get(elHtmlKeyCurrent);
    ExpressionTypeInput expressionTypeInput = getExpressionTypeInput();
    if (expressionTypeInput != null) 
    {
      expressionTypeInput.setElInputText(null);
      expressionTypeInput.setHtmlInputText(null);
    }
    
    if (elHtmlValueCurrent == null)
    {
      elHtmlValueCurrent = new ElHtmlValue();
      setExpressionTypeInput(ExpressionTypeInput.LITERAL);
    }
    else
    {
      if (elHtmlValueCurrent.getEl() != null)
      {
        setExpressionTypeInput(ExpressionTypeInput.EL);
        getExpressionTypeInput().setElInputText(elHtmlValueCurrent.getEl());
      }
      else
      {
        setExpressionTypeInput(ExpressionTypeInput.HTML);
        getExpressionTypeInput().setHtmlInputText(elHtmlValueCurrent.getHtml());
      }
    }
    selectedELValue = "#{elBean.today}";
    RichPopup.PopupHints hints = new RichPopup.PopupHints();
    elHtmlPopup.show(hints);
  }
  
  public String selectELAction()
  {
    getExpressionTypeInput().setElInputText(selectedELValue);
    return null;
  }
  
  public void setSelectedELValue(String selectedELValue)
  {
    this.selectedELValue = selectedELValue;
  }

  public String getSelectedELValue()
  {
    return selectedELValue;
  }
  
  public Boolean getSelectInputTypeEL()
  {
    if (getExpressionTypeInput() == null) System.err.println("@EL error");
    return ExpressionTypeInput.EL.equals(getExpressionTypeInput());
  }
  
  public void setSelectInputTypeEL(Boolean value)
  {
    if ((value != null) && value)  setExpressionTypeInput(ExpressionTypeInput.EL);
  }
  
  public Boolean getSelectInputTypeHTML()
  {
    if (getExpressionTypeInput() == null) System.err.println("@HTML error");
    return ExpressionTypeInput.HTML.equals(getExpressionTypeInput());
  }
  public void setSelectInputTypeHTML(Boolean value)
  {
    if ((value != null) && value)  setExpressionTypeInput(ExpressionTypeInput.HTML);
  }
  public Boolean getSelectInputTypeLiteral()
  {
    if (getExpressionTypeInput() == null) System.err.println("@LITERAL error");
    return ExpressionTypeInput.LITERAL.equals(getExpressionTypeInput());
  }
  public void setSelectInputTypeLiteral(Boolean value)
  {
    if ((value != null) && value)  setExpressionTypeInput(ExpressionTypeInput.LITERAL);
  }
  
  public void setExpressionTypeInput(ExpressionTypeInput expressionTypeInput)
  {
    AdminPageFlowBean.getInstance().setExpressionTypeInput(expressionTypeInput);
  }

  public ExpressionTypeInput getExpressionTypeInput()
  {
    return AdminPageFlowBean.getInstance().getExpressionTypeInput();
  }

  public void checkElActionListener(ActionEvent event)
  {
    String exp = (String) event.getComponent().getAttributes().get("value");
    if (StringFunc.isEmpty(exp))
    {
      JSFUtils.addFacesErrorMessage("Пустое выражение");
      return;
    }
    try
    {
      Object value = JSFUtils.resolveExpression(exp);
      JSFUtils.addFacesInformationMessage("Получено : "+value);
    }
    catch(Exception e)
    {
      JSFUtils.addFacesErrorMessage("Ошибка : "+e.getMessage());
    }
    return;
  }
  
  public void updateActionListener(ActionEvent event) 
  {
    update();
    AdminPageFlowBean.getInstance().setStatusMessage(MessageType.info,"Данные сохранены");
  }
  public TaskFlowParametersBase getTypeParams() 
  {
    TaskFlowParametersBase rc = null;
    try
    {
      return getMainRecord().getTaskFlowParameters();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }
  //==================================================
  public TaskFlowParametersBaseOnSql getParamsForOnSql() 
  {
    return (TaskFlowParametersBaseOnSql) getTypeParams();
  }
  
  public TableAdfParameters getParamsForAdfTable() 
  {
    return (TableAdfParameters) getTypeParams();
  }
  
  public TableHtmlParameters getParamsForHtmlTable() 
  {
    return (TableHtmlParameters) getTypeParams();
  }
  
  public FormTypeParameters getParamsForForm() 
  {
    return (FormTypeParameters) getTypeParams();
  }
  public TreeTypeParameters getParamsForTree() 
  {
    return (TreeTypeParameters) getTypeParams();
  }
  public DiagramTypeParameters getParamsForDiagram() 
  {
    return (DiagramTypeParameters) getTypeParams();
  }
  //==================================================
  public ChildPropertyTreeModel getTaskFlowTypeTreeModel()
  {
    if (taskFlowTypeTreeModel == null) 
    {
      taskFlowTypeTreeModel = TaskFlowTypeNode.buildTaskFlowTypeTreeModel();
    }
    return taskFlowTypeTreeModel;
  }

  public Map<TaskFlowTypeNode, Boolean> getTypeNodeSelectedMap()
  {
    return typeNodeSelectedMap;
  }
  
  public List<SelectItem> getLabelAlignSelectItems() 
  {
    return LabelAlign.getSelectItems();
  }
  public boolean isAccessibility()
  {
    Accessibility accessibility = TaskFlowSystemProperties.getInstance().getAccessibility();
    if (accessibility == null) 
    {
      accessibility = new AccessibilityDefault();
    }
    return accessibility.isAccessToAdminInterface();
  }
  //-------
  
  public void setSelectedFieldsInTable(SqlAttribute[] selectedFieldsInTable)
  {
    this.selectedFieldsInTable = selectedFieldsInTable;
  }

  public SqlAttribute[] getSelectedFieldsInTable()
  {
    if (selectedFieldsInTable == null) 
    {
      try
      {
        List<SqlAttribute> list = getMainRecord().getTaskFlowParametersAsOnSql().getVisibleFieldsInTable();
        selectedFieldsInTable = list.toArray(new SqlAttribute[list.size()]);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return selectedFieldsInTable;
  }

  public void setSelectedFieldsInForm(SqlAttribute[] selectedFieldsInForm)
  {
    this.selectedFieldsInForm = selectedFieldsInForm;
  }

  public SqlAttribute[] getSelectedFieldsInForm()
  {
    if (selectedFieldsInForm == null) 
    {
      try
      {
        List<SqlAttribute> list = getMainRecord().getTaskFlowParametersAsOnSql().getVisibleFieldsInForm();
        selectedFieldsInForm = list.toArray(new SqlAttribute[list.size()]);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return selectedFieldsInForm;
  }
  
  public SelectItem[] getAllFieldsSelectItems() 
  {
    SelectItem[] rc = null;
    try
    {
      List<SqlAttribute> list = getMainRecord().getTaskFlowParametersAsOnSql().getSqlAttributesList();
      SqlAttribute[] listFields = list.toArray(new SqlAttribute[list.size()]); 
      rc = new SelectItem[listFields.length];
      for (int i=0; i < listFields.length; i++) 
      {
        rc[i] = new SelectItem(listFields[i],listFields[i].getName(),listFields[i].getName());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }
  
  public void selectFieldsActionListener(ActionEvent event) 
  {
    String parentComponent = (String) event.getComponent().getAttributes().get("parentComponent");
    if ("table".equals(parentComponent)) 
    {
      List<SqlAttribute> list = getMainRecord().getTaskFlowParametersAsOnSql().getSqlAttributesList();
      for (SqlAttribute item : list) 
      {
        item.setVisibleInTable(false);
        item.setIndexTable(null);
      }
      if (selectedFieldsInTable != null) 
      {
        
        for (int i=0; i < selectedFieldsInTable.length; i++) 
        {
          SqlAttribute item = selectedFieldsInTable[i];
          item.setIndexTable((i+1)*100);
          item.setVisibleInTable(true);
        }
        selectedFieldsInTable = null;
      }
    }
    else if ("form".equals(parentComponent)) 
    {
      if (selectedFieldsInForm != null) 
      {
        List<SqlAttribute> list = getMainRecord().getTaskFlowParametersAsOnSql().getSqlAttributesList();
        for (SqlAttribute item : list) 
        {
          item.setVisibleInForm(false);
          item.setIndexForm(null);
        }
        for (int i=0; i < selectedFieldsInForm.length; i++) 
        {
          SqlAttribute item = selectedFieldsInForm[i];
          item.setIndexForm((i+1)*100);
          item.setVisibleInForm(true);
        }
        selectedFieldsInForm = null;
      }
    }
  }
  public void cancelFieldsActionListener(ActionEvent event) 
  {
    String parentComponent = (String) event.getComponent().getAttributes().get("parentComponent");
    if ("table".equals(parentComponent)) 
    {
      selectedFieldsInTable = null;
    }
    else if ("form".equals(parentComponent)) 
    {
      selectedFieldsInForm = null;
    }
  }

  //  public void typeTreeListener(SelectionEvent selectionEvent)
//  {
//    RichTree tree = (RichTree) selectionEvent.getComponent();
//    TaskFlowTypeNode node = TaskFlowTypeNode.getSelectedNodeFromTree(tree);
//    if (!node.isLeaf()) return;
//    getMainRecord().setTaskFlowType(node.getType());
//    return;
//  }
  //================================
  private class TypeNodeSelectedMap extends HashMap<TaskFlowTypeNode,Boolean>
  {
    private static final long serialVersionUID = 1L;
    @Override
    public Boolean get(Object obj) 
    {
      boolean rc = false;
      TaskFlowTypeNode node = (TaskFlowTypeNode) obj;
      if (node.isLeaf() && node.getType().equals(getMainRecord().getTaskFlowType())) 
      {
        rc = true;
      }
      return rc;
    }
    
    @Override
    public Boolean put(TaskFlowTypeNode node, Boolean value) 
    {
      boolean rc = false;
      //System.out.println("@panelTrainHeader = "+panelTrainHeader);
      if (value != null && value && node != null && node.isLeaf()) 
      {
        getMainRecord().setTaskFlowType(node.getType());
        rc = true;
      }
      UIComponent panelTrainHeader = AdminPageFlowBean.getInstance().getPanelTrainHeader();
      if (panelTrainHeader != null) 
      {
        addPartialTarget(panelTrainHeader);
      }
      return rc;
    }
  }
  
  public class MapIsContains extends HashMap<Object,Boolean>
  {
    private static final long serialVersionUID = 1L;
    @Override
    public Boolean get(Object attrs)
    {
      Map map = (Map) attrs;
      ElHtmlKey key = new ElHtmlKey((String) map.get("propertyClassName"), 
                                    (String) map.get("propertyName"), 
                                    (Serializable) map.get("objectId"));
      boolean rc = getMainRecord().getElHtmlValues().containsKey(key);
      return rc;
    }
  }
  
  public class MapElHtmlValuesByString extends HashMap<String,String>
  {
    private static final long serialVersionUID = 1L;
    @Override
    public String get(Object attrs)
    {
      String rc = getValue(attrs);
      if (rc != null && rc.length() > 60) 
      {
        rc = rc.substring(0, 60)+"...";
      }
      return rc;
    }
    
    private String getValue(Object attrs)
    {
      Map map = (Map) attrs;
      String rc = null;
      ElHtmlKey key = new ElHtmlKey((String) map.get("propertyClassName"), 
                                        (String) map.get("propertyName"), 
                                        (Serializable) map.get("objectId"));
      ElHtmlValue value = getMainRecord().getElHtmlValues().get(key);
      if (value != null)
      {
        rc = value.getValue();
      }
      return rc;
    }
  }

}
