package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.util.Duration;

public class HTMLEditorController implements Initializable {

	@FXML private Parent root;
	@FXML private HTMLEditor htmlEditor;
	@FXML private Label lblLiteratureTitle;
	@FXML private Label lblMessage;
	@FXML private Button btnSave;


	private HTMLEditorAction saveAction;
	private boolean needsToSave;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		setStateNeedsToSave(false);
	}

	@FXML
	private void btnSaveAction(ActionEvent e){

		save();

		flashMessage(lblMessage, "Saved");

	}


	public void save(){
		String htmlText = removeContentEditableFlag(htmlEditor.getHtmlText());

		saveAction.setData(htmlText);
		Thread th = new Thread(saveAction);
		th.setName("HTMLEditorController");
		th.start();

		setStateNeedsToSave(false);
	}

	private void flashMessage(Label lbl, String message){

		lbl.setText(message);

		FadeTransition fadeTransition = new FadeTransition(Duration.millis(750), lbl);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		lbl.setVisible(true);
		fadeTransition.play();
		fadeTransition.setOnFinished(e -> {
			FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(250), lbl);
			fadeTransition2.setDelay(Duration.millis(5000));
			fadeTransition2.setFromValue(1.0);
			fadeTransition2.setToValue(0.0);
			fadeTransition2.play();
			fadeTransition2.setOnFinished(e2 -> {
				lbl.setText("");
				lbl.setVisible(false);
			});
		});
	}

	@FXML
	private void htmlEditorMouseClicked(MouseEvent e){
		setStateNeedsToSave(true);
	}

	@FXML
	private void htmlEditorKeyPressed(KeyEvent e){
		setStateNeedsToSave(true);
	}

	private void setStateNeedsToSave(boolean state){
		if (state){
			this.needsToSave = true;
			this.btnSave.disableProperty().set(false);
		} else {
			this.needsToSave = false;
			this.btnSave.disableProperty().set(true);
		}

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

	public boolean needsToSave() {
		return needsToSave;
	}

	public void setNeedsToSave(boolean needsToSave) {
		this.needsToSave = needsToSave;
	}


	// Helper methods -----------------------------



}

