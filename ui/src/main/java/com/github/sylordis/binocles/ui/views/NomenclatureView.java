package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.ui.BinoclesController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Pane component for a {@link Nomenclature} item.
 */
public class NomenclatureView extends BorderPane implements Initializable, BinoclesTabPane {

	/**
	 * Current item being represented in this view.
	 */
	private Nomenclature nomenclature;
	/**
	 * Parent controller.
	 */
	private BinoclesController mainController;

	@FXML
	private Text nomenclatureTitle;
	@FXML
	private ScrollPane mainScrollPane;
	@FXML
	private VBox nomenclatureZoneVBox;
	@FXML
	private GridPane commentTypesListGrid;
	@FXML
	private GridPane usedInListGrid;
	@FXML
	private GridPane bookGrid;

	public NomenclatureView(BinoclesController mainController, Nomenclature nomenclature) {
		super();
		this.mainController = mainController;
		this.nomenclature = nomenclature;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("nomenclature_view.fxml"));
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
		nomenclatureTitle.setText(nomenclature.getName());
		updateCommentTypesList();
		updateUsedInList();
	}

	private void updateCommentTypesList() {
		commentTypesListGrid.getChildren().clear();
		if (nomenclature.getTypes().isEmpty()) {
			Text noteNoCommentsType = new Text("No comment types registered for this nomenclature.");
			noteNoCommentsType.getStyleClass().add("text-note");
			commentTypesListGrid.add(noteNoCommentsType, 0, 0, 2, 1);
		} else {
			int i = 0;
			for (CommentType type : nomenclature.getTypes()) {
				Label label = new Label(type.getName() + ":");
				Text description = new Text(type.getDescription());
				// TODO Add colour preview, with boxes for foreground and background
				TextFlow descriptionFlow = new TextFlow(description);
				commentTypesListGrid.addRow(i++, label, descriptionFlow);
			}
		}
	}

	private void updateUsedInList() {
		usedInListGrid.getChildren().clear();
		List<Book> booksUsing = mainController.getModel().getBooks().stream()
		        .filter(b -> nomenclature.equals(b.getNomenclature())).collect(Collectors.toList());
		if (booksUsing.isEmpty()) {
			Text noteNotUsed = new Text("This nomenclature is not used.");
			noteNotUsed.getStyleClass().add("text-note");
			usedInListGrid.add(noteNotUsed, 0, 0, 2, 1);
		} else {
			int i = 0;
			for (Book book : booksUsing) {
				Text title = new Text(book.getTitle());
				TextFlow titleFlow = new TextFlow(title);
				usedInListGrid.addRow(i++, titleFlow);
			}
		}
	}

	@Override
	public void updateControllerStatus(BinoclesController controller) {
		nomenclatureTitle.setText(nomenclature.getName());
		updateCommentTypesList();
		updateUsedInList();
	}

	@Override
	public Object getItem() {
		return nomenclature;
	}

}
