package com.github.sylordis.binocle.model.legend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.sylordis.binocle.model.review.CommentType;

class LegendConfigurationTypeTest {

	private final String NAME = "Namey";
	private final String DESCRIPTION = "Descriptive description mmh";
	/**
	 * Object under test, renewed in {@link #setUp()}.
	 */
	private CommentType type;

	@BeforeEach
	void setUp() throws Exception {
		type = new CommentType(NAME, DESCRIPTION);
	}

	@ParameterizedTest
	@ValueSource(strings = { "hello", "Mamaaaa", "with  spaces" })
	void testLegendConfigurationTypeString(String name) {
		type = new CommentType(name);
		assertNotNull(type, "Type should not be null");
		assertNotNull(type);
		assertEquals(name, type.getName());
		assertTrue(type.getDescription().isEmpty());
	}

	@Test
	void testLegendConfigurationTypeString_Blank() {
		assertThrows(IllegalArgumentException.class, () -> new CommentType("   "));
	}

	@Test
	void testLegendConfigurationTypeString_Null() {
		assertThrows(NullPointerException.class, () -> new CommentType(null));
	}

	@Test
	void testLegendConfigurationTypeStringString() {
		assertNotNull(type);
	}

	@Test
	void testLegendConfigurationTypeStringString_NullName() {
		assertThrows(NullPointerException.class, () -> new CommentType(null, "something"));
	}

	@Test
	void testLegendConfigurationTypeStringString_NullDescription() {
		type = new CommentType(NAME, null);
		assertNotNull(type);
		assertTrue(type.getDescription().isEmpty());
	}

	@Test
	void testIs() {
		CommentType nType = new CommentType(NAME);
		assertTrue(nType.is(type));
	}

	@Test
	void testIs_False() {
		CommentType nType = new CommentType("a" + NAME);
		assertFalse(nType.is(type));
	}

	@Test
	void testIs_Null() {
		assertFalse(type.is(null));
	}

	@Test
	void testGetID() {
		assertNotNull(type.getID());
		assertFalse(type.getID().isBlank());
	}

	@ParameterizedTest
	@ValueSource(strings = { "foobar" })
	void testGetID_AfterSet(String input) {
		final String oldID = type.getID();
		type.setName(input);
		assertNotNull(type.getID());
		assertFalse(type.getID().isBlank());
		assertNotEquals(oldID, type.getID());
	}

	@Test
	void testGetName() {
		assertEquals(NAME, type.getName());
	}

	@ParameterizedTest
	@ValueSource(strings = { "typey" })
	void testSetName(String input) {
		type.setName(input);
		assertEquals(input, type.getName());
	}

	@ParameterizedTest
	@NullSource
	void testSetName_Null(String name) {
		assertThrows(NullPointerException.class, () -> type.setName(name));
	}

	@ParameterizedTest
	@EmptySource
	void testSetName_Empty(String name) {
		assertThrows(IllegalArgumentException.class, () -> type.setName(name));
	}

	@Test
	void testSetName_Blank() {
		assertThrows(IllegalArgumentException.class, () -> type.setName("      	"));
	}

	@Test
	void testGetDescription() {
		assertNotNull(type.getDescription());
		assertEquals(DESCRIPTION, type.getDescription());
	}

	@ParameterizedTest
	@ValueSource(strings = { "This is a comment type.", "	  	" })
	void testSetDescription_inclBlanks(String input) {
		type.setDescription(input);
		assertEquals(input, type.getDescription());
	}

	@ParameterizedTest
	@NullSource
	void testSetDescription_Null(String descr) {
		type.setDescription(descr);
		assertNotNull(type.getDescription());
		assertTrue(type.getDescription().isEmpty());
	}

	@Test
	void testGetStyles() {
		assertNotNull(type.getStyles());
		assertTrue(type.getStyles().isEmpty());
		assertThrows(UnsupportedOperationException.class, () -> type.getStyles().put("hello", "world"));
	}

