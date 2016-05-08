package universal_taskflow.common.types.default_container_resolvers;

import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.types.ContainerType;
import universal_taskflow.common.types.DefaultContainerResolver;

public class TableFormContainerResolver implements DefaultContainerResolver
{
  private static final TableFormContainerResolver instance = new TableFormContainerResolver();
  private TableFormContainerResolver()
  {
    super();
  }
  public ContainerType resolveContainer(TaskFlowParametersBase parameters)
  {
    // TODO
    return ContainerType.TABLE_LAYOUT;
  }

  public static TableFormContainerResolver getInstance()
  {
    return instance;
  }
}
