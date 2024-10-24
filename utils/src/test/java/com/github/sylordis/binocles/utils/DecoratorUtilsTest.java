package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test class for {@link DecoratorUtils}.
 */
class DecoratorUtilsTest {

	/**
	 * Test method for {@link com.github.sylordis.binocles.utils.DecoratorUtils#yesNo(boolean)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { "true,yes", "false,no" })
	void testYesNo(boolean b, String e) {
		assertEquals(e, DecoratorUtils.yesNo(b));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.DecoratorUtils#yesNoNull(java.lang.Object, java.util.function.Predicate)}.
	 */
	@ParameterizedTest
	@MethodSource("provideYesNoNull")
	void testYesNoNull(Object o, Predicate<Object> p, String e) {
		assertEquals(e, DecoratorUtils.yesNoNull(o, p));
	}

	private static Stream<Arguments> provideYesNoNull() {
		return Stream.of( //
		        Arguments.of(null, (Predicate<Object>) o -> false, DecoratorUtils.NULL), //
		        Arguments.of(new Object(), (Predicate<Object>) o -> o != null, DecoratorUtils.YES), //
		        Arguments.of("Hello world", (Predicate<String>) o -> !o.isBlank(), DecoratorUtils.YES), //
		        Arguments.of("Hello world", (Predicate<Object>) o -> o.getClass().equals(String.class), DecoratorUtils.YES), //
		        Arguments.of(new Object(), (Predicate<Object>) o -> o.getClass().equals(String.class), DecoratorUtils.NO), //
		        Arguments.of("", (Predicate<String>) o -> !o.isBlank(), DecoratorUtils.NO) //
		);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.DecoratorUtils#hasTextYNN(java.lang.String)}.
	 */
	@ParameterizedTest
	@CsvSource(value = { ",null", "foo,yes", "'',no", "'  ',no" })
	void testHasTextYNN(String s, String e) {
		assertEquals(e, DecoratorUtils.hasTextYNN(s));
	}

}
