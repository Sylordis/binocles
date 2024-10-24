package com.github.sylordis.binocles.model.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NomenclatureMigrationTest {

	@Mock
	private Nomenclature next;
	@Mock
	private CommentType type1;
	@Mock
	private CommentType type2;
	@Mock
	private CommentTypeMigration change1;
	@Mock
	private CommentTypeMigration change2;

	private Map<CommentType, CommentTypeMigration> converts;

	/**
	 * Object under test.
	 */
	private NomenclatureMigration change;

	@BeforeEach
	void setUp() throws Exception {
		change = new NomenclatureMigration(next);
		converts = Map.of(type1, change1, type2, change2);
	}

	@Test
	void testNomenclatureChange() {
		change = new NomenclatureMigration();
		assertNotNull(change);
		assertNull(change.getNextConfiguration());
	}

	@Test
	void testNomenclatureChangeLegendConfiguration() {
		assertNotNull(change);
	}

	@Test
	void testNomenclatureChangeLegendConfiguration_Null() {
		change = new NomenclatureMigration(null);
		assertNotNull(change);
		assertNull(change.getNextConfiguration());
	}

	@Test
	void testNomenclatureChangeLegendConfigurationMapOfLegendConfigurationTypeLegendConfigurationTypeChange(
			@Mock Nomenclature nextCfg) {
		change = new NomenclatureMigration(nextCfg, converts);
		assertNotNull(change);
		assertEquals(nextCfg, change.getNextConfiguration());
		assertEquals(converts, change.getTypeConversions());
	}

	@Test
	void testGetNextConfiguration() {
		assertEquals(next, change.getNextConfiguration());
	}

	@Test
	void testSetNextConfiguration(@Mock Nomenclature future) {
		change.setNextConfiguration(future);
		assertEquals(future, change.getNextConfiguration());
	}

	@Test
	void testGetTypeConversions() {
		assertNotNull(change.getTypeConversions());
		assertTrue(change.getTypeConversions().isEmpty());
		assertThrows(UnsupportedOperationException.class, () -> change.getTypeConversions()
				.put(mock(CommentType.class), mock(CommentTypeMigration.class)));
	}

	@Test
	void testAddTypeConversion(@Mock CommentType origin, @Mock CommentTypeMigration convert) {
		change.addTypeConversion(origin, convert);
		assertTrue(change.hasTypeChangeFor(origin));
	}

	@Test
	void testAddTypeConversion_NullOriginType(@Mock CommentTypeMigration convert) {
		assertThrows(NullPointerException.class, () -> change.addTypeConversion(null, convert));
	}

	@Test
	void testAddTypeConversion_NullConversion(@Mock CommentType origin) {
		assertThrows(NullPointerException.class, () -> change.addTypeConversion(origin, null));
	}

	@Test
	void testSetTypeConversions() {
		change.setTypeConversions(converts);
		assertEquals(converts, change.getTypeConversions());
	}

	@Test
	void testSetTypeConversions_Null() {
		change.setTypeConversions(null);
		assertNotNull(change.getTypeConversions());
		assertTrue(change.getTypeConversions().isEmpty());
	}

	@Test
	void testSetTypeConversions_Empty() {
		change.setTypeConversions(new HashMap<>());
		assertNotNull(change.getTypeConversions());
		assertTrue(change.getTypeConversions().isEmpty());
	}

	@Test
	void testRemoveTypeConversion() {
		Map<CommentType, CommentTypeMigration> expected = new HashMap<>();
		expected.put(type2, change2);
		change.setTypeConversions(converts);
		assertEquals(change1, change.removeTypeConversion(type1));
		assertEquals(expected, change.getTypeConversions());
	}

	@Test
	void testRemoveTypeConversion_FromEmpty() {
		assertNull(change.removeTypeConversion(mock(CommentType.class)));
	}

	@Test
	void testRemoveTypeConversion_NonExisting() {
		change.setTypeConversions(converts);
		assertNull(change.removeTypeConversion(mock(CommentType.class)));
		assertEquals(converts, change.getTypeConversions());
	}

	@Test
	void testRemoveTypeConversion_Null() {
		change.setTypeConversions(converts);
		assertNull(change.removeTypeConversion(null));
	}

	@Test
	void testHasTypeChangeFor() {
		change.setTypeConversions(converts);
		assertTrue(change.hasTypeChangeFor(type2));
	}

	@Test
	void testHasTypeChangeFor_Null() {
		assertFalse(change.hasTypeChangeFor(null));
	}

	@Test
	void testHasTypeChangeFor_NoTypeChange() {
		change.setTypeConversions(converts);
		assertFalse(change.hasTypeChangeFor(mock(CommentType.class)));
	}
}
