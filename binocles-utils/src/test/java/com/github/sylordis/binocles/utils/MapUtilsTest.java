package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MapUtilsTest {

	private static final Integer[] KEYS = { 1, 2, 3, 4, 5, 6 };
	private static final String[] VALUES = { "one", "two", "three", "four", "five" };

	@Test
	void testConstructor() {
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
		Map<Integer,String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreate_ValuesDifferentSize() {
		// KEYS is bigger than VALUES
		Map<Integer, String> map = MapUtils.create(KEYS, Arrays.copyOf(VALUES, VALUES.length - 1));
		assertNotNull(map);
		Map<Integer,String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length-1, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	public void testCreate_SameLength() {
		Map<Integer, String> map = MapUtils.create(Arrays.copyOf(KEYS, VALUES.length), VALUES);
		Map<Integer,String> expected = new HashMap<>();
		for (int i = 0; i < Math.min(VALUES.length, KEYS.length); i++)
			expected.put(KEYS[i], VALUES[i]);
		assertEquals(expected, map);
	}

	@Test
	void testPutOrRemoveIfNull_PutOnly() {
		final Map<String, String> map = new HashMap<>(Map.of("c", "C", "d", "D"));
		MapUtils.putOrRemoveIfNull(map, "a", "b");
		final Map<String, String> expected = Map.of("a", "b", "c", "C", "d", "D");
		assertEquals(expected, map);
	}

	@Test
	void testPutOrRemoveIfNull_Replace() {
		final Map<Integer, Integer> map = new HashMap<>(Map.of(105, 2, 3, 4));
		MapUtils.putOrRemoveIfNull(map, 105, 5);
		final Map<Integer, Integer> expected = Map.of(105, 5, 3, 4);
		assertEquals(expected, map);
	}

	@Test
	void testPutOrRemoveIfNull_RemoveOnly() {
		final Map<String, String> map = new HashMap<>(Map.of("key1", "v1", "key2", "v2", "key3", "v3"));
		MapUtils.putOrRemoveIfNull(map, "key3", null);
		final Map<String, String> expected = Map.of("key1", "v1", "key2", "v2");
		assertEquals(expected, map);
	}

	@Test
	void testPutOrRemoveIfNull_NothingToRemove() {
		final Map<String, String> map = new HashMap<>(Map.of("key1", "v1", "key2", "v2", "key3", "v3"));
		final Map<String, String> expected = new HashMap<>(map);
		MapUtils.putOrRemoveIfNull(map, "key6", null);
		assertEquals(expected, map);
	}

	@Test
	void testPutOrRemoveIfNull_Both() {
		final Map<String, String> map = new HashMap<>(Map.of("key1", "v1", "key2", "v2", "key3", "v3"));
		MapUtils.putOrRemoveIfNull(map, "key3", null);
		MapUtils.putOrRemoveIfNull(map, "key4", "foo");
		final Map<String, String> expected = Map.of("key1", "v1", "key2", "v2", "key4", "foo");
		assertEquals(expected, map);
	}

	@Test
	void testPutOrRemoveIfNull_NullMap() {
		final Map<String, String> map = null;
		MapUtils.putOrRemoveIfNull(map, "key4", "not");
		assertNull(map);
	}

}
