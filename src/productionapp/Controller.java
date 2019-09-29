package productionapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Class that contains the logic for the GUI. This class handles initializing values for some of
 * the gui components as well as managing the connection and interaction with the H2 database.
 * @author Dylan Ingram
 */
public class Controller {

  private static final String JDBCDRIVER = "org.h2.Driver";
  private static final String DBURL = "jdbc:h2:.\\res\\ProductionDB";
  private Connection conn = null; // blank connection
  private PreparedStatement addProductStmt = null;

  /**
   * Constructor for the controller class.
   * @throws SQLException Used for handling exceptions that could be thrown from interactions with
   *                      the H2 database.
   */
  public Controller() throws SQLException {
    initializeDB();
    String addProductString = "INSERT INTO Product(type, manufacturer, name) VALUES "
        + "(?, ?, ?);";
  }

  @FXML
  private TextField taProductName;

  @FXML
  private TextField taManufacturer;

  @FXML
  private ChoiceBox<String> cbItemType;

  @FXML
  private ComboBox<Integer> cbbProduceChooseQuantity;

  /**
   * Executed once at startup to populate the fields that could not be set in scenebuilder.
   * @returns none
   */
  @FXML
  public void initialize() {
    cbItemType.getItems().addAll("Audio", "Paper", "Toy");
    for (int x = 1; x <= 10; x++) {
      cbbProduceChooseQuantity.getItems().addAll(x);
    }
    cbItemType.getSelectionModel().selectFirst(); // set default to first choice
    cbbProduceChooseQuantity.setEditable(true); // allow user to enter a custom value for quantity
    cbbProduceChooseQuantity.getSelectionModel().selectFirst(); // default to first choice
  }

  /**
   * Button handler for clicking the Add Product button.
   * @param event Contains information from the event system that triggers this method.
   * @throws SQLException Contains the information for problem encountered in the SQL connection.
   */
  @FXML
  void addProductClicked(MouseEvent event) throws SQLException {
    addProductStmt.setString(1, cbItemType.getValue());
    addProductStmt.setString(2, taManufacturer.getText());
    addProductStmt.setString(3, taProductName.getText());
    addProductStmt.execute();
  }

  /**
   * Button handler for the Record Product button.
   * @param event Contains information from the event system that triggers this method.
   */
  @FXML
  void recordProductButtonClick(MouseEvent event) {
      // todo: add logic for the record button
  }

  /**
   * Method to connect to the H2 database and settup the prepared statements for use later.
   * @returns none
   */
  private void initializeDB() {

    // Database credentials
    final String User = "";
    // findbugs error for not setting password, will be set at a later time.
    final String Pass = "";

    String addProductString = "INSERT INTO PUBLIC.PRODUCT(type, manufacturer, name) "
        + "VALUES (?, ?, ?);";

    try {
      //STEP 1: Register JDBC driver
      Class.forName(JDBCDRIVER);

      //findbugs wants a password to be set but was it not specified for this project.
      conn = DriverManager.getConnection(DBURL, User, Pass);
      addProductStmt = conn.prepareStatement(addProductString);

    } catch (Exception ex) {
      ex.printStackTrace();
    } //end try catch
  } //end method initializeDB

}
