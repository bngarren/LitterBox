package application;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.model.LiteratureHeading;

public class LiteratureHeadingListCell extends ListCell<LiteratureHeading> {

	@Override
	protected void updateItem(LiteratureHeading heading, boolean b) {

		super.updateItem(heading, b);

		if (heading != null){

			Label label = new Label(heading.getTitle());

			if (heading.isPDFAvailable()){
				Image image = new Image(getClass().getResourceAsStream("/assets/pdf_icon.gif"));
				label.setGraphic(new ImageView(image));
				label.setContentDisplay(ContentDisplay.LEFT);
				setPadding(new Insets(5, 10, 5, 5));
			}

			setGraphic(label);
		}
	}



}
