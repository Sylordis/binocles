package com.github.sylordis.binocles.model.text;

import com.github.sylordis.binocles.utils.contracts.Identifiable;
import com.github.sylordis.binocles.utils.contracts.SelfCopying;

/**
 * Represents a chapter in a book, with a possible title and its content.
 * 
 * @author sylordis
 *
 */
public class Chapter extends ReviewableContent implements SelfCopying<Chapter> {

	private static final long serialVersionUID = -372544124080055257L;
	/**
	 * Title of the book, optional.
	 */
	private String title;
	/**
	 * Content of the chapter.
	 */
	private String content;

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
		this.content = text;
	}

	@Override
	public String getId() {
		return Identifiable.formatId(title);
	}

	@Override
	public String toString() {
		return "Chapter [title=" + title + ", content length="+content.length()+"]";
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
	 * @return the content of the chapter
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param text the text to set as content, null will be translated to "empty string".
	 */
	public void setContent(String text) {
		if (text == null)
			this.content = "";
		else
			this.content = text;
	}

	/**
	 * Gets the number of comments, including the global comment if not blank.
	 * 
	 * @return
	 */
	public int getCommentsCount() {
		return getComments().size() + (getGlobalComment().isBlank() ? 0 : 1);
	}

	/**
	 * Copies all class fields of the provided item.
	 * 
	 * @param chapter chapter to copy
	 */
	@Override
	public void copy(Chapter chapter) {
		this.setTitle(chapter.getTitle());
		this.setContent(chapter.getContent());
	}

}
