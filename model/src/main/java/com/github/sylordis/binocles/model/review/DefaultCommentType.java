package com.github.sylordis.binocles.model.review;

/**
 * Default comment type with just a field for text.
 */
public class DefaultCommentType extends CommentType {

	private static final long serialVersionUID = 5459454206697003927L;

	/**
	 * Name for default comment type.
	 */
	public static final String NAME = "Comment";

	/**
	 * Creates a new default comment type.
	 */
	public DefaultCommentType() {
		super(NAME);
		this.setField("text", "Text of the comment.");
		// TODO Be able to change default comment type style in preferences
		this.setStyle("color", "#4b5da1");
		this.setStyle("font-style", "italic");
	}

}
