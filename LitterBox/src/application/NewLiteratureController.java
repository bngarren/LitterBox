package application;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class NewLiteratureController implements Initializable {

	@FXML
	private BorderPane root;

	// Tag box system -------------------------------
	@FXML private AnchorPane tagBox;
	@FXML private ImageView tagIcon;
	@FXML private TextField txtTagBoxInput;
	@FXML private ListView tagListView;
	// ----------------------------------------------

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {




		ChangeListener<Boolean> tagBoxFocusListener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (!newValue && !NewLiteratureController.this.txtTagBoxInput.isFocused()
						&& !NewLiteratureController.this.tagListView.isFocused()){
					fadeOutTagBox();
				}
			}
		};

		this.txtTagBoxInput.focusedProperty().addListener(tagBoxFocusListener);
		this.tagListView.focusedProperty().addListener(tagBoxFocusListener);




	}

	private void populateTagListView(){
		tagListView.setItems(FXCollections.observableList(Arrays.asList("Pediatrics", "Medicine", "Surgery", "Guidelines", "Family Medicine")));
	}

	@FXML
	private void tagIconClicked(MouseEvent e){

		if(!tagBox.isVisible()){
			populateTagListView();
			fadeInTagBox();
		} else {
			fadeOutTagBox();
		}


	}

	private void fadeInTagBox(){
		FadeTransition ft = new FadeTransition(Duration.millis(250), tagBox);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		tagBox.setVisible(true);
		ft.play();
		txtTagBoxInput.requestFocus();
	}

	private void fadeOutTagBox(){
		FadeTransition ft = new FadeTransition(Duration.millis(200), tagBox);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		tagBox.setVisible(false);
		ft.play();
	}


}
