package com.rcore.global;
// ШМЯ

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Класс для работы с файловой системой
 *
 * @author mark
 */
public final class Files
{
  /**
   * Включать директорию в список
   */
  public static final int INCLUDE_DIR = 1;

  /**
   * Не включать директорию в список
   */
  public static final int EXCLUDE_DIR = 2;

  /**
   * Включать директорию в список, в соответствии с расширением
   */
  public static final int INCLUDE_DIR_BY_EXTENSION = 3;

  /**
   * Удалить файл
   * 
   * @param file -
   *            имя файла
   * @return true - при успешном удалении, false - в противном случае
   */
  public static boolean removeFile(String file)
  {
    File f = new File(file);
    return f.delete();
  }

  /**
   * Получить имя временного файла
   * 
   * @param prefix -
   *            префикс файла
   * @param end -
   *            суффикс файла
   * @return Имя временного файла
   */
  public static synchronized String getTempNameFile(String prefix, 
                                                    String end)
  {
    String rc = prefix + "." + end;
    int i = 0;
    while (true)
    {
      if (i > 0)
      {
        rc = prefix + "." + i + "." + end;
      }
      if (!Files.isFileExists(rc))
      {
        break;
      }
      i++;
    }
    return rc;
  }

  /**
   * Проверяет наличие файла
   * 
   * @param fileName -
   *            имя файла
   * @return true - если файл есть, false - иначе
   */
  public static boolean isFileExists(String fileName)
  {
    boolean rc = false;
    File file = new File(fileName);
    rc = file.exists();
    if (!rc)
    {
      try
      {
        URL url = new URL(fileName);
        InputStream input = url.openStream();
        input.close();
        rc = true;
      } // try
      catch (Exception ex)
      {
        ;
      } // catch
    }
    return rc;
  } // public static boolean isFileExists(String fileName)

  /**
   * Получить файл как массив байтов
   * 
   * @param fileName -
   *            Имя файла
   * @return массив байтов
   * @throws IOException
   */
  public static byte[] getByteArrayFromFile(String fileName)
    throws IOException
  {
    InputStream iStream = fileToStream(fileName);
    byte buffer[] = Resource.inputStreamToByteArray(iStream);
    iStream.close();
    return buffer;
  }

  /**
   * Записать файл
   * 
   * @param fileName -
   *            имя файла
   * @param str -
   *            строка для содержимого файла
   * @throws IOException
   */
  public static synchronized void putFile(String fileName, String str)
    throws IOException
  {
    makeDirsByFile(fileName);
    FileOutputStream oStream = new FileOutputStream(fileName, false);
    oStream.write(str.getBytes());
    oStream.close();
  }

  public static synchronized void saveFile(String fileName, String str)
    throws IOException
  {
    putFile(fileName, str);
  }

  /**
   * Создание все директорий (поддиректорий) связанных с файлом
   * 
   * @param fileName -
   *            имя файла
   * @return true
   * @throws IOException
   */
  public static synchronized boolean makeDirsByFile(String fileName)
    throws IOException
  {
    boolean rc = false;
    String dir = getDirName(fileName);
    File file = new File(dir);
    file = new File(file.getCanonicalPath());
    rc = file.mkdirs();
    return rc;
  }

  /**
   * Запись последовательности байтов в файл
   * 
   * @param fileName -
   *            имя файла
   * @param value -
   *            последовательность байтов
   * @throws IOException
   */
  public static synchronized void putFile(String fileName, byte[] value)
    throws IOException
  {
    FileOutputStream oStream = new FileOutputStream(fileName, false);
    oStream.write(value);
    oStream.close();
  }

  /**
   * Запись последовательности байтов в файл с дополнением файла
   * 
   * @param fileName -
   *            имя файла
   * @param value -
   *            последовательность байтов
   * @throws IOException
   */
  public static synchronized void putFileAppend(String fileName, 
                                                byte[] value)
    throws IOException
  {
    FileOutputStream oStream = new FileOutputStream(fileName, true);
    oStream.write(value);
    oStream.close();
  }

