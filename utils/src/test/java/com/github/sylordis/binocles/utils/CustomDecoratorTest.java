package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link CustomDecorator} class.
 */
class CustomDecoratorTest {

	/**
	 * Instance under test.
	 */
	private CustomDecorator<Object> decorator;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		decorator = new CustomDecorator<Object>();
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#CustomDecorator()}.
	 */
	@Test
	void testCustomDecorator() {
		assertNotNull(decorator);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#CustomDecorator(java.lang.String)}.
	 */
	@Test
	void testCustomDecoratorString() {
		final String separator = "/abc";
		decorator = new CustomDecorator<Object>(separator);
		assertEquals(separator, decorator.getSeparator());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#then(java.util.function.Function, java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAndFunctionOfTStringStringString() {
		final String result = decorator.then(t -> "testy", "[", "]").print(new Object());
		assertEquals("[testy]", result);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#then(java.util.function.Function, java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAndFunctionOfTStringStringString_Multiple() {
		final String result = decorator.then(t -> "testy", "[", "]").then(t -> "solar", "*", "%")
		        .print(new Object());
		assertEquals("[testy] *solar%", result);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.CustomDecorator#and(java.util.function.Function<T,java.lang.String>[])}.
	 */
	@Test
	void testAndFunctionOfTStringArray() {
		final String part1 = "not so simple decorator";
		final String result = decorator.then(t -> t.getClass().getSimpleName(), t -> part1).print(new Object());
		final String expected = Object.class.getSimpleName() + CustomDecorator.SEPARATOR_DEFAULT + part1;
		assertEquals(expected, result);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#print(java.lang.Object)}.
	 */
	@Test
	void testPrint() {
		assertEquals("", decorator.print(new Object()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#print(java.lang.Object)}.
	 */
	@Test
	void testPrint_DifferentSeparator() {
		final String separator = "/";
		decorator.setSeparator(separator);
		final String[] parts = { "new separator", "and another" };
		final String result = decorator.then(t -> parts[0], t -> parts[1]).print(new Object());
		final String expected = String.join(separator, parts);
		assertEquals(expected, result);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#print(java.lang.Object)}.
	 */
	@Test
	void testPrint_Null() {
		assertEquals("", decorator.print(null),
		        "This method should return an empty string and not return in error since no suppliers were added.");
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#getSeparator()}.
	 */
	@Test
	void testGetSeparator() {
		assertEquals(CustomDecorator.SEPARATOR_DEFAULT, decorator.getSeparator());
	}


	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#setSeparator(String)}.
	 */
	@ParameterizedTest
	@ValueSource(strings = {"a", "/", " heLlo ", "", "\t  \t"})
	void testSetSeparator(String separator) {
		decorator.setSeparator(separator);
		assertEquals(separator, decorator.getSeparator());
	}


	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#setSeparator(String)}.
	 */
	@Test
	void testSetSeparator_Null() {
		decorator = new CustomDecorator<Object>("abc");
		decorator.setSeparator(null);
		assertEquals(CustomDecorator.SEPARATOR_DEFAULT, decorator.getSeparator());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.CustomDecorator#getSuppliers()}.
	 */
	@Test
	void testGetSuppliers() {
		assertAll(() -> assertNotNull(decorator.getSuppliers()), () -> assertTrue(decorator.getSuppliers().isEmpty()));
	}

}
