package com.github.sylordis.binocles.model.review;

import java.io.Serializable;
import java.util.Objects;

/**
 * Meta fields for comment types.
 */
public class CommentTypeField implements Serializable {

	private static final long serialVersionUID = 8592334393810850293L;

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
	 * Creates a short text comment type field.
	 * 
	 * @param name
	 * @param description
	 */
	public CommentTypeField(String name, String description) {
		this(name, description, false);
	}

	/**
	 * Creates a comment type field.
	 * 
	 * @param name
	 * @param description
	 */
	public CommentTypeField(String name, String description, boolean isLongText) {
		this.name = name;
		this.description = description;
		this.isLongText = isLongText;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentTypeField other = (CommentTypeField) obj;
		return Objects.equals(description, other.description) && Objects.equals(isLongText, other.isLongText)
		        && Objects.equals(name, other.name);
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
	public boolean getIsLongText() {
		return isLongText;
	}

	@Override
	public String toString() {
		return "CommentTypeField [name=" + name + ", description=" + description + ", isLongText=" + isLongText + "]";
	}

}
