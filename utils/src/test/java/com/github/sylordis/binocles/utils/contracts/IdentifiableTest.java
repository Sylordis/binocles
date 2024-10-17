package com.github.sylordis.binocles.utils.contracts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.sylordis.binocles.contracts.Identifiable;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;

class IdentifiableTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testId() {
		final String id = "FooMan";
		assertNotNull(id, new IdentifiableImpl(id).id());
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

	@ParameterizedTest
	@NullSource
	void testIsIdentifiable_Null(Identifiable i) {
		final String id1 = "Masters";
		Identifiable o1 = new IdentifiableImpl(id1);
		assertFalse(o1.is(i));
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
	@NullSource
	void testIsString_Null(String id) {
		assertFalse(new IdentifiableImpl(id).is((String) null));
	}

	@ParameterizedTest
	@ValueSource(strings = { "a", "  Mc", "Fife" })
	void testHasId(String id) {
		assertTrue(new IdentifiableImpl(id).hasId());
	}

	@ParameterizedTest
	@NullSource
	void testHasId_Null(String id) {
		assertFalse(new IdentifiableImpl(id).hasId());
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

	@Test
	void testFormatId_Null() {
		assertEquals(Identifiable.EMPTY_ID, Identifiable.formatId(null));
	}
	
	@Test
	void testCheckIfUnique_NullNeedle() {
		assertDoesNotThrow(() -> Identifiable.checkIfUnique(null, new ArrayList<Identifiable>()));
	}

	@Test
	void testCheckIfUnique_NullHaystack() {
		assertDoesNotThrow(() -> Identifiable.checkIfUnique(new IdentifiableImpl(""), null));
	}

	@Test
	void testCheckIfUnique_EmptyHaystack() {
		assertDoesNotThrow(() -> Identifiable.checkIfUnique(new IdentifiableImpl(""), new ArrayList<Identifiable>()));
	}

	@Test
	void testCheckIfUnique_IsUnique() {
		Collection<Identifiable> haystack = List.of(new IdentifiableImpl("def"), new IdentifiableImpl("fgh"), new IdentifiableImpl("zabc"));
		assertDoesNotThrow(() -> Identifiable.checkIfUnique(new IdentifiableImpl("abc"), haystack));
	}

	@Test
	void testCheckIfUnique_IsNotUnique() {
		Collection<Identifiable> haystack = List.of(new IdentifiableImpl("def"), new IdentifiableImpl("fgh"), new IdentifiableImpl("abc"));
		assertThrows(UniqueIDException.class, () -> Identifiable.checkIfUnique(new IdentifiableImpl("abc"), haystack));
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
