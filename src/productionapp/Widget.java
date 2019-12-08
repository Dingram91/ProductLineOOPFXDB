package productionapp;

/**
 * Implementation of the abstract product class.
 *
 * @author Dylan Ingram
 */
public class Widget extends Product {

  public Widget(int id, String name, String manufacture, ItemType type) {
    super(id, name, manufacture, type);
  }

  public Widget(String name, String manufacture, ItemType type) {
    super(name, manufacture, type);
  }
}
