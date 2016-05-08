package universal_taskflow.common.types;

public enum AdminPageType
{
  PageTaskFlowType("Тип потока задач",""),
  PageMainData("Основные данные", ""),
  PageTaskFlowTypeParameters("Параметры представления данных", ""),
  PageSqlQuery("Sql - запрос к базе данных", ""),
  PageSqlQueryParameters("Параметры sql - запроса", ""),
  PageFinish("Финиш", "")
  ;
  private static final long serialVersionUID = 1L;
  private String caption = null;
  private String description = null;

  private AdminPageType(String caption, String description) 
  {
    this.caption = caption;
    this.description = description;
  }


  public String getCaption()
  {
    return caption;
  }

  public String getDescription()
  {
    return description;
  }
}
