package test.universal_taskflow.common.utils.data;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;

import java.util.Map;

public class TestEvents implements Serializable
{
  @SuppressWarnings("compatibility:-3017110112704338717")
  private static final long serialVersionUID = 1L;
  
  private String s_dymamic_attr = null;
  
  private void xstreamBeforeMarshall(HierarchicalStreamWriter writer) 
  {
    writer.addAttribute("dymamic_attr", "1");
  }
  private void xstreamAfterUnMarshall(Map<String,String> attributes) 
  {
    s_dymamic_attr = attributes.get("dymamic_attr");
  }

  public String getDymamic_attr()
  {
    return s_dymamic_attr;
  }
}
