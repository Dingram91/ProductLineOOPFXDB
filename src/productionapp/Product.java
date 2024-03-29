package productionapp;

/**
 * Abstract product class that defines the fields and methods for producible products.
 *
 * @author Dylan Ingram
 */
public abstract class Product implements Item {

  int id;
  ItemType type;
  String manufacture;
  String name;

  /**
   * Constructor for the Product class.
   *
   * @param name        The name of the product.
   * @param manufacture The name of the product's manufacturer.
   * @param type        The type of the product.
   */
  public Product(String name, String manufacture, ItemType type) {
    this.name = name;
    this.manufacture = manufacture;
    this.type = type;
  }

  /**
   * Overloaded Constructor for the Product class for products that have already been given an ID by
   * the database.
   *
   * @param id          The id assigned by the database to the product.
   * @param name        The name of the product.
   * @param manufacture The name of the product's manufacturer.
   * @param type        The type of the product.
   */
  public Product(int id, String name, String manufacture, ItemType type) {
    this.id = id;
    this.name = name;
    this.manufacture = manufacture;
    this.type = type;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setManufacture(String manufacture) {
    this.manufacture = manufacture;
  }

  @Override
  public String getManufacture() {
    return manufacture;
  }

  public ItemType getType() {
    return type;
  }

  public void setType(ItemType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Name: " + name + "\nManufacture: " + manufacture + "\nType: " + type.getCode() + "\n";
  }
}
