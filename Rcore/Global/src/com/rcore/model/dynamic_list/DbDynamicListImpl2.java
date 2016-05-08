package com.rcore.model.dynamic_list;


import com.rcore.global.bcomponent.base.ViewObjectImplRcore;

import java.util.Map;


public class DbDynamicListImpl2 extends DynamicListSignature<Map<String,Object>>
{
  @SuppressWarnings("compatibility:-9024936253169707575")
  private static final long serialVersionUID = 1;
  private ViewObjectImplRcore viewObject = null;
  private int nSize = 0;
  private boolean fieldNameAsAttributeName = false;

  public DbDynamicListImpl2(
            ViewObjectImplRcore viewObject,
            boolean fieldNameAsAttributeName
        )
  {
    super();
    this.fieldNameAsAttributeName = fieldNameAsAttributeName;
    this.viewObject = viewObject;
    this.nSize = sizeImpl();
  }
  
  private int sizeImpl() 
  {
    //Debug.timingStart("sizeImpl");
    int rc = 0;
    try 
    {
      rc = ViewObjectImplRcore.getRowCountByQuery(viewObject);
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    //Debug.timingFinish("sizeImpl",true);
    return rc;
  }
  
  public int size()
  {
    return nSize;
  }

  public Map<String,Object> get(int i)
  {
    Map<String,Object> rc = null;
    rc = viewObject.getRecordByJdbc(i,nSize, fieldNameAsAttributeName);
    return rc;
  }
    
  public void refresh() 
  {
    viewObject.refreshJdbcReader();
    nSize = sizeImpl();
  }
  
//  @Override
//  public void dispose() 
//  {
//    viewObject.refreshJdbcReader();
//    nSize = 0;
//  }
  
//========= TEST
//  private static void testToday(ApplicationModule mod,String printId)
//  throws SQLException
//  {
//    boolean isPrameter = (mod != null);
//    if (mod == null) {
//      mod = Configuration.createRootApplicationModule(
//            "com.rdtex.model.appmodule.universalPortlet1.appmodule.AppModule_BOND_ADMIN", 
//            "AppModule_BOND_ADMINLocal");
//    }
//    String sql = "select * from test_today where test_date = :td";
//    String sqlCount = "select count(1) from (select * from test_today where test_date = :td)";
//    ViewObjectImpl vo =
//    (ViewObjectImpl) mod.createViewObjectFromQueryStmt("vo", sql, "com.cb.model.base.bc.ViewObjectImplCB");
//    java.sql.Date javaSqlDate = new java.sql.Date(System.currentTimeMillis());
//    Object value = new oracle.jbo.domain.Date(javaSqlDate);
//    //Object value = javaSqlDate;
//    vo.defineNamedWhereClauseParam("td", value, new int[]{0});
//    vo.setNamedWhereClauseParam("td", value);
//    vo.executeQuery();
//    System.out.println(printId+" @1="+vo.getRowCount());
//    //================================================
//    DBTransaction tran = (DBTransaction) vo.getApplicationModule().getTransaction();
//    OraclePreparedStatement st = (OraclePreparedStatement) tran.createPreparedStatement(sqlCount, 1);
//    ViewObjectImplRcore.setupPreparedStatementByViewObject(st,vo);
//    ResultSet rs = st.executeQuery();
//    rs.next();
//    System.out.println(printId+" @2="+rs.getInt(1));
//    //================================================
//    if (!isPrameter)
//      Configuration.releaseRootApplicationModule(mod, true);      
//  }
//  
//  private static void testToday2(ViewObjectImpl vo, String printId)
//  throws SQLException
//  {
//    //String sql = "select * from test_today where test_date = :td";
//    String sqlCount = "select count(1) from (select * from test_today where test_date = :td)";
//    java.sql.Date javaSqlDate = new java.sql.Date(System.currentTimeMillis());
//    Object value = new oracle.jbo.domain.Date(javaSqlDate);
//    //Object value = javaSqlDate;
//    vo.defineNamedWhereClauseParam("td", value, new int[]{0});
//    vo.setNamedWhereClauseParam("td", value);
//    vo.executeQuery();
//    System.out.println(printId+" @1="+vo.getRowCount());
//    //================================================
//    DBTransaction tran = (DBTransaction) vo.getApplicationModule().getTransaction();
//    OraclePreparedStatement st = (OraclePreparedStatement) tran.createPreparedStatement(sqlCount, 1);
//    ViewObjectImplRcore.setupPreparedStatementByViewObject(st,vo);
//    ResultSet rs = st.executeQuery();
//    rs.next();
//    System.out.println(printId+" @2="+rs.getInt(1));
//    //================================================
//  }
//  private static void testToday3(ViewObjectImpl vo, String printId)
//  throws SQLException
//  {
//    //String sql = "select * from test_today where test_date = :td";
//    String sqlCount = "select count(1) from (select * from test_today where test_date = :td)";
//    //String sqlCount = "select count(1) from (select * from test_today where test_date = :td)";
//    //java.sql.Date javaSqlDate = new java.sql.Date(System.currentTimeMillis());
//    //Object value = new oracle.jbo.domain.Date(javaSqlDate);
//    //Object value = javaSqlDate;
////    vo.defineNamedWhereClauseParam("td", value, new int[]{0});
////    vo.setNamedWhereClauseParam("td", value);
////    vo.executeQuery();
////    System.out.println(printId+" @1="+vo.getRowCount());
//    //================================================
//    DBTransaction tran = (DBTransaction) vo.getApplicationModule().getTransaction();
//    OraclePreparedStatement st = (OraclePreparedStatement) tran.createPreparedStatement(sqlCount, 1);
//    ViewObjectImplRcore.setupPreparedStatementByViewObject(st,vo);
//    ResultSet rs = st.executeQuery();
//    rs.next();
//    System.out.println(printId+" @2="+rs.getInt(1));
//    //================================================
//  }
  //========= TEST

}
