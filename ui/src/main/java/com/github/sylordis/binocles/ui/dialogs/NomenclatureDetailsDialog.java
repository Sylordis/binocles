package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.functional.ListenerValidator;
import com.github.sylordis.binocles.utils.contracts.Identifiable;

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
public class NomenclatureDetailsDialog extends AbstractAnswerDialog<Nomenclature> {

	/**
	 * Text field for the book name.
	 */
	private TextField fieldNomenclatureTitle;
	/**
	 * Current nomenclature for edit.
	 */
	private Nomenclature nomenclature;

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model
	 */
	public NomenclatureDetailsDialog(BinoclesModel model) {
		this(model, null);
	}

	/**
	 * Creates a new book editing dialog.
	 * 
	 * @param model
	 * @param nomenclature nomenclature for edition, null for new object
	 */
	public NomenclatureDetailsDialog(BinoclesModel model, Nomenclature nomenclature) {
		super(nomenclature == null ? "Create a new nomenclature"
		        : "Editing nomenclature \"" + nomenclature.getName() + "\"", model);
		setIcon(AppIcons.ICON_NOMENCLATURE);
		setHeader("Please indicate the name of the nomenclature.");
		this.nomenclature = nomenclature;
	}

	@Override
	public void build() {
		// Title fields
		Label labelTitle = new Label("Title");
		fieldNomenclatureTitle = new TextField();
		// Set dialog content
		if (nomenclature != null) {
			fieldNomenclatureTitle.setText(nomenclature.getName());
		}
		addFormFeedback();
		getGridPane().addRow(1, labelTitle, fieldNomenclatureTitle);
		// Set up listeners
		ListenerValidator<String> nomenclatureNameUIValidator = new ListenerValidator<String>()
		        .link(fieldNomenclatureTitle)
		        .validIf("Nomenclature name can't be blank or empty.", (o, n) -> !n.isBlank())
		        .validIf("Nomenclature with the same name already exists (case insensitive)",
		                (o, n) -> Identifiable.checkNewNameUniquenessValidityAmongParent(n, getModel(), nomenclature,
		                        (p, s) -> p.hasNomenclature(s)))
		        .feed(getFormCtrl()::feedback).onEither(getFormCtrl()::valid).andThen(this::updateFormStatus);
		fieldNomenclatureTitle.textProperty().addListener(nomenclatureNameUIValidator);
		// Set up components status
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(true);
		nomenclatureNameUIValidator.changed(null, null, fieldNomenclatureTitle.getText());
		// Focus on book name field
		Platform.runLater(() -> fieldNomenclatureTitle.requestFocus());
	}

	@Override
	public Nomenclature convertResult(ButtonType button) {
		Nomenclature answer = null;
		if (button.equals(getConfirmButton())) {
			answer = new Nomenclature(fieldNomenclatureTitle.getText().trim());
		}
		return answer;
	}

}
