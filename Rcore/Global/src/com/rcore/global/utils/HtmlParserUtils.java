package com.rcore.global.utils;
// ШМЯ

import com.rcore.global.DateTime;
import com.rcore.global.Resource;
import com.rcore.global.StringFunc;

import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;


public final class HtmlParserUtils
{
  private HtmlParserUtils()
  {
    super();
  }
  public static Parser createParser(String htmlContent) 
  {
    return createParser(htmlContent,null);
  }
  public static Parser createParser(String htmlContent,String charSet) 
    
  {
    if (charSet == null) 
    {
      charSet = "windows-1251";
    }
    Parser rc = Parser.createParser(htmlContent,charSet);
    return rc;
  }
  public static List<Node> getAllNodes(Parser parser)
    throws Exception
  {
    List<Node> rc = new ArrayList<Node>();
    NodeIterator iter = parser.elements();
    while(iter.hasMoreNodes()) 
    {
      getAllNodes(rc,iter.nextNode());
    }
    return rc;
  }
  
  public static void getAllNodes(List<Node> rc, Node n)
  {
    rc.add(n);
    NodeList list = n.getChildren();
    if (list == null) 
    {
      return;
    }
    SimpleNodeIterator iter = list.elements();
    while(iter.hasMoreNodes())
    {
      getAllNodes(rc,iter.nextNode());
    }
  }
  
  public static void adoptHtmlNodeForSave(
        Node html,boolean isAcceptMode,double browserVer,String serverPath) 
  {
    insertMetaForBrowser(html,browserVer);
    //------------------------------------
    List<Node> rc = new ArrayList<Node>();
    getAllNodes(rc,html);
    for(Node n : rc) 
    {
      Node parent = n.getParent();
      if (n instanceof ScriptTag) 
      {
        if (parent != null) {
          parent.getChildren().remove(n);
        }
      }
      else if (n instanceof Div)
      {
        Div div = (Div) n;
        String style = div.getAttribute("style");
        if (style != null) 
        {
          String upper = style.toUpperCase();
          if (upper.contains("DISPLAY: NONE") || upper.contains("DISPLAY:NONE")) 
          {
            if (parent != null) {
              parent.getChildren().remove(n);
            }
          }
        }
      }
      else if (n instanceof InputTag) 
      {
        //System.out.println(n.toHtml());
        InputTag it = (InputTag) n;
        it.setAttribute("disabled","disabled");
      }
      else if (n instanceof LinkTag) 
      {
        LinkTag lt = (LinkTag) n; 
        int count = lt.getChildCount();
        if (count > 0) {
         lt.setAttribute("disabled","disabled");
        }
      }
      else if (n instanceof SelectTag) 
      {
        SelectTag st = (SelectTag) n;
        st.setAttribute("disabled","disabled");
      }
      else if (n instanceof TextareaTag) 
      {
        TextareaTag ta = (TextareaTag) n;
        ta.setAttribute("disabled","disabled");
      }
      else 
      {
        if (n instanceof TagNode) 
        {
          TagNode tn = (TagNode) n;
          if ("BUTTON".equals(tn.getTagName().toUpperCase())) 
          {
            tn.setAttribute("disabled","disabled");
          }
          else if ("LINK".equals(tn.getTagName().toUpperCase())) {
            String rel  = tn.getAttribute("rel");
            String href = tn.getAttribute("href");
            if (rel != null && "stylesheet".equals(rel.toLowerCase())) {
              if (href != null) {
//                tn.removeAttribute("rel");
//                tn.removeAttribute("href");
//                try {
//                  appendStyleNode(parent,href,browserVer,serverPath);
//                }
//                catch(Throwable ex) {
//                  ex.printStackTrace();
//                  // Не удалось получить тело css :
//                  if (browserVer == 7.0) {
//                    tn.setAttribute("rel",rel);
//                    tn.setAttribute("href","/realty21/css/IE7Default.css");
//                  }
//                  else if (browserVer == 8.0) {
//                    tn.setAttribute("rel",rel);
//                    tn.setAttribute("href","/realty21/css/IE8Default.css");
//                  }
//                }
                tn.setAttribute("href","/realty21/css/${IE}Default.css");
              }
            }
          }
          else {
            String id = tn.getAttribute("id");
            if (id != null) {
              if (id.endsWith("__save_change_date::content")) {
                // Data изменения:
//                Node child = tn.getFirstChild();
//                if (child != null) {
//                  tn.getChildren().remove(child);
//                }
//                tn.getChildren().add(new TextNode(DateTime.getDate(".")));
                NodeList newNodeList = new NodeList();
                newNodeList.add(new TextNode(DateTime.getDate(".")));
                tn.setChildren(newNodeList);
              }
              else if (id.endsWith("__save_assertion_date::content")) {
                if (isAcceptMode) {
//                  Node child = tn.getFirstChild();
//                  if (child != null) {
//                    tn.getChildren().remove(child);
//                  }
//                  tn.getChildren().add(new TextNode(DateTime.getDate(".")));
                    NodeList newNodeList = new NodeList();
                    newNodeList.add(new TextNode(DateTime.getDate(".")));
                    tn.setChildren(newNodeList);
                }
              }
              else if (id.endsWith("__save_panel_buttons")) {
                // Удалим:
                try {
                  tn.getParent().getChildren().remove(tn);
                }
                catch(Throwable ex) {
                  ex.printStackTrace();
                }
              }
            }
          }
        }
      }
    }
  }
  
