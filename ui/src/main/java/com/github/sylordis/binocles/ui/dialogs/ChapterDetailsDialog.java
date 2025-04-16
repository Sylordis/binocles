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
import com.github.sylordis.binocles.utils.contracts.Identifiable;

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
 * @author Sylordis
 *
 */
public class ChapterDetailsDialog extends AbstractAnswerDialog<ChapterPropertiesAnswer> {

	/**
	 * Current chapter for edit.
	 */
	private Chapter chapter;
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
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Creates a new chapter creation dialog.
	 * 
	 * @param model current model being edited for other data collection
	 * @param book  current book where the chapter would be added
	 */
	public ChapterDetailsDialog(BinoclesModel model, Book book) {
		this(model, book, null);
	}

	/**
	 * Creates a chapter editing dialog.
	 * 
	 * @param model   current model being edited for other data collection
	 * @param book    current book where the chapter would be added
	 * @param chapter chapter being edited
	 */
	public ChapterDetailsDialog(BinoclesModel model, Book book, Chapter chapter) {
		super(chapter == null ? "Adding a new chapter" : "Editing chapter \"" + chapter.getTitle() + "\"", model);
		this.book = book;
		this.chapter = chapter;
		setIcon(AppIcons.ICON_CHAPTER);
		setHeader("Please indicate the name of the chapter in the chosen book.");
	}

	@Override
	protected void build() {
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
		int row = 0;
		addFormFeedback();
		getGridPane().addRow(++row, labelBookChoice, fieldBookParent);
		getGridPane().addRow(++row, labelChapterName, fieldChapterName);
		getGridPane().addRow(++row, labelChapterContent);
		GridPane.setColumnSpan(labelChapterContent, GridPane.REMAINING);
		getGridPane().addRow(++row, fieldChapterContent);
		GridPane.setColumnSpan(fieldChapterContent, GridPane.REMAINING);
		// Set up listeners
		ListenerValidator<String> chapterNameUIValidator = new ListenerValidator<String>()
				.link(fieldChapterName)
		        .validIf("Chapter name cannot be blank or empty.", (o, n) -> !n.isBlank())
		        .validIf("Chapter with the same name already exists in the book (case insensitive).",
		                (o, n) -> Identifiable.checkNewNameUniquenessValidityAmongParent(n, book, chapter,
		                        (b, s) -> b.hasChapter(s)))
		        .feed(getFormCtrl()::feedback)
		        .onEither(getFormCtrl()::valid).andThen(this::updateFormStatus);
		ListenerValidator<Book> bookParentUIValidator = new ListenerValidator<Book>()
				.link(fieldBookParent)
		        .validIf("You have to pick a book to add this chapter to.", (o, n) -> null != n)
		        .feed(getFormCtrl()::feedback).onEither((o,b) -> {
			        this.book = fieldBookParent.getSelectionModel().getSelectedItem();
			        getFormCtrl().valid(o, b);
			        chapterNameUIValidator.changed(null, null, fieldChapterName.getText());
			        logger.debug("Book parent changed, new={}", fieldBookParent.getSelectionModel().getSelectedItem());
		        }).andThen(this::updateFormStatus);
		ListenerValidator<String> chapterContentUIValidator = new ListenerValidator<String>()
				.link(fieldChapterContent)
		        .validIf("Chapter content cannot be blank or empty.", (o, n) -> !n.isBlank())
		        .feed(getFormCtrl()::feedback).onEither(getFormCtrl()::valid)
		        .andThen(this::updateFormStatus);
		fieldBookParent.selectionModelProperty().addListener((opt, o, n) -> logger
		        .debug("Book parent changed, old={}, new={}", n.getSelectedItem(), o.getSelectedItem()));
		fieldBookParent.valueProperty().addListener(bookParentUIValidator);
		fieldChapterName.textProperty().addListener(chapterNameUIValidator);
		fieldChapterContent.textProperty().addListener(chapterContentUIValidator);
		// Feedback setup
		getFormCtrl().register(fieldBookParent, fieldChapterName, fieldChapterContent);
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
		// If edit, set components values
		if (chapter != null) {
			labelBookChoice.setText("Book");
			fieldBookParent.setDisable(false);
			fieldChapterName.setText(chapter.getTitle());
			fieldChapterContent.setText(chapter.getContent());
			fieldChapterContent.setEditable(false);
		}
		// Fire listeners
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

}
