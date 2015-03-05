package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

	public static final String TITLE = "LitterBox";

	@Override
	public void start(Stage primaryStage) {
		try {

			createAndShowLogin(this, primaryStage);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void createAndShowLogin(Object parent, Stage stage) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(parent.getClass().getResource("Login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Scene scene = new Scene(root);
		scene.getStylesheets().add(parent.getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("LitterBox - Login");
		stage.centerOnScreen();
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
