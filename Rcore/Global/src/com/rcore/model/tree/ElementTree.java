package com.rcore.model.tree;


import com.sun.xml.tree.XmlDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class ElementTree implements Element
{
  private Element element;

  public ElementTree(Element element)
  {
    super();
    this.element = element;
  }
  public ElementTree(XmlDocument doc)
  {
    super();
    this.element = doc.getDocumentElement();
  }

  public String getTagName()
  {
    return element.getTagName();
  }

  public String getAttribute(String name)
  {
    return element.getAttribute(name);
  }

  public void setAttribute(String name, String value)
  {
    element.setAttribute(name, value);
    attruibutesMap = null;
  }

  public void removeAttribute(String name)
  {
    element.removeAttribute(name);
  }

  public Attr getAttributeNode(String name)
  {
    return element.getAttributeNode(name);
  }

  public Attr setAttributeNode(Attr newAttr)
  {
    attruibutesMap = null;
    return element.setAttributeNode(newAttr);
  }

  public Attr removeAttributeNode(Attr oldAttr)
  {
    return element.removeAttributeNode(oldAttr);
  }

  public NodeList getElementsByTagName(String name)
  {
    return element.getElementsByTagName(name);
  }

  public String getAttributeNS(String namespaceURI, String localName)
  {
    return element.getAttributeNS(namespaceURI, localName);
  }

  public void setAttributeNS(
      String namespaceURI, String qualifiedName, String value)
  {
    attruibutesMap = null;
    element.setAttributeNS(namespaceURI, qualifiedName, value);
  }

  public void removeAttributeNS(String namespaceURI, String localName)
  {
    element.removeAttributeNS(namespaceURI, localName);
  }

  public Attr getAttributeNodeNS(String namespaceURI, String localName)
  {
    return element.getAttributeNodeNS(namespaceURI, localName);
  }

  public Attr setAttributeNodeNS(Attr newAttr)
  {
    attruibutesMap = null;
    return element.setAttributeNodeNS(newAttr);
  }

  public NodeList getElementsByTagNameNS(String namespaceURI,
                                         String localName)
  {
    return element.getElementsByTagNameNS(namespaceURI, localName);
  }

  public boolean hasAttribute(String name)
  {
    return element.hasAttribute(name);
  }

  public boolean hasAttributeNS(String namespaceURI, String localName)
  {
    return element.hasAttributeNS(namespaceURI, localName);
  }

  public TypeInfo getSchemaTypeInfo()
  {
    return element.getSchemaTypeInfo();
  }

  public void setIdAttribute(String name, boolean isId)
  {
    element.setIdAttribute(name, isId);
  }

  public void setIdAttributeNS(String namespaceURI, String localName,
                               boolean isId)
  {
    element.setIdAttributeNS(namespaceURI, localName, isId);
  }

  public void setIdAttributeNode(Attr idAttr, boolean isId)
  {
    element.setIdAttributeNode(idAttr, isId);
  }

  public String getNodeName()
  {
    return element.getNodeName();
  }

  public String getNodeValue()
  {
    return element.getNodeValue();
  }

  public void setNodeValue(String nodeValue)
  {
    attruibutesMap = null;
    element.setNodeValue(nodeValue);
  }

  public short getNodeType()
  {
    return element.getNodeType();
  }

  public org.w3c.dom.Node getParentNode()
  {
    return element.getParentNode();
  }

  public NodeList getChildNodes()
  {
    return element.getChildNodes();
  }

  public org.w3c.dom.Node getFirstChild()
  {
    return element.getFirstChild();
  }

  public org.w3c.dom.Node getLastChild()
  {
    return element.getLastChild();
  }

  public org.w3c.dom.Node getPreviousSibling()
  {
    return element.getPreviousSibling();
  }

  public org.w3c.dom.Node getNextSibling()
  {
    return element.getNextSibling();
  }

  public NamedNodeMap getAttributes()
  {
    return element.getAttributes();
  }

  public Document getOwnerDocument()
  {
    return element.getOwnerDocument();
  }

  public org.w3c.dom.Node insertBefore(org.w3c.dom.Node newChild,
                                       org.w3c.dom.Node refChild)
  {
    return element.insertBefore(newChild, refChild);
  }

  public org.w3c.dom.Node replaceChild(org.w3c.dom.Node newChild,
                                       org.w3c.dom.Node oldChild)
  {
    return element.replaceChild(newChild, oldChild);
  }

  public org.w3c.dom.Node removeChild(org.w3c.dom.Node oldChild)
  {
    return element.removeChild(oldChild);
  }

  public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild)
  {
    return element.appendChild(newChild);
  }

  public boolean hasChildNodes()
  {
    return element.hasChildNodes();
  }

  public org.w3c.dom.Node cloneNode(boolean deep)
  {
    return new ElementTree((Element)element.cloneNode(deep));
  }

  public void normalize()
  {
    element.normalize();
  }

  public boolean isSupported(String feature, String version)
  {
    return element.isSupported(feature, version);
  }

  public String getNamespaceURI()
  {
    return element.getNamespaceURI();
  }

  public String getPrefix()
  {
    return element.getPrefix();
  }

  public void setPrefix(String prefix)
  {
    element.setPrefix(prefix);
  }

  public String getLocalName()
  {
    return element.getLocalName();
  }

  public boolean hasAttributes()
  {
    return element.hasAttributes();
  }

  public String getBaseURI()
  {
    return element.getBaseURI();
  }

  public short compareDocumentPosition(org.w3c.dom.Node other)
  {
    return element.compareDocumentPosition(other);
  }

  public String getTextContent()
  {
    return element.getTextContent();
  }

  public void setTextContent(String textContent)
  {
    element.setTextContent(textContent);
  }

  public boolean isSameNode(org.w3c.dom.Node other)
  {
    return element.isSameNode(other);
  }

  public String lookupPrefix(String namespaceURI)
  {
    return element.lookupPrefix(namespaceURI);
  }

  public boolean isDefaultNamespace(String namespaceURI)
  {
    return element.isDefaultNamespace(namespaceURI);
  }

  public String lookupNamespaceURI(String prefix)
  {
    return element.lookupNamespaceURI(prefix);
  }

  public boolean isEqualNode(org.w3c.dom.Node arg)
  {
    return element.isEqualNode(arg);
  }

  public Object getFeature(String feature, String version)
  {
    return element.getFeature(feature, version);
  }

  public Object setUserData(String key, Object data,
                            UserDataHandler handler)
  {
    return element.setUserData(key, data, handler);
  }

  public Object getUserData(String key)
  {
    return element.getUserData(key);
  }
  public List<ElementTree> getChildsList() 
  {
    List<ElementTree> rc = new ArrayList<ElementTree>();
    NodeList nodeList = element.getChildNodes();
    int size = nodeList.getLength();
    for (int i=0; i < size; i++) 
    {
      Node item = nodeList.item(i);
      if (item instanceof Element) 
      {
        rc.add((ElementTree)(item instanceof ElementTree ? item : new ElementTree((Element)item)));
      }
    }
    return rc;
  }
  private Map<String,String> attruibutesMap = null;
  public Map<String,String> getAttributesMap() 
  {
    if (attruibutesMap != null) 
    {
      return attruibutesMap;
    }
    refreshAttributesMap();
    return attruibutesMap;
  }
  private void refreshAttributesMap() 
  {
    NamedNodeMap nodeMap = getAttributes();
    int len = nodeMap.getLength();
    attruibutesMap = new HashMap<String,String>();
    for (int i=0; i < len; i++) 
    {
      Node attr = nodeMap.item(i);
      attruibutesMap.put(attr.getNodeName(), attr.getNodeValue());
    }
  }
}
