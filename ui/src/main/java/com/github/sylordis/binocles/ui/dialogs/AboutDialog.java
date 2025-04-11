package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.BinoclesConfiguration;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.contracts.Displayable;
import com.github.sylordis.binocles.ui.javafxutils.Browser;
import com.github.sylordis.binocles.ui.settings.BinoclesUIConstants;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Small dialog displaying the "About" information.
 * 
 * @author sylordis
 *
 */
public class AboutDialog implements Displayable<Void> {

	/**
	 * Main alert pane.
	 */
	private Alert alert;
	/**
	 * Layout of the alert pane.
	 */
	private GridPane gridPane;

	/**
	 * Builds a new alert dialog.
	 */
	public AboutDialog() {
		alert = new Alert(AlertType.NONE);
	}

	/**
	 * Builds and setups the pane and all its components.
	 */
	protected void build() {
		alert.getButtonTypes().add(ButtonType.CLOSE);

		// Set grid
		// Description
		Text descriptionField = new Text(BinoclesUIConstants.SOFTWARE_NAME
		        + " is a software for beta-reading. Commenting and reviewing made easy!");
		gridPane.addRow(0, AppIcons.createImageView(AppIcons.ICON_SOFTWARE, 64, 64), descriptionField);
		// Version
		gridPane.addRow(1, new Label("Version"), new Text(BinoclesConfiguration.getInstance().getVersion()));
		// Official website
		Hyperlink officialWebsiteLink = new Hyperlink(BinoclesUIConstants.WEBSITE_LINK);
		officialWebsiteLink.setOnAction(e -> new Browser().open(BinoclesUIConstants.WEBSITE_LINK));
		gridPane.addRow(2, new Label("Official website"), officialWebsiteLink);
		// License
		gridPane.addRow(3, new Label("License (short version)"));
		TextArea licenseField = new TextArea(BinoclesUIConstants.LICENSE_SHORT);
		licenseField.setEditable(false);
		licenseField.setFocusTraversable(false);
		licenseField.getStyleClass().addAll("edge-to-edge", "no-focus");
		gridPane.addRow(4, licenseField);
		GridPane.setColumnSpan(licenseField, GridPane.REMAINING);
	}

	/**
	 * Builds the pane and displays it.
	 */
	public Void display() {
		alert.getDialogPane().getStylesheets().add(getClass().getResource("../styles/main.css").toExternalForm());
		alert.setTitle("About " + BinoclesUIConstants.SOFTWARE_NAME);
		alert.getDialogPane().setId("about-dialog");
		alert.getDialogPane().getStyleClass().add("form-dialog");
		Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(AppIcons.ICON_HELP);

		gridPane = new GridPane();
		gridPane.getStyleClass().add("form");

		build();

		alert.getDialogPane().setContent(gridPane);
		alert.showAndWait();
		return null;
	}
}
