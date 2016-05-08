package universal_taskflow.common.data.taskflow_type_parameters;

import java.io.Serializable;

import java.util.List;

import javax.faces.model.SelectItem;

import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableAdfParameters;
import universal_taskflow.common.types.ContainerType;

import universal_taskflow.edit_defaults.beans.EditDefaultsBean;

/**
 * Базовый класс для всех типов параметров
 */
public abstract class TaskFlowParametersBase implements Serializable
{
  protected ContainerType containerType = ContainerType.DEFAULT;
  protected String containerCSS = null;
  private static final long serialVersionUID = 1L;

  public void setContainerType(ContainerType containerType)
  {
    this.containerType = containerType;
  }

  public ContainerType getContainerType()
  {
    if (containerType == null) 
    {
      containerType = ContainerType.DEFAULT;
    }
    return containerType;
  }
  
  public List<SelectItem> getContainerTypeSelectItems() 
  {
    return ContainerType.getSelectItems();
  }
    
  public static <V> V createTaskFlowParameters(Class<V> cl)
    throws Exception
  {
    return cl.newInstance();
  }
  public EditDefaultsBean getEditDefaultsBean()
  {
    return EditDefaultsBean.getInstance();
  }
  public MainRecord getMainRecord()
  {
    return getEditDefaultsBean().getMainRecord();
  }
  public TaskFlowParametersBase getTaskFlowParameters() 
  {
    TaskFlowParametersBase rc = null;
    try
    {
      rc = getMainRecord().getTaskFlowParameters();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rc;
  }
  //====================
  
  public static void main(String[] args)
    throws Exception
  {
    TableAdfParameters p = createTaskFlowParameters(TableAdfParameters.class);
    System.out.println(p.getClass().getName());
  }

  public void setContainerCSS(String containerCSS)
  {
    this.containerCSS = containerCSS;
  }

  public String getContainerCSS()
  {
    return containerCSS;
  }
}
