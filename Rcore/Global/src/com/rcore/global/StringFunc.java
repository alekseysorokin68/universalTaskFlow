package com.rcore.global;

import java.awt.Rectangle;

import java.io.UnsupportedEncodingException;

import java.util.HashMap;


/**
 * Набор статических методов для работы сo строками
 */
public final class StringFunc
{ // Begin Main Class
  private static final String PREFIX_ENCODE = "~`";
  private static final String SUFFIX_ENCODE = "`~";
  public static final String ENG_LOW_LETTERS = 
    "qwertyuiopasdfghjklzxcvbnm";
  public static final String ENG_UP_LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM";
  public static final String RUS_LOW_LETTERS = 
    "йцукенгшщзхъфывапролджэячсмитьбюё";
  public static final String RUS_UP_LETTERS = 
    "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮЁ";
  public static final String RUS_ALL_LETTERS = 
    "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮЁйцукенгшщзхъфывапролджэячсмитьбюё" + 
    (char) (0) + (char) (1) + (char) (2) + (char) (3) + (char) (4) + 
    (char) (5) + (char) (6) + (char) (7) + (char) (8) + (char) (9) + 
    (char) (10) + (char) (11) + (char) (12) + (char) (13) + (char) (14) + 
    (char) (15) + (char) (16) + (char) (17) + (char) (18) + (char) (19) + 
    (char) (20) + (char) (21) + (char) (22) + (char) (23) + (char) (24) + 
    (char) (25) + (char) (26) + (char) (27) + (char) (28) + (char) (29) + 
    (char) (30) + (char) (31);

