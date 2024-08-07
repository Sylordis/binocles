package com.github.sylordis.binocles.model.review;

import java.util.Map;

/**
 * This comment type is common to all nomenclature and is the default type for all comments without
 * a type following a nomenclature change. It should not be available to chose when creating a new
 * comment.
 * 
 * @author sylordis
 *
 */
public final class OrphanType extends CommentType {

	private static final long serialVersionUID = -3082866966943048660L;

	/**
	 * Creates the type.
	 */
	public OrphanType() {
		super("Orphan", "This type is assigned automatically to a comment which has no other type");
		this.editFields(Map.of("content", "Content of the comment"));
	}

}
