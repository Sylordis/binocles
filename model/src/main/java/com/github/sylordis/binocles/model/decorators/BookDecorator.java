package com.github.sylordis.binocles.model.decorators;

import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.utils.CustomDecorator;

/**
 * Decorator for books.
 */
public class BookDecorator extends CustomDecorator<Book> {

	public static final String CHAPTERS_PREFIX = "[";
	public static final String CHAPTERS_SUFFIX = "[";
	public static final String NOMENCLATURE_PREFIX = "<";
	public static final String NOMENCLATURE_SUFFIX = ">";

	/**
	 * Adds the title of the book.
	 * 
	 * @return itself
	 */
	public BookDecorator title() {
		this.and(b -> b.getTitle());
		return this;
	}

	/**
	 * Adds the nomenclature name with default prefix and suffix.
	 * 
	 * @return itself
	 * @see #NOMENCLATURE_PREFIX
	 * @see #NOMENCLATURE_SUFFIX
	 */
	public BookDecorator nomenclature() {
		return this.nomenclature(NOMENCLATURE_PREFIX, NOMENCLATURE_SUFFIX);
	}

	/**
	 * Adds the nomenclature name.
	 * 
	 * @param suffix
	 * @param prefix
	 * @return itself
	 */
	public BookDecorator nomenclature(String suffix, String prefix) {
		this.and(b -> (null != b.getNomenclature() ? b.getNomenclature().getName() : "none"), prefix, suffix);
		return this;
	}

	/**
	 * Adds the number of chapters with default prefix and suffix.
	 * 
	 * @return itself
	 * @see #CHAPTERS_PREFIX
	 * @see #CHAPTERS_SUFFIX
	 */
	public BookDecorator chapters() {
		return this.chapters(CHAPTERS_PREFIX, CHAPTERS_SUFFIX);
	}

	/**
	 * Adds the number of chapters.
	 * 
	 * @param prefix
	 * @param suffix
	 * @return itself
	 */
	public BookDecorator chapters(String prefix, String suffix) {
		this.and(b -> Integer.toString(b.getChapters().size()), prefix, suffix);
		return this;
	}
}