  private static byte[] LIST_CHARSET =
  // DOS WIN KOI8-R ISO
  { ("й".getBytes())[0], (byte) 0xA9, (byte) 0xE9, (byte) 0xCA, 
    (byte) 0xD9, ("ц".getBytes())[0], (byte) 0xE6, (byte) 0xF6, 
    (byte) 0xC3, (byte) 0xE6, ("у".getBytes())[0], (byte) 0xE3, 
    (byte) 0xF3, (byte) 0xD5, (byte) 0xE3, ("к".getBytes())[0], 
    (byte) 0xAA, (byte) 0xEA, (byte) 0xCB, (byte) 0xDA, 
    ("е".getBytes())[0], (byte) 0xA5, (byte) 0xE5, (byte) 0xC5, 
    (byte) 0xD5, ("н".getBytes())[0], (byte) 0xAD, (byte) 0xED, 
    (byte) 0xCE, (byte) 0xDD, ("г".getBytes())[0], (byte) 0xA3, 
    (byte) 0xE3, (byte) 0xC7, (byte) 0xD3, ("ш".getBytes())[0], 
    (byte) 0xE8, (byte) 0xF8, (byte) 0xDB, (byte) 0xE8, 
    ("щ".getBytes())[0], (byte) 0xE9, (byte) 0xF9, (byte) 0xDD, 
    (byte) 0xE9, ("з".getBytes())[0], (byte) 0xA7, (byte) 0xE7, 
    (byte) 0xDA, (byte) 0xD7, ("х".getBytes())[0], (byte) 0xE5, 
    (byte) 0xF5, (byte) 0xC8, (byte) 0xE5, ("ъ".getBytes())[0], 
    (byte) 0xEA, (byte) 0xFA, (byte) 0xDF, (byte) 0xEA, 
    ("ф".getBytes())[0], (byte) 0xE4, (byte) 0xF4, (byte) 0xC6, 
    (byte) 0xE4, ("ы".getBytes())[0], (byte) 0xEB, (byte) 0xFB, 
    (byte) 0xD9, (byte) 0xEB, ("в".getBytes())[0], (byte) 0xA2, 
    (byte) 0xE2, (byte) 0xD7, (byte) 0xD2, ("а".getBytes())[0], 
    (byte) 0xA0, (byte) 0xE0, (byte) 0xC1, (byte) 0xD0, 
    ("п".getBytes())[0], (byte) 0xAF, (byte) 0xEF, (byte) 0xD0, 
    (byte) 0xDF, ("р".getBytes())[0], (byte) 0xE0, (byte) 0xF0, 
    (byte) 0xD2, (byte) 0xE0, ("о".getBytes())[0], (byte) 0xAE, 
    (byte) 0xEE, (byte) 0xCF, (byte) 0xDE, ("л".getBytes())[0], 
    (byte) 0xAB, (byte) 0xEB, (byte) 0xCC, (byte) 0xDB, 
    ("д".getBytes())[0], (byte) 0xA4, (byte) 0xE4, (byte) 0xC4, 
    (byte) 0xD4, ("ж".getBytes())[0], (byte) 0xA6, (byte) 0xE6, 
    (byte) 0xD6, (byte) 0xD6, ("э".getBytes())[0], (byte) 0xED, 
    (byte) 0xFD, (byte) 0xDC, (byte) 0xED, ("я".getBytes())[0], 
    (byte) 0xEF, (byte) 0xFF, (byte) 0xD1, (byte) 0xEF, 
    ("ч".getBytes())[0], (byte) 0xE7, (byte) 0xF7, (byte) 0xDE, 
    (byte) 0xE7, ("с".getBytes())[0], (byte) 0xE1, (byte) 0xF1, 
    (byte) 0xD3, (byte) 0xE1, ("м".getBytes())[0], (byte) 0xAC, 
    (byte) 0xEC, (byte) 0xCD, (byte) 0xDC, ("и".getBytes())[0], 
    (byte) 0xA8, (byte) 0xE8, (byte) 0xC9, (byte) 0xD8, 
    ("т".getBytes())[0], (byte) 0xE2, (byte) 0xF2, (byte) 0xD4, 
    (byte) 0xE2, ("ь".getBytes())[0], (byte) 0xEC, (byte) 0xFC, 
    (byte) 0xD8, (byte) 0xEC, ("б".getBytes())[0], (byte) 0xA1, 
    (byte) 0xE1, (byte) 0xC2, (byte) 0xD1, ("ю".getBytes())[0], 
    (byte) 0xEE, (byte) 0xFE, (byte) 0xC0, (byte) 0xEE, 
    ("ё".getBytes())[0], (byte) 0xF1, (byte) 0xB8, (byte) 0xA3, 
    (byte) 0xF1, ("Й".getBytes())[0], (byte) 0x89, (byte) 0xC9, 
    (byte) 0xEA, (byte) 0xB9, ("Ц".getBytes())[0], (byte) 0x96, 
    (byte) 0xD6, (byte) 0xE3, (byte) 0xC6, ("У".getBytes())[0], 
    (byte) 0x93, (byte) 0xD3, (byte) 0xF5, (byte) 0xC3, 
    ("К".getBytes())[0], (byte) 0x8A, (byte) 0xCA, (byte) 0xEB, 
    (byte) 0xBA, ("Е".getBytes())[0], (byte) 0x85, (byte) 0xC5, 
    (byte) 0xE5, (byte) 0xB5, ("Н".getBytes())[0], (byte) 0x8D, 
    (byte) 0xCD, (byte) 0xEE, (byte) 0xBD, ("Г".getBytes())[0], 
    (byte) 0x83, (byte) 0xC3, (byte) 0xE7, (byte) 0xB3, 
    ("Ш".getBytes())[0], (byte) 0x98, (byte) 0xD8, (byte) 0xFB, 
    (byte) 0xC8, ("Щ".getBytes())[0], (byte) 0x99, (byte) 0xD9, 
    (byte) 0xFD, (byte) 0xC9, ("З".getBytes())[0], (byte) 0x87, 
    (byte) 0xC7, (byte) 0xFA, (byte) 0xB7, ("Х".getBytes())[0], 
    (byte) 0x95, (byte) 0xD5, (byte) 0xE8, (byte) 0xC5, 
    ("Ъ".getBytes())[0], (byte) 0x9A, (byte) 0xDA, (byte) 0xFF, 
    (byte) 0xCA, ("Ф".getBytes())[0], (byte) 0x94, (byte) 0xD4, 
    (byte) 0xE6, (byte) 0xC4, ("Ы".getBytes())[0], (byte) 0x9B, 
    (byte) 0xDB, (byte) 0xF9, (byte) 0xCB, ("В".getBytes())[0], 
    (byte) 0x82, (byte) 0xC2, (byte) 0xF7, (byte) 0xB2, 
    ("А".getBytes())[0], (byte) 0x80, (byte) 0xC0, (byte) 0xE1, 
    (byte) 0xB0, ("П".getBytes())[0], (byte) 0x8F, (byte) 0xCF, 
    (byte) 0xF0, (byte) 0xBF, ("Р".getBytes())[0], (byte) 0x90, 
    (byte) 0xD0, (byte) 0xF2, (byte) 0xC0, ("О".getBytes())[0], 
    (byte) 0x8E, (byte) 0xCE, (byte) 0xEF, (byte) 0xBE, 
    ("Л".getBytes())[0], (byte) 0x8B, (byte) 0xCB, (byte) 0xEC, 
    (byte) 0xBB, ("Д".getBytes())[0], (byte) 0x84, (byte) 0xC4, 
    (byte) 0xE4, (byte) 0xB4, ("Ж".getBytes())[0], (byte) 0x86, 
    (byte) 0xC6, (byte) 0xF6, (byte) 0xB6, ("Э".getBytes())[0], 
    (byte) 0x9D, (byte) 0xDD, (byte) 0xFC, (byte) 0xCD, 
    ("Я".getBytes())[0], (byte) 0x9F, (byte) 0xDF, (byte) 0xF1, 
    (byte) 0xCF, ("Ч".getBytes())[0], (byte) 0x97, (byte) 0xD7, 
    (byte) 0xFE, (byte) 0xC7, ("С".getBytes())[0], (byte) 0x91, 
    (byte) 0xD1, (byte) 0xF3, (byte) 0xC1, ("М".getBytes())[0], 
    (byte) 0x8C, (byte) 0xCC, (byte) 0xED, (byte) 0xBC, 
    ("И".getBytes())[0], (byte) 0x88, (byte) 0xC8, (byte) 0xE9, 
    (byte) 0xB8, ("Т".getBytes())[0], (byte) 0x92, (byte) 0xD2, 
    (byte) 0xF4, (byte) 0xC2, ("Ь".getBytes())[0], (byte) 0x9C, 
    (byte) 0xDC, (byte) 0xF8, (byte) 0xCC, ("Б".getBytes())[0], 
    (byte) 0x81, (byte) 0xC1, (byte) 0xE2, (byte) 0xB1, 
    ("Ю".getBytes())[0], (byte) 0x9E, (byte) 0xDE, (byte) 0xE0, 
    (byte) 0xCE, ("Ё".getBytes())[0], (byte) 0xF0, (byte) 0xA8, 
    (byte) 0xB3, (byte) 0xA1 };

