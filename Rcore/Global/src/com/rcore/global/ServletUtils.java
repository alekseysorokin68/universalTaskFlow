package com.rcore.global;
// ШМЯ

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

public final class ServletUtils
{

  public static String getRealPath(String pathFromRoot, 
                                   PageContext pageContext)
  {
    String rc = pageContext.getServletContext().getRealPath("");
    rc += ("/" + pathFromRoot);
    rc.replace('\\', '/');
    rc.replace("//", "/");
    return rc;
  }

  public static void setCookie(HttpServletResponse response, String name, 
                               String value, int age)
  {
    Cookie cook = new Cookie(name, value);
    cook.setMaxAge(age);
    response.addCookie(cook);
  }

  public static void removeCookie(HttpServletResponse response, 
                                  String name)
  {
    // Проверено !
    Cookie cook = new Cookie(name, null);
    response.addCookie(cook);
  }

  public static String getCookie(HttpServletRequest request, String name)
  {
    Cookie[] cookList = request.getCookies();
    if (cookList == null)
    {
      return null;
    }
    for (int i = 0; i < cookList.length; i++)
    {
      Cookie item = cookList[i];
      if (name.equals(item.getName()))
      {
        return item.getValue();
      }
    }
    return null;
  }

  public static void main(String[] args)
  {
    // ..
  }

  /**
   *  Возвращает куки-идентификатор компьютера, если его не было - ставим эту пометку.
   *
   * @param cookie_id_computer_user
   * @param maximum_time_cookie
   * @param request
   * @param response
   * @return - идентификатор компьютера
   */
  public static String getIdComputer(String cookie_id_computer_user, 
                                     int maximum_time_cookie, 
                                     HttpServletRequest request, 
                                     HttpServletResponse response)
  {
    final String KEY = cookie_id_computer_user;
    String rc = getCookie(request, KEY);
    if (rc == null)
    {
      rc = System.currentTimeMillis() + "_" + Math.random();
      setCookie(response, KEY, rc, maximum_time_cookie);
    }
    return rc;
  }

  /**
   * Наличие этого метода связано с тем, что request.getQueryString() не дружит с кирилицей.
   */
  public static String getQueryString(HttpServletRequest request, 
                                      String paramExclude)
  {
    String queryString = request.getQueryString();
    if (StringFunc.isEmpty(queryString))
    {
      return queryString;
    }
    StringBuffer rc = new StringBuffer();
    Enumeration parNames = request.getParameterNames();
    while (parNames.hasMoreElements())
    {
      String param = parNames.nextElement().toString();
      if (paramExclude != null)
      {
        if (paramExclude.equals(param))
        {
          continue;
        }
      }
      String value = request.getParameter(param);
      if (rc.length() == 0)
      {
        rc.append(param);
      }
      else
      {
        rc.append("&");
        rc.append(param);
      }
      rc.append("=").append(value);
    } // while
    return rc.toString();
  }
}
