package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.CommentTypeField;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.BinoclesController;
import com.github.sylordis.binocles.ui.javafxutils.StyleUtilsFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Pane component for a {@link CommentType} item.
 */
public class CommentTypeView extends BorderPane implements Initializable, BinoclesTabPane {

	/**
	 * Current item being represented in this view.
	 */
	private CommentType commentType;
	/**
	 * Parent item of the current comment type.
	 */
	private Nomenclature nomenclature;
	/**
	 * Parent controller.
	 */
	private BinoclesController mainController;

	@FXML
	private Text commentTypeName;
	@FXML
	private Text nomenclatureName;
	@FXML
	private TextFlow descriptionFlow;
	@FXML
	private TextFlow styleFlow;
	@FXML
	private TextFlow previewFlow;
	@FXML
	private GridPane commentTypeGrid;
	@FXML
	private GridPane fieldsListGrid;

	/**
	 * Creates a new comment type view.
	 * 
	 * @param mainController parent controller
	 * @param nomenclature   parent nomenclature
	 * @param commentType    comment type displayed in this view
	 */
	public CommentTypeView(BinoclesController mainController, Nomenclature nomenclature, CommentType commentType) {
		super();
		this.mainController = mainController;
		this.nomenclature = nomenclature;
		this.commentType = commentType;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("comment_type_view.fxml"));
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
		commentTypeName.setText(StringUtils.capitalize(commentType.getName()));
		nomenclatureName.setText(StringUtils.capitalize(nomenclature.getName()));
		if (commentType.getDescription().isEmpty()) {
			Text text = new Text("No description provided.");
			text.getStyleClass().addAll("text-muted", "text-note");
			descriptionFlow.getChildren().add(text);
		} else {
			Text text = new Text(commentType.getDescription());
			descriptionFlow.getChildren().add(text);
		}
		styleFlow.getChildren().setAll(StyleUtilsFX.createDecoratedHumanFormatNodes(commentType.getStyles()));
		updateFieldsList();
	}

	private void updateFieldsList() {
		fieldsListGrid.getChildren().clear();
		int index = 0;
		for (CommentTypeField field : commentType.getFields().values()) {
			Label label = new Label(
			        StringUtils.capitalize(field.getName()) + (field.getIsLongText() ? " (long)" : "") + ":");
			Text text = new Text(field.getDescription());
			TextFlow textFlow = new TextFlow(text);
			fieldsListGrid.addRow(index++, label, textFlow);
		}
	}

	@Override
	public Object getItem() {
		return commentType;
	}

	/**
	 * @return the nomenclature
	 */
	public Nomenclature getNomenclature() {
		return nomenclature;
	}

	/**
	 * @param nomenclature the nomenclature to set
	 */
	public void setNomenclature(Nomenclature nomenclature) {
		this.nomenclature = nomenclature;
		nomenclatureName.setText(nomenclature.getName());
	}

}
