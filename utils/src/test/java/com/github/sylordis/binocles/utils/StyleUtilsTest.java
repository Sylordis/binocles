package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.sylordis.binocles.utils.StyleUtils.CSSBlockStyle;
import com.github.sylordis.binocles.utils.StyleUtils.CSSType;
import com.google.common.collect.Maps;

/**
 * 
 */
class StyleUtilsTest {

	private static final CSSType TYPE = new CSSType(Map.of("color", "fill"), e -> "-a--" + e);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#convertCSSPropertyTo(java.lang.String, com.github.sylordis.binocles.utils.StyleUtils.CSSType)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "color,color", "abc,abc" })
	void testConvertCSSPropertyTo_Standard(String property, String expected) {
		assertEquals(expected, StyleUtils.convertCSSPropertyTo(property, CSSType.STANDARD));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#convertCSSPropertyTo(java.lang.String, com.github.sylordis.binocles.utils.StyleUtils.CSSType)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "color,-a--fill", "abc,-a--abc" })
	void testConvertCSSPropertyTo(String property, String expected) {
		assertEquals(expected, StyleUtils.convertCSSPropertyTo(property, TYPE));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#convertCSSPropertyTo(String, Map, java.util.function.Function)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "color,COLOR", "abc,ABC", "font-weight,CASE-WEIGHT" })
	void testConvertCSSPropertyToStringMapFunction(String property, String expected) {
		final CSSType type = new CSSType(Map.of("font", "case"), String::toUpperCase);
		assertEquals(expected, StyleUtils.convertCSSPropertyTo(expected, type.replacements(), type.converter()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#convertCSSPropertyTo(String, Map, java.util.function.Function)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "color,COLOR", "abc,ABC", "font-weight,FONT-WEIGHT" })
	void testConvertCSSPropertyToStringMapFunction_nullReplacements(String property, String expected) {
		final CSSType type = new CSSType(null, String::toUpperCase);
		assertEquals(expected, StyleUtils.convertCSSPropertyTo(expected, type.replacements(), type.converter()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#convertCSSPropertyTo(String, Map, java.util.function.Function)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "color,color", "abc,abc", "font-weight,case-weight" })
	void testConvertCSSPropertyToStringMapFunction_nullConverter(String property, String expected) {
		final CSSType type = new CSSType(Map.of("font", "case"), null);
		assertEquals(expected, StyleUtils.convertCSSPropertyTo(expected, type.replacements(), type.converter()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#createCSSDeclarationOfType(java.lang.String, java.lang.String, com.github.sylordis.binocles.utils.StyleUtils.CSSType)}.
	 */
	@ParameterizedTest
	@MethodSource("provideFor_testCreateCSSDeclarationOfType")
	void testCreateCSSDeclarationOfType(String property, String value, CSSType type, String expected) {
		assertEquals(expected, StyleUtils.createCSSDeclarationOfType(property, value, type));
	}

	/**
	 * Arguments provider for {@link #testCreateCSSDeclarationOfType(String, String, CSSType, String)}.
	 */
	private static Stream<Arguments> provideFor_testCreateCSSDeclarationOfType() {
		return Stream.of(Arguments.of("color", "#ABCDEF", CSSType.STANDARD, "color: #ABCDEF;"),
		        Arguments.of("color", "#ABCDEF", TYPE, "-a--fill: #ABCDEF;"),
		        Arguments.of("font-weight", "bold", CSSType.STANDARD, "font-weight: bold;"),
		        Arguments.of("font-weight", "bold", TYPE, "-a--font-weight: bold;"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#createCSSDeclaration(java.lang.String, java.lang.String, java.util.Map, java.util.function.Function)}.
	 */
	@ParameterizedTest
	@MethodSource("provideFor_testCreateCSSDeclarationOfType")
	void testCreateCSSDeclaration(String property, String value, CSSType type, String expected) {
		assertEquals(expected, StyleUtils.createCSSDeclaration(property, value, type.replacements(), type.converter()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#createCSSBlock(Map, CSSBlockStyle)}.
	 */
	@Test
	void testCreateCSSBlock_inlineStandard() {
		Map<String, String> styles = new LinkedHashMap<>();
		styles.put("color", "#ABCDEF");
		styles.put("font-weight", "italic");
		assertEquals("color: #ABCDEF; font-weight: italic;", StyleUtils.createCSSBlock(styles, CSSBlockStyle.INLINE));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#createCSSBlock(Map, CSSBlockStyle)}.
	 */
	@Test
	void testCreateCSSBlock_onePerLineStandard() {
		Map<String, String> styles = new LinkedHashMap<>();
		styles.put("color", "#ABCDEF");
		styles.put("font-weight", "italic");
		assertEquals("color: #ABCDEF;\nfont-weight: italic;",
		        StyleUtils.createCSSBlock(styles, CSSBlockStyle.ONE_PER_LINE));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#toHumanString(java.util.Map)}.
	 */
	@ParameterizedTest
	@MethodSource("providerFor_testToHumanString")
	void testToHumanString(Collection<String> properties, Collection<String> values, String expected) {
		// Creating a linked hashmap to conserve ordering
		Map<String, String> styles = MapUtils.create(new LinkedHashMap<>(),
		        properties.toArray(String[]::new), values.toArray(String[]::new));
		assertEquals(expected, StyleUtils.toHumanString(styles));
	}

	private static Stream<Arguments> providerFor_testToHumanString() {
		return Stream.of(
		        Arguments.of(List.of("font-style", "font-weight", "color"), List.of("italic", "bold", "#2E74B5"),
		                "Color: #2E74B5; Styles: bold, italic"),
		        Arguments.of(List.of("color", "bg-color"), List.of("#2E74B5", "blue"),
		                "Color: #2E74B5; Background: blue"));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.StyleUtils#strToMap(java.lang.String)}.
	 */
	@Test
	void testStrToMap_inline() {
		String block = "position:absolute; right:0;width: 0 ;height:  0   ;border-style:solid;border-right-width: 200px;";
		Map<String, String> expected = Map.of("position", "absolute", "right", "0", "width", "0", "height", "0",
		        "border-style", "solid", "border-right-width", "200px");
		assertTrue(Maps.difference(expected, StyleUtils.strToMap(block)).areEqual());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.StyleUtils#strToMap(java.lang.String, boolean)}.
	 */
	@Test
	void testStrToMapStringBoolean_inlineOrdered() {
		String block = "position:absolute; right:0;width: 0;height: 0;border-style: solid;border-right-width: 200px;";
		Map<String, String> expected = new LinkedHashMap<>();
		expected.put("position", "absolute");
		expected.put("right", "0");
		expected.put("width", "0");
		expected.put("height", "0");
		expected.put("border-style", "solid");
		expected.put("border-right-width", "200px");
		assertEquals(expected, StyleUtils.strToMap(block, true));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.StyleUtils#strToMap(java.lang.String)}.
	 */
	@Test
	void testStrToMap_block() {
		String block = """
		        border-right-width: 130px;
		        border-bottom-width: 0;
		        border-top-width: 200px;
		        border-left-width: 130px;
		        border-color: green transparent transparent transparent;""";
		Map<String, String> expected = Map.of("border-right-width", "130px", "border-bottom-width", "0",
		        "border-top-width", "200px", "border-left-width", "130px", "border-color",
		        "green transparent transparent transparent");
		assertTrue(Maps.difference(expected, StyleUtils.strToMap(block)).areEqual());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.StyleUtils#strToMap(java.lang.String)}.
	 */
	@Test
	void testStrToMapStringBoolean_blockOrdered() {
		String block = """
		        border-right-width: 130px;
		        border-bottom-width: 0;
		        border-top-width: 200px;
		        border-left-width: 130px;
		        border-color: green transparent transparent transparent;""";
		Map<String, String> expected = new LinkedHashMap<>();
		expected.put("border-right-width", "130px");
		expected.put("border-bottom-width", "0");
		expected.put("border-top-width", "200px");
		expected.put("border-left-width", "130px");
		expected.put("border-color", "green transparent transparent transparent");
		assertEquals(expected, StyleUtils.strToMap(block, true));
	}

}
