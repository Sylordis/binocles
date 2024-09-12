package com.github.sylordis.binocles.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * Utilities tools for managing colours and styling. This class also manages CSS transformations
 * from standard to another style (JavaFX or RichTextFX).
 */
public final class StyleUtils {

	/**
	 * Separators between properties and values.
	 */
	public static final String CSS_PROPERTY_VALUE_SEPARATOR = ":";

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
	 * 
	 * @param converter    text converter to apply after replacements
	 * @param replacements dictionary of replacements to perform on properties
	 */
	public record CSSType(Map<String, String> replacements, Function<String, String> converter) {

		/**
		 * Standard CSS properties, use this type if you don't want to convert the properties.
		 */
		public final static CSSType STANDARD = new CSSType(Map.of(), e -> e);

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
		return convertCSSPropertyTo(entry, type.replacements(), type.converter());
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
	public static String convertCSSPropertyTo(String entry, Map<String, String> replacements,
	        Function<String, String> converter) {
		String result = entry;
		if (replacements != null && replacements.containsKey(entry))
			result = replacements.get(entry);
		if (converter != null)
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
		return createCSSDeclaration(property, value, type.replacements(), type.converter());
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
		return convertCSSPropertyTo(property, replacements, converter) + CSS_PROPERTY_VALUE_SEPARATOR + " " + value
		        + CSS_SEPARATOR;
	}

	/**
	 * Creates a CSS block from a map of declarations with Property => Value. This method expects all
	 * declarations to already have a separator at their end.
	 * 
	 * @param styles      a collection
	 * @param outputStyle style of the block
	 * @return one string with all declarations
	 */
	public static String createCSSBlock(Map<String, String> styles, CSSBlockStyle outputStyle) {
		return createCSSBlock(styles, outputStyle, CSSType.STANDARD);
	}

	/**
	 * Creates a CSS block from a collection of declarations. This method expects all declarations to
	 * already have a separator at their end.
	 * 
	 * @param styles      a collection
	 * @param outputStyle style of the block
	 * @return one string with all declarations
	 */
	public static String createCSSBlock(Map<String, String> styles, CSSBlockStyle outputStyle, CSSType converter) {
		Collection<String> collected = new ArrayList<>();
		collected.addAll(styles.entrySet().stream()
		        .map(e -> StyleUtils.createCSSDeclarationOfType(e.getKey(), e.getValue(), converter))
		        .collect(Collectors.toList()));
		return String.join(outputStyle.getSeparator(), collected);
	}

	/**
	 * Creates a human-readable list of strings from the given dictionary of styles.
	 * 
	 * @param styles dictionary of styles with property => value
	 * @return a list of styles alphabetically ordered
	 */
	public static List<String> toHumanList(Map<String, String> styles) {
		List<String> parts = new ArrayList<>();
		List<String> fontStyles = new ArrayList<>();
		for (Entry<String, String> entry : styles.entrySet()) {
			switch (entry.getKey()) {
				case "font-style":
				case "font-weight":
					if (!entry.getValue().isEmpty())
						fontStyles.add(entry.getValue());
					break;
				case "text-decoration":
					parts.add("Decoration: " + String.join(", ", entry.getValue().split("\\s* \\s*")));
					break;
				case "bg-color":
					parts.add("Background: " + entry.getValue());
					break;
				default:
					parts.add(StringUtils.capitalize(entry.getKey().replace('-', ' ')) + CSS_PROPERTY_VALUE_SEPARATOR
					        + " " + entry.getValue());
					break;
			}
		}
		if (!fontStyles.isEmpty()) {
			Collections.sort(fontStyles);
			parts.add("Styles: " + String.join(", ", fontStyles));
		}
		Collections.sort(parts);
		return parts;
	}

	/**
	 * Creates a human-readable inline string from the given dictionary of styles.
	 * 
	 * @param styles dictionary of styles with property => value
	 * @return a string
	 */
	public static String toHumanString(Map<String, String> styles) {
		return String.join(CSS_SEPARATOR + " ", toHumanList(styles));
	}

	/**
	 * Takes a block of CSS and creates a map of the styles with Property => Value.
	 * 
	 * @param block a CSS block with one or several declarations
	 * @return a map
	 */
	public static Map<String, String> strToMap(String block) {
		return strToMap(block, false);
	}

	/**
	 * Takes a block (inline or block) of CSS and creates a map of the styles with Property => Value.
	 * 
	 * @param block         a CSS block with one or several declarations
	 * @param preserveOrder preserves the order of the declaration in the map if true
	 * @return a map with Property => Value
	 */
	public static Map<String, String> strToMap(String block, boolean preserveOrder) {
		Map<String, String> styles = preserveOrder ? new LinkedHashMap<String, String>() : new HashMap<>();
		String[] declarations = block.split("\\s*" + CSS_SEPARATOR + "\\s*");
		for (String declaration : declarations) {
			String[] parts = declaration.split("\\s*:\\s*");
			styles.put(parts[0], parts[1]);
		}
		return styles;
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private StyleUtils() {
		// Nothing to see here
	}

}
