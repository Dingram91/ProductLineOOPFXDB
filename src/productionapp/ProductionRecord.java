package productionapp;

import java.util.Date;

/**
 * Production record that represents a specific product that was produced.
 */
public class ProductionRecord {

  private int productionNum;
  private int productID;
  private String serialNum;
  private Date prodDate;

  /**
   * Constructor for production record class.
   *
   * @param productID The product id for the product.
   */
  public ProductionRecord(int productID) {
    this.productID = productID;
    this.productionNum = 0;
    this.serialNum = "0";
    this.prodDate = new Date();
  }

  /**
   * Constructor for production record class.
   *
   * @param productionNum The Production number.
   * @param productID     The ID for the product.
   * @param serialNum     The serial number for the product.
   * @param prodDate      The date the product was produced.
   */
  public ProductionRecord(int productionNum, int productID, String serialNum,
      Date prodDate) {
    this.productionNum = productionNum;
    this.productID = productID;
    this.serialNum = serialNum;
    this.prodDate = new Date(prodDate.getTime());
  }

  /**
   * Constructor for the production record class.
   *
   * @param productProduced The product class for the record.
   * @param itemsProduced   The number of copys of the item that were produced.
   */
  public ProductionRecord(Product productProduced, int itemsProduced) {
    // call other constructor to initialize other fields
    this(productProduced.getId());

    // add leading zeros for the itemType count
    String itemCodeWithLeadingZeros = String.format("%05d", itemsProduced);
    // set the serial number with generated unique code
    this.serialNum = productProduced.getManufacture().substring(0, 3)
        + productProduced.type.getCode() + itemCodeWithLeadingZeros;
  }

  public int getProductionNum() {
    return productionNum;
  }

  public void setProductionNum(int productionNum) {
    this.productionNum = productionNum;
  }

  public int getProductID() {
    return productID;
  }

  public void setProductID(int productID) {
    this.productID = productID;
  }

  public String getSerialNum() {
    return serialNum;
  }

  public void setSerialNum(String serialNum) {
    this.serialNum = serialNum;
  }

  public Date getProdDate() {
    return new Date(prodDate.getTime());
  }

  public void setProdDate(Date prodDate) {
    this.prodDate = new Date(prodDate.getTime());
  }


  @Override
  public String toString() {
    return "Prod. Num: " + productionNum + " Product ID: " + productID + " Serial Num: "
        + serialNum + " Date: " + prodDate + "\n";
  }

  public String toStringWithName(String name) {
    return "Prod. Name: " + name + " Product ID: " + productID + " Serial Num: "
        + serialNum + " Date: " + prodDate + "\n";
  }
}

