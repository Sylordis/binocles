package org.github.sylordis.binocle.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class MapUtilsTest {

	@Test
	void testConstructor() {
		Constructor<?>[] constructors = MapUtils.class.getDeclaredConstructors();
		assertThrows(IllegalAccessException.class, () -> constructors[0].newInstance());
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
