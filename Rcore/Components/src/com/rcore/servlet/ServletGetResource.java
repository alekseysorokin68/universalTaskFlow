package com.rcore.servlet;


import com.rcore.global.Files;
import com.rcore.global.Resource;
import com.rcore.global.StringFunc;
import com.rcore.global.log.FacesLog;
import com.rcore.global.log.FacesLogImpl;
import com.rcore.global.log.LogLevel;
import com.rcore.model.dynamic_list.ConnectionFactory;
import com.rcore.model.dynamic_list.ConnectionInterface;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import javax.naming.NamingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import oracle.jbo.ApplicationModule;
import oracle.jbo.server.DBTransactionImpl;


/**
 * Сервлет для обращения к java-ресурсам как к URL
 * В конкретном проекте нужно в web.xml внести следующую информацию:
 <pre>
  &lt;servlet&gt;
    &lt;servlet-name&gt;ServletGetResource</servlet-name&gt;
    &lt;servlet-class>com.rcore.servlet.ServletGetResource&lt;/servlet-class&gt;
  &lt;/servlet&gt;

   &lt;servlet-mapping&gt;
     &lt;servlet-name&gt;ServletGetResource&lt;/servlet-name&gt;
     &lt;url-pattern&gt;/rcore.resource.servelet&lt;/url-pattern&gt;
   &lt;/servlet-mapping&gt;

   Синтаксис использования:
    1) /rcore.resource.servelet?
      resource={Полный путь к ресурсу}&amp;
      [typeInput={javaResource=default|file}]&amp;
      [typeOutput={text=default|image}]&amp;
      [charset=<Кодировка, по умолчанию UTF-8>]&amp;

    2) /rcore.resource.servelet?
      typeInput=db&amp;
      [typeOutput={text=default|image}]&amp;
      [charset=<Кодировка, по умолчанию UTF-8>]&amp;
      [[connectionInterfaceClass=<имя класса>]|[dataSourceName=<Ds Name>]|[sessionKey={key for appModule,DbTransaction,Connection}]]&amp;
      [sql=<sql query>]


   Пример использования:
   &lt;trh:script source="/rcore.resource.servelet?resource=/META-INF/adf/js/jquery.js" id="s1"/&gt;
   &lt;trh:script source="/rcore.resource.servelet?resource=/META-INF/adf/js/html_tree.js" id="s2"/&gt;
 </pre>
 */
public class ServletGetResource extends HttpServlet
{
  //private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
  //private static final FacesLog log = new FacesLogImpl(ServletGetResource.class,LogLevel.DEBUG);
  private static final FacesLog log = new FacesLogImpl(ServletGetResource.class,null);
  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    log.log(LogLevel.DEBUG,"doGet - log");
    