  private static HashMap<Byte, byte[]> TABLE_CHARSET = 
    new HashMap<Byte, byte[]>();
  static {
    for (int i = 0; i < LIST_CHARSET.length; i = i + 5)
    {
      TABLE_CHARSET.put(
            new Byte(LIST_CHARSET[i]), new byte[]
              { LIST_CHARSET[i + 1], LIST_CHARSET[i + 2], LIST_CHARSET[i + 3], 
                LIST_CHARSET[i + 4] });
    } // for
  }

  
  public static final String CR = "" + (char) (13);
  public static final String LF = "" + (char) (10);
  public static final String CRLF = "" + (char) (13) + ((char) (10));

  // Substring for Format :

  /**
   * Извлечение подстроки
   * 
   * @param str - Исходная строка
   * @param beginIndex - Индекс для начала поиска
   */
  public String substring(String str, int beginIndex)
  {
    String rc = "";
    if (str == null)
      return rc;
    int len = str.length();
    if ((beginIndex > (len - 1)) || (beginIndex < 0))
      return rc;
    try
    {
      rc = str.substring(beginIndex);
    }
    catch (Throwable ex)
    {
      rc = "";
    } // catch
    return rc;
  }

  /**
   * Извлечение подстроки
   * 
   * @param str - Исходная строка
   * @param beginIndex - Индекс для начала поиска
   * @param endIndex -  Индекс для конца поиска
   */
  public String substring(String str, int beginIndex, int endIndex)
  {
    String rc = "";
    if (str == null)
      return rc;
    if (beginIndex > endIndex)
      return rc;
    if (beginIndex < 0)
      return rc;
    if (endIndex > (str.length() - 1))
      return rc;
    try
    {
      rc = str.substring(beginIndex, endIndex);
    } // try
    catch (Throwable ex)
    {
      rc = "";
    } // catch
    return rc;
  }

