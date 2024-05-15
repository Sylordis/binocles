package com.github.sylordis.binocles.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class holding configuration variables for the model or the software in general.
 * 
 * @author sylordis
 */
public class BinoclesConfiguration {

	/**
	 * Name of the properties file.
	 */
	public static final String PROPERTIES_FILE = "binocles.properties";

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
	private static BinoclesConfiguration instance = null;

	/**
	 * Gets the singleton instance of the configuration.
	 * 
	 * @return
	 */
	public synchronized static BinoclesConfiguration getInstance() {
		if (instance == null)
			instance = new BinoclesConfiguration();
		return instance;
	}

	/**
	 * Creates a new instance.
	 */
	private BinoclesConfiguration() {
		properties = new Properties();
		loadInternalConfigFile();
	}

	/**
	 * Loads the internal configuration file.
	 */
	private void loadInternalConfigFile() {
		try {
			System.out.println(new File(getClass().getClassLoader().getResource("ah").toURI()));
			System.out.println(new File(getClass().getClassLoader().getResource("ah").toURI()).getAbsolutePath());
			properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException | URISyntaxException e) {
			logger.info("Couldn't load internal settings file.");
		} catch (NullPointerException e) {
			logger.error("uhoh");
		}
	}

	/**
	 * Gets a property.
	 * 
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		return properties.get(key);
	}

	/**
	 * Gets a property, or return a default value if it doesn't exist.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Object getOrDefault(Object key, Object defaultValue) {
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
		return (String) getOrDefault(VERSION, VERSION_DEFAULT);
	}

	/**
	 * Gets the properties.
	 * 
	 * @return
	 */
	Properties getProperties() {
		return properties;
	}

	/**
	 * Clears the singleton instance. Use only for testing or extreme cases.
	 */
	static void clearInstance() {
		instance = null;
	}

}
