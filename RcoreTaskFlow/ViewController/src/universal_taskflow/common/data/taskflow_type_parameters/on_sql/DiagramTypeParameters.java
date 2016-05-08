package universal_taskflow.common.data.taskflow_type_parameters.on_sql;


import com.rcore.global.jsf.JSFUtils;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import universal_taskflow.common.data.AttributeColor;
import universal_taskflow.common.types.GraphStyle;
import universal_taskflow.common.types.HorizontalAlign;
import universal_taskflow.common.types.ImageFormat;
import universal_taskflow.common.types.VerticalAlign;


public class DiagramTypeParameters extends TaskFlowParametersBaseOnSql
{
  private static final long serialVersionUID = 1L;
  public DiagramTypeParameters()
  {
    super();
  }
  //----------------------------------------------
  // Вторая зеркальная шкала
  private Boolean secondMirrorScale = true;

  // Наличие слайдера масштабирования
  private Boolean scalingsSlaider = false;

  // Количество диапазонов
  private Integer quantityRanges = 5;

  //maxY максимальное значение шкалы Y
  //если задано, то берется отсюда, если не задано то вычисляется по данным
  private Double maxY = null;

  //minY максимальное значение шкалы Y
  //если задано, то берется отсюда, если не задано то вычисляется по данным
  private Double minY = null;


  //Размер интервала. Указывается либо количество диапазонов либо размер интервала
  //если указано и то и то, то преоритет за размером интервала
  private Double majorIncrement = null;

  private Boolean effect3d = false;

  private String width = null;
  private String height = null;

  // Всплывающее описание
  private String shortDesc = null;

  // Цвет - заполнитель
  private Color colorFiller = Color.WHITE;

  // Заголовок по оси Х
  private String titleX = null;

  // Заголовок по оси Y
  private String titleY = null;

  // Расположение заголовка по оси Х
  private HorizontalAlign titleXAlign = HorizontalAlign.CENTER;

  // Расположение заголовка по оси Y
  private VerticalAlign titleYAlign = VerticalAlign.MIDDLE;

  // Цвета полей
  private List<AttributeColor> attributesColor =  new ArrayList<AttributeColor>();

  // Наличие стандартной легенды
  private Boolean standartLegendExists = false;

  //толщина линии графика
  private Integer lineWidth = 1;

  //стиль графика, (есть возможность переопределить в UserData ?)
  private GraphStyle graphStyle = GraphStyle.APRIL;

  private ImageFormat imageFormat = ImageFormat.AUTO;

  //открывается по двойному клику в текущем окне или в новом
  private Boolean isOpenImageInCurrentWindow = true;
  //========================================================

  /**
   * @param graphStyle
   */
  public void setGraphStyle(GraphStyle graphStyle)
  {
    this.graphStyle = graphStyle;
  }

  /**
   * @return
   */
  public GraphStyle getGraphStyle()
  {
    return graphStyle;
  }

  /**
   * @param secondMirrorScale
   */
  public void setSecondMirrorScale(Boolean secondMirrorScale)
  {
    this.secondMirrorScale = secondMirrorScale;
  }

  /**
   * @return
   */
  public Boolean getSecondMirrorScale()
  {
    return secondMirrorScale;
  }

  /**
   * @param scalingsSlaider
   */
  public void setScalingsSlaider(Boolean scalingsSlaider)
  {
    this.scalingsSlaider = scalingsSlaider;
  }

  public Boolean getScalingsSlaider()
  {
    return scalingsSlaider;
  }

  public void setQuantityRanges(Integer quantityRanges)
  {
    this.quantityRanges = quantityRanges;
  }

  /**
   * @return
   */
  public Integer getQuantityRanges()
  {
    return quantityRanges;
  }

  /**
   * @param effect3d
   */
  public void setEffect3d(Boolean effect3d)
  {
    this.effect3d = effect3d;
  }

  /**
   * @return
   */
  public Boolean getEffect3d()
  {
    return effect3d;
  }

  /**
   * @param width
   */
  public void setWidth(String width)
  {
    this.width = width;
  }

  /**
   * @return width
   */
  public String getWidth()
  {
    return width;
  }
  
  public String getResolvedWidth()
  {
    if (width == null || width.trim().length() == 0) {
      return width;
    }
    if (!width.startsWith("#{")) 
    {
      return width;
    }
    return (String)(JSFUtils.resolveExpression(width));
  }

  /**
   * @param height
   */
  public void setHeight(String height)
  {
    this.height = height;
  }

  /**
   * @return
   */
  public String getHeight()
  {
    return height;
  }
  
