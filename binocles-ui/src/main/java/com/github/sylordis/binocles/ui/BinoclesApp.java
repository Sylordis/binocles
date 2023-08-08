package com.github.sylordis.binocles.ui;

import com.github.sylordis.binocles.ui.settings.BinoclesConstants;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BinoclesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("scene.fxml"));

		Scene scene = new Scene(root);

		primaryStage.getIcons().add(AppIcons.ICON_SOFTWARE);
		primaryStage.setTitle(BinoclesConstants.SOFTWARE_NAME);
		primaryStage.setScene(scene);

		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
