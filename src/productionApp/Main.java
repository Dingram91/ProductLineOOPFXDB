package productionApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Dylan Ingram
 */
public class Main extends Application {

  /**
   *
   * @param primaryStage
   * @throws Exception
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
    primaryStage.setTitle("Hello World");
    root.getStylesheets().add
        (Main.class.getResource("main.css").toExternalForm());
    primaryStage.setScene(new Scene(root, 800, 500));
    primaryStage.show();
  }


  /**
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }
}
