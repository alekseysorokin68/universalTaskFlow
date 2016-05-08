package com.rcore.utils.table;

import com.rcore.model.dynamic_list.ConnectionInterface;

import com.rcore.model.dynamic_list.DbListSimple;
import com.rcore.model.dynamic_list.DynamicListSignature;

import java.util.List;
import java.util.Map;

import oracle.adf.view.rich.component.rich.data.RichTable;

public class TableModelesIntegratorSimple extends TableModelesIntegratorImpl
{
  private static final long serialVersionUID = 1L;
  public static final int  MAX_ROWS_DEFAULT = 10000;
  //private final int maxRows = MAX_ROWS_DEFAULT;

  public TableModelesIntegratorSimple(ConnectionInterface connectionInterface,
                                    String sql,
                                    Map<String, Object> sqlParams, // new
                                    Map<String,Integer> sqlTypesFileds,
                                    RichTable table)
  {
    super(connectionInterface, sql, sqlParams, MAX_ROWS_DEFAULT, sqlTypesFileds, table);
  }

  public TableModelesIntegratorSimple(ConnectionInterface connectionInterface, 
                                      String sql, 
                                      Map<String, Object> sqlParams,
                                      List<String> filterAbleList, 
                                      Map<String, Integer> sqlTypesFileds)
  {
    super(connectionInterface, sql, sqlParams, filterAbleList, MAX_ROWS_DEFAULT ,sqlTypesFileds);
  }

  public TableModelesIntegratorSimple(ConnectionInterface connectionInterface, 
                                      String sql, 
                                      Map<String, Object> sqlParams,
                                      List<String> filterAbleList
                                     )
  {
    super(connectionInterface, sql, sqlParams, filterAbleList, MAX_ROWS_DEFAULT);
  }
  //--------------------------------------------------------------------
  public TableModelesIntegratorSimple(ConnectionInterface connectionInterface,
                                    String sql,
                                    Map<String, Object> sqlParams, // new
                                    Map<String,Integer> sqlTypesFileds,
                                    RichTable table,
                                    int maxRows  
                                    )
  {
    super(connectionInterface, sql, sqlParams, maxRows, sqlTypesFileds, table);
  }

  public TableModelesIntegratorSimple(ConnectionInterface connectionInterface, 
                                      String sql, 
                                      Map<String, Object> sqlParams,
                                      List<String> filterAbleList, 
                                      Map<String, Integer> sqlTypesFileds,
                                      int maxRows  
                                      )
  {
    super(connectionInterface, sql, sqlParams, filterAbleList, maxRows ,sqlTypesFileds);
  }

  public TableModelesIntegratorSimple(ConnectionInterface connectionInterface, 
                                      String sql, 
                                      Map<String, Object> sqlParams,
                                      List<String> filterAbleList,
                                      int maxRows  
                                     )
  {
    super(connectionInterface, sql, sqlParams, filterAbleList, maxRows);
  }
  //=====================================================================
  @Override
  protected DynamicListSignature<Map<String, Object>> 
                buildDynamicListSignature(String dataSignature) 
  {
    String newSql = calculateNewSql(sql, filterModel, sortModel, sqlTypesFileds);
//    DynamicListSignature<Map<String, Object>> rc =
//      new DbListSimple(newSql, sqlParams, connectionInterface, maxRows);
    DynamicListSignature<Map<String, Object>> rc =
      new DbListSimple(newSql, sqlParams, connectionInterface, getRangeSize());
    if (dataSignature != null)
    {
      rc.setSignature(dataSignature);
    }
    return rc;
  }
}
