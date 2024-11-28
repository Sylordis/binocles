package com.github.sylordis.binocles.ui;

import java.io.File;
import java.util.List;

import com.github.sylordis.binocles.ui.settings.BinoclesUIConstants;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the UI.
 */
public class BinoclesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("scene.fxml"));
		Parent root = fxmlLoader.load();

		Scene scene = new Scene(root);

		primaryStage.getIcons().add(AppIcons.ICON_SOFTWARE);
		primaryStage.setTitle(BinoclesUIConstants.SOFTWARE_NAME);
		primaryStage.setScene(scene);

		primaryStage.show();

		// Parameters processing, take last argument if exist and load it
		Parameters params = getParameters();
		List<String> parameters = params.getRaw();
		if (!parameters.isEmpty()) {
			String file = parameters.getLast();
			((BinoclesController) fxmlLoader.getController()).openFile(new File(file));
		}

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
