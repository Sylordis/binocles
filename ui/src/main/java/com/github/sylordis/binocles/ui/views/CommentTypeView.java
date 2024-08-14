package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.CommentTypeField;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.BinoclesController;
import com.github.sylordis.binocles.utils.StyleUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Pane component for a chapter.
 */
public class CommentTypeView extends BorderPane implements Initializable, BinoclesTabPane {

	private final static int FIELDS_ROW_START = 3;

	private CommentType commentType;
	private Nomenclature nomenclature;

	@FXML
	private Text commentTypeName;
	@FXML
	private Text nomenclatureName;
	@FXML
	private TextFlow descriptionFlow;
	@FXML
	private TextFlow styleFlow;
	@FXML
	private GridPane commentTypeGrid;

	public CommentTypeView(BinoclesController parent, Nomenclature nomenclature, CommentType commentType) {
		super();
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
		styleFlow.getChildren().add(new Text(StyleUtils.toHumanString(commentType.getStyles())));
		int index = FIELDS_ROW_START;
		for (CommentTypeField field : commentType.getFields().values()) {
			Label label = new Label(StringUtils.capitalize(field.getName()) + (field.getIsLongText() ? " (long)" : ""));
			Text text = new Text(field.getDescription());
			TextFlow textFlow = new TextFlow(text);
			commentTypeGrid.addRow(index, label, textFlow);
			index++;
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
