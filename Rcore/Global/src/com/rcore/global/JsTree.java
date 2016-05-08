package com.rcore.global;
// ШМЯ

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.xml.tree.XmlDocument;

import com.rcore.global.xml.XmlDoc;

public final class JsTree
{
  public static String getJsArrayTree(ResultSet rs, JsTreeParams params)
  {
    String idName = params.idName;
    String idParentName = params.idParentName;
    Element root = getXmlElementRoot(rs, idName, idParentName);

    StringBuffer rc = new StringBuffer("");
    fillJsArrayTreeRoot(rc, root, params);
    return rc.toString();
  }

  private static void fillJsArrayTreeRoot(StringBuffer rc, Element root, 
                                          JsTreeParams params)
  {
    rc.append("[");
    NodeList childs = root.getChildNodes();
    if (childs != null)
    {
      int len = childs.getLength();
      for (int i = 0; i < len; i++)
      {
        Element n = (Element) childs.item(i);
        fillJsArrayTreeItem(rc, n, params, (i == len - 1));
      }
    }
    rc.append("\n]");
  }

  private static void fillJsArrayTreeItem(StringBuffer buf, Element parent, 
                                          JsTreeParams params, 
                                          boolean isLastChild)
  {
    NodeList childs = parent.getChildNodes();
    fillJsArrayTreeForOneHeader(buf, parent, params, isLastChild);
    if (childs != null)
    {
      int len = childs.getLength();
      for (int i = 0; i < len; i++)
      {
        Element n = (Element) childs.item(i);
        fillJsArrayTreeItem(buf, n, params, (i == len - 1));
      }
    }
    fillJsArrayTreeForOneFooter(buf, parent, params, isLastChild);
  } // func

  private static void fillJsArrayTreeForOneFooter(StringBuffer buf, 
                                                  Element parent, 
                                                  JsTreeParams params, 
                                                  boolean isLastChild)
  {
    NodeList childs = parent.getChildNodes();
    if (childs == null || childs.getLength() == 0)
    {
      buf.append("]");
    }
    else
    {
      String indent = getIndent(parent);
      buf.append("\n").append(indent).append("]");
    }
    if (!isLastChild)
    {
      buf.append(",");
    }
  }

  /**
   * optional ID; caption; URL (or "null"); name of the target frame (or
   * "null") optional format specifier (COOLjsTree Professional only) , Or
   * Array Of Childs
   */
  private static

  void fillJsArrayTreeForOneHeader(StringBuffer buf, Element item, 
                                   JsTreeParams params, 
                                   boolean isLastChild)
  {
    NodeList childs = item.getChildNodes();
    String idValue = item.getAttribute(params.idName);
    String indent = getIndent(item);
    buf.append("\n").append(indent).append("[");
    buf.append("{id:").append(idValue).append("},");
    if (params.captionName != null)
    {
      buf.append("'").append(item.getAttribute(params.captionName)).append("',");
    }
    else
    {
      buf.append("null,");
    }

    StringByElementConstructor url = params.url;
    if (url == null)
    {
      buf.append("null");
    }
    else
    {
      String urlCaption = url.createString(item);
      if (urlCaption != null)
      {
        buf.append("'").append(urlCaption).append("'");
      }
      else
      {
        buf.append("null");
      }
    }
    if (!params.buildFrameName)
    {
      buf.append(",null");
    }
    else
    {
      buf.append(",'fr_").append(item.getAttribute(params.idName)).append("'");
    }
    if (childs == null || childs.getLength() == 0)
    {
      ;
    }
    else
    {
      buf.append(",");
    }
  }

  private static String getIndent(Element item)
  {
    int level = XmlDoc.getLevelNode(item);
    String rc = StringFunc.replicate(" ", level * 2);
    return rc;
  }

