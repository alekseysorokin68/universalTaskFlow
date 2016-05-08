package universal_taskflow.common.types;

public enum TaskFlowGroup
{
  ON_SQL("Потоки задач, основанные на базе данных",null,null),
    TABLE("Таблицы",ON_SQL,null),
    FORM("Формы",ON_SQL,null),
    TREE("Деревья",ON_SQL,null),
    DIAGRAM("Диаграммы",ON_SQL,null)
  //,ON_SQL_TEST("*TEST*",null,null)
  ;
  private static final long serialVersionUID = 1L;
  private String caption = null;
  private TaskFlowGroup parent = null;
  private String description = null;

  private TaskFlowGroup(String caption, 
                        TaskFlowGroup parent,
                        String description
                        ) 
  {
    this.parent = parent;
    this.caption = caption;
    this.description = description;
  }

  public String getCaption()
  {
    return caption;
  }

  public TaskFlowGroup getParent()
  {
    return parent;
  }

  public String getDescription()
  {
    return description;
  }
}
