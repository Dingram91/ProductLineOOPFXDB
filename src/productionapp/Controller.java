/*
Author: Dylan Ingram
Date: 9/28/19
Description: Controller class for the GUI.
*/

package productionapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
  private Label lblProduceErrorMessage;

  @FXML
  private Label lblQuantityError;

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

  @FXML
  private Label lblProductNameErrorMessage;

  @FXML
  private Label lblManufactureErrorMessage;

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

    // hide the error messages for fields by default
    lblManufactureErrorMessage.setVisible(false);
    lblProductNameErrorMessage.setVisible(false);
    lblProduceErrorMessage.setVisible(false);
    lblQuantityError.setVisible(false);

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

  /**
   * This method sets up a list of products for our producible products and associates the table in
   * the Product Line table with this list.
   */
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

  /**
   * This method adds them items from the product line to the produce tab so that they can be used
   * to track production.
   */
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

    // check for errors in any of the fields before executing database update
    if (!checkProductLineTabErrors()) {
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
  }

  /**
   * This method validates the fields in the product line and displays errors to the user.
   *
   * @return returns true if an error is encountered in one or more entry fields
   */
  private boolean checkProductLineTabErrors() {
    // This string will store our error message to display to users if one of the fields is
    // incorrect.
    String productNameErrorMessage = "";
    String manufactureErrorMessage = "";
    if (taProductName.getText().isEmpty()) {
      taProductName.setStyle("-fx-background-color: orangered");
      productNameErrorMessage = "Product name is required.";
    } else {
      taProductName.setStyle("-fx-background-color: white");
    }
    if (taManufacturer.getText().isEmpty()) {
      taManufacturer.setStyle("-fx-background-color: orangered");
      manufactureErrorMessage = "Manufacture name is required.";
    } else {
      taManufacturer.setStyle("-fx-background-color: white");
    }

    // check for an error message and display it if one is found, else hide it
    if (productNameErrorMessage.isEmpty()) {
      lblProductNameErrorMessage.setVisible(false);
    } else {
      lblProductNameErrorMessage.setText(productNameErrorMessage);
      lblProductNameErrorMessage.setVisible(true);
    }
    if (manufactureErrorMessage.isEmpty()) {
      lblManufactureErrorMessage.setVisible(false);
    } else {
      lblManufactureErrorMessage.setText(manufactureErrorMessage);
      lblManufactureErrorMessage.setVisible(true);
    }

    // return false if no errors were found
    if (productNameErrorMessage.isEmpty() && manufactureErrorMessage.isEmpty()) {
      return false;
    } else {
      return true;
    }

  }

  /**
   * This method validates the fields on a specified page to prevent errors.
   *
   * @return returns true if an error is encountered in one or more entry fields
   */
  private boolean checkProduceTabErrors() {
    // String to display to user if an error is found
    String chooseProductErrorMessage = "";
    String chooseQuantityErrorMessage = "";

    // regex for checking that quantity is a positive int
    String regex = "^(?!0+$)\\d+$";
    String quantityChoice = cbbProduceChooseQuantity.getValue();
    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(quantityChoice);
    if (!matcher.find()) {
      cbbProduceChooseQuantity.setStyle("-fx-background-color: orangered");
      chooseQuantityErrorMessage = "Quantity must be a positive integer";
    } else {
      cbbProduceChooseQuantity.setStyle("-fx-background-color: white");
    }

    // check that a Product has been selected
    if (lvChooseProduct.getSelectionModel().getSelectedItem() == null) {
      lvChooseProduct.setStyle("-fx-background-color: orangered");
      chooseProductErrorMessage = "Must choose a product to produce.";
    } else {
      lvChooseProduct.setStyle("-fx-background-color: white");
    }

    // display error messages
    if (!chooseProductErrorMessage.isEmpty()) {
      lblProduceErrorMessage.setText(chooseProductErrorMessage);
      lblProduceErrorMessage.setVisible(true);
    } else {
      lblProduceErrorMessage.setVisible(false);
    }
    if (!chooseQuantityErrorMessage.isEmpty()) {
      lblQuantityError.setText(chooseQuantityErrorMessage);
      lblQuantityError.setVisible(true);
    } else {
      lblQuantityError.setVisible(false);
    }

    // return true is an error was found
    if (chooseProductErrorMessage.isEmpty() && chooseQuantityErrorMessage.isEmpty()) {
      return false;
    } else {
      return true;
    }

  }

  /**
   * Button handler for the Record Product button.
   *
   * @param event Contains information from the event system that triggers this method.
   */
  @FXML
  void recordProductButtonClick(MouseEvent event) {
    // check for errors before accessing database
    if (!checkProduceTabErrors()) {
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
        ProductionRecord productionRecord = new ProductionRecord(producedProduct);

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
  }

  /**
   * This method loops through a list of Product Records for items that have been produced and adds
   * them to the ProductionRecord table in our database.
   *
   * @param productionRun ArrayList of ProductionRecords
   */
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
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

  /**
   * This method loads the ProductionRecords from the database and appends them to our list of
   * production records and calls another method to display them.
   */
  void loadProductionLog() {

    // clear production log before loading
    productionLog.clear();

    // reset the class variable items type counts in the ProductionRecords class so proper item
    // serial numbers are maintained
    ProductionRecord.resetItemCounts();

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

  /**
   * This method loops through our productionLog containing all of our ProductionRecords and
   * displays them in the Production Log tab.
   */
  private void showProduction() {
    // clear the text area
    taProductionLog.clear();

    // loop through production lgo and add to the text area
    for (ProductionRecord productionRecord : productionLog) {
      String itemName = getProductNameFromId(productionRecord.getProductID());
      taProductionLog.appendText(productionRecord.toStringWithName(itemName));
    }
  }

  /**
   * This method loads the products from our database and adds them to our productLine list.
   */
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
        Product newProduct = new Widget(id, name, manufacture, itemType);
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

    // Non hardcoded password is fetched from the properties file
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("res/properties"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // reverse the order
    final String Pass = reverseString(prop.getProperty("password"));

    Connection conn;

    try {
      //Register JDBC driver
      Class.forName(JdbcDriver);

      // create connection
      conn = DriverManager.getConnection(DbUrl, User, Pass);

      return conn;

    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException("Could not connect to the database", e);
    }
  }

  /**
   * This is a recursive method used to reverse the string containing the database password.
   *
   * @param pw The clear text password that needs to be reversed.
   * @return The parameter string in reverse order.
   */
  public String reverseString(String pw) {
    if (pw.length() == 1) {
      return pw;
    } else {
      return reverseString(pw.substring(1, pw.length())) + pw.charAt(0);
    }
  }

}
