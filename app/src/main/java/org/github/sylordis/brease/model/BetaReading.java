package org.github.sylordis.brease.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.github.sylordis.brease.model.legend.LegendConfiguration;
import org.github.sylordis.brease.model.legend.LegendConfigurationChange;

/**
 * Base class for beta readings that contains the text and the comments, including the legend.
 *
 * @author Sylordis
 *
 */
public class BetaReading {

	/**
	 * Generic information provided by user.
	 */
	private TextDescription description;
	/**
	 * The text being beta read.
	 */
	private String text;
	/**
	 * All comments for the review. This variable should never be null.
	 */
	private final Set<Comment> comments;
	/**
	 * Global comments of the review.
	 */
	private String globalComment;
	/**
	 * Current annotation legend.
	 */
	private LegendConfiguration legend;

	/**
	 * Basic constructor for a BetaReading without any legend configuration.
	 */
	public BetaReading() {
		this(null);
	}

	/**
	 * Basic constructor for a BetaReading.
	 *
	 * @param legend base legend configuration
	 */
	public BetaReading(LegendConfiguration legend) {
		this.description = new TextDescription();
		this.text = "";
		this.globalComment = "";
		this.comments = new TreeSet<>();
		this.legend = legend;
	}

	/**
	 * Performs a change of legend configuration, converting all comments according
	 * to previous configuration in place.
	 *
	 * @param change to perform
	 * @return the number of comments changed
	 */
	public int reconfigureLegend(LegendConfigurationChange change) {
		int count = 0;
		for (Comment c : this.comments)
			if (change.changeComment(c))
				count++;
		this.legend = change.getNextConfiguration();
		return count;
	}

	/**
	 * @return the globalComment
	 */
	public String getGlobalComment() {
		return this.globalComment;
	}

	/**
	 * @param globalComment the globalComment to set
	 */
	public void setGlobalComment(String globalComment) {
		if (globalComment == null)
			this.globalComment = "";
		else
			this.globalComment = globalComment;
	}

	/**
	 * @return the description
	 */
	public TextDescription getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(TextDescription description) {
		this.description = description;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets a collection of comments that are out of bounds compared to current text.
	 *
	 * @return a collection of comments
	 * @see #isCommentOutOfTextBounds(Comment)
	 */
	public Collection<Comment> getCommentsOutOfBounds() {
		return this.comments.stream().filter(this::isCommentOutOfTextBounds).toList();
	}

	/**
	 * Checks if a comment has an index (start or end) out of bounds compared to the text.
	 *
	 * @param comment to check limits of
	 * @return true if comment's start is before 0 (shouldn't be possible) or its end is after the text
	 *         length
	 */
	public boolean isCommentOutOfTextBounds(Comment comment) {
		return comment.getStartIndex() < 0 || comment.getEndIndex() > this.text.length();
	}

	/**
	 * @return the legend
	 */
	public LegendConfiguration getLegend() {
		return this.legend;
	}

	/**
	 * Sets a new legend configuration to this beta reading.
	 *
	 * @param legend the legend to set
	 */
	public void setLegend(LegendConfiguration legend) {
		this.legend = legend;
	}

	/**
	 * @return the review
	 */
	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments.clear();
		if (comments != null)
			this.comments.addAll(comments);
	}

}