  // ======================================================

  /**
   * Повторяет StringBuffer нужное кол-во раз
   */
  public static StringBuffer replicateStringBuffer(StringBuffer str, int n)
  {
    if (n <= 0) {
      return new StringBuffer();
    }
    for (int i = 1; i <= n; i++)
    {
      str.append(str);
    } // for
    return str;
  }

  /**
   * Повторяет String нужное кол-во раз
   */
  public static String replicate(String str, int n)
  {
    StringBuffer result = new StringBuffer("");
    if (n <= 0)  return "";
    for (int i = 1; i <= n; i++) {
      result = result.append(str);
    } // for
    return result.toString();
  }

  /**
   * Дополняет строку слева
   * 
   * @param str - строка
   * @param len - целевая длина
   * @param ch - символ - заполнитель
   * @return - целевая строка
   */
  public static String padl(String str, int len, char ch)
  {
    StringBuffer sch = new StringBuffer();
    int strlen = str.length();
    while ((strlen + sch.length()) < len)
    {
      sch = sch.append(ch); // concat(new String(ach));
    }
    sch = sch.append(str);

    int schlen = sch.length();
    if (schlen > len)
    {
      char[] arr = new char[schlen];
      sch.getChars(0, schlen, arr, 0);
      return String.copyValueOf(arr, schlen - len, len);
    }
    else
    {
      return sch.toString();
    }
  }

  /**
   * Дополняет строку справа
   * 
   * @param str - строка
   * @param len - целевая длина
   * @param ch -  символ - заполнитель
   * @return - целевая строка
   */
  public static String padr(String str, int len, char ch)
  {
    StringBuffer sch = new StringBuffer("");
    while ((str.length() + sch.toString().length()) < len)
    {
      // char ach[] = {ch};
      sch = sch.append(ch); // concat(new String(ach));
    }
    sch = new StringBuffer(str + sch.toString());
    if (sch.toString().length() > len)
    {
      return String.copyValueOf(sch.toString().toCharArray(), 0, len);
    }
    else
    {
      return sch.toString();
    }
  }

  /**
   * Центрирует строку
   * 
   * @param str - строка
   * @param len - целевая длина
   * @param ch -  символ - заполнитель
   * @return - целевая строка
   */
  public static String padc(String str, int len, char ch)
  {
    if (str.length() == len)
      return str;
    if (str.length() > len)
      return String.copyValueOf(str.toCharArray(), 0, len);
    StringBuffer sch1 = new StringBuffer("");
    StringBuffer sch2 = new StringBuffer("");
    StringBuffer result;
    // char ach[] = {ch};
    int i1 = 0;
    int i2 = 0;
    while ((str.length() + i1 + i2) < len)
    {
      sch1 = sch1.append(ch); // (new String(ach));
      sch2 = sch2.append(ch); // concat(new String(ach));
      i1++;
      i2++;
    }
    result = sch1.append(str);
    result = result.append(sch2.toString());
    if (result.toString().length() > len)
    {
      return String.copyValueOf(result.toString().toCharArray(), 0, len);
    }
    else
    {
      return result.toString();
    }
  }

