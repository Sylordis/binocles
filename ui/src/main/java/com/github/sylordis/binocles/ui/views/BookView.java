package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.ui.BinoclesController;
import com.github.sylordis.binocles.ui.javafxutils.Browser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Pane component for a book.
 */
public class BookView extends BorderPane implements Initializable, BinoclesTabPane {

	private static final int METADATA_START = 3;

	private Book book;

	@FXML
	private Text bookTitle;
	@FXML
	private VBox bookZoneVBox;
	@FXML
	private Text nomenclatureField;
	@FXML
	private TextFlow bookSynopsisField;
	@FXML
	private TextFlow bookDescriptionField;
	@FXML
	private GridPane bookGrid;

	public BookView(BinoclesController mainController, Book book) {
		super();
		this.book = book;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book_view.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set content
		bookTitle.setText(book.getTitle());
		if (book.getNomenclature() != null) {
			nomenclatureField.setText(book.getNomenclature().getName());
			nomenclatureField.getStyleClass().clear();
		} else {
			nomenclatureField.setText("No nomenclature set.");
			nomenclatureField.getStyleClass().addAll("text-note");
		}
		bookSynopsisField.getChildren().add(new Text(book.getSynopsis()));
		if (book.getDescription().isEmpty()) {
			Text text = new Text("No description provided.");
			text.getStyleClass().addAll("text-muted", "text-note");
			bookDescriptionField.getChildren().add(text);
		} else {
			bookDescriptionField.getChildren().add(new Text(book.getDescription()));
		}
		int index = METADATA_START;
		for (Map.Entry<String, String> metadata : book.getMetadata().entrySet()) {
			Label label = new Label(StringUtils.capitalize(metadata.getKey()));
			Node text = null;
			if (metadata.getValue().startsWith("https://")) {
				text = new Hyperlink(metadata.getValue());
				((Hyperlink) text).setOnAction(e -> new Browser().open(metadata.getValue()));
			} else {
				text = new Text(metadata.getValue());
			}
			TextFlow textFlow = new TextFlow(text);
			bookGrid.addRow(index, label, textFlow);
			index++;
		}
	}

	@Override
	public Object getItem() {
		return book;
	}

}
