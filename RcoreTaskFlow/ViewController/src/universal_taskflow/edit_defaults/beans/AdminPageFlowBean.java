package universal_taskflow.edit_defaults.beans;


import com.rcore.global.jsf.JSFUtils;

import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.output.RichMessage;

import universal_taskflow.common.beans.BaseViewBean;
import universal_taskflow.common.data.DataInfo;


public class AdminPageFlowBean extends BaseViewBean
{
  private static final long serialVersionUID = 1L;
  private DataInfo dataInfo = null;
  private transient RichMessage messageInStatusLine = null;
  //-------------------------------
  private transient RichPanelGroupLayout panelTrainHeader = null;
  //private transient List<MainRecord> mainRecordTableModel = null;
  //-------------------------------
  private transient ExpressionTypeInput expressionTypeInput = null;
  public AdminPageFlowBean()
  {
    super();
  }
  
  public void initTaskFlow()
  {
    super.initTaskFlow();
  }
  
  protected void finalize() throws Throwable 
  {
    super.finalize();
  }
  
  public void setExpressionTypeInput(ExpressionTypeInput expressionTypeInput)
  {
    this.expressionTypeInput = expressionTypeInput;
  }

  public ExpressionTypeInput getExpressionTypeInput()
  {
    return expressionTypeInput;
  }
  
  
  public static AdminPageFlowBean getInstance() 
  {
    return (AdminPageFlowBean)(JSFUtils.resolveExpression("#{pageFlowScope.adminPageFlowBean}"));
  }

  public void setDataInfo(DataInfo dataInfo)
  {
    this.dataInfo = dataInfo;
  }

  public DataInfo getDataInfo()
  {
    return dataInfo;
  }
  //---------------------------
  // Статусная строка
  public void clearStatusMessage() 
  {
    setStatusMessage(MessageType.none,null);
  }
  public void setStatusMessage(MessageType type,String message) 
  {
    if (messageInStatusLine != null) 
    {
      messageInStatusLine.setRendered(message != null);
      messageInStatusLine.setMessage(message);
      messageInStatusLine.setMessageType(type.name());
      addPartialTarget(messageInStatusLine);
    }
  }

  public void setMessageInStatusLine(RichMessage messageInStatusLine)
  {
    this.messageInStatusLine = messageInStatusLine;
  }

  public RichMessage getMessageInStatusLine()
  {
    return messageInStatusLine;
  }
  //--------------------------------
//  public String okActionInPopupEL()
//  {
//    if (expressionTypeInput.equals(ExpressionTypeInput.EL))
//    {
//      String exp = expressionTypeInput.getInputText();
//      if (StringFunc.isEmpty(exp))
//      {
//        JSFUtils.addFacesErrorMessage("Значение EL пусто");
//        return null;
//      }
//      try
//      {
//        JSFUtils.resolveExpression(exp);
//      }
//      catch (Exception e)
//      {
//        JSFUtils.addFacesErrorMessage("Ошибка в EL выражении : " + e.getMessage());
//        return null;
//      }
//      //----------------------------
//      elHtmlValueCurrent.setEl(exp);
//      elHtmlValueCurrent.setHtml(null);
//      getMainRecord().getElHtmlValues().put(elHtmlKeyCurrent, elHtmlValueCurrent);
//    }
//    else if (expressionTypeInput.equals(ExpressionTypeInput.HTML))
//    {
//      String exp = expressionTypeInput.getInputText();
//      if (StringFunc.isEmpty(exp))
//      {
//        JSFUtils.addFacesErrorMessage("Значение HTML - пусто");
//        return null;
//      }
//      elHtmlValueCurrent.setEl(null);
//      elHtmlValueCurrent.setHtml(expressionTypeInput.getInputText());
//      getMainRecord().getElHtmlValues().put(elHtmlKeyCurrent, elHtmlValueCurrent);
//    }
//    else if (expressionTypeInput.equals(ExpressionTypeInput.LITERAL))
//    {
//      getMainRecord().getElHtmlValues().remove(elHtmlKeyCurrent);
//    }
//    elHtmlPopup.hide();
//    return null;
//  }

  //--------------------------------
  public static void main(String[] args)
  {
    //Class clAdmin = AdminPageFlowBean.class;
    Class clBase  = BaseViewBean.class;
    System.out.println("@="+clBase.isInstance(new AdminPageFlowBean()));
  }

  public void setPanelTrainHeader(RichPanelGroupLayout panelTrainHeader)
  {
    this.panelTrainHeader = panelTrainHeader;
  }

  public RichPanelGroupLayout getPanelTrainHeader()
  {
    return panelTrainHeader;
  }
  //--------------------------------
}
