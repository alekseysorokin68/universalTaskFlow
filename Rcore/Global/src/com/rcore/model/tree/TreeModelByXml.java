package com.rcore.model.tree;


import com.rcore.global.Resource;
import com.rcore.global.xml.XmlDoc;

import com.sun.xml.tree.XmlDocument;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;

import org.w3c.dom.Element;

import org.xml.sax.SAXException;

/**
<pre>
  Базовый класс модели - org.apache.myfaces.trinidad.model.ChildPropertyTreeModel.
  Пример использования ChildPropertyTreeModel:
  public class Person
  {
      private final String _name;
      private final List _kids = new ArrayList();
      
      public Person(String name)  {_name = name;}
      public String getName() { return _name;}
      public List getKids() { return _kids;}
  }
  
  public void main(String[] args) {
    Person john = new Person("John Smith");
      Person kim = new Person("Kim Smith");
      Person tom = new Person("Tom Smith");

    Person ira = new Person("Ira Wickrememsinghe");
      Person mallika = new Person("Mallika Wickremesinghe");
    //------------------------------------------------------
    john.getKids().add(kim);
    john.getKids().add(tom);
    
    ira.getKids().add(mallika);
    //-------------------------
    List<Person> people = new ArrayList<Person>();
    people.add(john);
    people.add(ira);
  }
  
  Пример использования TreeModelByXml:
  1) На уровне .jspx:
   &lt;af:tree  var="node" 
             value="#{viewScope.treeBean.treeRoot}" 
             id="t1" 
             summary="tree1"
             expandAllEnabled="true"
             initiallyExpanded="true"
>
     &lt;f:facet name="nodeStamp"&gt;
       &lt;af:panelGroupLayout id="pgl1"&gt;
         &lt;af:outputText value="#{node.attributesMap.caption}" id="ot1"/&gt;
       &lt;/af:panelGroupLayout&gt;
     &lt;/f:facet&gt;
   &lt;/af:tree&gt;

  2) На уровне .java:
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
</pre>
 */

public class TreeModelByXml extends ChildPropertyTreeModel
{
  public static final String CHILD_PROPERTY = "childsList";
  public TreeModelByXml(Element element)
  {
    super((element instanceof ElementTree ? element : new ElementTree(element)), 
          CHILD_PROPERTY);
  }
  public TreeModelByXml(XmlDocument document)
  {
    super((document.getDocumentElement() instanceof ElementTree ? 
            document.getDocumentElement() : new ElementTree(document.getDocumentElement())),
          CHILD_PROPERTY);
  }
  
  public TreeModelByXml(List<Element> elementList)
  {
    super(wrapList(elementList), CHILD_PROPERTY);
  }
  public static TreeModelByXml createModelByDocumentList(List<XmlDocument> documentList)
  {
    return new TreeModelByXml(getElementList(documentList));
  }
  //--------------------------------
  public static ElementTree getRootElementByString(String str) throws IOException,
                                                                      SAXException
  {
//    Properties props = new Properties();
//    props.put("item", ElementTree.class.getName());
//    props.put("items", ElementTree.class.getName());
//    props.put("*Element", ElementTree.class.getName());
//    XmlDocument doc = XmlDoc.createXmlFromStringWithMaps(str, false, props);
    XmlDocument doc = XmlDoc.createXmlFromString(str, false);
    Element rc = doc.getDocumentElement();
    return new ElementTree(rc);
  }
  public static ElementTree getRootElementByResource(Object res) throws IOException,
                                                                        SAXException
    {
      ElementTree rc = null;
      if (res instanceof String) {
        String str = Resource.getResourceString(Resource.class, (String)res);
        rc = getRootElementByString(str);
      }
      else if (res instanceof XmlDocument) 
      {
        Element elem = ((XmlDocument)res).getDocumentElement();
        if (elem instanceof ElementTree) 
        {
          rc = (ElementTree) elem;
        }
        else 
        {
          rc = new ElementTree(elem);
        }
      }
      return rc;
    }
  //--------------------------------
  private static List<Element> getElementList(List<XmlDocument> documentList)
  {
    List<Element> rc = new ArrayList<Element>(documentList.size());
    for (XmlDocument doc : documentList) 
    {
      Element item = doc.getDocumentElement();
      ElementTree itemElTree =
        (ElementTree)(item instanceof ElementTree ? item : new ElementTree(item));
      rc.add(itemElTree);
    }
    return rc;
  }

  private static List<ElementTree> wrapList(List<Element> elementList)
  {
    List<ElementTree> rc = new ArrayList<ElementTree>();
    for (Element item : elementList) 
    {
      ElementTree itemElTree =
        (ElementTree)(item instanceof ElementTree ? item : new ElementTree(item));
      rc.add(itemElTree);
    }
    return rc;
  }
  //--------------------------------------
  
}
