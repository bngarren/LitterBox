package application.dialog;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import com.model.Literature;

public class UploadDialogController implements Initializable {

	@FXML private Button btnYes;
	@FXML private Button btnNo;
	@FXML private Label lblLiteratureTitle;
	@FXML private Label lblFilename;

	private UploadDialogListener listener;

	private Literature literature;
	private File file;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {


	}

	@FXML private void btnYesAction(ActionEvent e){

		Node root = (Node) e.getSource();
		root.getScene().getWindow().hide();

		if (listener != null)
			listener.accepted(this.file);

	}

	@FXML private void btnNoAction(ActionEvent e){

		Node root = (Node) e.getSource();
		root.getScene().getWindow().hide();

		if (listener != null)
			listener.denied();

	}


	public UploadDialogListener getListener() {
		return listener;
	}



	public void setListener(UploadDialogListener listener) {
		this.listener = listener;
	}



	public Literature getLiterature() {
		return literature;
	}

	public void setLiterature(Literature literature) {
		this.literature = literature;
		lblLiteratureTitle.setText(this.literature.getTitle());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		lblFilename.setText(this.file.getName());
	}

}
