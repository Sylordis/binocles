package com.github.sylordis.binocles.model.io;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.BinoclesModel;

/**
 * Factory to distribute the proper type of importer/exporter.
 * 
 * @author sylordis
 *
 */
public class IOFactory {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Dictionary of file importers.
	 */
	private final Map<String, Class<? extends FileImporter<BinoclesModel>>> importers = Map.of("yaml",
	        YamlFileImporter.class);

	/**
	 * Gets the importer according to file type.
	 * 
	 * @param file file to import
	 * @return
	 */
	public FileImporter<BinoclesModel> getFileImporter(File file) {
		FileImporter<BinoclesModel> importer = null;
		String ext = null;
		try {
			ext = file.getName().substring(file.getName().indexOf(".")).toLowerCase();
			Class<? extends FileImporter<BinoclesModel>> type = importers.get(ext);
			importer = type.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | NoSuchMethodException | SecurityException e) {
			logger.atError().withThrowable(e).log("Could not create a new instance of file importer for type {}", ext);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Cannot determine the type of file to import (no extension found)");
		}
		return importer;
	}

}
