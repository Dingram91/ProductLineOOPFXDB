package productionApp;
// make sure to do analys inspect code (no suspicios code found)

// file - settings - pluggins - FindBugs-IDEA (Install) check code before submitting

// Formatter ctrl - alt - L (use google-java-format) (want to see no lines changed, every file!
//
// one more file called check-style)

// java-docs for every method note //* will generate some of the information automatically
// view javadoc requirements in project page

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.*;

/**
 * @author Dylan Ingram
 */
public class Controller {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:C:\\Users\\Dingr\\OneDrive - Florida Gulf Coast University\\COP3003\\Projects\\gui_database\\res\\HR";
    Connection conn = null; // blank connection
    private PreparedStatement addProductSTMT = null;

    public Controller() throws SQLException {
        initializeDB();
        String addProductString = "INSERT INTO Product(type, manufacturer, name) VALUES (?, ?, ?);";

    }

    @FXML
    private TextField ta_productName;

    @FXML
    private TextField ta_manufacturer;

    @FXML
    private ChoiceBox<String> cb_ItemType;


    @FXML
    private ComboBox<Integer> cbb_produce_chooseQuantity;


    @FXML
    public void initialize() {
        cb_ItemType.getItems().addAll("Audio", "Paper", "Toy");
        for (int x = 1; x <= 10; x++){
            cbb_produce_chooseQuantity.getItems().addAll(x);
        }

    }

    @FXML
    void addProductClicked(MouseEvent event) throws SQLException {
        addProductSTMT.setString(1, cb_ItemType.getValue());
        addProductSTMT.setString(2, ta_manufacturer.getText());
        addProductSTMT.setString(3, ta_productName.getText());
        addProductSTMT.execute();
    }

    @FXML
    void recordProductButtonClick(MouseEvent event) {

    }

    private void initializeDB() {
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:.\\res\\ProductionDB";

        // Database credentials
        final String USER = "";
        final String PASS = "";
        conn = null;

        String addProductString = "INSERT INTO PUBLIC.PRODUCT(type, manufacturer, name) VALUES (?, ?, ?);";

        try {
            //STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //STEP 2: Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            addProductSTMT = conn.prepareStatement(addProductString);


            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rsTables = dbmd.getTables(null,"PUBLIC",null, new String[] {"TABLE_NAME"});
            ResultSet rsSchemas = dbmd.getSchemas();




        }catch (Exception ex) {
            ex.printStackTrace();
        }//end try catch
    }//end method initializeDB

}
