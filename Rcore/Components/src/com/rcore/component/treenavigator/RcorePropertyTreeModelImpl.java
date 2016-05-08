package com.rcore.component.treenavigator;


import com.rcore.global.Resource;
import com.rcore.global.xml.XmlDoc;

import com.rcore.model.dynamic_list.ConnectionFactory;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;

import org.xml.sax.SAXException;
import com.sun.xml.tree.XmlDocument;
import oracle.jbo.server.DataSourceContextFactory;




public class RcorePropertyTreeModelImpl extends ChildPropertyTreeModel implements RcorePropertyTreeModel
{
  public static final String ATTRIBUTE_ID_VALUE = "_ATTRIBUTE_ID_VALUE";
  public static final String ATTRIBUTE_CAPTION_VALUE = "_ATTRIBUTE_CAPTION_VALUE";
  //----
  private XmlDocument document = null;
  private RcorePropertyElement root = null;
  private boolean isVisualChildsAsList = true;
  private int tableFetchSize;
  private Object bean = null;

  public RcorePropertyTreeModelImpl(Object root)
  {
    super(root, "visualChilds");
    this.root = (RcorePropertyElement)root;
    this.root.initElementRequrcive();
  }

  @Override
  public void setDocument(XmlDocument document)
  {
    this.document = document;
  }

  @Override 
  public XmlDocument getDocument()
  {
    return document;
  }

  @Override /**
   * ��������������� �� ��������
   */
  public void destroy()
  {
    ;
  }

  
  //===============================================================
  public static RcorePropertyElement getRootElementByString(String str) throws IOException,
                                                                                SAXException
  {
    Properties props = new Properties();
    props.put("item", RcorePropertyElement.class.getName());
    props.put("items", RcorePropertyElement.class.getName());
    props.put("*Element", RcoreElementNode.class.getName());
    
    XmlDocument doc = XmlDoc.createXmlFromStringWithMaps(str, false, props);
    XmlDoc.normalize(doc);
    RcorePropertyElement rc = (RcorePropertyElement)doc.getDocumentElement();
    return rc;
  }
  public static RcorePropertyElement getRootElementByResource(Object res) throws IOException,
                                                                                SAXException
  {
    RcorePropertyElement rc = null;
    if (res instanceof String) {
      String str = Resource.getResourceString(Resource.class, (String)res);
      rc = getRootElementByString(str);
    }
    else if (res instanceof XmlDocument) 
    {
      rc = (RcorePropertyElement)(((XmlDocument)res).getDocumentElement());
    }
    return rc;
  }
  public static RcorePropertyElement getRootElementByResource() throws IOException,
                                                                      SAXException
  {
    String str = Resource.getResourceString(Resource.class,"/com/inec/rcore/model/inecPropertyTreeModel.xml");
    return getRootElementByString(str);
  }
  
  @Override 
  public Connection getConnection()
  {
    Connection rc = null;
    try {
      rc = ConnectionFactory.getConnection(getRoot().getAttribute("dsname"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return rc;
  }
  //===============================================================
  public static void main(String[] args) throws Exception
  {
    String dsName="java:comp/env/jdbc/Realty3dbDS";
    Connection rc = null;
    final Hashtable<String, String> tableJndiEnv = new Hashtable<String, String>();
    tableJndiEnv.put("java.naming.factory.initial",
                     DataSourceContextFactory.class.getName());
    final Context initContext = new InitialContext(tableJndiEnv);
    final DataSource pool = (DataSource)initContext.lookup(dsName);
    rc = pool.getConnection();
    System.out.println(rc);
    System.out.println("Exit");
  }

  public RcorePropertyElement getRoot()
  {
    return root;
  }

  public boolean getVisualNodesAsList()
  {
    return isVisualChildsAsList;
  }

  public void setIsVisualChildsAsList(boolean isVisualChildsAsList)
  {
    this.isVisualChildsAsList = isVisualChildsAsList;
  }

  public void setTableFetchSize(int tableFetchSize)
  {
    this.tableFetchSize = tableFetchSize;
  }

  public int getTableFetchSize()
  {
    return tableFetchSize;
  }

  public void setBean(Object bean)
  {
    this.bean = bean;
  }

  public Object getBean()
  {
    return bean;
  }
}
