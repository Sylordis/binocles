package com.github.sylordis.binocles.model.review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.github.sylordis.binocles.utils.Identifiable;

/**
 * Reviewable content is a class to be inherited for all content that can hold comments.
 * It will provide the class the capacity to receive a general comment and to set comments.
 * 
 * @author sylordis
 *
 */
public abstract class ReviewableContent implements Identifiable {

	/**
	 * The general comment on the content.
	 */
	private String generalComment;

	/**
	 * List of comments of the content. This list cannot be null.
	 */
	private List<Comment> comments;

	/**
	 * Creates a new reviewable content, initialising all fields to defaults.
	 */
	public ReviewableContent() {
		this.generalComment = "";
		this.comments = new ArrayList<>();
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
		return generalComment;
	}

	/**
	 * Gets the comments on the content.
	 * 
	 * @return
	 */
	public List<Comment> getComments() {
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
	 * Replaces the current list of comments. Null objects will be ignored.
	 * @param comments
	 */
	public void setComments(List<Comment> comments) {
		this.comments.clear();
		if (null != comments) {
			for (Comment comment : comments) {
				if (null != comment)
					this.comments.add(comment);
			}
		}
	}

	/**
	 * Removes all registered comments.
	 */
	public void removeAllComments() {
		this.comments.clear();
	}

	/**
	 * @param generalComment the globalComment to set
	 */
	public void setGeneralComment(String generalComment) {
		this.generalComment = generalComment;
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
