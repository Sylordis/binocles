package com.github.sylordis.binocles.model.exceptions;

public class ExporterException extends Exception {

	/**
	 * 
	 */
	public ExporterException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ExporterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExporterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ExporterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExporterException(Throwable cause) {
		super(cause);
	}

}
