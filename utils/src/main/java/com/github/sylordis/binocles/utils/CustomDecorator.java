package com.github.sylordis.binocles.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Base class for custom Decorators which are build around a list of String suppliers to output the
 * desired information. Each supplier content will be separated by a specified separator or {@link #SEPARATOR_DEFAULT}.
 */
public class CustomDecorator<T> {

	/**
	 * Default separator for custom decorators, a whitespace.
	 */
	public static final String SEPARATOR_DEFAULT = " ";

	/**
	 * List of String suppliers. This should never be null.
	 */
	protected final List<Function<T, String>> suppliers;
	/**
	 * Separator for all entries. Default is a whitespace.
	 */
	private String separator;

	/**
	 * Creates a new custom decorator with default separator.
	 * 
	 * @see #SEPARATOR_DEFAULT
	 */
	public CustomDecorator() {
		this(SEPARATOR_DEFAULT);
	}

	/**
	 * Creates a new custom decorator with a specified separator.
	 */
	public CustomDecorator(String separator) {
		suppliers = new ArrayList<>();
		this.separator = separator;
	}

	/**
	 * Adds an entry to this decorator.
	 * 
	 * @param decorator
	 * @param suffix
	 * @param prefix
	 * @return itself
	 */
	public CustomDecorator<T> and(Function<T, String> decorator, String prefix, String suffix) {
		this.suppliers.add(t -> prefix + decorator.apply(t) + suffix);
		return this;
	}

	/**
	 * Adds multiple entries to this decorator.
	 * 
	 * @param decorators
	 * @return itself
	 */
	@SafeVarargs
	public final CustomDecorator<T> and(Function<T, String>... entries) {
		this.suppliers.addAll(Arrays.asList(entries));
		return this;
	}

	/**
	 * Prints the desired output according to the decorator configuration.
	 * 
	 * @param item
	 * @return
	 */
	public String print(T item) {
		StringBuilder builder = new StringBuilder();
		for (Function<T, String> supplier : suppliers) {
			if (!builder.isEmpty())
				builder.append(separator);
			builder.append(supplier.apply(item));
		}
		return builder.toString();
	}

	/**
	 * @return the suppliers
	 */
	public List<Function<T, String>> getSuppliers() {
		return suppliers;
	}

	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Will reset to {@link #SEPARATOR_DEFAULT} if set as null.
	 * 
	 * @param separator the separator to set
	 */
	public void setSeparator(String separator) {
		if (null == separator)
			this.separator = SEPARATOR_DEFAULT;
		else
			this.separator = separator;
	}

}
