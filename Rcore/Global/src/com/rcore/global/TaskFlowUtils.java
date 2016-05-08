package com.rcore.global;

import java.util.Collection;

import java.util.HashMap;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowContext;
import oracle.adf.controller.TaskFlowId;
import oracle.adf.controller.ViewPortContext;
import oracle.adf.controller.metadata.MetadataService;
import oracle.adf.controller.metadata.model.DataControlScopeType;
import oracle.adf.controller.metadata.model.NamedParameter;
import oracle.adf.controller.metadata.model.TaskFlowDefinition;
import oracle.adf.controller.metadata.model.TaskFlowInputParameter;
import oracle.adf.controller.metadata.model.TransactionType;
import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.view.rich.context.AdfFacesContext;

public class TaskFlowUtils
{
  private static TaskFlowUtils instance = new TaskFlowUtils();

  private TaskFlowUtils()
  {
    super();
  }

  public static TaskFlowUtils getInstance()
  {
    return instance;
  }
  //----------------------------------------

  public String getTaskFlowName()
  {
    MetadataService metadataService = MetadataService.getInstance();
    TaskFlowDefinition taskFlowDefinition = metadataService.getTaskFlowDefinition(getCurrentTaskFlowId());
    String taskFlowName = null;
    if (taskFlowDefinition != null)
    {
      TaskFlowId taskFlowId = taskFlowDefinition.getTaskFlowId();

      if (taskFlowId != null)
        taskFlowName = taskFlowDefinition.getTaskFlowId().getFullyQualifiedName();
      else
        taskFlowName = "Null";
    }
    else
      taskFlowName = "Null";


    return taskFlowName;
  }

  public String getTaskFlowControlScopeName()
  {
    MetadataService metadataService = MetadataService.getInstance();
    String controlScopeTypeString;
    TaskFlowDefinition taskFlowDefinition = metadataService.getTaskFlowDefinition(getCurrentTaskFlowId());
    if (taskFlowDefinition != null)
    {
      //String taskFlowName = taskFlowDefinition.getTaskFlowId().getFullyQualifiedName();
      DataControlScopeType controlScopeType = taskFlowDefinition.getDataControlScopeType();

      if (controlScopeType == null || controlScopeType == DataControlScopeType.SHARED)
        controlScopeTypeString = "Shared Data Control Scope";
      else if (controlScopeType == DataControlScopeType.ISOLATED)
        controlScopeTypeString = "Isolated Data Control Scope";
      else
        controlScopeTypeString = "UNKNOWN Data Control Scope";
    }
    else
      controlScopeTypeString = "Null";

    return controlScopeTypeString;
  }

  public String getTaskFlowControlTransactionTypeName()
  {
    MetadataService metadataService = MetadataService.getInstance();
    String transactionTypeString;
    TaskFlowDefinition taskFlowDefinition = metadataService.getTaskFlowDefinition(getCurrentTaskFlowId());
    if (taskFlowDefinition != null)
    {
      //String taskFlowName = taskFlowDefinition.getTaskFlowId().getFullyQualifiedName();

      TransactionType transactionType = taskFlowDefinition.getTransactionType();

      if (transactionType == null)
        transactionTypeString = "-No Controller Transaction-";
      else if (transactionType == TransactionType.NEW_TRANSACTION)
        transactionTypeString = "Always Begin New Transaction";
      else if (transactionType == TransactionType.REQUIRES_TRANSACTION)
        transactionTypeString = "Use Existing Transaction if Possible";
      else if (transactionType == TransactionType.REQUIRES_EXISTING_TRANSACTION)
        transactionTypeString = "Always Use Existing Transaction";
      else
        transactionTypeString = "UNKNOWN Transaction Type";
    }
    else
      transactionTypeString = "Null";

    return transactionTypeString;
  }

