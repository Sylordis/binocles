package com.github.sylordis.binocles.model.text;

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
		return title;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set, empty string if null
	 */
	public void setTitle(String title) {
		if (title == null)
			this.title = "";
		else
			this.title = title;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set, empty string if null
	 */
	public void setText(String text) {
		if (text == null)
			this.text = "";
		else
			this.text = text;
	}

	/**
	 * Gets the number of comments, including the global comment if not blank.
	 * 
	 * @return
	 */
	public int getCommentsCount() {
		return getComments().size() + (getGlobalComment().isBlank() ? 0 : 1);
	}

}
