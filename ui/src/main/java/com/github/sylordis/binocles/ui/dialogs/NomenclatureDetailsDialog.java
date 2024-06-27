package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.functional.ListenerValidator;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Dialog to create a new or edit a nomenclature.
 * 
 * @author sylordis
 *
 */
public class NomenclatureDetailsDialog extends AbstractAnswerDialog<String> {

	/**
	 * Text field for the book name.
	 */
	private TextField fieldNomenclatureTitle;

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model
	 */
	public NomenclatureDetailsDialog(BinoclesModel model) {
		super("Create a new nomenclature", model);
		setIcon(AppIcons.ICON_NOMENCLATURE);
		setHeader("Please indicate the name of the new nomenclature.");
	}

	@Override
	public void build() {
		// Title fields
		Label labelTitle = new Label("Title");
		fieldNomenclatureTitle = new TextField();
		// Set dialog content
		addFormFeedback();
		getGridPane().addRow(1, labelTitle, fieldNomenclatureTitle);
		// Set up listeners
		ListenerValidator<String> nomenclatureNameUIValidator = new ListenerValidator<String>()
		        .validIf("Nomenclature name can't be blank or empty.", (o, n) -> !n.isBlank())
		        .validIf("Nomenclature with the same name already exists (case insensitive)",
		                (o, n) -> !getModel().hasNomenclature(n))
		        .feed(this::setFeedback).onEither(b -> setConfirmButtonDisable(!b))
		        .andThen(this::sizeToScene);
		fieldNomenclatureTitle.textProperty().addListener(nomenclatureNameUIValidator);
		// Set up components status
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(true);
		nomenclatureNameUIValidator.changed(null, null, fieldNomenclatureTitle.getText());
		// Focus on book name field
		Platform.runLater(() -> fieldNomenclatureTitle.requestFocus());
	}

	@Override
	public String convertResult(ButtonType button) {
		String answer = null;
		if (button.equals(getConfirmButton())) {
			answer = fieldNomenclatureTitle.getText().trim();
		}
		return answer;
	}

}
