package com.github.sylordis.binocle.ui.settings;

/**
 * Main configuration class.
 * @author sylordis
 *
 */
public class BinocleConfiguration {

	/**
	 * Holder for singleton instance.
	 * @author sylordis
	 *
	 */
	private static class BinocleConfigurationHolder {
		/**
		 * The singleton instance.
		 */
		private static final BinocleConfiguration instance = new BinocleConfiguration();
	}
	
	/**
	 * Gets the singleton instance of the configuration.
	 * @return
	 */
	public static BinocleConfiguration getInstance() {
		return BinocleConfigurationHolder.instance;
	}
	
	/**
	 * Display size.
	 */
	private DisplaySize displaySize;
	
	/**
	 * Creates a new instance.
	 */
	private BinocleConfiguration() {
		displaySize = DisplaySize.NORMAL;
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