	@Test
	void testSetStyle() {
		final String styleName = "weight";
		final String styleValue = "bold";
		type.setStyle(styleName, styleValue);
		assertEquals(1, type.getStyles().size());
		assertTrue(type.getStyles().containsKey(styleName));
		assertEquals(styleValue, type.getStyles().get(styleName));
	}

	@Test
	void testSetStyle_NullProperty() {
		assertThrows(NullPointerException.class, () -> type.setStyle(null, "gotcha"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "   " })
	void testSetStyle_EmptyProperty(String input) {
		assertThrows(IllegalArgumentException.class, () -> type.setStyle(input, "bwah"));
	}

	@Test
	void testSetStyle_NullValue() {
		final String styleName = "weight";
		final String styleValue = "bold";
		type.setStyle(styleName, styleValue);
		type.setStyle(styleName, null);
		assertTrue(type.getStyles().isEmpty());
	}

	@Test
	void testSetStyle_EmptyValue() {
		final String styleName = "slimed";
		final String styleValue = "";
		type.setStyle(styleName, styleValue);
		assertEquals(1, type.getStyles().size());
		assertTrue(type.getStyles().containsKey(styleName));
		assertEquals(styleValue, type.getStyles().get(styleName));
	}

	@Test
	void testSetStyle_Overwrite() {
		final String styleName = "weight";
		final String styleValue = "bold";
		final String styleValueNew = "italic";
		type.setStyle(styleName, styleValue);
		type.setStyle(styleName, styleValueNew);
		assertEquals(1, type.getStyles().size());
		assertTrue(type.getStyles().containsKey(styleName));
		assertEquals(styleValueNew, type.getStyles().get(styleName));
	}

	@Test
	void testEditStyles() {
		final Map<String, String> styles = Map.of("color", "blue", "font-weight", "bold");
		type.editStyles(styles);
		assertEquals(styles, type.getStyles());
	}

	@Test
	void testEditStyles_Null() {
		assertThrows(NullPointerException.class, () -> type.editStyles(null));
	}

	@Test
	void testEditStyles_Empty() {
		type.editStyles(new HashMap<String, String>());
		assertTrue(type.getStyles().isEmpty());
	}

	@Test
	void testEditStyles_Overwrite() {
		final Map<String, String> styles = Map.of("color", "red", "font-weight", "bold");
		type.editStyles(styles);
		assertEquals(styles, type.getStyles());
		final Map<String, String> styles2 = Map.of("color", "blue", "font-weight", "normal");
		type.editStyles(styles2);
		assertEquals(styles2, type.getStyles());
	}

	@Test
	void testEditStyles_RemoveAndChange() {
		final Map<String, String> styles = Map.of("color", "red", "font-weight", "bold");
		type.editStyles(styles);
		final Map<String, String> styles2 = new HashMap<>(Map.of("color", "purple", "font-weight", ""));
		styles2.put("font-weight", null);
		type.editStyles(styles2);
		Map<String, String> expected = Map.of("color", "purple");
		assertEquals(expected, type.getStyles());
	}

	@Test
	void testEditStyles_OneValueEmpty() {
		final Map<String, String> styles = Map.of("color", "red", "font-weight", "");
		type.editStyles(styles);
		assertEquals(styles, type.getStyles());
	}

	@Test
	void testResetStyles() {
		final Map<String, String> styles = Map.of("color", "red", "font-weight", "bold");
		type.editStyles(styles);
		type.resetStyles();
		assertNotNull(type.getStyles());
		assertTrue(type.getStyles().isEmpty());
	}

	@Test
	void testGetFields() {
		assertNotNull(type.getFields());
		assertTrue(type.getFields().isEmpty());
		assertThrows(UnsupportedOperationException.class, () -> type.getFields().put("hello", "world"));
	}

	@Test
	void testSetFields() {
		Map<String, String> fields = Map.of("a", "", "b", "", "c", "");
		type.setFields(fields);
		assertEquals(fields, type.getFields());
	}

	@Test
	void testSetFields_Overwrite() {
		type.setFields(Map.of("a", "", "b", "", "c", ""));
		Map<String, String> fields = Map.of("red", "FF0000", "black", "000000", "green", "00FF00");
		type.setFields(fields);
		assertEquals(fields, type.getFields());
	}

	@Test
	void testSetFields_Null() {
		type.setFields(null);
		assertNotNull(type.getFields());
		assertTrue(type.getFields().isEmpty());
	}

	@Test
	void testSetFields_NullStillResets() {
		type.setFields(Map.of("a", "", "b", "", "c", ""));
		type.setFields(null);
		assertNotNull(type.getFields());
		assertTrue(type.getFields().isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ "hello,world" })
	void testSetField(String name, String value) {
		type.setField(name, value);
		Map<String, String> expected = Map.of(name, value);
		assertEquals(expected, type.getFields());
	}

	@ParameterizedTest
	@CsvSource({ "hello,world", "foo,bar" })
	void testSetField_Replace(String name, String description) {
		type.setField(name, description);
		type.setField(name, "apocalypse");
		Map<String, String> expected = Map.of(name, "apocalypse");
		assertEquals(expected, type.getFields());
	}

	@ParameterizedTest
	@NullSource
	void testSetField_Remove(String description) {
		type.setFields(Map.of("hello", "world"));
		type.setField("hello", description);
		assertEquals(new HashMap<>(), type.getFields());
	}

	@ParameterizedTest
	@NullSource
	void testSetField_NothingToRemove(String description) {
		type.setField("hello", description);
		Map<String, String> expected = new HashMap<>();
		assertEquals(expected, type.getFields());
	}

	@ParameterizedTest
	@NullSource
	void testSetField_NameNull(String name) {
		assertThrows(NullPointerException.class, () -> type.setField(name, "Enchantments?"));
	}

	@ParameterizedTest
	@EmptySource
	void testSetField_NameBlank(String name) {
		assertThrows(IllegalArgumentException.class, () -> type.setField(name, "Enchantments!"));
	}

	@ParameterizedTest
	@EmptySource
	void testSetField_DescriptionBlank(String description) {
		type.setField("hello", description);
		Map<String, String> expected = Map.of("hello", description);
		assertEquals(expected, type.getFields());
	}

	@Test
	void testEditFields() {
		final Map<String, String> fields = Map.of("color", "red", "font-weight", "bold");
		type.editFields(fields);
		assertEquals(fields, type.getFields());
	}

	@Test
	void testEditFields_Null() {
		assertThrows(NullPointerException.class, () -> type.editFields(null));
	}

	@Test
	void testEditFields_Empty() {
		type.editFields(new HashMap<String, String>());
		assertTrue(type.getFields().isEmpty());
	}

	@Test
	void testEditFields_RemoveAndChange() {
		final Map<String, String> fields = Map.of("x", "field x", "y", "field y");
		type.setFields(fields);
		final Map<String, String> fields2 = new HashMap<>(Map.of("x", "This is different", "y", ""));
		fields2.put("y", null);
		type.editFields(fields2);
		final Map<String, String> expected = Map.of("x", "This is different");
		assertEquals(expected, type.getFields());
	}

	@Test
	void testEditFields_OneValueEmpty() {
		final Map<String, String> fields = Map.of("color", "red", "font-weight", "");
		type.editFields(fields);
		assertEquals(fields, type.getFields());
	}

	@Test
	void testHasField() {
		Map<String, String> fields = Map.of("a", "", "b", "", "c", "");
		type.setFields(fields);
		assertTrue(type.hasField("b"));
	}

	@Test
	void testHasField_Not() {
		assertFalse(type.hasField("Hello"));
	}

	@Test
	void testResetFields() {
		final Map<String, String> fields = Map.of("x", "field x", "y", "field y");
		type.editFields(fields);
		type.resetFields();
		assertNotNull(type.getFields());
		assertTrue(type.getFields().isEmpty());
	}
}
