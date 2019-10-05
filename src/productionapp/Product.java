package productionapp;

public abstract class Product implements Item {
  int id;
  String type;
  String manufacture;
  String name;

  // constructor
  public Product(String name, String manufacture, String  type){
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
    this.manufacture =  manufacture;
  }

  @Override
  public String getManufacture() {
    return manufacture;
  }

  @Override
  public String toString(){
    return "Name: " + name + "\nManufacture: " + manufacture + "\nType: " + type + "\n";
  }
}
