package com.rcore.global.jsf;
// ШМЯ

import java.io.IOException;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.NullValue;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.RowID;
import oracle.jbo.domain.Timestamp;


/**
 * General useful static utilies for working with JSF.
 * NOTE: Updated to use JSF 1.2 ExpressionFactory.
 *
 * @author Duncan Mills
 * @author Steve Muench
 *
 * $Id: JSFUtils.java 1885 2007-06-26 00:47:41Z ralsmith $
 */
public class JSFUtils
{

  private static final String NO_RESOURCE_FOUND = "Missing resource: ";

  /**
   * Method for taking a reference to a JSF binding expression and returning
   * the matching object (or creating it).
   * @param expression EL expression
   * @return Managed object
   * Работает
   */
  public static Object resolveExpression(String expression)
  {
    FacesContext facesContext = getFacesContext();
    return resolveExpression(expression,facesContext);
  }

  public static Object resolveExpression(String expression,FacesContext facesContext)
  {
    Application app = facesContext.getApplication();
    ExpressionFactory elFactory = app.getExpressionFactory();
    ELContext elContext = facesContext.getELContext();
    ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
    return valueExp.getValue(elContext);
  }

  /**
   * Непонятно - возвращает null
   */
  public static String resolveRemoteUser()
  {
    FacesContext facesContext = getFacesContext();
    return resolveRemoteUser(facesContext);
  }
  public static String resolveRemoteUser(FacesContext facesContext)
  {
    ExternalContext ectx = facesContext.getExternalContext();
    return ectx.getRemoteUser();
  }

  /**
   * Непроверенно, в обычных условиях - вызывает Exception
   */
  public static String resolveUserPrincipal()
  {
    FacesContext facesContext = getFacesContext();
    return resolveUserPrincipal(facesContext);
  }
  public static String resolveUserPrincipal(FacesContext facesContext)
  {
    ExternalContext ectx = facesContext.getExternalContext();
    HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
    return request.getUserPrincipal().getName();
  }

  public static Object resolveMethodExpression(
    String expression,
    Class returnType,
    Class[] argTypes,
    Object[] argValues)
  {
    FacesContext facesContext = getFacesContext();
    return resolveMethodExpression(expression,returnType,argTypes,argValues,facesContext);
  }
  
  
  public static Object resolveMethodExpression(
    String expression,
    Class returnType,
    Class[] argTypes,
    Object[] argValues,
    FacesContext facesContext
    )
  {
    Application app = facesContext.getApplication();
    ExpressionFactory elFactory = app.getExpressionFactory();
    ELContext elContext = facesContext.getELContext();
    MethodExpression methodExpression = elFactory.createMethodExpression(elContext, expression, returnType, argTypes);
    return methodExpression.invoke(elContext, argValues);
  }
  
  //-------------
  public static MethodExpression createMethodExpressionForAction(String expression)
  {
    return createMethodExpression(expression,String.class,new Class[0]);
  }
  
  public static MethodExpression createMethodExpression(
    String expression,
    Class returnType,
    Class[] argTypes
  )
  {
    return createMethodExpression(expression,returnType,argTypes,getFacesContext());
  }
  
  public static MethodExpression createMethodExpression(
    String expression,
    Class returnType,
    Class[] argTypes,
    FacesContext facesContext
    )
  {
    Application app = facesContext.getApplication();
    ExpressionFactory elFactory = app.getExpressionFactory();
    ELContext elContext = facesContext.getELContext();
    MethodExpression methodExpression = elFactory.createMethodExpression(elContext, 
                                                                         expression, 
                                                                         returnType, 
                                                                         argTypes);
    return methodExpression;
  }
  //-------------    

  /**
   * Method for taking a reference to a JSF binding expression and returning
   * the matching Boolean.
   * @param expression EL expression
   * @return Managed object
   */
  public static Boolean resolveExpressionAsBoolean(String expression)
  {
    return (Boolean) resolveExpression(expression);
  }

  /**
   * Method for taking a reference to a JSF binding expression and returning
   * the matching String (or creating it).
   * @param expression EL expression
   * @return Managed object
   */
  public static String resolveExpressionAsString(String expression)
  {
    return (String) resolveExpression(expression);
  }

