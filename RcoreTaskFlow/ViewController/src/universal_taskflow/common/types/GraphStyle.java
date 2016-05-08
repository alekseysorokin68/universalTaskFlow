package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;


/**
 * Перечисление стилей графиков.
 */
public enum GraphStyle
{
  APRIL("April"),
  AUTUMN("Autumn"),
  BLACK_AND_WHITE("Black and White"),
  COMET("Comet"),
  CONFETTI("Confetti"),
  DEFAULT("Default"),
  EARTH("Earth"),
  EXECUTIVE("Executive"),
  FINANCIAL("Financial"),
  GLASS("Glass"),
  NAUTICAL("Nautical"),
  PROJECTION("Projection"),
  REGATTA("Regatta"),
  SOUTHWEST("Southwest"),
  TRANSPARENT("Transparent");
  private static final long serialVersionUID = 1L;

  private String caption = null;

  private GraphStyle(String caption)
  {
    this.caption = caption;
  }

  /**
   * @return
   */
  public String getCaption()
  {
    return caption;
  }

  /**
   * @return
   */
  public static List<SelectItem> getSelectItems()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (GraphStyle item: GraphStyle.values())
    {
      rc.add(new SelectItem(item, item.getCaption(), item.getCaption()));
    }
    return rc;
  }
}
