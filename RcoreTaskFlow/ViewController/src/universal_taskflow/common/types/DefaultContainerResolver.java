package universal_taskflow.common.types;

import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;

/*
 * Определить контейнера ко умолчанию
 */
public interface DefaultContainerResolver
{
  ContainerType resolveContainer(TaskFlowParametersBase parameters);
}
