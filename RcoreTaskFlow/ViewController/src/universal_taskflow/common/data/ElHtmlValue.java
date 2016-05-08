package universal_taskflow.common.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

import java.util.Map;

import universal_taskflow.common.beans.ELBean;

/**
 * Для хранения выражений El и HTML для каждого поля
 */
@XStreamAlias("ElHtmlValue")
public class ElHtmlValue implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String el = null;
  private String html = null;

  public void setEl(String el)
  {
    this.el = el;
  }

  public String getEl()
  {
    return el;
  }

  public void setHtml(String html)
  {
    this.html = html;
  }

  public String getHtml()
  {
    return html;
  }
  public String getValue() 
  {
    String rc = null;
    if (el != null) 
    {
      rc = el;
    }
    else if (html != null) 
    {
      rc = html;
    }
    return rc;
  }
  
  public Object getResolvedValue() 
  {
    Object rc = null;
    if (el != null) 
    {
      rc = ELBean.getInstance().resolveExpression(el);
    }
    else if (html != null) 
    {
      rc = html;
    }
    return rc;
  }
  
  public Object getResolvedValue(Map<String,Object> row) 
  {
    Object rc = null;
    if (el != null) 
    {
      rc = ELBean.getInstance().resolveExpression(el,row);
    }
    else if (html != null) 
    {
      rc = html;
    }
    return rc;
  }
}
