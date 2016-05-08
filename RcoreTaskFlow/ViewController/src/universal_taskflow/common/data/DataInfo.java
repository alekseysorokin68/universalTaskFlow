package universal_taskflow.common.data;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

import universal_taskflow.common.utils.serialization.drivers.SerializationDriverBase;


public class DataInfo implements Serializable
{
  @SuppressWarnings("compatibility:-4026943289141738749")
  private static final long serialVersionUID = 1L;
  //-----
  private MainRecord mainRecord = null;
  private Map<KeyUsers, UserRecord> taskFlowUsersMap = new HashMap<KeyUsers, UserRecord>();
  //-----
  public DataInfo(MainRecord mainRecord) 
  {
    super();
    this.mainRecord = mainRecord;
  }
  public DataInfo(MainRecord mainRecord, UserRecord userRecord) 
  {
    super();
    this.mainRecord = mainRecord;
    taskFlowUsersMap.put(KeyUsers.getCurrentKey(), userRecord);
  }
  public DataInfo(String id) throws Exception
  {
    super();
    MainRecord mainRecord = SerializationDriverBase.getInstance().fullReadMainRecordFromRepositories(id);
    UserRecord userRecord = SerializationDriverBase.getInstance().readUserRecord(id);
    this.mainRecord = mainRecord;
    taskFlowUsersMap.put(KeyUsers.getCurrentKey(), userRecord);
  }

  public MainRecord getMainRecord()
  {
    return mainRecord;
  }
  public UserRecord getUserRecord()
  {
    return taskFlowUsersMap.get(KeyUsers.getCurrentKey());
  }
  
  public Map<KeyUsers, UserRecord> getTaskFlowUsersMap()
  {
    return taskFlowUsersMap;
  }
}
