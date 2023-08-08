package com.github.sylordis.binocles.ui.dialogs;

import java.util.ArrayList;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.answers.CreateChapterAnswer;
import com.github.sylordis.binocles.ui.components.CustomListCell;
import com.github.sylordis.binocles.ui.listeners.ListenerValidator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Dialog to create a new chapter.
 * 
 * @author sylordis
 *
 */
public class CreateChapterDialog extends CustomDialog<CreateChapterAnswer> {

	/**
	 * Book to which add the chapter.
	 */
	private Book book;
	/**
	 * Text field for the book name.
	 */
	private TextField fieldChapterName;
	/**
	 * Text area for the chapter content..
	 */
	private TextArea fieldChapterContent;
	/**
	 * User feedback.
	 */
	private Text formFeedback;
	/**
	 * Choice of book, by default set on current book.
	 */
	private ComboBox<Book> fieldBookParent;

	/**
	 * Error feedback for the name field.
	 */
	private String chapterNameFeedback = "";
	/**
	 * Error feedback for the content field.
	 */
	private String chapterContentFeedback = "";
	/**
	 * Validity of the name field's input. False means there's an error (not valid).
	 */
	private boolean chapterNameValid = false;
	/**
	 * Validity status of the content field's input. False means there's an error (not valid).
	 */
	private boolean chapterContentValid = false;

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model
	 */
	public CreateChapterDialog(BinoclesModel model, Book book) {
		super("Adding a new chapter", model);
		this.book = book;
		setIcon(AppIcons.ICON_CHAPTER);
		setHeader("Please indicate the name of the new chapter of '" + book.getTitle() + "'");
	}

	@Override
	public void build() {
		// Book choice field
		Label labelBookChoice = new Label("Add chapter to...");
		fieldBookParent = new ComboBox<>(FXCollections.observableList(new ArrayList<>(getModel().getBooks())));
		// Chapter title fields
		Label labelChapterName = new Label("Chapter name");
		fieldChapterName = new TextField();
		// Content fields
		Label labelChapterContent = new Label("Content of the chapter");
		fieldChapterContent = new TextArea();
		// Other fields
		formFeedback = new Text("");
		formFeedback.getStyleClass().add("text-danger");
		// Set dialog content
		getGridPane().addRow(0, formFeedback);
		GridPane.setColumnSpan(formFeedback, GridPane.REMAINING);
		getGridPane().addRow(1, labelBookChoice, fieldBookParent);
		getGridPane().addRow(2, labelChapterName, fieldChapterName);
		getGridPane().addRow(3, labelChapterContent);
		GridPane.setColumnSpan(labelChapterContent, GridPane.REMAINING);
		getGridPane().addRow(4, fieldChapterContent);
		GridPane.setColumnSpan(fieldChapterContent, GridPane.REMAINING);
		// Set up listeners
		ListenerValidator<String> chapterNameUIValidator = new ListenerValidator<String>()
		        .error("Chapter name cannot be blank or empty.", (o, n) -> !n.isBlank())
		        .error("Chapter with the same name already exists in the book (case insensitive).",
		                (o, n) -> !book.hasChapter(n))
		        .feed(this::setChapterNameFeedback).onEither(this::setChapterNameError).andThen(this::postActions);
		ListenerValidator<String> chapterContentUIValidator = new ListenerValidator<String>()
		        .error("Chapter content cannot be blank or empty.", (o, n) -> !n.isBlank())
		        .feed(this::setChapterContentFeedback).onEither(this::setChapterContentError)
		        .andThen(this::postActions);
		fieldChapterName.textProperty().addListener(chapterNameUIValidator);
		fieldChapterContent.textProperty().addListener(chapterContentUIValidator);
		// Set up components status
		checkConfirmButtonDisabled();
		fieldBookParent.setButtonCell(new CustomListCell<Book>(b -> b.getTitle()));
		fieldBookParent.setCellFactory(p -> {
			return new CustomListCell<>(b -> b.getTitle());
		});
		fieldBookParent.getSelectionModel().select(book);
		// Focus on book name field
		Platform.runLater(() -> fieldChapterName.requestFocus());
	}

	/**
	 * Action to run post validation step.
	 */
	public void postActions() {
		checkConfirmButtonDisabled();
		combineFeedbacks();
	}

	/**
	 * Sets the disabling of the confirm button according to error status of all fields..
	 */
	public void checkConfirmButtonDisabled() {
		setConfirmButtonDisable(!(this.chapterContentValid && this.chapterNameValid));
	}

	@Override
	public CreateChapterAnswer convertResult(ButtonType button) {
		CreateChapterAnswer answer = null;
		if (button.equals(getConfirmButton())) {
			answer = new CreateChapterAnswer(fieldChapterName.getText().trim(), fieldChapterContent.getText(),
			        fieldBookParent.getValue());
		}
		return answer;
	}

	/**
	 * Combines both feedback from fields into one and sets the feedback field's content.
	 */
	private void combineFeedbacks() {
		StringBuilder collect = new StringBuilder(chapterNameFeedback);
		if (!collect.isEmpty())
			collect.append("\n");
		collect.append(chapterContentFeedback);
		formFeedback.setText(collect.toString());
	}

	/**
	 * @param error the chapterContentError to set
	 */
	public void setChapterContentError(boolean error) {
		this.chapterContentValid = error;
	}

	/**
	 * @param feedback the chapterContentFeedback to set
	 */
	public void setChapterContentFeedback(String feedback) {
		this.chapterContentFeedback = feedback;
	}

	/**
	 * @param error the chapterNameError to set
	 */
	public void setChapterNameError(boolean error) {
		this.chapterNameValid = error;
	}

	/**
	 * @param feedback the chapterNameFeedback to set
	 */
	public void setChapterNameFeedback(String feedback) {
		this.chapterNameFeedback = feedback;
	}

}
