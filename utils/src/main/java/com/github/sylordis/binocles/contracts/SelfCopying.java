package com.github.sylordis.binocles.contracts;

/**
 * A self copying object is one that can take another instance of the same class and copy all its
 * values.
 * 
 * @param <T> Class name
 */
public interface SelfCopying<T extends SelfCopying<T>> {

	/**
	 * Copies all class fields from the given item.
	 * 
	 * @param item item to copy all class fields from
	 */
	void copy(T item);

}
