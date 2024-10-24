package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MapUtilsTest {

	private static final Integer[] KEYS = { 1, 2, 3, 4, 5, 6 };
	private static final String[] VALUES = { "one", "two", "three", "four", "five" };
	private static final Map<String, String> ENTRIES = Map.of( //
	        "Pen", "A writing instrument used for applying ink to a surface.", //
	        "Book", "A set of written, printed, or blank pages bound together.", //
	        "Phone", "A device used for communication, typically wirelessly.", //
	        "Table", "A piece of furniture with a flat top and one or more legs.", //
	        "Chair", "A piece of furniture designed to be sat on, usually with a back.", //
	        "Laptop", "A portable personal computer with a clamshell form factor.", //
	        "Cup", "A small, typically cylindrical container for drinking beverages.", //
	        "Watch", "A timekeeping device worn on the wrist.", //
	        "Bag", "A flexible container with an opening at the top, used for carrying items.", //
	        "Key", "A metal instrument used for opening locks." //
	);

	@Test
	public void testConstructor() {
		Constructor<?>[] constructors = MapUtils.class.getDeclaredConstructors();
		assertThrows(IllegalAccessException.class, () -> constructors[0].newInstance());
	}

	@Test
	public void testCreate_Nulls() {
		Map<Integer, String> map = MapUtils.create(null, null);
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testCreate_NullKeys() {
		Map<Integer, String> map = MapUtils.create(null, VALUES);
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testCreate_EmptyKeysAndValues() {
		Map<Integer, String> map = MapUtils.create(new Integer[] {}, new String[] {});
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testCreate_EmptyValues() {
		Map<Integer, String> map = MapUtils.create(KEYS, new String[] {});
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testCreate_EmptyKeys() {
		Map<Integer, String> map = MapUtils.create(new Integer[] {}, VALUES);
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testCreate_KeysDifferentSize() {
		// KEYS is bigger than VALUES
		Map<Integer, String> map = MapUtils.create(KEYS, VALUES);
		assertNotNull(map);
		Map<Integer, String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreate_ValuesDifferentSize() {
		// KEYS is bigger than VALUES
		Map<Integer, String> map = MapUtils.create(KEYS, Arrays.copyOf(VALUES, VALUES.length - 1));
		assertNotNull(map);
		Map<Integer, String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length - 1, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreate_SameLength() {
		Map<Integer, String> map = MapUtils.create(Arrays.copyOf(KEYS, VALUES.length), VALUES);
		Map<Integer, String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreateMapArrayArray() {
		Map<Integer, String> map = MapUtils.create(new TreeMap<>(), KEYS, VALUES);
		assertNotNull(map);
		Map<Integer, String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreateMapArrayArray_NullMap() {
		Map<Integer, String> map = MapUtils.create(null, KEYS, VALUES);
		assertNotNull(map);
		Map<Integer, String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreateVariableMapArray() {
		final String[] keys = { "ak", "bk", "ck" };
		final String[] values = { "av", "bv", "cv" };
		Map<String, String> map = MapUtils.createVariable(new LinkedHashMap<>(), keys[0], values[0], keys[1], values[1],
		        keys[2], values[2]);
		assertNotNull(map);
		Map<String, String> expected = new LinkedHashMap<>();
		for (int i = 0; i < keys.length; i++) {
			expected.put(keys[i], values[i]);
		}
		assertEquals(expected, map);
	}

	@Test
	public void testCreateVariableMapArray_OddEntries() {
		final String[] keys = { "ak", "bk", "ck" };
		final String[] values = { "av", "bv" };
		Map<String, String> map = MapUtils.createVariable(new LinkedHashMap<>(), keys[0], values[0], keys[1], values[1],
		        keys[2]);
		assertNotNull(map);
		Map<String, String> expected = new LinkedHashMap<>();
		for (int i = 0; i < keys.length; i++) {
			expected.put(keys[i], values.length <= i ? null : values[i]);
		}
		assertEquals(expected, map);
	}

	@Test
	public void testCreateVariableMapArray_NullEntries() {
		Map<String, String> map = MapUtils.createVariable(new TreeMap<>(), (String[]) null);
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testCreateVariableMapArray_NullMap() {
		final String[] keys = { "ak" };
		final String[] values = { "av" };
		Map<String, String> map = MapUtils.createVariable(null, keys[0], values[0]);
		assertNotNull(map);
		assertEquals(HashMap.class, map.getClass());
		Map<String, String> expected = new LinkedHashMap<>();
		expected.put(keys[0], values[0]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreateVariableArray() {
		final String[] keys = { "ak" };
		final String[] values = { "av" };
		Map<String, String> map = MapUtils.createVariable(keys[0], values[0]);
		assertNotNull(map);
		assertEquals(HashMap.class, map.getClass());
		Map<String, String> expected = new HashMap<>();
		expected.put(keys[0], values[0]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreateVariableArray_NullEntries() {
		Map<String, String> map = MapUtils.createVariable((String[]) null);
		assertAll(() -> assertNotNull(map), () -> assertEquals(HashMap.class, map.getClass()),
		        () -> assertTrue(map.isEmpty()));
	}

	@Test
	public void testConvertMap() {
		Map<String, Object> origin = Map.of("1", "hello", "5", "world");
		Map<Integer, String> expected = Map.of(1, "hello", 5, "world");
		Map<Integer, String> result = MapUtils.convertMap(origin, e -> Integer.parseInt(e.getKey()),
		        e -> (String) e.getValue());
		assertEquals(expected, result);
	}

	@Test
	public void testPutOrRemoveIfNull_PutOnly() {
		final Map<String, String> map = new HashMap<>(Map.of("c", "C", "d", "D"));
		MapUtils.putOrRemoveIfNull(map, "a", "b");
		final Map<String, String> expected = Map.of("a", "b", "c", "C", "d", "D");
		assertEquals(expected, map);
	}

	@Test
	public void testPutOrRemoveIfNull_Replace() {
		final Map<Integer, Integer> map = new HashMap<>(Map.of(105, 2, 3, 4));
		MapUtils.putOrRemoveIfNull(map, 105, 5);
		final Map<Integer, Integer> expected = Map.of(105, 5, 3, 4);
		assertEquals(expected, map);
	}

	@Test
	public void testPutOrRemoveIfNull_RemoveOnly() {
		final Map<String, String> map = new HashMap<>(Map.of("key1", "v1", "key2", "v2", "key3", "v3"));
		MapUtils.putOrRemoveIfNull(map, "key3", null);
		final Map<String, String> expected = Map.of("key1", "v1", "key2", "v2");
		assertEquals(expected, map);
	}

	@Test
	public void testPutOrRemoveIfNull_NothingToRemove() {
		final Map<String, String> map = new HashMap<>(Map.of("key1", "v1", "key2", "v2", "key3", "v3"));
		final Map<String, String> expected = new HashMap<>(map);
		MapUtils.putOrRemoveIfNull(map, "key6", null);
		assertEquals(expected, map);
	}

	@Test
	public void testPutOrRemoveIfNull_Both() {
		final Map<String, String> map = new HashMap<>(Map.of("key1", "v1", "key2", "v2", "key3", "v3"));
		MapUtils.putOrRemoveIfNull(map, "key3", null);
		MapUtils.putOrRemoveIfNull(map, "key4", "foo");
		final Map<String, String> expected = Map.of("key1", "v1", "key2", "v2", "key4", "foo");
		assertEquals(expected, map);
	}

	@Test
	public void testPutOrRemoveIfNull_NullMap() {
		final Map<String, String> map = null;
		MapUtils.putOrRemoveIfNull(map, "key4", "not");
		assertNull(map);
	}

	@ParameterizedTest
	@CsvSource(value = { "Pen,", "Book,written", "Watch,A timekeeping device worn on the wrist." })
	public void testContainsKeyAndValue(String key, String value) {
		assertTrue(MapUtils.containsKeyAndValue(ENTRIES, key, value, Function.identity(), String::contains));
	}

	@ParameterizedTest
	@CsvSource(value = { "Handbag,", "Laptop,public", "Cup,A metal instrument used for opening locks." })
	public void testContainsKeyAndValue_Not(String key, String value) {
		assertFalse(MapUtils.containsKeyAndValue(ENTRIES, key, value, Function.identity(), String::contains));
	}
}
