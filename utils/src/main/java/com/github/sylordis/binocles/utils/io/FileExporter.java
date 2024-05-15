package com.github.sylordis.binocles.utils.io;

import java.io.File;
import java.io.IOException;

import com.github.sylordis.binocles.utils.exceptions.ExportException;

/**
 * Interface for classes allowing to load files.
 * 
 * @author sylordis
 *
 * @param <T> Type of the object to export
 */
public interface FileExporter<T> {

	/**
	 * Exports the provided model to a given file.
	 * 
	 * @param file File to import
	 * @return an object, can be null if the import is not correctly done or fails
	 * @throws IOException if a problem with IO occurs
	 * @Throws ImporterException if something else than an IO error happens during the import process
	 */
	void export(T o, File file) throws ExportException, IOException;

}
