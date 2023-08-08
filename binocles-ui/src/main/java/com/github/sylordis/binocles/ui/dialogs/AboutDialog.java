package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.javafxutils.Browser;
import com.github.sylordis.binocles.ui.settings.BinoclesConstants;

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
public class AboutDialog {

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
		Text descriptionField = new Text(BinoclesConstants.SOFTWARE_NAME
		        + " is a software for beta-reading. Commenting and reviewing made easy!");
		gridPane.addRow(0, AppIcons.createImageView(AppIcons.ICON_SOFTWARE, 64, 64), descriptionField);
		// Version
		gridPane.addRow(1, new Label("Version"), new Text("0.1-Alpha"));
		// Official website
		Hyperlink officialWebsiteLink = new Hyperlink(BinoclesConstants.WEBSITE_LINK);
		officialWebsiteLink.setOnAction(e -> new Browser().open(BinoclesConstants.WEBSITE_LINK));
		gridPane.addRow(2, new Label("Official website"), officialWebsiteLink);
		// License
		gridPane.addRow(3, new Label("License"));
		TextArea licenseField = new TextArea(BinoclesConstants.LICENSE_SHORT);
		licenseField.setEditable(false);
		gridPane.addRow(4, licenseField);
		GridPane.setColumnSpan(licenseField, GridPane.REMAINING);
	}

	/**
	 * Builds the pane and displays it.
	 */
	public void display() {
		alert.getDialogPane().getStylesheets().add(getClass().getResource("../styles/main.css").toExternalForm());
		alert.setTitle("About " + BinoclesConstants.SOFTWARE_NAME);
		alert.getDialogPane().setId("about-dialog");
		Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(AppIcons.ICON_HELP);

		gridPane = new GridPane();
		gridPane.getStyleClass().add("form");

		build();

		alert.getDialogPane().setContent(gridPane);
		alert.showAndWait();
	}
}
