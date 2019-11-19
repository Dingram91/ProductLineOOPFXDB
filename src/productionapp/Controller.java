/*
Author: Dylan Ingram
Date: 9/28/19
Description: Controller class for the GUI.
*/

package productionapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * Class that contains the logic for the GUI. This class handles initializing values for some of the
 * gui components as well as managing the connection and interaction with the H2 database.
 *
 * @author Dylan Ingram
 */
public class Controller {

  @FXML
  private TextField taProductName;

  @FXML
  private TextField taManufacturer;

  @FXML
  private ChoiceBox<String> cbItemType;

  // Produce tab components
  @FXML
  private ListView<Product> lvChooseProduct;

  @FXML
  private ComboBox<String> cbbProduceChooseQuantity;

  // Product Line tab
  @FXML
  private TableView<Product> tvProductLine;

  @FXML
  private TableColumn<?, ?> tcProductLineName;

  @FXML
  private TableColumn<?, ?> tcProductLineType;

  @FXML
  private TableColumn<?, ?> tcProductLineManufacture;

  // Production Log components
  @FXML
  private TextArea taProductionLog;

  // Records for use in all tabs
  // Observable list to hold our Products we can make
  ObservableList<Product> productLine;

  // Array list for storing the production log records
  ArrayList<ProductionRecord> productionLog = new ArrayList<>();


  /**
   * Executed once at startup to populate the fields that could not be set in scene builder.
   */
  @FXML
  public void initialize() {
    // set choices for the choicebox in product line
    for (ItemType item : ItemType.values()) {
      cbItemType.getItems().add(String.valueOf(item));
    }

    for (int x = 1; x <= 10; x++) {
      cbbProduceChooseQuantity.getItems().add(String.valueOf(x));
    }
    cbItemType.getSelectionModel().selectFirst(); // set default to first choice
    cbbProduceChooseQuantity.setEditable(true); // allow user to enter a custom value for quantity
    cbbProduceChooseQuantity.getSelectionModel().selectFirst(); // default to first choice

    // Setup the product line products table
    setupProductsLineTable();

    // load products from database
    loadProductList();

    // setup products in Produce tab list view
    setupProduceListView();

    // load the production log
    loadProductionLog();

  }

  void setupProductsLineTable() {
    // create an observable array for the productLine list
    productLine = FXCollections.observableArrayList();

    // setup products table in product line tab
    tcProductLineName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tcProductLineType.setCellValueFactory(new PropertyValueFactory<>("type"));
    tcProductLineManufacture.setCellValueFactory(new PropertyValueFactory<>("manufacture"));

    // bind the "Product Line" products table to products list
    tvProductLine.setItems(productLine);

  }

  void setupProduceListView() {
    lvChooseProduct.setItems(productLine);
  }

  /**
   * Button handler for clicking the Add Product button.
   *
   * @param event Contains information from the event system that triggers this method.
   * @throws SQLException Contains the information for problem encountered in the SQL connection.
   */
  @FXML
  void addProductClicked(ActionEvent event) throws SQLException {
    // get the values from the value fields for a new product
    String type = cbItemType.getValue();
    String manufacturer = taManufacturer.getText();
    String productName = taProductName.getText();

    // create a new product from the Widget class the implements product
    Widget newProduct = new Widget(productName, manufacturer, ItemType.valueOf(type));

    // get a connection to the database
    Connection connection = getdbconnection();

    // create our prepared statement
    String addProductString = "INSERT INTO PUBLIC.PRODUCT(type, manufacturer, name) "
        + "VALUES (?, ?, ?);";
    PreparedStatement addProductStmt = connection.prepareStatement(addProductString);

    // set fields for prepared statement
    addProductStmt.setString(1, cbItemType.getValue());
    addProductStmt.setString(2, newProduct.getManufacture());
    addProductStmt.setString(3, newProduct.getName());

    // execute statement and close statement and connection
    addProductStmt.execute();
    addProductStmt.close();

    // reload the products list
    loadProductList();
  }

