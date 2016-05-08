package com.rcore.global.xml;
// ШМЯ

import com.sun.xml.parser.Parser;
import com.sun.xml.parser.Resolver;
import com.sun.xml.parser.ValidatingParser;
import com.sun.xml.tree.ElementNode;
import com.sun.xml.tree.SimpleElementFactory;
import com.sun.xml.tree.XmlDocument;

import com.sun.xml.tree.XmlDocumentBuilder;

import com.sun.xml.tree.XmlWriteContext;


import com.rcore.global.Files;
import com.rcore.global.Resource;
import com.rcore.global.StringFunc;

import com.rcore.global.Token;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Набор статических методов, расширяющих XML - API
 */
public final class XmlDoc
{
  private static final String CRLF = "" + (char) (13) + ((char) (10));
  private static int INDENT_FOR_LEVEL = 2;
  private static final String EMPTY = "";

  private static final String IDERROR_DTD = "com.sun.xml.parser/P-055 ";

  private static final String ATR_BEGIN = "=\"";
  private static final String ATR_END = "\"";

  private static final String SPACE = " ";

  /*
	 * Возвращает уровень (целое число) XML-узла
	 */

  public static int getLevelNode(Node node)
  {
    Node parent = node.getParentNode();
    if (parent == null)
    {
      return 0;
    }
    return (getLevelNode(parent) + 1);
  }

  /**
   * Ищет XmlDocument, содержащий текущий узел
   * 
   * @param node -
   *            Xml - узел
   * @return XmlDocument, содержащий текущий узел
   */
  public static XmlDocument getParentXmlDocument(Node node)
  {
    Node rc = node;
    while (true)
    {
      if (rc == null)
      {
        break;
      }
      if (rc instanceof XmlDocument)
      {
        break;
      }
      rc = rc.getParentNode();
    }
    return (XmlDocument) rc;
  } // public static XmlDocument getParentXmlDocument(Node node)

  /**
   * Получить "сыновний" индекс узла.
   * 
   * @param node -
   *            Xml - узел
   * @return - "сыновний" индекс узла
   */
  public static int getChildIndexNode(Node node)
  {
    Node parent = node.getParentNode();
    if (parent == null)
    {
      return 0;
    }
    if (parent instanceof XmlDocument)
    {
      return 0;
    }
    //
    NodeList childs = parent.getChildNodes();
    int rc = 0;
    for (int i = 0; i < childs.getLength(); i++)
    {
      if (childs.item(i).equals(node))
      {
        rc = i;
        break;
      }
    }
    // for
    return rc;
  } // public static int getChildIndexNode(Node node)

  /**
   * Получить список аттрибутов узла
   * 
   * @param node -
   *            Xml - узел
   * @return ArrayList_Node - список аттрибутов узла
   */
  public static ArrayList<Node> getAttributes(Node node)
  {
    ArrayList<Node> rc = new ArrayList<Node>();
    NamedNodeMap attListMap = node.getAttributes();
    if (attListMap == null)
    {
      return rc;
    }
    for (int i = 0; i < attListMap.getLength(); i++)
    {
      Node att = attListMap.item(i);
      rc.add(att);
    }
    // for
    return rc;
  }

