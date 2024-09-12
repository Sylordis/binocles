package com.github.sylordis.binocles.utils;

public interface SelfCopying<T extends SelfCopying<T>> {

	/**
	 * Copies all class fields from the given item.
	 * 
	 * @param item item to copy all class fields from
	 */
	void copy(T item);

}
