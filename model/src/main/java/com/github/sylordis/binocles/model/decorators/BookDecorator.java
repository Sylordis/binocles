package com.github.sylordis.binocles.model.decorators;

import java.util.function.Function;

import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.utils.CustomDecorator;

/**
 * Decorator for books.
 */
public class BookDecorator extends CustomDecorator<Book> {

	public static final String CHAPTERS_PREFIX = "[";
	public static final String CHAPTERS_SUFFIX = "]";
	public static final String NOMENCLATURE_PREFIX = "<";
	public static final String NOMENCLATURE_SUFFIX = ">";
	public static final String NO_NOMENCLATURE = "none";
	public static final String CHAPTER_WORD_SINGULAR = "chapter";
	public static final String CHAPTER_WORD_PLURAL = "chapters";
	public static final String COMMENT_WORD_SINGULAR = "comments";
	public static final String COMMENT_WORD_PLURAL = "comment";

	/**
	 * Adds the title of the book.
	 * 
	 * @return itself
	 */
	public BookDecorator thenTitle() {
		this.then(b -> b.getTitle());
		return this;
	}

	/**
	 * Adds the nomenclature name with default prefix and suffix.
	 * 
	 * @return itself
	 * @see #NOMENCLATURE_PREFIX
	 * @see #NOMENCLATURE_SUFFIX
	 */
	public BookDecorator thenNomenclature() {
		return this.thenNomenclature(NOMENCLATURE_PREFIX, NOMENCLATURE_SUFFIX);
	}

	/**
	 * Adds the nomenclature name.
	 * 
	 * @param suffix
	 * @param prefix
	 * @return itself
	 */
	public BookDecorator thenNomenclature(String prefix, String suffix) {
		this.then(b -> (null != b.getNomenclature() ? b.getNomenclature().getName() : NO_NOMENCLATURE), prefix, suffix);
		return this;
	}

	/**
	 * Adds the number of chapters with default prefix and suffix.
	 * 
	 * @return itself
	 * @see #CHAPTERS_PREFIX
	 * @see #CHAPTERS_SUFFIX
	 */
	public BookDecorator thenChapterCountWithText() {
		return this.thenChapterCount(CHAPTERS_PREFIX, CHAPTERS_SUFFIX, true);
	}

	/**
	 * Adds the number of chapters.
	 * 
	 * @param prefix prefix to add before the string
	 * @param suffix suffix to add after the string
	 * @param text   display a text "chapter"
	 * @return itself
	 */
	public BookDecorator thenChapterCount(String prefix, String suffix, boolean text) {
		Function<Book, String> decorator = b -> {
			StringBuilder builder = new StringBuilder();
			builder.append(b.getChapters().size());
			if (text) {
				builder.append(" ");
				builder.append(b.getChapters().size() > 1 ? CHAPTER_WORD_PLURAL : CHAPTER_WORD_SINGULAR);
			}
			return builder.toString();
		};
		this.then(decorator, prefix, suffix);
		return this;
	}

	/**
	 * Adds the total number of comments on this book, e.g. the number of comments on all chapters and
	 * global comments.
	 * 
	 * @return
	 */
	public BookDecorator thenCommentsCountWithText() {
		return this.thenCommentsCount(CHAPTERS_PREFIX, CHAPTERS_SUFFIX, true);
	}
	
	/**
	 * Adds the total number of comments on this book, e.g. the number of comments on all chapters and
	 * global comments.
	 * 
	 * @return
	 */
	public BookDecorator thenCommentsCount(String prefix, String suffix, boolean text) {
		Function<Book, String> decorator = b -> {
			StringBuilder builder = new StringBuilder();
			builder.append(b.getCommentsCount());
			if (text) {
				builder.append(" ");
				builder.append(b.getCommentsCount() > 1 ? COMMENT_WORD_PLURAL : COMMENT_WORD_SINGULAR);
			}
			return builder.toString();
		};
		this.then(decorator, prefix, suffix);
		return this;
	}
}
