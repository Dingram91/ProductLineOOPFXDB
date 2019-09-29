/*
Author: Dylan Ingram
Date: 9/28/19
Description: Main method for serving as the entry point of program as well as initalizing
the scene and controller.
*/

package productionapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that contains the program entry point and sets up and executes the scenes for the GUI.
 *
 * @author Dylan Ingram
 */
public class Main extends Application {

  /**
   * Start method that initializes the windows and loads the scenes for the program.
   *
   * @param primaryStage The container that hosts the scenes for the program
   * @throws Exception containing the information about the problem with the connection
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
    primaryStage.setTitle("Hello World");
    root.getStylesheets().add(Main.class.getResource("main.css").toExternalForm());
    primaryStage.setScene(new Scene(root, 800, 500));
    primaryStage.show();
  }

  /**
   * Main method that serves only as the entry point to the program.
   *
   * @param args string array for command line arguments, required for execution but not used.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
