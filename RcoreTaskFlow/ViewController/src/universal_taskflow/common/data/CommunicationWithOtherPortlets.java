package universal_taskflow.common.data;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс описывающий связь портлета с другими
 */

public class CommunicationWithOtherPortlets implements Serializable
{
  @SuppressWarnings("compatibility:-3344388969982530019")
  private static final long serialVersionUID = 1L;

  private Set<String> portletsIdToRefresh = new HashSet<String>();
  private List<LinkParameter> parameters = new ArrayList<LinkParameter>();
  //=====

  /**
   * @param portletsIdToRefresh
   */
  public void setPortletsIdToRefresh(Set<String> portletsIdToRefresh)
  {
    this.portletsIdToRefresh = portletsIdToRefresh;
  }

  /**
   * @return
   */
  public Set<String> getPortletsIdToRefresh()
  {
    return portletsIdToRefresh;
  }

  /**
   * @param parameters
   */
  public void setParameters(List<LinkParameter> parameters)
  {
    this.parameters = parameters;
  }

  /**
   * @return
   */
  public List<LinkParameter> getParameters()
  {
    return parameters;
  }
}
