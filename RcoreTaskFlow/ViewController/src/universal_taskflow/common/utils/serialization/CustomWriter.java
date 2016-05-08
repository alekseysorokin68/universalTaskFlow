package universal_taskflow.common.utils.serialization;


import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

public class CustomWriter extends PrettyPrintWriter
{
  public CustomWriter(java.io.Writer out) 
  {
    super(out);
  }
  
  public void startNode(String name, Class clazz)
  {
    super.startNode(name, clazz);
  }

  protected void writeText(QuickWriter writer, String text)
  {
    if (isCDATA(text))
    {
        writer.write("<![CDATA[");
        writer.write(text);
        writer.write("]]>");
    }
    else 
    {
        super.writeText(writer,text);
    }
  }
  //=====================================

  private static boolean isCDATA(String text)
  {
    boolean rc = false;
    if (text != null) 
    {
      int length = text.length();
      for (int i = 0; i < length; i++ ) 
      {
        char c = text.charAt(i);
        if ("\0&<>\"'\r\t\n".indexOf(c) > -1) 
        {
          rc = true;
          break;
        }
      }
    }
    return rc;
  }
}
