package com.github.sylordis.binocles.model.review;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.sylordis.binocles.contracts.Identifiable;
import com.github.sylordis.binocles.contracts.SelfCopying;
import com.google.common.base.Preconditions;

/**
 * Overall legend configuration for the review, with all types of comments.
 *
 * @author Sylordis
 *
 */
public class Nomenclature implements NomenclatureItem, Identifiable, Serializable, SelfCopying<Nomenclature> {

	private static final long serialVersionUID = 4661489455335692980L;
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
	 * @see #Nomenclature(String, List)
	 */
	public Nomenclature(String name) {
		this(name, null);
	}

	/**
	 * Creates a new Legend configuration with an already existing list of types.
	 *
	 * @param name  name of the configuration
	 * @param types List of types, creates a default list if null provided
	 * @throws NullPointerException     if name is null
	 * @throws IllegalArgumentException if name is blank
	 */
	public Nomenclature(String name, List<CommentType> types) {
		Preconditions.checkNotNull(name, "Nomenclature name should not be null");
		Preconditions.checkArgument(!name.isBlank(), "Nomenclature name should not be blank");
		this.name = name;
		if (types == null)
			this.types = new ArrayList<CommentType>();
		else
			this.types = types;
	}

	@Override
	public String getId() {
		return Identifiable.formatId(name);
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
	 * Checks that the nomenclature has a given comment type, matching on ID.
	 * 
	 * @param n
	 * @see Identifiable#is(String)
	 * @return
	 */
	public boolean hasCommentType(CommentType type) {
		return this.types.stream().anyMatch(c -> c.is(type));
	}

	/**
	 * Checks that the nomenclature has a given comment type, matching on ID.
	 * 
	 * @param n
	 * @see Identifiable#is(String)
	 * @return
	 */
	public boolean hasCommentType(String id) {
		return this.types.stream().anyMatch(c -> c.is(id));
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

	/**
	 * Indicates if this nomenclature is the default one. This should return false for all nomenclature
	 * except for the default one. Having several default nomenclatures is not recommended.
	 * 
	 * @return
	 */
	public boolean isDefaultNomenclature() {
		return false;
	}

	@Override
	public void copy(Nomenclature item) {
		this.name = item.name;
	}

}
