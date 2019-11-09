package productionapp;

/**
 * Item type enum that represents the different categories of products that can be produced.
 */
public enum ItemType {
  AUDIO("AU"),
  VISUAL("VI"),
  AUDIOMOBILE("AM"),
  VISUALMOBILE("VM");

  private String code;

  // Constructor
  ItemType(String selection) {
    this.code = selection;
  }

  public String getCode() {
    return code;
  }
}

