package com.github.sylordis.binocles.utils;

/**
 * Identifiable classes possess a unique ID amongst their peers.
 * @author sylordis
 *
 */
public interface Identifiable {

	/**
	 * Retrieves the unique id of the entity.
	 * @return
	 */
	String getId();
	
	/**
	 * Checks if this entity is the same as the one provided.
	 * @param i
	 * @return true if the ID of both entities match.
	 */
	default boolean is(Identifiable i) {
		return null != i && getId().equals(i.getId());
	}

	/**
	 * Checks if this entity as the same id..
	 * @param i
	 * @return true if the ID of both entities match.
	 */
	default boolean is(String name) {
		return null != name && getId().equals(formatId(name)); 
	}
	
	/**
	 * Checks if an entity has a non-blank id.
	 * @return
	 */
	default boolean hasId() {
		return null != getId() && !getId().isBlank();
	}
	
	/**
	 * Harmonises the id creation.
	 * @param text
	 * @return
	 */
	static String formatId(String text) {
		String id;
		if (null == text)
			id = "";
		else
			id = text.trim().toLowerCase();
		return id;
	}
	
}
