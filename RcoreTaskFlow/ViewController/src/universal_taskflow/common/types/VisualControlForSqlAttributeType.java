package universal_taskflow.common.types;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.HashMap;
import java.util.Map;


/**
 * Элементы управления для редактирования поля запроса.
 */
@XStreamAlias("VisualControlForSqlAttributeType")
public enum VisualControlForSqlAttributeType
{
   CHECK_BOX("Чекбокс (selectBooleanCheckbox)",
             "Элемент управления, позволяющий сделать выбор из двух возможностей. Недопустимо выбирать этот элемент для таблицы и формы одновременно.",
             new ValidatorType[]{},
             new ConvertorType[]{}
             ),

   DATE("Дата",
        "Элемент управления для ввода даты с помощью календаря",
        new ValidatorType[]{
                            ValidatorType.validateDateTimeRange,
                            ValidatorType.validateDateRestriction
                           },
        new ConvertorType[]{
                            ConvertorType.convertDateTime
                           }
        ), //*

   INPUT("Поле для отображения и ввода (inputText)",
         "Простое поле для отображения и ввода, если ввод запрещен - поле поле только для отображения",
         new ValidatorType[]{
                              ValidatorType.validateLength,
                              ValidatorType.validateDoubleRange
                            },
         new ConvertorType[]{
                              ConvertorType.convertNumber
                            }
         ), //*
   
   INPUT_NUMBER("Поле для ввода чисел (inputNumberSpinbox)",
                "Поле для ввода чисел с кнопками 'Увеличить', 'Уменьшить'",
          new ValidatorType[]{
                       ValidatorType.validateDoubleRange
                     },
          new ConvertorType[]{
                       ConvertorType.convertNumber
                     }
          ),
   IMAGE("Рисунок (image)",
         "Элемент управления для отображения изображений",
         new ValidatorType[]{},
         new ConvertorType[]{}
    ),
   
   LINK("Гиперссылка (goLink)",
        "Элемент управления для ссылки на другую страницу и обновления (при этом) других портлетов имеет свой интерфейс",
        new ValidatorType[]{},
        new ConvertorType[]{}
    ), //-
   
   DROP_DOWN_LIST("Выпадающий список",
                  "Элемент управления, позволяющий выбрать одно значение из нескольких (из выпадающего списка)",
        new ValidatorType[]{},
        new ConvertorType[]{}          
    ),
   
   OUTPUT_TEXT("Не редактируемый текст (outputText)",
               "Простой не редактируемый текст",
              new ValidatorType[]{},
              new ConvertorType[]{
                    ConvertorType.convertDateTime,
                    ConvertorType.convertNumber
                } 
    ), //*
   
   OUTPUT_LABEL("Не редактируемая метка (outputLabel)",
                "Простой не редактируемый текст, по стилю несколько отличный от outputText",
                new ValidatorType[]{},
                new ConvertorType[]{
                    ConvertorType.convertDateTime,
                    ConvertorType.convertNumber
                } 
        ), //*
   
   OUTPUT_FORMATTED("Не редактируемый HTML-текст (outputText,escape=false)",
                    "Разметка текста в формате HTML. Элемент управления показывает результат обработки текста",
                    new ValidatorType[]{},
                    new ConvertorType[]{}                     
          )
  //,LINK_TO_VIEW_FORM("Ссылка на просмотр текущей записи в виде формы","По этой ссылке текущая запись таблицы будет показана в виде формы. Допустимо только для 'Таблица + Форма' и для 'ADF таблица без бизнес - компонент'")
  ;
            
  private static final long serialVersionUID = 1L;
  private static final Map<String, String> captionByName;
  private static final Map<String, String> descriptionByName;
  
  static
  {
    captionByName = new HashMap<String, String>();
    for (VisualControlForSqlAttributeType item: values())
    {
      captionByName.put(item.name(), item.getCaption());
    } // for

    descriptionByName = new HashMap<String, String>();
    for (VisualControlForSqlAttributeType item: values())
    {
      descriptionByName.put(item.name(), item.getDescription());
    } // for
  }


  private String caption = null;
  private String description = null;
  private ValidatorType[] validators = null;
  private ConvertorType[] convertors = null;

  private VisualControlForSqlAttributeType(
        String caption, 
        String description,
        ValidatorType[] validators, // Возможные валидаторы
        ConvertorType[] convertors  // Возможные коверторы
                                          )
  {
    this.caption     = caption;
    this.description = description;
    this.validators  = validators;
    this.convertors  = convertors;
  }

  public String getCaption()
  {
    return caption;
  }

  public String getDescription()
  {
    return description;
  }

  public static Map<String, String> getCaptionByName()
  {
    return captionByName;
  }
  public static Map<String, String> getDescriptionByName()
  {
    return descriptionByName;
  }

  public String getName()
  {
    return name();
  }

  public ValidatorType[] getValidators()
  {
    return validators;
  }

  public ConvertorType[] getConvertors()
  {
    return convertors;
  }
}
