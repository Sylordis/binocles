package com.github.sylordis.binocle.ui.dialogs;

import java.util.Optional;

import com.github.sylordis.binocle.model.BinocleModel;
import com.github.sylordis.binocle.model.review.Nomenclature;
import com.github.sylordis.binocle.ui.AppIcons;
import com.github.sylordis.binocle.ui.answers.CreateBookAnswer;
import com.github.sylordis.binocle.ui.listeners.TextListenerValidator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * 
 * @author sylordis
 *
 */
public class CreateBookDialog extends CustomDialog<CreateBookAnswer> {

	/**
	 * Text field for the book name.
	 */
	private TextField fieldBookName;
	/**
	 * Combo box for the nomenclature.
	 */
	private ComboBox<Nomenclature> fieldNomenclatureChoice;
	/**
	 * User feedback.
	 */
	private Text formFeedback;

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model
	 */
	public CreateBookDialog(BinocleModel model) {
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
		// Other fields
		formFeedback = new Text("");
		formFeedback.getStyleClass().add("text-danger");
		// Set dialog content
		getGridPane().addRow(0, formFeedback);
		GridPane.setColumnSpan(formFeedback, GridPane.REMAINING);
		getGridPane().addRow(1, labelBookName, fieldBookName);
		getGridPane().addRow(2, labelNomenclature, fieldNomenclatureChoice);
		// Set up listeners
		TextListenerValidator bookNameUIValidator = new TextListenerValidator()
		        .error("Book name can't be blank or empty.", (o, n) -> n.isBlank())
		        .error("Book with the same name already exists (case insensitive)", (o, n) -> getModel().hasBook(n))
		        .feed(formFeedback::setText)
		        .onEither(b -> setConfirmButtonDisable(!b));
		fieldBookName.textProperty().addListener(bookNameUIValidator);
		// Set up components status
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(true);
		// Focus on book name field
		Platform.runLater(() -> fieldBookName.requestFocus());
	}

	@Override
	public CreateBookAnswer convertResult(ButtonType button) {
		CreateBookAnswer answer = null;
		if (button.equals(getConfirmButton())) {
			answer = new CreateBookAnswer(fieldBookName.getText(),
			        Optional.ofNullable(fieldNomenclatureChoice.getSelectionModel().getSelectedItem()));
		}
		return answer;
	}

}
