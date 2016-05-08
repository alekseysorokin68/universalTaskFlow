package universal_taskflow.common.utils.serialization.drivers;


import com.rcore.global.Files;

import java.lang.reflect.Constructor;

import java.net.URL;

import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import universal_taskflow.common.config.TaskFlowSystemProperties;
import universal_taskflow.common.data.KeyUsers;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.UserRecord;
import universal_taskflow.common.utils.CommonUtilsImpl;
import universal_taskflow.common.utils.serialization.XStreamFacade;


/**
 * Базовый класс драйверов сериализации.
 * Методы физического чтения и записи.
 */
public abstract class SerializationDriverBase
{
  private static SerializationDriverBase instance = null;
  protected SerializationDriverBase() 
  {
    super();
  }
  
  public static SerializationDriverBase getInstance() 
  {
    if (instance == null)
    {
      Class cl = TaskFlowSystemProperties.getInstance().getSerializationDriver();
      try
      {
        Constructor constr = cl.getDeclaredConstructor(new Class[]{});
        constr.setAccessible(true);
        instance = (SerializationDriverBase) constr.newInstance();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return instance;
  }
  public MainRecord readLocalMainRecord() throws Exception
  {
    return readLocalMainRecord(CommonUtilsImpl.getInstance().getTaskFlowParameterId());
  }
  public MainRecord readLocalMainRecord(String id) throws Exception
  {
    // TODO
    String sLocalUrl = TaskFlowSystemProperties.getInstance().getApplicationTaskflowRepository();
    String urlPath = getFullUrlToMainRecord(sLocalUrl);
    MainRecord rc = null;
    if (Files.isFileExists(urlPath)) 
    {
      rc = (MainRecord) XStreamFacade.fromXML(urlPath);
    }
    return rc;
  }
  protected String getFullUrlToMainRecord(String sUrl)
  {
    return getFullUrlToMainRecord(sUrl,CommonUtilsImpl.getInstance().getTaskFlowParameterId());
  }
  
  protected String getFullUrlToMainRecord(String sUrl,String id)
  {
    sUrl = getRealPathByUrlPath(sUrl);
    StringBuilder rc = new StringBuilder(sUrl);
    if (sUrl.endsWith("/") || sUrl.endsWith("\\")) 
    {
      rc.setLength(rc.length()-1);
    }
    rc.append("/").
      append(TaskFlowSystemProperties.getInstance().MAIN_TASKFLOW_DIR).
      append("/").
      append(id).append(".xml")
    ;
    return rc.toString();
  }
  
  protected String getFullUrlToServerRepository(String sUrl)
  {
    sUrl = getRealPathByUrlPath(sUrl);
    StringBuilder rc = new StringBuilder(sUrl);
    if (sUrl.endsWith("/") || sUrl.endsWith("\\")) 
    {
      rc.setLength(rc.length()-1);
    }
    rc.append("/").
      append(TaskFlowSystemProperties.getInstance().MAIN_TASKFLOW_DIR);
    ;
    return rc.toString();
  }
  /**
   * Вычисление файлового пути по URL
   * URL не должен включать контекст приложения в явном виде,
   * а конструкции типа:
   *    /....    путь от контекста приложения
   *    //....   путь от сервера приложений
   * @return
   */
  public String getRealPathByUrlPath(String urlPath)
  {
    String rc = null;
    if (urlPath != null)
    {
      //final String FILE_PREFIX = "file://";
      if (urlPath.contains("://"))
      {
        try
        {
          //rc = urlPath.substring(FILE_PREFIX.length());
          rc = new URL(urlPath).getFile();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      else
      {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        rc = session.getServletContext().getRealPath(urlPath);
      }
    }
    return rc;
  }
  //------------------------------------
  public abstract MainRecord readServerMainRecord() throws Exception;
  public abstract MainRecord readServerMainRecord(String id) throws Exception;
  public abstract MainRecord fullReadMainRecordFromRepositories() throws Exception;
  public abstract MainRecord fullReadMainRecordFromRepositories(String id) throws Exception;
  public abstract UserRecord readUserRecord() throws Exception;
  public abstract UserRecord readUserRecord(String id) throws Exception;
  //------------------------------------
  public abstract void writeMainRecord(MainRecord mainRecord) throws Exception;
  public abstract void writeUserRecord(UserRecord userRecord) throws Exception;
  public abstract void writeUserRecord(UserRecord userRecord, String id) throws Exception;
  public abstract void writeUserRecord(KeyUsers key,UserRecord userRecord) throws Exception;
  public abstract void writeUserRecord(KeyUsers key,UserRecord userRecord, String id) throws Exception;
  //--------- Общие операции --------------
  public abstract List<String> getIdList();
  public abstract List<MainRecord> getMainRecordList();
  
}
