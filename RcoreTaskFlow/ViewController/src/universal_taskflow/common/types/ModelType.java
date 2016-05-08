package universal_taskflow.common.types;

/**
 * Класс описывающий модель данных taskflow.
 */
public enum ModelType
{
  READ_ONLY,
  READ_WRITE;
  private static final long serialVersionUID = 1L;

  /**
   * @return
   */
  public String getName()
  {
    return name();
  }
}