    String resource = null;
    String typeInput = null;
    String typeOutput = null;
    String charset = null;
    String connectionInterfaceClass = null;
    String dataSourceName = null;
    String sessionKey = null;
    String sql = null;
    try
    {
      resource = request.getParameter("resource");
      typeInput = request.getParameter("typeInput");
      typeOutput = request.getParameter("typeOutput");
      charset = request.getParameter("charset");
      connectionInterfaceClass = request.getParameter("connectionInterfaceClass");
      dataSourceName = request.getParameter("dataSourceName");
      sessionKey = request.getParameter("sessionKey");
      sql = request.getParameter("sql");
      //----------------------------------------------
      if (StringFunc.isEmpty(typeInput)) 
      {
        typeInput="javaResource";
      }
      if (StringFunc.isEmpty(typeOutput)) 
      {
        typeOutput="text";
      }
      if (StringFunc.isEmpty(charset)) 
      {
        charset="UTF-8";
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    //--------------------------------------------
    String contentType = getContentType(typeOutput,charset);
    if (contentType != null) 
    {
      response.setContentType(contentType);
    }
    response.setHeader("pragma", "no-cache");
    //---------------------------------------------
    ServletOutputStream out = response.getOutputStream();
    //--------------------------------------------
    Object dbSessionValue = null;
    if (sessionKey != null) 
    {
      HttpSession session = request.getSession();
      if (session != null) {
        dbSessionValue = session.getAttribute(sessionKey);
      }
    }
    doGet(out,resource,typeInput,typeOutput,charset,connectionInterfaceClass,dataSourceName,dbSessionValue,sql);
  }

  private static String getContentType(String typeOutput, String charset)
  {
    String rc = null;
    if      ("text".equals(typeOutput)) {
      rc = "text/html; charset="+charset;
    }
    else if ("image".equals(typeOutput)) 
    {
      rc = "image/jpeg";
    }
    return rc;
  }

  private static void putToOutputStream(InputStream is, OutputStream out) throws IOException
  {
    byte[] buffer = new byte[10 * 1024];
    while (true)
    {
      int nread = is.read(buffer);
      if (nread <= 0) 
      {
        break;
      }
      out.write(buffer, 0, nread);
    }
  }

  private static void doGet(OutputStream out, 
                            String resource, 
                            String typeInput,
                            String typeOutput, 
                            String charset, 
                            String connectionInterfaceClass,
                            String dataSourceName, 
                            Object dbSessionValue, 
                            String sql)
    throws IOException
  {
    InputStream is = null;
    try
    {
      if ("javaResource".equals(typeInput))
      {
        is = Resource.getResourceInputStream(ServletGetResource.class, resource);
      }
      else if ("file".equals(typeInput))
      {
        is = Files.fileToStream(resource);
      }
      else if ("db".equals(typeInput))
      {
        is = dbToStream(typeOutput,charset,connectionInterfaceClass,dataSourceName,dbSessionValue,sql);
      }
      if (is == null)
      {
        System.err.println("ServletGetResource 1: resource " + resource + " not found");
        out.close();
        return;
      }
      putToOutputStream(is, out);
    }
    catch (Exception ex)
    {
      if (out != null)
      {
        out.close();
      }
      if (is != null)
      {
        is.close();
      }
    }
  }

  private static InputStream dbToStream(String typeOutput, 
                                        String charset,
                                        String connectionInterfaceClass, 
                                        String dataSourceName,
                                        Object dbSessionValue, 
                                        String sql)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException,
           NamingException, SQLException
  {
    ConnectionInterface connectionInterface = null;
    if (connectionInterfaceClass != null) 
    {
      connectionInterface = (ConnectionInterface)(Class.forName(connectionInterfaceClass).newInstance());
    }
    else if (dataSourceName != null) 
    {
      connectionInterface = ConnectionFactory.getConnectionInterface(dataSourceName);
    }
    else if (dbSessionValue != null) 
    {
      connectionInterface = new ConnectionInterfaceByObject(dbSessionValue);
    }
    if (connectionInterface == null) 
    {
      return null;
    }
    InputStream rc = null;
    Connection con = null;
    try {
      byte[] bytes = null;
      con = connectionInterface.getConnection();
      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery(sql);
      if (rs.next()) {
        log.log(LogLevel.DEBUG,"2");
        bytes = rs.getBytes(1);
        rc = new ByteArrayInputStream(bytes);
      }
      rs.close();
      st.close();
    }
    finally 
    {
      if (con != null) {
        connectionInterface.closeConnection(con);
      }
    }
    return rc;
  }
  private static class ConnectionInterfaceByObject implements ConnectionInterface 
  {

    private Connection con = null;

    private ConnectionInterfaceByObject(Object dbSessionValue)
    {
      super();
      if (dbSessionValue instanceof ApplicationModule) 
      {
        con = ((DBTransactionImpl)(((ApplicationModule)dbSessionValue).getTransaction())).getPersistManagerConnection();
      }
      else if (dbSessionValue instanceof DBTransactionImpl) 
      {
        con = ((DBTransactionImpl)dbSessionValue).getPersistManagerConnection();
      }
      else if (dbSessionValue instanceof Connection) 
      {
        con = (Connection)dbSessionValue;
      }
      
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
  //-----------------------
  public static void main(String[] args) throws Exception
  {
    FileOutputStream out=new FileOutputStream("c:/temp/out.txt");
    //doGet()
  }
}
