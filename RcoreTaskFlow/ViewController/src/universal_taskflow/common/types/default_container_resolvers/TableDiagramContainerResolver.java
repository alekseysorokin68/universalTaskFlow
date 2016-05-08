package universal_taskflow.common.types.default_container_resolvers;

import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.types.ContainerType;
import universal_taskflow.common.types.DefaultContainerResolver;

public class TableDiagramContainerResolver implements DefaultContainerResolver
{
  private static final TableDiagramContainerResolver instance = new TableDiagramContainerResolver();
  private TableDiagramContainerResolver()
  {
    super();
  }
  public ContainerType resolveContainer(TaskFlowParametersBase parameters)
  {
    // TODO
    return ContainerType.PANEL_STRECH_LAYOUT;
  }

  public static TableDiagramContainerResolver getInstance()
  {
    return instance;
  }
}
