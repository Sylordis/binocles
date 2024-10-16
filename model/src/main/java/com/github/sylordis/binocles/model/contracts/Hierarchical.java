package com.github.sylordis.binocles.model.contracts;

import java.util.List;

/**
 * Defines a class as being part of a hierarchy, e.g. that it can be a parent to multiple children or it be a child.
 * 
 * Parents should override those methods.
 * 
 * @param T type of children
 */
public interface Hierarchical<T> {

	/**
	 * Checks if this instance has children. If this returns true, then the method
	 * {@link #getChildren()} should return a non empty list. If this item is not supposed to have
	 * children, this method should return false.
	 * 
	 * @return true if this object can have children and has some, false otherwise.
	 */
	default boolean hasChildren() {
		return false;
	}

	/**
	 * Gets the list of children of this object, or null if it's not suppose to have some.
	 * 
	 * @return list of children or null if it's not supposed to have any.
	 */
	default List<? extends T> getChildren() {
		return null;
	}

}