  /**
   * Подсчет вхождений подстроки в строку
   * 
   * @param substr - подстрока
   * @param str - строка
   * @return - кол-во вхождений
   */
  public static int countSubStr(String substr, String str)
  {
    int result = 0;
    int lensubstr = substr.length();
    for (int i = 0; ((i + lensubstr) <= str.length()); i++)
    {
      String sub = str.substring(i, i + lensubstr);
      if (sub.compareTo(substr) == 0)
        result++;
    }
    return result;
  }
  
  public static int countLines(String str)
  {
    if (str == null || str.length() == 0)
        return 0;
    int lines = 1;
    int len = str.length();
    for( int pos = 0; pos < len; pos++) {
        char c = str.charAt(pos);
        if( c == '\r' ) {
            lines++;
            if ( pos+1 < len && str.charAt(pos+1) == '\n' )
                pos++;
        } else if( c == '\n' ) {
            lines++;
        }
    }
    return lines;
  }

  /**
   * Удаление левых символов из строки, из заданного списка
   * 
   * @param str - строка
   * @param del - массив символов для удаления
   * @return - преобразованная строка
   */
  public static String ltrim(String str, char[] del)
  {
    for (int i = 0; i < del.length; i++)
    {
      str = ltrim(str, del[i]);
    } // for
    return str;
  }

  /**
   * Удаление правых символов из строки, из заданного списка
   * 
   * @param str - строка
   * @param del - массив символов для удаления
   * @return - преобразованная строка
   */
  public static String rtrim(String str, char[] del)
  {
    for (int i = 0; i < del.length; i++)
    {
      str = rtrim(str, del[i]);
    } // for
    return str;
  }

  /**
   * Удаление левых и правых символов из строки, из заданного списка
   * 
   * @param str - строка
   * @param del - массив символов для удаления
   * @return - преобразованная строка
   */
  public static String alltrim(String str, char[] del)
  {
    for (int i = 0; i < del.length; i++)
    {
      str = alltrim(str, del[i]);
    } // for
    return str;
  }

  /**
   * Удаление левых и правых пробелов из строки
   * 
   * @param str - строка
   * @return - преобразованная строка
   */
  public static String alltrim(String str)
  {
    return alltrim(str, ' ');
  }

  /**
   * Удаление левых и правых символов из строки
   * 
   * @param str - строка
   * @param del - символ для удаления
   * @return - преобразованная строка
   */
  public static String alltrim(String str, char del)
  {
    return (ltrim(rtrim(str, del), del));
  }

  /**
   * Удаление левых и правых пробелов из строки
   * 
   * @param str - строка
   * @return - преобразованная строка
   */
  public static String ltrim(String str)
  {
    return ltrim(str, ' ');
  }

  /**
   * Удаление левых символов из строки
   * 
   * @param str - строка
   * @param del - символ для удаления
   * @return - преобразованная строка
   */
  public static String ltrim(String str, char del)
  {
    if (str.length() == 0)
      return str;
    char ach[] = str.toCharArray();
    char ch;
    int i = 0;
    while (i < str.length())
    {
      ch = ach[i];
      if (ch != del)
        break;
      i++;
    }
    if (i == str.length())
      return str;
    return String.copyValueOf(str.toCharArray(), i, (str.length() - i));
  }

  /**
   * Удаление правых символов из строки
   * 
   * @param str - строка
   * @return - преобразованная строка
   */
  public static String rtrim(String str)
  {
    return rtrim(str, ' ');
  }

  /**
   * Удаление правых символов из строки
   * 
   * @param str - строка
   * @param del - символ для удаления
   * @return - преобразованная строка
   */
  public static String rtrim(String str, char del)
  {
    if (str.length() == 0)
      return str;
    char ach[] = str.toCharArray();
    char ch;
    int i = str.length() - 1;
    while (i > -1)
    {
      ch = ach[i];
      if (ch != del)
        break;
      i--;
    }
    if (i == -1)
      return str;
    return String.copyValueOf(str.toCharArray(), 0, i + 1);
  }

