package com.github.sylordis.binocles.model.text;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.sylordis.binocles.model.review.Comment;

/**
 * Test class for Chapter class.
 */
class ChapterTest {

	private final static String DUMMY_TITLE = "Dummy Chapter";

	/**
	 * Object under test.
	 */
	private Chapter chapter;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		chapter = new Chapter(DUMMY_TITLE);
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Chapter#getTitle()}.
	 */
	@Test
	void testGetTitle() {
		assertEquals(DUMMY_TITLE, chapter.getTitle());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Chapter#Chapter()}.
	 */
	@Test
	void testChapter() {
		chapter = new Chapter();
		assertAll(() -> assertNotNull(new Chapter()), () -> assertTrue(chapter.getTitle().isEmpty()),
		        () -> assertTrue(chapter.getText().isEmpty()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Chapter#Chapter(java.lang.String)}.
	 */
	@Test
	void testChapterString() {
		assertAll(() -> assertNotNull(chapter), () -> assertEquals(DUMMY_TITLE, chapter.getTitle()),
		        () -> assertTrue(chapter.getText().isEmpty()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Chapter#Chapter(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testChapterStringString() {
		final String title = "The Worst Day Since yesterday";
		final String text = """
		        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac lacus et lorem blandit vestibulum at vitae neque. Integer commodo vestibulum augue id laoreet. Nulla scelerisque euismod turpis elementum egestas. Vivamus tempor fermentum efficitur. Mauris vel urna maximus, condimentum velit in, ultrices est. Curabitur sed leo sit amet purus viverra blandit. Integer sit amet mi ante. Duis congue cursus sapien, mollis tempus nulla maximus id. Mauris et varius felis, ut porttitor velit. Sed vitae molestie lectus, sit amet varius arcu. Sed viverra nunc eu volutpat suscipit. Morbi rutrum libero a faucibus faucibus. Proin nisi massa, porttitor vel lacus sed, porttitor placerat tellus. Cras in ante varius, tempor lorem non, bibendum ante. Nulla vitae quam non lorem tincidunt vestibulum et ac erat.
		        		        """;
		chapter = new Chapter(title, text);
		assertAll(() -> assertNotNull(chapter), () -> assertEquals(title, chapter.getTitle()),
		        () -> assertEquals(text, chapter.getText()));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Chapter#getId()}.
	 */
	@Test
	void testGetId() {
		final String title = "This IS AlSo   tHe Id  ";
		chapter = new Chapter(title);
		assertFalse(chapter.getId().isBlank());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Chapter#toString()}.
	 */
	@Test
	void testToString() {
		assertNotNull(chapter.toString());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Chapter#toString()}.
	 */
	@Test
	void testToString_WithComments() {
		chapter.addComment(mock(Comment.class));
		assertNotNull(chapter.toString());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Chapter#setTitle(java.lang.String)}.
	 */
	@Test
	void testSetTitle() {
		final String title = "A bit sad, innit?";
		chapter.setTitle(title);
		assertEquals(title, chapter.getTitle());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Chapter#setTitle(java.lang.String)}.
	 */
	@Test
	void testSetTitle_Null() {
		chapter.setTitle(null);
		assertEquals("", chapter.getTitle());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Chapter#getText()}.
	 */
	@Test
	void testGetText() {
		assertEquals("", chapter.getText());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Chapter#setText(java.lang.String)}.
	 */
	@Test
	void testSetText() {
		final String text = "This is\na new\ntext";
		chapter.setText(text);
		assertEquals(text, chapter.getText());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Chapter#setText(java.lang.String)}.
	 */
	@Test
	void testSetText_Null() {
		chapter.setText("something");
		chapter.setText(null);
		assertEquals("", chapter.getText());
	}

}
