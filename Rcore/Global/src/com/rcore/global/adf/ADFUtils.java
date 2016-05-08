package com.rcore.global.adf;
// ШМЯ

import com.rcore.global.jsf.JSFUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCParameter;
import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.ControlBinding;
import oracle.jbo.ApplicationModule;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.mom.CpxUtils;
import oracle.jbo.uicli.mom.JUApplicationDefImpl;
import oracle.jbo.uicli.mom.JUMetaObjectManager;


//import oracle.srdemo.model.common.SRService;
/**
 * A series of convenience functions for dealing with ADF Bindings.
 */
public class ADFUtils {
  /**
   * Constant for signalling failed SRService checkout during eager test.
   */
  public static final String SRSERVICE_CHECKOUT_FAILED = "SRServiceFailed";

  /**
   * Get application module for an application module data control by name.
   * @param name application module data control name
   * @return ApplicationModule
   * При наличии FacesContext работает - проверенно
   * Пример:
   * ApplicationModule app = ADFUtils.getApplicationModuleForDataControl("AppModuleDataControl");
   * Однако на форме должен быть хотя бы один элемент из этого DataControl,
   * если его нет - возвратит null
   */
  public static ApplicationModule getApplicationModuleForDataControl(String name) {
    return (ApplicationModule) JSFUtils.resolveExpression("#{data."+name+".dataProvider}");
  }
  /**
   * A convenience method for getting the value of a bound attribute in the
   * current page context programatically.
   * @param attributeName of the bound value in the pageDef
   * @return value of the attribute
   * Работает, пример:
   * String val = (String)(ADFUtils.getBoundAttributeValue("DepartmentName"));
   */
  public static Object getBoundAttributeValue(String attributeName) {
    return findControlBinding(attributeName).getInputValue();
  }

  /**
   * A convenience method for setting the value of a bound attribute in the
   * context of the current page.
   * @param attributeName of the bound value in the pageDef
   * @param value to set
   * Работает, пример:
   * ADFUtils.setBoundAttributeValue("DepartmentName","Admin1");
   * Важное замечание:
   * При использовании не требуется обеспечивать связи через partialTriggers.
   * Метод обновляет все связанные контроли!
   */
  public static void setBoundAttributeValue(String attributeName, 
                                            Object value)
  {
    findControlBinding(attributeName).setInputValue(value);
  }

  /**
   * Returns the evaluated value of a pageDef parameter.
   * @param pageDefName reference to the page definition file of the page with the parameter
   * @param parameterName name of the pagedef parameter
   * @return evaluated value of the parameter as a String
   * Использование параметров на странице pageDef:
   * <parameters>
   *   <parameter id="par1" value="parameter1"></parameter>
   * </parameters>
   * 
   * На странице .jsf:
   * <af:document title="#{bindings.par1}" id="d1">
   * ...
   * </af:document>
   * 
   * Метод работает, пример:
   * Object obj = ADFUtils.getPageDefParameterValue("test_test1PageDef", "par1");
   * где test_test1PageDef - берется из DataBindings.cpx:
   * <pageMap>
   *    <page path="/test1.jsf" usageId="test_test1PageDef" />
   * </pageMap>
   */
  public static Object getPageDefParameterValue(String pageDefName, String parameterName)
  {
    BindingContainer bindings = findBindingContainer(pageDefName);
    DCParameter param = ((DCBindingContainer) bindings).findParameter(parameterName);
    return param.getValue();
  }
  
  /**
   * Мой метод 
   * @param parameterName
   * @return value
   */
  public static Object getPageDefParameterValue(String parameterName)
  {
    DCBindingContainer bindings = (DCBindingContainer)(JSFUtils.resolveExpression("#{bindings}"));
    DCParameter param = bindings.findParameter(parameterName);
    return param.getValue();
  }
  
  /**
   * Мой метод 
   * @return Map
   */
  public static Map getPageDefParametersMap()
  {
    DCBindingContainer bindings = (DCBindingContainer)(JSFUtils.resolveExpression("#{bindings}"));
    Map rc = bindings.getParametersMap();
    return rc;
  }
  
  /**
   * Мой метод 
   * @return Map
   */
  public static Map getPageDefParametersMap(String pageDefName)
  {
    DCBindingContainer bindings = (DCBindingContainer)(findBindingContainer(pageDefName));
    Map rc = bindings.getParametersMap();
    return rc;
  }

