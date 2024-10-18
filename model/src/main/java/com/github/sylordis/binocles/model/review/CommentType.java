package com.github.sylordis.binocles.model.review;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.github.sylordis.binocles.utils.MapUtils;
import com.github.sylordis.binocles.utils.contracts.Identifiable;
import com.google.common.base.Preconditions;

/**
 * Legend item, holding all information on a specific item of the legend. At any moment, none of the
 * fields of this class can be null, but maps can be empty.
 *
 * @author Sylordis
 *
 */
public class CommentType implements Serializable, NomenclatureItem, Identifiable {

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
	private Map<String, String> styles;
	/**
	 * All fields a comment of this type should have, with an association name => description. Field
	 * name cannot be empty or null.
	 */
	private Map<String, CommentTypeField> fields;

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
		this.fields = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getId() {
		return Identifiable.formatId(this.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, fields, name, styles);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentType other = (CommentType) obj;
		return Objects.equals(description, other.description) && Objects.equals(fields, other.fields)
		        && Objects.equals(name, other.name) && Objects.equals(styles, other.styles);
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
	public Map<String, CommentTypeField> getFields() {
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
			fields.forEach(this::setField);
	}

	/**
	 * Replaces previous fields settings.
	 *
	 * @param fields new fields
	 */
	public void setFields(Collection<CommentTypeField> fields) {
		this.fields.clear();
		if (fields != null)
			fields.forEach(this::setField);
	}

	/**
	 * Resets all registered fields.
	 */
	public void resetFields() {
		this.fields.clear();
	}

	/**
	 *
	 * Sets a specific field if not null. If the description is null, removes the entry instead.
	 *
	 * @param field
	 * @throws NullPointerException if field or its name are null
	 */
	public void setField(CommentTypeField field) {
		Preconditions.checkNotNull(field, "Comment type field cannot be null");
		Preconditions.checkNotNull(field.getName(), "Comment type field name cannot be null");
		if (field.getDescription() != null) {
			fields.put(field.getName(), field);
		} else
			fields.remove(field.getName());
	}

	/**
	 *
	 * Sets a specific field if the key is not null and not blank. If the description is null, removes
	 * the entry instead.
	 *
	 * @param name
	 * @param description
	 * @throws NullPointerException     if name is null
	 * @throws IllegalArgumentException if name is empty
	 */
	public void setField(String name, String description) {
		setField(name, description, false);
	}

	/**
	 *
	 * Sets a specific field if the key is not null and not blank. If the description is null, removes
	 * the entry instead.
	 *
	 * @param name
	 * @param description
	 * @param isLong
	 * @throws NullPointerException     if name is null
	 * @throws IllegalArgumentException if name is empty
	 */
	public void setField(String name, String description, boolean isLong) {
		Preconditions.checkNotNull(name, "Comment type field name cannot be set to null");
		Preconditions.checkArgument(!name.isBlank(), "Comment type field name cannot be blank");
		if (description != null) {
			fields.put(name, new CommentTypeField(name, description, isLong));
		} else
			fields.remove(name);
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

	/**
	 * Sets styles for this comment type, replacing any previous one set.
	 * 
	 * @param styles
	 */
	public void setStyles(Map<String, String> styles) {
		this.styles.clear();
		if (null != styles)
			this.styles.putAll(styles);
	}

}
