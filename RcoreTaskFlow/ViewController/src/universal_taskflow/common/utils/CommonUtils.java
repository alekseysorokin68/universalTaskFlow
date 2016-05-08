package universal_taskflow.common.utils;

import java.io.Serializable;

import java.util.Map;

import universal_taskflow.common.data.DataInfo;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.UserRecord;


public interface CommonUtils
{
  boolean isEditDefaultsMode();
  String getTaskFlowParameterId();
  Double getTaskFlowParameterVer();
  DataInfo getDataInfo();
  MainRecord getMainRecord();
  UserRecord getUserRecord();
  boolean isModelReadOnly();
  void refreshData();
  String getUserName();
  boolean isTaskFlowActivated();
  void setTaskFlowActivated(boolean activated);
  void setTextNotActivated(String textNotActivated);
  String getTextNotActivated();
  Map<String, String> getTaskFlowParametersFromRequest();
  void update();
  void updateUserData();
  //boolean isFirstTime();
  void saveSqlParametersToUserData(Map<String, Serializable> params);;
}
