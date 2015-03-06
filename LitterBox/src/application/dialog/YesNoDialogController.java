package application.dialog;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class YesNoDialogController implements Initializable {


	@FXML
	private Label lblMessage;

	@FXML
	private Button btnNo;

	@FXML
	private Button btnYes;




	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {


	}

	@FXML
	void btnYesAction(ActionEvent event) {

	}

	@FXML
	void btnNoAction(ActionEvent event) {

	}

}
