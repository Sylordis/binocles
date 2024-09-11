package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ToolbarButton extends Button implements Initializable {

	@FXML
	private ImageView graphicComponent;
	@FXML
	private Tooltip tooltipComponent;

	private Image image;
	private String tooltip;

	/**
	 * Creates a new Toolbar button based on the FXML schema.
	 * 
	 * @param image image to set as icon
	 * @param tooltip tooltip for the user
	 */
	public ToolbarButton(Image image, String tooltip) {
		this.image = image;
		this.tooltip = tooltip;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("toolbar_button.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		graphicComponent.setImage(image);
		tooltipComponent.setText(this.tooltip);
	}

}
