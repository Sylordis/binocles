package com.github.sylordis.binocles.model.review;

/**
 * Meta fields for comment types.
 */
public class CommentTypeField {

	/**
	 * Name of the field.
	 */
	private String name;
	/**
	 * Description of the field.
	 */
	private String description;
	/**
	 * Whether this field is supposed to hold a long text.
	 */
	private Boolean isLongText;

	/**
	 * @param name
	 * @param description
	 */
	public CommentTypeField(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the fact that this type represents a long text or not.
	 * 
	 * @param isLongText
	 */
	public void setIsLongText(boolean isLongText) {
		this.isLongText = isLongText;
	}

	/**
	 * Whether this field is a long text or not.
	 * 
	 * @return
	 */
	public boolean isLongText() {
		return isLongText;
	}
}
