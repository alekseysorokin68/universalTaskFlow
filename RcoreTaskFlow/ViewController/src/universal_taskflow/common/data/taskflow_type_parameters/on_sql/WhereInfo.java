package universal_taskflow.common.data.taskflow_type_parameters.on_sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jbo.Row;

import universal_taskflow.common.data.SqlAttribute;


public class WhereInfo
{
  private String where = null;
  private Map<String,Object> params = new HashMap<String,Object>();
  public WhereInfo(Row row, List<SqlAttribute> attributes)
  {
    super();
    buildSqlAndParams(row,attributes);
  }
  public WhereInfo(Map<String,Object> row, List<SqlAttribute> attributes)
  {
    super();
    buildSqlAndParams(row,attributes);
  }
  
  private void buildSqlAndParams(Row row, List<SqlAttribute> lsSql)
  {
    final String AND = " and ";
    StringBuilder where = new StringBuilder();
    params.clear();
    //List<Object> paramsObj = new ArrayList<Object>();
    for (int i = 0; i < lsSql.size(); i++)
    {
      SqlAttribute attr = lsSql.get(i);
      if (attr.getPrimaryKey())
      {
        String name = attr.getName(); 
        String key = name + "_where";
        Object value = row.getAttribute(attr.getName()); 
        //-----
        if (value != null) {
          params.put(key, value);
          where.append(name).append(" = :").append(key).append(AND);
        }
        else 
        {
          where.append(name).append(" is null ").append(AND);
        }
      }
    } // for
    if (where.length() > 0)
    {
      where.setLength(where.length() - AND.length());
    }
    this.where = where.toString();
    return;
  }
  
  private void buildSqlAndParams(Map<String,Object> row,List<SqlAttribute> lsSql)
  {
    final String AND = " and ";
    StringBuilder where = new StringBuilder();
    params.clear();
    
    for (int i = 0; i < lsSql.size(); i++)
    {
      SqlAttribute attr = lsSql.get(i);
      if (attr.getPrimaryKey())
      {
        String name = attr.getName(); 
        String key = name + "_where";
        Object value = row.get(attr.getName()); 
        if (value != null) {
          params.put(key, value);
          where.append(name).append(" = :").append(key).append(AND);
        }
        else 
        {
          where.append(name).append(" is null ").append(AND);
        }
      }
    } // for
    if (where.length() > 0)
    {
      where.setLength(where.length() - AND.length());
    }
    this.where = where.toString();
    return;
  }


  public String getWhere()
  {
    return where;
  }

  public Map<String,Object> getParams()
  {
    return params;
  }
}
