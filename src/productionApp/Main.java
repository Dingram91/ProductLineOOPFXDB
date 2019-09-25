package productionApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 */
public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
    primaryStage.setTitle("Hello World");
    primaryStage.setScene(new Scene(root, 800, 275));
    primaryStage.show();

  }


  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }
}
