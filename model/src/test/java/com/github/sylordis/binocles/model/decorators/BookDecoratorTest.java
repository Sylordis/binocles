package com.github.sylordis.binocles.model.decorators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;

/**
 * 
 */
@ExtendWith(MockitoExtension.class)
class BookDecoratorTest {

	private final String BOOK_TITLE = "The Infinite and the Divine";
	private final String NOMENCLATURE_TITLE = "Heavy boltering";

	/**
	 * Instance under test.
	 */
	private BookDecorator decorator;
	@Mock
	private Book book;
	@Mock
	private Nomenclature nomenclature;
	private List<Chapter> chapters;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		decorator = new BookDecorator();
		lenient().when(book.getTitle()).thenReturn(BOOK_TITLE);
		lenient().when(book.getNomenclature()).thenReturn(nomenclature);
		chapters = List.of(mock(Chapter.class), mock(Chapter.class), mock(Chapter.class));
		lenient().when(book.getChapters()).thenReturn(chapters);
		lenient().when(nomenclature.getName()).thenReturn(NOMENCLATURE_TITLE);
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenTitle()}.
	 */
	@Test
	void testThenTitle() {
		decorator.thenTitle();
		assertEquals(BOOK_TITLE, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenNomenclature()}.
	 */
	@Test
	void testThenNomenclature() {
		decorator.thenNomenclature();
		final String expected = BookDecorator.NOMENCLATURE_PREFIX + NOMENCLATURE_TITLE
		        + BookDecorator.NOMENCLATURE_SUFFIX;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenNomenclature()}.
	 */
	@Test
	void testThenNomenclature_NoNomenclature() {
		when(book.getNomenclature()).thenReturn(null);
		decorator.thenNomenclature();
		final String expected = BookDecorator.NOMENCLATURE_PREFIX + BookDecorator.NO_NOMENCLATURE
		        + BookDecorator.NOMENCLATURE_SUFFIX;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenNomenclature(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testThenNomenclatureStringString() {
		final String prefix = "=";
		final String suffix = "%";
		decorator.thenNomenclature(prefix, suffix);
		final String expected = prefix + NOMENCLATURE_TITLE + suffix;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenNomenclature(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testThenNomenclatureStringString_NoNomenclature() {
		final String prefix = "!";
		final String suffix = "!";
		when(book.getNomenclature()).thenReturn(null);
		decorator.thenNomenclature(prefix, suffix);
		final String expected = prefix + BookDecorator.NO_NOMENCLATURE + suffix;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenChapterCountWithText()}.
	 */
	@Test
	void testThenChapterCountWithText() {
		decorator.thenChapterCountWithText();
		final String expected = BookDecorator.CHAPTERS_PREFIX + chapters.size() + " "
		        + BookDecorator.CHAPTER_WORD_PLURAL + BookDecorator.CHAPTERS_SUFFIX;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenChapterCountWithText()}.
	 */
	@Test
	void testThenChapterCountWithText_One() {
		when(book.getChapters()).thenReturn(List.of(mock(Chapter.class)));
		decorator.thenChapterCountWithText();
		final String expected = BookDecorator.CHAPTERS_PREFIX + "1 " + BookDecorator.CHAPTER_WORD_SINGULAR
		        + BookDecorator.CHAPTERS_SUFFIX;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenChapterCountWithText()}.
	 */
	@Test
	void testThenChapterCountWithText_None() {
		when(book.getChapters()).thenReturn(List.of());
		decorator.thenChapterCountWithText();
		final String expected = BookDecorator.CHAPTERS_PREFIX + "0 " + BookDecorator.CHAPTER_WORD_SINGULAR
		        + BookDecorator.CHAPTERS_SUFFIX;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenCommentsCountWithText()}.
	 */
	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 3, Integer.MAX_VALUE })
	void testThenCommentsCountWithText(int value) {
		when(book.getCommentsCount()).thenReturn(value);
		decorator.thenCommentsCountWithText();
		final String expected = BookDecorator.CHAPTERS_PREFIX + value + " "
		        + (value > 1 ? BookDecorator.COMMENT_WORD_PLURAL : BookDecorator.COMMENT_WORD_SINGULAR)
		        + BookDecorator.CHAPTERS_SUFFIX;
		assertEquals(expected, decorator.print(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.decorators.BookDecorator#thenCommentsCount(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testThenCommentsCount() {
		final int count = 823;
		final String prefix = "(=";
		final String suffix = ")";
		when(book.getCommentsCount()).thenReturn(count);
		decorator.thenCommentsCount(prefix, suffix, false);
		final String expected = prefix + count + suffix;
		assertEquals(expected, decorator.print(book));
	}

}
