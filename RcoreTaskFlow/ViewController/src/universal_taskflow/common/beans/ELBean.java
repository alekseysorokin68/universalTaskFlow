package universal_taskflow.common.beans;


import com.rcore.global.jsf.JSFUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletRequest;

import oracle.adf.share.ADFContext;


public class ELBean  extends BaseTaskFlowBean
{
  private static final long serialVersionUID = 1L;
  private transient Map<String, Object> fieldValue = new FieldValuesMap();
  private transient Map<String, Object> fieldParamValue = new ParamValuesMap();
  public static ELBean getInstance() 
  {
    return (ELBean)(JSFUtils.resolveExpression("#{elBean}"));
  }
  
  public Object resolveExpression(String el)
  {
    return resolveExpression(el, null);
  }
  
  public Object resolveExpression(String el, Map<String,Object> row) 
  {
    HttpServletRequest request = getRequest();
    request.setAttribute("elBean", this);
    if (row != null)
    {
      request.setAttribute("row", row);
    }
    else 
    {
      request.removeAttribute("row");
    }
    Object rc = JSFUtils.resolveExpression(el);
    return rc;
  }
  
  public List<SelectItem> getElSelectItems() 
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    rc.add(new SelectItem("#{elBean.today}","Текущая дата"));
    rc.add(new SelectItem("#{elBean.userName}","Текущий пользователь"));
    rc.add(new SelectItem("#{elBean.fieldValue['<имя поля основного запроса>']}",
                              "Значение поля основного запроса"));
    rc.add(new SelectItem("#{elBean.fieldParamValue['<имя параметра основного запроса>']}",
                              "Значение параметра основного запроса"));
    return rc;
  }
  public Date getToday() 
  {
    return new Date();
  }
  public String getUserName() 
  {
    return ADFContext.getCurrent().getSecurityContext().getUserName();
  }

  public Map<String, Object> getFieldValue()
  {
    return fieldValue;
  }

  public Map<String, Object> getFieldParamValue()
  {
    return fieldParamValue;
  }

  //============================================
  public static class FieldValuesMap extends HashMap<String,Object>
  {
    private static final long serialVersionUID = 1L;
    /**
     * Значение поля записи основного запроса
     * @param objName
     * @return
     */
    @Override
    public Object get(Object objName) 
    {
      Object rc = null;
      //String name = (String) objName;
      // TODO
      //String EditDefaultsBean.getInstance().getParamsForOnSql().getSqlAttributes().get(name);
      return rc;
    }
  }

  public static class ParamValuesMap extends HashMap<String,Object>
  {
    private static final long serialVersionUID = 1L;
    /**
     * Значение параметра основного запроса
     * @param objName
     * @return
     */
    @Override
    public Object get(Object objName) 
    {
      Object rc = null;
      //String name = (String) objName;
      // TODO
      //String EditDefaultsBean.getInstance().getParamsForOnSql().getSqlAttributes().get(name);
      return rc;
    }
  }
}
