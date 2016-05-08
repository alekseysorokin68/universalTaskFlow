package universal_taskflow.common.types;


import com.rcore.global.jsf.JSFUtils;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@XStreamAlias("DataChangeType")
public enum DataChangeType
{
  INSERT,
  DELETE,
  UPDATE
  ;
  private static final long serialVersionUID = 1L;
  //------------------------------------------------
}
