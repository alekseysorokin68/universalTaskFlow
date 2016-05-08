package com.rcore.component.treenavigator;


import com.rcore.component.base.BaseComponentBean;
import com.rcore.global.StringFunc;

import com.rcore.global.Token;

import com.rcore.global.adf.AdfJdbcUtils;
import com.rcore.global.adf.DbRecord;
import com.rcore.global.jsf.JSFUtils;
import com.rcore.global.xml.XmlDoc;
import com.rcore.model.dynamic_list.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.MethodExpression;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;

import org.w3c.dom.Node;


// #{addrCompBean.testProperty}
//InecPropertyElement
//InecPropertyTreeModel
public class TreeNavigatorBean extends BaseComponentBean

{
  private RcorePropertyTreeModelImpl model = null;
  private RcorePropertyElement   selectedNode = null;
  private int                   tableRows = 25;
  private int                   tableFetchSize = 25;
  private static final boolean  _isDebug = false;
  private static Map<String,Object> debugAttrsMap = null;
  private RichTable tableData;
  private RichTree tree;
  private RowKeySet treeSelectionRowKeySet = null;
  //private boolean isRenderedBtnSelectValues=false;
  private MethodExpression onInit=null; //+
  private MethodExpression onSelectInTreeListener=null; //+
  private MethodExpression onSelectInTableListener=null;
  private MethodExpression onSelectValues=null; //+
  private MethodExpression onClearSelected=null;
  private List<String> nodeStampInTableListId=null;
  static 
  {
    if (isDebug()) 
    {
      debugAttrsMap = new HashMap<String,Object>();
      debugAttrsMap.put("#{attrs.xmlResource}", "/com/rcore/component/treenavigator/AddressPropertyTreeModel.xml");
      debugAttrsMap.put("#{attrs.tableRows}", "25");
      debugAttrsMap.put("#{attrs.tableFetchSize}", "25");
      debugAttrsMap.put("#{attrs.visualNodesAsList}", true);
      debugAttrsMap.put("#{attrs.initLevel}", "0,0");
      debugAttrsMap.put("#{attrs.nodeStampInTableListId}", "");
    }
  }
  
