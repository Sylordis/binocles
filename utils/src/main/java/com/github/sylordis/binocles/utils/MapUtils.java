package com.github.sylordis.binocles.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	 * Creates a map from an array of keys and an array of values, where the new map will contain
	 * entries with keys(i) => values(i). If both arrays do not match in length, data will only be
	 * inserted up to the shortest array. This method does not manage null values in arrays.
	 * 
	 * @param <K>    Type of the keys
	 * @param <V>    Type of the values
	 * @param map    base map instance for a particular type, if map is null, creates a new
	 *               {@link HashMap}.
	 * @param keys   Keys to add to the map
	 * @param values Values to link to the keys
	 * @return the map filled with entries
	 */
	public static <K, V> Map<K, V> create(Map<K, V> map, K[] keys, V[] values) {
		if (null == map)
			map = new HashMap<>();
		// Check that there is data
		if (keys != null && keys.length > 0 && values != null && values.length > 0) {
			int min = Math.min(keys.length, values.length);
			for (int i = 0; i < min; i++)
				map.put(keys[i], values[i]);
		}
		return map;
	}

	/**
	 * Creates a map from an array of keys and an array of values, where the new map will contain
	 * entries with keys(i) => values(i). If both arrays do not match in length, data will only be
	 * inserted up to the shortest array. This method does not manage null values in arrays.
	 *
	 * @param <K>    Type of the keys
	 * @param <V>    Type of the values
	 * @param keys   Keys to add to the map
	 * @param values Values to link to the keys
	 * @return a new {@link HashMap} with the keys associated to the values.
	 */
	public static <K, V> Map<K, V> create(K[] keys, V[] values) {
		return create(new HashMap<>(), keys, values);
	}

	/**
	 * Creates a map from a variable number of entries, alternating them between keys and values (i =>
	 * keys, i+1 => values). Any missing last value will be considered null.
	 * 
	 * @param <T>     Type of the entries (keys and values)
	 * @param map     base map instance for a particular type, if map is null, creates a new
	 *                {@link HashMap}
	 * @param entries all entries to be put in the map with alternating keys and values
	 * @return a new {@link HashMap} with the associated entries
	 */
	@SafeVarargs
	public static <T> Map<T, T> createVariable(T... entries) {
		return createVariable(new HashMap<>(), entries);
	}

	/**
	 * Creates a map from a variable number of entries, alternating them between keys and values (i =>
	 * keys, i+1 => values). Any missing last value will be considered null.
	 * 
	 * @param <T>     Type of the entries (keys and values)
	 * @param map     base map instance for a particular type, if map is null, creates a new
	 *                {@link HashMap}
	 * @param entries all entries to be put in the map with alternating keys and values
	 * @return
	 */
	@SafeVarargs
	public static <T> Map<T, T> createVariable(Map<T, T> map, T... entries) {
		if (null == map)
			map = new HashMap<>();
		if (null != entries && entries.length > 0) {
			for (int i = 0; i < entries.length; i += 2) {
				map.put(entries[i], entries.length > i + 1 ? entries[i + 1] : null);
			}
		}
		return map;
	}

	/**
	 * Converts the types defined in a Map to other types.
	 * 
	 * @param <Ko>        Type of the origin map's keys
	 * @param <Vo>        Type of the origin map's values
	 * @param <Kn>        Type of the new map's keys
	 * @param <Vn>        Type of the new map's values
	 * @param origin      Map to convert
	 * @param keyMapper   Mapper to create new keys
	 * @param valueMapper Mapper to create new values
	 * @return a new map with the converted types
	 */
	public static <Ko, Vo, Kn, Vn> Map<Kn, Vn> convertMap(Map<Ko, Vo> origin,
	        Function<? super Map.Entry<Ko, Vo>, ? extends Kn> keyMapper,
	        Function<? super Map.Entry<Ko, Vo>, ? extends Vn> valueMapper) {
		Map<Kn, Vn> result = origin.entrySet().stream().collect(Collectors.toMap(keyMapper, valueMapper));
		return result;
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
