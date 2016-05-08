package com.rcore.test.bc;


import com.rcore.global.bcomponent.base.ApplicationModuleImplRcore;
import com.rcore.model.AppModuleImpl;
import com.rcore.model.TCountryViewImpl;
import com.rcore.model.TCountryViewRowImpl;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;

import oracle.jbo.ApplicationModuleHandle;
import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaItem;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.XMLInterface;

import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLNode;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import org.w3c.dom.Node;


//import static junit.
//import static junit.framework.Assert.assertNotNull;
//import static junit.framework.Assert.assertNull;


public class ViewObjectTestCase
{
  private static final String AM_NAME = "com.rcore.model.AppModule";
  private static final String CONFIG_NAME = "AppModuleShared";
  private static ApplicationModuleHandle handle = null;
  private static AppModuleImpl am = null;
  public ViewObjectTestCase()
  {
    super();
  }

  public static void main(String[] args)
  {
    String[] args2 = { 
        ViewObjectTestCase.class.getName() 
    };
    JUnitCore.main(args2);
  }

  @Before
  public void setUp()
    throws Exception
  {
  }

  @After
  public void tearDown()
    throws Exception
  {
  }

  @BeforeClass
  public static void setUpBeforeClass()
    throws Exception
  {
    handle = ApplicationModuleImplRcore.createRootApplicationModuleHandle(
                                      AM_NAME, CONFIG_NAME);
    am = (AppModuleImpl)handle.useApplicationModule();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    ApplicationModuleImplRcore.releaseRootApplicationModuleHandle(handle, false);
  }
  //======================================================
  @Test
  @Ignore
  public void print() 
  {
    TCountryViewImpl countryView = (TCountryViewImpl)am.getTCountryView1();
    countryView.print();
  }
  
  @Test
  @Ignore
  public void nvl() 
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    assertEquals(vo.getRowCount(),2);
    vo.setcountry("1");
    vo.executeQuery();
    assertEquals(vo.getRowCount(),1);
    vo.setcountry(null);
    vo.executeQuery();
    assertEquals(vo.getRowCount(),2);
  }
  /**
   * next,last,... устанавливают текущую строку
   * reset - сбрасывает текущую строку в null
   */
  @Test
  @Ignore
  public void moveCurentLine() 
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    assertNull(vo.getCurrentRow());
    vo.first();
    assertNotNull(vo.getCurrentRow());
    
    vo.reset();
    assertNull("not null!",vo.getCurrentRow());
    
    vo.last();
    assertNotNull(vo.getCurrentRow());
    
    vo.previous();
    assertNotNull(vo.getCurrentRow());
    
    vo.next();
    assertNotNull(vo.getCurrentRow());
  }
  @Test
  @Ignore
  public void writeXml()
    throws IOException
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    HashMap<String,String[]> h = new HashMap<String,String[]>();
    h.put("com.rcore.model.TCountryView", new String[]{"CountryId","Sname"});
    
    Node n = vo.writeXML(XMLInterface.XML_OPT_ALL_ROWS,h);
    
    XMLDocument doc = (XMLDocument) n.getOwnerDocument();
    n = doc.adoptNode(n);
    doc.appendChild(n);
    
    PrintWriter pw = new PrintWriter(System.out);
    ((XMLNode)n).print(pw);
  }
  
  @Test
  @Ignore
  public void getViewName() 
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    System.out.println(vo.getViewName());
  }
  
  @Test
  @Ignore
  public void toXMLDocument()
    throws IOException
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    XMLDocument doc = vo.toXMLDocument();
    PrintWriter pw = new PrintWriter(System.out);
    ((XMLNode)doc.getDocumentElement()).print(pw);
    
  }
  @Test
  @Ignore
  public void findByKey() 
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    Row[] rows = vo.findByKey(vo.buildKey(new Object[]{2}), -1);
    TCountryViewRowImpl row = (TCountryViewRowImpl) rows[0];
    assertTrue(2.0 == row.getCountryId().getValue());
    row = (TCountryViewRowImpl)(vo.getCurrentRow());
    assertNull(row);
  }
  
  @Test
  @Ignore
  public void findByViewCriteria()
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    ViewCriteria criteria = vo.createViewCriteria();
    
    
    ViewCriteriaRow vcRow = criteria.createViewCriteriaRow();
    ViewCriteriaItem item = vcRow.ensureCriteriaItem("Sname");
    item.setOperator("=");
    item.getValues().get(0).setValue("УКРАИНА");
    criteria.add(vcRow);
    
    vcRow = criteria.createViewCriteriaRow();
    item = vcRow.ensureCriteriaItem("CountryId");
    item.setOperator("=");
    item.getValues().get(0).setValue(new Integer(2));
    criteria.add(vcRow);
    
    vo.applyViewCriteria(criteria);
    vo.executeQuery();
    vo.first();
    TCountryViewRowImpl row = (TCountryViewRowImpl) vo.getCurrentRow();
    double id = row.getCountryId().getValue();
    assertTrue(2.0 == id);
  }
  
  @Test
  public void resetWhere()
  {
    TCountryViewImpl vo = (TCountryViewImpl)am.getTCountryView1();
    String where = "SNAME = 'УКРАИНА'";
    vo.setWhereClause(where);
    vo.executeQuery();
    vo.first();
    TCountryViewRowImpl row = (TCountryViewRowImpl) vo.getCurrentRow();
    double id = row.getCountryId().getValue();
    assertTrue(2.0 == id);
  
    vo.resetWhereClause();
    vo.executeQuery();
    vo.first();
    int rowCount = vo.getRowCount();
    assertTrue(rowCount > 1);
  }
  
  /**
   * public Row[] findByKey(Key key,
                       int maxNumOfRows,
                       boolean skipWhere - не работает
                  )
   */
  
  /**
   * findByAltKey работает плохо. Пользоваться не стоит
   */
  
  /**
   * needsRefresh() - всегда возвращает false
   * public boolean needsRefresh()
     {
        return false;
     }
   */
  
  
}
