package com.github.sylordis.binocles.model.review;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.github.sylordis.binocles.utils.Identifiable;

/**
 * Reviewable content is a class to be inherited for all content that can hold comments.
 * 
 * @author sylordis
 *
 */
public abstract class ReviewableContent implements Identifiable {

	/**
	 * The global comment on the content.
	 */
	private String globalComment;

	/**
	 * List of comments of the content. This list cannot be null.
	 */
	private Set<Comment> comments;

	/**
	 * Creates a new reviewable content, initialising all fields to defaults.
	 */
	public ReviewableContent() {
		this.globalComment = "";
		this.comments = new TreeSet<>();
	}

	/**
	 * Gets the title of the content.
	 * @return
	 */
	public abstract String getTitle();
	
	/**
	 * Gets the global comment on the content.
	 * 
	 * @return
	 */
	public String getGlobalComment() {
		return globalComment;
	}

	/**
	 * Gets the comments on the content.
	 * 
	 * @return
	 */
	public Set<Comment> getComments() {
		return comments;
	}

	/**
	 * Adds a comment if it is not null.
	 * 
	 * @param comment
	 * @see Set#add(Object)
	 */
	public void addComment(Comment comment) {
		if (comment != null) {
			this.comments.add(comment);
		}
	}

	/**
	 * Removes a comment from the set of comments if it exists
	 * 
	 * @param comment comment to be removed
	 * @return true if a comment was removed
	 * @see Set#remove(Object)
	 */
	public boolean removeComment(Comment comment) {
		return this.comments.remove(comment);
	}

	/**
	 * Removes all registered comments.
	 */
	public void removeAllComments() {
		this.comments.clear();
	}

	/**
	 * @param globalComment the globalComment to set
	 */
	public void setGlobalComment(String globalComment) {
		this.globalComment = globalComment;
	}

	/**
	 * Replaces the set of comments from the content. If the collection is null or empty, the new set
	 * will be empty. This method is null safe.
	 * 
	 * @param comments the comments to set
	 */
	public void setComments(Collection<Comment> comments) {
		this.comments.clear();
		if (comments != null)
			this.comments.addAll(comments);
	}

}
