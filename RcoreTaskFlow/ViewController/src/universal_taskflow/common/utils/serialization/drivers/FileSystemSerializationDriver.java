package universal_taskflow.common.utils.serialization.drivers;


import com.rcore.global.Files;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.KeyUsers;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.UserRecord;
import universal_taskflow.common.utils.CommonUtilsImpl;
import universal_taskflow.common.utils.serialization.XStreamFacade;


public class FileSystemSerializationDriver extends SerializationDriverBase
{
  protected FileSystemSerializationDriver()
  {
    super();
  }

  public MainRecord readServerMainRecord(String id) throws Exception
  {
    MainRecord rc = null;
    String sServerUrl  = TaskFlowSystemProperties.getInstance().getServerTaskflowRepository();
    String realPathToMainRecord = getFullUrlToMainRecord(sServerUrl, id);
    //--------------------
    if (Files.isFileExists(realPathToMainRecord)) 
    {
      Serializable obj = XStreamFacade.fromXML(realPathToMainRecord);
      if (obj != null) 
      {
        if (obj instanceof MainRecord) 
        {
          rc = (MainRecord) obj;
        }
        else 
        {
          throw new Exception("Файл - "+realPathToMainRecord+" дефектный");          
        }
        if (rc.isCurrentLibVerChanged() || rc.isCurrentTaskFlowVerChanged())
        {
          // Принудительное обновление с локального репозитория 
          MainRecord localRecord = readLocalMainRecord(id);
          if (localRecord != null) 
          {
            writeMainRecord(localRecord);
            rc = localRecord;
          }
        } // if
      }
    }
    return rc;
  }

  @Override public MainRecord readServerMainRecord() throws Exception
  {
    return readServerMainRecord(CommonUtilsImpl.getInstance().getTaskFlowParameterId());
  }
  
  public UserRecord readUserRecord(String id) throws Exception
  {
    UserRecord rc = null;
    String sServerUrl  = TaskFlowSystemProperties.getInstance().getServerTaskflowRepository();
    String fullUrlToUserRecord = getFullUrlToUserRecord(sServerUrl,id);
    if (Files.isFileExists(fullUrlToUserRecord)) 
    {
      Serializable obj = XStreamFacade.fromXML(fullUrlToUserRecord);
      if (obj != null) 
      {
        if (obj instanceof UserRecord) 
        {
          rc = (UserRecord) obj;
        }
        else 
        {
          throw new Exception("Файл "+fullUrlToUserRecord+" дефектный");          
        }
      }
    }
    return rc;
  }

  @Override public UserRecord readUserRecord() throws Exception
  {
    return readUserRecord(CommonUtilsImpl.getInstance().getTaskFlowParameterId());
  }
  private String getFullUrlToUserRecord(String sUrl,String id) 
  {
    return getFullUrlToUserRecord(sUrl,KeyUsers.getCurrentKey(),id);
  }
  private String getFullUrlToUserRecord(String sUrl,KeyUsers key,String id)
  {
    sUrl = getRealPathByUrlPath(sUrl);
    StringBuilder rc = new StringBuilder(sUrl);
    if (sUrl.endsWith("/") || sUrl.endsWith("\\")) 
    {
      rc.setLength(rc.length()-1);
    }
    rc.append("/").
      append(TaskFlowSystemProperties.getInstance().USER_TASKFLOW_DIR).
      append("/").
      append(id).append("/").
      append(CommonUtilsImpl.getInstance().getUserName()).append("/").
      append(key.getIp()).append(".xml")
    ;
    return rc.toString();
  }

  @Override public void writeMainRecord(MainRecord mainRecord) throws Exception
  {
    String sUrl = TaskFlowSystemProperties.getInstance().getServerTaskflowRepository();
    String fullUrl = getFullUrlToMainRecord(sUrl,mainRecord.getId());
    String dir = Files.getDirName(fullUrl);
    File f = new File(dir);
    f.mkdirs();
    XStreamFacade.toXML(mainRecord, fullUrl);
  }

