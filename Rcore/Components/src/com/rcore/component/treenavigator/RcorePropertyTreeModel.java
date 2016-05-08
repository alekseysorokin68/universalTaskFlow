package com.rcore.component.treenavigator;

import com.sun.xml.tree.XmlDocument;

import java.sql.Connection;


public interface RcorePropertyTreeModel
{
  void setDocument(XmlDocument value);
  XmlDocument getDocument();
  void destroy();
  Connection getConnection();
}
