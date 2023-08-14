package com.github.sylordis.binocles.model.exceptions;

/**
 * Exception to use when a file is wrongly formatted.
 * 
 * @author sylordis
 *
 */
public class FileFormatException extends ImporterException {

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
