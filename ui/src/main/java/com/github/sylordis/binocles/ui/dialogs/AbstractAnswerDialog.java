package com.github.sylordis.binocles.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.sylordis.binocles.model.BinoclesModel;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class to create a custom dialog with a complex return type.
 * 
 * @author sylordis
 * @param R type of the answer
 *
 */
public abstract class AbstractAnswerDialog<R> implements Displayable<Optional<R>> {

	/**
	 * Dialog.
	 */
	private Dialog<R> dialog;
	/**
	 * Layout of the dialog.
	 */
	private GridPane gridPane;
	/**
	 * Model affected by the dialog.
	 */
	private BinoclesModel model;
	/**
	 * Confirm button of the dialog, automatically instantiated if {@link #confirmButtonText} is
	 * provided.
	 */
	private ButtonType confirmButton;
	/**
	 * Title of the dialog.
	 */
	private String title;
	/**
	 * Header of the dialog, automatically configured if provided.
	 */
	private String header;
	/**
	 * Text of the confirm button, defaults to standard OK otherwise, automatically configured if
	 * provided.
	 */
	private String confirmButtonText;
	/**
	 * Icon of the dialog, automatically configured if provided.
	 */
	private Image icon;
	/**
	 * All suppliers to gather feedback when needed.
	 * 
	 * @see #combineFeedbacks()
	 */
	private List<Supplier<String>> feedbackCollectors;
	/**
	 * Suppliers that check the current dialog validity. A field is valid when its validator returns
	 * true.
	 */
	private List<Supplier<Boolean>> formValidators;

	/**
	 * User feedback, only set if {@link #addFormFeedback(int)} is called.
	 */
	private Text formFeedback;

	/**
	 * Creates a new custom dialog.
	 * 
	 * @param model
	 */
	public AbstractAnswerDialog(String title, BinoclesModel model) {
		this.title = title;
		this.model = model;
		feedbackCollectors = new ArrayList<>();
		formValidators = new ArrayList<>();
	}

	/**
	 * Set up the dialog, display it and wait for user action. It is the last method to be called.
	 * 
	 * @return
	 */
	public Optional<R> display() {
		dialog = new Dialog<>();
		dialog.getDialogPane().getStylesheets().add(getClass().getResource("../styles/main.css").toExternalForm());
		dialog.setTitle(title);
		// Configure header if provided
		dialog.setHeaderText(header);
		// Configure buttons, OK with provided or default if not
		if (null != confirmButtonText && !confirmButtonText.isBlank()) {
			confirmButton = new ButtonType(confirmButtonText, ButtonData.OK_DONE);
		} else {
			confirmButton = ButtonType.OK;
		}
		dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);
		// Configure icon if set
		if (null != icon) {
			Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(icon);
		}
		// Set grid
		gridPane = new GridPane();
		gridPane.getStyleClass().add("form-dialog");

		build();

