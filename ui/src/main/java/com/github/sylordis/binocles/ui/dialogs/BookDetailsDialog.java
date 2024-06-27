package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.components.CustomListCell;
import com.github.sylordis.binocles.ui.functional.ListenerValidator;

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
	 * @param model
	 */
	public BookDetailsDialog(BinoclesModel model) {
		super("Create a new book", model);
		setIcon(AppIcons.ICON_BOOK);
		setHeader("Please indicate the name of the new book and its nomenclature (if any)");
	}

	@Override
	public void build() {
		// Book name fields
		Label labelBookName = new Label("Book name");
		fieldBookName = new TextField();
		// Nomenclature fields
		Label labelNomenclature = new Label("Nomenclature (optional)");
		fieldNomenclatureChoice = new ComboBox<>(FXCollections.observableArrayList(getModel().getNomenclatures()));
		// Set dialog content
		addFormFeedback();
		getGridPane().addRow(1, labelBookName, fieldBookName);
		getGridPane().addRow(2, labelNomenclature, fieldNomenclatureChoice);
		// Set up listeners
		ListenerValidator<String> bookNameUIValidator = new ListenerValidator<String>()
		        .validIf("Book name can't be blank or empty.", (o, n) -> !n.isBlank())
		        .validIf("Book with the same name already exists (case insensitive)", (o, n) -> !getModel().hasBook(n))
		        .feed(this::setFeedback).onEither(b -> setConfirmButtonDisable(!b));
		fieldBookName.textProperty().addListener(bookNameUIValidator);
		// Set up components status
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(true);
		bookNameUIValidator.changed(null, null, fieldBookName.getText());
		fieldNomenclatureChoice.setButtonCell(new CustomListCell<Nomenclature>(b -> b.getName()));
		fieldNomenclatureChoice.setCellFactory(p -> {
			return new CustomListCell<>(b -> b.getName());
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
