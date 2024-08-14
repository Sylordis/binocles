package com.github.sylordis.binocles.model.review;

import java.util.List;

import com.github.sylordis.binocles.model.BinoclesConfiguration;
import com.github.sylordis.binocles.utils.StyleUtils;

/**
 * Default comment type with just a field for text.
 */
public class DefaultCommentType extends CommentType {

	private static final long serialVersionUID = 5459454206697003927L;

	/**
	 * Name for default comment type.
	 */
	public static final String NAME = "Comment";

	private static final String FIELD_NAME = "text";
	private static final String FIELD_DESCRIPTION = "Text of the comment";

	/**
	 * Creates a new default comment type.
	 */
	public DefaultCommentType() {
		super(NAME);
		this.setFields(List.of(new CommentTypeField(FIELD_NAME, FIELD_DESCRIPTION, true)));
		this.editStyles(StyleUtils.strToMap(BinoclesConfiguration.getInstance().getDefaultCommentTypeStyle(), true));
	}

}