  public static Element getXmlElementRoot(ResultSet rs, String idName, 
                                          String idParentName)
  {
    XmlDocument doc = new XmlDocument();
    Element rc = doc.createElement("root");
    doc.appendChild(rc);
    Map<Integer, Element> mapIdElement = new HashMap<Integer, Element>();
    ResultSetMetaData md = null;
    try
    {
      md = rs.getMetaData();
      List<String> columns = new ArrayList<String>();
      int count = md.getColumnCount();
      for (int i = 1; i <= count; i++)
      {
        String columnName = md.getColumnName(i);
        columns.add(columnName);
      }
      while (rs.next())
      {
        int id = rs.getInt(idName);
        Element item = doc.createElement("item");
        for (String col: columns)
        {
          item.setAttribute(col, rs.getString(col));
        }
        mapIdElement.put(id, item);
      } // while
      Set<Integer> idSet = mapIdElement.keySet();
      for (Integer id: idSet)
      {
        Element item = mapIdElement.get(id);
        String parentId = item.getAttribute(idParentName);
        if (StringFunc.isEmpty(parentId) || parentId.equals("0"))
        {
          rc.appendChild(item);
        }
        else
        {
          int parentIdInt = Integer.parseInt(parentId);
          Element parentElement = mapIdElement.get(parentIdInt);
          if (parentElement != null)
          {
            parentElement.appendChild(item);
          }
          else
          {
            rc.appendChild(item);
          }
        }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return rc;
  }

  public static void main(String[] args)
  {

    test1();
    System.out.println("*********************");
    test2();
    System.out.println("*********************");
    test3();
    System.out.println("*********************");
    test4();
    System.out.println("*********************");
    test5();
    System.out.println("*********************");
    test6();
  }

  private static void test6()
  {
    XmlDocument doc = new XmlDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);

    Element item1 = doc.createElement("item");
    root.appendChild(item1);
    item1.setAttribute("id", "1");
    item1.setAttribute("id_parent", "0");

    Element item2 = doc.createElement("item");
    root.appendChild(item2);
    item2.setAttribute("id", "2");
    item2.setAttribute("id_parent", "0");

    Element item21 = doc.createElement("item");
    item2.appendChild(item21);
    item21.setAttribute("id", "21");
    item21.setAttribute("id_parent", "2");

    Element item22 = doc.createElement("item");
    item2.appendChild(item22);
    item22.setAttribute("id", "22");
    item22.setAttribute("id_parent", "2");

    Element item11 = doc.createElement("item");
    item1.appendChild(item11);
    item11.setAttribute("id", "11");
    item11.setAttribute("id_parent", "1");

    Element item12 = doc.createElement("item");
    item1.appendChild(item12);
    item12.setAttribute("id", "12");
    item12.setAttribute("id_parent", "1");

    // ----------------------------------
    StringBuffer rc = new StringBuffer();
    JsTreeParams params = new JsTreeParams();
    fillJsArrayTreeRoot(rc, root, params);
    System.out.println(rc.toString());

  }

  private static void test5()
  {
    XmlDocument doc = new XmlDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);

    Element item1 = doc.createElement("item");
    root.appendChild(item1);
    item1.setAttribute("id", "1");
    item1.setAttribute("id_parent", "0");

    Element item2 = doc.createElement("item");
    root.appendChild(item2);
    item2.setAttribute("id", "2");
    item2.setAttribute("id_parent", "0");

    Element item21 = doc.createElement("item");
    item1.appendChild(item21);
    item21.setAttribute("id", "21");
    item21.setAttribute("id_parent", "1");

    Element item22 = doc.createElement("item");
    item1.appendChild(item22);
    item22.setAttribute("id", "22");
    item22.setAttribute("id_parent", "1");

    // ----------------------------------
    StringBuffer rc = new StringBuffer();
    JsTreeParams params = new JsTreeParams();
    fillJsArrayTreeRoot(rc, root, params);
    System.out.println(rc.toString());

  }

  private static void test4()
  {
    XmlDocument doc = new XmlDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);

    Element item = doc.createElement("item");
    root.appendChild(item);
    item.setAttribute("id", "1");
    item.setAttribute("id_parent", "0");

    Element item21 = doc.createElement("item");
    item.appendChild(item21);
    item21.setAttribute("id", "2");
    item21.setAttribute("id_parent", "1");

    Element item22 = doc.createElement("item");
    item.appendChild(item22);
    item22.setAttribute("id", "3");
    item22.setAttribute("id_parent", "1");

    // ----------------------------------
    StringBuffer rc = new StringBuffer();
    JsTreeParams params = new JsTreeParams();
    fillJsArrayTreeRoot(rc, root, params);
    System.out.println(rc.toString());

  }

  private static void test3()
  {
    XmlDocument doc = new XmlDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);

    Element item = doc.createElement("item");
    root.appendChild(item);
    item.setAttribute("id", "1");
    item.setAttribute("id_parent", "0");

    Element item2 = doc.createElement("item");
    item.appendChild(item2);
    item.setAttribute("id", "2");
    item.setAttribute("id_parent", "1");

    // ----------------------------------
    StringBuffer rc = new StringBuffer();
    JsTreeParams params = new JsTreeParams();
    fillJsArrayTreeRoot(rc, root, params);
    System.out.println(rc.toString());
  }

  private static void test2()
  {
    XmlDocument doc = new XmlDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);

    Element item = doc.createElement("item");
    root.appendChild(item);
    item.setAttribute("id", "1");
    item.setAttribute("id_parent", "0");
    // ----------------------------------
    StringBuffer rc = new StringBuffer();
    JsTreeParams params = new JsTreeParams();
    fillJsArrayTreeRoot(rc, root, params);
    System.out.println(rc.toString());
  }

  private static void test1()
  {
    XmlDocument doc = new XmlDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);
    // ----------------------------------
    StringBuffer rc = new StringBuffer();
    JsTreeParams params = new JsTreeParams();
    fillJsArrayTreeRoot(rc, root, params);
    System.out.println(rc.toString());
  }
}