  public String getResolvedHeight()
  {
    if (height == null || height.trim().length() == 0) {
      return height;
    }
    if (!height.startsWith("#{")) 
    {
      return height;
    }
    return (String)(JSFUtils.resolveExpression(height));
  }

  /**
   * @param shortDesc
   */
  public void setShortDesc(String shortDesc)
  {
    this.shortDesc = shortDesc;
  }

  /**
   * @return
   */
  public String getShortDesc()
  {
    return shortDesc;
  }

  /**
   * @param colorFiller
   */
  public void setColorFiller(Color colorFiller)
  {
    this.colorFiller = colorFiller;
  }

  /**
   * @return
   */
  public Color getColorFiller()
  {
    return colorFiller;
  }

  /**
   * @return
   */
  public Color getDefaultColorFiller()
  {
    return Color.WHITE;
  }

  /**
   * @param titleX
   */
  public void setTitleX(String titleX)
  {
    this.titleX = titleX;
  }

  /**
   * @return
   */
  public String getTitleX()
  {
    return titleX;
  }

  /**
   * @param titleY
   */
  public void setTitleY(String titleY)
  {
    this.titleY = titleY;
  }

  /**
   * @return
   */
  public String getTitleY()
  {
    return titleY;
  }

  /**
   * @param titleXAlign
   */
  public void setTitleXAlign(HorizontalAlign titleXAlign)
  {
    this.titleXAlign = titleXAlign;
  }

  /**
   * @return
   */
  public HorizontalAlign getTitleXAlign()
  {
    return titleXAlign;
  }

  /**
   * @param titleYAlign
   */
  public void setTitleYAlign(VerticalAlign titleYAlign)
  {
    this.titleYAlign = titleYAlign;
  }

  /**
   * @return
   */
  public VerticalAlign getTitleYAlign()
  {
    return titleYAlign;
  }

  /**
   * @param attributesColor
   */
  public void setAttributesColor(List<AttributeColor> attributesColor)
  {
    this.attributesColor = attributesColor;
  }

  /**
   * @return
   */
  public List<AttributeColor> getAttributesColor()
  {
    return attributesColor;
  }

  /**
   * @param standartLegendExists
   */
  public void setStandartLegendExists(Boolean standartLegendExists)
  {
    this.standartLegendExists = standartLegendExists;
  }

  /**
   * @return
   */
  public Boolean getStandartLegendExists()
  {
    return standartLegendExists;
  }
  
   
  /**
   * @param majorIncrement
   */
  public void setMajorIncrement(Double majorIncrement)
  {
    this.majorIncrement = majorIncrement;
  }

  /**
   * @return
   */
  public Double getMajorIncrement()
  {
    return majorIncrement;
  }

  /**
   * @param lineWidth
   */
  public void setLineWidth(Integer lineWidth)
  {
    this.lineWidth = lineWidth;
  }

  /**
   * @return
   */
  public Integer getLineWidth()
  {
    return lineWidth;
  }

  /**
   * @param maxY
   */
  public void setMaxY(Double maxY)
  {
    this.maxY = maxY;
  }

  /**
   * @return
   */
  public Double getMaxY()
  {
    return maxY;
  }

  /**
   * @param minY
   */
  public void setMinY(Double minY)
  {
    this.minY = minY;
  }

  /**
   * @return
   */
  public Double getMinY()
  {
    return minY;
  }

  /**
   * @param imageFormat
   */
  public void setImageFormat(ImageFormat imageFormat)
  {
    if (imageFormat == null)
    {
      imageFormat = ImageFormat.AUTO;
    }
    this.imageFormat = imageFormat;
  }
  
  public List<SelectItem> getImageFormatSelectItems()
  {
    return ImageFormat.getImageFormatSelectItems();
  }


  /**
   * @return
   */
  public ImageFormat getImageFormat()
  {
    if (imageFormat == null)
    {
      imageFormat = ImageFormat.AUTO;
    }
    return imageFormat;
  }

  /**
   * @param isOpenImageInCurrentWindow
   */
  public void setIsOpenImageInCurrentWindow(Boolean isOpenImageInCurrentWindow)
  {
    if (isOpenImageInCurrentWindow == null)
      isOpenImageInCurrentWindow = true;
    this.isOpenImageInCurrentWindow = isOpenImageInCurrentWindow;
  }

  /**
   * @return
   */
  public Boolean getIsOpenImageInCurrentWindow()
  {
    if (isOpenImageInCurrentWindow == null)
      isOpenImageInCurrentWindow = true;
    return isOpenImageInCurrentWindow;
  }

  public List<SelectItem> getVerticalAlignSelectItems()
  {
    return VerticalAlign.getSelectItems();
  }
  
  public List<SelectItem> getGraphStyleSelectItems()
   {
     return GraphStyle.getSelectItems();
   }


}
