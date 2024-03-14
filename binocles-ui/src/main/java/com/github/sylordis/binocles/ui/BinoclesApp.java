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
//		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("scene.fxml"));
		Parent root = FXMLLoader.load(this.getClass().getResource("scene.fxml"));;
		
		Scene scene = new Scene(root);

		primaryStage.getIcons().add(AppIcons.ICON_SOFTWARE);
		primaryStage.setTitle(BinoclesConstants.SOFTWARE_NAME);
		primaryStage.setScene(scene);

		// Parameters processing, take last argument if exist and load it
//		Parameters params = getParameters();		
//		List<String> parameters = params.getRaw();
//		if (!parameters.isEmpty()) {
//			String file = parameters.getLast();
//			((BinoclesController) loader.getController()).openFile(new File(file));
//		}

		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
