package com.rcore.view;


import com.rcore.model.dynamic_list.ConnectionFactory;
//import com.rcore.utils.table.CustomFilterableQueryDescriptor;
import com.rcore.utils.table.TableModelesIntegratorImpl;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.annotation.PostConstruct;


public class TestTableModelesBean
{
  private TableModelesIntegratorImpl modeles = null;
  private List<Map<String, Object>> listModel = new ArrayList<Map<String, Object>>();

  public TestTableModelesBean()
  {
    super();
    Map<String,Object> map = null;
    map = new HashMap<String,Object>(); map.put("ID", 1);  listModel.add(map);
    map = new HashMap<String,Object>(); map.put("ID", 2);  listModel.add(map);
    map = new HashMap<String,Object>(); map.put("ID", 3);  listModel.add(map);
    map = new HashMap<String,Object>(); map.put("ID", 4);  listModel.add(map);
  }
  //----------- Custom Filter 0
  public List<Map<String,Object>> getTableModel() 
  {
    return listModel;
  }
//  public CustomFilterableQueryDescriptor getFilterModel() 
//  {
//    CustomFilterableQueryDescriptor rc = new CustomFilterableQueryDescriptor();
//    return rc;
//  }
  //----------- Custom Filter 9
  
  public TableModelesIntegratorImpl getModeles()
  {
    return modeles;
  }
  @PostConstruct
  public void initBean()
  {
    try
    {
      List<String> listFilterFields = new ArrayList<String>();
      listFilterFields.add("ID");
      listFilterFields.add("VALUE");
      List<String> listSortFields = new ArrayList<String>();
      listSortFields.add("ID");
      listSortFields.add("VALUE");
      modeles = new TableModelesIntegratorImpl(
                  ConnectionFactory.getConnectionInterface(getConnection()),
                  "select ID ID, VALUE VALUE from TEST order by ID",
                  null,
                  listFilterFields,
                  //listSortFields,
                  25
      );
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  public static Connection getConnection()
    throws SQLException
  {
    return TestDynamicBean.getConnection();
  }
}