  /**
   * Дополнение числа слева - нулями
   * 
   * @param value - целое число
   * @param len
   * @return - Преобразованная число - строка
   */
  public static String strZero(int value, int len)
  {
    return padl(Integer.toString(value), len, '0');
  }

  /**
   * Получить имя файла из полного имени файла
   * 
   * @param fullFileName - полного имени файла
   * @return - сокращенное имя файла
   */
  public static String getFileName(String fullFileName)
  {
    String rc = fullFileName.trim();
    int rSlesh = rc.lastIndexOf("\\");
    if (rSlesh != -1)
    {
      rc = rc.substring(rSlesh + 1);
    }
    else
    {
      rSlesh = rc.lastIndexOf("/");
      if (rSlesh != -1)
      {
        rc = rc.substring(rSlesh + 1);
      }

    }
    return rc;
  }

  /**
   * Конструирует полное имя файла
   * 
   * @param fileName - имя файла
   * @param dirName - имя директории
   * @return - полное имя файла
   */
  public static String getFullFileName(String fileName, String dirName)
  {
    String d = dirName.trim();
    String f = fileName.trim();
    if (!(d.equals("")))
    {
      if (!(d.substring(d.length() - 1)).equals("\\"))
      {
        d = d + "\\";
      }
    }
    return (d + f);
  }

  /**
   * Конвертация объекта типа Rectangle в String
   * 
   * @param r - Rectangle
   * @return - String
   */
  public static String rectangleToString(Rectangle r)
  {
    if (r == null)
      return "0,0,0,0";
    return r.x + "," + r.y + "," + r.width + "," + r.height;
  }

  /**
   * Проверка строки на непустоту
   * 
   * @param s - строка
   * @return - true - строка null,'',или содержит только пробелы. Иначе -
   *         false
   */
  public static boolean isEmpty(String s)
  {
    if (s == null)
      return true;
    s = s.trim();
    if (s.length() == 0)
      return true;
    return false;
  }

  public static boolean stob(String s)
  {
    if (s == null)
      return false;
    s = s.trim();
    if (s.equals("1"))
      return true;
    if (s.equalsIgnoreCase("true"))
      return true;
    return false;
  }

  public static Boolean stringToBoolean(String s)
  {
    boolean b = stob(s);
    return b;
  }

  public int parseInt(String s, int defint)
  {
    if (s == null)
      return defint;
    int rc = defint;
    try
    {
      rc = Integer.parseInt(s);
    } // try
    catch (Exception ex)
    {
      rc = defint;
    } // catch
    return rc;
  }

  /** ********************** strtran in String : ************* */

  /**
   * Замена подстроки в строке на другую подстроку
   * 
   * @param text - Исходная строка
   * @param find - Строка для поиска
   * @param repl - Строка для замены
   * @return - преобразованная строка
   */
  public static String replace(String text, String find, String repl)
  {
    return replace(text, find, repl, -1);
  }

  public static String replace(String text, String[] find, String[] repl)
  {
    int len = find.length;
    for (int i = 0; i < len; i++)
    {
      String f = find[i];
      String r = repl[i];
      text = replace(text, f, r);
    }
    return text;
  }


  // ---------------------------------------------------------------------------

  /**
   * Замена подстроки в строке на другую подстроку ограниченное число раз
   * 
   * @param text - Исходная строка
   * @param find - Строка для поиска
   * @param repl - Строка для замены
   * @return - преобразованная строка
   */
  public static String replace(String text, String find, String repl, 
                               int max)
  {
    if (text == null)
      return null;
    StringBuffer buf = new StringBuffer(text.length());
    int start = 0;
    int end = 0;
    while (true) 
    {
      end = text.indexOf(find, start);
      if (end == -1) 
      {
        break;
      }
      buf.append(text.substring(start, end)).append(repl);
      start = end + find.length();
      if (--max == 0) {
        break;
      }
    }
    buf.append(text.substring(start));
    return buf.toString();
  }

