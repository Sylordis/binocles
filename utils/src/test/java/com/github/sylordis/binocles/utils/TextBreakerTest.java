package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.sylordis.binocles.utils.TextBreaker.BreakingPolicy;
import com.github.sylordis.binocles.utils.TextBreaker.ReadDirection;

/**
 * 
 */
class TextBreakerTest {

	/**
	 * Object under test.
	 */
	private TextBreaker breaker;

	private final String TEXT = """
	        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac tristique ante. Quisque eu dignissim neque, dictum convallis ante. Pellentesque et sodales dolor. Suspendisse fermentum dapibus metus eget rhoncus. Suspendisse ultrices condimentum nulla, sed condimentum massa. Nullam ut lobortis mi, quis tempus ipsum. Vivamus a diam ut risus faucibus imperdiet sit amet id ex.
	        In vel ante in leo tempus commodo. Ut nec velit mauris. Integer non augue non libero scelerisque molestie. Vivamus tincidunt sit amet arcu eu interdum. In tempor lacus vitae metus blandit posuere. Maecenas a risus neque. Nulla feugiat tortor in ligula scelerisque dapibus.""";

	@BeforeEach
	public void beforeEach() {
		breaker = new TextBreaker();
	}

	@Test
	public void testReadDirection_default() {
		assertTrue(breaker.doesReadForward());
	}

	@Test
	public void testGetBreakPolicy_default() {
		assertEquals(BreakingPolicy.FIRST, breaker.getBreakPolicy());
	}

	@Test
	public void testGetBreakpoints_default() {
		assertNotNull(breaker.getBreakpoints());
		assertIterableEquals(new HashSet<>(Arrays.asList(TextBreaker.DEFAULT_BREAKPOINTS)), breaker.getBreakpoints());
	}

	@Test
	public void testGetMandatoryBreakPolicy_default() {
		assertNotNull(breaker.getMandatoryBreakpoints());
		assertTrue(breaker.getMandatoryBreakpoints().isEmpty());
	}

	@Test
	public void testFindClosestTextBreak_nullText() {
		assertThrows(NullPointerException.class, () -> breaker.breakText(null, 0, 0, 0));
	}

	@ParameterizedTest
	@ValueSource(ints = { Integer.MIN_VALUE, -2, 800, Integer.MAX_VALUE })
	public void testFindClosestTextBreak_fromOutOfBounds(int from) {
		assertThrows(IndexOutOfBoundsException.class, () -> breaker.breakText(TEXT, from, 0, 0));
	}

	@Nested
	class Forward {

		@BeforeEach
		public void beforeEach() {
			breaker.setReadDirection(ReadDirection.FORWARD);
		}

		@Test
		public void testDoesReadForward() {
			assertTrue(breaker.doesReadForward());
		}

		@Nested
		class First {

			@BeforeEach
			public void beforeEach() {
				breaker.setBreakingPolicy(BreakingPolicy.FIRST);
			}

			@Test
			public void testGetBreakingPolicy() {
				assertEquals(BreakingPolicy.FIRST, breaker.getBreakPolicy());
			}