  public static String toHtml(List<Node> nodes) 
  {
    StringBuffer rc = new StringBuffer();
    for(Node node: nodes) 
    {
      if (rc.length() > 0) 
      {
        rc.append("\n");
      }
      rc.append(node.toHtml(false));
    } // for
    return rc.toString();
  }
  
  //=============================================================
  public static void main(String[] args)
    throws ParserException
  {
    String htmlContent = "<HTML class=p_AFMaximized lang=ru-RU dir=ltr><META content=\"IE=7\" http-equiv=X-UA-Compatible><HEAD><TITLE>Недвижимость 2.1</TITLE><LINK href=\"/realty21/adf/styles/cache/blafplus-rich-desktop-8lqkk3-ltr-ie-cmp.css\" type=text/css charset=UTF-8 rel=stylesheet></LINK></HEAD></HTML>";
    Parser rc = createParser(htmlContent,null);
    Html obj = (Html) rc.elements().nextNode();
    adoptHtmlNodeForSave(obj,false,8.0,"http://127.0.0.1:7101/");
    System.out.println(obj.toHtml(false));
  }


  private static void insertMetaForBrowser(Node html,double browserVer)
  {
    if (html == null) 
    {
      return;
    }
    if (!(html instanceof Html)) 
    {
      return;
    }
    Html htmlNode = (Html) html;
    Node child = html.getFirstChild();
    if (child == null) 
    {
      return;
    }
    if (!(child instanceof MetaTag)) 
    {
      insertMetaForBrowserImpl(htmlNode);
    }
  }

  private static void insertMetaForBrowserImpl(Html htmlNode)
  {
    //final String META="<META content='IE=7' http-equiv='X-UA-Compatible'>";
    MetaTag meta = new MetaTag();
    meta.setAttribute("content","IE=7");
    meta.setAttribute("http-equiv","X-UA-Compatible");
    //htmlNode.getChildren().add(meta);
    //Node[] childs = htmlNode.getChildrenAsNodeArray();
    NodeList newNodeList = new NodeList(meta);
    newNodeList.add(htmlNode.getChildren());
    htmlNode.setChildren(newNodeList);
  }
  //------------------------------------------------------------
  /**
   * Установка даты утверждения документа
   * param: _текст измененного документа
   * @return _текст утвержденного документа
   */
  public static String acceptDocument(String content) throws ParserException {
    Parser parser = HtmlParserUtils.createParser(content,null);
    NodeIterator iter = parser.elements();
    List<Node> listHtml = new ArrayList<Node>();
    while (iter.hasMoreNodes()) {
      Node html = iter.nextNode();
      listHtml.add(html);
      acceptOneDocument(html);
    }
    content = HtmlParserUtils.toHtml(listHtml);
    content = adoptAcceptContent(content);
    //---------------------------------------------------------
    return content;
  }
  private static void acceptOneDocument(Node html) {
    List<Node> rc = new ArrayList<Node>();
    getAllNodes(rc,html);
    for(Node n : rc) 
    {
      //Node parent = n.getParent();
      if (n instanceof TagNode) {
        TagNode tn = (TagNode) n;
        String id = tn.getAttribute("id");
        if (id != null) {
          if (id.endsWith("__save_assertion_date::content")) {
            NodeList newNodeList = new NodeList();
            newNodeList.add(new TextNode(DateTime.getDate(".")));
            tn.setChildren(newNodeList);
          }
        }
      }
    } // for
  }
  
  public static String adoptAcceptContent(String content) {
    //content = StringFunc.replace(content,"подлежит оформлению","оформлен",1);
    content = StringFunc.replace(content,"В подготовке","Выпущен",1);
  //    int i = content.indexOf("Утвержден");
  //    if (i > -1) {
  //      i = content.toUpperCase().indexOf("</SPAN>",i);
  //      if (i > -1) {
  //        content = content.substring(0,i)+DateTime.getDate(".")+content.substring(i);
  //      }
  //    }
    return content;
  }

  public static void appendStyleNode(Node parent,String href,double browserVer,String serverPath) 
    throws Exception 
  {
    String fullPath = serverPath+href;
    if (serverPath.endsWith("/") && href.startsWith("/")) {
      fullPath = serverPath+href.substring(1);
    }
    //String fullPath = href;
    URL url = new URL(fullPath);
    HttpURLConnection connection =(HttpURLConnection)(url.openConnection());
    InputStream is = connection.getInputStream();
    String content = Resource.inputStreamToString(is);
    is.close();
    connection.disconnect();
    //----------------------
    NodeList childs = parent.getChildren();
    if (childs == null) {
      childs = new NodeList();
      parent.setChildren(childs);
    }
    childs.add(createStyleNode(content));
    return;
  }

  private static TextNode createStyleNode(String content) {
    TextNode rc = new TextNode("<STYLE media='screen' type='text/css'>"+content+"</STYLE>");
    return rc;
  }
}
