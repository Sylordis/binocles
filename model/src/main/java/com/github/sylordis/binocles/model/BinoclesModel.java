package com.github.sylordis.binocles.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.review.DefaultNomenclature;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.utils.Identifiable;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;
import com.google.common.base.Preconditions;

/**
 * Model of the application.
 * 
 * @author sylordis
 *
 */
public class BinoclesModel {

	/**
	 * Set of books, as a {@link TreeSet} alphabetically ordered by ID.
	 */
	private final List<Book> books;
	/**
	 * Set of nomenclatures, as a {@link TreeSet} alphabetically ordered by ID.
	 */
	private final List<Nomenclature> nomenclatures;

	/**
	 * Local logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Creates a new model.
	 */
	public BinoclesModel() {
		this.books = new ArrayList<>();
		this.nomenclatures = new ArrayList<>();
	}

	/**
	 * Adds a new book to the model, checking if no other book with the same ID exists.<br/>
	 * Use <code>getBooks().add(Book)</code> to add a book without the ID check.
	 * 
	 * @param book
	 * @param nomenclature
	 * @throws UniqueIDException if a book or nomenclature with the same id exists in the model.
	 */
	public void addBook(Book book) throws UniqueIDException {
		Identifiable.checkIfUnique(book, this.books);
		books.add(book);
		logger.info("New book added: '{}'", book.getTitle());
	}

	/**
	 * Adds a nomenclature to the model, checking if no other nomenclature with the same ID exists.<br/>
	 * Use <code>getNomenclatures().add(Nomenclature)</code> to add a nomenclature without the ID check.
	 * 
	 * @param nomenclature
	 * @throws UniqueIDException if a nomenclature with the same id exists in the model.
	 */
	public void addNomenclature(Nomenclature nomenclature) throws UniqueIDException {
		Preconditions.checkArgument(nomenclature.hasId(), "Nomenclature id cannot be empty or null", nomenclature);
		Identifiable.checkIfUnique(nomenclature, this.nomenclatures);
		nomenclatures.add(nomenclature);
		logger.info("New nomenclature added: '{}'", nomenclature.getName());
	}

	/**
	 * @return the books
	 */
	public List<Book> getBooks() {
		return books;
	}

	/**
	 * Replaces all books. Providing a null object does not nullify the current collection but will just
	 * empty it. This method does not check for books with unique names.
	 * 
	 * @param books
	 */
	public void setBooks(Collection<? extends Book> books) {
		this.books.clear();
		if (null != books) {
			this.books.addAll(books);
		}
	}

	/**
	 * Replaces all books. Providing a null object does not nullify the current collection but will just
	 * empty it. This method checks for books with unique names.
	 * 
	 * @param books
	 * @throws UniqueIDException
	 */
	public void setBooksUnique(Collection<? extends Book> books) throws UniqueIDException {
		this.books.clear();
		if (null != books) {
			for (Book book : books) {
				addBook(book);
			}
		}
	}

	/**
	 * Get a book based on its ID.
	 * 
	 * @param id
	 * @return
	 */
	public Book getBook(String id) {
		Book result = null;
		for (Book book : books) {
			if (book.is(id)) {
				result = book;
				break;
			}
		}
		return result;
	}

	/**
	 * Checks if the model has a book which can be identified as the provided one through its ID.
	 * 
	 * @param title
	 * @return true if the book is not null and is the same as an existing one.
	 * @see Identifiable#formatId(String)
	 */
	public boolean hasBook(String title) {
		return null != title && books.stream().anyMatch(b -> b.is(title));
	}

	/**
	 * Checks if the model has a book which can be identified as the provided one.
	 * 
	 * @param book
	 * @return true if the book is not null and is the same as an existing one.
	 */
	public boolean hasBook(Book book) {
		return null != book && books.stream().anyMatch(n -> n.is(book));
	}

	/**
	 * Checks if this model has books.
	 * 
	 * @return
	 */
	public boolean hasBooks() {
		return !books.isEmpty();
	}

	/**
	 * Checks if the model has a nomenclature which can be identified as the provided one.
	 * 
	 * @param nomenclature
	 * @return true if the nomenclature is not null and is the same as an existing one.
	 */
	public boolean hasNomenclature(Nomenclature nomenclature) {
		return nomenclature != null && nomenclatures.stream().anyMatch(n -> n.is(nomenclature));
	}

