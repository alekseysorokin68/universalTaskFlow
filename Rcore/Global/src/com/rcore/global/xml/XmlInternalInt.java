package com.rcore.global.xml;
// ШМЯ

import java.net.URL;

import java.util.List;
import java.util.Map;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface XmlInternalInt
{
  public Document documentFromStringNoValid(String text)
    throws Exception;

  public Document documentFromFileNoValid(String fileName)
    throws Exception;

  public Document documentFromUriNoValid(String fileName)
    throws Exception;

  public SchemaFactory getSchemaFactory();

  public Document documentFromStringValidSchema(String text, Schema schema, 
                                                StringBuffer isErrors)
    throws Exception;

  public Document documentFromFileValidSchema(String file, Schema schema, 
                                              StringBuffer isErrors)
    throws Exception;

  public Document documentFromUriValidSchema(String uri, Schema schema, 
                                             StringBuffer isErrors)
    throws Exception;

  public Document documentFromStringValidDtd(String text, 
                                             Map<String, URL> mapSystemId, 
                                             StringBuffer isErrors)
    throws Exception;

  public Document documentFromFileValidDtd(String file, 
                                           Map<String, URL> mapSystemId, 
                                           StringBuffer isErrors)
    throws Exception;

  public Document documentFromUriValidDtd(String uri, 
                                          Map<String, URL> mapSystemId, 
                                          StringBuffer isErrors)
    throws Exception;

  public void normalize(Document doc);

  public void normalize(Element elem);

  // ----------------------------------------------------
  // Следующие два метода выбирают только Element ?!
  // Этим объясняется наличие public List<Node> getChilds(Node node);

  public Node selectNode(String xPath, Node root)
    throws Exception;

  public List<Node> selectNodes(String xPath, Node root)
    throws Exception;

  public List<Node> getChilds(Node node);

  // ----------------------------------------------------

  public int getLevelNode(Node node);

  public String getPath(Node root, Node node)
    throws Exception;

  public String getPath(Node root, Node node, boolean withIndexes)
    throws Exception;

  public int getChildIndexNode(Node node);

  public int getElementIndexNode(Node node);

  public boolean isEmptyNode(Node node)
    throws Exception;

  public List<Node> getAttributes(Node node);

  // ----------------------------------------------------

  public void replaceChild(Node newChild, Node refChild);

  public void removeChilds(Node node);

  public void removeChilds(Node node, String tagName);

  public void insertBefore(Node newChild, Node refChild);

  public void insertAfter(Node newChild, Node refChild)
    throws Exception;

  public void removeAllAttributes(Element element);

  public void appendChild(Node parent, Node newChild);
  // ---------------------------------------------------------
  public String toString(Node n, boolean isPrintXmlDeclaration)
    throws Exception;

  public String toString(Node n)
    throws Exception;
}
