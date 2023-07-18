package com.github.sylordis.binocle.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.github.sylordis.binocle.model.exceptions.UniqueNameException;
import com.github.sylordis.binocle.model.review.Nomenclature;
import com.github.sylordis.binocle.model.text.Book;
import com.google.common.base.Preconditions;

/**
 * Model of the application.
 * 
 * @author sylordis
 *
 */
public class BinocleModel {

	/**
	 * Set of books.
	 */
	private final Set<Book> books;
	/**
	 * Set of nomenclatures.
	 */
	private final Set<Nomenclature> nomenclatures;

	/**
	 * Creates a new model.
	 */
	public BinocleModel() {
		this.books = new TreeSet<>(Comparator.comparing(Book::toString));
		this.nomenclatures = new TreeSet<>(Comparator.comparing(Nomenclature::toString));
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
	}

	/**
	 * @return the books
	 */
	public Set<Book> getBooks() {
		return books;
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
