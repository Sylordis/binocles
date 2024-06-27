package com.github.sylordis.binocles.ui.dialogs;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.components.CustomListCell;
import com.github.sylordis.binocles.ui.doa.ChapterPropertiesAnswer;
import com.github.sylordis.binocles.ui.functional.ListenerValidator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Dialog to create a new or edit a chapter.
 * 
 * @author sylordis
 *
 */
public class ChapterDetailsDialog extends AbstractAnswerDialog<ChapterPropertiesAnswer> {

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
	 * Choice of book, by default set on current book.
	 */
	private ComboBox<Book> fieldBookParent;

	/**
	 * Error feedback for the book choice field.
	 */
	private String bookParentFeedback = "";
	/**
	 * Error feedback for the name field.
	 */
	private String chapterNameFeedback = "";
	/**
	 * Error feedback for the content field.
	 */
	private String chapterContentFeedback = "";
	/**
	 * Validity of the book choice's input. False means there's an error (not valid).
	 */
	private boolean bookParentValid = false;
	/**
	 * Validity of the name field's input. False means there's an error (not valid).
	 */
	private boolean chapterNameValid = false;
	/**
	 * Validity status of the content field's input. False means there's an error (not valid).
	 */
	private boolean chapterContentValid = false;

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model
	 */
	public ChapterDetailsDialog(BinoclesModel model, Book book) {
		super("Adding a new chapter", model);
		this.book = book;
		setIcon(AppIcons.ICON_CHAPTER);
		setHeader("Please indicate the name of the new chapter in the chosen book");
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
		// Set dialog content
		addFormFeedback();
		getGridPane().addRow(1, labelBookChoice, fieldBookParent);
		getGridPane().addRow(2, labelChapterName, fieldChapterName);
		getGridPane().addRow(3, labelChapterContent);
		GridPane.setColumnSpan(labelChapterContent, GridPane.REMAINING);
		getGridPane().addRow(4, fieldChapterContent);
		GridPane.setColumnSpan(fieldChapterContent, GridPane.REMAINING);
		// Set up listeners
		ListenerValidator<Book> bookParentUIValidator = new ListenerValidator<Book>()
		        .validIf("You have to pick a book to add this chapter to.", (o, n) -> null != n)
		        .feed(this::setBookParentFeedback).onEither(b -> {
			        this.book = fieldBookParent.getSelectionModel().getSelectedItem();
			        setBookParentValidity(b);
			        logger.debug("Book parent changed, new={}", fieldBookParent.getSelectionModel().getSelectedItem());
		        }).andThen(this::updateFormStatus);
		ListenerValidator<String> chapterNameUIValidator = new ListenerValidator<String>()
		        .validIf("Chapter name cannot be blank or empty.", (o, n) -> !n.isBlank())
		        .validIf("Chapter with the same name already exists in the book (case insensitive).",
		                (o, n) -> this.book == null || !book.hasChapter(n))
		        .feed(this::setChapterNameFeedback).onEither(this::setChapterNameValid).andThen(this::updateFormStatus);
		ListenerValidator<String> chapterContentUIValidator = new ListenerValidator<String>()
		        .validIf("Chapter content cannot be blank or empty.", (o, n) -> !n.isBlank())
		        .feed(this::setChapterContentFeedback).onEither(this::setChapterContentValid)
		        .andThen(this::updateFormStatus);
		fieldBookParent.selectionModelProperty().addListener((opt, o, n) -> logger
		        .debug("Book parent changed, old={}, new={}", n.getSelectedItem(), o.getSelectedItem()));
		fieldBookParent.valueProperty().addListener(bookParentUIValidator);
		fieldChapterName.textProperty().addListener(chapterNameUIValidator);
		fieldChapterContent.textProperty().addListener(chapterContentUIValidator);
		// Feedback setup
		addFeedbackCollector(() -> bookParentFeedback);
		addFeedbackCollector(() -> chapterNameFeedback);
		addFeedbackCollector(() -> chapterContentFeedback);
		addFormValidator(() -> bookParentValid);
		addFormValidator(() -> chapterContentValid);
		addFormValidator(() -> chapterNameValid);
		// Set up components status
		setConfirmButtonDisable(true);
		fieldBookParent.setButtonCell(new CustomListCell<Book>(b -> b.getTitle()));
		fieldBookParent.setCellFactory(p -> {
			return new CustomListCell<>(b -> b.getTitle());
		});
		if (this.book != null) {
			fieldBookParent.getSelectionModel().select(this.book);
		} else {
			bookParentUIValidator.changed(null, null, null);
		}
		chapterNameUIValidator.changed(null, null, fieldChapterName.getText());
		chapterContentUIValidator.changed(null, null, fieldChapterContent.getText());
		// Focus on book name field
		Platform.runLater(() -> fieldChapterName.requestFocus());
	}

	@Override
	public ChapterPropertiesAnswer convertResult(ButtonType button) {
		ChapterPropertiesAnswer answer = null;
		if (button.equals(getConfirmButton())) {
			Chapter chapter = new Chapter(fieldChapterName.getText().trim(), fieldChapterContent.getText());
			Book parentBook = fieldBookParent.getSelectionModel().getSelectedItem();
			answer = new ChapterPropertiesAnswer(parentBook, chapter);
		}
		return answer;
	}

	/**
	 * @param valid the bookParentValid to set
	 */
	public void setBookParentValidity(boolean valid) {
		this.bookParentValid = valid;
	}

	/**
	 * @param feedback the bookParentFeedback to set
	 */
	public void setBookParentFeedback(String feedback) {
		this.bookParentFeedback = feedback;
	}

	/**
	 * @param valid the chapterContentValid to set
	 */
	public void setChapterContentValid(boolean valid) {
		this.chapterContentValid = valid;
	}

	/**
	 * @param feedback the chapterContentFeedback to set
	 */
	public void setChapterContentFeedback(String feedback) {
		this.chapterContentFeedback = feedback;
	}

	/**
	 * @param valid the chapterNameValid to set
	 */
	public void setChapterNameValid(boolean valid) {
		this.chapterNameValid = valid;
	}

	/**
	 * @param feedback the chapterNameFeedback to set
	 */
	public void setChapterNameFeedback(String feedback) {
		this.chapterNameFeedback = feedback;
	}

}
