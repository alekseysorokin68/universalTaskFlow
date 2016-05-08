package com.rcore.global;
// ШМЯ

public class EMD5
{
  private static final String EMD5_CODE = "Secret_rgb_lib_33344551111";

  public static String hash(String str_in)
  {
    String str = str_in.trim();
    String srv = MD5.crypt(EMD5_CODE);
    str = MD5.crypt(str);
    String rc = null;
    if (str_in.length() % 3 == 0)
    {
      rc = str + srv;
    }
    else if (str_in.length() % 2 == 1)
    {
      rc = srv + str;
    }
    else
    {
      rc = str.substring(0, 1) + srv + str.substring(1);
    }
    return rc;
  }

  public static void main(String[] args)
  {
    System.out.println(EMD5.hash("4321qaz")); // 7b03850f9f850b6f71f7813ee429aa9d67feda1e2bd28c385ffe5c5d2792cd34
    System.out.println(EMD5.hash("olma")); // 77b03850f9f850b6f71f7813ee429aa9d802b241e083493b8fd9cdc99169c450
  }
}
