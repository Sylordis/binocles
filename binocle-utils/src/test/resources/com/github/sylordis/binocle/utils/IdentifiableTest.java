package com.github.sylordis.binocle.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class IdentifiableTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * We're not testing here for equality as getId() can be implemented however people want.
	 */
	@Test
	void testGetId() {
		final String id = "Skeletor";
		assertNotNull(id, new IdentifiableImpl(id).getId());
	}

	@Test
	void testIsIdentifiable() {
		final String id = "HeMan";
		Identifiable o1 = new IdentifiableImpl(id);
		Identifiable o2 = new IdentifiableImpl(id);
		assertTrue(o1.is(o2));
	}

	@Test
	void testIsIdentifiable_Self() {
		final String id = "SheRa";
		Identifiable o = new IdentifiableImpl(id);
		assertTrue(o.is(o));
	}

	@Test
	void testIsIdentifiable_Not() {
		final String id1 = "Masters";
		final String id2 = "Universe";
		Identifiable o1 = new IdentifiableImpl(id1);
		Identifiable o2 = new IdentifiableImpl(id2);
		assertFalse(o1.is(o2));
	}

	@Test
	void testIsString() {
		final String id = "Glory";
		assertTrue(new IdentifiableImpl(id).is(id));
	}

	@Test
	void testIsString_Not() {
		final String id = "Angus";
		assertFalse(new IdentifiableImpl(id).is("Zargothrax"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "a", "  Mc", "Fife" })
	void testHasId(String id) {
		assertTrue(new IdentifiableImpl(id).hasId());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "  \t"} )
	@NullSource
	void testHasId_BlankOrEmpty(String id) {
		assertFalse(new IdentifiableImpl(id).hasId());
	}

	@ParameterizedTest
	@CsvSource(value = { "Ralathor,ralathor", "Ser Proletius,ser proletius", "  Hootsman  ,hootsman" })
	void testFormatId(String in, String expected) {
		assertEquals(expected, Identifiable.formatId(in));
	}

	/**
	 * Private class for testing.
	 * 
	 * @author sylordis
	 *
	 */
	private record IdentifiableImpl(String id) implements Identifiable {

		@Override
		public String getId() {
			return Identifiable.formatId(this.id);
		}
	};

}