  /**
   * Convenience method for resolving a reference to a managed bean by name
   * rather than by expression.
   * @param beanName name of managed bean
   * @return Managed object
   */
  public static Object getManagedBeanValue(String beanName)
  {
    StringBuffer buff = new StringBuffer("#{");
    buff.append(beanName);
    buff.append("}");
    return resolveExpression(buff.toString());
  }

  /**
   * Method for setting a new object into a JSF managed bean
   * Note: will fail silently if the supplied object does
   * not match the type of the managed bean.
   * @param expression EL expression
   * @param newValue new value to set
   * Работает
   */
  public static void setExpressionValue(String expression, Object newValue)
  {
    FacesContext facesContext = getFacesContext();
    setExpressionValue(expression, newValue, facesContext);
  }

  public static void setExpressionValue(String expression, Object newValue, FacesContext facesContext)
  {
    Application app = facesContext.getApplication();
    ExpressionFactory elFactory = app.getExpressionFactory();
    ELContext elContext = facesContext.getELContext();
    ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);

    //Check that the input newValue can be cast to the property type
    //expected by the managed bean.
    //If the managed Bean expects a primitive we rely on Auto-Unboxing
    Class bindClass = valueExp.getType(elContext);
    if (bindClass.isPrimitive() || bindClass.isInstance(newValue))
    {
      valueExp.setValue(elContext, newValue);
    }
  }

  /**
   * Convenience method for setting the value of a managed bean by name
   * rather than by expression.
   * @param beanName name of managed bean
   * @param newValue new value to set
   */
  public static void setManagedBeanValue(String beanName, Object newValue)
  {
    StringBuffer buff = new StringBuffer("#{");
    buff.append(beanName);
    buff.append("}");
    setExpressionValue(buff.toString(), newValue);
  }
  public static void setManagedBeanValue(String beanName, Object newValue, FacesContext facesContext)
  {
    StringBuffer buff = new StringBuffer("#{");
    buff.append(beanName);
    buff.append("}");
    setExpressionValue(buff.toString(), newValue, facesContext);
  }


  /**
   * Convenience method for setting Session variables.
   * @param key object key
   * @param object value to store
   */

  public static void storeOnSession(String key, Object object)
  {
    FacesContext ctx = getFacesContext();
    Map sessionState = ctx.getExternalContext().getSessionMap();
    sessionState.put(key, object);
  }

  public static void putToSession(String key, Object object)
  {
    storeOnSession(key,object);
  }


  /**
   * Convenience method for getting Session variables.
   * @param key object key
   * @return session object for key
   */
  public static Object getFromSession(String key)
  {
    FacesContext ctx = getFacesContext();
    Map sessionState = ctx.getExternalContext().getSessionMap();
    return sessionState.get(key);
  }

  public static String getFromHeader(String key)
  {
    FacesContext ctx = getFacesContext();
    ExternalContext ectx = ctx.getExternalContext();
    return ectx.getRequestHeaderMap().get(key);
  }

  /**
   * Convenience method for getting Request variables.
   * @param key object key
   * @return request object for key
   */
  public static Object getFromRequest(String key)
  {
    FacesContext ctx = getFacesContext();
    Map requestMap = ctx.getExternalContext().getRequestMap();
    return requestMap.get(key);
  }

  /**
   * Pulls a String resource from the property bundle that
   * is defined under the application &lt;message-bundle&gt; element in
   * the faces config. Respects Locale
   * @param key string message key
   * @return Resource value or placeholder error String
   */
  public static String getStringFromBundle(String key)
  {
    ResourceBundle bundle = getBundle();
    return getStringSafely(bundle, key, null);
  }


  /**
   * Convenience method to construct a <code>FacesMesssage</code>
   * from a defined error key and severity
   * This assumes that the error keys follow the convention of
   * using <b>_detail</b> for the detailed part of the
   * message, otherwise the main message is returned for the
   * detail as well.
   * @param key for the error message in the resource bundle
   * @param severity severity of message
   * @return Faces Message object
   */
  public static FacesMessage getMessageFromBundle(String key, FacesMessage.Severity severity)
  {
    ResourceBundle bundle = getBundle();
    String summary = getStringSafely(bundle, key, null);
    String detail = getStringSafely(bundle, key + "_detail", summary);
    FacesMessage message = new FacesMessage(summary, detail);
    message.setSeverity(severity);
    return message;
  }

  /**
   * Add JSF info message.
   * @param msg info message string
   */
  public static void addFacesInformationMessage(String msg)
  {
    FacesContext ctx = getFacesContext();
    addFacesInformationMessage(msg, ctx);
  }
  public static void addFacesInformationMessage(String msg, FacesContext ctx)
  {
    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
    ctx.addMessage(getRootViewComponentId(), fm);
  }

  /**
   * Add JSF error message.
   * @param msg error message string
   */
  public static void addFacesErrorMessage(String msg)
  {
    FacesContext ctx = getFacesContext();
    addFacesErrorMessage(msg, ctx);
  }
  public static void addFacesErrorMessage(String msg, FacesContext ctx)
  {
    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "");
    ctx.addMessage(getRootViewComponentId(), fm);
  }


  public static void addFacesErrorMessage(String msg, String details)
  {
    FacesContext ctx = getFacesContext();
    addFacesErrorMessage(msg, details, ctx);
  }
  public static void addFacesErrorMessage(String msg, String details, FacesContext ctx)
  {
    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, details);
    ctx.addMessage(getRootViewComponentId(), fm);
  }

  public static void addFacesWarningMessage(String msg)
  {
    FacesContext ctx = getFacesContext();
    addFacesWarningMessage(msg, ctx);
  }

  public static void addFacesWarningMessage(String msg, FacesContext ctx)
  {
    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, "");
    ctx.addMessage(getRootViewComponentId(), fm);
  }

  /**
   * Сообщения на клиенте из java
   * @param severity
   * @param msg
   * @param details
   */
  public static void addFacesMessage(FacesMessage.Severity severity, String msg, String details)
  {
    FacesContext ctx = getFacesContext();
    addFacesMessage(severity, msg, details, ctx);
  }
  public static void addFacesMessage(FacesMessage.Severity severity, String msg, String details, FacesContext ctx)
  {
    FacesMessage fm = new FacesMessage(severity, msg, details);
    ctx.addMessage(getRootViewComponentId(), fm);
  }

  // Informational getters

  /**
   * Get view id of the view root.
   * @return view id of the view root
   */
  public static String getRootViewId()
  {
    return getFacesContext().getViewRoot().getViewId();
  }

  /**
   * Get component id of the view root.
   * @return component id of the view root
   */
  public static String getRootViewComponentId()
  {
    return getFacesContext().getViewRoot().getId();
  }

  /**
   * Get FacesContext.
   * @return FacesContext
   */
  public static FacesContext getFacesContext()
  {
    return FacesContext.getCurrentInstance();
  }
  /*
   * Internal method to pull out the correct local
   * message bundle
   */

  public static ResourceBundle getBundle()
  {
    FacesContext ctx = getFacesContext();
    return getBundle(ctx);
  }
  public static ResourceBundle getBundle(FacesContext ctx)
  {
    UIViewRoot uiRoot = ctx.getViewRoot();
    Locale locale = uiRoot.getLocale();
    ClassLoader ldr = Thread.currentThread().getContextClassLoader();
    javax.faces.application.Application app = ctx.getApplication();
    String messageBundle = app.getMessageBundle();
    ResourceBundle rc = ResourceBundle.getBundle(messageBundle, locale, ldr);
    return rc;
  }

  /**
   * Get an HTTP Request attribute.
   * @param name attribute name
   * @return attribute value
   */
  public static Object getRequestAttribute(String name)
  {
    return  getRequestAttribute(name, getFacesContext());
  }
  public static Object getRequestAttribute(String name, FacesContext ctx)
  {
    return ctx.getExternalContext().getRequestMap().get(name);
  }

  /**
   * Set an HTTP Request attribute.
   * @param name attribute name
   * @param value attribute value
   */
  public static void setRequestAttribute(String name, Object value)
  {
    setRequestAttribute(name, value, getFacesContext());
  }
  public static void setRequestAttribute(String name, Object value, FacesContext ctx)
  {
    ctx.getExternalContext().getRequestMap().put(name, value);
  }

  /*
   * Internal method to proxy for resource keys that don't exist
   */

  private static String getStringSafely(ResourceBundle bundle, String key, String defaultValue)
  {
    String resource = null;
    try
    {
      resource = bundle.getString(key);
    }
    catch (MissingResourceException mrex)
    {
      if (defaultValue != null)
      {
        resource = defaultValue;
      }
      else
      {
        resource = NO_RESOURCE_FOUND + key;
      }
    }
    return resource;
  }

  /**
   * Locate an UIComponent in view root with its component id. Use a recursive way to achieve this.
   * @param id UIComponent id
   * @return UIComponent object
   */
  public static UIComponent findComponentInRoot(String id)
  {
    return findComponentInRoot(id,FacesContext.getCurrentInstance());
  }
  public static UIComponent findComponentInRoot(String id, FacesContext facesContext)
  {
    UIComponent component = null;
    if (facesContext != null)
    {
      UIComponent root = facesContext.getViewRoot();
      component = findComponent(root, id);
    }
    return component;
  }

  /**
   * Locate an UIComponent from its root component.
   * Taken from http://www.jroller.com/page/mert?entry=how_to_find_a_uicomponent
   * @param base root Component (parent)
   * @param id UIComponent id
   * @return UIComponent object
   */
  public static UIComponent findComponent(UIComponent base, String id)
  {
    if (id.equals(base.getId()))
      return base;

    UIComponent children = null;
    UIComponent result = null;
    Iterator childrens = base.getFacetsAndChildren();
    while (childrens.hasNext() && (result == null))
    {
      children = (UIComponent) childrens.next();
      if (id.equals(children.getId()))
      {
        result = children;
        break;
      }
      result = findComponent(children, id);
      if (result != null)
      {
        break;
      }
    }
    return result;
  }

  /**
   * Method to create a redirect URL. The assumption is that the JSF servlet mapping is
   * "faces", which is the default
   *
   * @param view the JSP or JSPX page to redirect to
   * @return a URL to redirect to
   * Проверено - работает
   */
  public static String getPageURL(final String view)
  {
    FacesContext facesContext = getFacesContext();
    return getPageURL(view, facesContext);
  }
  public static String getPageURL(final String view, final FacesContext facesContext)
  {
    ExternalContext externalContext = facesContext.getExternalContext();
    String url = ((HttpServletRequest) externalContext.getRequest()).getRequestURL().toString();
    StringBuffer newUrlBuffer = new StringBuffer();
    newUrlBuffer.append(url.substring(0, url.lastIndexOf("faces/")));
    newUrlBuffer.append("faces");
    String targetPageUrl = view.startsWith("/")? view: "/" + view;
    newUrlBuffer.append(targetPageUrl);
    return newUrlBuffer.toString();
  }


  // ****************** Мои добавления ==============
  //                    Мои добавления
  // ****************** Мои добавления ==============

  public static HttpServletRequest getRequest()
  {
    return (HttpServletRequest)(getFacesContext().getExternalContext().getRequest());
  }
  public static HttpServletRequest getRequest(FacesContext ctx)
  {
    return (HttpServletRequest)(ctx.getExternalContext().getRequest());
  }
  public static HttpServletResponse getResponse()
  {
    return (HttpServletResponse)(getFacesContext().getExternalContext().getResponse());
  }
  public static HttpServletResponse getResponse(FacesContext ctx)
  {
    return (HttpServletResponse)(ctx.getExternalContext().getResponse());
  }
  public static HttpSession getSession()
  {
    return (HttpSession)(getFacesContext().getExternalContext().getSession(false));
  }
  public static Application getApplication()
  {
    FacesContext facesContext = getFacesContext();
    Application app = facesContext.getApplication();
    return app;
  }

  public static String getFullUrl(String shortUrl) // /untitled2.jspx
  {
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext eContext = context.getExternalContext();
    String contextPath = eContext.getRequestContextPath();
    //String servletPath = eContext.getRequestServletPath();
    String faces = "/faces"; //? Где-то можно взять
    String rc = eContext.encodeActionURL(contextPath + faces + shortUrl);
    return rc;
  }

  public static void redirect(String shortUrl)  throws IOException
  {
    redirect(shortUrl,FacesContext.getCurrentInstance());
  }

  public static void redirect(String shortUrl, FacesContext context)  throws IOException
  {
    String url = getFullUrl(shortUrl);
    ExternalContext eContext = context.getExternalContext();
    eContext.redirect(url);
  }

  public static String getMessageBundle(String key)
  {
    return getStringFromBundle(key);
  }

  public static String resolveDomainExpression(String exp)
  {
    return resolveDomainExpression(exp, false);
  }
  public static String resolveDomainExpression(String exp, FacesContext context)
  {
    return resolveDomainExpression(exp, false, context);
  }

  public static java.util.Date resolveDomainDate(String exp)
  {
    return resolveDomainDate(exp, getFacesContext());
  }
  public static java.util.Date resolveDomainDate(String exp, FacesContext ctx)
  {
    Object obj = resolveExpression(exp,ctx);
    java.util.Date rc = null;
    if (obj == null)
    {
      return null;
    }
    if (obj instanceof Date)
    {
      rc = ((Date) obj).getValue();
      // 2008-09-30 --> 30.09.2008
      //rc = DateTime.convertDateToDateInCalendar(rc.toString());
    }
    return rc;
  }


  public static String resolveDomainExpression(String exp, boolean isInteger)
  {
    return resolveDomainExpression(exp, isInteger, getFacesContext());
  }
  public static String resolveDomainExpression(String exp, boolean isInteger, FacesContext ctx)
  {
    Object obj = resolveExpression(exp,ctx);
    Object rc = null;
    if (obj == null)
    {
      return null;
    }
    if (obj instanceof DBSequence)
    {
      rc = ((DBSequence) obj).getValue();
    }
    else if (obj instanceof Number)
    {
      rc = ((Number) obj).getValue();
    }
    else if (obj instanceof Date)
    {
      rc = ((Date) obj).getValue();
    }
    else if (obj instanceof NullValue)
    {
      ;
    }
    else if (obj instanceof RowID)
    {
      rc = ((RowID) obj).getValue();
    }
    else if (obj instanceof Timestamp)
    {
      rc = ((Timestamp) obj).getValue();
    }
    else if (obj instanceof String)
    {
      rc = obj;
    }
    else
    {
      if (obj != null)
      {
        rc = obj.toString();
      }
    }
    if (rc == null)
    {
      return null;
    }
    String sRc = rc.toString();
    if (isInteger)
    {
      int i = sRc.indexOf('.');
      if (i > -1)
      {
        sRc = sRc.substring(0, i);
      }
    }
    return sRc;
  }
  //-------------------------
  /*
   * Example :
   * createValueExpression("#{cRReviewReportBean.input1}", String.class))
   */
  public static ValueExpression createValueExpression(String valueExpression)
  {
    return createValueExpression(valueExpression, Object.class, getFacesContext());
  }
  
  public static ValueExpression createValueExpression(String valueExpression, FacesContext context)
  {
    return createValueExpression(valueExpression, Object.class, context);
  }
  
  public static ValueExpression createValueExpression(String valueExpression, Class<?> valueType)
  {
    return createValueExpression(valueExpression, valueType, getFacesContext());
  }

  public static ValueExpression createValueExpression(
        String valueExpression,
        Class<?> valueType,
        FacesContext context
  )
  {
      return context.getApplication().getExpressionFactory()
          .createValueExpression(context.getELContext(),
                                 valueExpression,
                                 valueType);
  }

  public static MethodExpressionActionListener createActionListenerByElExp(String elExp)
  {
    FacesContext faces = FacesContext.getCurrentInstance();
    return createActionListenerByElExp(elExp,faces);
  }
  
  public static MethodExpressionActionListener createActionListenerByElExp(String elExp, FacesContext faces)
  {
    MethodExpressionActionListener rc = null;
    MethodExpression me =  faces.getApplication().getExpressionFactory().createMethodExpression(
      faces.getELContext(), elExp, null,
      new Class[] { ActionEvent.class });
    rc = new MethodExpressionActionListener(me);
    return rc;
  }

}
