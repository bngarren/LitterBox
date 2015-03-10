package application.dbsearch;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import application.LiteratureHeadingListCell;

import com.db.DAOFactory;
import com.db.LiteratureDAO;
import com.model.Literature;
import com.model.LiteratureHeading;

public class TestSearchController implements Initializable {

	@FXML
	private TextArea console;

	@FXML
	private Button btnCancelSearch;

	@FXML private ProgressIndicator progress;

	@FXML
	private ListView<LiteratureHeading> listView;

	@FXML
	private TextField txtSearch;

	private LiteratureDAO literatureDAO;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		btnCancelSearch.setVisible(false);

		DAOFactory daoFactory = DAOFactory.getInstance("lit_keeper");
		literatureDAO = daoFactory.getLiteratureDAO();

		txtSearch.textProperty().addListener(change -> {
			if (txtSearch.textProperty().getValue().isEmpty()) {
				TestSearchController.this.cancelSearch();
				return;
			}

			System.out.println(txtSearch.textProperty().getValue());
			long startTime = System.nanoTime();
			List<Literature> matches = TestSearchController.this.getLiteratureThatMatchesString(txtSearch.textProperty().getValue());
			TestSearchController.this.fillListView(matches);
			long delta = System.nanoTime() - startTime;
			TestSearchController.this.console.appendText("\nQuery '" +
					txtSearch.textProperty().getValue() + "' delta: " + Duration.ofNanos(delta).toMillis() + " ms.");
			TestSearchController.this.btnCancelSearch.setVisible(true);
		});


		// Set the listView's cell factory, i.e. how each cell is displayed
		listView.setCellFactory((Callback<ListView<LiteratureHeading>, ListCell<LiteratureHeading>>) callback -> {
			return new LiteratureHeadingListCell();
		});

	}

	@FXML
	private void btnCancelSearchAction(ActionEvent e){
		txtSearch.setText("");
		cancelSearch();
	}

	private void cancelSearch(){
		List<Literature> list = literatureDAO.list();
		fillListView(list);
		btnCancelSearch.setVisible(false);

	}

	private void fillListView(List<Literature> list){
		List<LiteratureHeading> headings = new ArrayList<LiteratureHeading>();
		list.forEach(lit -> headings.add(lit.getLiteratureHeading()));

		ObservableList<LiteratureHeading> data = FXCollections.observableArrayList(headings);
		listView.setItems(data);
		System.out.println("filling list view with " + data.size() + " items");
	}


	private List<Literature> getLiteratureThatMatchesString(String input){
		List<Literature> list = new ArrayList<Literature>();
		list = literatureDAO.searchTitleAndSummaryFor(input);
		list.forEach(lit -> System.out.println(lit.getTitle()));

		return list;
	}


}
