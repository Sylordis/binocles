package com.github.sylordis.binocle.model.legend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.sylordis.binocle.model.review.CommentType;
import com.github.sylordis.binocle.model.review.Nomenclature;

@ExtendWith(MockitoExtension.class)
class LegendConfigurationTest {

	private final String NAME = "Configuy";

	@Mock(name = "type1")
	private CommentType type1;
	@Mock(name = "type2")
	private CommentType type2;
	@Mock(name = "type3")
	private CommentType type3;

	private List<CommentType> typesAll;

	/**
	 * Object under test, renewed in {@link #setUp()}.
	 */
	private Nomenclature cfg;

	@BeforeEach
	void setUp() throws Exception {
		cfg = new Nomenclature(NAME);
		typesAll = new ArrayList<>(Arrays.asList(new CommentType[] { type1, type2, type3 }));
	}

	@Test
	void testLegendConfigurationString() {
		assertNotNull(cfg);
	}

	@Test
	void testLegendConfigurationString_NameBlank() {
		assertThrows(IllegalArgumentException.class, () -> new Nomenclature("   "));
	}

	@Test
	void testLegendConfigurationString_NameNull() {
		assertThrows(NullPointerException.class, () -> new Nomenclature(null));
	}

	@Test
	void testLegendConfigurationStringListOfLegendConfigurationType() {
		List<CommentType> types = new ArrayList<>(typesAll);
		final String name = "HotSauce";
		cfg = new Nomenclature(name, types);
		assertEquals(name, cfg.getName());
		assertEquals(types, cfg.getTypes());
	}

	@Test
	void testLegendConfigurationStringListOfLegendConfigurationType_NameBlank() {
		assertThrows(IllegalArgumentException.class, () -> new Nomenclature("   ", new ArrayList<>()));
	}

	@Test
	void testLegendConfigurationStringListOfLegendConfigurationType_NameNull() {
		assertThrows(NullPointerException.class, () -> new Nomenclature(null, new ArrayList<>()));
	}

	@Test
	void testLegendConfigurationStringListOfLegendConfigurationType_TypesNull() {
		assertThrows(NullPointerException.class, () -> new Nomenclature(NAME, null));
	}

	@Test
	void testLegendConfigurationStringListOfLegendConfigurationType_TypesEmpty() {
		List<CommentType> types = new ArrayList<>();
		final String name = "Chwing";
		cfg = new Nomenclature(name, types);
		assertEquals(name, cfg.getName());
		assertEquals(types, cfg.getTypes());
	}

	@Test
	void testGetName() {
		assertEquals(NAME, cfg.getName());
	}

	@Test
	void testSetName() {
		final String name = "HeyThere";
		cfg.setName(name);
		assertEquals(name, cfg.getName());
	}

	@Test
	void testSetName_Blank() {
		assertThrows(IllegalArgumentException.class, () -> cfg.setName("		           "));
	}

	@Test
	void testSetName_Null() {
		assertThrows(NullPointerException.class, () -> cfg.setName(null));
	}

	@Test
	void testGetTypes() {
		assertNotNull(cfg.getTypes());
		assertTrue(cfg.getTypes().isEmpty());
	}

	@Test
	void testSetTypes() {
		cfg.setTypes(typesAll);
		assertEquals(typesAll, cfg.getTypes());
	}

	@Test
	void testSetTypes_Overwrite() {
		cfg.setTypes(typesAll);
		List<CommentType> types = new ArrayList<>();
		types.clear();
		types.add(mock(CommentType.class));
		cfg.setTypes(types);
		assertEquals(types, cfg.getTypes());
	}

	@Test
	void testSetTypes_Null() {
		assertThrows(NullPointerException.class, () -> cfg.setTypes(null));
	}

	@Test
	void testSetTypeIndex() {
		cfg.setTypes(typesAll);
		List<CommentType> types = new ArrayList<>();
		types.add(type2);
		types.add(type1);
		types.add(type3);
		cfg.setTypeIndex(type2, 0);
		assertEquals(types, cfg.getTypes());
	}

	@Test
	void testSetTypeIndex_Null() {
		assertThrows(NullPointerException.class, () -> cfg.setTypes(null));
	}

	@Test
	void testSetTypeIndex_OutOfBounds() {
		cfg.setTypes(typesAll);
		assertThrows(IndexOutOfBoundsException.class, () -> cfg.setTypeIndex(type2, -5));
	}

}
