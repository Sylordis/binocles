package com.github.sylordis.binocle.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BinocleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("scene.fxml"));

		Scene scene = new Scene(root, 600, 400);
		
		primaryStage.getIcons().add(AppIcons.ICON_SOFTWARE);
		primaryStage.setTitle("Binocle");
		primaryStage.setScene(scene);

		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