	/**
	 * Checks if the model has a nomenclature which can be identified as the provided one.
	 * 
	 * @param nomenclature
	 * @return true if the nomenclature is not null and is the same as an existing one.
	 */
	public boolean hasNomenclature(String id) {
		return nomenclatures.stream().anyMatch(n -> n.is(id));
	}

	/**
	 * Checks if this model has nomenclatures, including the default one.
	 * 
	 * @return
	 */
	public boolean hasNomenclatures() {
		return !nomenclatures.isEmpty();
	}

	/**
	 * Checks if this model has nomenclatures except default ones.
	 * 
	 * @return
	 * @see Nomenclature#isDefaultNomenclature()
	 */
	public boolean hasCustomNomenclatures() {
		return !nomenclatures.isEmpty() && !nomenclatures.stream().allMatch(n -> n.isDefaultNomenclature());
	}

	/**
	 * @return the nomenclatures
	 */
	public List<Nomenclature> getNomenclatures() {
		return nomenclatures;
	}

	/**
	 * Returns a set of nomenclatures with a choice to exclude all default nomenclatures.
	 * 
	 * @param excludeDefaults if set to true, will include default nomenclatures
	 * @return the nomenclatures
	 */
	public List<Nomenclature> getNomenclatures(boolean excludeDefaults) {
		List<Nomenclature> nomenclatures = null;
		if (!excludeDefaults) {
			nomenclatures = this.nomenclatures;
		} else {
			nomenclatures = new ArrayList<>(this.nomenclatures);
			nomenclatures.removeIf(n -> n.isDefaultNomenclature());
		}
		return nomenclatures;
	}

	/**
	 * Gets a nomenclature from its ID if it exists.
	 * 
	 * @param id ID of the nomenclature
	 * @return the nomenclature with the provided name/id or null if it doesn't exist
	 */
	public Nomenclature getNomenclature(String id) {
		Nomenclature result = null;
		for (Nomenclature nomenclature : nomenclatures) {
			if (nomenclature.is(id)) {
				result = nomenclature;
				break;
			}
		}
		return result;
	}

	/**
	 * Gets the first default nomenclature in the set of nomenclatures.
	 * 
	 * @see DefaultNomenclature
	 * @return
	 * @see Nomenclature#isDefaultNomenclature()
	 */
	public Nomenclature getDefaultNomenclature() {
		Nomenclature result = null;
		for (Nomenclature nomenclature : nomenclatures) {
			if (nomenclature.isDefaultNomenclature()) {
				result = nomenclature;
				break;
			}
		}
		return result;
	}

	/**
	 * Gets a list of nomenclature according to a predicate.
	 * 
	 * @param predicate
	 * @return
	 */
	public List<Nomenclature> getNomenclatures(Predicate<Nomenclature> predicate) {
		List<Nomenclature> result = new ArrayList<>();
		for (Nomenclature nomenclature : nomenclatures) {
			if (predicate.test(nomenclature)) {
				result.add(nomenclature);
			}
		}
		return result;
	}

	/**
	 * Replaces all nomenclatures. Providing a null object does not nullify the current collection but
	 * will just empty it. This method does not check for unique ID.
	 * 
	 * @param nomenclatures
	 */
	public void setNomenclatures(Collection<? extends Nomenclature> nomenclatures) {
		this.nomenclatures.clear();
		if (null != nomenclatures) {
			this.nomenclatures.addAll(nomenclatures);
		}
	}

	/**
	 * Replaces all nomenclatures. Providing a null object does not nullify the current collection but
	 * will just empty it.
	 * 
	 * @param nomenclatures
	 * @throws UniqueIDException if any of the provided nomenclatures have the same name
	 */
	public void setNomenclaturesUnique(Collection<? extends Nomenclature> nomenclatures) throws UniqueIDException {
		this.nomenclatures.clear();
		if (null != nomenclatures) {
			for (Nomenclature nomenclature : nomenclatures) {
				addNomenclature(nomenclature);
			}
		}
	}
}
