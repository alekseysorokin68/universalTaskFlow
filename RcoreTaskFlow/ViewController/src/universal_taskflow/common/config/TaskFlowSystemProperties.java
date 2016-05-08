package universal_taskflow.common.config;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import universal_taskflow.common.types.DebugMode;
import universal_taskflow.common.utils.DataSourceInfo;
import universal_taskflow.common.utils.serialization.drivers.FileSystemSerializationDriver;


/**
 * Системные параметры библиотеки и вместилище констант.
 * Основной конфигурационный файл.
 */
public class TaskFlowSystemProperties implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final TaskFlowSystemProperties instance = new TaskFlowSystemProperties();
  public  static final String MAIN_TASKFLOW_DIR = "main_taskflow";
  public  static final String USER_TASKFLOW_DIR = "user_taskflow";
  //--------------------------------------------------------------
  private String applicationTaskflowRepository = "/universal_taskflow/application_taskflow_repository";
  //private String serverTaskflowRepository      = "/server_taskflow_repository";
  //private String serverTaskflowRepository      = "file://c:/JDeveloper/mywork/11119/RcoreTaskFlow/RcoreTaskFlowImpl/Test/public_html/server_taskflow_repository";
  private String serverTaskflowRepository      = "file://c:/JDeveloper/mywork/122100/mywork/RcoreTaskFlow/RcoreTaskFlowImpl/Test/public_html/server_taskflow_repository";
  private List<DataSourceInfo> listDatasource  = new ArrayList<DataSourceInfo>();
  private Locale locale = Locale.getDefault(); // Пока не используется
  // TODO private String style = "/universal_taskflow/taskflow_on_sql_resources/css/style.css";
  private String versionLib = "1";
  private long defaultLifeTimeInCashView = 1000*60*15;
  private DebugMode debugMode = DebugMode.PRODUCTION;
  private Class<?> serializationDriver = FileSystemSerializationDriver.class;
  private int maxCountRecordForDropDownCombobox = 2048; 
  private Accessibility accessibility = new AccessibilityDefault();
  //--------------------------------------------------
  public static TaskFlowSystemProperties getInstance()
  {
    return instance;
  }
  
  public void registerDataSource(String title, String jndiName) 
  {
    DataSourceInfo info = new DataSourceInfo(title,jndiName);
    if (!listDatasource.contains(info)) 
    {
      listDatasource.add(new DataSourceInfo(title,jndiName));
    }
  }

  public void unRegisterDataSource(String jndiName)
  {
    DataSourceInfo info = new DataSourceInfo("", jndiName);
    listDatasource.remove(info);
  }

  public void clearDataSources()
  {
    listDatasource.clear();
  }
  
  private TaskFlowSystemProperties() 
  {
    super();
//  listDatasource.add(new DataSourceInfo("TRD_OLAP"  , "java:comp/env/jdbc/tradeEntiretPooledDS"));
//  listDatasource.add(new DataSourceInfo("BOND_ADMIN", "java:comp/env/jdbc/bond_adminDS"));
    listDatasource.add(new DataSourceInfo("TRD_OLAP"  , "jdbc/tradeEntiretPooledDS"));
    listDatasource.add(new DataSourceInfo("BOND_ADMIN", "jdbc/bond_adminDS"));
  }
  public void setApplicationTaskflowRepository(String applicationTaskflowRepository)
  {
    this.applicationTaskflowRepository = applicationTaskflowRepository;
  }

  public String getApplicationTaskflowRepository()
  {
    return applicationTaskflowRepository;
  }

  public void setServerTaskflowRepository(String serverTaskflowRepository)
  {
    this.serverTaskflowRepository = serverTaskflowRepository;
  }

  public String getServerTaskflowRepository()
  {
    return serverTaskflowRepository;
  }

  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  public Locale getLocale()
  {
    return locale;
  }
  //------------------

  public String getVersionLib()
  {
    return versionLib;
  }

  public void setDefaultLifeTimeInCashView(long defaultLifeTimeInCashView)
  {
    this.defaultLifeTimeInCashView = defaultLifeTimeInCashView;
  }

  public long getDefaultLifeTimeInCashView()
  {
    return defaultLifeTimeInCashView;
  }

  public void setDebugMode(DebugMode debugMode)
  {
    this.debugMode = debugMode;
  }

  public DebugMode getDebugMode()
  {
    return debugMode;
  }

  public void setSerializationDriver(Class<?> serializationDriver)
  {
    this.serializationDriver = serializationDriver;
  }

  public Class<?> getSerializationDriver()
  {
    return serializationDriver;
  }

  public List<DataSourceInfo> getListDatasource()
  {
    return listDatasource;
  }

  public void setMaxCountRecordForDropDownCombobox(int maxCountRecordForDropDownCombobox)
  {
    this.maxCountRecordForDropDownCombobox = maxCountRecordForDropDownCombobox;
  }

  public int getMaxCountRecordForDropDownCombobox()
  {
    return maxCountRecordForDropDownCombobox;
  }

  public void setAccessibility(Accessibility accessibility)
  {
    this.accessibility = accessibility;
  }

  public Accessibility getAccessibility()
  {
    return accessibility;
  }
}