  /**
   * Button handler for the Record Product button.
   *
   * @param event Contains information from the event system that triggers this method.
   */
  @FXML
  void recordProductButtonClick(MouseEvent event) {
    System.out.println("Record Product Button Clicked");

    // create a product from the selection in the Products list
    Product producedProduct = lvChooseProduct.getSelectionModel().getSelectedItem();

    // get the value for items produced by parsing the string given by the combobox
    int itemsProduced = Integer.parseInt(cbbProduceChooseQuantity.getSelectionModel()
        .getSelectedItem());

    ArrayList<ProductionRecord> productionRun = new ArrayList<>();
    // create a new production record for each item in the Produced count and append to 
    // production log

    while (itemsProduced > 0) {
      // create a new production record for each item in the Produced count
      ProductionRecord productionRecord = new ProductionRecord(producedProduct, itemsProduced);

      // add product to production run
      productionRun.add(productionRecord);

      // decrement itemsProduced
      itemsProduced--;
    }

    // add the products in production run into the database
    addToProductionDB(productionRun);

    // load the production log
    loadProductionLog();

  }

  void addToProductionDB(ArrayList<ProductionRecord> productionRun) {
    // get connection to the database
    Connection connection = getdbconnection();

    // sql String for inserting into database
    String sqlString = "INSERT into "
        + "PRODUCTIONRECORD(PRODUCT_ID, SERIAL_NUM, DATE_PRODUCED) VALUES ( ?, ?, ? );";

    // create prepared statement
    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString);) {

      // loop through production run entries and insert them into the database
      for (ProductionRecord productionRecord : productionRun) {
        preparedStatement.setInt(1, productionRecord.getProductID());
        preparedStatement.setString(2, productionRecord.getSerialNum());
        preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        preparedStatement.execute();
      }
      preparedStatement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private String getProductNameFromId(int id) {
    String itemName = "";

    Connection connection = getdbconnection();

    String sql = "SELECT NAME from Product where id = ?;";
    try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        itemName = resultSet.getString(1);
      }


    } catch (SQLException e) {
      e.printStackTrace();
    }
    return itemName;
  }

  void loadProductionLog() {

    // clear production log before loading
    productionLog.clear();

    // connect to the database
    Connection connection = getdbconnection();

    // sql query string
    String sqlStatement = "SELECT * from PRODUCTIONRECORD";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);) {
      // create the prepared statement

      // execute statement and get results
      ResultSet resultSet = preparedStatement.executeQuery();

      // loop through results and add to production log array

      while (resultSet.next()) {
        int productNumber = resultSet.getInt("PRODUCTION_NUM");
        int productId = resultSet.getInt("PRODUCT_ID");
        String serialNum = resultSet.getString("SERIAL_NUM");
        Timestamp timestamp = resultSet.getTimestamp("DATE_PRODUCED");

        // create a product record from query result
        ProductionRecord productionRecord = new ProductionRecord(productNumber, productId,
            serialNum, new Date(timestamp.getTime()));

        // add production record to productionLog array
        productionLog.add(productionRecord);
      }

      resultSet.close();


    } catch (SQLException e) {
      e.printStackTrace();
    }

    // show the records
    showProduction();

  }

  private void showProduction() {
    // clear the text area
    taProductionLog.clear();

    // loop through production lgo and add to the text area
    for (ProductionRecord productionRecord : productionLog) {
      String itemName = getProductNameFromId(productionRecord.getProductID());
      taProductionLog.appendText(productionRecord.toStringWithName(itemName));
    }

  }

  void loadProductList() {
    // clear the productLine before loading
    productLine.clear();

    // connect to the database
    Connection connection = getdbconnection();

    // make prepared statement
    String sqlStatement = "SELECT * from PRODUCT";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);) {

      ResultSet resultSet = preparedStatement.executeQuery();

      //loop through resultset and add products to the productLine array
      while (resultSet.next()) {
        // get result fields for item
        int id = resultSet.getInt("ID");
        String name = resultSet.getString("NAME");
        String manufacture = resultSet.getString("MANUFACTURER");
        ItemType itemType = ItemType.valueOf(resultSet.getString("TYPE"));
        // create a product
        Product newProduct = new Widget(id ,name, manufacture, itemType);
        // append product to productLine array
        productLine.add(newProduct);

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }


  }


  /**
   * Gets a connection to the database.
   *
   * @return Connection
   */
  private Connection getdbconnection() {
    // setup variables with database url and driver locations
    final String JdbcDriver = "org.h2.Driver";
    final String DbUrl = "jdbc:h2:.\\res\\ProductionDB";

    // Database credentials
    final String User = "";
    // findbugs error for not setting password, will be set at a later time.
    final String Pass = "";

    Connection conn;

    try {
      //Register JDBC driver
      Class.forName(JdbcDriver);

      //findbugs wants a password to be set but was it was not specified for this project.
      // create connection
      conn = DriverManager.getConnection(DbUrl, User, Pass);

      return conn;

    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException("Could not connect to the database", e);
    }
  }


}


