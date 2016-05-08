package universal_taskflow.view_types.table;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import universal_taskflow.common.data.SqlAttribute;

public class ColumnInfo  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String category = null;
  private List<SqlAttribute> columns = new ArrayList<SqlAttribute>();
  //-----------------------------------------------------------------
  public ColumnInfo(String category, SqlAttribute attr)
  {
    super();
    this.category = category;
    columns.add(attr);
  }

  public String getCategory()
  {
    return category;
  }

  //    public int getColSpan()
  //    {
  //      return colSpan;
  //    }
  //    @Override
  //    public String toString()
  //    {
  //      return category+" ; "+colSpan;
  //    }

  public List<SqlAttribute> getColumns()
  {
    return columns;
  }
  
}
