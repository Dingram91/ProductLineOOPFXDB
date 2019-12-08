package productionapp;

import java.util.Date;
import java.util.HashMap;

/**
 * Production record that represents a specific product that was produced.
 *
 * @author Dylan Ingram
 */
public class ProductionRecord {

  private int productionNum;
  private int productID;
  private String serialNum;
  private Date prodDate;
  private static HashMap<String, Integer> itemTypeCounts = initializeItemCounts();


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
   * Constructor for production record class. This method also increments the itemTypeCounts so that
   * proper serial numbers can be assigned to future products.
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

    String itemTypeCode = serialNum.substring(3, 5);
    itemTypeCounts.put(itemTypeCode, itemTypeCounts.get(itemTypeCode) + 1);
  }

  /**
   * Constructor for the production record class. This method also increments the itemTypeCounts so
   * that proper serial numbers can be assigned to future products
   *
   * @param productProduced The product class for the record.
   */
  public ProductionRecord(Product productProduced) {
    // call other constructor to initialize other fields
    this(productProduced.getId());

    // increment the item counts so the proper code is assigned
    String itemTypeCode = productProduced.getType().getCode();
    itemTypeCounts.put(itemTypeCode, itemTypeCounts.get(itemTypeCode) + 1);

    // add leading zeros for the itemType count
    String itemCodeWithLeadingZeros = String.format("%05d", itemTypeCounts.get(itemTypeCode));
    // set the serial number with generated unique code
    this.serialNum = productProduced.getManufacture().substring(0, 3)
        + productProduced.type.getCode() + itemCodeWithLeadingZeros;

  }

  /**
   * This method initalizes the item counts for each product type for use in generating appropriate
   * serial numbers.
   */
  private static HashMap<String, Integer> initializeItemCounts() {
    HashMap<String, Integer> itemTypeCounts = new HashMap<>();
    itemTypeCounts.put("AU", 0);
    itemTypeCounts.put("VI", 0);
    itemTypeCounts.put("AM", 0);
    itemTypeCounts.put("VM", 0);
    return itemTypeCounts;
  }

  /**
   * This method resets the counts for each item type so that proper serial numbers can be
   * generated.
   */
  public static void resetItemCounts() {
    itemTypeCounts.put("AU", 0);
    itemTypeCounts.put("VI", 0);
    itemTypeCounts.put("AM", 0);
    itemTypeCounts.put("VM", 0);
  }


  /**
   * Gets the product ID for the production record.
   *
   * @return int containing production id.
   */
  public int getProductID() {
    return productID;
  }


  /**
   * Gets the serial number for the production record.
   *
   * @return String containing the serial number for the product record.
   */
  public String getSerialNum() {
    return serialNum;
  }

  /**
   * outputs a string containing the production record information.
   *
   * @return string containing the production record information.
   */
  @Override
  public String toString() {
    return "Prod. Num: " + productionNum + " Product ID: " + productID + " Serial Num: "
        + serialNum + " Date: " + prodDate + "\n";
  }

  /**
   * Outputs a string containing the production record information with the product number replaced
   * with the product name.
   *
   * @param name string containing the name of the product the record is for.
   * @return string containing the production record information with the the product name.
   */
  public String toStringWithName(String name) {
    return "Prod. Name: " + name + " Product ID: " + productID + " Serial Num: "
        + serialNum + " Date: " + prodDate + "\n";
  }
}

