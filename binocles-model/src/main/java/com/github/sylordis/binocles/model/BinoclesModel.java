package com.github.sylordis.binocles.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.exceptions.UniqueNameException;
import com.github.sylordis.binocles.model.review.DefaultNomenclature;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.utils.Identifiable;
import com.google.common.base.Preconditions;

/**
 * Model of the application.
 * 
 * @author sylordis
 *
 */
public class BinoclesModel {

	/**
	 * Set of books.
	 */
	private final Set<Book> books;
	/**
	 * Set of nomenclatures.
	 */
	private final Set<Nomenclature> nomenclatures;

	/**
	 * Local logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Creates a new model.
	 */
	public BinoclesModel() {
		this.books = new TreeSet<>(Comparator.comparing(Book::getId));
		this.nomenclatures = new TreeSet<>(Comparator.comparing(Nomenclature::getId));
		nomenclatures.add(new DefaultNomenclature());
	}

	/**
	 * Adds a new book to the model.
	 * 
	 * @param book
	 * @param nomenclature
	 * @throws UniqueNameException if a book or nomenclature with the same id exists in the model.
	 */
	public void addBook(Book book) throws UniqueNameException {
		Preconditions.checkArgument(book.hasId(), "Book title can't be empty or null", book);
		if (books.stream().anyMatch(b -> b.is(book))) {
			throw new UniqueNameException(Book.class, book.getTitle());
		}
		books.add(book);
		logger.info("New book added: '{}'", book.getTitle());
	}

	/**
	 * Adds a nomenclature to the model.
	 * 
	 * @param nomenclature
	 * @throws UniqueNameException if a nomenclature with the same id exists in the model.
	 */
	public void addNomenclature(Nomenclature nomenclature) throws UniqueNameException {
		Preconditions.checkArgument(nomenclature.hasId(), "Nomenclature id cannot be empty or null", nomenclature);
		if (nomenclatures.stream().anyMatch(b -> b.is(nomenclature))) {
			throw new UniqueNameException(Nomenclature.class, nomenclature.getName());
		}
		nomenclatures.add(nomenclature);
		logger.info("New nomenclature added: '{}'", nomenclature.getName());
	}

	/**
	 * @return the books
	 */
	public Set<Book> getBooks() {
		return books;
	}

	/**
	 * Replaces all books. Providing a null object does not nullify the current collection but will just
	 * empty it.
	 * 
	 * @param books
	 */
	public void setBooks(Collection<? extends Book> books) {
		this.books.clear();
		if (null != books)
			this.books.addAll(books);
	}

	/**
	 * Checks if the model has a book which can be identified as the provided one.
	 * 
	 * @param title
	 * @return true if the book is not null and is the same as an existing one.
	 */
	public boolean hasBook(String title) {
		return null != title && books.stream().anyMatch(b -> b.is(Identifiable.formatId(title)));
	}

	/**
	 * Checks if the model has a book which can be identified as the provided one.
	 * 
	 * @param book
	 * @return true if the book is not null and is the same as an existing one.
	 */
	public boolean hasBook(Book book) {
		return book != null && books.stream().anyMatch(n -> n.is(book));
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
	 * Checks if this model has nomenclatures.
	 * 
	 * @return
	 */
	public boolean hasNomenclatures() {
		return !nomenclatures.isEmpty();
	}

	/**
	 * Checks if this model has nomenclatures except the default one.
	 * 
	 * @return
	 */
	public boolean hasCustomNomenclatures() {
		return nomenclatures.size() > 1;
	}

	/**
	 * @return the nomenclatures
	 */
	public Set<Nomenclature> getNomenclatures() {
		return nomenclatures;
	}

	/**
	 * Returns a non modifiable set of nomenclatures with a choice to exclude the default nomenclature.
	 * @param includeDefault if set to true, will include the default nomenclature
	 * @return the nomenclatures
	 */
	public Set<Nomenclature> getNomenclatures(boolean includeDefault) {
		Set<Nomenclature> nomenclatures = null;
		if (includeDefault) {
			nomenclatures = this.nomenclatures;
		} else {
			nomenclatures = new TreeSet<>(Comparator.comparing(Nomenclature::getId));
			nomenclatures.addAll(this.nomenclatures);
			nomenclatures.removeIf(n -> n.isDefaultNomenclature());
		}
		return Collections.unmodifiableSet(nomenclatures);
	}

	/**
	 * Gets a nomenclature from its ID if it exists.
	 * 
	 * @param id ID of the nomenclature
	 * @return a nomenclature or null
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
	 * Gets the default nomenclature.
	 * 
	 * @see DefaultNomenclature
	 * @return
	 */
	public Nomenclature getDefaultNomenclature() {
		return getNomenclature(DefaultNomenclature.NAME);
	}

	/**
	 * Replaces all nomenclatures except the default. Providing a null object does not nullify the current collection but
	 * will just empty it.
	 * 
	 * @param nomenclatures
	 */
	public void setNomenclatures(Collection<? extends Nomenclature> nomenclatures) throws UniqueNameException {
		this.nomenclatures.clear();
		this.nomenclatures.add(new DefaultNomenclature());
		if (null != nomenclatures) {
			for (Nomenclature nomenclature : nomenclatures) {
				addNomenclature(nomenclature);
			}
		}
	}
}
