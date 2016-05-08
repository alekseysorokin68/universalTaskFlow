package test.universal_taskflow.common.utils.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("ClassForSerialization")
public class ClassForSerialization implements Serializable
{
  @SuppressWarnings("compatibility:-2114544317242355865")
  private static final long serialVersionUID = 2L;
  private int simple = 1;
  @XStreamAsAttribute
  private String attr = "attr";
  private String rus = "Цапля";
  private TestEnum testEnum = TestEnum.TWO;
  private List<Serializable> list = new ArrayList<Serializable>();
  private ClassForSerialization ts = null;

  public ClassForSerialization()
  {
    super();
    list.add(1);
    list.add(2);
  }

  public List<Serializable> getList()
  {
    return list;
  }

  public void setTs(ClassForSerialization ts)
  {
    this.ts = ts;
  }

  public ClassForSerialization getTs()
  {
    return ts;
  }
  //-------------------------
  public static enum TestEnum 
  {
    ONE,
    TWO;
    @SuppressWarnings("compatibility:-3578421129271339676")
    private static final long serialVersionUID = 1L;
  }
}
