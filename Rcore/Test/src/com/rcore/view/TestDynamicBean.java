package com.rcore.view;

import com.rcore.model.dynamic_list.ConnectionInterface;
import com.rcore.model.dynamic_list.DbDynamicListImpl0;

import java.sql.*;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.annotation.PreDestroy;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.jdbc.OracleDriver;

import org.apache.myfaces.trinidad.event.SortEvent;
import org.apache.myfaces.trinidad.model.SortCriterion;

public class TestDynamicBean
{
  private DbDynamicListImpl0 model = null;
  private Connection con = null;
  private SortCriterion sortCriterion = new SortCriterion("ID",true);
  private RichTable t1;
  //-------------------------------------
  public TestDynamicBean()
  {
    super();
  }
  @PostConstruct
  public void initBean() 
  {
    try
    {
      con = getConnection();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  @PreDestroy
  public void desctoyBean() 
  {
    try
    {
      con.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  public DbDynamicListImpl0 getModel() throws Exception
  {
    if (model == null) 
    {
      ConnectionInterface conInt = new MyConnectionInterface(con);
      String sqlBody = "select * FROM (select ID,VALUE, rownum rn from TEST order by ${FIELD} ${ASC_DESC}) WHERE rn >=? and rn <=?";
      sqlBody = sqlBody.replace("${FIELD}", sortCriterion.getProperty());
      sqlBody = sqlBody.replace("${ASC_DESC}", (sortCriterion.isAscending() ? "asc" : "desc" )  );
      model = new DbDynamicListImpl0(
        "select count(1) from TEST",
        sqlBody,
        25,
        conInt
      ) ;
    }
    return model;
  }
  
//  public static Connection getConnection()
//    throws SQLException
//  {
//    String username = "scott";
//    String password = "tiger";
//    String thinConn = "jdbc:oracle:thin:@b2b-sibur-sso.vm-p.rdtex.ru:1521:ORCL";
//    DriverManager.registerDriver(new OracleDriver());
//    Connection conn =
//      DriverManager.getConnection(thinConn, username, password);
//    conn.setAutoCommit(false);
//    return conn;
//  }
  
  public static Connection getConnection()
    throws SQLException
  {
    String username = "cb_test";
    String password = "cb_test";
    String thinConn = "jdbc:oracle:thin:@windows-xp-32-2:1521:ORCL";
    DriverManager.registerDriver(new OracleDriver());
    Connection conn =
      DriverManager.getConnection(thinConn, username, password);
    conn.setAutoCommit(false);
    return conn;
  }
  
  public static void main(String[] args) throws SQLException
  {
    createRecords();
//    DbDynamicListImpl model = new TestDynamicBean().getModel();
//    System.out.println("aaa");
//    System.out.println(model.get(0));
//    System.out.println(model.get(299));
  }

  private static void createRecords() throws SQLException
  {
    Connection con = getConnection();
    PreparedStatement ps = null;
    ps = con.prepareStatement("truncate table TEST");
    //ps.executeUpdate();
    con.commit();
    ps = con.prepareStatement(
      "insert into test (ID, VALUE) values  (? , ?)");
    for(int i=10000; i < 40000; i++) 
    {
      ps.setInt(1, (i+1));
      ps.setString(2, "value_"+(i+1));
      ps.executeUpdate();
      if (i % 100 == 0) 
      {
        System.out.println(i);
        con.commit();
      }
    } // for
    con.commit();
  }

  public void sortListener(SortEvent sortEvent)
  {
    List<SortCriterion> criterion = sortEvent.getSortCriteria();
    if (criterion != null && criterion.size() > 0) 
    {
      sortCriterion = criterion.get(0);
      model = null;
    }
  }

  public void setT1(RichTable t1)
  {
    this.t1 = t1;
  }

  public RichTable getT1()
  {
    return t1;
  }

  private class MyConnectionInterface implements ConnectionInterface
  {
    @SuppressWarnings("compatibility:-537919570905308504")
    private static final long serialVersionUID = 1L;
    private Connection con=null;
    private MyConnectionInterface(Connection con)
    {
      super();
      this.con = con;
    }
    public Connection getConnection()
    {
      return con;
    }

    public void closeConnection(Connection con)
    {
      ;
    }
  }
}
