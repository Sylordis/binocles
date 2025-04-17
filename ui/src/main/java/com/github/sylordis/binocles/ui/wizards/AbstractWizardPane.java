package com.github.sylordis.binocles.ui.wizards;

import java.util.function.Consumer;

import org.controlsfx.dialog.WizardPane;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.ui.components.FormValidationControl;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Abstract class to manage base for Wizard panes. It handles a basic grid pane, the feedback and
 * validation if need be and has a link to the model.
 */
public abstract class AbstractWizardPane extends WizardPane {

	/**
	 * Manages all feedback and validation.
	 */
	protected FormValidationControl formCtrl;
	/**
	 * Model affected by the pane.
	 */
	private BinoclesModel model;
	/**
	 * Layout of the dialog.
	 */
	private GridPane gridPane;
	/**
	 * User feedback, only set if {@link #addFormFeedback(int)} is called.
	 */
	private Text formFeedback;

	/**
	 * Builds a new wizard pane.
	 * 
	 * @param model Model to get data from
	 */
	public AbstractWizardPane(BinoclesModel model) {
		formCtrl = new FormValidationControl();
		this.model = model;
	}

	/**
	 * To be called to construct the pane and all its components.
	 */
	public void construct() {
		gridPane = new GridPane();
		gridPane.getStyleClass().add("form-dialog");

		build();

		setContent(gridPane);
		setMinWidth(Region.USE_PREF_SIZE);
	}

	/**
	 * Call to build and set up the pane.
	 */
	protected abstract void build();

	/**
	 * Instantiates and adds the form feedback field on row 0.
	 * 
	 * @see #addFormFeedback(int)
	 */
	public void addFormFeedback() {
		addFormFeedback(0);
	}

	/**
	 * Instantiates and adds the form feedback field at the specified row.
	 */
	public void addFormFeedback(int row) {
		formFeedback = new Text("");
		formFeedback.getStyleClass().add("text-danger");
		getGridPane().addRow(row, formFeedback);
		GridPane.setColumnSpan(formFeedback, GridPane.REMAINING);
	}

	/**
	 * Default method to update the form status, triggering an update of the status of the confirm
	 * button and processes all feedback from the feedback collectors to set the feedback field. Check
	 * the "see also" to see which methods are called.
	 * 
	 * @see #setConfirmButtonDisabledOnValidity()
	 * @see #combineAndProcessFeedback(Consumer)
	 */
	public void updateFormStatus() {
		formCtrl.combineAndProcessFeedback(formFeedback::setText);
	}

	/**
	 * @return the formFeedback
	 */
	public void setFeedback(String feedback) {
		formFeedback.setText(feedback);
	}

	/**
	 * @return the formFeedback
	 */
	public Text getFormFeedback() {
		return formFeedback;
	}

	/**
	 * @return the formCtrl
	 */
	public FormValidationControl getFormCtrl() {
		return formCtrl;
	}

	/**
	 * @param formCtrl the formCtrl to set
	 */
	public void setFormCtrl(FormValidationControl formCtrl) {
		this.formCtrl = formCtrl;
	}

	/**
	 * @return the model
	 */
	public BinoclesModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(BinoclesModel model) {
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
