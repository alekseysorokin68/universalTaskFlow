package com.rcore.global;
// ШМЯ


import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

public abstract class CreateZipAbstract
{
  /**
   * @param zipFile
   * @param request
   * @param response
   * @param pageContext
   * @throws IOException
   */
  public abstract void openZip(String zipFile, 
                               HttpServletRequest request, 
                               HttpServletResponse response, 
                               PageContext pageContext)
    throws IOException;

  /**
   * @param existFile
   * @throws IOException
   */
  public abstract void addFile(String existFile)
    throws IOException;

  /**
   * @throws IOException
   */
  public abstract void closeZip()
    throws IOException;

  public abstract void forceDownload();
}
