package com.github.sylordis.binocles.utils.exceptions;

/**
 * Exception generated when trying to insert a new entity which possess a name which already exists
 * in its collection.
 * 
 * @author sylordis
 *
 */
public class UniqueIDException extends Exception {

	/**
	 * @param type Type of the content
	 * @param name Unique name which is violated
	 */
	public UniqueIDException(Class<?> type, String name) {
		super("Error while creating " + type.getSimpleName() + ": id should be unique, where '" + name
		        + "' already exists.");
	}

}
