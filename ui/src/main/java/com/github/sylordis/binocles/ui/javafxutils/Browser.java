package com.github.sylordis.binocles.ui.javafxutils;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Small utility class to open the browser.
 * @author sylordis
 *
 */
public class Browser extends Application {

	/**
	 * Opens the default browser to given URL.
	 * @param url
	 */
	public void open(String url) { 
		getHostServices().showDocument(url);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
	}

}
