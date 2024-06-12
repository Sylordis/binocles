package com.github.sylordis.binocles.model.decorators;

import java.util.function.Function;

import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.utils.CustomDecorator;

public class ChapterDecorator extends CustomDecorator<Chapter> {

	public static final String COMMENTS_PREFIX = "[";
	public static final String COMMENTS_SUFFIX = "]";

	/**
	 * Adds the title of the book.
	 * 
	 * @return itself
	 */
	public ChapterDecorator thenTitle() {
		this.and(b -> b.getTitle());
		return this;
	}

	/**
	 * Adds the number of comments with default prefix and suffix.
	 * 
	 * @return itself
	 * @see #CHAPTERS_PREFIX
	 * @see #CHAPTERS_SUFFIX
	 */
	public ChapterDecorator thenCommentsCountWithText() {
		return this.thenCommentsCount(COMMENTS_PREFIX, COMMENTS_SUFFIX, true);
	}

	/**
	 * Adds the number of comments with default prefix and suffix.
	 * 
	 * @param prefix prefix to add before the string
	 * @param suffix suffix to add after the string
	 * @return itself
	 */
	public ChapterDecorator thenCommentsCountWithText(String prefix, String suffix) {
		return this.thenCommentsCount(prefix, suffix, true);
	}

	/**
	 * Adds the number of comments with default prefix and suffix.
	 * 
	 * @return itself
	 * @see #CHAPTERS_PREFIX
	 * @see #CHAPTERS_SUFFIX
	 */
	public ChapterDecorator thenCommentsCount() {
		return this.thenCommentsCount(COMMENTS_PREFIX, COMMENTS_SUFFIX, false);
	}

	/**
	 * Adds the number of comments with default prefix and suffix.
	 * 
	 * @param prefix prefix to add before the string
	 * @param suffix suffix to add after the string
	 * @return itself
	 */
	public ChapterDecorator thenCommentsCount(String prefix, String suffix) {
		return this.thenCommentsCount(prefix, suffix, false);
	}

	/**
	 * Adds the number of comments, including the global comment.
	 * 
	 * @param prefix prefix to add before the string
	 * @param suffix suffix to add after the string
	 * @return itself
	 */
	public ChapterDecorator thenCommentsCount(String prefix, String suffix, boolean text) {
		Function<Chapter, String> decorator = c -> {
			StringBuilder builder = new StringBuilder();
			builder.append(c.getCommentsCount());
			if (text)
				builder.append(" comment");
			if (text && c.getCommentsCount() > 1)
				builder.append("s");
			return builder.toString();
		};
		this.and(decorator, prefix, suffix);
		return this;
	}

}
