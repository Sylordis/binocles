package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.ui.javafxutils.FXFormatUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Pane component for a book.
 */
public class BookView extends AnchorPane implements Initializable, BinoclesTabPane {

	private Book book;

	@FXML
	private Text bookTitle;
	@FXML
	private VBox bookZoneVBox;
	@FXML
	private TextFlow bookSynopsisField;
	@FXML
	private TextFlow bookDescriptionField;
	@FXML
	private GridPane bookGrid;
	@FXML
	private ScrollPane bookZoneScroll;
	@FXML
	private TableView<Map.Entry<String, String>> bookMetadataTable;

	private ObservableList<Map.Entry<String, String>> bookMetadataTableData;

	public BookView(Book book) {
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
		bookSynopsisField.getChildren().add(new Text(book.getSynopsis()));
		bookDescriptionField.getChildren().add(new Text(book.getDescription()));
		bookMetadataTable.setPlaceholder(new Label("No metadata provided"));
		bookMetadataTableData = FXCollections.observableArrayList();
		bookMetadataTableData.addAll(book.getMetadata().entrySet());
		bookMetadataTable.setItems(bookMetadataTableData);
		// Format
		FXFormatUtils.bindToParent(bookZoneScroll);
		bookSynopsisField.prefWidthProperty().bind(this.widthProperty());
	}

	@Override
	public Object getItem() {
		return book;
	}

}
