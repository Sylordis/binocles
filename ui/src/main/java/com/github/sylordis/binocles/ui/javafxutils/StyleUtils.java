package com.github.sylordis.binocles.ui.javafxutils;

import javafx.scene.paint.Color;

/**
 * Util tools for managing colours.
 */
public final class StyleUtils {

	/**
	 * Converts a CSS Property into its JavaFX CSS declaration for styling. They are mainly the same,
	 * but prefixed with <code>-fx-</code> and some properties are changed. This method takes care of
	 * that.
	 * 
	 * @param entry
	 * @return
	 */
	public static String convertCSStoFXCSS(String entry) {
		String result = entry;
		if (entry.equals("color"))
			result = "fill";
		result = "-fx-" + result;
		return result;
	}

	/**
	 * Converts a CSS Property into its JavaFX CSS style declaration for styling. They are mainly the
	 * same, but prefixed with <code>-fx-</code> and some properties are changed. This method takes care
	 * of that.<br/>
	 * 
	 * The result should be <pre>-fx-&lt;entry&gt;: &lt;value&gt;;</pre>.
	 * 
	 * @param property
	 * @param value
	 * @return the full css style declaration
	 */
	public static String convertCSStoFXstyle(String property, String value) {
		return convertCSStoFXCSS(property) + ": " + value + ";";
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
	 * Private constructor to prevent instantiation.
	 */
	private StyleUtils() {
		// Nothing to see here
	}

}
