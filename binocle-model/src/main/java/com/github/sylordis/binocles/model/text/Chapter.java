package com.github.sylordis.binocle.model.text;

import com.github.sylordis.binocle.model.review.ReviewableContent;
import com.github.sylordis.binocle.utils.Identifiable;

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
	 * Creates a new chapter without a title.
	 * 
	 * @param text Content of the chapter.
	 */
	public Chapter(String text) {
		this("", text);
	}

	/**
	 * Creates a new chapter with a title and a 
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
		return title;
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
