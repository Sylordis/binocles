package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test class for {@link Flags}.
 */
class FlagsTest {

	@ParameterizedTest
	@MethodSource("provideIfAllDo")
	void testIfAllDo(boolean expected, int[] flags) {
		boolean[] result = { false };
		Flags.ifAllDo(() -> result[0] = true, flags);
		assertEquals(expected, result[0]);
	}

	private static Stream<Arguments> provideIfAllDo() {
		// expected, {input,desired}...
		return Stream.of(
		        // No flags
		        Arguments.of(false, new int[] {}),
		        // 1 flag
		        Arguments.of(false, new int[] { 1 }),
		        // 2 flags and obvious true
		        Arguments.of(true, new int[] { 0x111, 0x001 }),
		        // 2 flags and true
		        Arguments.of(true, new int[] { 0x011010111, 0x010010010 }),
		        // 2 flags and false
		        Arguments.of(false, new int[] { 0x1011, 0x0100 }),
		        // all the flags and true
		        Arguments.of(true, new int[] { 0x1001, 0x0001, 0x111, 0x100, 0x00000001, 0x1, 0x011011, 0x011000 }),
		        // all the flags and false
		        Arguments.of(false, new int[] { 0x0110110, 0x1000000, 0x10, 0x01, 0x1100, 0x1001 }),
		        // Odd number of flags (>1) and true
		        Arguments.of(true, new int[] {  0x1001, 0x0001, 0x111, 0x100, 0x00000001, 0x1, 0x011011, 0x011000, 0x10010101 }),
		        // Odd number of flags (>1) and false
		        Arguments.of(false, new int[] { 0x0110110, 0x1000000, 0x10, 0x01, 0x1100, 0x1001, 0x0}));
	}

	@ParameterizedTest
	@MethodSource("provideAny")
	void testAny(boolean expected, int input, int[] flags) {
		assertEquals(expected, Flags.any(input, flags));
	}
	
	private static Stream<Arguments> provideAny() {
		return Stream.of(
				// One with normal int true
				Arguments.of(true, 0x01, new int[] { 1 }),
				// One true
				Arguments.of(true, 0x01, new int[] { 0x01 }),
				// One false
				Arguments.of(false, 0x01, new int[] { 0x111 }),
				// One of true
				Arguments.of(true, 0x01, new int[] { 0x1000, 0x011, 0x0001 }),
				// One false
				Arguments.of(false, 0x10, new int[] { 0x10000 }),
				// One of false
				Arguments.of(false, 0x10, new int[] { 0x00100, 0x11 })
		);
	}
	
	@ParameterizedTest
	@CsvSource(value = { "0x001,0x001,true", "0x100,0x001,false", "0x111,0x001,true", "0x1000,0x1,false" })
	void testHas(int input, int wanted, boolean expected) {
		assertEquals(expected, Flags.has(input, wanted));
	}

}
