package com.rcore.component.treenavigator;

import com.rcore.global.xml.XmlDoc;

import com.sun.xml.tree.ElementNode;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class RcoreElementNode extends ElementNode
{
  public RcoreElementNode()
  {
    super();
  }
  public RcoreElementNode(String tagName)
  {
    super();
    setTag(tagName);
  }
  
  public List<RcoreElementNode> getChilds()
  {
    List<Node> childNodes = XmlDoc.getChilds(this);
    List<RcoreElementNode> rc = new ArrayList<RcoreElementNode>();
    for(Node n : childNodes) 
    {
      if (n instanceof RcoreElementNode) 
      {
        rc.add((RcoreElementNode) n);
      }
    } // for
    return rc;
  } // public List<ElementNodeImpl> getChilds()
  
//  public void setTag(String tag) 
//  {
//    super.setTag(tag);
//  }
  //======================================================
  public boolean isSupported(String string, String string1)
  {
    return false;
  }

  public String getNamespaceURI()
  {
    return null;
  }

  public boolean hasAttributes()
  {
    return false;
  }

  public String getBaseURI()
  {
    return null;
  }

  public short compareDocumentPosition(org.w3c.dom.Node node)
  {
    return 0;
  }

  public String getTextContent()
  {
    return null;
  }

  public void setTextContent(String string)
  {
  }

  public boolean isSameNode(org.w3c.dom.Node node)
  {
    return false;
  }

  public String lookupPrefix(String string)
  {
    return null;
  }

  public boolean isDefaultNamespace(String string)
  {
    return false;
  }

  public String lookupNamespaceURI(String string)
  {
    return null;
  }

  public boolean isEqualNode(org.w3c.dom.Node node)
  {
    return false;
  }

  public Object getFeature(String string, String string1)
  {
    return null;
  }

  public Object setUserData(String string, Object object,
                            UserDataHandler userDataHandler)
  {
    return null;
  }

  public Object getUserData(String string)
  {
    return null;
  }

  public String getAttributeNS(String string, String string1)
  {
    return null;
  }

  public void setAttributeNS(String string, String string1, String string2)
  {
  }

  public void removeAttributeNS(String string, String string1)
  {
  }

  public Attr getAttributeNodeNS(String string, String string1)
  {
    return null;
  }

  public Attr setAttributeNodeNS(Attr attr)
  {
    return null;
  }

  public NodeList getElementsByTagNameNS(String string, String string1)
  {
    return null;
  }

  public boolean hasAttribute(String string)
  {
    return false;
  }

  public boolean hasAttributeNS(String string, String string1)
  {
    return false;
  }

  public TypeInfo getSchemaTypeInfo()
  {
    return null;
  }

  public void setIdAttribute(String string, boolean b)
  {
  }

  public void setIdAttributeNS(String string, String string1, boolean b)
  {
  }

  public void setIdAttributeNode(Attr attr, boolean b)
  {
  }
}
