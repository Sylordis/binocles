package com.github.sylordis.binocles.model.review;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * Conversion class holding the mapping for fields substitutions.
 *
 * @author Sylordis
 *
 */
public class CommentTypeMigration implements Consumer<Comment> {

	/**
	 * Future type to be applied.
	 */
	private CommentType future;
	/**
	 * All substitutions to be done, with old field => new field mapping. This map cannot be null.
	 */
	private Map<String, String> substitutions;

	/**
	 * Constructs a new type change without substitutions.
	 *
	 * @param origin        type to be changed
	 * @param future        type to be applied
	 */
	public CommentTypeMigration(CommentType future) {
		this(future, null);
	}

	/**
	 * Constructs a new type change.
	 *
	 * @param origin        type to be changed
	 * @param future        type to be applied
	 * @param substitutions to be done, old field => new field mapping
	 */
	public CommentTypeMigration(CommentType future,
			Map<String, String> substitutions) {
		this.future = future;
		this.substitutions = new HashMap<>();
		if (substitutions != null)
			this.substitutions.putAll(substitutions);
	}

	@Override
	public void accept(Comment t) {
		if (future == null)
			throw new UnsupportedOperationException("Can't operate a type change without all parameters setup");
		// Change fields
		Map<String, String> fields = new HashMap<>();
		for (Entry<String, String> e : substitutions.entrySet()) {
			// Take field from the comment which matches the substitution key and transpose it into the new
			// fields.
			String currentValue = t.getFields().get(e.getKey());
			if (currentValue != null)
				fields.put(e.getValue(), currentValue);
		}
		t.setType(future);
		t.setFields(fields);
	}

	/**
	 * @return the future
	 */
	public CommentType getFuture() {
		return future;
	}

	/**
	 * @param future the future to set
	 */
	public void setFuture(CommentType future) {
		this.future = future;
	}

	/**
	 * @return the substitutions
	 */
	public Map<String, String> getSubstitutions() {
		return substitutions;
	}

	/**
	 * Replaces the substitutions entries with the provided ones.
	 *
	 * @param substitutions the substitutions to set
	 */
	public void setSubstitutions(Map<String, String> substitutions) {
		this.substitutions.clear();
		if (substitutions != null)
			this.substitutions.putAll(substitutions);
	}

	/**
	 * Removes all configuration substitutions. The Map will be empty.
	 */
	public void resetSubstitutions() {
		this.substitutions.clear();
	}
}
