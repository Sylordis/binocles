package com.github.sylordis.binocles.ui.settings;

/**
 * Main configuration class.
 * @author sylordis
 *
 */
public class BinoclesConfiguration {

	/**
	 * Holder for singleton instance.
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
	 * @return
	 */
	public static BinoclesConfiguration getInstance() {
		return BinocleConfigurationHolder.instance;
	}
	
	/**
	 * Display size.
	 */
	private DisplaySize displaySize;
	
	/**
	 * Creates a new instance.
	 */
	private BinoclesConfiguration() {
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