		dialog.getDialogPane().setContent(gridPane);
		getDialog().getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
		// Set dialog return
		dialog.setResultConverter(this::convertResult);
		return dialog.showAndWait();
	}

	/**
	 * Resize the dialog to current scene.
	 */
	public void sizeToScene() {
		getDialog().getDialogPane().getScene().getWindow().sizeToScene();
	}

	/**
	 * Builds the content of the dialog.
	 * 
	 * @see #getGridPane()
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
	 * Checks all validators set in this dialog and returns a final answer.
	 * 
	 * @return
	 */
	public boolean checkFormValidity() {
		boolean validity = true;
		for (Supplier<Boolean> validator : formValidators) {
			validity = validity && validator.get();
		}
		return validity;
	}

	/**
	 * Creates a resulting object wrapped in an {@link Optional} that can be processed by the calling
	 * class and as a result of the {@link Dialog#showAndWait()} method. A <code>null</code> result
	 * indicates a cancellation.<br/>
	 * If you implement this on a normal dialog that can be cancelled, do not forget to check for which
	 * button comes the action with:
	 * 
	 * <pre>
	 * <code>
	 * if (button.equals(getConfirmButton())) { .. }
	 * </code>
	 * </pre>
	 * 
	 * @param button the button triggering the dialog's state (close, accept, ...)
	 * @return null if no object can be processed (cancellation of a dialog for example), the expected object
	 *         otherwise
	 */
	public abstract R convertResult(ButtonType button);

	/**
	 * Combines all feedback from the collectors and consumes it.
	 * 
	 * @param consumer
	 */
	public void combineAndProcessFeedback(Consumer<String> consumer) {
		StringBuilder collect = new StringBuilder();
		for (Supplier<String> s : feedbackCollectors) {
			if (!collect.isEmpty())
				collect.append("\n");
			collect.append(s.get());
		}
		consumer.accept(collect.toString());
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
		setConfirmButtonDisabledOnValidity();
		combineAndProcessFeedback(formFeedback::setText);
		sizeToScene();
	}

	/**
	 * @return the dialog
	 */
	protected Dialog<R> getDialog() {
		return this.dialog;
	}

	/**
	 * Gets the grid pane.
	 * 
	 * @return
	 */
	protected GridPane getGridPane() {
		return this.gridPane;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Sets the header text.
	 * 
	 * @param header
	 * @return itself for chain calls
	 */
	public AbstractAnswerDialog<R> header(String header) {
		setHeader(header);
		return this;
	}

	/**
	 * @return the confirmButtonText
	 */
	public String getConfirmButtonText() {
		return confirmButtonText;
	}

	/**
	 * @return the confirm button
	 */
	protected ButtonType getConfirmButton() {
		return confirmButton;
	}

	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Image icon) {
		this.icon = icon;
	}

	/**
	 * @return the model
	 */
	public BinoclesModel getModel() {
		return this.model;
	}

	/**
	 * @return the feedbackCollectors
	 */
	public List<Supplier<String>> getFeedbackCollectors() {
		return feedbackCollectors;
	}

	/**
	 * @param feedbackCollectors the feedbackCollectors to set
	 */
	public void setFeedbackCollectors(List<Supplier<String>> feedbackCollectors) {
		this.feedbackCollectors = feedbackCollectors;
	}

	/**
	 * Adds a new feedback collector.
	 * 
	 * @param collector
	 */
	public void addFeedbackCollector(Supplier<String> collector) {
		feedbackCollectors.add(collector);
	}

	/**
	 * Adds multiple feedback collectors.
	 * 
	 * @param collectors
	 */
	public void addFeedbackCollectors(Collection<? extends Supplier<String>> collectors) {
		feedbackCollectors.addAll(collectors);
	}

	/**
	 * @return the formValidators
	 */
	public List<Supplier<Boolean>> getFormValidators() {
		return formValidators;
	}

	/**
	 * @param formValidators the formValidators to set
	 */
	public void setFormValidators(List<Supplier<Boolean>> formValidators) {
		this.formValidators = formValidators;
	}

	/**
	 * Adds a new validator for this dialog.
	 * 
	 * @param validator
	 */
	public void addFormValidator(Supplier<Boolean> validator) {
		formValidators.add(validator);
	}

	/**
	 * Adds multiple validators for this dialog.
	 * 
	 * @param validators
	 */
	public void addFormValidators(Collection<? extends Supplier<Boolean>> validators) {
		formValidators.addAll(validators);
	}

	/**
	 * Sets the disabled status of the confirm button.
	 * 
	 * @param disable true to disable, false to enable
	 * @see Button#setDisable(boolean)
	 */
	public void setConfirmButtonDisable(boolean disable) {
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(disable);
	}

	/**
	 * Sets the confirmation button disabled property according to the form validity. If the form is
	 * valid, the button will be enabled, it will be disabled otherwise.
	 * 
	 * @see #checkFormValidity()
	 * @see #setConfirmButtonDisable(boolean)
	 */
	public void setConfirmButtonDisabledOnValidity() {
		setConfirmButtonDisable(!checkFormValidity());
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

}
