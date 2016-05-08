package com.rcore.test;


import com.rcore.model.tree.TreeModelByXml;

import com.sun.xml.tree.XmlDocument;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;


public class TestBean
{
  private XmlDocument doc = null;
  public TestBean()
  {
    super();
    doc = new XmlDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);
    Element child = doc.createElement("child");
    root.appendChild(child);
    root.setAttribute("caption", "root");
    child.setAttribute("caption", "child");
  }
  public Object getTreeRoot() 
  {
    List<Element> list = new ArrayList<Element>();
    list.add(doc.getDocumentElement());
    list.add(doc.getDocumentElement());
    
    return new TreeModelByXml(list);
  }
}
