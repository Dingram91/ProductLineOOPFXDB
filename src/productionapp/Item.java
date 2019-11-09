package productionapp;

/**
 * interface for items that defines what an item should be able to do.
 */
public interface Item {

  public int getId();

  public void setName(String name);

  public String getName();

  public void setManufacture(String manufacture);

  public String getManufacture();
}
