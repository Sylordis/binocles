package com.github.sylordis.binocles.model.exceptions;

public class ImporterException extends Exception {

	/**
	 * 
	 */
	public ImporterException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ImporterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ImporterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ImporterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ImporterException(Throwable cause) {
		super(cause);
	}

}
