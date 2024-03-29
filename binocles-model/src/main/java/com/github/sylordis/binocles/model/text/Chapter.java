package com.github.sylordis.binocles.model.text;

import com.github.sylordis.binocles.model.review.ReviewableContent;
import com.github.sylordis.binocles.utils.Identifiable;

/**
 * Represents a chapter in a book, with a possible title and its content.
 * 
 * @author sylordis
 *
 */
public class Chapter extends ReviewableContent {

	/**
	 * Title of the book, optional.
	 */
	private String title;
	/**
	 * Content of the chapter.
	 */
	private String text;

	/**
	 * Creates a new empty chapter.
	 */
	public Chapter() {
		this("", "");
	}

	/**
	 * Creates a new chapter without content.
	 * 
	 * @param title Title of the chapter.
	 */
	public Chapter(String title) {
		this(title, "");
	}

	/**
	 * Creates a new chapter with a title and a
	 * 
	 * @param title
	 * @param text
	 */
	public Chapter(String title, String text) {
		super();
		this.title = title;
		this.text = text;
	}

	@Override
	public String getId() {
		return Identifiable.formatId(title);
	}

	@Override
	public String toString() {
		StringBuilder rame = new StringBuilder();
		rame.append(title);
		if (this.hasComments()) {
			rame.append(" [").append(this.getComments().size()).append("]");
		}
		return rame.toString();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
