package com.github.sylordis.binocles.ui.dialogs;

import java.util.Optional;

import com.github.sylordis.binocles.model.BinoclesModel;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Class to create a custom dialog with a complex return type.
 * 
 * @author sylordis
 * @param R type of the answer
 *
 */
public abstract class CustomDialog<R> {

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
	 * Creates a new custom dialog.
	 * 
	 * @param model
	 */
	public CustomDialog(String title, BinoclesModel model) {
		this.title = title;
		this.model = model;
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
		} else
			confirmButton = ButtonType.OK;
		dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);
		// Configure icon if set
		if (null != icon) {
			Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(icon);
		}
		// Set grid
		gridPane = new GridPane();
		gridPane.getStyleClass().add("form");

		build();

		dialog.getDialogPane().setContent(gridPane);
		// Set dialog return
		dialog.setResultConverter(this::convertResult);
		return dialog.showAndWait();
	}

	/**
	 * Builds the content of the dialog.
	 * 
	 * @see #getGridPane()
	 */
	protected abstract void build();

	/**
	 * Converts the result
	 * 
	 * @param button
	 * @return
	 */
	public abstract R convertResult(ButtonType button);

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
	public CustomDialog<R> header(String header) {
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
	 * Sets the disabled status of the confirm button.
	 * 
	 * @param disable
	 */
	protected void setConfirmButtonDisable(boolean disable) {
		getDialog().getDialogPane().lookupButton(getConfirmButton()).setDisable(disable);
	}

}
