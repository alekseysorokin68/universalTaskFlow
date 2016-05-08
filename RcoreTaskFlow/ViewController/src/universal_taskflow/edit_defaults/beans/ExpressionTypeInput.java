package universal_taskflow.edit_defaults.beans;

public enum ExpressionTypeInput
{
  EL,
  HTML,
  LITERAL;
  private static final long serialVersionUID = 1L;
  private String elInputText = null;
  private String htmlInputText = null;

  public String getName()
  {
    return name();
  }

  public void setElInputText(String elInputText)
  {
    this.elInputText = elInputText;
  }

  public String getElInputText()
  {
    return elInputText;
  }

  public void setHtmlInputText(String htmlInputText)
  {
    this.htmlInputText = htmlInputText;
  }

  public String getHtmlInputText()
  {
    return htmlInputText;
  }
}
