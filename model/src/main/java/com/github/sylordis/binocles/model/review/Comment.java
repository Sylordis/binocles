package com.github.sylordis.binocles.model.review;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * Class representing a comment on a beta read text. Each comment is defined by its type, its start
 * index, end index and different fields it should have according to its type.<br/>
 * Basic comparator between comments compare their start indexes.
 *
 * @author Sylordis
 *
 */
public class Comment implements Comparable<Comment>, Serializable {

	private static final long serialVersionUID = 8636110855533266235L;

	/**
	 * Type of the comment, linked to configuration.
	 */
	private CommentType type;
	/**
	 * Index of the start of the comment in the text.
	 */
	private int startIndex;
	/**
	 * Index of the end of the comment in the text.
	 */
	private int endIndex;
	/**
	 * All fields with content for this comment, according to its type and current legend configuration.
	 * Association is: field name => field value. This map should never be null.
	 */
	private Map<String, String> fields;

	/**
	 * Constructs a new comment without any values for fields. The base fields keys will be created from
	 * the type via {@link #setType(CommentType)}.
	 *
	 * @param type  type of the comment
	 * @param start start index of the comment
	 * @param end   end index of the comment
	 * @throws IllegalArgumentException if the new end position is before the start position
	 * @throws IllegalArgumentException if the new end position is not a positive integer
	 */
	public Comment(CommentType type, int start, int end) {
		this(type, start, end, new HashMap<>());
	}

	/**
	 * Full constructor for a new comment.
	 *
	 * @param type   type of the comment
	 * @param start  start index of the comment
	 * @param end    end index of the comment
	 * @param fields different content fields according to type
	 * @throws IllegalArgumentException if the new end position is before the start position
	 * @throws IllegalArgumentException if the new end position is not a positive integer
	 */
	public Comment(CommentType type, int start, int end, Map<String, String> fields) {
		Preconditions.checkArgument(start <= end, "The end of a comment cannot be before its start");
		Preconditions.checkArgument(start >= 0, "The start of a comment should be a positive integer.");
		this.type = type;
		this.startIndex = start;
		this.endIndex = end;
		this.fields = new LinkedHashMap<>();
		if (fields != null)
			this.fields.putAll(fields);
	}

	@Override
	public int compareTo(Comment o) {
		int ret = Integer.compare(this.startIndex, o.startIndex);
		if (ret == 0)
			ret = Integer.compare(this.endIndex, o.endIndex);
		return ret;
	}

	@Override
	public int hashCode() {
		return Objects.hash(endIndex, fields, startIndex, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		return endIndex == other.endIndex && startIndex == other.startIndex && Objects.equals(fields, other.fields)
		        && Objects.equals(type, other.type);
	}

	/**
	 * Copies all fields of the provided comment.
	 * 
	 * @param comment comment to copy
	 */
	public void copy(Comment comment) {
		this.setType(comment.getType());
		this.setStartIndex(comment.getStartIndex());
		this.setEndIndex(comment.getEndIndex());
		this.setFields(comment.getFields());
	}

	/**
	 * Gets the current set type.
	 *
	 * @return
	 */
	public CommentType getType() {
		return this.type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 */
	public void setType(CommentType type) {
		this.type = type;
	}

	/**
	 * Gets the start index.
	 *
	 * @return
	 */
	public int getStartIndex() {
		return this.startIndex;
	}

	/**
	 * Sets the start index.
	 *
	 * @param start
	 * @throws IllegalArgumentException if the new start index is not a positive integer
	 * @throws IllegalArgumentException if the new start index is greater than the end index
	 */
	public void setStartIndex(int start) {
		Preconditions.checkArgument(start >= 0, "The start of a comment should be a positive integer.");
		Preconditions.checkArgument(start <= this.endIndex, "The start of a comment cannot be after the end.");
		this.startIndex = start;
	}

	/**
	 * Gets the end index.
	 *
	 * @return
	 */
	public int getEndIndex() {
		return this.endIndex;
	}

	/**
	 * Sets the end index.
	 *
	 * @param end
	 * @throws IllegalArgumentException if the new end position is lower the start position
	 */
	public void setEndIndex(int end) {
		Preconditions.checkArgument(this.startIndex <= end, "The end of a comment cannot be before its start");
		this.endIndex = end;
	}

	/**
	 * Sets both start and end indexes.
	 *
	 * @param start
	 * @param end
	 * @throws IllegalArgumentException if the new start index is not a positive integer
	 * @throws IllegalArgumentException if the new start index is greater than the end index
	 */
	public void setBoundaries(int start, int end) {
		Preconditions.checkArgument(start >= 0, "The start of a comment should be a positive integer.");
		Preconditions.checkArgument(start <= end, "The start of a comment cannot be after the end.");
		this.startIndex = start;
		this.endIndex = end;
	}

	/**
	 * Sets start and end indexes according to provided indexes. Lowest one will be the start, Highest
	 * one will be the end.
	 *
	 * @param index1 First index to set
	 * @param index2 Second index to set
	 * @throws IllegalArgumentException if any of the provided index is negative.
	 */
	public void setAdaptativeBoundaries(int index1, int index2) {
		Preconditions.checkArgument(index1 >= 0 && index2 >= 0,
		        "The start and end indexes of a comment should be positive integers.");
		this.startIndex = Math.min(index1, index2);
		this.endIndex = Math.max(index1, index2);
	}

	/**
	 * Gets all fields.
	 *
	 * @return
	 */
	public Map<String, String> getFields() {
		return this.fields;
	}

	/**
	 * Sets a field if the key is present in the type. Does nothing if either the type is not set (null)
	 * or the type does not have the field in its configuration.
	 *
	 * @param key   name of the field
	 * @param value value of the field, empty string if null provided
	 */
	public void setFieldFromType(String key, String value) {
		Preconditions.checkNotNull(key, "Cannot have a null field name");
		Preconditions.checkArgument(!key.isBlank(), "Name of the field cannot be blank");
		if (this.type != null && this.type.hasField(key))
			setField(key, value);
	}

	/**
	 * Sets a specific field and its value
	 *
	 * @param key
	 * @param value the value, empty string if null provided
	 * @throws NullPointerException     if the key is null
	 * @throws IllegalArgumentException if the key is blank
	 */
	public void setField(String key, String value) {
		Preconditions.checkNotNull(key, "Cannot have a null field name");
		Preconditions.checkArgument(!key.isBlank(), "Name of the field cannot be blank");
		this.fields.put(key, value == null ? "" : value);
	}

	/**
	 * Sets all fields, replacing previous ones. Calling {@link #setField(String, String)} for each
	 * value. The state of the map is uncertain is any of the keys is null or blank.
	 *
	 * @precondition No key is null or blank
	 * @param fields
	 * @see #setField(String, String)
	 */
	public void setFields(Map<String, String> fields) {
		this.fields.clear();
		if (fields != null)
			fields.forEach(this::setField);
	}

	/**
	 * Removes a single entry from the fields.
	 *
	 * @param key
	 * @return the removed value if any entry was removed
	 */
	public String removeField(String key) {
		return this.fields.remove(key);
	}

	/**
	 * Removes all fields.
	 */
	public void clearFields() {
		this.fields.clear();
	}

	@Override
	public String toString() {
		return "Comment[" + startIndex + "," + endIndex + "] [type=" + type + ", " + fields + "]";
	}

}
