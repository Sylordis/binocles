package org.github.sylordis.binocle.model.legend;

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
class LegendConfigurationChangeTest {

	@Mock
	private LegendConfiguration next;
	@Mock
	private LegendConfigurationType type1;
	@Mock
	private LegendConfigurationType type2;
	@Mock
	private LegendConfigurationTypeChange change1;
	@Mock
	private LegendConfigurationTypeChange change2;

	private Map<LegendConfigurationType, LegendConfigurationTypeChange> converts;

	/**
	 * Object under test.
	 */
	private LegendConfigurationChange change;

	@BeforeEach
	void setUp() throws Exception {
		change = new LegendConfigurationChange(next);
		converts = Map.of(type1, change1, type2, change2);
	}

	@Test
	void testLegendConfigurationChange() {
		change = new LegendConfigurationChange();
		assertNotNull(change);
		assertNull(change.getNextConfiguration());
	}

	@Test
	void testLegendConfigurationChangeLegendConfiguration() {
		assertNotNull(change);
	}

	@Test
	void testLegendConfigurationChangeLegendConfiguration_Null() {
		change = new LegendConfigurationChange(null);
		assertNotNull(change);
		assertNull(change.getNextConfiguration());
	}

	@Test
	void testLegendConfigurationChangeLegendConfigurationMapOfLegendConfigurationTypeLegendConfigurationTypeChange(
			@Mock LegendConfiguration nextCfg) {
		change = new LegendConfigurationChange(nextCfg, converts);
		assertNotNull(change);
		assertEquals(nextCfg, change.getNextConfiguration());
		assertEquals(converts, change.getTypeConversions());
	}

	@Test
	void testGetNextConfiguration() {
		assertEquals(next, change.getNextConfiguration());
	}

	@Test
	void testSetNextConfiguration(@Mock LegendConfiguration future) {
		change.setNextConfiguration(future);
		assertEquals(future, change.getNextConfiguration());
	}

	@Test
	void testGetTypeConversions() {
		assertNotNull(change.getTypeConversions());
		assertTrue(change.getTypeConversions().isEmpty());
		assertThrows(UnsupportedOperationException.class, () -> change.getTypeConversions()
				.put(mock(LegendConfigurationType.class), mock(LegendConfigurationTypeChange.class)));
	}

	@Test
	void testAddTypeConversion(@Mock LegendConfigurationType origin, @Mock LegendConfigurationTypeChange convert) {
		change.addTypeConversion(origin, convert);
		assertTrue(change.hasTypeChangeFor(origin));
	}

	@Test
	void testAddTypeConversion_NullOriginType(@Mock LegendConfigurationTypeChange convert) {
		assertThrows(NullPointerException.class, () -> change.addTypeConversion(null, convert));
	}

	@Test
	void testAddTypeConversion_NullConversion(@Mock LegendConfigurationType origin) {
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
		Map<LegendConfigurationType, LegendConfigurationTypeChange> expected = new HashMap<>();
		expected.put(type2, change2);
		change.setTypeConversions(converts);
		assertEquals(change1, change.removeTypeConversion(type1));
		assertEquals(expected, change.getTypeConversions());
	}

	@Test
	void testRemoveTypeConversion_FromEmpty() {
		assertNull(change.removeTypeConversion(mock(LegendConfigurationType.class)));
	}

	@Test
	void testRemoveTypeConversion_NonExisting() {
		change.setTypeConversions(converts);
		assertNull(change.removeTypeConversion(mock(LegendConfigurationType.class)));
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
		assertFalse(change.hasTypeChangeFor(mock(LegendConfigurationType.class)));
	}
}
