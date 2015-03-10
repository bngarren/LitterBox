package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import application.clientmanager.ClientManager;

import com.db.ClientDAO;
import com.db.DAOFactory;
import com.model.Client;

public class LoginController implements Initializable {


	@FXML private Label lblMessage;
	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private Button btnLogin;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML
	private void btnLoginAction(ActionEvent e) throws IOException{
		login(e);
	}

	@FXML
	private void txtEnterAction(KeyEvent e) throws IOException{
		if (e.getCode() == KeyCode.ENTER){
			login(e);
		}

	}

	@FXML
	private void btnCloseClicked(MouseEvent e){
		Platform.exit();
	}

	private void login(Event e) throws IOException{

		String username = txtUsername.getText();
		String password = txtPassword.getText();
		if (!isTxtValid(username)){
			lblMessage.setText("Username is invalid.");
			return;
		} else {

			if (!isTxtValid(password)){
				lblMessage.setText("Password is invalid.");
				return;
			}
		}

		DAOFactory daoFactory = DAOFactory.getInstance("lit_keeper");
		ClientDAO clientDAO = daoFactory.getClientDAO();

		Client client = clientDAO.find(username);

		if (client == null){
			lblMessage.setText("Username not found.");
			return;
		}

		if (client.getPassword().equals(password)){

			URL url = this.getClass().getResource("/application/RootView.fxml");

			FXMLLoader loader = new FXMLLoader(url);

			Parent parent = loader.load();

			RootViewController controller = (RootViewController) loader.getController();
			//controller.setClient(client);

			ClientManager cm = ClientManager.INSTANCE;

			cm.setListener(controller);
			cm.setClient(client);

			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setTitle(Main.TITLE);

			stage.show();

			controller.setStage(stage);

			Node source = (Node) e.getSource();
			source.getScene().getWindow().hide();

		} else {
			lblMessage.setText("Incorrect username or password.");
		}
	}


	private boolean isTxtValid(String input){

		String text = input.trim();

		if (text.isEmpty())
			return false;

		if (text.equals(""))
			return false;

		return true;

	}


}
