package test.universal_taskflow.common.utils.data;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Map;

import universal_taskflow.common.utils.serialization.XStreamFacade;

public enum TestEnum
{
  A,
  B
  ;
  private static final long serialVersionUID = 1L;
  private Integer f1 = 1;
  
  //------------------
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer)
  {
//    writer.startNode("f1");
//    writer.setValue(f1.toString());
//    writer.endNode();
     //writer.addAttribute("f1", f1.toString()); 
     //f1 = XStreamFacade.toXML(f1);
    try
    {
      writer.addAttribute("f1",XStreamFacade.toXML(f1));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  private void xstreamAfterUnMarshall(Map<String, String> attributes)
  {
    //f1 = Integer.parseInt(attributes.get("f1"));
    try
    {
      f1 = (Integer) XStreamFacade.fromStringXML(attributes.get("f1"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  //------------------ 


  public void setF1(Integer f1)
  {
    this.f1 = f1;
  }

  public Integer getF1()
  {
    return f1;
  }
}
