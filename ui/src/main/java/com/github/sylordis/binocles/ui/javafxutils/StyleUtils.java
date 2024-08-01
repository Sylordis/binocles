package com.github.sylordis.binocles.ui.javafxutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Utilities tools for managing colours and styling. This class also manages CSS transformations
 * from standard to another style (JavaFX or RichTextFX).
 */
public final class StyleUtils {

	/**
	 * CSS to FXCSS property replacement map.
	 */
	private final static Map<String, String> cssToFxcssDictionary = Map.of("color", "fill");

	/**
	 * Separators between properties and values.
	 */
	public static final String CSS_PROPERTY_VALUE_SEPARATOR = ": ";

	/**
	 * Separator for CSS declarations.
	 */
	public final static String CSS_SEPARATOR = ";";

	/**
	 * Defines a text style for CSS declarations.
	 */
	public enum CSSBlockStyle {
		/**
		 * All declarations are on the same line.
		 */
		INLINE(" "),
		/**
		 * One declaration per line.
		 */
		ONE_PER_LINE("\n");

		/**
		 * Separator to use.
		 */
		private final String separator;

		private CSSBlockStyle(String separator) {
			this.separator = separator;
		}

		/**
		 * @return the separator
		 */
		String getSeparator() {
			return separator;
		}

	}

	/**
	 * Types of CSS to be managed.
	 */
	public enum CSSType {

		/**
		 * Standard CSS properties, use this type if you don't want to convert the properties.
		 */
		STANDARD(e -> e, new HashMap<>()),
		/**
		 * JavaFX CSS style, where all properties are written <code>-fx-&lt;property&gt;</code>.
		 */
		JAVA_FX(e -> "-fx-" + e, cssToFxcssDictionary),
		/**
		 * RichTextFX CSS style, where all properties are written <code>-fx-&lt;property&gt;</code>.
		 */
		RICH_TEXT_FX(e -> "-fx-" + e, cssToFxcssDictionary);

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
	 * Converts a CSS Property to another CSS property type from the {@link CSSType} enumeration in
	 * order to be used by the type target. Ex: calling this with {@link CSSType#JAVA_FX} will convert
	 * the CSS property to one usable by Java FX.
	 * 
	 * @param entry        entry to convert
	 * @param replacements dictionary of replacements, first applied before the converter
	 * @param converter    basic string to string converter to apply any modifications
	 * @return the converted CSS property
	 */
	public static String convertCSSPropertyTo(String entry, CSSType type) {
		return convertCSSPropertyTo(entry, type.getReplacements(), type.getConverter());
	}

	/**
	 * Converts a CSS Property into another CSS property style, by first applying a replacement
	 * dictionary for the property and then converting it to its desired state.
	 * 
	 * @param entry        entry to convert
	 * @param replacements dictionary of replacements, first applied before the converter
	 * @param converter    basic string to string converter to apply any modifications
	 * @return the converted CSS property
	 */
	private static String convertCSSPropertyTo(String entry, Map<String, String> replacements,
	        Function<String, String> converter) {
		String result = entry;
		if (replacements != null && replacements.containsKey(entry))
			result = replacements.get(entry);
		result = converter.apply(result);
		return result;
	}

	/**
	 * Creates a CSS declaration from a property and a value, where the replacement dictionary and the
	 * global string converter are based on a given type.
	 * 
	 * @param property a CSS property
	 * @param value    the value associated to the property
	 * @param type     a CSS type to convert the properties to
	 * @return a CSS declaration
	 * @see #createCSSDeclaration(String, String, Map, Function)
	 */
	public static String createCSSDeclarationOfType(String property, String value, CSSType type) {
		return createCSSDeclaration(property, value, type.getReplacements(), type.getConverter());
	}

	/**
	 * Creates a CSS declaration from a property and a value, based on a replacement dictionary and a
	 * global string converter applied to all properties.
	 * 
	 * @param property     a CSS property
	 * @param value        the value associated to the property
	 * @param replacements a replacement dictionary for properties
	 * @param converter    a global string converter for properties
	 * @return a CSS declaration
	 */
	public static String createCSSDeclaration(String property, String value, Map<String, String> replacements,
	        Function<String, String> converter) {
		return convertCSSPropertyTo(property, replacements, converter) + CSS_PROPERTY_VALUE_SEPARATOR + value
		        + CSS_SEPARATOR;
	}

	/**
	 * Converts a CSS styling dictionary (property => value) to a collection of declarations for a given
	 * {@link CSSType}.
	 * 
	 * @param styles all styles with [property] => [value]
	 * @param type   a CSS type to convert the properties to
	 * @return
	 */
	public static Collection<String> styleDictionaryToCollectionOfType(Map<String, String> styles, CSSType cssType) {
		return styleDictionaryToCollection(styles, cssType.getReplacements(), cssType.getConverter());
	}

	/**
	 * Converts a CSS styling dictionary (property => value) to a collection of declarations.
	 * 
	 * @param styles       all styles with [property] => [value]
	 * @param replacements replacements dictionary for all properties
	 * @param converter    text converter applied after replacements on all properties
	 * @return a collection with all styles properties and their values
	 */
	private static Collection<String> styleDictionaryToCollection(Map<String, String> styles,
	        Map<String, String> replacements, Function<String, String> converter) {
		Collection<String> collected = new ArrayList<>();
		collected.addAll(styles.entrySet().stream()
		        .map(e -> StyleUtils.createCSSDeclaration(e.getKey(), e.getValue(), replacements, converter))
		        .collect(Collectors.toList()));
		return collected;
	}

	/**
	 * Creates a CSS block from a collection of declarations. This method expects all declarations to
	 * already have a separator at their end.
	 * 
	 * @param styles      a collection
	 * @param outputStyle style of the block
	 * @return one string with all declarations
	 */
	public static String createCSSBlock(Collection<String> styles, CSSBlockStyle outputStyle) {
		return String.join(outputStyle.getSeparator(), styles);
	}

	/**
	 * Transforms a colour into an rgba string value separated by commas, destined to be transformed
	 * back into a {@link Color} with {@link Color#web(String)}. This method is null safe.
	 * 
	 * @param color the {@link Color} to transform
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
	 * Brightens and desaturates the given colour.
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
	 * Creates a human-readable inline string from the given dictionary of styles.
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
					parts.add(StringUtils.capitalize(entry.getKey().replace('-', ' ')) + CSS_PROPERTY_VALUE_SEPARATOR
					        + entry.getValue());
					break;
			}
		}
		if (!fontStyles.isEmpty()) {
			parts.add("Styles: " + StringUtils.join(", ", fontStyles));
		}
		return String.join("; ", parts);
	}

	/**
	 * Makes a font bold.
	 * 
	 * @param font
	 * @return
	 */
	public static Font bolden(Font font) {
		return Font.font(font.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, font.getSize());
	}

	/**
	 * Makes a Text's font bold.
	 * 
	 * @param font
	 * @return
	 */
	public static void bolden(Text text) {
		Font current = text.getFont();
		text.setFont(Font.font(current.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, current.getSize()));
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private StyleUtils() {
		// Nothing to see here
	}

}
