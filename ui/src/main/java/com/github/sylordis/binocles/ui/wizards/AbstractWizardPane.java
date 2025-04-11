package com.github.sylordis.binocles.ui.wizards;

import org.controlsfx.dialog.WizardPane;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.ui.components.FormValidationControl;

import javafx.scene.layout.GridPane;

public class AbstractWizardPane extends WizardPane {

	/**
	 * Manages all feedback and validation.
	 */
	protected FormValidationControl formCtrl;
	/**
	 * Model affected by the dialog.
	 */
	private BinoclesModel model;
	/**
	 * Layout of the dialog.
	 */
	private GridPane gridPane;

	public AbstractWizardPane(BinoclesModel model) {
		formCtrl = new FormValidationControl();
		this.model = model;
		gridPane = new GridPane();
		gridPane.getStyleClass().add("form-dialog");
	}

	/**
	 * @return the formCtrl
	 */
	protected FormValidationControl getFormCtrl() {
		return formCtrl;
	}

	/**
	 * @param formCtrl the formCtrl to set
	 */
	protected void setFormCtrl(FormValidationControl formCtrl) {
		this.formCtrl = formCtrl;
	}

	/**
	 * @return the model
	 */
	protected BinoclesModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	protected void setModel(BinoclesModel model) {
		this.model = model;
	}

	/**
	 * @return the gridPane
	 */
	protected GridPane getGridPane() {
		return gridPane;
	}

	/**
	 * @param gridPane the gridPane to set
	 */
	protected void setGridPane(GridPane gridPane) {
		this.gridPane = gridPane;
	}

}