  /**
   * Перестановка символов в строке в обратном порядке
   * 
   * @param str - исходная строка
   * @return - преобразованная строка
   */
  public static String reverse(String str)
  {
    return (new StringBuffer(str)).reverse().toString();
  }

  public static int lastIndexOfAny(String str, String[] strs)
  {
    int sz = strs.length;
    int ret = -1;
    int tmp = 0;
    for (int i = 0; i < sz; i++)
    {
      tmp = str.lastIndexOf(strs[i]);
      if (tmp > ret)
        ret = tmp;
    }

    return ret;
  }

  /**
   * "Заворачивание" строки
   * 
   * @param str - Исходная строка
   * @param width - Ширина строки
   * @param delim - Разделители в строке
   * @return - преобразованная строка
   */
  public static String wordWrap(String str, int width, String delim)
  {
    int sz = str.length();
    width++;
    StringBuffer buffer = 
      new StringBuffer((sz / width) * delim.length() + sz);
    width -= delim.length();
    int idx = -1;
    String substr = null;
    for (int i = 0; i < sz; i += width)
    {
      if (i > sz - width)
      {
        buffer.append(str.substring(i));
        break;
      }
      substr = str.substring(i, i + width);
      idx = substr.indexOf(delim);
      if (idx != -1)
      {
        buffer.append(substr.substring(0, idx));
        buffer.append(delim);
        i -= width - idx - delim.length();
        if (substr.charAt(idx + 1) != '\n' && 
            Character.isWhitespace(substr.charAt(idx + 1)))
          i++;
      }
      else
      {
        idx = -1;
        char chrs[] = substr.toCharArray();
        for (int j = width; j > 0; j--)
        {
          if (!Character.isWhitespace(chrs[j - 1]))
            continue;
          idx = j;
          break;
        }

        if (idx == -1)
        {
          for (int j = width; j > 0; j--)
          {
            if (chrs[j - 1] != '-')
              continue;
            idx = j;
            break;
          }

          if (idx == -1)
          {
            buffer.append(substr);
            buffer.append(delim);
          }
          else
          {
            if (idx != width)
              idx++;
            buffer.append(substr.substring(0, idx));
            buffer.append(delim);
            i -= width - idx;
          }
        }
        else
        {
          buffer.append(substr.substring(0, idx));
          buffer.append(replicate(" ", width - idx));
          buffer.append(delim);
          i -= width - idx;
        }
      }
    }

    return buffer.toString();
  }

  /**
   * Проверяет - является ли строка - словом
   * 
   * @param str - тестируемая строка
   * @return - true - строка - слово. false - иначе.
   */
  public static boolean isWord(String str)
  {
    int sz = str.length();
    for (int i = 0; i < sz; i++)
      if (!Character.isLetter(str.charAt(i)))
        return false;

    return true;
  }

  /**
   * Проверяет - является ли строка - алфавитно - цифровым набором
   * 
   * @param str - тестируемая строка
   * @return - true - является . false - иначе .
   */
  public static boolean isAlphanumeric(String str)
  {
    int sz = str.length();
    for (int i = 0; i < sz; i++)
      if (!Character.isLetterOrDigit(str.charAt(i)))
        return false;

    return true;
  }

