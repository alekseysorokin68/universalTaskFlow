package universal_taskflow.common.types;

import javax.faces.model.SelectItem;

/**
 * Стратегия извлечения данных из базы
 */
public enum ReadFromDbType
{
  JUST_READ_ALL("Прочитать все данные сразу","но не более чем"),
  READ_PORTIONS("Читать данные порциями, по мере необходимости","размер порции")
  ;
  private static final long serialVersionUID = 1L;
  private String caption = null;
  private String textForNumber = null;
  private ReadFromDbType(String caption,String textForNumber) 
  {
    this.caption = caption;
    this.textForNumber = textForNumber;
  }
  public static SelectItem[] getSelectItemsForReadFromDbType() 
  { 
    ReadFromDbType[] items = values();
    SelectItem[] rc = new SelectItem[items.length];
    for (int i = 0; i <items.length; i++ ) 
    {
      ReadFromDbType item = items[i];
      rc[i] = new SelectItem(item,item.caption, item.caption);
    }
    return rc;
  }

  public String getCaption()
  {
    return caption;
  }

  public String getTextForNumber()
  {
    return textForNumber;
  }
}
