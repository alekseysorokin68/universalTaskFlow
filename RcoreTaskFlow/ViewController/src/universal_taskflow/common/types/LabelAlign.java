package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;


/*
 * Положение метки поля на форме
 */
public enum LabelAlign
{
  LEFT("Слева"),
  TOP("Сверху");
  private static final long serialVersionUID = 1L;

  private String caption = null;

  private LabelAlign(String caption)
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
    for (LabelAlign value: LabelAlign.values())
    {
      rc.add(new SelectItem(value, value.getCaption(),
                            value.getCaption()));
    }
    return rc;
  }
}
