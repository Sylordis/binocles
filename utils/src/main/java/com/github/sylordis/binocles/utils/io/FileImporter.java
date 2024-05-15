package com.github.sylordis.binocles.utils.io;

import java.io.File;
import java.io.IOException;

import com.github.sylordis.binocles.utils.exceptions.ImportException;


/**
 * Interface for classes allowing to load files.
 * 
 * @author sylordis
 *
 * @param <T> type of the created object resulting of import process 
 */
public interface FileImporter<T> {

	/**
	 * Loads a file to act on the software.
	 * 
	 * @param file File to import
	 * @return an object, can be null if the import is not correctly done or fails
	 * @throws IOException if a problem with IO occurs
	 * @Throws ImporterException if something else than an IO error happens during the import process
	 */
	T load(File file) throws ImportException, IOException;

}
