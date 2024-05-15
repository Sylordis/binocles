package com.github.sylordis.binocles.model.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinoclesIOFactoryTest {

	/**
	 * Object under test.
	 */
	BinoclesIOFactory factory;
	
	@BeforeEach
	void setUp() throws Exception {
		factory = new BinoclesIOFactory();
	}

	@Test
	void testInitImporters() {
		assertFalse(factory.getImporters().isEmpty());
		assertEquals(2, factory.getImporters().size());
	}

	@Test
	void testInitExporters() {
		assertFalse(factory.getExporters().isEmpty());
		assertEquals(2, factory.getExporters().size());
	}

}