  /**
   * Запись последовательности байтов в файл с дополнением файла
   * 
   * @param fileName -
   *            имя файла
   * @param str -
   *            последовательность байтов
   * @throws IOException
   */
  public static synchronized void putFileAppend(String fileName, 
                                                String str)
    throws IOException
  {
    makeDirsByFile(fileName);
    FileOutputStream oStream = new FileOutputStream(fileName, true);
    oStream.write(str.getBytes());
    oStream.close();
  }

  /**
   * Открытие файла как InputStream
   * 
   * @param file -
   *            имя файла
   * @return - InputStream
   */
  public static InputStream fileToStream(String file)
  {
    InputStream input = null;
    file = file.trim();
    try
    {
      input = new FileInputStream(file);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    return input;
  }

  /**
   * Получить файл в виде строки
   * 
   * @param file -
   *            Имя файла
   * @return - Строка - содержимое файла
   */
  public static String getFile(String file)
  {
    InputStream input = fileToStream(file);
    if (input == null)
      return null;

    String rc = null;
    try
    {
      rc = Resource.inputStreamToString(input);
      input.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return rc;
  }

  // ****** trace ****************** End

  /**
   * Получить оглавление ZIP - файла
   */
  public static ArrayList<String> getEntriesInZip(String zip)
  {
    ArrayList<String> rc = new ArrayList<String>();
    FileInputStream input = null;
    ZipInputStream zipStream = null;
    try
    {
      input = new FileInputStream(zip);
      zipStream = new ZipInputStream(input);
      while (true)
      {
        ZipEntry entry = zipStream.getNextEntry();
        rc.add(entry.getName());
      } // while
    } // try
    catch (Exception ex)
    {
      ;
    } // catch
    finally
    {
      if (zipStream != null)
      {
        try
        {
          zipStream.close();
        } // try
        catch (Exception ex)
        {
          ;
        }
      }
    } // finally
    return rc;
  }

  /**
   * Проверить наличие файла в zip - архиве
   * 
   * @param zip -
   *            имя zip - архива
   * @param name -
   *            имя файла
   * @return - true - файл присутствует, false - отсутствует
   */
  public static boolean isFileInZip(String zip, String name)
  {
    ArrayList<String> list = getEntriesInZip(zip);
    return (list.indexOf(name) > -1);
  } // public static boolean isFileInZip(String zip,String name) {

  /**
   * Получить файл из Zip - архива
   * 
   * @param zip -
   *            имя zip-архива
   * @param name -
   *            имя файла
   * @return - последовательность байтов - сожержимое файла
   * @throws IOException
   * @throws FileNotFoundException
   */
  public static byte[] getFileFromZip(String zip, String name)
    throws IOException, FileNotFoundException
  {
    FileInputStream input = null;
    ZipInputStream zipStream = null;
    input = new FileInputStream(zip);
    zipStream = new ZipInputStream(input);
    byte rc[] = new byte[0];
    try
    {
      while (true)
      {
        ZipEntry entry = zipStream.getNextEntry();
        if (entry.getName().equals(name))
        {
          break;
        }
      } // while
      rc = getFileFromZipStream(zipStream);
      zipStream.closeEntry();
    } // try
    catch (Exception ex)
    {
      throw new FileNotFoundException("File " + name + " not Found In " + 
                                      zip);
    }
    finally
    {
      if (zipStream != null)
      {
        try
        {
          zipStream.close();
        } // try
        catch (Exception ex)
        {
          ;
        }
      }
    }
    return rc;
  } // public static byte[] getFileFromZip(String zip,String name) {

  private static byte[] getFileFromZipStream(ZipInputStream zipStream)
  {
    int count = 0;
    byte rc[] = null;
    byte buf[] = new byte[1024];
    ArrayList<byte[]> list = new ArrayList<byte[]>();
    int off = 0;
    int len = 0;
    try
    {
      while (true)
      {
        len = zipStream.read(buf, off, 1024);
        if (len == -1)
          break;
        count += (len);
        byte tmp[] = new byte[len];
        for (int j = 0; j < tmp.length; j++)
        {
          tmp[j] = buf[j];
        } // for (j)
        list.add(tmp);
      } // while
    } // try
    catch (Exception ex)
    {
      ;
    }
    finally
    {
      rc = new byte[count];
      int k = -1;
      for (int i = 0; i < list.size(); i++)
      {
        byte tmp[] = (list.get(i));
        for (int j = 0; j < tmp.length; j++)
        {
          k++;
          rc[k] = tmp[j];
        } // for (j)
      } // for (i)
    }
    return rc;
  }

  /**
   * Получить оглавление директории
   * 
   * @param path -
   *            директория
   * @param extension -
   *            расширение файлов
   * @return - ArrayList<File> - список файлов из директории с нужным
   *         расширением
   */
  public static List<File> getFileList(String path, String extension)
  {
    return getFileList(path, false, extension, INCLUDE_DIR_BY_EXTENSION);
  }

  /**
   * Получить оглавление директории
   * 
   * @param path -
   *            директория
   * @return - ArrayList<File> - список файлов из директории
   */
  public static List<File> getFileList(String path)
  {
    return getFileList(path, false);
  }

  /**
   * Получить оглавление директории и (опционально) поддиректорий
   * 
   * @param path -
   *            директория
   * @param reqursive -
   *            признак рекурсивного поиска
   * @return - ArrayList<File> - список файлов из директории
   */
  public static List<File> getFileList(String path, boolean reqursive)
  {
    return getFileList(path, reqursive, null, INCLUDE_DIR_BY_EXTENSION);
  }

  /**
   * Удаление директории
   * 
   * @param file -
   *            объект, указывающий на директорию.
   */
  public static void removeDirectory(File file)
  {
    try
    {
      String path = file.getCanonicalPath();
      List<File> list = getFileList(path, false, null, INCLUDE_DIR);
      if (list == null)
        list = new ArrayList<File>();
      for (int i = 0; i < list.size(); i++)
      {
        File fileItem =  (list.get(i));
        if (!fileItem.isDirectory())
        {
          try
          {
            fileItem.delete();
          } // try
          catch (Throwable ex)
          {
            ;
          } // catch
        }
        else
        {
          removeDirectory(fileItem);
        }
      } // for
      try
      {
        file.delete();
      } // try
      catch (Throwable ex)
      {
        ;
        // ex.printStackTrace();
      } // catch

    } // try
    catch (Throwable ex)
    {
      ex.printStackTrace();
    } // catch
    finally
    {

    } // finally
  }

  /**
   * Получить список файлов из директории (директорий)
   * 
   * @param path -
   *            директория
   * @param reqursive -
   *            признак рекурсивного поиска
   * @param extension -
   *            расширение файлов
   * @param includeDirMode -
   *            признак того, что в список надо включать имена поддиректорий
   * @return - ArrayList<File> список файлов и директорий
   */
  public static List<File> getFileList(String path, 
                                       boolean reqursive, 
                                       String extension, 
                                       int includeDirMode)
  {
    ArrayList<File> rc = new ArrayList<File>();
    getFileList(path, reqursive, extension, includeDirMode, rc);
    return rc;
  }

  private static void getFileList(String path, boolean reqursive, 
                                  String extension, int includeDirMode, 
                                  List<File> listFiles)
  {
    File fileDir = new File(path);
    String[] list = fileDir.list();
    if (list == null)
    {
      list = new String[0];
    }
    for (int i = 0; i < list.length; i++)
    {
      String name = list[i];
      File file = new File(fileDir, name);
      boolean isDir = file.isDirectory();
      String fullName = name;
      try
      {
        fullName = file.getCanonicalPath();
      }
      catch (Throwable ex)
      {
        ex.printStackTrace();
      } // catch
      int j = fullName.lastIndexOf('.');
      String ext = "";
      if (j >= 0)
        ext = fullName.substring(j + 1);
      if (isDir)
      {
        if (includeDirMode == INCLUDE_DIR)
        {
          ;
        }
        else if (includeDirMode == EXCLUDE_DIR)
        {
          ;
        }
        else if (includeDirMode == INCLUDE_DIR_BY_EXTENSION)
        {
          ;
        }
        else
        {
          includeDirMode = INCLUDE_DIR_BY_EXTENSION;
        }
        // =================================================
        if (includeDirMode == INCLUDE_DIR)
        {
          listFiles.add(file);
        }
        else if (includeDirMode == EXCLUDE_DIR)
        {
          ;
        }
        else if (includeDirMode == INCLUDE_DIR_BY_EXTENSION)
        {
          if (extension == null)
          {
            listFiles.add(file);
          }
          else
          {
            if (extension.equals(ext))
            {
              listFiles.add(file);
            }
          }
        } // else if
        if (reqursive)
        {
          getFileList(fullName, true, extension, includeDirMode, 
                      listFiles);
        }
      } // if (isDir)
      else
      // if (isDir)
      {
        if (extension == null)
        {
          listFiles.add(file);
        }
        else
        {
          if (extension.equals(ext))
          {
            listFiles.add(file);
          }
        }
      }
    } // for i
  }

  public static byte[] getFileNoCaseSensitive(File file)
    throws Exception
  {
    byte rc[] = null;
    boolean isExists = file.exists();
    if (isExists)
    {
      long llength = file.length();
      if (llength > Integer.MAX_VALUE)
      {
        throw new Exception("DMPI-1005: Long documents are not supported " + 
                            llength);
      }
      rc = new byte[(int) llength];

      FileInputStream fis = new FileInputStream(file);
      fis.read(rc);
      fis.close();
    }
    else
    {
      String canonicalPath = file.getCanonicalPath();
      String dir = getDirName(canonicalPath);
      String fileName = StringFunc.getFileName(canonicalPath);
      String lowFileName = fileName.toLowerCase();

      file = new File(dir);
      String[] list = file.list();
      String newFileName = null;
      for (int i = 0; i < list.length; i++)
      {
        String curFileName = list[i];
        if (curFileName.toLowerCase().equals(lowFileName))
        {
          newFileName = curFileName;
          break;
        }
      } // for i
      if (newFileName != null)
      {
        System.out.println("Change Name Reading File : " + fileName + 
                           " To " + newFileName);
        rc = getFileNoCaseSensitive(new File(dir, newFileName));
      }
      else
      {
        throw new FileNotFoundException(canonicalPath);
      }
    }
    return rc;
  } // public static byte[] getFileNoCaseSensitive(File file)

  // mark 22.09.2006 18:11:12 -----------------------

  public static String getFileName(String path)
  {
    int i = path.lastIndexOf("/");
    if (i == -1)
    {
      i = path.lastIndexOf("\\");
    }
    if (i == -1)
    {
      return path;
    }
    String rc = path.substring(i + 1);
    return rc;
  }

  public static String getFileExtention(String fileName)
  {
    int index = fileName.lastIndexOf('.');
    if (index == -1)
      return "";
    String rc = fileName.substring(index + 1);
    return rc;
  }

  public static String getFileNameWithoutExt(String fileName)
  {
    String rc = getFileName(fileName);
    int index = rc.lastIndexOf('.');
    if (index == 0)
      return rc;
    rc = rc.substring(0, index);
    return rc;
  }

  /**
   * Получить имя директории из полного имени файла
   * 
   * @param fullFileName -
   *            полное имя файла
   * @return - имя директории
   */
  public static String getDirName(String fullFileName)
  {
    String rc = fullFileName.trim();
    int rSlesh = rc.lastIndexOf("\\");
    if (rSlesh != -1)
    {
      rc = rc.substring(0, rSlesh + 1);
    }
    else
    {
      rSlesh = rc.lastIndexOf("/");
      if (rSlesh != -1)
      {
        rc = rc.substring(0, rSlesh + 1);
      }
      else
      {
        rc = "";
      }
    }

    return rc;
  }
  
  public static void saveFileByWriter(String file, String content)
    throws IOException
  {
    FileWriter wr = new FileWriter(file);
    wr.write(content);
    wr.close();
  }
  public static void saveFileInUTF8(String file, String content)
    throws Exception
  {
    Writer out = new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
    out.write(content);
    out.close();
  }
  
  public static void main(String[] args)
    throws Exception
  {
    //saveFileByWriter("c:/temp/test_saveFileByWriter.txt","Цапля QQQQQ");
    saveFileInUTF8("c:/temp/test_saveFileByUTF8.txt","Цапля QQQQQ");
  }

} // class
