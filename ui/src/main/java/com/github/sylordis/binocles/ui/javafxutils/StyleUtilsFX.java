package com.github.sylordis.binocles.ui.javafxutils;

import java.util.Map;

import com.github.sylordis.binocles.utils.StyleUtils.CSSType;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public final class StyleUtilsFX {

	/**
	 * CSS to FXCSS property replacement map.
	 */
	private final static Map<String, String> cssToFxcssDictionary = Map.of("color", "fill");
	/**
	 * JavaFX CSS style, where all properties are written <code>-fx-&lt;property&gt;</code> with some
	 * replacements.
	 * 
	 * @see #cssToFxcssDictionary
	 */
	public final static CSSType JAVA_FX = new CSSType(cssToFxcssDictionary, e -> "-fx-" + e);

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
	 * Transforms a color text representation to its equivalent Color.
	 * 
	 * @param hex
	 * @return
	 */
	public static Color fromHex(String hex) {
		return Color.valueOf(hex);
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
	 * Private construction to prevent instantiation.
	 */
	private StyleUtilsFX() {
		// Nothing to see here
	}

}