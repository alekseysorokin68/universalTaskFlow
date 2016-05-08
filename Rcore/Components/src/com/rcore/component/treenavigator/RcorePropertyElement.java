package com.rcore.component.treenavigator;


import com.rcore.global.Resource;
import com.rcore.global.StringFunc;
import com.rcore.global.xml.XmlDoc;
import com.rcore.model.dynamic_list.ConnectionInterface;
import com.rcore.model.dynamic_list.DbDynamicListImpl0;
import com.rcore.model.dynamic_list.DynamicListSignature;

import com.sun.xml.tree.ElementNode;
import com.sun.xml.tree.XmlDocument;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;


public class RcorePropertyElement extends RcoreElementNode
{
  private RcorePropertyTreeModelImpl model = null;
  private FilterInfo            filterInfo = new FilterInfo();
  private DynamicListSignature<Map<String, Object>>  dbDynamicList = null;
  private TableSelectedInfo     tableSelectedInfoPriv = null;
  private boolean               isFirstCreateDataModel = true;
  //------------------------------------------------
  public RcorePropertyElement()
  {
    super();
  }
  public RcorePropertyElement(String tagName)
  {
    super(tagName);
  }
  
  public boolean getIsRenderedTable() 
  {
    
    if ("item".equals(getNodeName())) {
      return true;
    }
    return false;
  }
  
    public List<RcorePropertyElement> getVisualChilds() 
    {
      RcorePropertyTreeModelImpl model = getModel();
      if (model != null) {
        if (model.getVisualNodesAsList()) 
        {
          return getVisualChildsAsList();
        }
        else 
        {
          return getVisualChildsNormal();
        }
      }
      else 
      {
        return getVisualChildsNormal();
      }
    }
    
    public List<RcorePropertyElement> getVisualChildsNormal() 
    {
      List<Node> childs = null;
      childs = XmlDoc.getChilds(this, "item");
      if (childs == null) 
      {
        return new ArrayList<RcorePropertyElement>();
      }
      List<RcorePropertyElement> rc = new ArrayList<RcorePropertyElement>();
      for(Node n : childs) 
      {
        rc.add((RcorePropertyElement)n);
      }
      return rc;
    }
    
    public List<RcorePropertyElement> getVisualChildsAsList() 
    {
      List<Node> childs = null;
      String nodeName = getNodeName();
      if ("items".equals(nodeName)) 
      {
        childs = XmlDoc.getNodesDown(this,"item");
      }
      else 
      {
        ;
      }
      if (childs == null) 
      {
        return new ArrayList<RcorePropertyElement>();
      }
      List<RcorePropertyElement> rc = new ArrayList<RcorePropertyElement>();
      for(Node n : childs) 
      {
        rc.add((RcorePropertyElement)n);
      }
      return rc;
    }
  
  public String getHeader() 
  {
    StringBuffer rc = new StringBuffer();
    rc.append(getSimpleHeader());
    if (getTableSelectedInfo() != null) 
    {
      String value = null;
      try {
        TableSelectedInfo tableSelectedInfo = getTableSelectedInfo();
        Map<String,Object> selectedRowData = tableSelectedInfo.getSelectedRowData();
        value = (String)selectedRowData.get("SNAME");
      }
      catch(Exception ex) 
      {
        ex.printStackTrace();
      }
      if (value != null) 
      {
        rc.append(" - ").append(value);
      }
    }
    return rc.toString();
  }
  
  @Override 
  public String getAttribute(String name) 
  {
    if (RcorePropertyTreeModelImpl.ATTRIBUTE_CAPTION_VALUE.equals(name)) 
    {
      return this.getDbCaptionValue();
    }
    else if (RcorePropertyTreeModelImpl.ATTRIBUTE_ID_VALUE.equals(name)) 
    {
      return this.getDbIdValue();
    }
    String rc = super.getAttribute(name);
    return rc;
  }
  
  
  public String getSimpleHeader() 
  {
    return getAttribute("header");
  }
  
//  public Object getSimpleTableModel() 
//  {
//    String nodeName = this.getNodeName();
//    if ("items".equals(nodeName)) 
//    {
//      List<DbRecord> rc = new ArrayList<DbRecord>();
//      DbRecord rec = new DbRecord();
//      rec.put("id", "");
//      rec.put("sname", "");
//      rc.add(rec);
//      return rc;
//    }
//    String tableOrView = getAttribute("tableOrView");
//    if (tableOrView == null) 
//    {
//      return null;
//    }
//    String sql = "SELECT $idAttribute id,$captionAttribute sname FROM $tableOrView WHERE rownum <= 25";
//    sql = sql.replace("$idAttribute", getAttribute("idAttribute"));
//    sql = sql.replace("$captionAttribute", getAttribute("captionAttribute"));
//    sql = sql.replace("$tableOrView", tableOrView);
//    List<DbRecord> dbList = null;
//    try {
//      Connection conn = getConnection();
//      ResultSet rs = AdfJdbcUtils.getInstance().executeQuery(sql, conn);
//      dbList = AdfJdbcUtils.resultSet2DbRecord(rs);
//      rs.close();
//      conn.close();
//    }
//    catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return dbList;
//  }

