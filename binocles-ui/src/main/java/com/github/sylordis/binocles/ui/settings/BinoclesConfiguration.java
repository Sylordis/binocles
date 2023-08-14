package com.github.sylordis.binocles.ui.settings;

import java.util.List;

import javafx.stage.FileChooser;

/**
 * Main configuration class.
 * 
 * @author sylordis
 *
 */
public class BinoclesConfiguration {

	/**
	 * Display size.
	 */
	private DisplaySize displaySize;

	/**
	 * Holder for singleton instance.
	 * 
	 * @author sylordis
	 *
	 */
	private static class BinocleConfigurationHolder {
		/**
		 * The singleton instance.
		 */
		private static final BinoclesConfiguration instance = new BinoclesConfiguration();
	}

	/**
	 * Gets the singleton instance of the configuration.
	 * 
	 * @return
	 */
	public static BinoclesConfiguration getInstance() {
		return BinocleConfigurationHolder.instance;
	}

	/**
	 * Creates a new instance.
	 */
	private BinoclesConfiguration() {
		displaySize = DisplaySize.NORMAL;
	}

	public List<FileChooser.ExtensionFilter> getFileFilters() {
		return List.of(new FileChooser.ExtensionFilter("Binocles format", "*.bino"),
		        new FileChooser.ExtensionFilter("YAML file", "*.yaml"));
	}

	/**
	 * @return the displaySize
	 */
	public DisplaySize getDisplaySize() {
		return displaySize;
	}

	/**
	 * @param displaySize the displaySize to set
	 */
	public void setDisplaySize(DisplaySize displaySize) {
		this.displaySize = displaySize;
	}

}