  /**
   * @param bindingContainer
   * @param attributeName
   * @return
   * Работает, пример:
   * AttributeBinding attrBind = ADFUtils.findControlBinding(ADFUtils.getBindingContainer(),"DepartmentName");
   * System.out.println("attrBind = "+attrBind); // Administration
   */
  public static AttributeBinding findControlBinding(DCBindingContainer bindingContainer, String attributeName)
  {
    if (attributeName != null)
    {
      if (bindingContainer != null)
      {
        ControlBinding ctrlBinding = bindingContainer.getControlBinding(attributeName);
        if (ctrlBinding instanceof AttributeBinding)
        {
          return (AttributeBinding) ctrlBinding;
        }
      }
    }
    return null;
  }
  
  public static AttributeBinding findControlBinding(String attributeName)
  {
    return findControlBinding(getDCBindingContainer(),attributeName);
  }
  

  /**
   * Return the current page's binding container.
   * @return the current page's binding container
   */
  public static DCBindingContainer getBindingContainer() {
    Object rc = JSFUtils.resolveExpression("#{bindings}");
    return (DCBindingContainer)rc;
  }

  /**
   * Return the Binding Container as a DCBindingContainer.
   * @return current binding container as a DCBindingContainer
   */
  public static DCBindingContainer getDCBindingContainer() {
    return getBindingContainer();
  }
  
