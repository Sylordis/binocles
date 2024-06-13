package com.github.sylordis.binocles.ui.dialogs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.CommentTypeField;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.components.CustomListCell;
import com.github.sylordis.binocles.ui.doa.CommentDetailsAnswer;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Dialog to create or edit a comment. When editing, the nomenclature should be fixed.
 */
public class CommentDetailsDialog extends AbstractAnswerDialog<CommentDetailsAnswer> {

	private static final int ROW_FIELDS_START = 6;

	private Book book;
	private Chapter chapter;
	private int start;
	private int end;

	private ComboBox<CommentType> fieldCommentTypeChoice;
	private TextFlow fieldTextExcerpt;
	private Map<String, TextInputControl> fieldsFields;

	public CommentDetailsDialog(BinoclesModel model, Book book, Chapter chapter, int start, int end) {
		super("Comment details", model);
		setIcon(AppIcons.ICON_COMMENT);
		this.book = book;
		this.chapter = chapter;
		this.start = start;
		this.end = end;
		fieldsFields = new LinkedHashMap<>();
	}

	@Override
	protected void build() {
		// Create components
		Label labelChapter = new Label("Chapter");
		Text fieldChapter = new Text(this.chapter.getTitle());
		Label labelNomenclature = new Label("Nomenclature");
		Nomenclature nomenclature = book.getNomenclature() == null ? getModel().getDefaultNomenclature()
		        : book.getNomenclature();
		Text fieldNomenclature = new Text(nomenclature.getName());
		Label labelCommentType = new Label("Comment type");
		fieldCommentTypeChoice = new ComboBox<CommentType>(FXCollections.observableList(nomenclature.getTypes()));
		Label labelTextExcerpt = new Label("Excerpt");
		fieldTextExcerpt = new TextFlow();
		fieldTextExcerpt.getChildren().add(new Text(chapter.getText().substring(start, end)));
		// TODO All fields according to comment type
		// Set dialog content
		addFormFeedback();
		getGridPane().addRow(1, labelChapter, fieldChapter);
		getGridPane().addRow(2, labelNomenclature, fieldNomenclature);
		getGridPane().addRow(3, labelCommentType, fieldCommentTypeChoice);
		getGridPane().addRow(4, labelTextExcerpt);
		GridPane.setColumnSpan(labelTextExcerpt, GridPane.REMAINING);
		getGridPane().addRow(5, fieldTextExcerpt);
		GridPane.setColumnSpan(fieldTextExcerpt, GridPane.REMAINING);
		// Setup listeners
		ChangeListener<? super CommentType> fieldCommentTypeChoiceListener = (c,o,n) -> {
			removeCommentTypeFields();
			recreateCommentTypeFields(n);
		};
		fieldCommentTypeChoice.valueProperty().addListener(fieldCommentTypeChoiceListener);
		// TODO Set feedback collectors & form validators
		//
		// Styling and component status
		fieldCommentTypeChoice.setButtonCell(new CustomListCell<CommentType>(b -> b.getName()));
		fieldCommentTypeChoice.setCellFactory(p -> {
			return new CustomListCell<>(b -> b.getName());
		});
		// Actions and runnables
		fieldCommentTypeChoiceListener.changed(null, null, fieldCommentTypeChoice.getSelectionModel().getSelectedItem());
//		Platform.runLater(() -> fieldName.requestFocus()); TODO
	}

	private void removeCommentTypeFields() {
		getGridPane().getChildren().removeIf(node -> GridPane.getRowIndex(node) >= ROW_FIELDS_START);
		fieldsFields.clear();
	}

	private void recreateCommentTypeFields(CommentType type) {
		if (type != null) {
			int row = ROW_FIELDS_START;
			for (Entry<String, CommentTypeField> entry : type.getFields().entrySet()) {
				Label label = new Label(entry.getKey());
				if (entry.getValue().isLongText()) {
					TextArea textField = new TextArea();
					fieldsFields.put(entry.getKey(), textField);
					getGridPane().addRow(row, label);
					GridPane.setColumnSpan(label, GridPane.REMAINING);
					getGridPane().addRow(++row, textField);
					GridPane.setColumnSpan(textField, GridPane.REMAINING);
				} else {
					TextField textField = new TextField();
					fieldsFields.put(entry.getKey(), textField);
					getGridPane().addRow(row, label, textField);
				}
				row++;
			}
		}
	}

	@Override
	public CommentDetailsAnswer convertResult(ButtonType button) {
		// TODO Auto-generated method stub
		return new CommentDetailsAnswer();
	}

}
