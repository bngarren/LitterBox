package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import application.dialog.UploadDialogController;
import application.dialog.UploadDialogListener;
import application.dialog.YesNoDialogController;

import com.db.DAOFactory;
import com.db.LiteratureDAO;
import com.model.Client;
import com.model.Literature;
import com.model.LiteratureHeading;
import com.server.LBServer;

public class RootViewController implements Initializable {

	@FXML private BorderPane root;
	@FXML private TabPane tabPane;
	@FXML private Tab tabHome, tabEditor;
	@FXML private BorderPane tabEditorBorderPane;
	@FXML private ProgressIndicator progressIndicator;
	@FXML private Accordion accordion;
	@FXML private ListView<LiteratureHeading> listView;
	@FXML private Menu menuClient;
	@FXML private Label lblUser;
	@FXML private WebView webViewSummary;
	@FXML private ContextMenu webViewSummaryContextMenu;
	@FXML private Label lblPDFAvailable;

	private Client client;
	private URL clientServerDirectory;


	private LiteratureDAO literatureDAO;

	private Literature currentLiterature;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		currentLiterature = null;

		DAOFactory litKeeperDB = DAOFactory.getInstance("lit_keeper");
		literatureDAO = litKeeperDB.getLiteratureDAO();



		// Set the listView's cell factory, i.e. how each cell is displayed
		listView.setCellFactory((Callback<ListView<LiteratureHeading>, ListCell<LiteratureHeading>>) callback -> {
			return new LiteratureHeadingListCell();
		});


		// What to do when a new selection is made
		listView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