			@ParameterizedTest(name = "Case: {4}")
			@CsvSource(value = { "0,50,80,56,2 breakpoints", "20,50,90,79,Only one breakpoint",
			        "360,20,49,401,No breakpoints", "0,50,-1,56,Unlimited max" })
			public void testFindClosestBreakingPoint(int from, int min, int max, int expected, String name) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max));
			}

			@ParameterizedTest(name = "Edge case: {4}")
			@CsvSource(value = { "0,0,0,0,No thresholds,Should return the starting point",
			        "575,35,100,648,To the upper edge,Should return the upper edge",
			        "612,200,500,648,from+min threshold > text length,Should return the upper edge when minimum threshold goes over." })
			public void testFindClosestBreakingPoint_edgeCases(int from, int min, int max, int expected, String name,
			        String reason) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max), reason);
			}
		}

		@Nested
		class Last {

			@BeforeEach
			public void beforeEach() {
				breaker.setBreakingPolicy(BreakingPolicy.LAST);
			}

			@Test
			public void testGetBreakingPolicy() {
				assertEquals(BreakingPolicy.LAST, breaker.getBreakPolicy());
			}

			@ParameterizedTest(name = "Case: {4}")
			@CsvSource(value = { "0,50,80,79,2 breakpoints", "20,50,90,79,Only one breakpoint",
			        "360,20,49,401,No breakpoints", "0,50,-1,648,Unlimited max" })
			public void testFindClosestBreakingPoint(int from, int min, int max, int expected, String name) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max));
			}

			@ParameterizedTest(name = "Edge case: {4}")
			@CsvSource(value = { "0,0,0,0,No thresholds,Should return the starting point",
			        "575,35,100,648,To the upper edge,Should return the upper edge",
			        "612,200,500,648,from+min threshold > text length,Should return the upper edge when minimum threshold goes over." })
			public void testFindClosestBreakingPoint_edgeCases(int from, int min, int max, int expected, String name,
			        String reason) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max), reason);
			}
		}

	}

	@Nested
	class Backward {

		@BeforeEach
		public void beforeEach() {
			breaker.setReadDirection(ReadDirection.BACKWARD);
		}

		@Test
		public void testDoesReadForward() {
			assertFalse(breaker.doesReadForward());
		}

		@Nested
		class First {

			@BeforeEach
			public void beforeEach() {
				breaker.setBreakingPolicy(BreakingPolicy.FIRST);
			}

			@Test
			public void testGetBreakingPolicy() {
				assertEquals(BreakingPolicy.FIRST, breaker.getBreakPolicy());
			}

			@ParameterizedTest(name = "Case: {4}")
			@CsvSource(value = { "647,50,80,597,2 breakpoints", "470,30,50,432,Only one breakpoint",
			        "370,20,50,325,No breakpoints", "300,20,-1,275,Unlimited max" })
			public void testFindClosestBreakingPoint(int from, int min, int max, int expected, String name) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max));
			}

			@ParameterizedTest(name = "Edge case: {4}")
			@CsvSource(value = { "250,0,0,250,No thresholds,Should return the starting point",
			        "70,15,100,0,To the lower edge,Should return the lower edge",
			        "50,100,500,0,from-min threshold < 0,Should return the lower edge when minimum threshold goes over." })
			public void testFindClosestBreakingPoint_edgeCases(int from, int min, int max, int expected, String name,
			        String reason) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max), reason);
			}
		}

		@Nested
		class Last {

			@BeforeEach
			public void beforeEach() {
				breaker.setBreakingPolicy(BreakingPolicy.LAST);
			}

			@Test
			public void testGetBreakingPolicy() {
				assertEquals(BreakingPolicy.LAST, breaker.getBreakPolicy());
			}

			@ParameterizedTest(name = "Case: {4}")
			@CsvSource(value = { "647,50,80,573,2 breakpoints", "470,30,50,432,Only one breakpoint",
			        "370,20,50,325,No breakpoints", "300,20,-1,57,Unlimited max" })
			public void testFindClosestBreakingPoint(int from, int min, int max, int expected, String name) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max));
			}

			@ParameterizedTest(name = "Edge case: {4}")
			@CsvSource(value = { "250,0,0,250,No thresholds,Should return the starting point",
			        "70,15,100,0,To the lower edge,Should return the lower edge",
			        "50,100,500,0,from-min threshold < 0,Should return the lower edge when minimum threshold goes over." })
			public void testFindClosestBreakingPoint_edgeCases(int from, int min, int max, int expected, String name,
			        String reason) {
				assertEquals(expected, breaker.findClosestBreakingPoint(TEXT, from, min, max), reason);
			}
		}

	}

	// TODO Test mandatory breakpoints.
}
