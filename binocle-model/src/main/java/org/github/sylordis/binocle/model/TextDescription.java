package org.github.sylordis.binocle.model;

import java.io.Serializable;

/**
 * Holds all generic information input by user.
 *
 * @author Sylordis
 *
 * @param source Source of the text (URL, book title, etc).
 * @param author Author name.
 */
public record TextDescription(String source, String author) implements Serializable {

	/**
	 * Default empty constructor.
	 */
	public TextDescription() {
		this("","");
	}

}