  /**
   * Проверяет - является ли строка - числом
   * 
   * @param str - тестируемая строка
   * @return - true - является . false - иначе .
   */
  public static boolean isNumeric(String str)
  {
    int sz = str.length();
    for (int i = 0; i < sz; i++)
    {
      if (!Character.isDigit(str.charAt(i)))
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Замена в строке всех символов \ на / И // на /
   * 
   * @param fileName - исходная строка
   * @return - преобразованная строка
   */
  public static String normalizeFileName(String fileName)
  {
    fileName = fileName.replace("\\\\", "\\");
    fileName = fileName.replace("//", "/");
    return fileName;
  }

  public static String splitLine(String inStr, int maxLineLen, String eol)
  {
    if (maxLineLen <= 0)
    {
      return inStr;
    }
    StringBuffer rc = new StringBuffer();
    final String sep = ((char) 254) + "";
    String listInStr = inStr.replace(eol, sep);
    // String listInStr = replace(inStr, eol, sep);
    String[] tok = listInStr.split(sep);
    for (int i = 0; i < tok.length; i++)
    {
      rc.append(splitOneSimpleLine(tok[i], maxLineLen, sep));
      if (i < (tok.length - 1))
      {
        rc.append(sep);
      }
    }
    String rcStr = rc.toString();
    rcStr = rcStr.replace(sep, eol);
    return rcStr;
  }

  private static StringBuffer splitOneSimpleLine(String inStr, 
                                                 int maxLineLen, 
                                                 String sep)
  {
    StringBuffer rc = new StringBuffer();
    int index = 0;
    int len = inStr.length();
    while (true)
    {
      if (index >= len)
      {
        break;
      }
      int index2 = index + maxLineLen;
      index2 = Math.min(index2, len);
      if (rc.length() > 0)
      {
        rc.append(sep);
      }
      rc.append(inStr.substring(index, index2));
      // -------------
      index = index2;
    }
    return rc;
  }

  // --------------------------

  /**
   * Преобразование из одного формата в другой :
   */
  static boolean isCp1252(final char ch)
  {
    final int min = 168;
    final int maх = 255;
    final int cur = (ch);
    if (cur >= min && cur <= maх)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  static boolean isCp1251(final char ch)
  {
    final int min = 1025;
    final int maх = 1105;
    final int cur = (ch);
    if (cur >= min && cur <= maх)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public static boolean isRusText(String text)
  {
    if (isEmpty(text))
    {
      return false;
    }
    char[] chars = RUS_LOW_LETTERS.toCharArray();
    for (char ch: chars)
    {
      if (text.indexOf(ch) > -1)
      {
        return true;
      }
    }
    return false;
  }

  public static String decode(String input, String inputEcoding, 
                              // "Cp1252"
    String outputEncoding) // "Cp1251"
  {
    try
    {
      if (input != null)
      {
        byte[] tmp = input.getBytes(inputEcoding);
        return (new String(tmp, outputEncoding));
      }
      else
      {
        return (null);
      }
    }
    catch (UnsupportedEncodingException ex)
    {
      ex.printStackTrace();
      return (null);
    }
  }
  //--------------------------------

  public static String encodeRusString(String txt)
  {
    if (StringFunc.isEmpty(txt))
    {
      return txt;
    }
    final String allRusChars = StringFunc.RUS_ALL_LETTERS;
    StringBuffer rc = new StringBuffer();
    char[] chars = txt.toCharArray();
    //for (char ch : chars) {
    for (int i = 0; i < chars.length; i++)
    {
      char ch = chars[i];
      int indexOf = allRusChars.indexOf(ch);
      if (indexOf == -1)
      {
        rc.append(ch);
      }
      else
      {
        rc.append(encodeRusChar(indexOf));
      }
    }
    return rc.toString();
  }

  private static String encodeRusChar(int i)
  {
    return PREFIX_ENCODE + i + SUFFIX_ENCODE;
  }

  public static String decodeRusString(String txt)
  {
    //String RUS_ALL_LETTERS = StringFunc.RUS_ALL_LETTERS;
    //String PREFIX_ENCODE = AjaxUtils.PREFIX_ENCODE;
    //String SUFFIX_ENCODE = AjaxUtils.SUFFIX_ENCODE;
    for (int i = 0; i < RUS_ALL_LETTERS.length(); i++)
    {
      String find = PREFIX_ENCODE + i + SUFFIX_ENCODE;
      String target = RUS_ALL_LETTERS.substring(i, i + 1);
      txt = StringFunc.replace(txt, find, target);
    }
    return txt;
  }
  public static void main(String[] args)
  {
    String t = "Цапля";
    String t1 = encodeRusString(t);
    String t2 = decodeRusString(t1);
    System.out.println(t2);
  }

}