  /**
   * Работает, пример:
   * ApplicationModule app = ADFUtils.getBindingApplicationModule();
   */
  public static ApplicationModule getBindingApplicationModule() 
  {
    return getDCBindingContainer().getDataControl().getApplicationModule();
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding.
   * 
   * Uses the value of the 'valueAttrName' attribute as the key for
   * the SelectItem key.
   * 
   * @param iteratorName ADF iterator binding name
   * @param valueAttrName name of the value attribute to use
   * @param displayAttrName name of the attribute from iterator rows to display
   * @return ADF Faces SelectItem for an iterator binding
   */
  public static List<SelectItem> selectItemsForIterator(String iteratorName, 
                                                        String valueAttrName, 
                                                        String displayAttrName)
  {
    return selectItemsForIterator(findIterator(iteratorName), 
                                  valueAttrName, displayAttrName);
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding with description.
   * 
   * Uses the value of the 'valueAttrName' attribute as the key for
   * the SelectItem key.
   * 
   * @param iteratorName ADF iterator binding name
   * @param valueAttrName name of the value attribute to use
   * @param displayAttrName name of the attribute from iterator rows to display
   * @param descriptionAttrName name of the attribute to use for description
   * @return ADF Faces SelectItem for an iterator binding with description
   */
  public static List<SelectItem> selectItemsForIterator(String iteratorName, 
                                                        String valueAttrName, 
                                                        String displayAttrName, 
                                                        String descriptionAttrName) 
  {
    return selectItemsForIterator(findIterator(iteratorName), valueAttrName, 
                                  displayAttrName, descriptionAttrName);
  }

  /**
   * Get List of attribute values for an iterator.
   * @param iteratorName ADF iterator binding name
   * @param valueAttrName value attribute to use
   * @return List of attribute values for an iterator
   */
  public static List attributeListForIterator(String iteratorName, 
                                              String valueAttrName) {
    return attributeListForIterator(findIterator(iteratorName), valueAttrName);
  }

  /**
   * Get List of Key objects for rows in an iterator.
   * @param iteratorName iterabot binding name
   * @return List of Key objects for rows
   */
  public static List<Key> keyListForIterator(String iteratorName) {
    return keyListForIterator(findIterator(iteratorName));
  }

  /**
   * Get List of Key objects for rows in an iterator.
   * @param iter iterator binding
   * @return List of Key objects for rows
   */
  public static List<Key> keyListForIterator(DCIteratorBinding iter) {
    List<Key> attributeList = new ArrayList<Key>();
    for (Row r: iter.getAllRowsInRange()) {
      attributeList.add(r.getKey());
    }
    return attributeList;
  }

  /**
   * Get List of Key objects for rows in an iterator using key attribute.
   * @param iteratorName iterator binding name
   * @param keyAttrName name of key attribute to use
   * @return List of Key objects for rows
   */
  public static List<Key> keyAttrListForIterator(String iteratorName, 
                                                 String keyAttrName) {
    return keyAttrListForIterator(findIterator(iteratorName), keyAttrName);
  }

  /**
   * Get List of Key objects for rows in an iterator using key attribute.
   * 
   * @param iter iterator binding
   * @param keyAttrName name of key attribute to use
   * @return List of Key objects for rows
   */
  public static List<Key> keyAttrListForIterator(DCIteratorBinding iter, 
                                                 String keyAttrName) {
    List<Key> attributeList = new ArrayList<Key>();
    for (Row r: iter.getAllRowsInRange()) {
      attributeList.add(new Key(new Object[] { r.getAttribute(keyAttrName) }));
    }
    return attributeList;
  }

  /**
   * Get a List of attribute values for an iterator.
   * 
   * @param iter iterator binding
   * @param valueAttrName name of value attribute to use
   * @return List of attribute values
   */
  public static List attributeListForIterator(DCIteratorBinding iter, 
                                              String valueAttrName) {
    List attributeList = new ArrayList();
    for (Row r: iter.getAllRowsInRange()) {
      attributeList.add(r.getAttribute(valueAttrName));
    }
    return attributeList;
  }

  /**
   * Find an iterator binding in the current binding container by name.
   * 
   * @param name iterator binding name
   * @return iterator binding
   */
  public static DCIteratorBinding findIterator(String name) {
    DCIteratorBinding iter = getDCBindingContainer().findIteratorBinding(name);
    if (iter == null) {
      throw new RuntimeException("Iterator '" + name + "' not found");
    }
    return iter;
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding with description.
   * 
   * Uses the value of the 'valueAttrName' attribute as the key for
   * the SelectItem key.
   * 
   * @param iter ADF iterator binding
   * @param valueAttrName name of value attribute to use for key
   * @param displayAttrName name of the attribute from iterator rows to display
   * @param descriptionAttrName name of the attribute for description
   * @return ADF Faces SelectItem for an iterator binding with description
   */
  public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, 
                                                        String valueAttrName, 
                                                        String displayAttrName, 
                                                        String descriptionAttrName)
  {
    List<SelectItem> selectItems = new ArrayList<SelectItem>();
    for (Row r: iter.getAllRowsInRange())
    {
      selectItems.add(new SelectItem(r.getAttribute(valueAttrName), 
                                     (String) r.getAttribute(displayAttrName), 
                                     (String) r.getAttribute(descriptionAttrName)));
    }
    return selectItems;
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding.
   * 
   * Uses the value of the 'valueAttrName' attribute as the key for
   * the SelectItem key.
   * 
   * @param iter ADF iterator binding
   * @param valueAttrName name of value attribute to use for key
   * @param displayAttrName name of the attribute from iterator rows to display
   * @return ADF Faces SelectItem for an iterator binding
   */
  public static List<SelectItem> selectItemsForIterator(DCIteratorBinding iter, 
                                                        String valueAttrName, 
                                                        String displayAttrName)
  {
    List<SelectItem> selectItems = new ArrayList<SelectItem>();
    for (Row r: iter.getAllRowsInRange())
    {
      selectItems.add(new SelectItem(r.getAttribute(valueAttrName), 
                                     (String) r.getAttribute(displayAttrName)));
    }
    return selectItems;
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding.
   * 
   * Uses the rowKey of each row as the SelectItem key.
   * 
   * @param iteratorName ADF iterator binding name
   * @param displayAttrName name of the attribute from iterator rows to display
   * @return ADF Faces SelectItem for an iterator binding
   */
  public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, 
                                                             String displayAttrName)
  {
    return selectItemsByKeyForIterator(findIterator(iteratorName), 
                                       displayAttrName);
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding with discription.
   * 
   * Uses the rowKey of each row as the SelectItem key.
   * 
   * @param iteratorName ADF iterator binding name
   * @param displayAttrName name of the attribute from iterator rows to display
   * @param descriptionAttrName name of the attribute for description
   * @return ADF Faces SelectItem for an iterator binding with discription
   */
  public static List<SelectItem> selectItemsByKeyForIterator(String iteratorName, 
                                                             String displayAttrName, 
                                                             String descriptionAttrName)
  {
    return selectItemsByKeyForIterator(findIterator(iteratorName), 
                                       displayAttrName, 
                                       descriptionAttrName);
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding with discription.
   * 
   * Uses the rowKey of each row as the SelectItem key.
   * 
   * @param iter ADF iterator binding
   * @param displayAttrName name of the attribute from iterator rows to display
   * @param descriptionAttrName name of the attribute for description
   * @return ADF Faces SelectItem for an iterator binding with discription
   */
  public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, 
                                                             String displayAttrName, 
                                                             String descriptionAttrName)
  {
    List<SelectItem> selectItems = new ArrayList<SelectItem>();
    for (Row r: iter.getAllRowsInRange())
    {
      selectItems.add(new SelectItem(r.getKey(), 
                                     (String) r.getAttribute(displayAttrName), 
                                     (String) r.getAttribute(descriptionAttrName)));
    }
    return selectItems;
  }

  /**
   * Get List of ADF Faces SelectItem for an iterator binding.
   * 
   * Uses the rowKey of each row as the SelectItem key.
   * 
   * @param iter ADF iterator binding
   * @param displayAttrName name of the attribute from iterator rows to display
   * @return List of ADF Faces SelectItem for an iterator binding
   */
  public static List<SelectItem> selectItemsByKeyForIterator(DCIteratorBinding iter, 
                                                             String displayAttrName) {
    List<SelectItem> selectItems = new ArrayList<SelectItem>();
    for (Row r: iter.getAllRowsInRange()) {
      selectItems.add(new SelectItem(r.getKey(), 
                                     (String)r.getAttribute(displayAttrName)));
    }
    return selectItems;
  }

  
  /**
   * Find the BindingContainer for a page definition by name.
   *
   * Typically used to refer eagerly to page definition parameters. It is
   * not best practice to reference or set bindings in binding containers
   * that are not the one for the current page.
   *
   * @param pageDefName name of the page defintion XML file to use
   * @return BindingContainer ref for the named definition
   * Работает
   */
  private static BindingContainer findBindingContainer(String pageDefName)
  {
    BindingContext bctx = getDCBindingContainer().getBindingContext();
    BindingContainer foundContainer = bctx.findBindingContainer(pageDefName);
    return foundContainer;
  }
  //--------- MARK -----------------
  /**
   * Для El выражений типа #{bindings.CreateInsert.execute}
   * Вызов ADFUtils.adfBindingActionListener("CreateInsert",actionEvent);.
   * @param action
   * @param actionEvent
   * Работает
   */
  public static void adfBindingActionListener(String action, ActionEvent actionEvent) 
  {
    DCBindingContainer binding = (DCBindingContainer)(JSFUtils.resolveExpression("#{bindings}"));
    Object actionBinding = binding.get(action); // Это на самом деле - FacesCtrlActionBinding, но Model об этом не должна знать
    // actionBinding.execute(actionEvent) :
    Method method = null;
    try
    {
      method = actionBinding.getClass().getMethod("execute", new Class[]{ActionEvent.class});
      method.invoke(actionBinding, actionEvent);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return;
  }
  //------------- Databindings.cpx ------------------
  /**
   * Возвращает список всех DataBindings.cpx из всех adfm.xml как классов
   * Вид пути : com.rdtex.portal.DataBindings.cpx
   */
  public static List<String> getCpxListFromMetadata() 
  {
    return CpxUtils.getCpxListFromMetadata();
  }
  
  /**
   * Аналогично предыдущему, но для сужения возврата используется фильтр
   * @param filter например "rdtex.", может быть null
   * @return список всех DataBindings.cpx из всех adfm.xml как классов, например {com.rdtex.portal.DataBindings.cpx}
   */
  public static List<String> getCpxListFromMetadata(String filter) 
  {
    List<String> all = CpxUtils.getCpxListFromMetadata();
    if (filter == null) 
    {
      return all;
    }
    List<String> rc = new ArrayList<String>();
    for(String cpx : all) 
    {
      if (cpx.contains(filter)) 
      {
        rc.add(cpx);
      }
    }
    return rc;      
  }
  
  /**
   * Аналогично предыдущему
   * Параметр может быть null
   */
  public static List<JUApplicationDefImpl> getCpxListAsMetaObjects(String filter) 
  {
    List<String> listStr = getCpxListFromMetadata(filter);
    List<JUApplicationDefImpl> rc = new ArrayList<JUApplicationDefImpl>();
    for(String cpx : listStr) 
    {
      rc.add(JUMetaObjectManager.findCpx(cpx));
    }
    return rc;      
  }
  /**
   *
   * @param filterCpx - может быть null
   * @return - список путей к страницам, содержащим pageDef
   */
  public static Set<String> getPagesWithPageDef(String filterCpx) 
  {
    List<JUApplicationDefImpl> listMeta = getCpxListAsMetaObjects(filterCpx);
    Set<String> rc = new HashSet<String>();
    for(JUApplicationDefImpl meta : listMeta) 
    {
      Map<String,String> map = meta.getPageMap();
      if (map != null) {
        rc.addAll(map.keySet());
      }
    }
    return rc;      
  }
  
  /**
   *
   * @param cpxPath - точное значение для пути к Databindings.cpx
   * @return список путей к страницам, содержащим pageDef, например - "com.rdtex.admin.DataBindings.cpx"
   */
  public static Set<String> getPagesWithPageDefByCpxPath(String cpxPath) 
  {
    Set<String> rc = new HashSet<String>();
    JUApplicationDefImpl meta = JUMetaObjectManager.findCpx(cpxPath);
    if (meta != null) {
      Map<String,String> map = meta.getPageMap();
      if (map != null) {
        rc.addAll(map.keySet());
      }
    }
    return rc;      
  }
  public static Set<String> getPagesWithPageDef()
  {
    return getPagesWithPageDef(null);
  }
  //-------------------------------------------------
  public static void main(String[] args)
  {
    ;
  } // main
}
