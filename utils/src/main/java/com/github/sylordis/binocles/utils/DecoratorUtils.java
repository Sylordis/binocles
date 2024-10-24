package com.github.sylordis.binocles.utils;

import java.util.function.Predicate;

import org.apache.logging.log4j.util.Strings;

/**
 * Utilities class for printing stuff.
 */
public final class DecoratorUtils {

	/**
	 * Official text representation for "null".
	 */
	public static final String NULL = "null";
	/**
	 * Official text representation for false/no.
	 */
	public static final String NO = "no";
	/**
	 * Official text representation for true/yes.
	 */
	public static final String YES = "yes";

	// Private constructor to prevent instantiation
	private DecoratorUtils() {
		// Nothing to do here
	}

	/**
	 * Transforms a boolean to a yes/no representation.
	 * 
	 * @param b boolean to transform.
	 * @return yes if true, no if false
	 */
	public static String yesNo(boolean b) {
		return b ? YES : NO;
	}

	/**
	 * Checks the state of an object and returns if it is null, otherwise if it matches the predicate or
	 * not.
	 * 
	 * @param o         an object to check
	 * @param predicate a predicate to test the object against
	 * @return null if the object is null otherwise yes/no according to if the object matches the
	 *         predicate or not
	 */
	public static <T> String yesNoNull(T o, Predicate<T> predicate) {
		return o == null ? NULL : yesNo(predicate.test(o));
	}

	/**
	 * Checks the state of a string, returns null if it is, otherwise checks if it is not blank.
	 * 
	 * @param text text to check
	 * @return null if the text is null, yes if the string is not blank or no otherwise
	 */
	public static String hasTextYNN(String text) {
		return yesNoNull(text, s -> !Strings.isBlank(s));
	}
}
