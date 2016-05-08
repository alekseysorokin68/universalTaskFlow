package test.universal_taskflow.common.utils.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("ClassForSerializableAliasAnotation")
public class ClassForSerializableAliasAnotation implements Serializable
{
  @SuppressWarnings("compatibility:-5856055105420939052")
  private static final long serialVersionUID = 1L;

  public ClassForSerializableAliasAnotation()
  {
    super();
  }
}
