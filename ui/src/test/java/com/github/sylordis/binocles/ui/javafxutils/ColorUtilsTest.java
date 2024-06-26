package com.github.sylordis.binocles.ui.javafxutils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javafx.scene.paint.Color;

class ColorUtilsTest {

	@ParameterizedTest
	@MethodSource("provideColorTestValues")
	void testToHexString(Color color, String expected) {
		assertEquals(expected, StyleUtils.toHexString(color));
	}
	
	@Test
	void testToHexString_Null() {
		assertEquals("", StyleUtils.toHexString(null));
	}

	private static Stream<Arguments> provideColorTestValues() {
		return Stream.of(Arguments.of(Color.rgb(0, 0, 0, 0), "#00000000"),
		        Arguments.of(Color.rgb(0, 0, 0, 1), "#000000FF"), Arguments.of(Color.rgb(23, 92, 244, 1), "#175CF4FF"),
		        Arguments.of(Color.rgb(255, 255, 255, 0.5), "#FFFFFF80"));
	}

}