  private void setupByDefaultValue(DynamicListSignature<Map<String, Object>> dbDynamicList)
  {
    String defaultId = getAttribute("defaultId");
    if (!StringFunc.isEmpty(defaultId)) 
    {
      setTableSelectedInfo(null);
      for(int i = 0; i < dbDynamicList.size(); i++ )
      {
        Map<String,Object> rec = dbDynamicList.get(i);
        Object objValue = rec.get("ID");
        if (objValue == null) 
        {
          continue;
        }
        String sValue = objValue.toString();
        if (sValue.equals(defaultId)) 
        {
          RowKeySet rowKeySet = new RowKeySetImpl();
          rowKeySet.add(i);
          setTableSelectedInfo(new TableSelectedInfo(rowKeySet));
          break;
        }
      } // for
    }
  }

  void initElementRequrcive()
  {
    initElementNoRecurcive();
    List<Node> childs = XmlDoc.getChilds(this, "item");
    if (childs == null) 
    {
      return;
    }
    for(Node n : childs) 
    {
      RcorePropertyElement e = (RcorePropertyElement)n;
      e.initElementRequrcive();
    }
  }

  private void initElementNoRecurcive()
  {
    // ���������� id_ref_def_item:
    String id_ref_def_item = getAttribute("id_ref_def_item");
    if (StringFunc.isEmpty(id_ref_def_item)) 
    {
      return;
    }
    Element def_item = XmlDoc.getFirstElementByAttList(getRoot(), "def_item", "id", id_ref_def_item);
    if (def_item == null) 
    {
      return;
    }
    List<Node> attrs = XmlDoc.getAttributes(def_item);
    for(Node att : attrs) 
    {
      String name = att.getNodeName();
      String valueOld = getAttribute(name);
      if (!StringFunc.isEmpty(valueOld)) 
      {
        continue; // ������� ���������� � ��� �������� ����� ������������
      }
      setAttribute(name, att.getNodeValue()); // �������� �� �����������
    } // for
    // ������ id_ref_def_item - �� ��� ���������
    removeAttribute("id_ref_def_item");
    //XmlDoc.removeChild(def_item); ��� ������ !!
  }

  private void setTableSelectedInfo(RcorePropertyElement.TableSelectedInfo tableSelectedInfoPriv)
  {
    this.tableSelectedInfoPriv = tableSelectedInfoPriv;
  }

  private RcorePropertyElement.TableSelectedInfo getTableSelectedInfo()
  {
    return this.tableSelectedInfoPriv;
  }

  private class TableSelectedInfo 
  {
    RowKeySet          selectedRowKeys = null;
    TableSelectedInfo(RowKeySet selectedRowKeys) 
    {
      super();
      this.selectedRowKeys = selectedRowKeys;
    }
    private Map<String,Object> getSelectedRowData() 
    {
      Map<String,Object> rc = null;
      if (this.selectedRowKeys == null) 
      {
        return rc;
      }
      try {
        int index = (this.selectedRowKeys.toArray(new Integer[this.selectedRowKeys.size()]))[0];
        rc = getTableModel().get(index);
      }
      catch(Exception e) 
      {
        e.printStackTrace();
      }
      return rc;
    }
    @Override
    public String toString() 
    {
      StringBuffer rc = new StringBuffer();
      rc.append("@TableSelectedInfo = {selectedRowData =").append(getSelectedRowData()).append(" ; selectedRowKeys ="+selectedRowKeys);
      return rc.toString();
    }
  }
  
  public boolean isRoot() 
  {
    Node parent = getParentNode();
    boolean rc = false;
    if (parent == null) 
    {
      rc = true;
    }
    else 
    {
      if (!(parent instanceof RcorePropertyElement)) 
      {
        rc = true;
      }
    }
    return rc;
  }
  
