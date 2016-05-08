package com.rcore.global.utils;
// ШМЯ

import com.rcore.global.DateTime;


import com.rcore.global.StringFunc;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;

public class HtmlAdapterBase implements HtmlAdapter {
  protected static final String NAME_BROWSER_GENERATOR = "BROSER_GENERATOR";
  protected static final String IEVER_PSEVDO = "${__IE_VER}";
  public HtmlAdapterBase() {
    super();
  }

  public void adoptHtmlForSave(
        Node    html,
        String  idDocType,
        boolean isAcceptMode,
        Double  ieBrowserVer,
        String  serverPath) 
  {
    insertMetaForBrowser(html,ieBrowserVer);
    String browserGenerator = getBrowserGenerator(html);
    if (browserGenerator == null) {
      setBrowserGenerator(html,ieBrowserVer);
      browserGenerator = ieBrowserVer+"";
    }
    else {
      if (!isAcceptMode) {
        setBrowserGenerator(html,ieBrowserVer);
        browserGenerator = ieBrowserVer+"";
      }
    }
    //------------------------------------
    List<Node> rc = new ArrayList<Node>();
    HtmlParserUtils.getAllNodes(rc,html);
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
    //                  // �� ������� �������� ���� css :
    //                  if (browserVer == 7.0) {
    //                    tn.setAttribute("rel",rel);
    //                    tn.setAttribute("href","/realty21/css/IE7Default.css");
    //                  }
    //                  else if (browserVer == 8.0) {
    //                    tn.setAttribute("rel",rel);
    //                    tn.setAttribute("href","/realty21/css/IE8Default.css");
    //                  }
    //                }
                // ����� �������� ����� �������
                tn.setAttribute("href","/realty21/css/"+IEVER_PSEVDO+"Default.css"); //${_IE_VER}Default.css");
              }
            }
          }
          else {
            String id = tn.getAttribute("id");
            if (id != null) {
              if (id.endsWith("__save_change_date::content")) {
                // Data ���������:
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
                // ������:
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

  public String getHtmlForView(Node html,String idDocType,Double ieBrowserVer,String serverPath) {
    String browserGenerator = getBrowserGenerator(html);
    if (browserGenerator == null) {
      browserGenerator = "7";
    }
    int i = browserGenerator.indexOf(".");
    if (i >= 0) {
      browserGenerator = browserGenerator.substring(0,i);
    }
    double nBrowserGenerator = Double.parseDouble(browserGenerator);
    //====================
    if ((ieBrowserVer != null && ieBrowserVer < 8) ||
        (nBrowserGenerator < 8)
       ) 
    {
      // 7-� ������ �������� !
      // ������ ��� ����� � <div ���� � ��� ���� POSITION: absolute
      removeStylesDivWithPositionAbsolute(html);
    }
    //====================
    String rc = html.toHtml(false);
    //rc = StringFunc.replace(rc,IEVER_PSEVDO,"IE"+browserGenerator);
    if (ieBrowserVer == null) {
      ieBrowserVer = 7.0;
    }
    String sIeBrowserVer = ieBrowserVer.toString();
    i = sIeBrowserVer.indexOf(".");
    if (i >= 0) {
      sIeBrowserVer = sIeBrowserVer.substring(0,i);
    }
    rc = StringFunc.replace(rc,IEVER_PSEVDO,"IE"+sIeBrowserVer);
    return rc;
  }
  //---------------------------------
  protected static void insertMetaForBrowser(Node html,double browserVer)
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
    meta.setAttribute("http-equiv","X-UA-Compatible");
    meta.setAttribute("content","IE=7");
    //htmlNode.getChildren().add(meta);
    //Node[] childs = htmlNode.getChildrenAsNodeArray();
    NodeList newNodeList = new NodeList(meta);
    newNodeList.add(htmlNode.getChildren());
    htmlNode.setChildren(newNodeList);
  }

  protected String getBrowserGenerator(Node html) {
    String rc = null;
    NodeList childs = html.getChildren();
    if (childs == null) {
      return rc;
    }
    int size = childs.size();
    for(int i=0; i < size; i++) {
      Node item = childs.elementAt(i);
      if (!(item instanceof MetaTag)) {
        continue;
      }
      MetaTag meta = (MetaTag)item;
      String name = meta.getAttribute("name");
      if (name == null) {
        continue;
      }
      if (!name.equals(NAME_BROWSER_GENERATOR)) {
        continue;
      }
      rc = meta.getAttribute("content");
      break;
    } // for
    return rc;
  }

  protected void setBrowserGenerator(Node html,Double ieBrowserVer) {
    if (ieBrowserVer == null) {
      return;
    }
    MetaTag meta = new MetaTag();
    meta.setAttribute("name",NAME_BROWSER_GENERATOR);
    meta.setAttribute("content",ieBrowserVer.toString());
    NodeList childs = html.getChildren();
    if (childs == null) {
      childs = new NodeList(meta);
      html.setChildren(childs);
    }
    else {
      childs.add(meta);
    }
  }

  protected void removeStylesDivWithPositionAbsolute(Node html) {
    //if (true) return;
    List<Node> rc = new ArrayList<Node>();
    HtmlParserUtils.getAllNodes(rc,html);
    for(Node n : rc) 
    {
      if (n instanceof Div || n instanceof Span) {
        CompositeTag div = (CompositeTag)n;
        String style = div.getAttribute("style");
        if (style == null) {
          continue;
        }
        style = style.trim();
        if (style.length()==0) {
          continue;
        }
        String upperStyle = style.toUpperCase();
        if (upperStyle.contains("POSITION: ABSOLUTE")) {
          div.removeAttribute("style");
          continue;
        }
        if (upperStyle.contains("POSITION:ABSOLUTE")) {
          div.removeAttribute("style");
          continue;
        }
      } // if
    } // for
  }
  //----------------------
  protected void removeDivByClass(Node html,String cssClass, int count) 
  {
    List<Node> rc = new ArrayList<Node>();
    HtmlParserUtils.getAllNodes(rc,html);
    cssClass = cssClass.trim().toUpperCase();
    int cc = 0;
    for(Node n : rc) 
    {
      if (n instanceof Div) {
        Div div = (Div)n;
        Object oClass = div.getAttribute("class");
        if (oClass == null) {
          continue;
        }
        if (oClass.toString().trim().toUpperCase().equals(cssClass)) {
          if (count > 0) {
            if (cc < count) {
              try {
                n.getParent().getChildren().remove(n);
                cc++;
              }
              catch (Exception ex) {
                ex.printStackTrace();
              }
              if (cc == count) {
                break;
              }
            }
          } // count <= 0
          else {
            try {
              n.getParent().getChildren().remove(n);
              cc++;
            }
            catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        } // if
      } // if
    } //for
  }
  
  protected void removeLinkToCss(Node html) {
    List<Node> rc = new ArrayList<Node>();
    HtmlParserUtils.getAllNodes(rc,html);
    for(Node n : rc) {
      if (n instanceof TagNode) 
      {
        TagNode tn = (TagNode) n;
        if ("LINK".equals(tn.getTagName().toUpperCase())) {
          String rel  = tn.getAttribute("rel");
          if (rel != null && "stylesheet".equals(rel.toLowerCase())) {
            tn.removeAttribute("rel");
            tn.removeAttribute("href");
            break;
          } 
        } // if link
      } // if
    } // for
  }
}
