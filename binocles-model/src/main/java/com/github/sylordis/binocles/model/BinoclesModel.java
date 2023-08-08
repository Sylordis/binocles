package com.github.sylordis.binocles.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocle.utils.Identifiable;
import com.github.sylordis.binocles.model.exceptions.UniqueNameException;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
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
	 * Checks if the model has a book which can be identified as the provided one.
	 * @param title
	 * @return true if the book is not null and is the same as an existing one.
	 */
	public boolean hasBook(String title) {
		return null != title && books.stream().anyMatch(b -> b.is(Identifiable.formatId(title)));
	}
	
	/**
	 * Checks if the model has a book which can be identified as the provided one.
	 * @param book
	 * @return true if the book is not null and is the same as an existing one.
	 */
	public boolean hasBook(Book book) {
		return book != null && books.stream().anyMatch(n -> n.is(book));
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
	 * @return the nomenclatures
	 */
	public Set<Nomenclature> getNomenclatures() {
		return nomenclatures;
	}

}
