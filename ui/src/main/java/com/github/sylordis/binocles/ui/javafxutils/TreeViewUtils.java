package com.github.sylordis.binocles.ui.javafxutils;

import java.util.Collection;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

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
	 * Builds a default callback for CheckboxTreeCells.
	 * 
	 * @param <T> Type of the tree view items.
	 * @return a default callback or null if the cell is not a {@link CheckBoxTreeItem}.
	 */
	public static <T> Callback<TreeItem<T>, ObservableValue<Boolean>> getCheckBoxTreeCellDefaultCallback() {
		return item -> {
			if (item instanceof CheckBoxTreeItem<?>)
				return ((CheckBoxTreeItem<?>) item).selectedProperty();
			return null;
		};
	}

	public static <T> void removeSubtree(Collection<TreeItem<T>> collection, CheckBoxTreeItem<T> item) {
	    if (item.isSelected()) {
	        collection.remove(item);
	    } else if (!item.isIndeterminate() && !item.isIndependent()) {
	        return;
	    }
	    for (TreeItem<T> child : item.getChildren()) {
	        removeSubtree(collection, (CheckBoxTreeItem<T>) child);
	    }
	}
	
	public static <T> void addSubtree(Collection<TreeItem<T>> collection, CheckBoxTreeItem<T> item) {
	    if (item.isSelected()) {
	        collection.add(item);
	    } else if (!item.isIndeterminate() && !item.isIndependent()) {
	        return;
	    }
	    for (TreeItem<T> child : item.getChildren()) {
	        addSubtree(collection, (CheckBoxTreeItem<T>) child);
	    }
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private TreeViewUtils() {
		// Nothing to do here.
	}
}
