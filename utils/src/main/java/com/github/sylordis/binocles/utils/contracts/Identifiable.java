package com.github.sylordis.binocles.utils.contracts;

import java.util.Collection;
import java.util.function.BiPredicate;

import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;

/**
 * Identifiable classes possess a unique ID amongst their peers.
 * 
 * @author sylordis
 *
 */
public interface Identifiable {

	/**
	 * Default ID for items that cannot provide an ID based on their data. According to definition, it
	 * is defined as not having an ID.
	 * 
	 * @see #hasId()
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
	 * Retrieves the unique ID of the entity. This should use {@link #formatId(String)}.
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
		return null != i && hasId() && getId().equals(i.getId());
	}

	/**
	 * Checks if this entity as the same id..
	 * 
	 * @param i
	 * @return true if the ID of both entities match.
	 */
	default boolean is(String name) {
		return null != name && hasId() && getId().equals(formatId(name));
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
		if (haystack != null && haystack.stream().anyMatch(b -> b.is(needle))) {
			throw new UniqueIDException(needle.getClass(), needle.getId());
		}
	}

	/**
	 * Checks if the current child name is valid.<br/>
	 * Conditions for being valid are:
	 * <ul>
	 * <li>If there's no parent existing, this doesn't apply and therefore considered valid.</li>
	 * <li>There is no existing child (child == null) and the name doesn't match any known child's id
	 * from the parent.</li>
	 * <li>The child already exists (child != null) and the name as id doesn't match any known
	 * children's id from the parent except if the only one matching is the child itself.</li>
	 * </ul>
	 * This method will normalise the provided name with {@link #formatId(String)} before checking its
	 * existence into the parent.
	 * 
	 * @param <P>                     Type of the parent
	 * @param <C>                     Type of the child
	 * @param newName                 New name wanted for the child
	 * @param parent                  Parent in which to check if the name is unique among its children
	 *                                (can be null)
	 * @param existingChild           Optional existing children (can be null)
	 * @param hasChildWithIdPredicate Way to check if the parent has a child of this id, accepting the
	 *                                parent and a normalised ID string
	 * @return true if the new name is unique regarding to its parent, false otherwise
	 * @see #formatId(String)
	 */
	static <P, C extends Identifiable> boolean checkNewNameUniquenessValidityAmongParent(String newName, P parent,
	        C existingChild, BiPredicate<P, String> hasChildWithIdPredicate) {
		String id = Identifiable.formatId(newName);
		return // No parent existing
		parent == null
		        // Child doesn't exist yet & parent doesn't have a child identifiable by the provided name
		        || (existingChild == null && !hasChildWithIdPredicate.test(parent, id))
				// Child exists, id(name) == child's id (e.g. the name forms the same id)
		        || (existingChild != null && (existingChild.is(id) || !hasChildWithIdPredicate.test(parent, id)));
	}

}