  public boolean isFirstChild() 
  {
    Node parent = getParentNode();
    if (parent == null) 
    {
      return false;
    }
    if (!(parent instanceof RcorePropertyElement)) 
    {
      return false;
    }
    RcorePropertyElement parentElement = (RcorePropertyElement)parent;
    if (parentElement.equals(getRoot())) 
    {
      return false;
    }
    if (this.equals(parentElement.getFirstChild())) 
    {
      return true;
    }
    else 
    {
      return false;
    }
  }
  
  public DynamicListSignature<Map<String, Object>> getTableModel()
  {
    String signatureDbDate = getSignatureDbDate();
    try {
      if (dbDynamicList == null) {
        dbDynamicList = createTableModel(signatureDbDate);
      }
      else {
        if (!(signatureDbDate.equals(dbDynamicList.getSignature()))) {
          dbDynamicList = createTableModel(signatureDbDate);
        }
      }
      return dbDynamicList;
    }
    finally {
      //System.out.println("@size=" + dbDynamicList.size());
    }
  }
  
  private DbDynamicListImpl0 createTableModel(String signatureDbDate)
  {
    RcorePropertyTreeModelImpl model = getModel();
    //String parentDbIdValue = getParentDbIdValue();
    DbDynamicListImpl0 rc = null;
    rc = new DbDynamicListImpl0(
                      getTablesOrVies(),                  // String[] getAttribute("tableOrView"),        //String tableOrView,
                      getIdAttributes(),                  // String[] getAttribute("idAttribute"),        //String idAttribute,
                      getAttribute("captionAttribute"),   // String   captionAttribute,
                      getIdParentAttributes(),            // String[] getAttribute("idParentAttribute"),  //String idParentAttribute,
                      getIdParentValues(),                // String[] parentDbIdValue,                    //String idParentValue,
                      getFilter(),                        // String filter,
                      model.getTableFetchSize(),          // int    rangeSize,
                      new ConnectionImpl(),               // ConnectionInterface connectionInterface
                      getAttribute("extendedAttributes")  // String 
      );
    setTableSelectedInfo(null);
    rc.setSignature(signatureDbDate);
    if (isFirstCreateDataModel) 
    {
      setupByDefaultValue(rc);
      isFirstCreateDataModel = false;
    }
    return rc;
  }
  
  private String[] getTablesOrVies() 
  {
    List<String> list = new ArrayList<String>();
    getTableOrView(list);
    Node parent = this;
    while(true) 
    {
      parent = parent.getParentNode();
      if (parent == null) 
      {
        break;
      }
      if (!(parent instanceof RcorePropertyElement)) 
      {
        break;
      }
      if (!parent.getNodeName().equals("item")) 
      {
        break;
      }
      RcorePropertyElement parentElement = (RcorePropertyElement)parent;
      parentElement.getTableOrView(list);
    } // while
    String[] rc = list.toArray(new String[list.size()]);
    return rc;
  }
  
  private void getTableOrView(List<String> list) 
  {
    list.add(getAttribute("tableOrView"));
  }
  
  private String[] getIdParentAttributes() 
  {
    List<String> list = new ArrayList<String>();
    getIdParentAttribute(list);
    Node parent = this;
    while(true) 
    {
      parent = parent.getParentNode();
      if (parent == null) 
      {
        break;
      }
      if (!(parent instanceof RcorePropertyElement)) 
      {
        break;
      }
      if (!parent.getNodeName().equals("item")) 
      {
        break;
      }
      RcorePropertyElement parentElement = (RcorePropertyElement)parent;
      parentElement.getIdParentAttribute(list);
    } // while
    String[] rc = list.toArray(new String[list.size()]);
    return rc;
  }
  
  private String[] getIdAttributes() 
  {
    List<String> list = new ArrayList<String>();
    getIdAttribute(list);
    Node parent = this;
    while(true) 
    {
      parent = parent.getParentNode();
      if (parent == null) 
      {
        break;
      }
      if (!(parent instanceof RcorePropertyElement)) 
      {
        break;
      }
      if (!parent.getNodeName().equals("item")) 
      {
        break;
      }
      RcorePropertyElement parentElement = (RcorePropertyElement)parent;
      parentElement.getIdAttribute(list);
    } // while
    String[] rc = list.toArray(new String[list.size()]);
    return rc;
  }
  
  private void getIdAttribute(List<String> list)
  {
    list.add(getAttribute("idAttribute"));
  }
  
  private void getIdParentAttribute(List<String> list) 
  {
    list.add(getAttribute("idParentAttribute"));
  }
  
