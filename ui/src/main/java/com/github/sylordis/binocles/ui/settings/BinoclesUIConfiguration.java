package com.github.sylordis.binocles.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.github.sylordis.binocles.model.BinoclesConfiguration;

import javafx.stage.FileChooser;

/**
 * Main configuration class for the UI. It uses the same file as {@link BinoclesConfiguration#CFG_FILE}.
 * 
 * @author sylordis
 *
 */
public class BinoclesUIConfiguration {

	// TODO Configuration uses the same as
	
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
		private static final BinoclesUIConfiguration instance = new BinoclesUIConfiguration();
	}

	/**
	 * Gets the singleton instance of the configuration.
	 * 
	 * @return
	 */
	public static BinoclesUIConfiguration getInstance() {
		return BinocleConfigurationHolder.instance;
	}

	/**
	 * Creates a new instance.
	 */
	private BinoclesUIConfiguration() {
		displaySize = DisplaySize.MEDIUM;
	}

	/**
	 * Gets the file filters for the software without a wild one.
	 * 
	 * @return
	 */
	public List<FileChooser.ExtensionFilter> getFileFilters() {
		return getFileFilters(false);
	}

	/**
	 * Gets the file filters for the software.
	 * 
	 * @param addWild Adds a wild option for all extensions.
	 * @return
	 */
	public List<FileChooser.ExtensionFilter> getFileFilters(boolean addWild) {
		List<FileChooser.ExtensionFilter> filters = new ArrayList<>();
		if (addWild)
			filters.add(new FileChooser.ExtensionFilter("All files", "*.*"));
		filters.addAll(List.of(new FileChooser.ExtensionFilter("Binocles format", "*.bino"),
		        new FileChooser.ExtensionFilter("YAML file", "*.yaml")));
		return filters;
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
