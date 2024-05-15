package com.github.sylordis.binocles.utils.exceptions;

/**
 * Exceptions raised when an error occurs during an export process.
 * 
 * @author sylordis
 */
public class ExportException extends Exception {

	/**
	 * 
	 */
	public ExportException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ExportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ExportException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExportException(Throwable cause) {
		super(cause);
	}

}
