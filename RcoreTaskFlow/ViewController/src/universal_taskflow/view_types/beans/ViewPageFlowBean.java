package universal_taskflow.view_types.beans;


import com.rcore.global.StringFunc;
import com.rcore.global.TaskFlowUtils;
import com.rcore.global.jsf.JSFUtils;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.component.UIComponent;

import oracle.adf.controller.ControllerContext;
import oracle.adf.view.rich.event.ClientListenerSet;

import universal_taskflow.common.beans.BaseViewBean;

import universal_taskflow.common.data.SqlAttribute;

import universal_taskflow.view_types.table.ColumnInfo;


public class ViewPageFlowBean extends BaseViewBean
{
  private static final long serialVersionUID = 1L;
  private transient UIComponent rootComponent = null;
  private transient UIComponent componentForStartPopupMenu = null;
  //---
  //..protected transient List<ColumnInfo> listCategories = null;
  protected transient String categoriesSignature = "";
  //---

  public ViewPageFlowBean()
  {
    super();
  }
  
  @PostConstruct
  public void initBean() 
  {
    //..listCategories = null;
    try
    {
      setNotCategoryChanged();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  //--------------- Categories: -----------------
  public List<ColumnInfo> getListCategories() throws Exception
  {
//    if (listCategories == null)
//    {
//      listCategories = getListCategoriesImpl();
//    }
//    return listCategories;
    return getListCategoriesImpl();
  }
  
  private List<ColumnInfo> getListCategoriesImpl() throws Exception
  {
    return getListCategoriesImpl(getVisibleFieldsInTable());
  }
  
  public List<SqlAttribute> getVisibleFieldsInTable() throws Exception
  {
    return getMainRecord().getTaskFlowParametersAsOnSql().getVisibleFieldsInTable();
  }
  
  public boolean isCategoryExists() throws Exception
  {
    List<ColumnInfo> list = getListCategories();
    boolean rc = (list != null && list.size() > 0);
    return rc;
  }
  
  public String getCategoriesSignature() throws Exception 
  {
    StringBuilder rc = new StringBuilder("");
    List<ColumnInfo> list = getListCategoriesImpl();
    if (list != null) 
    {
      for(ColumnInfo item : list) 
      {
        rc.append(item.getCategory()).append("_");
      }
    }
    return rc.toString();
  }
  
  public boolean isCategoryChanged()
  {
    boolean rc = false;
    try
    {
      rc = ( !getCategoriesSignature().equals(categoriesSignature) );
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }
  
  public void setNotCategoryChanged() 
  {
    try
    {
      categoriesSignature = getCategoriesSignature();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private static List<ColumnInfo> getListCategoriesImpl(List<SqlAttribute> columns)
  {
    List<ColumnInfo> rc = new ArrayList<ColumnInfo>();
    if (columns == null) 
    {
      return rc;
    }
    boolean isCategoryExist = false;
    for (SqlAttribute sqlAttribute : columns)
    {
      String category = sqlAttribute.getCategory();
      if (category != null)
      {
        isCategoryExist = true;
      }
      if (rc.size() == 0)
      {
        rc.add(new ColumnInfo(category,sqlAttribute)); // возможно category = null
      }
      else
      {
        ColumnInfo info = rc.get(rc.size()-1);
        if (StringFunc.isEmpty(category)) 
        {
          rc.add(new ColumnInfo(category,sqlAttribute)); // Для всех пустых - отдельную категорию
        }
        else 
        {
          if (category.equals(info.getCategory()))
          {
            info.getColumns().add(sqlAttribute);
          }
          else
          {
            rc.add(new ColumnInfo(category,sqlAttribute)); // возможно category = null
          }          
        }
      }
    } // for
    
    if (!isCategoryExist)
    {
      rc.clear();
    }
    return rc;
  }

  public static ViewPageFlowBean getInstance()
  {
    return (ViewPageFlowBean)(JSFUtils.resolveExpression("#{pageFlowScope.viewPageFlowBean}"));
  }
  //--------------------------------------------
  public void setComponentForStartPopupMenu(UIComponent afterContent)
  {
    this.componentForStartPopupMenu = afterContent;
    UIComponent root = rootComponent;
    
    if (root != null)
    {
      Boolean inited = (Boolean) root.getAttributes().get("inited");
      if (inited == null) 
      {
        setClientPopupMenu(root);
        root.getAttributes().put("inited", true);
      }
    }
    else 
    {
      System.err.println("@setComponentForStartPopupMenu root == null");
    }
  }

  public UIComponent getComponentForStartPopupMenu()
  {
    return componentForStartPopupMenu;
  }

  private void setClientPopupMenu(UIComponent uiComponent)
  {
    if (uiComponent == null) return;
    //---
    try
    {
      setClientPopupMenuNoRecursive(uiComponent);
    }
    catch (Exception e)
    {
      ;
      //System.err.println(e.getMessage());
    }
    Iterator<UIComponent> it = uiComponent.getFacetsAndChildren();
    if (it != null)
    {
      while (it.hasNext()) 
      {
        UIComponent item = it.next();
        setClientPopupMenu(item);
      } // while
    } // if
  } // private void setClientPopupMenu(UIComponent uiComponent)

  private void setClientPopupMenuNoRecursive(UIComponent uiComponent)
    throws Exception
  {
    Method method = uiComponent.getClass().getMethod("getClientListeners");
    ClientListenerSet clientListenerSet = null;
    if (method != null) 
    {
      clientListenerSet = (ClientListenerSet) method.invoke(uiComponent);
    }
    if (clientListenerSet == null) 
    {
      clientListenerSet = new ClientListenerSet();
    }
    clientListenerSet.addListener("contextMenu", "_rootContextMenu");
    method = uiComponent.getClass().getMethod("setClientListeners", new Class[]{ClientListenerSet.class});
    method.invoke(uiComponent, clientListenerSet);
    //-------------------------------------------
    //AdfFacesContext.getCurrentInstance().addPartialTarget(uiComponent);
  }

  public void setRootComponent(UIComponent scrollPanel)
  {
    this.rootComponent = scrollPanel;
  }

  public UIComponent getRootComponent()
  {
    return rootComponent;
  }
}
