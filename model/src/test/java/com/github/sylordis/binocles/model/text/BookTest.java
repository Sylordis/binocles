package com.github.sylordis.binocles.model.text;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.sylordis.binocles.contracts.Identifiable;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.utils.MapUtils;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;

/**
 * Test class for {@link Book}.
 */
class BookTest {

	private final static String DUMMY_TITLE = "Dummy for Dummies 101";

	/**
	 * Instance under test.
	 */
	private Book book;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		book = new Book(DUMMY_TITLE);
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#getTitle()}.
	 */
	@Test
	void testGetTitle() {
		assertEquals(DUMMY_TITLE, book.getTitle());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#Book(java.lang.String)}.
	 */
	@Test
	void testBookString() {
		assertNotNull(book);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#Book(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testBookStringString() {
		final String title = "The Dummy Test 2";
		final String synopsis = "This is a story about a dummy testing a book.";
		book = new Book(title, synopsis);
		assertAll(() -> assertEquals(synopsis, book.getSynopsis()), () -> assertEquals(title, book.getTitle()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#Book(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	void testBookStringStringString() {
		final String title = "The Dummy Test 3";
		final String synopsis = "This dummy was very persistent in testing this book.";
		final String description = "4096 pages of dummies";
		book = new Book(title, synopsis, description);
		assertAll(() -> assertEquals(title, book.getTitle()), () -> assertEquals(synopsis, book.getSynopsis()),
		        () -> assertEquals(description, book.getDescription()));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#Book(java.lang.String)}.
	 */
	@Test
	void testBookString_NullTitle() {
		assertThrows(NullPointerException.class, () -> new Book(null));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#Book(java.lang.String)}.
	 */
	@ParameterizedTest
	@ValueSource(strings = { "", "\t", "   " })
	void testBookString_BlankTitle(String title) {
		assertThrows(IllegalArgumentException.class, () -> new Book(title));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#Book(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testBookStringString_NullSynopsis() {
		book = new Book(DUMMY_TITLE, null);
		assertEquals("", book.getSynopsis());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#Book(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	void testBookStringStringString_NullDescription() {
		book = new Book(DUMMY_TITLE, "synop", null);
		assertEquals("", book.getDescription());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#toString()}.
	 */
	@Test
	void testToString() {
		assertNotNull(book.toString());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#toString()}.
	 * @throws UniqueIDException 
	 */
	@Test
	void testToString_WithChapter() throws UniqueIDException {
		book.addChapter(mock(Chapter.class));
		assertNotNull(book.toString());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#toString()}.
	 */
	@Test
	void testToString_WithNomenclature() {
		book.setNomenclature(mock(Nomenclature.class));
		assertNotNull(book.toString());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#getId()}.
	 */
	@Test
	void testGetId() {
		assertNotNull(book.getId());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setTitle(java.lang.String)}.
	 */
	@Test
	void testSetTitle() {
		final String title = "That small trail that smells like haselnuts";
		book.setTitle(title);
		assertEquals(title, book.getTitle());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setTitle(java.lang.String)}.
	 */
	@Test
	void testSetTitle_Null() {
		assertThrows(NullPointerException.class, () -> book.setTitle(null));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setTitle(java.lang.String)}.
	 */
	@ParameterizedTest
	@ValueSource(strings = { "", "\t", "\n", "   \t", "   " })
	void testSetTitle_Blank(String title) {
		assertThrows(IllegalArgumentException.class, () -> book.setTitle(title));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#createChapter()}.
	 */
	@Test
	void testCreateChapter() {
		final String chapterTitle = Book.CHAPTER_DEFAULT_TITLE + "1";
		book.createChapter();
		assertAll(() -> assertEquals(1, book.getChapters().size()),
		        () -> assertTrue(book.getChapters().get(0).is(chapterTitle)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#addChapter(com.github.sylordis.binocles.model.text.Chapter)}.
	 * @throws UniqueIDException 
	 */
	@Test
	void testAddChapter() throws UniqueIDException {
		final Chapter chapter = mock(Chapter.class);
		book.addChapter(chapter);
		assertAll(() -> assertEquals(1, book.getChapters().size()),
		        () -> assertTrue(book.getChapters().get(0).equals(chapter)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#addChapter(com.github.sylordis.binocles.model.text.Chapter)}.
	 * @throws UniqueIDException 
	 */
	@Test
	void testAddChapter_Multiple() throws UniqueIDException {
		List<Chapter> chapters = List.of(mock(Chapter.class), mock(Chapter.class), mock(Chapter.class));
		for (Chapter chapter : chapters) {
			book.addChapter(chapter);
		}
		assertIterableEquals(chapters, book.getChapters());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#getChapters()}.
	 */
	@Test
	void testGetChapters() {
		assertNotNull(book.getChapters());
		assertTrue(book.getChapters().isEmpty());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#hasChapters()}.
	 */
	@Test
	void testHasChapters_Not() {
		assertFalse(book.hasChapters());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#hasChapters()}.
	 * @throws UniqueIDException 
	 */
	@Test
	void testHasChapters() throws UniqueIDException {
		book.addChapter(mock(Chapter.class));
		assertTrue(book.hasChapters());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setChapters(java.util.List)}.
	 */
	@Test
	void testSetChapters() {
		List<Chapter> chapters = List.of(mock(Chapter.class), mock(Chapter.class), mock(Chapter.class));
		book.setChapters(chapters);
		assertIterableEquals(chapters, book.getChapters());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setChapters(java.util.List)}.
	 */
	@Test
	void testSetChapters_Null() {
		List<Chapter> chapters = List.of(mock(Chapter.class), mock(Chapter.class), mock(Chapter.class));
		book.setChapters(chapters);
		book.setChapters(null);
		assertFalse(book.hasChapters());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setChapters(java.util.List)}.
	 */
	@Test
	void testSetChapters_Replace() {
		List<Chapter> chapters = List.of(mock(Chapter.class), mock(Chapter.class), mock(Chapter.class));
		List<Chapter> chapters2 = List.of(mock(Chapter.class), mock(Chapter.class));
		book.setChapters(chapters);
		book.setChapters(chapters2);
		assertIterableEquals(chapters2, book.getChapters());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#getSynopsis()}.
	 */
	@Test
	void testGetSynopsis() {
		assertAll(() -> assertNotNull(book.getSynopsis()), () -> assertTrue(book.getSynopsis().isBlank()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#setSynopsis(java.lang.String)}.
	 */
	@Test
	void testSetSynopsis() {
		final String synopsis = "The new synopsis! Better, Faster, Stranger!";
		book.setSynopsis(synopsis);
		assertEquals(synopsis, book.getSynopsis());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#setSynopsis(java.lang.String)}.
	 */
	@Test
	void testSetSynopsis_Null() {
		final String synopsis = "I won't last long!";
		book.setSynopsis(synopsis);
		book.setSynopsis(null);
		assertEquals("", book.getSynopsis());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#getNomenclature()}.
	 */
	@Test
	void testGetNomenclature() {
		assertNull(book.getNomenclature());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#setNomenclature(com.github.sylordis.binocles.model.review.Nomenclature)}.
	 */
	@Test
	void testSetNomenclature() {
		final Nomenclature nomenclature = mock(Nomenclature.class);
		book.setNomenclature(nomenclature);
		assertEquals(nomenclature, book.getNomenclature());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#hasChapter(java.lang.String)}.
	 * @throws UniqueIDException 
	 */
	@Test
	void testHasChapter() throws UniqueIDException {
		final String title = "I am the title";
		final Chapter chapter = new Chapter(title);
		book.addChapter(chapter);
		assertTrue(book.hasChapter(Identifiable.formatId(title)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.text.Book#hasChapter(java.lang.String)}.
	 */
	@Test
	void testHasChapter_Not() {
		assertFalse(book.hasChapter("ANYTHING"));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setMetadata(java.util.Map)}.
	 */
	@Test
	void testSetMetadata() {
		Map<String,String> metadata = MapUtils.createVariable(new TreeMap<String,String>(), "a", "entry a", "b", "entry b");
		book.setMetadata(metadata);
		assertIterableEquals(metadata.entrySet(), book.getMetadata().entrySet());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setMetadata(java.util.Map)}.
	 */
	@Test
	void testSetMetadata_Replace() {
		Map<String,String> metadata = MapUtils.createVariable(new TreeMap<String,String>(), "a", "entry a", "b", "entry b");
		Map<String,String> metadata2 = MapUtils.createVariable(new TreeMap<String,String>(), "c", "entry c", "d", "entry d", "e", "entry e");
		book.setMetadata(metadata);
		book.setMetadata(metadata2);
		assertIterableEquals(metadata2.entrySet(), book.getMetadata().entrySet());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setMetadata(java.util.Map)}.
	 */
	@Test
	void testSetMetadata_Null() {
		Map<String,String> metadata = MapUtils.createVariable(new TreeMap<String,String>(), "f", "entry f", "g", "entry g");
		book.setMetadata(metadata);
		book.setMetadata(null);
		assertNotNull(book.getMetadata());
		assertTrue(book.getMetadata().isEmpty());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#getMetadata()}.
	 */
	@Test
	void testGetMetadata() {
		assertNotNull(book.getMetadata());
		assertTrue(book.getMetadata().isEmpty());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#getDescription()}.
	 */
	@Test
	void testGetDescription() {
		assertEquals("", book.getDescription());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setDescription(String)}.
	 */
	@Test
	void testSetDescription() {
		final String description = "I'm a half-elf-dwarf quadclassed: warrior, necromancer, druid with assassin option, I also possess gnome skills";
		book.setDescription(description);
		assertEquals(description, book.getDescription());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.text.Book#setDescription(String)}.
	 */
	@Test
	void testSetDescription_Null() {
		book.setDescription("Something!");
		book.setDescription(null);
		assertEquals("", book.getDescription());
	}
	
}