			populateAccordionWithLiteratureById(listView.getSelectionModel().getSelectedItem().getId());

		});

		// Start off with accordion hidden
		accordion.setVisible(false);

		//Tab closing policy
		this.tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

		// Start off with the editor tab not available
		this.tabPane.getTabs().remove(tabEditor);


		//Create webViewSummary's context menu

		webViewSummary.setContextMenuEnabled(false); // disable default context menu
		webViewSummaryContextMenu = new ContextMenu();
		MenuItem edit = new MenuItem("Edit...");
		System.out.println(Thread.currentThread().getName());
		edit.setOnAction(e -> {
			try {

				HTMLEditorAction saveAction = new HTMLEditorAction() {
					@Override
					void execute() {
						getLiterature().setSummary((String) getData());
					}
				};
				saveAction.setLiterature(currentLiterature);
				saveAction.setListener(new HTMLEditorActionListener() {
					@Override
					public void actionComplete(HTMLEditorAction action) {
						RootViewController.this.updateLiterature(action.getLiterature());
					}
				});

				openEditorTab(saveAction, currentLiterature.getSummary());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		webViewSummaryContextMenu.getItems().addAll(edit);

		webViewSummary.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				webViewSummaryContextMenu.show(webViewSummary, e.getScreenX(), e.getScreenY());
			} else {
				webViewSummaryContextMenu.hide();
				e.consume();
			}
		});

	}

	private void fillListView(){
		// First get a list of literature by owner
		// Then get literature heading for each literature

		List<Literature> listLiterature = literatureDAO.listByOwner(client.getUsername());
		List<LiteratureHeading> headings = new ArrayList<LiteratureHeading>();

		listLiterature.forEach(lit -> headings.add(lit.getLiteratureHeading()));

		ObservableList<LiteratureHeading> data = FXCollections.observableArrayList(headings);
		listView.setItems(data);
	}


	public void populateAccordionWithLiteratureById(int id){

		Task<Void> task = new Task<Void>(){

			private long startTime;

			@Override
			protected Void call() throws Exception {
				this.startTime = System.nanoTime();

				final Literature lit = literatureDAO.find(id);

				if (lit != null){
					Platform.runLater(new AccordionPopulator(lit));
				}
				return null;
			}

			@Override
			protected void succeeded() {
				@SuppressWarnings("unused")
				long delta = (System.nanoTime() - startTime)/1000000;
				//System.out.printf("Task took %s ms\n", delta);
				super.succeeded();
			}


		};

		progressIndicator.visibleProperty().bind(task.runningProperty());
		Thread th = new Thread(task);
		th.setName("DAO Thread");
		th.start();

	}

	class AccordionPopulator implements Runnable{


		AccordionPopulator(Literature lit){
			RootViewController.this.currentLiterature = lit;
		}

		@Override
		public void run() {
			System.out.println("AccordionPopulator run(): on thread " + Thread.currentThread().getName());
			System.out.println("\tPopulating accordion with literature: " + currentLiterature.getTitle());

			accordion.setExpandedPane(accordion.getPanes().get(0));

			String htmlStart = "<!DOCTYPE html><HTML><BODY style='font-family:arial; font-size:13px;'>";
			String htmlEnd = "</BODY></HTML>";
			WebEngine engine = webViewSummary.getEngine();
			engine.loadContent(htmlStart + currentLiterature.getSummary() + htmlEnd);

			accordion.setVisible(true);

			// Is a pdf available?
			if (currentLiterature.isPDFAvailable()){
				lblPDFAvailable.setText("Full text available");
				lblPDFAvailable.setDisable(false);
			} else {
				lblPDFAvailable.setText("No PDF available");
				lblPDFAvailable.setDisable(true);
			}

		}

	}


	private void openEditorTab(HTMLEditorAction action, String htmlContent) throws IOException{
		URL url = this.getClass().getResource("/application/HTMLEditor.fxml");

		FXMLLoader loader = new FXMLLoader(url);

		Parent parent = loader.load();

		// Get the controller so that we can pass it the ActionEvent for what to do when "save" is pressed
		HTMLEditorController controller = (HTMLEditorController) loader.getController();

		controller.setHtmlContent(htmlContent);
		controller.setSaveAction(action);

		this.tabPane.getTabs().add(tabEditor);
		this.tabEditorBorderPane.setCenter(parent);
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select(tabEditor);


	}

	@FXML
	private void tabEditorCloseRequested(Event e) throws IOException{

		URL url = this.getClass().getResource("/application/dialog/YesNoDialog.fxml");

		FXMLLoader loader = new FXMLLoader(url);

		Parent parent = loader.load();

		YesNoDialogController controller = (YesNoDialogController) loader.getController();

		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.UTILITY);
		stage.setTitle("Alert");
		stage.show();


	}

	private void openFileUploadDialog(Literature literature, File file) throws IOException{

		URL url = this.getClass().getResource("/application/dialog/UploadDialog.fxml");

		FXMLLoader loader = new FXMLLoader(url);

		Parent parent = loader.load();

		UploadDialogController controller = (UploadDialogController) loader.getController();
		controller.setLiterature(literature);
		controller.setFile(file);
		controller.setListener(new UploadDialogListener() {
			@Override
			public void denied() {
				System.out.println("File upload denied");
			}
			@Override
			public void accepted(File file) {
				System.out.println("File upload accepted");
			}
		});

		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.UTILITY);
		stage.setTitle("LitterBox - Upload File");
		stage.show();

	}

	@FXML private void lblPDFAvailableAction(MouseEvent e){

		System.out.println("Open pdf viewer for: " + this.currentLiterature.getTitle());

		Task<Void> task = new Task<Void>(){

			@Override
			protected Void call() throws Exception {

				File fileToLoad = null;
				PDFDownloader loader = null;

				long startTime = System.nanoTime();

				try {
					URL url = new URL(clientServerDirectory.toExternalForm() + "/" + currentLiterature.getPdfFilename() + ".pdf");
					loader = new PDFDownloader(url);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				fileToLoad = loader.getFileFromServer();

				if (fileToLoad.exists()){
					openPDFProgram(fileToLoad);
				}

				long delta = System.nanoTime() - startTime;
				System.out.println("Delta = " + delta/1000000);

				return null;
			}
		};

		Thread th = new Thread(task);
		th.start();


	}

	private void updateLiterature(Literature literature){

		System.out.println("Saving literature..." + literature.getTitle());

		literatureDAO.update(literature);

		populateAccordionWithLiteratureById(literature.getId());
	}







	@FXML
	private void pdfLabelPaneOnDragEntered(DragEvent e){

		Dragboard db = e.getDragboard();

		List<File> files = null;
		if (db.hasFiles())
			files = e.getDragboard().getFiles();
		else {
			e.consume();
			return;
		}

		if (files.size() > 1){
			e.consume();
			return;
		} else {

			lblPDFAvailable.setStyle("-fx-border-width: 2px; -fx-border-color: #7DBFAF;" );
			//lblPDFAvailable.setDisable(false);

		}



	}

	@FXML
	private void pdfLabelPaneOnDragOver(DragEvent e){

		Dragboard db = e.getDragboard();

		List<File> files = null;
		if (db.hasFiles())
			files = e.getDragboard().getFiles();
		else {
			e.consume();
			return;
		}

		if (files.size() > 1){
			e.consume();
			return;
		} else {
			e.acceptTransferModes(TransferMode.ANY);
		}

	}

	@FXML
	private void pdfLabelPaneOnDragDropped(DragEvent e){

		Dragboard db = e.getDragboard();

		boolean isDropSuccess = false;

		List<File> files = null;
		if (db.hasFiles())
			files = e.getDragboard().getFiles();
		else {
			e.consume();
			return;
		}

		if (files.size() > 1){
			e.consume();
			return;
		} else {
			// Drop file
			File droppedFile = db.getFiles().get(0);
			System.out.println("Sucessfully dropped " + droppedFile.getName());
			System.out.println("\tAbsolute path = " + droppedFile.getAbsolutePath());
			System.out.println("\tgetPath() = " + droppedFile.getPath());

			try {
				openFileUploadDialog(this.currentLiterature, droppedFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			isDropSuccess = true;

		}
		e.setDropCompleted(isDropSuccess);
		e.consume();

	}

	@FXML
	private void pdfLabelPaneOnDragDone(DragEvent e){
		System.out.println("Drag done");
	}

	@FXML
	private void pdfLabelPaneOnDragExited(DragEvent e){
		System.out.println("Drag exited");
		lblPDFAvailable.setStyle("");
	}


	@FXML
	private void switchUserAction(ActionEvent e) throws IOException{
		Main.createAndShowLogin(this, new Stage());

		this.root.getScene().getWindow().hide();
	}

	@FXML
	private void closeAction(ActionEvent e){
		this.root.getScene().getWindow().hide();
	}

	@FXML
	private void lblUserMouseEntered(MouseEvent e){

	}

	@FXML
	private void lblUserMouseExited(MouseEvent e){

	}

	private void openPDFProgram(File file){
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException ex) {
				// no application registered for PDFs
			}
		}
	}


	public Client getClient() {
		return client;
	}


	public void setClient(Client client){
		this.client = client;

		String clientDirectory = "/" + client.getServerDirectory();

		URL urlToClientDirectory = LBServer.INSTANCE.getURLOfClientDirectory(clientDirectory);

		if (urlToClientDirectory != null){
			if (LBServer.INSTANCE.doesClientDirectoryExist(urlToClientDirectory)){
				this.clientServerDirectory = urlToClientDirectory;
				System.out.println(client.getUsername() + "'s server directory URL is = " + clientServerDirectory.toExternalForm());
			} else {
				System.err.println("Could not find server directory for " + client.getUsername() + " at " + urlToClientDirectory);
			}
		} else {
			System.err.println("Could not resolve URL to server directory for " + client.getUsername() + " at " + urlToClientDirectory);
		}


		// Fill the list view of client's literature
		fillListView();


		// Update user label
		this.menuClient.setText(client.getUsername());
	}




}