  private String[] getIdParentValues() 
  {
    List<String> list = new ArrayList<String>();
    getIdParentValue(list);
    Node parent = this;
    while(true) 
    {
      parent = parent.getParentNode();
      if (parent == null) 
      {
        break;
      }
      if (!(parent instanceof RcorePropertyElement)) 
      {
        break;
      }
      if (!parent.getNodeName().equals("item")) 
      {
        break;
      }
      RcorePropertyElement parentElement = (RcorePropertyElement)parent;
      parentElement.getIdParentValue(list);
    } // while
    String[] rc = list.toArray(new String[list.size()]);
    return rc;
  }
  
  private void getIdParentValue(List<String> list) 
  {
    list.add(getParentDbIdValue());
  }
  
  private String getFilter() 
  {
    if (StringFunc.isEmpty(filterInfo.sname) && StringFunc.isEmpty(filterInfo.id)) 
    {
      return null;
    }
    String rc = null;
    if (!StringFunc.isEmpty(filterInfo.sname)) 
    {
      rc = "UPPER(${TABLE}.${SNAME}) LIKE '${VALUE}%'";
      rc = rc.replace("${TABLE}", getAttribute("tableOrView"));
      rc = rc.replace("${SNAME}", getAttribute("captionAttribute"));
      rc = rc.replace("${VALUE}", filterInfo.sname.toUpperCase());
    }
    else 
    {
      rc = "${TABLE}.${ID} = '${VALUE}'";
      rc = rc.replace("${TABLE}", getAttribute("tableOrView"));
      rc = rc.replace("${ID}", getAttribute("idAttribute"));
      rc = rc.replace("${VALUE}", filterInfo.id);
    }
    return rc;
  }
  
  public class ConnectionImpl implements ConnectionInterface 
  {

    @SuppressWarnings("compatibility:4265126949865725386")
    private static final long serialVersionUID = 1L;

    public Connection getConnection()
    {
      return getModel().getConnection();
    }

    public void closeConnection(Connection con) throws SQLException
    {
      con.close(); 
    }
  }
  
