package com.github.sylordis.binocles.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class holding configuration variables for the model or the software in general.
 */
public class BinoclesConfiguration {

	/**
	 * Constant for the version number stored in the configuration.
	 */
	public static final String VERSION = "version";

	/**
	 * Default version number. TODO Replace from properties.
	 */
	public static final String VERSION_DEFAULT = "0.0.1-SNAPSHOT";

	/**
	 * Properties for configuration.
	 */
	private Properties properties;

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

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
		properties = new Properties();
		loadInternalConfigFile();
	}

	/**
	 * Loads the internal config file.
	 */
	private void loadInternalConfigFile() {
		try {
			System.out.println(new File(getClass().getClassLoader().getResource(".").toURI()).getAbsolutePath());
			properties.load(getClass().getClassLoader().getResourceAsStream("binocles.properties"));
		} catch (IOException | URISyntaxException e) {
			logger.info("Couldn't load internal settings file.");
		} catch (NullPointerException e) {
			logger.error("uhoh");
		}
	}

	/**
	 * Gets a property, or return a default value if it doesn't exist. This method shouldn't be used directly except for testing.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Object getProperty(Object key, Object defaultValue) {
		Object prop = defaultValue;
		if (properties.containsKey(key))
			prop = properties.get(key);
		return prop;
	}

	/**
	 * Gets the current version.
	 * 
	 * @return
	 */
	public String getVersion() {
		return (String) getProperty(VERSION, VERSION_DEFAULT);
	}

}
