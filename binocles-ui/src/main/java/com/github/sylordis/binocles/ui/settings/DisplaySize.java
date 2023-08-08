package com.github.sylordis.binocles.ui.settings;

/**
 * Configuration display size.
 * @author sylordis
 *
 */
public enum DisplaySize {

	SMALL(10, 16),
	NORMAL(12, 16),
	BIG(18, 24);
	
	private final Integer fontSize;
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
