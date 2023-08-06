package com.github.sylordis.binocle.model.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NomenclatureCommentTypeChangeTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Comment comment;
	@Mock
	private CommentType future;

	private Map<String, String> subst;

	/**
	 * Object under test.
	 */
	private NomenclatureCommentTypeChange change;

	@BeforeEach
	void setUp() throws Exception {
		this.subst = Map.of("a", "x", "b", "y");
		change = new NomenclatureCommentTypeChange(future);
	}

	@Test
	void testNomenclatureCommentTypeChangeCommentType() {
		assertNotNull(change);
	}

	@Test
	void testNomenclatureCommentTypeChangeCommentTypeMapOfStringString() {
		change = new NomenclatureCommentTypeChange(future, subst);
		assertNotNull(change);
		assertEquals(subst, change.getSubstitutions());
	}

	@Test
	void testNomenclatureCommentTypeChangeCommentTypeMapOfStringString_Null() {
		change = new NomenclatureCommentTypeChange(future, null);
		assertNotNull(change);
		assertNotNull(change.getSubstitutions());
		assertTrue(change.getSubstitutions().isEmpty());
	}

	@Test
	void testAccept(@Mock Comment c) {
		Map<String, String> commentFields = Map.of("a", "A", "d", "D", "c", "C");
		lenient().when(c.getFields()).thenReturn(commentFields);
		Map<String, String> futureCommentFields = Map.of("x", "A");
		Map<String, String> substitutions = Map.of("a", "x", "b", "y");
		change.setSubstitutions(substitutions);
		change.accept(c);
		verify(c, times(1)).setFields(eq(futureCommentFields));
		verify(c, times(1)).setType(future);
	}

	@Test
	void testAccept_NullFuture(@Mock Comment c) {
		change.setFuture(null);
		assertThrows(UnsupportedOperationException.class, () -> change.accept(c));
	}

	@Test
	void testAccept_NoKeysToTranspose(@Mock Comment c) {
		Map<String, String> commentFields = Map.of("fieldA", "I'm field A", "fieldB", "I'm field B", "fieldC",
				"I'm field C");
		lenient().when(c.getFields()).thenReturn(commentFields);
		change.setSubstitutions(subst);
		change.accept(c);
		verify(c, times(1)).setFields(eq(new HashMap<String, String>()));
		verify(c, times(1)).setType(future);
	}

	@Test
	void testAccept_NoSubstitutions(@Mock Comment c) {
		Map<String, String> commentFields = Map.of("fieldA", "I'm field A", "fieldB", "I'm field B", "fieldC",
				"I'm field C");
		lenient().when(c.getFields()).thenReturn(commentFields);
		change.resetSubstitutions();
		change.accept(c);
		verify(c, times(1)).setFields(eq(new HashMap<String, String>()));
		verify(c, times(1)).setType(future);
	}

	@Test
	void testGetFuture() {
		assertEquals(future, change.getFuture());
	}

	@Test
	void testSetFuture(@Mock CommentType type) {
		change.setFuture(type);
		assertEquals(type, change.getFuture());
	}

	@Test
	void testSetFuture_Null() {
		change.setFuture(null);
		assertNull(change.getFuture());
	}

	@Test
	void testGetSubstitutions() {
		assertNotNull(change.getSubstitutions());
		assertTrue(change.getSubstitutions().isEmpty());
	}

	@Test
	void testSetSubstitutions() {
		Map<String, String> subst2 = new HashMap<>(Map.of("a1", "v", "a2", "f", "b3", ""));
		subst2.put("b3", null);
		change.setSubstitutions(subst2);
		assertEquals(subst2, change.getSubstitutions());
	}

	@Test
	void testSetSubstitutions_WithoutSubstDefinedOnConstructor() {
		change = new NomenclatureCommentTypeChange(future);
		assertNotNull(change.getSubstitutions());
		assertTrue(change.getSubstitutions().isEmpty());
	}

	@Test
	void testSetSubstitutions_Null() {
		change.setSubstitutions(null);
		assertNotNull(change.getSubstitutions());
		assertTrue(change.getSubstitutions().isEmpty());
	}

	@Test
	void testResetSubstitutions() {
		change.setSubstitutions(subst);
		assertFalse(change.getSubstitutions().isEmpty());
		change.resetSubstitutions();
		assertTrue(change.getSubstitutions().isEmpty());
	}
}
