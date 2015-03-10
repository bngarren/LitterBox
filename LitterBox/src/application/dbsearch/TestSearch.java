package application.dbsearch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestSearch extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("TestSearch.fxml"));
		BorderPane root = (BorderPane) loader.load();

		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("testSearch.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("LitterBox - Test Search");
		stage.centerOnScreen();
		stage.initStyle(StageStyle.UNIFIED);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
