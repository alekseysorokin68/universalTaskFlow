package universal_taskflow.common.types;


public interface TaskFlowType
{
  String getName();
  String getTitle();
  String getDescription();
  Class getViewTypeParameters();
  TaskFlowType[] getValues();
  String getStartViewDeclarativeComponent();
  String getAdminParametersDeclarativeComponent();
  TaskFlowGroup getGroup();
  //---------------------------
}
