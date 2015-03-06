package application.dialog;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class YesNoDialogController implements Initializable {


	@FXML
	private Label lblMessage;

	@FXML
	private Button btnNo;

	@FXML
	private Button btnYes;

	private YesNoDialogListener listener;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		this.lblMessage.setText("Unknown alert");
	}

	@FXML
	void btnYesAction(ActionEvent event) {

		Node root = (Node) event.getSource();
		root.getScene().getWindow().hide();

		if (listener != null)
			listener.yes();
	}

	@FXML
	void btnNoAction(ActionEvent event) {

		Node root = (Node) event.getSource();
		root.getScene().getWindow().hide();

		if (listener != null)
			listener.no();

	}

	public void setMessage(String message){
		this.lblMessage.setText(message);
	}

	public YesNoDialogListener getListener() {
		return listener;
	}

	public void setListener(YesNoDialogListener listener) {
		this.listener = listener;
	}

}
