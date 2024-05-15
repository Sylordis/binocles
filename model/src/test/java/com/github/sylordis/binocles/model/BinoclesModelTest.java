package com.github.sylordis.binocles.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;

/**
 * 
 */
class BinoclesModelTest {

	private BinoclesModel model;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		model = new BinoclesModel();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.BinoclesModel#BinoclesModel()}.
	 */
	@Test
	void testBinoclesModel() {
		assertNotNull(model);
		assertFalse(model.hasBooks());
		assertFalse(model.hasNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#addBook(com.github.sylordis.binocles.model.text.Book)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testAddBook() throws UniqueIDException {
		final String title = "This is a book!";
		model.addBook(new Book(title));
		assertEquals(1, model.getBooks().size());
		assertTrue(model.hasBook(title));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#addBook(com.github.sylordis.binocles.model.text.Book)}
	 * when adding a book with a name that already exists.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testAddBook_WithUniqueNameException() throws UniqueIDException {
		final String title = "This book will already exist";
		model.addBook(new Book(title));
		assertThrows(UniqueIDException.class, () -> model.addBook(new Book(title)));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#addNomenclature(com.github.sylordis.binocles.model.review.Nomenclature)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testAddNomenclature() throws UniqueIDException {
		final String name = "Nomenclatures are witchcraft";
		model.addNomenclature(new Nomenclature(name));
		assertEquals(1, model.getNomenclatures().size());
		assertTrue(model.hasNomenclature(name));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#addNomenclature(com.github.sylordis.binocles.model.review.Nomenclature)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testAddNomenclature_UniqueIDException() throws UniqueIDException {
		final String name = "Nomenclatures are witchcraft";
		model.addNomenclature(new Nomenclature(name));
		assertEquals(1, model.getNomenclatures().size());
		assertTrue(model.hasNomenclature(name));
	}

	/**
	 * Test method for {@link BinoclesModel#getBook(String)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testGetBook() throws UniqueIDException {
		final String title = "Life of a pangolin";
		final Book book = new Book(title);
		final Collection<Book> books = toBooks("I", "have", "to", "break", "free");
		model.setBooks(books);
		model.addBook(book);
		assertEquals(book, model.getBook(title));
	}

	/**
	 * Test method for {@link BinoclesModel#getBook(String)}.
	 */
	@Test
	void testGetBook_NotExisting() {
		final String title = "Wild Child";
		final Collection<Book> books = toBooks("I'm", "a", "maniac");
		model.setBooks(books);
		assertNull(model.getBook(title));
	}

	/**
	 * Test method for {@link BinoclesModel#getBook(String)} when providing null.
	 */
	@Test
	void testGetBook_Null() {
		final Collection<Book> books = toBooks("This", "is", "the", "best", "band", "of", "our", "universe");
		model.setBooks(books);
		assertNull(model.getBook(null));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.BinoclesModel#getBooks()}.
	 */
	@Test
	void testGetBooks() {
		assertNotNull(model.getBooks());
		assertTrue(model.getBooks().isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setBooks(java.util.Collection)}.
	 */
	@Test
	void testSetBooks() {
		List<Book> books = new ArrayList<>(List.of(new Book("Foo"), new Book("Bar")));
		model.setBooks(books);
		List<Book> expected = new ArrayList<>();
		expected.addAll(books);
		assertEquals(expected, model.getBooks());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setBooks(java.util.Collection)}.
	 */
	@Test
	void testSetBooks_NullAndClear() {
		List<Book> books = new ArrayList<>(List.of(new Book("Foo"), new Book("Bar")));
		model.setBooks(books);
		model.setBooks(null);
		assertFalse(model.hasBooks());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setBooks(java.util.Collection)}.
	 */
	@Test
	void testSetBooks_Replace() {
		Collection<Book> books = List.of(new Book("Foo"), new Book("Bar"));
		Collection<Book> books2 = new ArrayList<>(List.of(new Book("Hello"), new Book("World")));
		model.setBooks(books);
		model.setBooks(books2);
		List<Book> expected = new ArrayList<>();
		expected.addAll(books2);
		assertEquals(expected, model.getBooks());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setBooks(java.util.Collection)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testSetBooksUnique() throws UniqueIDException {
		Collection<Book> books = List.of(new Book("Foo"), new Book("Bar"));
		model.setBooksUnique(books);
		List<Book> expected = new ArrayList<>();
		expected.addAll(books);
		assertEquals(expected, model.getBooks());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setBooks(java.util.Collection)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testSetBooksUnique_NullAndReplace() throws UniqueIDException {
		Collection<Book> books = List.of(new Book("Foo"), new Book("Bar"));
		model.setBooksUnique(books);
		model.setBooksUnique(null);
		assertFalse(model.hasBooks());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setBooks(java.util.Collection)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testSetBooksUnique_Replace() throws UniqueIDException {
		Collection<Book> books = List.of(new Book("Foo"), new Book("Bar"));
		Collection<Book> books2 = List.of(new Book("Hello"), new Book("World"));
		model.setBooksUnique(books);
		model.setBooksUnique(books2);
		List<Book> expected = new ArrayList<>();
		expected.addAll(books2);
		assertEquals(expected, model.getBooks());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setBooks(java.util.Collection)}.
	 */
	@Test
	void testSetBooksUnique_UniqueIDException() {
		Collection<Book> books = List.of(new Book("Foo"), new Book("Bar"), new Book("Bar"));
		assertThrows(UniqueIDException.class, () -> model.setBooksUnique(books));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasBook(java.lang.String)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasBookString() throws UniqueIDException {
		final String title = "Bourne";
		model.addBook(new Book(title));
		assertTrue(model.hasBook(title));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasBook(java.lang.String)} when the model
	 * doesn't have a book with this id.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasBookString_NotExisting() throws UniqueIDException {
		final String title = "Bourne";
		model.addBook(new Book(title));
		assertFalse(model.hasBook("Shell"));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasBook(java.lang.String)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasBookString_Null() throws UniqueIDException {
		final String title = "Bourne";
		model.addBook(new Book(title));
		assertFalse(model.hasBook((String) null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasBook(com.github.sylordis.binocles.model.text.Book)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasBookBook() throws UniqueIDException {
		final Book book = new Book("The one and only");
		model.addBook(book);
		assertTrue(model.hasBook(book));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasBook(com.github.sylordis.binocles.model.text.Book)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasBookBook_NotExisting() throws UniqueIDException {
		final Book book = new Book("The one and only");
		model.addBook(book);
		assertFalse(model.hasBook(new Book("The other one and only")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasBook(com.github.sylordis.binocles.model.text.Book)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasBookBook_Null() throws UniqueIDException {
		final Book book = new Book("The one and only");
		model.addBook(book);
		assertFalse(model.hasBook((Book) null));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.BinoclesModel#hasBooks()}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasBooks() throws UniqueIDException {
		model.addBook(new Book("Sovereign"));
		assertTrue(model.hasBooks());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.BinoclesModel#hasBooks()}.
	 */
	@Test
	void testHasBooks_Not() {
		assertFalse(model.hasBooks());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasNomenclature(com.github.sylordis.binocles.model.review.Nomenclature)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasNomenclatureNomenclature() throws UniqueIDException {
		final Nomenclature nomenclature = new Nomenclature("TheDuck");
		model.addNomenclature(nomenclature);
		assertTrue(model.hasNomenclature(nomenclature));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasNomenclature(com.github.sylordis.binocles.model.review.Nomenclature)}.
	 */
	@Test
	void testHasNomenclatureNomenclature_Not() {
		assertFalse(model.hasNomenclature(new Nomenclature("Kwak")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasNomenclature(com.github.sylordis.binocles.model.review.Nomenclature)} when nomenclature is null.
	 */
	@Test
	void testHasNomenclatureNomenclature_Null() {
		assertFalse(model.hasNomenclature((Nomenclature) null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasNomenclature(java.lang.String)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasNomenclatureString() throws UniqueIDException {
		final String name = "Everybody do the flop!";
		model.addNomenclature(new Nomenclature(name));
		assertTrue(model.hasNomenclature(name));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasNomenclature(java.lang.String)}.
	 */
	@Test
	void testHasNomenclatureString_Not() {
		assertFalse(model.hasNomenclature("Hello"));
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.BinoclesModel#hasNomenclatures()}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasNomenclatures() throws UniqueIDException {
		model.addNomenclature(new Nomenclature("Brikoo"));
		assertTrue(model.hasNomenclatures());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.BinoclesModel#hasNomenclatures()}.
	 */
	@Test
	void testHasNomenclatures_Not() {
		assertFalse(model.hasNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasCustomNomenclatures()}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasCustomNomenclatures() throws UniqueIDException {
		Nomenclature nomD = new Nomenclature("Poppy") {

			@Override
			public boolean isDefaultNomenclature() {
				return true;
			}

		};
		model.addNomenclature(nomD);
		model.addNomenclature(new Nomenclature("Customise hippity hop"));
		assertTrue(model.hasCustomNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasCustomNomenclatures()}.
	 */
	@Test
	void testHasCustomNomenclatures_Empty() {
		assertFalse(model.hasCustomNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#hasCustomNomenclatures()}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testHasCustomNomenclatures_OnlyDefault() throws UniqueIDException {
		Nomenclature nomD = new Nomenclature("Miss Daisy") {

			@Override
			public boolean isDefaultNomenclature() {
				return true;
			}

		};
		model.addNomenclature(nomD);
		assertFalse(model.hasCustomNomenclatures());
	}

	/**
	 * Test method for {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclatures()}.
	 */
	@Test
	void testGetNomenclatures() {
		assertNotNull(model.getNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclatures(boolean)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testGetNomenclaturesBoolean_NoDefault() {
		final Nomenclature nom1 = new Nomenclature("Customise hippity hop");
		final Nomenclature nom2 = new Nomenclature("swiggity");
		Collection<Nomenclature> nomz = List.of(nom1, nom2);
		model.setNomenclatures(nomz);
		List<Nomenclature> expected = new ArrayList<>();
		expected.addAll(nomz);
		assertEquals(expected, model.getNomenclatures(true));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclatures(java.util.function.Predicate)}.
	 */
	@Test
	void testGetNomenclaturesPredicateNomenclature() {
		final Collection<Nomenclature> nomz = toNomenclatures("The", "Quick", "Brown", "Fox", "Jumps", "Over", "A",
		        "Lazy", "Dog");
		model.setNomenclatures(nomz);
		Collection<Nomenclature> extract = model.getNomenclatures(n -> n.getId().contains("a"));
		Collection<Nomenclature> expected = List.of(model.getNomenclature("A"), model.getNomenclature("Lazy"));
		assertIterableEquals(expected, extract);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclatures(java.util.function.Predicate)}
	 * when no nomenclatures are present.
	 */
	@Test
	void testGetNomenclaturesPredicateNomenclature_Empty() {
		Collection<Nomenclature> extract = model.getNomenclatures(n -> true);
		assertNotNull(extract);
		assertTrue(extract.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclatures(boolean)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testGetNomenclaturesBoolean_WithoutDefault() throws UniqueIDException {
		Nomenclature nomD = new Nomenclature("Poppy") {

			@Override
			public boolean isDefaultNomenclature() {
				return true;
			}

		};
		final Nomenclature nom1 = new Nomenclature("Customise hippity hop");
		final Nomenclature nom2 = new Nomenclature("swiggity");
		List<Nomenclature> nomz = List.of(nomD, nom1, nom2);
		model.setNomenclatures(nomz);
		List<Nomenclature> expected = new ArrayList<>();
		expected.addAll(List.of(nom1, nom2));
		assertEquals(expected, model.getNomenclatures(true));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclatures(boolean)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testGetNomenclaturesBoolean_NoExclude() throws UniqueIDException {
		Nomenclature nomD = new Nomenclature("Poppy") {

			@Override
			public boolean isDefaultNomenclature() {
				return true;
			}

		};
		final Nomenclature nom1 = new Nomenclature("Customise hippity hop");
		final Nomenclature nom2 = new Nomenclature("swiggity");
		List<Nomenclature> nomz = List.of(nomD, nom1, nom2);
		model.setNomenclatures(nomz);
		List<Nomenclature> expected = new ArrayList<>();
		expected.addAll(nomz);
		assertEquals(expected, model.getNomenclatures(false));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclature(java.lang.String)}.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testGetNomenclature() throws UniqueIDException {
		final String name = "My Nom and Clature";
		final Nomenclature nomenclature = new Nomenclature(name);
		model.addNomenclature(nomenclature);
		assertEquals(nomenclature, model.getNomenclature(name));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getNomenclature(java.lang.String)} when
	 * the nomenclature does not exist.
	 * 
	 * @throws UniqueIDException
	 */
	@Test
	void testGetNomenclature_NotExisting() throws UniqueIDException {
		final String name = "My Nom and Clature";
		final Nomenclature nomenclature = new Nomenclature("Cloak and dagger");
		model.addNomenclature(nomenclature);
		assertNull(model.getNomenclature(name));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getDefaultNomenclature()}.
	 */
	@Test
	void testGetDefaultNomenclature() {
		final Nomenclature nomD = new Nomenclature("Default") {

			@Override
			public boolean isDefaultNomenclature() {
				return true;
			}

		};
		Collection<Nomenclature> nomz = List.of(new Nomenclature("A"), new Nomenclature("B"), nomD,
		        new Nomenclature("C"));
		model.setNomenclatures(nomz);
		assertEquals(nomD, model.getDefaultNomenclature());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getDefaultNomenclature()}.
	 */
	@Test
	void testGetDefaultNomenclature_Empty() {
		assertNull(model.getDefaultNomenclature());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#getDefaultNomenclature()}.
	 */
	@Test
	void testGetDefaultNomenclature_MultipleDefault() {
		final Nomenclature nomD1 = new Nomenclature("Something of") {

			@Override
			public boolean isDefaultNomenclature() {
				return true;
			}

		};
		final Nomenclature nomD2 = new Nomenclature("a default myself") {

			@Override
			public boolean isDefaultNomenclature() {
				return true;
			}

		};
		Collection<Nomenclature> nomz = List.of(new Nomenclature("A"), new Nomenclature("B"), new Nomenclature("C"),
		        nomD1, nomD2);
		model.setNomenclatures(nomz);
		assertEquals(nomD1, model.getDefaultNomenclature());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclatures(java.util.Collection)}.
	 */
	@Test
	void testSetNomenclatures() {
		final Collection<Nomenclature> nomz = toNomenclatures("My", "little", "nomenclature");
		model.setNomenclatures(nomz);
		List<Nomenclature> expected = new ArrayList<>();
		expected.addAll(nomz);
		assertEquals(expected, model.getNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclatures(java.util.Collection)}.
	 */
	@Test
	void testSetNomenclatures_Empty() {
		model.setNomenclatures(new ArrayList<>());
		assertFalse(model.hasNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclatures(java.util.Collection)}
	 * when provided a null collection.
	 */
	@Test
	void testSetNomenclatures_Null() {
		model.setNomenclatures(null);
		assertFalse(model.hasNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclatures(java.util.Collection)}.
	 */
	@Test
	void testSetNomenclatures_Replace() {
		final Collection<Nomenclature> nomz = toNomenclatures("A", "B", "C");
		final Collection<Nomenclature> replacement = toNomenclatures("D", "A", "F");
		model.setNomenclatures(nomz);
		model.setNomenclatures(replacement);
		List<Nomenclature> expected = new ArrayList<>();
		expected.addAll(replacement);
		assertEquals(expected, model.getNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclaturesUnique(java.util.Collection)}.
	 */
	@Test
	void testSetNomenclaturesUnique() throws UniqueIDException {
		final Collection<Nomenclature> nomz = toNomenclatures("Commenting", "is", "magic");
		model.setNomenclaturesUnique(nomz);
		List<Nomenclature> expected = new ArrayList<>();
		expected.addAll(nomz);
		assertEquals(expected, model.getNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclaturesUnique(java.util.Collection)}.
	 */
	@Test
	void testSetNomenclaturesUnique_Empty() throws UniqueIDException {
		model.setNomenclaturesUnique(new ArrayList<>());
		assertFalse(model.hasNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclaturesUnique(java.util.Collection)}
	 * when providing null.
	 */
	@Test
	void testSetNomenclaturesUnique_NullAndClear() throws UniqueIDException {
		final Collection<Nomenclature> nomz = toNomenclatures("The", "Nomenclature", "doesn't", "exist");
		model.setNomenclaturesUnique(nomz);
		model.setNomenclaturesUnique(null);
		assertFalse(model.hasNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclaturesUnique(java.util.Collection)}.
	 */
	@Test
	void testSetNomenclaturesUnique_Replace() throws UniqueIDException {
		final Collection<Nomenclature> nomz = toNomenclatures("A", "B", "C");
		final Collection<Nomenclature> replacement = toNomenclatures("D", "A", "F");
		model.setNomenclaturesUnique(nomz);
		model.setNomenclaturesUnique(replacement);
		List<Nomenclature> expected = new ArrayList<>();
		expected.addAll(replacement);
		assertEquals(expected, model.getNomenclatures());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.BinoclesModel#setNomenclaturesUnique(java.util.Collection)}
	 * when some of the nomenclatures have the same id.
	 */
	@Test
	void testSetNomenclaturesUnique_WithUnique() throws UniqueIDException {
		final Collection<Nomenclature> nomz = toNomenclatures("This", "might be", "a little", "of", "this");
		assertThrows(UniqueIDException.class, () -> model.setNomenclaturesUnique(nomz));
	}

	/**
	 * Converts an array of names to a list of nomenclatures of said names. This doesn't care about
	 * unique IDs.
	 * 
	 * @param names
	 * @return
	 */
	private Collection<Nomenclature> toNomenclatures(String... names) {
		return Arrays.stream(names).map(n -> new Nomenclature(n)).collect(Collectors.toList());
	}

	/**
	 * Converts an array of strings to a list of books of said titles. This doesn't care about unique
	 * IDs.
	 * 
	 * @param titles
	 * @return
	 */
	private Collection<Book> toBooks(String... titles) {
		return Arrays.stream(titles).map(t -> new Book(t)).collect(Collectors.toList());
	}
}
