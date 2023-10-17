package com.github.sylordis.binocles.model.io;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.utils.FileUtils;

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
	        YamlFileImporter.class, "bino", YamlFileImporter.class);
	/**
	 * Dictionary of file exporters.
	 */
	private final Map<String, Class<? extends FileExporter>> exporters = Map.of("yaml", YamlFileExporter.class, "bino",
	        YamlFileExporter.class);

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
			ext = FileUtils.getExtension(file);
			if (null == ext) {
				logger.error("Cannot determine the type of file to import (no extension found)");
			} else {
				Class<? extends FileImporter<BinoclesModel>> type = importers.get(ext);
				logger.debug("Extension={} Importer type={}", ext, type);
				importer = type.getDeclaredConstructor().newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | NoSuchMethodException | SecurityException e) {
			logger.atError().withThrowable(e).log("Could not create a new instance of file importer for type {}", ext);
		}
		return importer;
	}

	/**
	 * Gets the exporter according to file type.
	 * @param file file to export
	 * @return
	 */
	public FileExporter getFileExporter(File file) {
		FileExporter exporter = null;
		String ext = null;
		try {
			ext = FileUtils.getExtension(file);
			if (null == ext) {
				logger.error("Cannot determine the type of file to export (no extension found)");
			} else {
				Class<? extends FileExporter> type = exporters.get(ext);
				logger.debug("Extension={} Exporter type={}", ext, type);
				exporter = type.getDeclaredConstructor().newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | NoSuchMethodException | SecurityException e) {
			logger.atError().withThrowable(e).log("Could not create a new instance of file exporter for type {}", ext);
		}
		return exporter;
	}
}
