package com.github.sylordis.binocle.model.review;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.sylordis.binocle.utils.MapUtils;

import com.google.common.base.Preconditions;

/**
 * Legend item, holding all information on a specific item of the legend. At any moment, none of the
 * fields of this class can be null, but maps can be empty.
 *
 * @author Sylordis
 *
 */
public class CommentType implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6537291946995941018L;
	/**
	 * Name of the item, cannot be null or empty.
	 */
	private String name;
	/**
	 * Description of the type.
	 */
	private String description;
	/**
	 * Style to apply to all comments with this item, with an association property name => value.
	 * Property name cannot be blank or null. Default used is CSS.
	 */
	private Map<String,String> styles;
	/**
	 * All fields a comment of this type should have, with an association name => description. Field
	 * name cannot be empty or null.
	 */
	private Map<String,String> fields;

	/**
	 * Creates an empty new legend configuration type without description. All maps are instantiated to
	 * empty ones.
	 *
	 * @param name name of the type
	 * @throws NullPointerException     if name is null
	 * @throws IllegalArgumentException if name is empty
	 */
	public CommentType(String name) {
		this(name, "");
	}

	/**
	 * Creates an empty new legend configuration type. All maps are instantiated to empty ones.
	 *
	 * @param name name of the type
	 * @throws NullPointerException     if name is null
	 * @throws IllegalArgumentException if name is blank
	 */
	public CommentType(String name, String description) {
		Preconditions.checkNotNull(name, "Legend configuration type name cannot be null");
		Preconditions.checkArgument(!name.isBlank(), "Legend configuration type name cannot be empty");
		this.name = name;
		this.description = description == null ? "" : description;
		this.styles = new HashMap<>();
		this.fields = new HashMap<>();
	}

	/**
	 * Normalising method for Legend configuration type ID. If the provided type is null, returns an
	 * empty string.
	 *
	 * @return a normalised ID
	 */
	public String getID() {
		return this.name.toLowerCase();
	}

	/**
	 * Checks if the provided type is the same than this type, based on matching IDs.
	 *
	 * @param t type to check
	 * @return true if t is not null and ids match.
	 * @see #getID(CommentType)
	 */
	public boolean is(CommentType t) {
		return t != null && getID().equals(t.getID());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 * @throws NullPointerException     if name is null
	 * @throws IllegalArgumentException if name is empty
	 */
	public void setName(String name) {
		Preconditions.checkNotNull(name, "Legend configuration type name cannot be set to null");
		Preconditions.checkArgument(!name.isBlank(), "Legend configuration type name cannot be blank");
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets a new description for the type. If provided null, will set the description to an empty
	 * string.
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		if (description == null)
			this.description = "";
		else
			this.description = description;
	}

	/**
	 * @return the fields as an unmodifiable view
	 */
	public Map<String, String> getFields() {
		return Collections.unmodifiableMap(fields);
	}

	/**
	 * Edit fields according to provided input by calling {@link #setField(String, String)} for each
	 * entry.
	 *
	 * @param fields the fields to set
	 */
	public void editFields(Map<String, String> fields) {
		Preconditions.checkNotNull(fields, "Legend configuration type provided fields cannot be null");
		fields.forEach(this::setField);
	}

	/**
	 * Checks if the type has a particular field.
	 *
	 * @param key name of the field
	 * @return
	 */
	public boolean hasField(String key) {
		return fields.containsKey(key);
	}

	/**
	 * Replaces previous fields settings.
	 *
	 * @param fields new fields
	 */
	public void setFields(Map<String, String> fields) {
		this.fields.clear();
		if (fields != null)
			this.fields.putAll(fields);
	}

	/**
	 * Resets all registered fields.
	 */
	public void resetFields() {
		this.fields.clear();
	}

	/**
	 *
	 * Sets a specific field if the key is not null and not blank. If the value is null, removes the
	 * entry instead.
	 *
	 * @param name
	 * @param description
	 * @throws NullPointerException     if name is null
	 * @throws IllegalArgumentException if name is empty
	 */
	public void setField(String name, String description) {
		Preconditions.checkNotNull(name, "Legend configuration type field name cannot be set to null");
		Preconditions.checkArgument(!name.isBlank(), "Legend configuration type field name cannot be blank");
		MapUtils.putOrRemoveIfNull(this.fields, name, description);
	}

	/**
	 * @return the styles as an unmodifiable view
	 */
	public Map<String, String> getStyles() {
		return Collections.unmodifiableMap(styles);
	}

	/**
	 * Edit styles according to provided input by calling {@link #setStyle(String, String)} for each
	 * entry.
	 *
	 * @param styles the styles to set
	 */
	public void editStyles(Map<String, String> styles) {
		Preconditions.checkNotNull(styles, "Legend configuration type provided styles cannot be null");
		styles.forEach(this::setStyle);
	}

	/**
	 * Resets all registered styles.
	 */
	public void resetStyles() {
		this.styles.clear();
	}

	/**
	 * Sets a specific style if the key is not null and not blank. If the value is null, removes the
	 * entry instead.
	 *
	 * @param key   name of the style property
	 * @param value value of the style property
	 * @throws NullPointerException     if key is null
	 * @throws IllegalArgumentException if key is empty
	 */
	public void setStyle(String key, String value) {
		Preconditions.checkNotNull(key, "Legend configuration type style property name cannot be set to null");
		Preconditions.checkArgument(!key.isBlank(), "Legend configuration type style property name cannot be blank");
		MapUtils.putOrRemoveIfNull(this.styles, key, value);
	}

}
