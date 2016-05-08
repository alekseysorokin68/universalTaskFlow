package universal_taskflow.common.types.default_container_resolvers;

import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableAdfParameters;
import universal_taskflow.common.types.ContainerType;
import universal_taskflow.common.types.DefaultContainerResolver;

public class TableAdfContainerResolver implements DefaultContainerResolver
{
  private static final TableAdfContainerResolver instance = new TableAdfContainerResolver();
  private TableAdfContainerResolver()
  {
    super();
  }
  public ContainerType resolveContainer(TaskFlowParametersBase parameters)
  {
    ContainerType rc = ContainerType.PANEL_STRECH_LAYOUT;
    TableAdfParameters tableAdfParameters = (TableAdfParameters)parameters;
    if (tableAdfParameters.getAutoHeightRows() != -1) 
    {
      rc = ContainerType.PANEL_GROUP_LAYOUT_VERTICAL;
      //rc = ContainerType.TABLE_LAYOUT;
    }
    return rc;
  }

  public static TableAdfContainerResolver getInstance()
  {
    return instance;
  }
}
