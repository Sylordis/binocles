package org.github.sylordis.brease.utils;

import java.util.Map;

/**
 * Utils for maps.
 *
 * @author Sylordis
 *
 */
public final class MapUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private MapUtils() {
		// Nothing to do here
	}

	/**
	 * Puts an entry if the provided value is not null, removes the entry otherwise.
	 *
	 * @param map   Map to perform the operation
	 * @param key   Key to insert/remove
	 * @param value Value of the new key, null to remove
	 */
	public static <T> void putOrRemoveIfNull(Map<T, T> map, T key, T value) {
		if (map != null) {
			if (null == value)
				map.remove(key);
			else
				map.put(key, value);
		}
	}

}
