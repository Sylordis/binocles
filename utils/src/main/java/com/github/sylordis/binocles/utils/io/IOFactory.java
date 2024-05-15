package com.github.sylordis.binocles.utils.io;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.utils.FileUtils;
import com.github.sylordis.binocles.utils.exceptions.ExportException;
import com.github.sylordis.binocles.utils.exceptions.ImportException;

/**
 * Factory to distribute the proper type of importer/exporter. Each exporter/importer used with this
 * class must be constructible from no arguments.
 * 
 * @author sylordis
 *
 */
public abstract class IOFactory<T> {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Dictionary of file importers, cannot be null.
	 */
	private final Map<String, Class<? extends FileImporter<T>>> importers;
	/**
	 * Dictionary of file exporters, cannot be null.
	 */
	private final Map<String, Class<? extends FileExporter<T>>> exporters;

	/**
	 * Creates a new IOFactory. Calls {@link #initExporters()} and {@link #initImporters()}.
	 */
	public IOFactory() {
		importers = new HashMap<>();
		exporters = new HashMap<>();
		initImporters();
		initExporters();
	}

	/**
	 * Fills the importers dictionary, called during object creation.
	 */
	protected abstract void initImporters();

	/**
	 * Fills the exporters dictionary, called during object creation.
	 */
	protected abstract void initExporters();

	/**
	 * Gets the importer according to file type.
	 * 
	 * @param file file to import
	 * @return
	 * @throws ExportException
	 */
	public FileImporter<T> getFileImporter(File file) throws ImportException {
		FileImporter<T> importer = null;
		String ext = FileUtils.getExtension(file);
		if (null == ext) {
			logger.error("Cannot determine the type of file to import (no extension found)");
		} else {
			try {
				Class<? extends FileImporter<T>> type = importers.get(ext);
				logger.debug("Extension={} Importer type={}", ext, type);
				if (null == type) {
					throw new ImportException("File type {} is not managed by this software");
				}
				importer = type.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger.atError().withThrowable(e).log("Could not create a new instance of file importer for type {}",
				        ext);
				throw new ImportException("Could not create a new instance of file importer for type {}" + ext);
			}
		}
		return importer;
	}

	/**
	 * Gets the exporter according to file type.
	 * 
	 * @param file file to export
	 * @return
	 * @throws ExportException
	 */
	public FileExporter<T> getFileExporter(File file) throws ExportException {
		FileExporter<T> exporter = null;
		String ext = FileUtils.getExtension(file);
		if (null == ext) {
			logger.error("Cannot determine the type of file to export (no extension found)");
		} else {
			try {
				Class<? extends FileExporter<T>> type = exporters.get(ext);
				logger.debug("Extension={} Exporter type={}", ext, type);
				if (null == type) {
					throw new ExportException("File type {} is not managed by this software");
				}
				exporter = type.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger.atError().withThrowable(e).log("Could not create a new instance of file exporter for type {}",
				        ext);
				throw new ExportException("Could not create a new instance of file exporter for type {}" + ext);
			}
		}
		return exporter;
	}

	/**
	 * @return the importers
	 */
	public Map<String, Class<? extends FileImporter<T>>> getImporters() {
		return importers;
	}

	/**
	 * @return the exporters
	 */
	public Map<String, Class<? extends FileExporter<T>>> getExporters() {
		return exporters;
	}

}
