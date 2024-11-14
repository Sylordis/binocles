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
	public static final String CFG_FILE = "binocles.ini";

	/**
	 * Constant for the version number stored in the configuration.
	 */
	public static final String VERSION = "version";

	/**
	 * Default version number. TODO Replace from properties.
	 */
	public static final String VERSION_DEFAULT = "0.1-SNAPSHOT";

	/**
	 * Constant for the default comment type style stored in the configuration.
	 */
	public static final Object DEFAULT_COMMENT_TYPE_STYLE = "style.commenttype.default";

	/**
	 * Default comment type style. TODO Replace from properties.
	 */
	private static final Object DEFAULT_COMMENT_TYPE_STYLE_DEFAULT = "color: #4b5da1; font-style: italic;";

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
			System.out.println(new File(getClass().getClassLoader().getResource(CFG_FILE).toURI()));
			System.out.println(new File(getClass().getClassLoader().getResource(CFG_FILE).toURI()).getAbsolutePath());
			properties.load(getClass().getClassLoader().getResourceAsStream(CFG_FILE));
		} catch (IOException | URISyntaxException e) {
			logger.atInfo().withThrowable(e).log("Couldn't load internal settings file.");
		} catch (NullPointerException e) {
			logger.error("Couldn't load internal settings file: {}", e.getMessage());
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
	 * Gets the configured default comment type style.
	 * 
	 * @return
	 */
	public String getDefaultCommentTypeStyle() {
		return (String) getOrDefault(DEFAULT_COMMENT_TYPE_STYLE, DEFAULT_COMMENT_TYPE_STYLE_DEFAULT);
	}

	/**
	 * Clears the singleton instance. Use only for testing or extreme cases.
	 */
	static void clearInstance() {
		instance = null;
	}

}
