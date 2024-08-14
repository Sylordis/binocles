package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * Pane component for the welcome tab.
 */
public class WelcomeView extends BorderPane implements Initializable, BinoclesTabPane {

	public WelcomeView() {
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome_view.fxml"));
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
	}

	@Override
	public Object getItem() {
		return null;
	}

}
