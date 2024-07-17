package com.github.sylordis.binocles.ui.javafxutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.paint.Color;

/**
 * Util tools for managing colours.
 */
public final class StyleUtils {

	/**
	 * CSS to FXCSS property replacement map.
	 */
	private final static Map<String, String> cssToFxcssDictionary = Map.of("color", "fill");

	/**
	 * Types of CSS to be managed.
	 */
	public enum CSSType {

		JavaFX(e -> "-fx-" + e, cssToFxcssDictionary),
		RichTextFX(e -> "-rtfx-" + e, cssToFxcssDictionary);

		private final Function<String, String> converter;
		private final Map<String, String> replacements;

		private CSSType(Function<String, String> converter, Map<String, String> replacements) {
			this.converter = converter;
			this.replacements = replacements;
		}

		/**
		 * @return the converter
		 */
		Function<String, String> getConverter() {
			return converter;
		}

		/**
		 * @return the replacements
		 */
		Map<String, String> getReplacements() {
			return replacements;
		}

	}

	/**
	 * Converts a CSS Property into its JavaFX CSS declaration for styling. They are mainly the same,
	 * but prefixed with <code>-fx-</code> and some properties are changed. This method takes care of
	 * that.
	 * 
	 * @param entry entry to convert
	 * @return the converted property
	 * @see #convertCSSPropertyTo(String, Function, Map)
	 */
	public static String convertPropertyCSStoFXCSS(String entry) {
		return convertCSSPropertyTo(entry, CSSType.JavaFX);
	}

	/**
	 * Converts a CSS Property into its JavaFX RichText CSS declaration for styling. They are mainly the
	 * same, but prefixed with <code>-rtfx-</code> and some properties are changed.
	 * 
	 * @param entry entry to convert
	 * @return the converted property
	 * @see #convertCSSPropertyTo(String, Function, Map)
	 */
	public static String convertPropertyCSStoRTFXCSS(String entry) {
		return convertCSSPropertyTo(entry, CSSType.RichTextFX);
	}

	/**
	 * Converts a CSS Property into another property style defined in the {@link CSSType} enumeration.
	 * 
	 * @param entry        entry to convert
	 * @param replacements dictionary of replacements, first applied before the converter
	 * @param converter    basic string to string converter to apply any modifications
	 * @return the converted property
	 */
	public static String convertCSSPropertyTo(String entry, CSSType type) {
		return convertCSSPropertyTo(entry, type.getReplacements(), type.getConverter());
	}

	/**
	 * Converts a CSS Property into another property style, by first applying a replacement dictionary
	 * for the property and then converting it to its desired state.
	 * 
	 * @param entry        entry to convert
	 * @param replacements dictionary of replacements, first applied before the converter
	 * @param converter    basic string to string converter to apply any modifications
	 * @return the converted property
	 */
	public static String convertCSSPropertyTo(String entry, Map<String, String> replacements,
	        Function<String, String> converter) {
		String result = entry;
		if (replacements != null && replacements.containsKey(entry))
			result = replacements.get(entry);
		result = converter.apply(result);
		return result;
	}

	/**
	 * Converts a CSS Property into its JavaFX CSS style declaration for styling. They are mainly the
	 * same, but prefixed with <code>-fx-</code> and some properties are changed. This method takes care
	 * of that.<br/>
	 * 
	 * @param property
	 * @param value
	 * @return the full css style declaration
	 */
	public static String convertCSStoTypeStyle(String property, String value, CSSType type) {
		return convertCSSPropertyTo(property, type) + ": " + value + ";";
	}

	/**
	 * Transforms a colour into an rgba string value separated by commas, destined to be transformed
	 * back into a {@link Color} with {@link Color#web(String)}. This method is null safe.
	 * 
	 * @param color the Color to transform
	 * @return a string representing the colour or empty string if the provided colour is null.
	 */
	public static String toHexString(Color color) {
		StringBuilder builder = new StringBuilder();
		if (color != null) {
			builder.append("#");
			builder.append(toHex(color.getRed()));
			builder.append(toHex(color.getGreen()));
			builder.append(toHex(color.getBlue()));
			builder.append(toHex(color.getOpacity()));
		}
		return builder.toString().toUpperCase();
	}

	/**
	 * Brightens the given color.
	 * 
	 * @param color
	 * @return
	 */
	public static Color brighten(Color color) {
		Color ncolor = color.brighter().desaturate();
		return ncolor;
	}

	/**
	 * Scales a double number to an int scale, its final result will end up in [0,scale].
	 * 
	 * @param val   double value to convert
	 * @param scale maximum value of the scale
	 * @return
	 */
	private static int scaleTo(double val, int scale) {
		return (int) Math.round(val * scale);
	}

	/**
	 * Transforms a double value [0.0, 1.0] into a 255 representation hex string.
	 * 
	 * @param val double value to convert
	 * @return
	 */
	private static String toHex(double val) {
		String in = Integer.toHexString(scaleTo(val, 255));
		return in.length() == 1 ? "0" + in : in;
	}

	/**
	 * Creates a human-readable string from the given dictionary of styles.
	 * 
	 * @param styles
	 * @return
	 */
	public static String toHumanString(Map<String, String> styles) {
		// TODO Style is fucked up
		List<String> parts = new ArrayList<>();
		List<String> fontStyles = new ArrayList<>();
		for (Entry<String, String> entry : styles.entrySet()) {
			switch (entry.getKey()) {
				case "font-style":
				case "font-weight":
					if (!entry.getValue().isEmpty())
						fontStyles.add(entry.getValue());
					break;
				case "bg-color":
					parts.add("Background: " + entry.getValue());
					break;
				default:
					parts.add(StringUtils.capitalize(entry.getKey().replace('-', ' ')) + ": " + entry.getValue());
					break;
			}
		}
		if (!fontStyles.isEmpty()) {
			parts.add("Styles: " + StringUtils.join(", ", fontStyles));
		}
		return String.join("; ", parts);
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private StyleUtils() {
		// Nothing to see here
	}

}