  /**
   * Получить первый сыновний Node c заданным именем тэга
   * 
   * @param parent -
   *            Родительский узел
   * @param nodeName -
   *            имя сыновнего тэга
   * @return - первый сыновний Node c именем nodeName
   */
  public static Node getFirstChildNode(Element parent, String nodeName)
  {
    Node rc = null;
    if (parent == null)
    {
      return rc;
    }
    if (nodeName == null)
    {
      return rc;
    }
    NodeList childs = parent.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node n = childs.item(i);
      if (n.getNodeName().equals(nodeName))
      {
        rc = n;
        break;
      }
    }
    // for
    return rc;
  }

  /**
   * Поиск первого Node c заданным именем тэга и значением определенного
   * атрибута
   * 
   * @param parent -
   *            Родительский Element
   * @param nodeName -
   *            Имя искомого тэга
   * @param attName -
   *            Имя атрибута
   * @param attValue -
   *            Значение атрибута
   * @return - Искомый Node
   */
  public static Node getFirstChildNode(Element parent, String nodeName, 
                                       String attName, String attValue)
  {
    if (attName == null)
    {
      return getFirstChildNode(parent, nodeName);
    }
    if (attValue == null)
    {
      return getFirstChildNode(parent, nodeName);
    }
    Node rc = null;
    if (parent == null)
    {
      return rc;
    }
    if (nodeName == null)
    {
      return rc;
    }
    NodeList childs = parent.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node n = childs.item(i);
      if (n.getNodeName().equals(nodeName))
      {
        String value = null;
        try
        {
          value = ((Element) n).getAttribute(attName);
        }
        catch (Exception e)
        {
          value = null;
        }
        if (value == null)
        {
          continue;
        }
        if (value.equals(attValue))
        {
          rc = n;
          break;
        }
      }
    }
    // for
    return rc;
  }

  /**
   * Получить список всех детей Node
   * 
   * @param parent -
   *            Xml - узел, в котором ищутся все дети
   * @return - ArrayList_Node - Список всех детей Node
   */
  public static ArrayList<Node> getChilds(Node parent)
  {
    return getChilds(parent, null);
  }

  /**
   * Получить список всех детей Node c заданным именем тэга
   * 
   * @param parent -
   *            родительский узел
   * @param tagName -
   *            имя тэга, для детей, которые ищутся
   * @return - ArrayList_Node - список всех детей Node c заданным именем
   *         тэга
   */
  public static ArrayList<Node> getChilds(Node parent, String tagName)
  {
    ArrayList<Node> rc = new ArrayList<Node>();
    if (parent == null)
    {
      return rc;
    }
    NodeList childs = parent.getChildNodes();
    if (childs == null)
    {
      return rc;
    }
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node n = childs.item(i);
      if (tagName == null)
      {
        rc.add(n);
        continue;
      }
      String nodeName = n.getNodeName();
      if (nodeName.equals(tagName))
      {
        rc.add(n);
      }
    }
    // for
    return rc;
  }

  /**
   * Поиск первого предка с заданным именем тэга
   * 
   * @param children -
   *            узел, для которого ищется предок
   * @param tagName -
   *            имя тэга, искомого предка
   * @return - Искомый предок
   */
  public static Node getFirstParentByTagName(Node children, String tagName)
  {
    if (children == null)
    {
      return null;
    }
    if (tagName == null)
    {
      return null;
    }
    Node parent = children;
    while ((parent != null) && (!(parent.getNodeName().equals(tagName))))
    {
      parent = parent.getParentNode();
    }
    return parent;
  }

  /**
   * Поиск первого предка с с заданным именем тэга и заданным значением
   * атрибута
   * 
   * @param children -
   *            узел, для которого ищется предок
   * @param tagName -
   *            имя тэга, искомого предка
   * @return - Искомый предок
   */
  public static Element getFirstParent(Element children, String tagName, 
                                       String attName, String attValue)
  {
    if (children == null)
    {
      return null;
    }
    if (tagName == null)
    {
      return null;
    }
    Element parent = children;
    while ((parent != null) && 
           ((!(parent.getNodeName().equals(tagName))) || 
            (!(attValue.equals(parent.getAttribute(attName))))))
    {
      Node nodeParent = parent.getParentNode();
      if (nodeParent == null)
      {
        return null;
      }
      if (!(nodeParent instanceof Element))
      {
        return null;
      }
      parent = (Element) (nodeParent);
    }
    return parent;
  }

  // -----------------------------------------------------

  /**
   * Парсирование ElementNode
   * 
   * @param sRoot -
   *            Строковое представление ElementNode
   * @param sysId -
   *            Системный идентификатор
   * @param p -
   *            Properties для мапирования узлов
   * @return ElementNode throws org.xml.sax.SAXException, IOException
   */
  public static ElementNode getDocumentElementFromString(String sRoot, 
                                                         String sysId, 
                                                         Properties p)
    throws org.xml.sax.SAXException, IOException
  {
    XmlDocument d = getDocumentFromString(sRoot, sysId, p);
    return (ElementNode) d.getDocumentElement();
  }

  /**
   * Парсирование XmlDocument
   * 
   * @param sRoot -
   *            Строковое представление XmlDocument
   * @param doValidate -
   *            валидирующий парсер или нет
   * @param domainProps -
   *            Доменные свойства
   * @param sysId -
   *            Системный идентификатор
   * @param ids -
   *            Свойства для мапирования узлов
   * @return XmlDocument throws org.xml.sax.SAXException, IOException
   */
  @SuppressWarnings("unchecked")
  public static XmlDocument getDocumentFromString(String sRoot, 
                                                  boolean doValidate, 
                                                  Properties domainProps, 
                                                  String sysId, 
                                                  Properties ids)
    throws org.xml.sax.SAXException, IOException
  {
    InputSource isr = new InputSource(new StringReader(sRoot));
    isr.setSystemId(getSysId(sysId));
    XmlDocumentBuilder builder = new XmlDocumentBuilder();
    Parser parser = (doValidate)? new ValidatingParser(): new Parser();

    Resolver resolver = new Resolver();
    if (ids != null)
    {
      for (Enumeration keys = ids.propertyNames(); keys.hasMoreElements(); 
      )
      {
        String publicId = (String) keys.nextElement();
        String systemId = ids.getProperty(publicId);
        resolver.registerCatalogEntry(publicId, systemId);
      }
    }
    {
      // Configure the builder to create the right elements.
      //

      // Properties props;
      SimpleElementFactory factory;

      if (domainProps != null)
      {
        factory = new SimpleElementFactory();
        factory.addMapping(domainProps, Properties.class.getClassLoader());
        builder.setElementFactory(factory);
      }
    }
    //
    // Configure the parser
    //
    parser.setErrorHandler(new ErrorPrinter());
    parser.setEntityResolver(resolver);
    parser.setDocumentHandler(builder);
    parser.parse(isr);
    if (false)
    {
      builder.getDocument().write(System.out);
    }

    XmlDocument rc = builder.getDocument();
    // clearTextNodes(rc);
    return rc;
  }

  /**
   * Парсирование XmlDocument
   * 
   * @param sRoot -
   *            Строковое представление XmlDocument
   * @param doValidate -
   *            валидирующий парсер или нет
   * @param domainPropsStream -
   *            Доменный поток
   * @param sysId -
   *            Системный идентификатор
   * @param ids -
   *            Свойства для мапирования узлов
   * @return XmlDocument throws org.xml.sax.SAXException, IOException
   */
  public static XmlDocument getDocumentFromString(String sRoot, 
                                                  boolean doValidate, 
                                                  InputStream domainPropsStream, 
                                                  String sysId, 
                                                  Properties ids)
    throws org.xml.sax.SAXException, IOException
  {
    Properties props = null;
    if (domainPropsStream != null)
    {
      props = new Properties();
      props.load(domainPropsStream);
    }
    return getDocumentFromString(sRoot, doValidate, props, sysId, ids);
  }

  /**
   * Парсирование системного идентификатора
   * 
   * @param sysId -
   *            системный идентификатор
   * @return преобразованный системный идентификатор
   */
  public static String getSysId(String sysId)
  {
    if (sysId == null)
    {
      return sysId;
    }

    sysId = StringFunc.replace(sysId, "\\", "/");
    String rc = sysId;

    URL url = null;
    try
    {
      url = new URL(sysId);
    }
    catch (Exception e)
    {
      url = null;
    }
    if (url == null)
    {
      // File

      try
      {
        File file = new File(sysId);
        String canonic = file.getCanonicalPath();
        sysId = canonic;
        sysId = StringFunc.replace(sysId, "\\", "/");
        rc = sysId;
        rc = "file:///" + rc;
      }
      catch (Exception ex)
      {
        ex.printStackTrace(System.out);
      }
    }
    return rc;
  }

  /**
   * Парсирование XmlDocument
   * 
   * @param sRoot -
   *            Строковое представление XmlDocument
   * @return XmlDocument
   * @throws org.xml.sax.SAXException
   * @throws IOException
   */
  public static XmlDocument getDocumentFromString(String sRoot)
    throws org.xml.sax.SAXException, IOException
  {
    return getDocumentFromString(sRoot, false, null, null);
  }

  /**
   * Парсирование XmlDocument
   * 
   * @param sRoot -
   *            Строковое представление XmlDocument
   * @param doValidate -
   *            валидирующий парсер или нет
   * @param sysId -
   *            Системный идентификатор
   * @param props -
   *            Свойства для мапирования узлов
   * @return XmlDocument
   * @throws org.xml.sax.SAXException
   * @throws IOException
   */
  public static XmlDocument getDocumentFromString(String sRoot, 
                                                  boolean doValidate, 
                                                  String sysId, 
                                                  Properties props)
    throws org.xml.sax.SAXException, IOException
  {
    return getDocumentFromString(sRoot, 
                                 doValidate, 
                                 (Properties) null, 
                                 sysId, 
                                 props);
  }

  /**
   * Парсирование XmlDocument
   * 
   * @param sRoot - Строковое представление XmlDocument
   * @param sysId - Системный идентификатор
   * @param props - Свойства для мапирования узлов
   * @return XmlDocument
   * @throws org.xml.sax.SAXException
   * @throws IOException
   */
  public static XmlDocument getDocumentFromString(String sRoot, 
                                                  String sysId, 
                                                  Properties props)
    throws org.xml.sax.SAXException, IOException
  {
    return getDocumentFromString(sRoot, false, sysId, props);
  }

  // ---------------------------------

  /**
   * Получить значение атрибута, с учетом значения по умолчанию Т.е. если
   * атрибут пуст или его нет - вернет значение по умолчанию
   * 
   * @param element - узел, в котором определяется значение атрибута
   * @param attName - имя атрибута
   * @param defaultValue - значение атрибута по умолчанию
   * @return String - значение атрибута
   */
  public static String getAttribute(Element element, String attName, 
                                    String defaultValue)
  {
    String rc = element.getAttribute(attName);
    if (rc == null)
    {
      rc = defaultValue;
    }
    return rc;
  }

  /**
   * Получить значение атрибута, как boolean
   * 
   * @param element - узел, в котором определяется значение атрибута
   * @param attName - имя атрибута
   * @return boolean - значение атрибута
   */
  public static boolean getAttribute_bool(Element element, String attName)
  {
    return getAttribute_bool(element, attName, false);
  }

  /**
   * Получить значение логического атрибута, с учетом значения по умолчанию
   * Т.е. если атрибут пуст или его нет - вернет значение по умолчанию
   * 
   * @param element - узел, в котором определяется значение атрибута
   * @param attName - имя атрибута
   * @param defaultValue - значение атрибута по умолчанию
   * @return boolean - значение атрибута
   */
  public static boolean getAttribute_bool(Element element, String attName, 
                                          boolean defaultValue)
  {
    String sRc = XmlDoc.getAttribute(element, attName, "" + defaultValue);
    boolean rc = ("true".equalsIgnoreCase(sRc));
    return rc;
  }

  /**
   * Получить значение целого атрибута
   * 
   * @param element - узел, в котором определяется значение атрибута
   * @param attName - имя атрибута
   * @return int - значение атрибута
   */
  public static int getAttribute_int(Element element, String attName)
  {
    return getAttribute_int(element, attName, 0);
  }

  /**
   * Получить значение целого атрибута, с учетом значения по умолчанию Т.е.
   * если атрибут пуст или его нет - вернет значение по умолчанию
   * 
   * @param element - узел, в котором определяется значение атрибута
   * @param attName - имя атрибута
   * @param defaultValue - значение атрибута по умолчанию
   * @return int - значение атрибута
   */
  public static int getAttribute_int(Element element, String attName, 
                                     int defaultValue)
  {
    String sRc = XmlDoc.getAttribute(element, attName, "" + defaultValue);
    int rc = defaultValue;
    try
    {
      rc = Integer.parseInt(sRc);
    }
    // try
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return rc;
  }

  /**
   * Получить значение атрибута типа double
   * 
   * @param element - узел, в котором определяется значение атрибута
   * @param attName - имя атрибута
   * @return double - значение атрибута
   */
  public static double getAttribute_double(Element element, String attName)
  {
    return getAttribute_double(element, attName, 0);
  }

  /**
   * Получить значение атрибута типа double, с учетом значения по умолчанию
   * Т.е. если атрибут пуст или его нет - вернет значение по умолчанию
   * 
   * @param element - узел, в котором определяется значение атрибута
   * @param attName - имя атрибута
   * @param defaultValue - значение атрибута по умолчанию
   * @return double - значение атрибута
   */
  public static double getAttribute_double(Element element, String attName, 
                                           double defaultValue)
  {
    String sRc = XmlDoc.getAttribute(element, attName, "" + defaultValue);
    double rc = defaultValue;
    try
    {
      rc = (Double.valueOf(sRc)).doubleValue();
    }
    // try
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return rc;
  }

  /**
   * Получить первый дочерний элемент по имени тэга
   * 
   * @param parent - Родительский элемент
   * @param tagName - Имя тэга
   * @return - Искомый элемент
   */
  public static Element getFirstElementChild(Element parent, 
                                             String tagName)
  {
    return (Element) (getFirstChildNode(parent, tagName));
  }

  /**
   * Получить первый дочерний элемент
   * 
   * @param parent -   Родительский элемент
   * @param tagName -  Имя тэга
   * @param attName -  Имя атрибута
   * @param attValue - Значение атрибута
   * @return Element - Искомый элемент, или null
   */
  public static Element getFirstElementChild(Element parent, 
                                             String tagName, 
                                             String attName, 
                                             String attValue)
  {
    return (Element) (getFirstChildNode(parent, tagName, attName, 
                                        attValue));
  }

  /**
   * Получить первого предка с нужным именем тэга
   * 
   * @param child - Элемент, от которого ищется предок
   * @param tagName - Имя тэга для предка
   * @return Element - предок, с заданными условиями
   */
  public static Element getFirstElementUp(Element child, String tagName)
  {
    return (Element) (getFirstParentByTagName(child, tagName));
  }

  /**
   * Получить первый элемент - предок с заданными условиями
   * 
   * @param child - Элемент, от которого ищется предок
   * @param tagName - Имя тэга для предка
   * @param attName - Имя атрибута
   * @param attValue - Значение атрибута
   * @return Element - первый элемент - предок с заданными условиями
   */
  public static Element getFirstElementUp(Element child, String tagName, 
                                          String attName, String attValue)
  {
    return (getFirstParent(child, tagName, attName, attValue));
  }

  /**
   * Получить список узлов - детей с заданным именем тэга
   * 
   * @param parent - Узел родитель
   * @param nodeName - Имя тэга
   * @param attName
   * @param attValue
   * @return ArrayList_Node - список узлов - детей с заданным именем тэга
   */
  public static ArrayList<Node> getElementsChild(Element parent, 
                                                String nodeName, 
                                                String attName, 
                                                String attValue)
  {
    if (attName == null)
    {
      return getChilds(parent, nodeName);
    }
    if (attValue == null)
    {
      return getChilds(parent, nodeName);
    }
    ArrayList<Node> rc = new ArrayList<Node>();
    if (parent == null)
    {
      return rc;
    }
    if (nodeName == null)
    {
      return rc;
    }
    NodeList childs = parent.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node n = childs.item(i);
      if (n.getNodeName().equals(nodeName))
      {
        String value = null;
        try
        {
          value = ((Element) n).getAttribute(attName);
        }
        catch (Exception e)
        {
          value = null;
        }
        if (value == null)
        {
          continue;
        }
        if (value.equals(attValue))
        {
          rc.add(n);
        }
      }
    }
    return rc;
  }

  /**
   * Получить символьный путь к узлу от корня дерева
   * 
   * @param node - узел
   * @return - String путь к узлу от корня дерева
   */
  public static String getPath(Node node)
  {
    return getPath(node, true);
  }

  /**
   * Получить символьный путь к узлу от корня дерева
   * 
   * @param node - узел
   * @param withIndexes - индикатор добавления к пути дочерних индексов
   * @return - String путь к узлу от корня дерева
   */
  public static String getPath(Node node, boolean withIndexes)
  {
    String rc = null;
    node.getNodeName();
    while ((node != null) && (!(node instanceof XmlDocument)))
    {
      String s1 = node.getNodeName();
      if (withIndexes)
      {
        s1 += ("(" + getChildIndexNode(node) + ")");
      }
      if (rc == null)
      {
        rc = s1;
      }
      else
      {
        rc = s1 + "/" + rc;
      }
      node = node.getParentNode();
    }
    // while
    if (rc == null)
    {
      rc = "";
    }
    else
    {
      rc = "/" + rc;
    }
    return rc;
  }

  /**
   * Получить символьный путь к узлу от заданного узла
   * 
   * @param root - узел, от которого надо получить путь
   * @param node - узел, к которому надо получить путь
   * @return - String путь к узлу от корня дерева
   */
  public static String getPath(Node root, Node node)
  {
    return getPath(root, node, true);
  }

  /**
   * Получить символьный путь к узлу от заданного узла
   * 
   * @param root - узел, от которого надо получить путь
   * @param node - узел, к которому надо получить путь
   * @param withIndexes - индикатор добавления к пути дочерних индексов
   * @return - String путь к узлу от корня дерева
   */
  public static String getPath(Node root, Node node, boolean withIndexes)
  {
    String rc = null;
    node.getNodeName();
    while ((node != null) && (!(node.equals(root))))
    {
      String s1 = node.getNodeName();
      if (withIndexes)
      {
        s1 += ("(" + getChildIndexNode(node) + ")");
      }
      if (rc == null)
      {
        rc = s1;
      }
      else
      {
        rc = s1 + "/" + rc;
      }
      node = node.getParentNode();
    }
    // while
    if (rc == null)
    {
      rc = "";
    }
    return rc;
  }

  /**
   * Метод индицирует факт отсутствия детей у узла
   * 
   * @param node - тестируемый узел
   * @return - true, если нет детей.Иначе - false
   */
  public static boolean isEmptyNode(Node node)
  {
    boolean rc = true;
    if (node == null)
    {
      return rc;
    }
    ArrayList<Node> childs = XmlDoc.getChilds(node);
    if (childs == null)
    {
      return rc;
    }
    if (childs.size() == 0)
    {
      return rc;
    }
    rc = false;
    return rc;
  }
  
    public static String getTextContent(Node node)
    {
      String rc = "";
      ArrayList<Node> childs = getChilds(node);
      for (int i = 0; i < childs.size(); i++)
      {
        Node child = (childs.get(i));
        if (child instanceof Text)
        {
          rc = child.getNodeValue();
          break;
        }
      }
      // for i
      return rc;
    }
    public static void setTextContent(Node node,String value)
    {      
      boolean st=false;  
      ArrayList<Node> childs = getChilds(node);
      for (int i = 0; i < childs.size(); i++)
      {
        Node child = (childs.get(i));
        if (child instanceof Text)
        {
          child.setNodeValue(value);
          st=true;
          break;
        }
      }   
      if(!st && value!=null){
          node.appendChild(node.getOwnerDocument().createTextNode(value));
      }
    }
  

  public static boolean isIncludeTextNodeInChilds(Node node)
  {
    boolean rc = false;
    ArrayList<Node> childs = getChilds(node);
    for (int i = 0; i < childs.size(); i++)
    {
      Node child = (childs.get(i));
      if (child instanceof Text)
      {
        rc = true;
        break;
      }
    }
    // for i
    return rc;
  }

  /**
   * Устанавливает атрибуты элементу
   * 
   * @param el - Element
   * @param attrList - Двухмерный массив атрибутов. элементы с индексом 0 - имя
   *            атрибута элементы с индексом 1 - значение атрибута
   */
  public static void setAttributes(Element el, String[][] attrList)
  {
    int len = attrList.length;
    for (int i = 0; i < len; i++)
    {
      String[] attribute = attrList[i];
      el.setAttribute(attribute[0], attribute[1]);
    }
    // for i
  }

  /**
   * Сохранить документ в файле
   * 
   * @param fileName - имя файла
   * @param elem - элемент
   * @param header - заголовок элемента
   * @param indent - размер отступов
   * @throws IOException
   */
  public static void saveElement(String fileName, 
                                 Element elem, 
                                 String header, 
                                 int indent)
    throws IOException
  {
    String outStr = header + CRLF;
    outStr += elementToStr(elem, 0, indent);
    Files.putFile(fileName, outStr);
  }

  /**
   * Cохранить документ в дисковом файле
   * 
   * @param fileName - имя файла
   * @param doc - Документ (XML)
   * @param header - Заголовок
   * @param indent - Размер отступов
   * @throws IOException
   */
  public static void saveDocument(String fileName, 
                                  XmlDocument doc, 
                                  String header, 
                                  int indent)
                                  throws IOException
  {
    String outStr = header + CRLF;
    Element root = doc.getDocumentElement();
    normalize(root);
    outStr += elementToStr(root, 0, indent);
    // ***********
    Files.putFile(fileName, outStr);
  }
  
  public static void saveDocumentByEncoding(
                                    String fileName, 
                                    XmlDocument doc, 
                                    int indent,
                                    String encoding
                                  )
                                    throws IOException
  {
      FileOutputStream fos = new FileOutputStream(fileName);
      Writer out = new OutputStreamWriter(fos, encoding);
      doc.writeXml(new XmlWriteContext(out, indent));
      out.close();
      fos.close();
  }
  
  
  /**
   *  
   * @param fileName
   * @param doc
   * @param header
   * @param indent
   * @param encoding
   * @throws IOException
   */
  public static void saveDocumentByEncoding(
                                  String fileName, 
                                  XmlDocument doc, 
                                  String header, 
                                  int indent,
                                  String encoding
                                  )
                                  throws IOException
  {
//    String outStr = header + CRLF;
//    Element root = doc.getDocumentElement();
//    normalize(root);
//    outStr += elementToStr(root, 0, indent);
//    // ***********
//    Files.putFile(fileName, outStr);
      StringWriter sw = new StringWriter();
      ElementNode root = (ElementNode) doc.getDocumentElement();
      root.writeXml(new XmlWriteContext(sw, indent));
  }
  
  public static String print(ElementNode node)
  {
    Writer writer = new StringWriter();
    try
    {
      node.writeXml(new XmlWriteContext(writer, 3));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    String rc = writer.toString();
    return rc;
  }      

  /**
   * Строковое представление Node
   * 
   * @param node - Xml - узел
   * @param level - уровень узла
   * @param indent - отступ для одного уровня
   * @return - Строковое представление Node
   */
  public static String elementToStr(Node node, int level, int indent)
  {
    if (node == null)
    {
      return "";
    }
    String rc = "";
    int totalIndent = level * indent;
    String tagName = node.getNodeName();
    String strIndent = StringFunc.replicate(" ", totalIndent);
    String strIndent2 = null;
    try
    {
      strIndent2 = StringFunc.replicate(" ", totalIndent + 2 + tagName.length());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println(e.getMessage());
      System.out.println("elem = " + node + "//" + tagName);
    }

    NamedNodeMap attList = node.getAttributes();
    int len = 0;
    if (attList != null)
    {
      len = attList.getLength();
    }
    rc = strIndent + "<" + tagName;
    if (len > 0)
    {
      rc += (" " + printAttribute(attList.item(0)) + CRLF);
    }
    else
    {
      rc += (">" + CRLF);
    }
    for (int i = 1; i < len; i++)
    {
      if (i == (attList.getLength() - 1))
      {
        rc += (strIndent2 + printAttribute(attList.item(i)) + ">" + CRLF);
      }
      else
      {
        rc += (strIndent2 + printAttribute(attList.item(i)) + CRLF);
      }
    }
    // ******************
    NodeList childs = node.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++)
    {
      rc = rc + elementToStr(childs.item(i), level + 1, indent);
    }
    rc = rc + strIndent + "</" + tagName + ">" + CRLF;
    return rc;
  }

  /**
   * Cоздание Xml-документа, с помощью валидирующего парсера
   * 
   * @param fileName - имя файла документа.
   * @return - Объект класса XmlDocument
   * @throws IOException
   * @throws SAXException
   */
  public static XmlDocument createDocument(String fileName)
    throws IOException, SAXException
  {
    InputSource is = new InputSource(new FileReader(fileName));
    XmlDocument rc = XmlDocument.createXmlDocument(is, true);
    return rc;
  }

  /**
   * Cоздание Xml-документа, с помощью не валидирующего парсера
   * 
   * @param fileName - имя файла документа.
   * @return - Объект класса XmlDocument
   * @throws IOException
   * @throws SAXException
   */
  public static XmlDocument createDocumentNoValiding(String fileName)
    throws IOException, SAXException
  {
    InputSource is = new InputSource(new FileReader(fileName));
    XmlDocument rc = XmlDocument.createXmlDocument(is, false);
    return rc;
  }

  public static String nodeToStrWithoutDefaultAttributes(Node node)
  {
    return nodeToStrWithoutDefaultAttributes(node);
  }

  public static String nodeToStrWithoutDefaultAttributes(Node node, 
                                                         boolean isRecursive)
  {
    return nodeToStrWithoutDefaultAttributes(node, isRecursive);
  }

  /**
   * Строковое представление Xml - документа
   * 
   * @param doc - Xml - документ
   * @return - String - строковое представление Xml - документа
   */
  public static String docToStr(XmlDocument doc)
  {
    String rc = null;
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    try
    {
      doc.write(stream);
      rc = stream.toString();
      stream.close();
    }
    // try
    catch (Throwable ex)
    {
      ex.printStackTrace();
    }
    // catch
    finally
    {
    }
    // finally
    return rc;
  }
  
  public static String docToStr(XmlDocument doc,String encoding)
  {
    String rc = null;
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    try
    {
      doc.write(stream);
      rc = stream.toString(encoding);
      stream.close();
    }
    // try
    catch (Throwable ex)
    {
      ex.printStackTrace();
    }
    // catch
    finally
    {
    }
    // finally
    return rc;
  }

  // public static String docToStr(XmlDocument doc)

  public static String convertAttribute(String attr)
  {
    StringBuffer sb = new StringBuffer();
    convertAttribute(sb, attr);
    return sb.toString();
  }

  // public static String convertAttribute(String attr)

  public static XmlDocument DocumentFromFile(String fileName)
    throws IOException, SAXException
  {
    fileName = fileName.trim();
    XmlDocumentBuilder builder = new XmlDocumentBuilder();
    ValidatingParser parser = new ValidatingParser();
    InputSource input = Resolver.createInputSource(new File(fileName));

    parser.setErrorHandler(new ErrorPrinter());
    parser.setEntityResolver(new Resolver());
    parser.setDocumentHandler(builder);
    parser.parse(input);

    XmlDocument doc = builder.getDocument();
    return doc;
  }

  /**
   * Удаление всех детей у узла
   * 
   * @param parent - узел
   */
  public static void removeChilds(Node parent)
  {
    removeChilds(parent, null);
  }

  /**
   * Удаление всех детей,с определенным именем у узла
   * 
   * @param parent - узел
   */
  public static void removeChilds(Node parent, String tagName)
  {
    NodeList nodeList = parent.getChildNodes();
    if (nodeList == null)
    {
      return;
    }
    for (int i = nodeList.getLength() - 1; i >= 0; i--)
    {
      Node n = nodeList.item(i);
      if (tagName != null)
      {
        if (!(tagName.equals(n.getNodeName())))
        {
          continue;
        }
      }
      try
      {
        parent.removeChild(n);
      }
      catch (Exception ex)
      {
        System.out.println(ex.getMessage());
      }
    }
    // for
  }

  /**
   * Добавление нового узла к узлу
   * 
   * @param parent - узел, к которому добавляется сын newChild
   * @param newChild - новый сын узла parent
   * @return - true - если добавление успешно. false - иначе.
   */
  public static boolean appendChild(Node parent, Node newChild)
  {
    boolean rc = true;
    try
    {
      XmlDocument doc = null;
      if (parent instanceof XmlDocument)
      {
        doc = (XmlDocument) parent;
      }
      else
      {
        doc = (XmlDocument) (parent.getOwnerDocument());
      }
      try
      {
        doc.changeNodeOwner(newChild);
      }
      catch (Exception exx)
      {
        exx.printStackTrace();
      }
      parent.appendChild(newChild);
    }
    // try
    catch (DOMException ex)
    {
      System.out.println(ex.getMessage());
      ex.printStackTrace(System.out);
      rc = false;
    }
    // catch
    return rc;
  }

  /**
   * Добавление нового "брата" к узлу. Так, чтобы этот брат стал следующим
   * 
   * @param newChild - узел, который добавляется к "отцу" узла refChild
   * @param refChild - существующий узел
   * @return - true - если добавление успешно. false - иначе.
   */
  public static boolean insertAfter(Node newChild, Node refChild)
  {
    if (refChild == null)
    {
      return false;
    }
    if (newChild == null)
    {
      return false;
    }
    Node parent = refChild.getParentNode();
    ArrayList<Node> childs = getChilds(parent);
    int index = childs.indexOf(refChild);
    if (index == (childs.size() - 1))
    {
      return appendChild(parent, newChild);
    }
    Node refChildBefore = (childs.get(index + 1));
    return insertBefore(newChild, refChildBefore);
  }

  /**
   * Добавление нового "брата" к узлу. Так, чтобы этот брат стал предыдущим
   * 
   * @param newChild - узел, который добавляется к "отцу" узла refChild
   * @param refChild - существующий узел
   * @return - true - если добавление успешно. false - иначе.
   */
  public static boolean insertBefore(Node newChild, Node refChild)
  {
    if (refChild == null)
    {
      return false;
    }
    Node parent = refChild.getParentNode();
    if (parent == null)
    {
      return false;
    }
    boolean rc = true;
    try
    {
      try
      {
        XmlDocument doc = (XmlDocument) (refChild.getOwnerDocument());
        doc.changeNodeOwner(newChild);
      }
      catch (Exception exx)
      {
        exx.printStackTrace();
      }
      parent.insertBefore(newChild, refChild);
    }
    // try
    catch (DOMException ex)
    {
      System.out.println(ex.getMessage());
      ex.printStackTrace(System.out);
      rc = false;
    }
    // catch
    return rc;
  }

  /**
   * Удаление узла из Xml - дерева
   * 
   * @param nodeForRemove - узел, который мы удаляем
   * @return - true - если удаление успешно. false - иначе.
   */
  public static boolean removeChild(Node nodeForRemove)
  {
    boolean rc = true;
    if (nodeForRemove == null)
    {
      return false;
    }
    Node parent = nodeForRemove.getParentNode();
    if (parent == null)
    {
      return false;
    }
    try
    {
      parent.removeChild(nodeForRemove);
    }
    // try
    catch (DOMException ex)
    {
      System.out.println(ex.getMessage());
      ex.printStackTrace(System.out);
      rc = false;
    }
    // catch
    return rc;
  }

  /**
   * Удаление узла без его детей (не рекурсивно)
   * 
   * @param nodeForRemove
   * @return - true - если удаление успешно. false - иначе.
   */
  public static boolean removeNodeWithoutChilds(Node nodeForRemove)
  {
    boolean rc = true;
    if (nodeForRemove == null)
    {
      return false;
    }
    Node parent = nodeForRemove.getParentNode();
    if (parent == null)
    {
      return false;
    }
    rc = removeChild(nodeForRemove);
    if (!rc)
    {
      return false;
    }
    ArrayList<Node> childs = XmlDoc.getChilds(nodeForRemove);
    for (int i = 0; i < childs.size(); i++)
    {
      Node n = (childs.get(i));
      rc = removeChild(n);
      if (!rc)
      {
        return false;
      }
      rc = appendChild(parent, n);
      if (!rc)
      {
        return false;
      }
    }
    // for (i)
    return true;
  }

  /**
   * Замена одного узла в Xml другим
   * 
   * @param newChild - новый узел
   * @param refChild - заменяемый узел
   * @return - true - если замена успешна. false - иначе.
   */
  public static boolean replaceChild(Node newChild, Node refChild)
  {
    if (refChild == null)
    {
      return false;
    }
    Node parent = refChild.getParentNode();
    if (parent == null)
    {
      return false;
    }
    boolean rc = true;
    try
    {
      try
      {
        XmlDocument doc = (XmlDocument) (refChild.getOwnerDocument());
        doc.changeNodeOwner(newChild);
      }
      catch (Exception exx)
      {
        exx.printStackTrace();
      }
      parent.replaceChild(newChild, refChild);
    }
    // try
    catch (DOMException ex)
    {
      System.out.println(ex.getMessage());
      ex.printStackTrace(System.out);
      rc = false;
    }
    // catch
    return rc;
  }

  /**
   * Нормализация (т.е. удаление лишних спец символов) Xml документа.
   * 
   * @param doc - XmlDocument
   */
  public static void normalize(XmlDocument doc)
  {
    if (doc == null)
    {
      return;
    }
    Element de = doc.getDocumentElement();
    if (de == null)
    {
      return;
    }
    normalize(de);
  } // public static void normalize(XmlDocument doc)

  /**
   * Нормализация (т.е. удаление лишних спец символов) элемента.
   * 
   * @param elem - Element
   */
  public static void normalize(Element elem)
  {
    // if (true) return;
    if (elem == null)
    {
      return;
    }
    elem.normalize();
    NodeList childs = elem.getChildNodes();
    for (int i = childs.getLength() - 1; i >= 0; i--)
    {
      Node ni = childs.item(i);
      if (ni instanceof Text)
      {
        Text ti = (Text) ni;
        String sti = ti.getData();
        if (StringFunc.isEmpty(sti))
        {
          elem.removeChild(ni);
        }
        // if isEmpty
      }
      // if
      if (ni instanceof Element)
      {
        normalize((Element) ni);
      }
    }
    // for

  }

  public static XmlDocument createXmlFromFile(String fileName)
    throws IOException, SAXException
  {
    return createXmlFromFile(fileName, false);
  }

  public static XmlDocument createXmlFromFile(String fileName, 
                                              boolean warnings)
    throws IOException, SAXException
  {
    return createXmlFromFile(fileName, warnings, null);
  }

  public static XmlDocument createXmlFromFile(String fileName, 
                                              boolean warnings, 
                                              Properties maps)
    throws IOException, SAXException
  {
    fileName = fileName.trim();
    XmlDocumentBuilder builder = new XmlDocumentBuilder();
    ValidatingParser parser = new ValidatingParser(false);
    if (maps != null)
    {
      SimpleElementFactory factory = new SimpleElementFactory();
      factory.addMapping(maps, XmlDoc.class.getClassLoader());
      builder.setElementFactory(factory);
    }

    File inputFile = new File(fileName);
    InputSource input = Resolver.createInputSource(inputFile);

    if (warnings)
    {
      parser.setErrorHandler(new ErrorPrinter());
    }
    else
    {
      parser.setErrorHandler(null);
    }

    parser.setEntityResolver(new Resolver());
    parser.setDocumentHandler(builder);
    // builder.setParser(parser);
    parser.parse(input);

    XmlDocument doc = builder.getDocument();
    return doc;
  }

  public static XmlDocument createXmlFromStringWithMaps(String str, 
                                                        boolean warnings, 
                                                        Properties maps)
    throws IOException, SAXException
  {
    str = str.trim();
    XmlDocumentBuilder builder = new XmlDocumentBuilder();
    ValidatingParser parser = new ValidatingParser();
    if (maps != null)
    {
      SimpleElementFactory factory = new SimpleElementFactory();
      factory.addMapping(maps, Properties.class.getClassLoader());
      builder.setElementFactory(factory);
    }

    InputSource input = new InputSource(new StringReader(str));

    if (warnings)
    {
      parser.setErrorHandler(new ErrorPrinter());
    }
    else
    {
      parser.setErrorHandler(null);
    }

    parser.setEntityResolver(new Resolver());
    parser.setDocumentHandler(builder);
    parser.parse(input);

    XmlDocument doc = builder.getDocument();
    return doc;
  }

  public static XmlDocument createXmlFromString(String text, 
                                                boolean warnings)
    throws IOException, SAXException
  {
    return createXmlFromString(text, warnings, null);
  }

  public static XmlDocument createXmlFromString(String text, String dtd)
    throws IOException, SAXException
  {
    return createXmlFromString(text, false, dtd);
  }

  public static XmlDocument createXmlFromString(String text)
    throws IOException, SAXException
  {
    return createXmlFromString(text, false, null);
  }

  public static XmlDocument createXmlFromString(String text, 
                                                boolean warnings, 
                                                String dtd)
    throws IOException, SAXException
  {
    text = text.trim();
    XmlDocumentBuilder builder = new XmlDocumentBuilder();
    ValidatingParser parser = new ValidatingParser();
    InputSource input = new InputSource(new StringReader(text));
    if (!(StringFunc.isEmpty(dtd)))
    {
      String sysId = getSysId(dtd);
      input.setSystemId(sysId);
    }
    // if isEmpty

    if (warnings)
    {
      parser.setErrorHandler(new ErrorPrinter());
    }
    else
    {
      parser.setErrorHandler(null);
    }

    parser.setEntityResolver(new Resolver());
    parser.setDocumentHandler(builder);
    try
    {
      parser.parse(input);
    }
    catch (SAXException ex)
    {
      String mess = ex.getMessage();
      if (mess.startsWith(IDERROR_DTD))
      {
        String dtdNew = mess.substring(IDERROR_DTD.length());
        return createXmlFromString(text, warnings, dtdNew);
      }
      else
      {
        throw ex;
      }
    }

    XmlDocument doc = builder.getDocument();
    return doc;
  }

  public static XmlDocument createXmlDocument(Properties props)
  {
    XmlDocument rc = new XmlDocument();
    SimpleElementFactory factory = new SimpleElementFactory();
    factory.addMapping(props, rc.getClass().getClassLoader());
    rc.setElementFactory(factory);
    return rc;
  }

  public static void appendByChildIndex(Node node, Node newChild, 
                                        int index)
  {
    ArrayList<Node> childs = getChilds(node);
    if (childs.size() == 0)
    {
      appendChild(node, newChild);
      return;
    }

    if (index > (childs.size() - 1))
    {
      appendChild(node, newChild);
      return;
    }

    if (index <= 0)
    {
      insertBefore(newChild, (childs.get(0)));
      return;
    }
    Node after = (childs.get(index - 1));
    insertAfter(newChild, after);
  }

  // =========================================================

  /**
   * Удаление всех атрибутов
   * 
   * @param element - Element
   */
  public static void removeAllAttributes(Element element)
  {
    ArrayList<Node> list = getAttributes(element);
    for (int i = 0; i < list.size(); i++)
    {
      Node attr = (list.get(i));
      String name = attr.getNodeName();
      element.removeAttribute(name);
    }
    // for i
  }

  public static String getIndentNode(Node node)
  {
    int i = getLevelNode(node);
    i = i * INDENT_FOR_LEVEL;
    if (i == 0)
    {
      return "";
    }
    return (StringFunc.replicate(" ", i));
  }

  private static void convertAttribute(StringBuffer sb, String attr)
  {
    for (int i = 0; i < attr.length(); i++)
    {
      char ch = attr.charAt(i);
      switch (ch)
      {
        case '<':
          sb.append(("&lt;"));
          continue;
        case '>':
          sb.append(("&gt;"));
          continue;
        case '&':
          sb.append(("&amp;"));
          continue;
        case '\'':
          sb.append(("&apos;"));
          continue;
        case '"':
          sb.append(("&quot;"));
          continue;
        default:
          sb.append((ch));
          continue;
      }
    }
  }

  static String printAttribute(Node node)
  {
    String rc = null;
    if (node == null)
    {
      return SPACE;
    }
    try
    {
      String s = node.getNodeValue();
      if (s == null)
      {
        s = EMPTY;
      }
      s = convertAttribute(s);
      String name = node.getNodeName();
      StringBuffer sb = new StringBuffer();
      sb.append(name).append(ATR_BEGIN).append(s).append(ATR_END);
      // rc = name + "=\"" + s + "\"";
      rc = sb.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }

  static class ErrorPrinter
    implements ErrorHandler
  {

    public void error(SAXParseException e)
    {
      // normally a validity error
      message("Error (recoverable)", e);
    }

    public void warning(SAXParseException e)
    {
      message("Warning", e);
    }

    public void fatalError(SAXParseException e)
    {
      message("FATAL ERROR", e);
    }

    private void message(String level, SAXParseException e)
    {
      System.out.print("** ");
      System.out.println(level);
      System.out.print("   URI = ");
      System.out.print(e.getSystemId());
      System.out.print(" Line = ");
      System.out.println(e.getLineNumber());
      System.out.print("   Message = ");
      System.out.println(e.getMessage());
    }
  }

  public static ArrayList<Node> getNodesDown(Node parent, String tagName)
  {
    if (tagName == null)
    {
      tagName = "*";
    }
    ArrayList<Node> rc = new ArrayList<Node>();
    getNodesDown(parent, tagName, rc);
    return rc;
  }

  private static void getNodesDown(Node parent, String tagName, 
                                   ArrayList<Node> rc)
  {
    if (tagName.equals(parent.getNodeName()))
    {
      rc.add(parent);
    }
    NodeList nodeList = parent.getChildNodes();
    int size = nodeList.getLength();
    for (int i = 0; i < size; i++)
    {
      Node item = nodeList.item(i);
      getNodesDown(item, tagName, rc);
    }
  }

  public static ArrayList<Node> getElementsDown(Element parent, 
                                                String tagName)
  {
    return getElementsByTagName(parent, tagName);
  }

  public static ArrayList<Node> getElementsByTagName(Element node, 
                                                     String tagname)
  {
    if (tagname == null)
    {
      tagname = "*";
    }
    ArrayList<Node> rc = new ArrayList<Node>();
    getElementsByTagName(node, tagname, rc);
    return rc;
  }

  private static void getElementsByTagName(Element node, String tagname, 
                                           ArrayList<Node> rc)
  {
    String s1 = node.getNodeName();
    if ((s1.equals(tagname)) || (tagname.equals("*")))
    {
      rc.add(node);
    }

    NodeList childs = node.getChildNodes();
    if (childs == null)
    {
      return;
    }
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node n = childs.item(i);
      if (!(n instanceof Element))
      {
        continue;
      }
      getElementsByTagName((Element) n, tagname, rc);
    }
    // for
  }

  
  public static Element getFirstElementByAttList(Element root, 
                                                 String tagName, 
                                                 String attName, 
                                                 String attValue)
  {
    Element rc = null;
    String[] listName = Token.aToken(attName, ",");
    String[] listValue = Token.aToken(attValue, ",");
    List<Node> list = getElementsDown(root, tagName);
    for (Node node: list)
    {
      Element item = (Element) node;
      if (isAttListOk(item, listName, listValue))
      {
        rc = item;
        break;
      }
    }
    return rc;
  }

  private static boolean isAttListOk(Element item, String[] listName, 
                                     String[] listValue)
  {
    boolean rc = true;
    for (int i = 0; i < listName.length; i++)
    {
      String name = listName[i];
      String value = listValue[i];
      if (!(item.getAttribute(name).equals(value)))
      {
        rc = false;
        break;
      }
    }
    return rc;
  }
  //-----
  public static ElementNode getXmlRootFromResource(Class cl, String resource)
    throws IOException, SAXException
  {
    return getXmlRootFromResource(cl,resource,true);
  }
  public static ElementNode getXmlRootFromResource(Class cl, String resource, boolean isNormalize)
    throws IOException, SAXException
  {
    ElementNode rc = null;
    InputStream content = Resource.getResourceInputStream(cl, resource);
    XmlDocument doc = XmlDocument.createXmlDocument(content, false);
    rc = (ElementNode)(doc.getDocumentElement());
    if (isNormalize) {
      normalize(rc);
    }
    return rc;
  }

  public static void main(String[] str)
  {
    // String str = nodeToStrWithoutDefaultAttributes(node)
  }

} // End of Class


