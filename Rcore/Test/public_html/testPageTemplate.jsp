<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://xmlns.oracle.com/adf/faces/rich" prefix="af"%>
<f:view>
  <af:document id="d1" title="TestPageTemplate">
    <af:form id="f1">
      <af:pageTemplate viewId="/com/rcore/pagetemplate/RcoreThreeColumnLayoutPageTempl.jspx"
                       id="pt1">
        <f:facet name="center">
          <af:outputLabel value="center" id="ol6"/>
        </f:facet>
        <f:facet name="header">
          <af:outputLabel value="header" id="ol5"/>
        </f:facet>
        <f:facet name="end">
          <af:outputLabel value="end" id="ol4"/>
        </f:facet>
        <f:facet name="start">
          <af:outputLabel value="start" id="ol3"/>
        </f:facet>
        <f:facet name="copyright">
          <af:outputLabel value="copyright" id="ol2"/>
        </f:facet>
        <f:facet name="status">
          <af:outputLabel value="status" id="ol1"/>
        </f:facet>
      </af:pageTemplate>
    </af:form>
  </af:document>
</f:view>