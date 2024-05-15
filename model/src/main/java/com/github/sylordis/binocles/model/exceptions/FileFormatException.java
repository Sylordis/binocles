package com.github.sylordis.binocles.model.exceptions;

import com.github.sylordis.binocles.utils.exceptions.ImportException;

/**
 * Exception to use when a file is wrongly formatted.
 * 
 * @author sylordis
 *
 */
public class FileFormatException extends ImportException {

	/**
	 * @param message
	 * @param cause
	 */
	public FileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public FileFormatException(String message) {
		super(message);
	}

}
