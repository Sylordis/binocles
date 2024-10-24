package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.components.CustomListCell;
import com.github.sylordis.binocles.ui.functional.ListenerValidator;
import com.github.sylordis.binocles.utils.contracts.Identifiable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Dialog to create a new or edit a book.
 * 
 * @author sylordis
 *
 */
public class BookDetailsDialog extends AbstractAnswerDialog<Book> {

	/**
	 * Book being edited, null if being created.
	 */
	private Book book;
	/**
	 * Text field for the book name.
	 */
	private TextField fieldBookName;
	/**
	 * Combo box for the nomenclature.
	 */
	private ComboBox<Nomenclature> fieldNomenclatureChoice;

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model model for reference
	 */
	public BookDetailsDialog(BinoclesModel model) {
		this(model, null);
	}

	/**
	 * Creates a book edit dialog.
	 * 
	 * @param model model for reference
	 * @param book  Book being edited
	 */
	public BookDetailsDialog(BinoclesModel model, Book book) {
		super(book == null ? "Create a new book" : "Editing book \"" + book.getTitle() + "\"", model);
		setIcon(AppIcons.ICON_BOOK);
		setHeader("Please indicate the name of the book and its nomenclature (if any).");
		this.book = book;
	}

	@Override
	public void build() {
		// Book name fields
		Label labelBookName = new Label("Book name");
		fieldBookName = new TextField();
		// Nomenclature fields
		Label labelNomenclature = new Label("Nomenclature (optional)");
		fieldNomenclatureChoice = new ComboBox<>(FXCollections.observableArrayList(getModel().getNomenclatures()));
		// TODO Description
		// TODO Synopsis
		// TODO Metadata
		// Set dialog content
		addFormFeedback();
		getGridPane().addRow(1, labelBookName, fieldBookName);
		getGridPane().addRow(2, labelNomenclature, fieldNomenclatureChoice);
		// Set up listeners
		ListenerValidator<String> bookNameUIValidator = new ListenerValidator<String>()
		        .validIf("Book name can't be blank or empty.", (o, n) -> !n.isBlank())
		        .validIf("Book with the same name already exists (case insensitive)",
		                (o, n) -> Identifiable.checkNewNameUniquenessValidityAmongParent(n, getModel(), book,
		                        (p, s) -> p.hasBook(s)))
		        .feed(this::setFeedback).onEither(b -> setConfirmButtonDisable(!b));
		fieldBookName.textProperty().addListener(bookNameUIValidator);
		// Set up components status
		if (book != null) {
			fieldBookName.setText(book.getTitle());
			fieldNomenclatureChoice.getSelectionModel().select(book.getNomenclature());
			fieldNomenclatureChoice.setDisable(true);
		}
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(true);
		bookNameUIValidator.changed(null, null, fieldBookName.getText());
		fieldNomenclatureChoice.setButtonCell(new CustomListCell<Nomenclature>(n -> n.getName()));
		fieldNomenclatureChoice.setCellFactory(p -> {
			return new CustomListCell<>(n -> n.getName());
		});
		// Focus on book name field
		Platform.runLater(() -> fieldBookName.requestFocus());
	}

	@Override
	public Book convertResult(ButtonType button) {
		Book answer = null;
		if (button.equals(getConfirmButton())) {
			answer = new Book(fieldBookName.getText().trim());
			Nomenclature nomenclature = fieldNomenclatureChoice.getSelectionModel().getSelectedItem();
			answer.setNomenclature(nomenclature == null ? getModel().getDefaultNomenclature() : nomenclature);
		}
		return answer;
	}

}
