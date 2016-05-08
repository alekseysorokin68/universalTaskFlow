package universal_taskflow.edit_defaults.beans;

import java.util.ArrayList;
import java.util.List;

import oracle.adf.view.rich.component.rich.data.RichTree;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;

import org.apache.myfaces.trinidad.model.RowKeySet;

import universal_taskflow.common.types.TaskFlowGroup;
import universal_taskflow.common.types.TaskFlowTypeImpl;


public class TaskFlowTypeNode
{
  private TaskFlowGroup group = null;
  private TaskFlowTypeImpl type  = null;
  
  public TaskFlowTypeNode(TaskFlowGroup group) 
  {
    super();
    this.group = group;
  }
  public TaskFlowTypeNode(TaskFlowTypeImpl type) 
  {
    super();
    this.type = type;
  }
  public boolean isRoot() 
  {
    return ((group != null) && (group.getParent() == null));
  }
  public String getCaption() 
  {
    String rc = null;
    if (group != null) 
    {
      rc = group.getCaption();
    }
    else 
    {
      rc = type.getTitle();
    }
    return rc;
  }
  
  public String getDescription() 
  {
    String rc = null;
    if (group != null) 
    {
      rc = group.getDescription();
    }
    else 
    {
      rc = type.getDescription();
    }
    return rc;
  }

  public TaskFlowTypeImpl getType()
  {
    return type;
  }
  
  public boolean isLeaf() 
  {
    return (type != null);
  }
  
//  public void setChilds(List<TaskFlowTypeNode> childs)
//  {
//    this.childs = childs;
//  }

  public List<TaskFlowTypeNode> getChilds()
  {
    List<TaskFlowTypeNode> rc = new ArrayList<TaskFlowTypeNode>();
    if (!isLeaf()) 
    {
      for (TaskFlowGroup item : TaskFlowGroup.values()) 
      {
        if (group.equals(item.getParent())) 
        {
          rc.add(new TaskFlowTypeNode(item));
        }
      } // for
      if (rc.isEmpty()) 
      {
        for (TaskFlowTypeImpl item : TaskFlowTypeImpl.values()) 
        {
          if (group.equals(item.getGroup())) 
          {
            rc.add(new TaskFlowTypeNode(item));
          }
        }
      } // for
    }
    else 
    {
      ;
    }
    return rc;
  }
  //--------------------------------------
  public static ChildPropertyTreeModel buildTaskFlowTypeTreeModel()
  {
    List<TaskFlowTypeNode> rootList = new ArrayList<TaskFlowTypeNode>();
    TaskFlowGroup[] groupList = TaskFlowGroup.values();
    for (TaskFlowGroup item : groupList) 
    {
      if (item.getParent() == null) 
      {
        TaskFlowTypeNode node = new TaskFlowTypeNode(item);
        rootList.add(node);
      }
    }
    ChildPropertyTreeModel rc = new ChildPropertyTreeModel(rootList,"childs");
    return rc;
  }
  
  public static TaskFlowTypeNode getSelectedNodeFromTree(RichTree tree) 
  {
    ChildPropertyTreeModel model = (ChildPropertyTreeModel) tree.getValue();
    RowKeySet rk = tree.getSelectedRowKeys();
    TaskFlowTypeNode rc = (TaskFlowTypeNode) (model.getRowData(rk.toArray()[0]));
    return rc;
  }
}
