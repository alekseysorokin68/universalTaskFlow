package universal_taskflow.common.types;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;


/**
 * Формат отображения поля.
 */
public enum DisplayFormat
{
  STRING("Строка"),
  NUMBER("Число"),
  CURRENCY("Деньги"),
  DATE("Дата");
  private static final long serialVersionUID = 1L;

  private String caption = null;

  private DisplayFormat(String caption)
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
  public List<SelectItem> getSelectItems()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (DisplayFormat item: values())
    {
      rc.add(new SelectItem(item, item.caption, item.caption));
    }
    return rc;
  }
}
