package com.github.sylordis.binocles.ui.doa;

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

}