  public void writeUserRecord(UserRecord userRecord, String id) throws Exception 
  {
    writeUserRecord(KeyUsers.getCurrentKey(),userRecord,CommonUtilsImpl.getInstance().getTaskFlowParameterId());
  }

  @Override public void writeUserRecord(UserRecord userRecord) throws Exception
  {
    writeUserRecord(KeyUsers.getCurrentKey(),userRecord);
  }

  public void writeUserRecord(KeyUsers key, UserRecord userRecord) throws Exception
  {
      writeUserRecord(KeyUsers.getCurrentKey(),userRecord,CommonUtilsImpl.getInstance().getTaskFlowParameterId());
//    String sUrl = TaskFlowSystemProperties.getInstance().getServerTaskflowRepository();
//    String fullUrl = getFullUrlToUserRecord(sUrl,key, CommonUtilsImpl.getInstance().getTaskFlowParameterId());
//    String dir = Files.getDirName(fullUrl);
//    File file = new File(dir);
//    file.mkdirs();
//    XStreamFacade.toXML(userRecord, fullUrl);
  }
  
  public void writeUserRecord(KeyUsers key, UserRecord userRecord, String id) throws Exception
  {
    String sUrl = TaskFlowSystemProperties.getInstance().getServerTaskflowRepository();
    String fullUrl = getFullUrlToUserRecord(sUrl,key, id);
    String dir = Files.getDirName(fullUrl);
    File file = new File(dir);
    file.mkdirs();
    XStreamFacade.toXML(userRecord, fullUrl);
  }
  
  public MainRecord fullReadMainRecordFromRepositories(String id) 
    throws Exception
  {
    MainRecord rc = null;
    SerializationDriverBase driver = SerializationDriverBase.getInstance();
    rc = driver.readServerMainRecord(id);
    if (rc == null) 
    {
      rc = driver.readLocalMainRecord(id);
    }
    if (rc == null) 
    {
      rc = new MainRecord(id);
      writeMainRecord(rc);
    }
    return rc;
  }
  
  public MainRecord fullReadMainRecordFromRepositories() throws Exception
  {
    return fullReadMainRecordFromRepositories(CommonUtilsImpl.getInstance().getTaskFlowParameterId());
//    MainRecord rc = null;
//    SerializationDriverBase driver = SerializationDriverBase.getInstance();
//    rc = driver.readServerMainRecord();
//    if (rc == null) 
//    {
//      rc = driver.readLocalMainRecord();
//    }
//    if (rc == null) 
//    {
//      rc = new MainRecord(CommonUtilsImpl.getInstance().getTaskFlowParameterId());
//      writeMainRecord(rc);
//    }
//    return rc;
  }

  //==============================
  public List<String> getIdList()
  {
    String sServerUrl  = TaskFlowSystemProperties.getInstance().getServerTaskflowRepository();
    String realPath  = getFullUrlToServerRepository(sServerUrl);
    List<File> fileList = Files.getFileList(realPath, 
                                              false, 
                                              "xml", 
                                              Files.EXCLUDE_DIR);
    List<String> rc = new ArrayList<String>(fileList.size());
    for (File item : fileList) rc.add(item.getName());
    return rc;
  }

  public List<MainRecord> getMainRecordList()
  {
    String sServerUrl  = TaskFlowSystemProperties.getInstance().getServerTaskflowRepository();
    String realPath  = getFullUrlToServerRepository(sServerUrl);
    List<File> fileList = Files.getFileList(realPath, 
                                              false, 
                                              "xml", 
                                              Files.EXCLUDE_DIR);
    List<MainRecord> rc = new ArrayList<MainRecord>(fileList.size());
    XStream xStream = XStreamFacade.getXStream();
    for (File item : fileList) 
    {
      rc.add((MainRecord) xStream.fromXML(item));
    }
    return rc;
  }
  //=================================
  public static void main(String[] args)
  {
    FileSystemSerializationDriver obj = new FileSystemSerializationDriver();
    obj.getMainRecordList();
  }
}
