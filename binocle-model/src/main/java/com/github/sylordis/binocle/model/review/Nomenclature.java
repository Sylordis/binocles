package com.github.sylordis.binocle.model.review;

import java.util.ArrayList;
import java.util.List;

import com.github.sylordis.binocle.model.common.Identifiable;
import com.google.common.base.Preconditions;

/**
 * Overall legend configuration for the review, with all types of comments.
 *
 * @author Sylordis
 *
 */
public class Nomenclature implements Identifiable {

	/**
	 * Name of the legend.
	 */
	private String name;
	/**
	 * All types that can be found in this legend, ordered by the user. This cannot be null.
	 */
	private List<CommentType> types;

	/**
	 * Creates a new Legend configuration without any types.
	 *
	 * @param name name of the configuration
	 */
	public Nomenclature(String name) {
		this(name, new ArrayList<CommentType>());
	}

	/**
	 * Creates a new Legend configuration with an already existing list of types.
	 *
	 * @param name  name of the configuration
	 * @param types List of types
	 * @throws NullPointerException     if either name or types is null
	 * @throws IllegalArgumentException if name is blank
	 */
	public Nomenclature(String name, List<CommentType> types) {
		Preconditions.checkNotNull(name, "Legend configuration name should not be null");
		Preconditions.checkArgument(!name.isBlank(), "Legend configuration name should not be blank");
		Preconditions.checkNotNull(types, "Legend configuration types list should not be null");
		this.name = name;
		this.types = types;
	}

	@Override
	public String getId() {
		return name.toLowerCase();
	}
	
	@Override
	public String toString() {
		return name;
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
	 * @throws IllegalArgumentException if name is blank
	 */
	public void setName(String name) {
		Preconditions.checkNotNull(name, "Legend configuration name should not be null");
		Preconditions.checkArgument(!name.isBlank(), "Legend configuration name should not be blank");
		this.name = name;
	}

	/**
	 * @return the types
	 */
	public List<CommentType> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 * @throws NullPointerException if provided types is null
	 */
	public void setTypes(List<CommentType> types) {
		Preconditions.checkNotNull(types, "Legend configuration types list should not be null");
		this.types = types;
	}

	/**
	 * Moves the specified type to a new index, if it exists in the configuration.
	 *
	 * @param type  type to move
	 * @param index index where to move the type to
	 */
	public void setTypeIndex(CommentType type, int index) {
		if (type != null && types.contains(type)) {
			types.remove(type);
			types.add(index, type);
		}
	}

}
