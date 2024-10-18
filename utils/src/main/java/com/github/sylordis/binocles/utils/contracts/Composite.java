package com.github.sylordis.binocles.utils.contracts;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a class as being part of a hierarchy, e.g. that it can be a parent to multiple children
 * or it be a child.
 * 
 * Parents should override those methods, leaf items can leave them as is.
 * 
 * @param T over-arching type for all items in this composite tree.
 */
public interface Composite<T> {

	/**
	 * Checks if this instance can have children, i.e. it's not a leaf object. If this returns true,
	 * then the method {@link #getChildren()} should return a list (that can be empty if no children are
	 * there). If this item is not supposed to have children, this method should return false.
	 * 
	 * @return true if this object can have children and has some, false otherwise.
	 */
	default boolean canHaveChildren() {
		return getChildren() != null;
	}

	/**
	 * Checks if this instance has children. If this returns true, then the method
	 * {@link #getChildren()} should return a non empty list. If this item is not supposed to have
	 * children, this method should return false.
	 * 
	 * @return true if this object can have children and has some, false otherwise.
	 */
	default boolean hasChildren() {
		return getChildren() != null && !getChildren().isEmpty();
	}

	/**
	 * Gets the list of children of this object, or null if it's not suppose to have some.
	 * 
	 * @return list of children or null if it's not supposed to have any.
	 */
	default List<? extends Composite<T>> getChildren() {
		return null;
	}

	/**
	 * Checks if this object is a child of a given parent, regardless of the number of levels of
	 * parenting. This is a recursive method.
	 * 
	 * @param parent
	 * @return true if the current object is a transitive child of the parent, false otherwise.
	 */
	default boolean isChildOf(Composite<T> parent) {
		boolean is = false;
		if (parent != null && parent.hasChildren()) {
			if (parent.getChildren().contains(this))
				is = true;
			else
				return parent.getChildren().stream().anyMatch(c -> this.isChildOf((Composite<T>) c));
		}
		return is;
	}

	/**
	 * Reduces a selection of objects down to their most atomic selection, e.g. children will be removed
	 * from the list if their parents are present in the same list.
	 * 
	 * @param list list of objects to reduce
	 * @return a reduced list
	 */
	public static <T> List<Composite<T>> reduce(List<Composite<T>> list) {
		List<Composite<T>> result = new ArrayList<>();
		List<Composite<T>> remaining = new ArrayList<>(list);
		while (!remaining.isEmpty()) {
			Composite<T> element = remaining.removeFirst();
			boolean isChild = result.stream().anyMatch(r -> element.isChildOf(r)) || remaining.stream().anyMatch(r -> element.isChildOf(r));
			if (!isChild)
				result.add(element);
		}
		return result;
	}
}
