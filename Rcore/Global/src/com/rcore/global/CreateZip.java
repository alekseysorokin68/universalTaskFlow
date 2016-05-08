package com.rcore.global;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * Class для работы с архивами
 * @see CreateZipAbstract
 */

public class CreateZip extends CreateZipAbstract {

  private String              archivFile;
  private ZipOutputStream     zipOutStream = null;
  private HttpServletResponse response;
  //private PageContext         pageContext;

  public void openZip(
      String archivFile,
      HttpServletRequest request,
      HttpServletResponse response,
      PageContext pageContext) throws IOException {
    this.archivFile = archivFile;
    //this.request = request;
    this.response = response;
    //this.pageContext = pageContext;
    //String fileByUrl = ServletUtils.getRealPath(archivFile, pageContext);
    OutputStream outStream = new FileOutputStream(archivFile);
    this.zipOutStream = new ZipOutputStream(outStream);
  }

  public void addFile(String existFile) throws IOException {

    String shortName = Files.getFileName(existFile);
    ZipEntry entry = new ZipEntry(shortName);
    zipOutStream.putNextEntry(entry);
    byte[] buf = getByteArrayFromFile(existFile);
    //System.out.println(new String(buf));
    zipOutStream.write(buf);
    zipOutStream.closeEntry();
  }

