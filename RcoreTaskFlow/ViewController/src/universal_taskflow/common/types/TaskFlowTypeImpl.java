package universal_taskflow.common.types;

import com.rcore.global.jsf.JSFUtils;

import java.io.Serializable;

import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.DiagramTypeParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.FormTypeParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableAdfParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableHtmlParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TreeTypeParameters;

import universal_taskflow.common.types.default_container_resolvers.TableAdfContainerResolver;

import universal_taskflow.common.types.default_container_resolvers.TableDiagramContainerResolver;
import universal_taskflow.common.types.default_container_resolvers.TableFormContainerResolver;
import universal_taskflow.common.types.default_container_resolvers.TableHtmlContainerResolver;

import universal_taskflow.common.types.default_container_resolvers.TableTreeContainerResolver;

import universal_taskflow.view_types.beans.ViewPageFlowBean;


public enum TaskFlowTypeImpl implements TaskFlowType,Serializable
{
  TABLE_ADF("ADF таблица",
        "Данные будут показаны в табличной форме",
        "table_adf",
        TaskFlowGroup.TABLE,
        TableAdfParameters.class,
        "#{pageFlowScope.viewAdfTableBean}",
        "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_table_adf_def.jsff",
        TableAdfContainerResolver.getInstance()
        ),

  TABLE_HTML("HTML таблица",
        "Данные будут показаны в табличной форме",
        "table_html",
        TaskFlowGroup.TABLE,
        TableHtmlParameters.class,
        "#{backingBeanScope.viewHtmlTableBean}",
        "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_table_html_def.jsff",
        TableHtmlContainerResolver.getInstance()
        ),

  FORM("Форма",
       "Представление данных в виде формы",
       "form",
       TaskFlowGroup.FORM,
       FormTypeParameters.class,
       "",
       "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_form_def.jsff",
       TableFormContainerResolver.getInstance()
       ),

  TREE("Дерево",
       "Древовидное представление данных",
       "tree",
       TaskFlowGroup.TREE,
       TreeTypeParameters.class,
       "",
       "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_tree_def.jsff",
       TableTreeContainerResolver.getInstance()
       ),

  // Графическое представление данных
  DIAGRAM_VERTICAL(
      "Cтолбчатая вертикальная",
      null,
      "diagram_vertical",
      TaskFlowGroup.DIAGRAM,
      DiagramTypeParameters.class,
      "",
      "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_diagram_def.jsff",
      TableDiagramContainerResolver.getInstance()
    ),
  // столбчатая вертикальная, накопительная
  DIAGRAM_VERTICAL_SUM(
    "Cтолбчатая вертикальная, накопительная",
    null,
    "diagram_vertical_sum",
    TaskFlowGroup.DIAGRAM,
    DiagramTypeParameters.class,
    "",
    "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_diagram_def.jsff",
    TableDiagramContainerResolver.getInstance()
    ),
  DIAGRAM_HORIZONTAL(
    "Cтолбчатая горизонтальная",
    null,
    "diagram_horizontal",
    TaskFlowGroup.DIAGRAM,
    DiagramTypeParameters.class,
    "",
    "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_diagram_def.jsff",
    TableDiagramContainerResolver.getInstance()
    ),
  // столбчатая горизонтальная, накопительная
  DIAGRAM_HORIZONTAL_SUM(
      "Cтолбчатая горизонтальная, накопительная",
      null,
      "diagram_horizontal_sum",
      TaskFlowGroup.DIAGRAM,
      DiagramTypeParameters.class,
      "",
      "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_diagram_def.jsff",
      TableDiagramContainerResolver.getInstance()
    ),

  DIAGRAM_GRAPH(
      "График",
      null,
      "diagram_graph",
      TaskFlowGroup.DIAGRAM,
      DiagramTypeParameters.class,
      "",
      "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_diagram_def.jsff",
      TableDiagramContainerResolver.getInstance()
    ),

  DIAGRAM_GRAPH_COMBINE(
      "График комбинированный",
      null,
      "diagram_graph_combine",
      TaskFlowGroup.DIAGRAM,
      DiagramTypeParameters.class,
      "",
      "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_diagram_def.jsff",
      TableDiagramContainerResolver.getInstance()
    ),

  DIAGRAM_PIE(
      "Круговая диаграмма",
      null,
      "diagram_pie",
      TaskFlowGroup.DIAGRAM,
      DiagramTypeParameters.class,
      "",
      "/universal_taskflow/pages/edit_defaults/type_parameters/on_sql/param_diagram_def.jsff",
      TableDiagramContainerResolver.getInstance()
    )
  ;
  private static final long serialVersionUID = 1L;

  private transient String        title = null;
  private transient String        description = null;
  private transient String        startViewDeclarativeComponent = null;
  private transient TaskFlowGroup group = null;
  private transient Class<? extends TaskFlowParametersBase> viewTypeParameters  = null;
  private String elBean = "";
  private String adminParametersDeclarativeComponent = null;
  private transient DefaultContainerResolver rootDefaultContainerResolver = null;

  private TaskFlowTypeImpl(
                   String title,
                   String description,
                   String startViewDeclarativeComponent,
                   TaskFlowGroup group,
                   Class<? extends TaskFlowParametersBase> viewTypeParameters,
                   String elBean,
                   String adminParametersDeclarativeComponent,
                   DefaultContainerResolver rootDefaultContainerResolver
                   )
  {
    this.title = title;
    this.description = description;
    this.startViewDeclarativeComponent = startViewDeclarativeComponent;
    this.group = group;
    this.viewTypeParameters = viewTypeParameters;
    this.elBean = elBean;
    this.adminParametersDeclarativeComponent = adminParametersDeclarativeComponent;
    this.rootDefaultContainerResolver = rootDefaultContainerResolver;
  }
  /**
   * @return
   */
  public String getName()
  {
    return name();
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public TaskFlowType[] getValues()
  {
    return this.values();
  }
//  public Map<String,ViewTypeImpl> getMap()
//  {
//    Map<String,ViewTypeImpl> rc = new HashMap<String,ViewTypeImpl>();
//    for (ViewTypeImpl item: ViewTypeImpl.values())
//    {
//      rc.put(item.name(), item);
//    }
//    return rc;
//  }

  public TaskFlowGroup getGroup()
  {
    return group;
  }

  public Class<? extends TaskFlowParametersBase> getViewTypeParameters()
  {
    return viewTypeParameters;
  }

  public String getStartViewDeclarativeComponent()
  {
    return startViewDeclarativeComponent;
  }

  public String getAdminParametersDeclarativeComponent()
  {
    return adminParametersDeclarativeComponent;
  }
  public Boolean getIsDiagram()
  {
    return this.name().startsWith("DIAGRAM_");
  }

  public DefaultContainerResolver getRootDefaultContainerResolver()
  {
    return rootDefaultContainerResolver;
  }
  public ViewPageFlowBean getViewBean()
  {
    Object rc = JSFUtils.resolveExpression(elBean);
    if (!(rc instanceof ViewPageFlowBean))
    {
      rc = null;
    }
    return (ViewPageFlowBean) rc;
  }
}
