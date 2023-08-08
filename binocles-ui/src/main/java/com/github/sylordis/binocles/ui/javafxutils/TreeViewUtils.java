package com.github.sylordis.binocles.ui.javafxutils;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Utilities classes for {@link TreeView} component.
 * 
 * @author sylordis
 *
 */
public final class TreeViewUtils {

	/**
	 * Searches an item in a javafx tree containing a given object. This method is recursive on all
	 * children.
	 * 
	 * @param <R>    Type of the items contained in the tree
	 * @param item   current item to search from
	 * @param needle item to search for
	 * @return the tree item containing the needle or null if it cannot be found
	 */
	public static <T> TreeItem<T> getTreeViewItem(TreeItem<T> item, T needle) {
		TreeItem<T> value = null;
		if (item != null && item.getValue().equals(needle)) {
			value = item;
		} else {
			for (TreeItem<T> child : item.getChildren()) {
				TreeItem<T> s = getTreeViewItem(child, needle);
				if (s != null) {
					value = s;
					break;
				}

			}
		}
		return value;
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private TreeViewUtils() {
		// Nothing to do here.
	}
}