  public String getDbIdValue() 
  {
    if (getTableSelectedInfo() == null) 
    {
      return null;
    }
    Map<String,Object> row = null;
    try {
      row = getTableSelectedInfo().getSelectedRowData();
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    if (row == null) 
    {
      return null;
    }
    String idName = "ID"; // getAttribute("idAttribute");
    //String idName = getAttribute("idAttribute");
    String rc = (String)row.get(idName);
    
    return rc;
  }
  
  public String getDbCaptionValue() 
  {
    if (getTableSelectedInfo() == null) 
    {
      return null;
    }
    Map<String,Object> row = null;
    try {
      row = getTableSelectedInfo().getSelectedRowData();
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    if (row == null) 
    {
      return null;
    }
    String name = "SNAME"; // getAttribute("idAttribute");
    String rc = (String)row.get(name);
    
    return rc;
  }
  
  public String getParentDbIdValue() 
  {
    Node parent = getParentNode();
    if ((parent == null) || (!(parent instanceof RcorePropertyElement))) 
    {
      return null;
    }
    RcorePropertyElement parentElement = (RcorePropertyElement)parent;
    return parentElement.getDbIdValue();
  }
  
  private String getSignatureDbDate() 
  {
    StringBuffer rc = new StringBuffer();
    Node parent = this;
    while(true) 
    {
      if (parent == null) 
      {
        break;
      }
      if (!parent.getNodeName().equals("item")) 
      {
        break;
      }
      rc.append(((RcorePropertyElement)parent).getSignatureDbDateElement()).append("__");
      parent = parent.getParentNode();
    } // while(true)
    return rc.toString();
  }
  
  public void refreshLevel(boolean isSaveTableSelectedInfo) 
  {
    TableSelectedInfo tableSelectedInfo = this.getTableSelectedInfo();
    dbDynamicList = null;
    getTableModel();
    if (isSaveTableSelectedInfo) {
        setTableSelectedInfo(tableSelectedInfo);
    }
  }
  
  public String getSignatureDbDateElement() 
  {
    StringBuffer rc = new StringBuffer();
    rc.append(getParentDbIdValue()).append("@").append(getFilter());
    return rc.toString();
  }
  
  public void tableSelectionListener(SelectionEvent selectionEvent)
  {
    RichTable table = (RichTable)selectionEvent.getSource();
    //Map<String,Object> selectedRowData = (Map<String, Object>) table.getSelectedRowData();
    RowKeySet rowKeySet = table.getSelectedRowKeys();
    // table ведь физически одна и та же ===> rowKeySet - одно и то же для разных узлов поэтому при создании
    // TableSelectedInfo его обязательно нужно клонировать !
    setTableSelectedInfo(new TableSelectedInfo(rowKeySet.clone()));
    // -------------
    // Переинициализируем все нижнее поддерево
    reInitDataSubTree();
  }
  
  private void reInitDataSubTree() 
  {
    List<Node> list = XmlDoc.getElementsDown(this, "item");
    for(Node node : list) 
    {
      if (!this.equals(node)) {
        ((RcorePropertyElement)(node)).reInitData();
      }
    }
  }
  
  private void reInitData() 
  {
    dbDynamicList = null;
    setTableSelectedInfo(null);
  }
  public RowKeySet getTableRowKeySet() 
  {
    RowKeySet rc = null;
    if (getTableSelectedInfo() == null) 
    {
      return rc;
    }
    rc = getTableSelectedInfo().selectedRowKeys;
    return rc;
  }
  
  public boolean getIsRenderedMessage() 
  {
    return false;
  }
  public String getMessageByBagInAfTable() 
  {
    //�������� � ���� ������ ������� N �������, �� ������������ ������ 53024. ����������� �����.
    return null;
  }
  
  public String getCaptionAttribute() 
  {
    return getAttribute("captionAttribute");
  }
  public void setModel(RcorePropertyTreeModelImpl model)
  {
    this.model = model;
  }

  public RcorePropertyTreeModelImpl getModel()
  {
    RcorePropertyElement root = getRoot();
    return root.model;
  }
  public RcorePropertyElement getRoot() 
  {
    XmlDocument doc = (XmlDocument)this.getOwnerDocument();
    RcorePropertyElement rc = (RcorePropertyElement)doc.getDocumentElement();
    return rc;
  }
  public Connection getConnection() 
  {
    return getModel().getConnection();
  }
  //======================================
  public static void main(String[] args) throws IOException, SAXException
  {
      String str = Resource.getResourceString(Resource.class, 
                                                     "/com/rcore/view/AddressPropertyTreeModel.xml");
      Properties props = new Properties();
      props.put("*Element", ElementNode.class.getName());
      props.put("items", RcorePropertyElement.class.getName());
      props.put("item", RcorePropertyElement.class.getName());
      XmlDocument doc = XmlDoc.createXmlFromStringWithMaps(str, false, props);
      Element root = doc.getDocumentElement();
      System.out.println(root.getClass().getName());
  }
  
  private class FilterInfo 
  {
    String sname = null;
    String id = null;
  }
  
  public String getValueFilterSname() 
  {
    return filterInfo.sname;
  }
  public void setValueFilterSname(String value) 
  {
    filterInfo.sname = value;
  }
  
  public String getValueFilterId() 
  {
    return filterInfo.id;
  }
  public void setValueFilterId(String value) 
  {
    filterInfo.id = value;
  }
  
  public void filterSnameValueChangeListener(ValueChangeEvent valueChangeEvent)
  {
    String value = (String)valueChangeEvent.getNewValue();
    if (value != null && value.trim().length() > 0) 
    {
      filterInfo.id = null;
    }
    reInitData();
    reInitDataSubTree();
  }
  public void filterIdValueChangeListener(ValueChangeEvent valueChangeEvent)
  {
    String value = (String)valueChangeEvent.getNewValue();
    if (value != null && value.trim().length() > 0) 
    {
      filterInfo.sname = null;
    }
    reInitData();
    reInitDataSubTree();
  }
  
  public void removeSelectForTable() 
  {
    List<Node> list = XmlDoc.getElementsDown(this, "item");
    for(Node node : list) 
    {
       ((RcorePropertyElement)(node)).dbDynamicList = null;
       ((RcorePropertyElement)(node)).setTableSelectedInfo(null);
    }
  }
  
  public void setSelectForTable() 
  {
    DynamicListSignature tableModel = getTableModel();
    if (tableModel == null || tableModel.size()==0) 
    {
      return;
    }
    removeSelectForTable();
    getTableModel();
    
    TableSelectedInfo info = getTableSelectedInfo();
    if (info == null) 
    {
      info = new TableSelectedInfo(new RowKeySetImpl());
    }
    
    RowKeySet keySet = info.selectedRowKeys;
    if (keySet == null) 
    {
      keySet = new RowKeySetImpl();
    }
    if (keySet.size() == 0) 
    {
      keySet.add(0);
    }
    info.selectedRowKeys = keySet;
    setTableSelectedInfo(info);
    return;
  }  
}
