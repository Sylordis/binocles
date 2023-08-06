package com.github.sylordis.binocle.ui.dialogs;

import com.github.sylordis.binocle.model.BinocleModel;
import com.github.sylordis.binocle.model.text.Book;
import com.github.sylordis.binocle.ui.AppIcons;
import com.github.sylordis.binocle.ui.answers.CreateChapterAnswer;
import com.github.sylordis.binocle.ui.listeners.TextListenerValidator;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
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

	private String chapterNameFeedback = "";
	private String chapterContentFeedback = "";
	private boolean chapterNameChecks = false;
	private boolean chapterContentChecks = false;

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model
	 */
	public CreateChapterDialog(BinocleModel model, Book book) {
		super("Adding a new chapter", model);
		setIcon(AppIcons.ICON_CHAPTER);
		setHeader("Please indicate the name of the new chapter of '" + book.getTitle() + "'");
	}

	@Override
	public void build() {
		// Chapter title fields
		Label labelBookName = new Label("Chapter name");
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
		getGridPane().addRow(1, labelBookName, fieldChapterName);
		getGridPane().addRow(2, labelChapterContent);
		GridPane.setColumnSpan(labelChapterContent, GridPane.REMAINING);
		getGridPane().addRow(3, fieldChapterContent);
		GridPane.setColumnSpan(fieldChapterContent, GridPane.REMAINING);
		// Set up listeners
		TextListenerValidator chapterNameUIValidator = new TextListenerValidator()
		        .error("Chapter name can't be blank or empty.", (o, n) -> n.isBlank())
		        .error("Chapter with the same name already exists in the book (case insensitive).",
		                (o, n) -> book.hasChapter(n))
		        .feed(s -> {
			        this.chapterNameFeedback = s;
			        combineFeedbacks();
		        }).onEither(b -> {
			        this.chapterNameChecks = b;
			        checkConfirmButtonDisabled();
		        });
		TextListenerValidator chapterContentUIValidator = new TextListenerValidator()
				.error("Chapter content cannot be blank or empty.", (o,n) -> n.isBlank())
				.feed(s -> {
					this.chapterContentFeedback = s;
					combineFeedbacks();
				}).onEither(b -> {
					this.chapterContentChecks = b;
					checkConfirmButtonDisabled();
				});
		fieldChapterName.textProperty().addListener(chapterNameUIValidator);
		fieldChapterContent.textProperty().addListener(chapterContentUIValidator);
		// Set up components status
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(true);
		// Focus on book name field
		Platform.runLater(() -> fieldChapterName.requestFocus());
	}

	/**
	 * Sets the disabling of the confirm button on all checks.
	 */
	public void checkConfirmButtonDisabled() {
		setConfirmButtonDisable(!this.chapterContentChecks && !this.chapterNameChecks);
	}

	@Override
	public CreateChapterAnswer convertResult(ButtonType button) {
		CreateChapterAnswer answer = null;
		if (button.equals(getConfirmButton())) {
			answer = new CreateChapterAnswer(fieldChapterName.getText(), fieldChapterContent.getText());
		}
		return answer;
	}

	/**
	 * Combines both feedback from fields into one.
	 */
	private void combineFeedbacks() {
		StringBuilder collect = new StringBuilder(chapterNameFeedback);
		if (!collect.isEmpty())
			collect.append("\n");
		collect.append(chapterContentFeedback);
		formFeedback.setText(collect.toString());
	}
}
