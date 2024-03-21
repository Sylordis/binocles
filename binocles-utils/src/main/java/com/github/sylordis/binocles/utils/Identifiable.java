package com.github.sylordis.binocles.utils;

import java.util.Collection;

import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;

/**
 * Identifiable classes possess a unique ID amongst their peers.
 * 
 * @author sylordis
 *
 */
public interface Identifiable {

	/**
	 * Default ID for items that cannot provide an ID based on their data.
	 */
	public final static String EMPTY_ID = "";

	/**
	 * Retrieves the unique ID of the entity.
	 * 
	 * @return
	 * @see #getId()
	 */
	default String id() {
		return getId();
	}

	/**
	 * Retrieves the unique ID of the entity.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Checks if this entity is the same as the one provided.
	 * 
	 * @param i
	 * @return true if the ID of both entities match.
	 */
	default boolean is(Identifiable i) {
		return null != i && getId().equals(i.getId());
	}

	/**
	 * Checks if this entity as the same id..
	 * 
	 * @param i
	 * @return true if the ID of both entities match.
	 */
	default boolean is(String name) {
		return null != name && getId().equals(formatId(name));
	}

	/**
	 * Checks if an entity has a non-blank id.
	 * 
	 * @return
	 */
	default boolean hasId() {
		return null != getId() && !getId().isBlank();
	}

	/**
	 * Gets the ID from a string in a normalised way.<br/>
	 * <br/>
	 * 
	 * Current: trimmed and lower cased string.
	 * 
	 * @param text text to transform into an ID
	 * @return the ID or {@link #EMPTY_ID} if provided null.
	 */
	static String formatId(String text) {
		String id;
		if (null == text)
			id = EMPTY_ID;
		else
			id = text.trim().toLowerCase();
		return id;
	}

	/**
	 * Checks if an element of the haystack can be identified to a needle, meaning its ID is equal to
	 * the needle's ID.
	 * 
	 * @param <T>      Type of the needle
	 * @param <U>      Type of the haystack, can be the same as the needle
	 * @param needle   Item which ID is to be matched
	 * @param haystack Collection to search in
	 * @throws UniqueIDException if one item of the haystack can be identified to one of the haystack
	 */
	static <T extends Identifiable, U extends Identifiable> void checkIfUnique(T needle, Collection<U> haystack)
	        throws UniqueIDException {
		if (haystack.stream().anyMatch(b -> b.is(needle))) {
			throw new UniqueIDException(needle.getClass(), needle.getId());
		}
	}

}
