package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.web.HTMLEditor;

public class HTMLEditorController implements Initializable {

	@FXML private Parent root;
	@FXML private HTMLEditor htmlEditor;
	@FXML private Label lblLiteratureTitle;

	private HTMLEditorAction saveAction;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {


	}

	@FXML
	private void btnSaveAction(ActionEvent e){

		String htmlText = removeContentEditableFlag(htmlEditor.getHtmlText());

		saveAction.setData(htmlText);
		Thread th = new Thread(saveAction);
		th.setName("HTMLEditorController");
		th.start();

	}

	@FXML
	private void btnSaveAndCloseAction(ActionEvent e){
		btnSaveAction(e);
		root.getScene().getWindow().hide();

	}

	private String removeContentEditableFlag(String html){
		if(html.contains("contenteditable=\"true\"")){
			html=html.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
		}
		return html;
	}


	public HTMLEditorAction getSaveAction() {

		return saveAction;
	}

	public void setSaveAction(HTMLEditorAction saveAction) {
		this.saveAction = saveAction;
		this.lblLiteratureTitle.setText(saveAction.getLiterature().getTitle());

	}

	public void setHtmlContent(String htmlContent){

		htmlEditor.setHtmlText(htmlContent);
	}


	// Helper methods -----------------------------



}

