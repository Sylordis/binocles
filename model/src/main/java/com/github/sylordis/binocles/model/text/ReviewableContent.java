package com.github.sylordis.binocles.model.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.utils.Identifiable;

/**
 * Reviewable content is a class to be inherited for all content that can hold comments. It will
 * provide the class the capacity to receive a general comment and to set comments.
 * 
 * @author sylordis
 *
 */
public abstract class ReviewableContent implements Identifiable, Serializable {

	private static final long serialVersionUID = -7788297468267195354L;

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
	 * 
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
	 * Checks if the global comment exists and is not blank.
	 * 
	 * @return
	 */
	public boolean hasGlobalComment() {
		return generalComment != null && !generalComment.isBlank();
	}

	/**
	 * Checks if this content has comments other than the global comment.
	 * 
	 * @return
	 */
	public boolean hasComments() {
		return !comments.isEmpty();
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
	 * 
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
		if (generalComment == null)
			this.generalComment = "";
		else
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

	/**
	 * Checks if this instance has children. If this returns true, then the method
	 * {@link #getChildren()} should return a non empty list. If this item is not supposed to have
	 * children, this method should return false.
	 * 
	 * @return true if this object can have children and has some, false otherwise.
	 */
	public boolean hasChildren() {
		return false;
	}

	/**
	 * Gets the list of children of this object, or null if it's not suppose to have some.
	 * 
	 * @return list of children or null if it's not supposed to have any.
	 */
	public List<? extends ReviewableContent> getChildren() {
		return null;
	}

}
