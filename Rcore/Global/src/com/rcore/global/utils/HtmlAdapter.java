package com.rcore.global.utils;
// ШМЯ
import org.htmlparser.Node;

public interface HtmlAdapter {
  void adoptHtmlForSave(Node html,String idDocType,boolean isAcceptMode,Double ieBrowserVer,String serverPath);
  String getHtmlForView(Node html,String idDocType,Double ieBrowserVer,String serverPath);
}
