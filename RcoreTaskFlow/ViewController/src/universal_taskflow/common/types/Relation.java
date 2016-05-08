package universal_taskflow.common.types;


import com.rcore.global.DefaultCompare;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;


public enum Relation
{
  EQ("=", "равно"),
  NE("!=", "не равно"),
  GT(">", "больше чем"),
  GE(">=", "больше, или равно"),
  LT("<", "меньше чем"),
  LE("<=", "меньше, или равно"),
  IN("Интервал", "Значение входит в интервал");
  
  private static final long serialVersionUID = 1L;

  private String caption;
  private String description;

  private Relation(String caption, String description)
  {
    this.caption = caption;
    this.description = description;
  }

  /**
   * @return
   */
  public static List<SelectItem> getSelectItems()
  {
    List<SelectItem> rc = new ArrayList<SelectItem>();
    for (Relation item: Relation.values())
    {
      rc.add(new SelectItem(item, item.getCaption(),
                            item.getDescription()));
    }
    return rc;
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
  public String getName()
  {
    return name();
  }

  /**
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param resultCompare
   * @return
   */
  public boolean isCompatibleWithIntResultCompare(int resultCompare)
  {
    boolean rc = false;
    if (resultCompare == DefaultCompare.EQ)
    {
      rc = this.equals(EQ);
    }
    else if (resultCompare == DefaultCompare.GT)
    {
      rc = (this.equals(GT) || this.equals(GE));
    }
    else if (resultCompare == DefaultCompare.LT)
    {
      rc = (this.equals(LT) || this.equals(LE));
    }
    else
    {
      System.err.println("isCompatibleWithIntResultCompare - ошибка - " +
                         resultCompare);
    }
    return rc;
  }
}
