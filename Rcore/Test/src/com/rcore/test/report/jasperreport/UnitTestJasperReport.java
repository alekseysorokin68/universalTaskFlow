package com.rcore.test.report.jasperreport;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;

import com.rcore.global.Files;
import com.rcore.view.TestDynamicBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class UnitTestJasperReport
{
  private static final String JASPER_REPORTS_HOME = getJasperReportsHome();
  private static final String JASPER_REPORTS_SOURCE_EXT   = ".jrxml";
  private static final String JASPER_REPORTS_COMPILED_EXT = ".jasper";

  public UnitTestJasperReport()
  {
    super();
  }

  //  public static void main(String[] args)
  //  {
  //    String[] args2 = { UnitTestJasperReport.class.getName() };
  //    JUnitCore.main(args2);
  //  }

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
  }

  @AfterClass
  public static void tearDownAfterClass()
    throws Exception
  {
  }
  //=======================================================
  @Test
  @Ignore
  public void test1_Compile() throws JRException
  {
    File compiled = new File(JASPER_REPORTS_HOME + "/compile/simple"  + JASPER_REPORTS_COMPILED_EXT);
    compiled.delete();
    JasperReport report = compileReport("simple");
    Assert.assertNotNull(report);
    Assert.assertTrue(compiled.exists());
    compiled.delete();
  }
  @Test
  @Ignore
  public void test2_JasperPrint() throws JRException, SQLException
  {
    JasperReport jasperReport = compileReport("simple");
    Connection connection = TestDynamicBean.getConnection();
    Map<String, Object> report_parameters = new HashMap<String, Object>();
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
    Assert.assertNotNull(jasperPrint);
    //---------------------------------------
    String fileName = JASPER_REPORTS_HOME + "/out/simple.html";
    File out = new File(fileName);
    JasperExportManager.exportReportToHtmlFile(jasperPrint,fileName);
    Assert.assertTrue(out.exists());
    out.delete();
    Files.removeDirectory(new File(JASPER_REPORTS_HOME + "/out/simple.html_files"));
    //---------------------------------------
    fileName = JASPER_REPORTS_HOME + "/out/simple.pdf";
    out = new File(fileName);
    JasperExportManager.exportReportToPdfFile(jasperPrint,fileName);
    Assert.assertTrue(out.exists());
    out.delete();
    //---------------------------------------
    fileName = JASPER_REPORTS_HOME + "/out/simple.xml";
    out = new File(fileName);
    JasperExportManager.exportReportToXmlFile(jasperPrint,fileName,true);
    Assert.assertTrue(out.exists());
    out.delete();
    //---------------------------------------
  }
