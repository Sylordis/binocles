package com.github.sylordis.binocle.ui.settings;

public enum DisplaySize {

	SMALL(10, 16),
	NORMAL(12, 16),
	BIG(18, 32);
	
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
