package universal_taskflow.common.types.default_container_resolvers;

import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.types.ContainerType;
import universal_taskflow.common.types.DefaultContainerResolver;

public class TableHtmlContainerResolver implements DefaultContainerResolver
{
  private static final TableHtmlContainerResolver instance = new TableHtmlContainerResolver();
  private TableHtmlContainerResolver()
  {
    super();
  }
  public ContainerType resolveContainer(TaskFlowParametersBase parameters)
  {
    // TODO
    return ContainerType.TABLE_LAYOUT;
  }

  public static TableHtmlContainerResolver getInstance()
  {
    return instance;
  }
}