//  @Test
//  @Ignore
//  public void test3_PDF2RTF() throws JRException, SQLException, IOException
//  {
//    JasperReport jasperReport = compileReport("simple");
//    Connection connection = TestDynamicBean.getConnection();
//    Map<String, Object> report_parameters = new HashMap<String, Object>();
//    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
//    Assert.assertNotNull(jasperPrint);
//    //---------------------------------------
//    String fileName = null;
//    File out = null;
//    //---------------------------------------
//    fileName = JASPER_REPORTS_HOME + "/out/simple.pdf";
//    out = new File(fileName);
//    JasperExportManager.exportReportToPdfFile(jasperPrint,fileName);
//    Assert.assertTrue(out.exists());
//    String fileRTF = JASPER_REPORTS_HOME + "/out/simple.rtf";
//    filePDF2fileRTF(fileName, fileRTF);
//    out = new File(fileRTF);
//    Assert.assertTrue(out.exists());
//    //---------------------------------------
//    out.delete();
//    new File(fileName).delete();
//  }
//  @Test
//  @Ignore
//  public void test3_PDF2TEXT() throws JRException, SQLException, IOException, DocumentException
//  {
//    JasperReport jasperReport = compileReport("simple");
//    Connection connection = TestDynamicBean.getConnection();
//    Map<String, Object> report_parameters = new HashMap<String, Object>();
//    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
//    Assert.assertNotNull(jasperPrint);
//    //---------------------------------------
//    String fileName = null;
//    File out = null;
//    //---------------------------------------
//    fileName = JASPER_REPORTS_HOME + "/out/simple.pdf";
//    out = new File(fileName);
//    JasperExportManager.exportReportToPdfFile(jasperPrint,fileName);
//    Assert.assertTrue(out.exists());
//    String fileTXT = JASPER_REPORTS_HOME + "/out/simple.txt";
//    filePDF2fileTEXT(fileName, fileTXT);
//    out = new File(fileTXT);
//    Assert.assertTrue(out.exists());
//    //---------------------------------------
//    out.delete();
//    new File(fileName).delete();
//  }
  
  @Test
  @Ignore
  public void test4_JRRtfExporter() throws Exception
  {
    JasperReport jasperReport = compileReport("simple");
    Connection connection = TestDynamicBean.getConnection();
    Map<String, Object> report_parameters = new HashMap<String, Object>();
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
    Assert.assertNotNull(jasperPrint);
    //===================================================================
    FileBufferedOutputStream fbos = new FileBufferedOutputStream();
    JRRtfExporter exporter = new JRRtfExporter();
    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
    //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, "...");
    //---
    exporter.exportReport();
    fbos.close(); 
    String fileOut = JASPER_REPORTS_HOME + "/out/simple.JRRtfExporter.rtf";
    OutputStream fileOS = new FileOutputStream(fileOut);
    fbos.writeData(fileOS);
    fileOS.close();
    fbos.dispose();
    //---
    new File(fileOut).delete();
  }
  
  @Test
  @Ignore
  public void test5_JRDocxExporter() throws Exception
  {
    JasperReport jasperReport = compileReport("simple");
    Connection connection = TestDynamicBean.getConnection();
    Map<String, Object> report_parameters = new HashMap<String, Object>();
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
    Assert.assertNotNull(jasperPrint);
    //===================================================================
    FileBufferedOutputStream fbos = new FileBufferedOutputStream();
    JRDocxExporter exporter = new JRDocxExporter();
    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
    //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, "...");
    //---
    exporter.exportReport();
    fbos.close(); 
    String fileOut = JASPER_REPORTS_HOME + "/out/simple.JRDocxExporter.docx";
    OutputStream fileOS = new FileOutputStream(fileOut);
    fbos.writeData(fileOS);
    fileOS.close();
    fbos.dispose();
    //---
    new File(fileOut).delete();
  }
  @Test
  @Ignore
  public void test6_JRPdfExporter() throws Exception
  {
    JasperReport jasperReport = compileReport("simple");
    Connection connection = TestDynamicBean.getConnection();
    Map<String, Object> report_parameters = new HashMap<String, Object>();
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
    Assert.assertNotNull(jasperPrint);
    //===================================================================
    FileBufferedOutputStream fbos = new FileBufferedOutputStream();
    JRPdfExporter exporter = new JRPdfExporter();
    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
    //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, "...");
    //---
    exporter.exportReport();
    fbos.close(); 
    String fileOut = JASPER_REPORTS_HOME + "/out/simple.JRPdfExporter.pdf";
    OutputStream fileOS = new FileOutputStream(fileOut);
    fbos.writeData(fileOS);
    fileOS.close();
    fbos.dispose();
    //---
    new File(fileOut).delete();
  }
  
  @Test
  @Ignore
  public void test7_JRPptxExporter() throws Exception
  {
    JasperReport jasperReport = compileReport("simple");
    Connection connection = TestDynamicBean.getConnection();
    Map<String, Object> report_parameters = new HashMap<String, Object>();
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
    Assert.assertNotNull(jasperPrint);
    //===================================================================
    FileBufferedOutputStream fbos = new FileBufferedOutputStream();
    JRPptxExporter exporter = new JRPptxExporter();
    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
    //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, "...");
    //---
    exporter.exportReport();
    fbos.close(); 
    String fileOut = JASPER_REPORTS_HOME + "/out/simple.JRPptxExporter.pptx";
    OutputStream fileOS = new FileOutputStream(fileOut);
    fbos.writeData(fileOS);
    fileOS.close();
    fbos.dispose();
    //---
    new File(fileOut).delete();
  }
  
  @Test
  @Ignore
  public void test8_JRTextExporter() throws Exception
  {
    JasperReport jasperReport = compileReport("simple");
    Connection connection = TestDynamicBean.getConnection();
    Map<String, Object> report_parameters = new HashMap<String, Object>();
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, report_parameters, connection);
    Assert.assertNotNull(jasperPrint);
    //===================================================================
    FileBufferedOutputStream fbos = new FileBufferedOutputStream();
    JRTextExporter exporter = new JRTextExporter();
    exporter.setParameter(JRTextExporterParameter.JASPER_PRINT, jasperPrint);
    exporter.setParameter(JRTextExporterParameter.OUTPUT_STREAM, fbos);
    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(10));
    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(12));
    exporter.setParameter(JRTextExporterParameter.LINE_SEPARATOR, "\n");
    //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, "...");
    //---
    exporter.exportReport();
    fbos.close(); 
    String fileOut = JASPER_REPORTS_HOME + "/out/simple.JRTextExporter.txt";
    OutputStream fileOS = new FileOutputStream(fileOut);
    fbos.writeData(fileOS);
    fileOS.close();
    fbos.dispose();
    //---
    new File(fileOut).delete();
  }
  //========================================================================
  protected static JasperReport compileReport(String name) throws JRException
  {
    File source = new File(JASPER_REPORTS_HOME + "/source/" +name + JASPER_REPORTS_SOURCE_EXT);
    File compiled = new File(JASPER_REPORTS_HOME + "/compile/"  +name + JASPER_REPORTS_COMPILED_EXT);
    if (!compiled.exists() || compiled.lastModified() < source.lastModified())
    {
      if (compiled.exists())
      { // compiled report is exists
        System.out.println("Updated report '" + name + "'; previous = " +
                           compiled.lastModified() + ", current = " + source.lastModified());
        compiled.delete();
        //compiled = new File(JASPER_REPORTS_HOME + name + JASPER_REPORTS_COMPILED_EXT);
      }
      System.out.println("Compiling report '" + name + "' ...");
      JasperCompileManager.compileReportToFile(source.getAbsolutePath(),
                                               compiled.getAbsolutePath());
    }
    try
    {
      return (JasperReport) JRLoader.loadObject(compiled);
    }
    catch (JRException e)
    {
      compiled.delete();
      throw e;
    }
  }
  protected void filePDF2fileRTF(String pdfFile, String rtfFile)
    throws IOException
  {
    Document document = new Document();
    document.open();
    PdfReader reader = new PdfReader(pdfFile);
    PdfDictionary dictionary = reader.getPageN(1);
    PRIndirectReference reference = (PRIndirectReference) dictionary.get(PdfName.CONTENTS);
    PRStream stream = (PRStream) PdfReader.getPdfObject(reference);
    byte[] bytes = PdfReader.getStreamBytes(stream);
    PRTokeniser tokenizer = new PRTokeniser(bytes);
    FileOutputStream fos = new FileOutputStream(rtfFile);
    StringBuffer buffer = new StringBuffer();
    while (tokenizer.nextToken())
    {
      if (tokenizer.getTokenType() == PRTokeniser.TK_STRING)
      {
        buffer.append(tokenizer.getStringValue());
      }
    }
    String test = buffer.toString();
    StringReader stReader = new StringReader(test);
    int t = 0;
    while (true)
    {
      t = stReader.read();
      if (t <= 0)
      {
        break;
      }
      fos.write(t);
    } // while
    //------------------------------------------
    fos.close();
    document.close();
    System.out.println("Converted Successfully");
  }
  
  protected void filePDF2fileTEXT(String pdfFile, String rtfFile)
    throws IOException, DocumentException
  {
    Document document = new Document();
    document.open();
    PdfReader reader = new PdfReader(pdfFile);
    PdfDictionary dictionary = reader.getPageN(1);
    PRIndirectReference reference = (PRIndirectReference) dictionary.get(PdfName.CONTENTS);
    PRStream stream = (PRStream) PdfReader.getPdfObject(reference);
    byte[] bytes = PdfReader.getStreamBytes(stream);
    PRTokeniser tokenizer = new PRTokeniser(bytes);
    FileOutputStream fos = new FileOutputStream(rtfFile);
    StringBuffer buffer = new StringBuffer();
    while (tokenizer.nextToken())
    {
      if (tokenizer.getTokenType() == PRTokeniser.TK_STRING)
      {
        buffer.append(tokenizer.getStringValue());
      }
    } // while
    String test = buffer.toString();
    StringReader stReader = new StringReader(test);
    int t = 0;
    while (true)
    {
      t = stReader.read();
      if (t <= 0)
      {
        break;
      }
      fos.write(t);
    } // while
    fos.close();
    document.add(new Paragraph(".."));
    document.close();
  }
  
  //============================================================================
  protected static String getJasperReportsHome()
  {
    String rc = null;
    rc = System.getProperty("user.dir");
    rc += ("/public_html/jasperReports");
    return rc;
  }
}
