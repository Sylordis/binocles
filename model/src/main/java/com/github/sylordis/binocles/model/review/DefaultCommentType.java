package com.github.sylordis.binocles.model.review;

/**
 * Default comment type with just a field for text.
 */
public class DefaultCommentType extends CommentType {

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
	}

}
