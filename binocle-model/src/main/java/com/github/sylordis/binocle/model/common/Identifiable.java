package com.github.sylordis.binocle.model.common;

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
		return i != null && getId().equals(i.getId());
	}
	
	/**
	 * Checks if an entity has a non-blank id.
	 * @return
	 */
	default boolean hasId() {
		return null != getId() && !getId().isEmpty();
	}
	
}
