package com.github.sylordis.binocles.model.io;

import java.io.File;
import java.io.IOException;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.exceptions.ExporterException;

/**
 * Interface for classes allowing to load files.
 * 
 * @author sylordis
 *
 * @param <T>
 */
public interface FileExporter {

	/**
	 * Loads a file to act on the software.
	 * 
	 * @param file File to import
	 * @return an object, can be null if the import is not correctly done or fails
	 * @throws IOException if a problem with IO occurs
	 * @Throws ImporterException if something else than an IO error happens during the import process
	 */
	void export(BinoclesModel model, File file) throws ExporterException, IOException;

}
