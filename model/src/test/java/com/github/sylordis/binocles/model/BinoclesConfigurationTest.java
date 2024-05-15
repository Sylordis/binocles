package com.github.sylordis.binocles.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinoclesConfigurationTest {

	/**
	 * 
	 */
	BinoclesConfiguration cfg;

	@BeforeEach
	void setUp() throws Exception {
		BinoclesConfiguration.clearInstance();
		cfg = BinoclesConfiguration.getInstance();
	}

	@Test
	void testGetInstance() {
		assertNotNull(cfg);
		assertNotNull(cfg.getProperties());
	}

	@Test
	void testGet() {
		final String key = "Jekill";
		final String value = "Hyde";
		cfg.getProperties().put(key, value);
		assertEquals(value, cfg.get(key));
	}

	@Test
	void testGet_Not() {
		assertNull(cfg.get("Anyone here?"));
	}

	@Test
	void testGet_Null() {
		assertThrows(NullPointerException.class, () -> cfg.get(null));
	}

	@Test
	void testGetOrDefault() {
		final String key = "Laurel";
		final String value = "Hardy";
		final String def = "The Lucky Dog";
		cfg.getProperties().put(key, value);
		assertEquals(value, cfg.getOrDefault(key, def));
	}

	@Test
	void testGetOrDefault_Not() {
		final String key = "Stan & Oliver";
		final String def = "Duck Soup";
		assertEquals(def, cfg.getOrDefault(key, def));
	}

	@Test
	void testGetOrDefault_NotAndNullDefault() {
		assertNull(cfg.getOrDefault("A simple song", null));
	}

	@Test
	void testGetVersion() {
		assertEquals(BinoclesConfiguration.VERSION_DEFAULT, cfg.getVersion());
	}

}