  //-----------------------------------------
  @Override
  public void initDelegateBean() 
  {
    //super.initBean();
    //-----------------------------------------------------------
    Object xmlResource = resoleAttrsExpression("#{attrs.xmlResource}"); //(String)JSFUtils.resolveExpression("#{attrs.xmlResource}");
    String nodeStampInTableListId = (String)resoleAttrsExpression("#{attrs.nodeStampInTableListId}");
    if (!StringFunc.isEmpty(nodeStampInTableListId)) 
    {
      this.nodeStampInTableListId = Token.aTokenList(nodeStampInTableListId, " ,;");
    }
    String sTableRows = (String)resoleAttrsExpression("#{attrs.tableRows}"); //(String)JSFUtils.resolveExpression("#{attrs.tableRows}");
    if (sTableRows != null) 
    {
      try {
        tableRows = Integer.parseInt(sTableRows);
      }
      catch(Exception ex) 
      {
        ex.printStackTrace();
      }
    }
    String sTableFetchSize = (String)resoleAttrsExpression("#{attrs.tableFetchSize}"); // (String)JSFUtils.resolveExpression("#{attrs.tableFetchSize}");
    if (sTableFetchSize != null) 
    {
      try {
        tableFetchSize = Integer.parseInt(sTableFetchSize);
      }
      catch(Exception ex) 
      {
        ex.printStackTrace();
      }
    }
    //---
    onInit                 =(MethodExpression)resoleAttrsExpression("#{attrs.onInit}");
    onSelectInTreeListener =(MethodExpression)resoleAttrsExpression("#{attrs.onSelectInTreeListener}");
    onSelectInTableListener=(MethodExpression)resoleAttrsExpression("#{attrs.onSelectInTableListener}");
    onSelectValues         =(MethodExpression)resoleAttrsExpression("#{attrs.onSelectValues}");
    onClearSelected        =(MethodExpression)resoleAttrsExpression("#{attrs.onClearSelected}");
    //System.out.println("@@onSelectValues = "+onSelectValues.getClass().getName());
    //---
    try {
      RcorePropertyElement root = RcorePropertyTreeModelImpl.getRootElementByResource(xmlResource);
      model = new RcorePropertyTreeModelImpl(root);
      model.setBean(this);
      model.setTableFetchSize(tableFetchSize);
      root.setModel(model);
      boolean isVisualNodesAsList = getIsVisiualChildsAsList();
      model.setIsVisualChildsAsList(isVisualNodesAsList);
      //--------------------------------------------------------
      List<Node> itemList = XmlDoc.getElementsDown(root, "item");
      for(Node node : itemList) 
      {
        RcorePropertyElement element = (RcorePropertyElement)node;
        element.getTableModel();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    //---
    treeSelectionRowKeySet = new RowKeySetImpl(); // [[0,1]]
    List<Integer> l0 = new ArrayList<Integer>();
    //l0.add(0);l0.add(0);
    String initLevel = (String)resoleAttrsExpression("#{attrs.initLevel}"); //(String)JSFUtils.resolveExpression("#{attrs.xmlResource}");
    //System.out.println("@initLevel="+initLevel);
    setLevel(l0,initLevel); //l0.add();  //l0.add(0);
    treeSelectionRowKeySet.add(l0);
    List<Integer> listIndexes =  l0; //(List<Integer>)keyList[0];
    RcorePropertyElement selectedNode = getElementByIndexes(model,listIndexes);
    setSelectedNode(selectedNode);
    //---
    fetchUserOnInit();
  }
  
  private void fetchUserOnInit() 
  {
    if (onInit != null) 
    {
      onInit.invoke(FacesContext.getCurrentInstance().getELContext(), 
                    new Object[]{getModel().getRoot()} );
    }
  }
  
  @Override
  public void destroyDelegateBean() 
  {
    model.destroy();
    //super.destroyBean();
  }
  //-----------------------------------------
  public List<Node> getAllNodesItem() 
  {
    RcorePropertyElement root = model.getRoot();
    List<Node> rc = XmlDoc.getElementsDown(root, "item");
    return rc;
  }
  //-----------------------------------------
  public Object getSimpleTableModel() 
  {
    String tableOrView = "T_HOUSE";
    String sql = "SELECT $idAttribute id ,$captionAttribute sname FROM $tableOrView WHERE rownum < 100";
    sql = sql.replace("$idAttribute", "HOUSE_ID");
    sql = sql.replace("$captionAttribute", "SNAME");
    sql = sql.replace("$tableOrView", tableOrView);
    List<DbRecord> dbList = null;
    try {
      Connection conn = ConnectionFactory.getConnection("java:comp/env/jdbc/Realty3dbDS");
      ResultSet rs = AdfJdbcUtils.getInstance().executeQuery(sql, conn);
      dbList = AdfJdbcUtils.resultSet2DbRecord(rs);
      rs.close();
      conn.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return dbList;
  }
  public TreeNavigatorBean()
  {
    super();
  }
  
  public RcorePropertyTreeModelImpl getModel() 
  {
    return model;
  }
  public RcorePropertyElement getSelectedNode() 
  {
    return this.selectedNode;
  }

  public void setSelectedNode(RcorePropertyElement currentNode)
  {
    this.selectedNode = currentNode;
  }
  
  public boolean getIsRenderedTable() 
  {
    RcorePropertyElement selectedNode = getSelectedNode();
    if (selectedNode == null) 
    {
      return false;
    }
    return selectedNode.getIsRenderedTable();
    //return true;
  }
  
  public Object getTableModel() 
  {
    
    if (selectedNode == null) 
    {
      List<DbRecord> rc = null;
      rc = new ArrayList<DbRecord>();
      DbRecord rec = new DbRecord();
      rec.put("id", "");
      rec.put("sname", "");
      rc.add(rec);
      return rc;
    }
    return selectedNode.getTableModel();
  }
  
  public String getSimpleHeader() 
  {
    if (selectedNode == null) 
    {
      return "";
    }
    return selectedNode.getSimpleHeader();
  }
  
  public void treeSelectionListener(SelectionEvent selectionEvent)
  {
    RichTree tree = (RichTree)selectionEvent.getSource();
    RowKeySet keySet = tree.getSelectedRowKeys();
    treeSelectionListener(tree,keySet);
  }
  
  private void treeSelectionListener(RichTree tree,RowKeySet keySet) 
  {
    Object[] keyList = keySet.toArray(); // [[0,0,0]]
    List<Integer> listIndexes = (List<Integer>)keyList[0];
    RcorePropertyElement selectedNode = getElementByIndexes(model,listIndexes);

    if (selectedNode != null && selectedNode.getNodeName().equals("items"))
    {
      return;
    }
    setSelectedNode(selectedNode);
    //---------------------------
    treeSelectionRowKeySet = keySet; //selectionEvent.getAddedSet();
    //---------------------------
    
    RowKeySet rowKeySet = getTableRowKeySet();
    if (rowKeySet != null) {
      tableData.setSelectedRowKeys(rowKeySet);
    }
    else 
    {
      return;
    }
    //-----------------------------------------------
    manageDetailStampFacet();
    //-----------------------------------------------
    fetchUserSelectInTreeListener();
  }
  
  private void manageDetailStampFacet() 
  {
    if (!isDetailStampAvalable()) 
    {
      tableData.getFacets().remove("detailStamp");
    }
    else 
    {
      if (detailStampFacet != null) {
        if (!tableData.getFacets().containsKey("detailStamp")) {
          tableData.getFacets().put("detailStamp", detailStampFacet);
        }
      }
    }
  }
  
  private boolean isDetailStampAvalable()
  {
    boolean rc = false;
    if (selectedNode != null) {
      if (nodeStampInTableListId == null) 
      {
        rc = (detailStampFacet != null);
      }
      else {
        rc = nodeStampInTableListId.contains(selectedNode.getAttribute("id"));
      }
    }
    return rc;
  }
  
  private void fetchUserSelectInTreeListener() 
  {
    MethodExpression met = getOnSelectInTreeListener();
    if (met == null) 
    {
      return;
    }
    RcorePropertyElement selectedNode = getSelectedNode();
    if (selectedNode == null) 
    {
      return;
    }
    String dbId = selectedNode.getDbIdValue();
    //String captionInTree = selectedNode.getSimpleHeader();
    String captionInTable = selectedNode.getDbCaptionValue();
    Object[] params = 
    {
      selectedNode,
      dbId, 
      captionInTable
    };
    Boolean isRefreshCurrentRecord =
      (Boolean)met.invoke(FacesContext.getCurrentInstance().getELContext(),params);
    if ((isRefreshCurrentRecord != null) && isRefreshCurrentRecord) 
    {
      selectedNode.refreshLevel(true);
    }
  }
  
  private static RcorePropertyElement getElementByIndexes(RcorePropertyTreeModelImpl model,List<Integer> listIndexes)
  {
    RcorePropertyElement root = model.getRoot();
    RcorePropertyElement rc = null;
    if (listIndexes.size()==1) 
    {
      return root;
    }
    rc = getElementByIndexes(root,listIndexes,1);
    return rc;
  }

  private static RcorePropertyElement getElementByIndexes(RcorePropertyElement root,
                                                         List<Integer> listIndexes,
                                                         int curentIndex)
  {
    int index = -1;
    try {
      index = listIndexes.get(curentIndex);
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
      System.out.println();
    }
    List<RcorePropertyElement> list = root.getVisualChilds();
    RcorePropertyElement rc = null;
    try {
      rc = list.get(index);
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    if (rc == null) 
    {
      return rc;
    }
    if (listIndexes.size()==(curentIndex+1)) 
    {
      return rc;
    }
    rc = getElementByIndexes(rc,listIndexes,++curentIndex);
    return rc;
  }


  private boolean getIsVisiualChildsAsList()
  {
    Object obj = resoleAttrsExpression("#{attrs.visualNodesAsList}"); // JSFUtils.resolveExpression("#{attrs.visualNodesAsList}");
    if (obj == null) 
    {
      return true;
    }
    String s = obj.toString();
    if ("true".equals(s)) 
    {
      return true;
    }
    return false;
  }

  public int getTableRows()
  {
    return tableRows;
  }

  public int getTableFetchSize()
  {
    return tableFetchSize;
  }
  
  public void tableSelectionListener(SelectionEvent selectionEvent)
  {
    if (selectedNode != null) 
    {
      selectedNode.tableSelectionListener(selectionEvent);
      AdfFacesContext.getCurrentInstance().addPartialTarget(tree);
      //------
      fetchUserSelectInTableListener();
    }
  }
  
  private void fetchUserSelectInTableListener() 
  {
    MethodExpression met = getOnSelectInTableListener();
    if (met == null) 
    {
      return;
    }
    RcorePropertyElement selectedNode = getSelectedNode();
    if (selectedNode == null) 
    {
      return;
    }
    String dbId = selectedNode.getDbIdValue();
    String captionInTable = selectedNode.getDbCaptionValue();
    Object[] params = 
    {
      selectedNode,
      dbId, 
      captionInTable
    };
    Boolean isRefreshCurrentRecord =
      (Boolean)met.invoke(FacesContext.getCurrentInstance().getELContext(),params);
    if ((isRefreshCurrentRecord != null) && isRefreshCurrentRecord) 
    {
      selectedNode.refreshLevel(true);
    }
  }
  
  public boolean getIsRenderedMessage() 
  {
    if (selectedNode != null) 
    {
      return selectedNode.getIsRenderedMessage();
    }
    return false;
  }
  
  public String getMessageByBagInAfTable() 
  {
    if (selectedNode != null) 
    {
      return selectedNode.getMessageByBagInAfTable();
    }
    return null;
  }
  public RowKeySet getTableRowKeySet() 
  {
    RowKeySet rc = null;
    if (selectedNode != null) 
    {
      rc = selectedNode.getTableRowKeySet();
    }
    else 
    {
      rc = new RowKeySetImpl();
    }
    return rc;
  }
  
  public RowKeySet getTreeSelectionRowKeySet() 
  {
    return treeSelectionRowKeySet;
  }

  private Object resoleAttrsExpression(String expr)
  {
    Object rc = null;
    if (isDebug()) 
    {
      rc = debugAttrsMap.get(expr);
    }
    else 
    {
      rc = JSFUtils.resolveExpression(expr);
    }
    return rc;
  }
  private static boolean isDebug() 
  {
    return _isDebug;
  }

  private UIComponent detailStampFacet = null;		
  public void setTableData(RichTable tableData)
  {
    this.tableData = tableData;
    if (detailStampFacet == null) {
    	if (tableData != null) {
    		detailStampFacet = tableData.getFacet("detailStamp");
    	}
    }
    manageDetailStampFacet();
    this.tableData = tableData;
  }
  
  public RichTable getTableData()
  {
    return tableData;
  }
  
  public void setTree(RichTree tree)
  {
    this.tree = tree;
  }

  public RichTree getTree()
  {
    return tree;
  }
  public boolean getIsLeftPanelRendered() 
  {
    return true;
  }

  public String getValueFilterSname() 
  {
    if (selectedNode != null) 
    {
      return selectedNode.getValueFilterSname();
    }
    else 
    {
      return null;  
    }
    
  }
  public void setValueFilterSname(String value) 
  {
    if (selectedNode != null) 
    {
      selectedNode.setValueFilterSname(value);
    }
  }
  
  public String getValueFilterId() 
  {
    if (selectedNode != null) 
    {
      return selectedNode.getValueFilterId();
    }
    else 
    {
      return null;  
    }
  }
  public void setValueFilterId(String value) 
  {
    if (selectedNode != null) 
    {
      selectedNode.setValueFilterId(value);
    }
  }

  public void filterSnameValueChangeListener(ValueChangeEvent valueChangeEvent)
  {
    if (selectedNode != null) 
    {
      selectedNode.filterSnameValueChangeListener(valueChangeEvent);
      tableData.setSelectedRowKeys(null);
      AdfFacesContext.getCurrentInstance().addPartialTarget(tree);
      AdfFacesContext.getCurrentInstance().addPartialTarget(tableData);
    }
  }
  public void filterIdValueChangeListener(ValueChangeEvent valueChangeEvent)
  {
    if (selectedNode != null) 
    {
      selectedNode.filterIdValueChangeListener(valueChangeEvent);
      tableData.setSelectedRowKeys(null);
      AdfFacesContext.getCurrentInstance().addPartialTarget(tree);
      AdfFacesContext.getCurrentInstance().addPartialTarget(tableData);
    }
  }
  
  public String removeSelectForTable() 
  {
    if (selectedNode != null) 
    {
      selectedNode.removeSelectForTable();
      tableData.setSelectedRowKeys(new RowKeySetImpl());
      AdfFacesContext.getCurrentInstance().addPartialTarget(tree);
      AdfFacesContext.getCurrentInstance().addPartialTarget(tableData);
      //-----
      fetchUserClearSelected();
    }
    return null;
  }
  
  public void setSelectForTable()
  {
    // TODO
    if (selectedNode != null)
    {
      selectedNode.setSelectForTable();
      //tableData.setSelectedRowKeys(new RowKeySetImpl());
      AdfFacesContext.getCurrentInstance().addPartialTarget(tree);
      AdfFacesContext.getCurrentInstance().addPartialTarget(tableData);
      //-----
      fetchUserClearSelected();
    }
  }

  private void fetchUserClearSelected() 
  {
    if (onClearSelected != null) 
    {
      onClearSelected.invoke(FacesContext.getCurrentInstance().getELContext(), 
                             new Object[]{selectedNode} ); 
    }
  }

  private static void setLevel(List<Integer> list, String initLevel)
  {
    String[] tok = initLevel.split(",");
    for(String s : tok) 
    {
      list.add(Integer.parseInt(s));
    }
    return;
  }
  public static void main(String[] args)
  {
    List<Integer> list = new ArrayList<Integer>();
    setLevel(list, "0,0,0,0");
    System.out.println(list);
  }
  public String getStrXmlDocument() 
  {
    StringBuffer rc = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    rc.append(XmlDoc.print(getModel().getRoot()));
    return rc.toString();
  }
  
  public String getChangeViewText() 
  {
    boolean isVisualNodesAsList = getModel().getVisualNodesAsList();
    String rc = null;
    if (isVisualNodesAsList) 
    {
      rc = "Установить иерархию";
    }
    else 
    {
      rc = "Изображать как список";
    }
    return rc;
  }
  public String changeView() 
  {
    boolean isVisualNodesAsList = getModel().getVisualNodesAsList();
    isVisualNodesAsList = !isVisualNodesAsList;
    getModel().setIsVisualChildsAsList(isVisualNodesAsList);
    List<Node> itemList = XmlDoc.getElementsDown(getModel().getRoot(), "item");
    for(Node node : itemList) 
    {
      RcorePropertyElement element = (RcorePropertyElement)node;
      element.getTableModel();
    }
    return null;
  }
  
  public String refreshLevel() 
  {
    if (selectedNode == null) 
    {
      ;
    }
    else 
    {
      selectedNode.refreshLevel(true);
    }
    return null;
  }
  public String refreshAllLevels() 
  {
    List<Node> list = XmlDoc.getElementsDown(getModel().getRoot(),"item");
    for(Node node : list) 
    {
      ((RcorePropertyElement)node).refreshLevel(false);
    }
    return null;
  }
  public boolean getIsRenderedBtnSelectValues() 
  {
    return (this.onSelectValues != null);
  }
  public int getHeightBtnSelectValues() 
  {
    if (getIsRenderedBtnSelectValues()) 
    {
      return 24;
    }
    else 
    {
      return 0;
    }
  }
  
  public String actOnSelectValues()
  {
    MethodExpression met = getOnSelectValues();
    if (met != null) {
      String message =
        (String)met.invoke(FacesContext.getCurrentInstance().getELContext(),
                           new Object[] { getModel().getRoot(), RcorePropertyTreeModelImpl.ATTRIBUTE_ID_VALUE, RcorePropertyTreeModelImpl.ATTRIBUTE_CAPTION_VALUE });
      if (message != null) {
        StringBuffer js = new StringBuffer();
        js.append("window.alert('").append(StringFunc.replace(message, "'",
                                                              "\\'")).append("');");
        javaScriptEvaluate(js.toString());
      }
    }
    return null;
  }

  public MethodExpression getOnSelectInTreeListener()
  {
    return onSelectInTreeListener;
  }

  public MethodExpression getOnSelectInTableListener()
  {
    return onSelectInTableListener;
  }

  public MethodExpression getOnSelectValues()
  {
    return onSelectValues;
  }
}