  public byte[] getByteArrayFromFile(String existFile) throws IOException {
    //URL url = new URL(existFile);
    //InputStream is = url.openStream();
    //String realpath = ServletUtils.getRealPath(existFile, this.pageContext);
    //InputStream is = new FileInputStream(realpath);
    InputStream is = new FileInputStream(existFile);
    byte[] rc = Resource.inputStreamToByteArray(is);
    try {
      is.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return rc;
  }

  public int getLengthFile(String existFile) throws IOException {
    //URL url = new URL(existFile);
    InputStream is = new FileInputStream(
        //ServletUtils.getRealPath(existFile,this.pageContext)
        existFile
        );
    int rc = is.available();
    is.close();
    return rc;
  }

  @Override
  public void closeZip() throws IOException {
    zipOutStream.close();
  }

  @Override
  public void forceDownload() {
    response.setHeader("Content-Type", "application/octet-stream;");
    String shortname = Files.getFileName(archivFile);
    response.setHeader("Content-Disposition", "filename=\"" + shortname + "\"");
    try {
      BufferedInputStream in = new BufferedInputStream(
          new FileInputStream(
              //ServletUtils.getRealPath(archivFile, pageContext))
              archivFile)
          );
      BufferedOutputStream binout = new BufferedOutputStream(response
          .getOutputStream());
      int ch = in.read();
      while (ch != -1) {
        binout.write(ch);
        ch = in.read();
      }
      binout.close();
      in.close();
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /*
   public void forceDownload() {
   response.setHeader("Pragma", "public");
   response.setHeader("Expires", "0");
   response.setHeader(
   "Cache-Control",
   "must-revalidate, post-check=0, pre-check=0, private");
   response.setHeader("Content-Type", "application/zip");
   response.setHeader("Content-Disposition", "attachment; filename="
   + Files.getFileName(archivFile)
   + ";");
   response.setHeader("Content-Transfer-Encoding", "binary");

   try {
   response.setHeader("Content-Length", getLengthFile(archivFile) + "");
   byte[] outBuffer = getByteArrayFromFile(archivFile);
   OutputStream outStream = new BufferedOutputStream(response
   .getOutputStream());
   outStream.write(outBuffer);
   outStream.close();
   }
   catch (IOException e) {
   e.printStackTrace();
   }
   }
   */

  public static void main(String[] args) {
    if (false) {
      test_zip_file();
      return;
    }
    String inpFile1 = "http://localhost:8080/rgb/test/a.txt";
    String inpFile2 = "http://localhost:8080/rgb/test/b.txt";
    //String archFile = "http://localhost:8080/rgb/test/arch.zip";
    CreateZip zip = new CreateZip();
    try {
      System.out.println("begin");
      //zip.openZip(archFile, new TestHttpServletResponse());
      zip.addFile(inpFile1);
      zip.addFile(inpFile2);
      zip.closeZip();
      System.out.println("end");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void test_zip_file() {
    //  These are the files to include in the ZIP file
    String[] filenames = new String[] {
        "d:\\Program Files\\Apache Group\\Tomcat 4.1\\webapps\\rgb_lib_jsp\\WebRoot\\test\\a.txt",
        "d:\\Program Files\\Apache Group\\Tomcat 4.1\\webapps\\rgb_lib_jsp\\WebRoot\\test\\b.txt" };
    String outFilename = "d:\\Program Files\\Apache Group\\Tomcat 4.1\\webapps\\rgb_lib_jsp\\WebRoot\\test\\arch.zip";
    // Create a buffer for reading the files
    byte[] buf = new byte[1024];

    try {
      // Create the ZIP file

      ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
          outFilename));

      // Compress the files
      for (int i = 0; i < filenames.length; i++) {
        FileInputStream in = new FileInputStream(filenames[i]);

        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(Files.getFileName(filenames[i])));

        // Transfer bytes from the file to the ZIP file
        int len;
        while (true) {
          len = in.read(buf);
          if (len <= 0) 
          {
            break;
          }
          out.write(buf, 0, len);
        }

        // Complete the entry
        out.closeEntry();
        in.close();
      }

      // Complete the ZIP file
      out.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    

  }

}

/*
 public class CreateZip {
 private ArrayList compressedData        = new ArrayList();
 private ArrayList centralDirectory      = new ArrayList();
 private String    endOfCentralDirectory = ""
 + (char) 0x50
 + (char) 0x4b
 + (char) 0x05
 + (char) 0x06
 + (char) 0x00
 + (char) 0x00
 + (char) 0x00
 + (char) 0x00;
 private int       oldOffset             = 0;
 //--------------------------------------------------------
 public void addDirectory(String directoryName) {
 directoryName = StringFunc.replace(directoryName,"\\","/");
 String feedArrayRow = "" + (char)0x50 + (char)0x4b + (char)0x03 + (char)0x04;  
 feedArrayRow += (char)0x0a + (char)0x00;
 feedArrayRow += (char)0x00 + (char)0x00;
 feedArrayRow += (char)0x00 + (char)0x00;
 feedArrayRow += (char)0x00 + (char)0x00 + (char)0x00 + (char)0x00;

 $feedArrayRow .= pack("V",0);
 $feedArrayRow .= pack("V",0);
 $feedArrayRow .= pack("V",0);
 $feedArrayRow .= pack("v", strlen($directoryName) );
 $feedArrayRow .= pack("v", 0 );
 $feedArrayRow .= $directoryName;

 $feedArrayRow .= pack("V",0);
 $feedArrayRow .= pack("V",0);
 $feedArrayRow .= pack("V",0);

 $this -> compressedData[] = $feedArrayRow;

 $newOffset = strlen(implode("", $this->compressedData));

 $addCentralRecord = "\x50\x4b\x01\x02";
 $addCentralRecord .="\x00\x00";
 $addCentralRecord .="\x0a\x00";
 $addCentralRecord .="\x00\x00";
 $addCentralRecord .="\x00\x00";
 $addCentralRecord .="\x00\x00\x00\x00";
 $addCentralRecord .= pack("V",0);
 $addCentralRecord .= pack("V",0);
 $addCentralRecord .= pack("V",0);
 $addCentralRecord .= pack("v", strlen($directoryName) );
 $addCentralRecord .= pack("v", 0 );
 $addCentralRecord .= pack("v", 0 );
 $addCentralRecord .= pack("v", 0 );
 $addCentralRecord .= pack("v", 0 );
 //$ext = "\x00\x00\x10\x00";
 //$ext = "\xff\xff\xff\xff";
 $addCentralRecord .= pack("V", 16 );

 $addCentralRecord .= pack("V", $this -> oldOffset );
 $this -> oldOffset = $newOffset;

 $addCentralRecord .= $directoryName;

 $this -> centralDirectory[] = $addCentralRecord;
 }
 
 }
 */
