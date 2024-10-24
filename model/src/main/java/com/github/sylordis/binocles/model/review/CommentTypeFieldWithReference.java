package com.github.sylordis.binocles.model.review;

/**
 * Meta fields for comment types.
 */
public class CommentTypeFieldWithReference extends CommentTypeField {

	/**
	 * Name of the field this field should migrate from.
	 */
	private CommentType reference;

	public CommentTypeFieldWithReference(String name, String description, boolean isLongText, CommentType reference) {
		super(name, description, isLongText);
		this.reference = reference;
	}

	public CommentTypeFieldWithReference(String name, String description, boolean isLongText) {
		this(name, description, isLongText, null);
	}

	public CommentTypeFieldWithReference(String name, String description) {
		this(name, description, false, null);
	}

	/**
	 * @return the reference
	 */
	public CommentType getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(CommentType reference) {
		this.reference = reference;
	}

}
