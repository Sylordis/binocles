package com.github.sylordis.binocles.utils.contracts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;

/**
 * 
 */
class IdentifiableTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Test method for {@link Identifiable#id()}.
	 */
	@Test
	void testId() {
		String id = "hello";
		Id item = new Id(id);
		assertEquals(id, item.id());
	}

	/**
	 * Test method for {@link Identifiable#is(Identifiable)}.
	 */
	@Test
	void testIsIdentifiable_withoutFormat() {
		String id = "Bruce";
		OtherId item = new OtherId(id);
		assertTrue(item.is(new OtherId(id)));
	}

	/**
	 * Test method for {@link Identifiable#is(Identifiable)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "foo,Foo", "FOO,foo", "FOO,FOO", "FoO,fOo" })
	void testIsIdentifiable_withFormat(String id1, String id2) {
		Id item = new Id(id1);
		assertTrue(item.is(new Id(id2)));
	}

	/**
	 * Test method for {@link Identifiable#is(Identifiable)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "foo,bar", "foo,FOO", "Foo,foo", "foo," + Identifiable.EMPTY_ID,
	        Identifiable.EMPTY_ID + "," + Identifiable.EMPTY_ID })
	void testIsIdentifiable_not(String id1, String id2) {
		assertFalse(new OtherId(id1).is(new OtherId(id2)));
	}

	/**
	 * Test method for {@link Identifiable#is(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "foo,Foo", "FOO,foo", "FOO,FOO", "FoO,fOo" })
	void testIsString(String id1, String id2) {
		assertTrue(new Id(id1).is(id2));
	}

	/**
	 * Test method for {@link Identifiable#is(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "foo,BAR", "FOO,bar" })
	void testIsString_not(String id1, String id2) {
		assertFalse(new Id(id1).is(id2));
	}

	/**
	 * Test method for {@link Identifiable#is(java.lang.String)}.
	 */
	@ParameterizedTest
	@NullAndEmptySource
	void testIsString_nullOrEmpty(String id) {
		assertFalse(new Id("motto").is(id));
	}

	/**
	 * Test method for {@link Identifiable#is(java.lang.String)}.
	 */
	@ParameterizedTest
	@NullAndEmptySource
	void testIsString_nullOrEmptySource(String id) {
		assertFalse(new Id(id).is("hello there"));
	}

	/**
	 * Test method for {@link Identifiable#hasId()}.
	 */
	@ParameterizedTest
	@ValueSource(strings = { "twinkle", "little", "star", " a ", "wap-a-doo", "a b c" })
	void testHasId(String id) {
		assertTrue(new Id(id).hasId());
	}

	/**
	 * Test method for {@link Identifiable#hasId()}.
	 */
	@ParameterizedTest
	@NullAndEmptySource
	void testHasId_nullAndEmpty(String id) {
		assertFalse(new Id(id).hasId());
	}

	/**
	 * Test method for {@link Identifiable#formatId(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "foo,foo", "FOO,foo", "   BaR   ,bar" })
	void testFormatId(String id, String expected) {
		assertEquals(expected, Identifiable.formatId(id));
	}

	/**
	 * Test method for {@link Identifiable#formatId(java.lang.String)}.
	 */
	@ParameterizedTest
	@NullAndEmptySource
	void testFormatId_null(String id) {
		assertEquals(Identifiable.EMPTY_ID, Identifiable.formatId(id));
	}

	/**
	 * Test method for {@link Identifiable#checkIfUnique(Identifiable, java.util.Collection)}.
	 * 
	 * @throws UniqueIDException
	 */
	@ParameterizedTest
	@ValueSource(strings = { "metal", "  is  ", "   for", "EVERYONE" })
	void testCheckIfUnique_isUnique(String id) throws UniqueIDException {
		assertDoesNotThrow(() -> Identifiable.checkIfUnique(new Id(id), makeIdsOf("a", "b", "c", "ish", "every")));
	}

	/**
	 * Test method for {@link Identifiable#checkIfUnique(Identifiable, java.util.Collection)}.
	 */
	@ParameterizedTest
	@ValueSource(strings = { "WE", "  are  ", "   warriors", "BorN", "\tfrom", "the   ", "LiGhT" })
	void testCheckIfUnique_isNotUnique(String id) throws UniqueIDException {
		assertThrows(UniqueIDException.class, () -> Identifiable.checkIfUnique(new Id(id),
		        makeIdsOf("we", "are", "warriors", "born", "from", "the", "light")));
	}

	/**
	 * Test method for {@link Identifiable#checkIfUnique(Identifiable, java.util.Collection)}.
	 * 
	 * @throws UniqueIDException
	 */
	@ParameterizedTest
	@NullAndEmptySource
	void testCheckIfUnique_nullOrEmptyHaystack(List<Id> haystack) throws UniqueIDException {
		assertDoesNotThrow(() -> Identifiable.checkIfUnique(new Id("Hello"), haystack));
	}

	@ParameterizedTest
	@MethodSource("provideCheckNewNameUniquenessValidityAmongParent")
	void testCheckNewNameUniquenessValidityAmongParent(Parent parent, Id id, String name, boolean expected) {
		assertEquals(expected, Identifiable.checkNewNameUniquenessValidityAmongParent(name, parent, id, (p,s) -> p.hasChild(s)));
	}

	private static Stream<Arguments> provideCheckNewNameUniquenessValidityAmongParent() {
		return Stream.of(Arguments.of(null, null, "So Alone", true),
				Arguments.of(new Parent(List.of()), null, "First", true),
				Arguments.of(new Parent(makeIdsOf("First")), null, "First", false),
				Arguments.of(new Parent(makeIdsOf("First", "Second", "Third")), null, "Fourth", true),
				Arguments.of(null, new Id("New born"), "Changrilah", true),
				Arguments.of(new Parent(List.of()), new Id("Fifth"), "Fifth", true),
				Arguments.of(new Parent(makeIdsOf("sixth")), new Id("sixth"), "Sixth", true),
				Arguments.of(new Parent(makeIdsOf("First", "Second", "Third")), new Id("first"), "second", false)
			);
	}

	/**
	 * Creates a list of {@link Id} from a list of string IDs.
	 * 
	 * @param ids
	 * @return a list of items
	 */
	private static List<Id> makeIdsOf(String... ids) {
		return Arrays.stream(ids).map(i -> new Id(i)).collect(Collectors.toList());
	}

	record Id(String id) implements Identifiable {

		@Override
		public String getId() {
			return Identifiable.formatId(id);
		}

	}

	record OtherId(String id) implements Identifiable {

		@Override
		public String getId() {
			return id;
		}

	}

	record Parent(List<? extends Identifiable> children) {

		public boolean hasChild(String id) {
			return this.children.stream().anyMatch(c -> c.is(id));
		}
	}
}
