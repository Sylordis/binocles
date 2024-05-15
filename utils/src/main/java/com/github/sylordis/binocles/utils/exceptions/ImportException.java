package com.github.sylordis.binocles.utils.exceptions;

/**
 * Exceptions raised when an error occurs during an import process.
 * 
 * @author sylordis
 */
public class ImportException extends Exception {

	/**
	 * 
	 */
	public ImportException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ImportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ImportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ImportException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ImportException(Throwable cause) {
		super(cause);
	}

}
