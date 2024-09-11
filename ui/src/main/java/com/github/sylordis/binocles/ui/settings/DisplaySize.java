package com.github.sylordis.binocles.ui.settings;

/**
 * Configuration display size according to preset categories.
 * 
 * @author sylordis
 *
 */
public enum DisplaySize {

    // Font size, icon max size.
	SMALL(10, 16),
	MEDIUM(12, 16),
	LARGE(14, 24),
	BIG(18, 24);

	/**
	 * Gets a {@link DisplaySize} according to its font size.
	 * 
	 * @param fontSize
	 * @return The display size matching the font size or null if there's none.
	 */
	public static DisplaySize getFromSize(int fontSize) {
		DisplaySize result = null;
		for (DisplaySize item : values()) {
			if (item.getFontSize() == fontSize) {
				result = item;
				break;
			}
		}
		return result;
	}

	/**
	 * Font size for display according to display size.
	 */
	private final int fontSize;
	/**
	 * Icon max size in pixels according to display size.
	 */
	private final int iconMaxSize;

	private DisplaySize(int fontSize, int iconMaxSize) {
		this.fontSize = fontSize;
		this.iconMaxSize = iconMaxSize;
	}

	/**
	 * @return the fontSize
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * @return the iconMaxSize
	 */
	public int getIconMaxSize() {
		return iconMaxSize;
	}

}
