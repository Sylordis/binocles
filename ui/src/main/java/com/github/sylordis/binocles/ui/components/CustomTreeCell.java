package com.github.sylordis.binocles.ui.components;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.doa.TreeCellTextSupplierIdentifier;
import com.github.sylordis.binocles.ui.doa.TreeCellTextSupplierIdentifier.CellExpansion;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;

/**
 * Defines a custom tree cell that can render it's item of type T with decorators defined on the fly
 * and provided with the {@link #decorate(Class, CellExpansion, Function)} methods.
 * 
 * @param <T>
 */
public class CustomTreeCell<T> extends TreeCell<T> {

	/**
	 * Text supplier for a tree item.
	 */
	private Map<TreeCellTextSupplierIdentifier<T>, Function<T, String>> textSuppliers;

	/**
	 * Builds a new custom tree cell.
	 */
	public CustomTreeCell() {
		this.textSuppliers = new HashMap<>();
	}

	/**
	 * Adds a new text supplier for all states of a tree cell (expanded or collapsed).
	 * 
	 * @param type
	 * @param supplier
	 * @return
	 */
	public CustomTreeCell<T> decorate(Class<? extends T> type, Function<? extends T, String> supplier) {
		this.textSuppliers.put(new TreeCellTextSupplierIdentifier<T>(type, CellExpansion.ANY),
		        (Function<T, String>) supplier);
		return this;
	}

	/**
	 * Adds a new text supplier for a particular class and a particular tree cell state. For
	 * {@link CellExpansion#ANY}, one can also use {@link #decorate(Class, Function)}.
	 * 
	 * @param type
	 * @param expansionType
	 * @param supplier
	 * @return
	 */
	public CustomTreeCell<T> decorate(Class<? extends T> type, CellExpansion expansionType,
	        Function<? extends T, String> supplier) {
		this.textSuppliers.put(new TreeCellTextSupplierIdentifier<T>(type, expansionType),
		        (Function<T, String>) supplier);
		return this;
	}

	/**
	 * Gets a specific supplier according to current state of the item. It will first return the
	 * supplier corresponding to the exact type, then return the {@link CellExpansion#ANY} one, or null
	 * if no specific or any are set up.
	 * 
	 * @param type
	 * @return
	 */
	private Function<T, String> getSupplier(Class<? extends Object> type) {
		Function<T, String> supplierSpecific = null;
		Function<T, String> supplierAny = null;
		for (TreeCellTextSupplierIdentifier<T> id : textSuppliers.keySet()) {
			if (id.type().equals(type)) {
				if (id.matchExactExpansionState(getTreeItem()))
					supplierSpecific = textSuppliers.get(id);
				if (id.state() == CellExpansion.ANY)
					supplierAny = textSuppliers.get(id);
			}
		}
		return supplierSpecific != null ? supplierSpecific : supplierAny;
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			setText("");
			setGraphic(null);
		} else {
			Image img = AppIcons.getImageForType(item.getClass());
			if (img != null)
				setGraphic(AppIcons.createImageViewFromConfig(img));
			Function<T, String> supplier = getSupplier(item.getClass());
			if (supplier == null)
				setText(item.toString());
			else
				setText(supplier.apply(item));
		}
	}

}