  public HashMap<String, String> formatTaskFlowParameters(Map btfParameters)
  {
    HashMap<String, String> taskFlowParameterValues = new HashMap<String, String>();

    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
    Map<String, Object> pageFlowScope = adfFacesContext.getPageFlowScope();

    for (Object parameter: btfParameters.values())
    {
      NamedParameter namedParameter = (NamedParameter) parameter;
      String parameterName = namedParameter.getName();
      String parameterExpression = namedParameter.getValueExpression();
      Object parameterValue;
      String stringValue;

      if (parameterExpression == null)
      {
        parameterValue = pageFlowScope.get(parameterName);
      }
      else
      {
        parameterValue = application.evaluateExpressionGet(facesContext, parameterExpression, Object.class);
      }

      if (parameterValue != null)
      {
        try
        {
          stringValue = parameterValue.toString();
        }
        catch (Exception e)
        {
          stringValue = "<complex>";
        }
      }
      else
      {
        stringValue = "<null>";
      }

      taskFlowParameterValues.put(parameterName, stringValue);
    }
    return taskFlowParameterValues;
  }

  public TaskFlowId getCurrentTaskFlowId()
  {
    ControllerContext controllerContext = ControllerContext.getInstance();
    ViewPortContext currentViewPort = controllerContext.getCurrentViewPort();
    TaskFlowContext taskFlowContext = currentViewPort.getTaskFlowContext();
    TaskFlowId taskFlowId = taskFlowContext.getTaskFlowId();

    return taskFlowId;
  }

  public TaskFlowDefinition getTaskFlowDefinition(TaskFlowId taskFlowId)
  {
    MetadataService metadataService = MetadataService.getInstance();
    TaskFlowDefinition taskFlowDefinition = metadataService.getTaskFlowDefinition(taskFlowId);

    return taskFlowDefinition;
  }

  public Map<String, TaskFlowInputParameter> getCurrentTaskFlowInputParameters()
  {
    return getInputParameters(getCurrentTaskFlowId());
  }

  public Map<String, TaskFlowInputParameter> getInputParameters(TaskFlowId taskFlowId)
  {
    TaskFlowDefinition taskFlowDefinition = getTaskFlowDefinition(taskFlowId);
    Map<String, TaskFlowInputParameter> taskFlowInputParameters = taskFlowDefinition.getInputParameters();

    return taskFlowInputParameters;
  }

  public Map<String, NamedParameter> getCurrentTaskFlowReturnParameters()
  {
    return getReturnParameters(getCurrentTaskFlowId());
  }

  public Map<String, NamedParameter> getReturnParameters(TaskFlowId taskFlowId)
  {
    TaskFlowDefinition taskFlowDefinition = getTaskFlowDefinition(taskFlowId);
    Map<String, NamedParameter> namedParameters = taskFlowDefinition.getReturnValues();

    return namedParameters;
  }


  public String getDataControlFrameName()
  {
    BindingContext bindingContext = oracle.adf.controller.binding.BindingUtils.getBindingContext();
    String dataControlFrameName = bindingContext.getCurrentDataControlFrame();

    return dataControlFrameName;
  }

  public DataControlFrame getDataControlFrame()
  {
    BindingContext bindingContext = oracle.adf.controller.binding.BindingUtils.getBindingContext();
    String dataControlFrameName = bindingContext.getCurrentDataControlFrame();
    DataControlFrame dataControlFrame = bindingContext.findDataControlFrame(dataControlFrameName);

    return dataControlFrame;
  }

  public void commitTaskFlow()
  {
    getDataControlFrame().commit();
  }

  public void rollbackTaskFlow()
  {
    getDataControlFrame().rollback();
  }

  public boolean isTaskFlowTransactionDirty()
  {
    return getDataControlFrame().isTransactionDirty();
  }

  public String getOpenTransactionName()
  {
    return getDataControlFrame().getOpenTransactionName();
  }

  public String getDataControlNames()
  {
    BindingContext bindingContext = oracle.adf.controller.binding.BindingUtils.getBindingContext();
    String dataControlFrameName = bindingContext.getCurrentDataControlFrame();
    DataControlFrame dataControlFrame = bindingContext.findDataControlFrame(dataControlFrameName);
    Collection<DCDataControl> dataControls = dataControlFrame.datacontrols();
    String names = "";
    for (DCDataControl dc: dataControls)
    {
      names = names + "," + dc.getName() + dc.hashCode();
    }

    return names;
  }
}
