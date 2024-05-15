package com.github.sylordis.binocles.ui.settings;

/**
 * Configuration display size according to preset categories.
 * @author sylordis
 *
 */
public enum DisplaySize {

	SMALL(10, 16),
	NORMAL(12, 16),
	BIG(18, 24);
	
	/**
	 * Font size for display according to display size.
	 */
	private final Integer fontSize;
	/**
	 * Icon max size in pixels according to display size. 
	 */
	private final Integer iconMaxSize;
	
	private DisplaySize(Integer fontSize, Integer iconMaxSize) {
		this.fontSize = fontSize;
		this.iconMaxSize = iconMaxSize;
	}

	/**
	 * @return the fontSize
	 */
	public Integer getFontSize() {
		return fontSize;
	}

	/**
	 * @return the iconMaxSize
	 */
	public Integer getIconMaxSize() {
		return iconMaxSize;
	}
	
	
}
