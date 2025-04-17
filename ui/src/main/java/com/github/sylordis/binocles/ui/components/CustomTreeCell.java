package com.github.sylordis.binocles.ui.components;

import java.util.function.Function;

import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.javafxutils.TreeItemTextSupplierManager;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;

/**
 * Defines a custom tree cell that 
 * 
 * @param <T>
 */
public class CustomTreeCell<T> extends TreeCell<T> {

	private TreeItemTextSupplierManager<T> supplierMgr;

	/**
	 * Builds a new custom tree cell.
	 */
	public CustomTreeCell() {
		this.supplierMgr = new TreeItemTextSupplierManager<>();
	}

	/**
	 * Builds a new custom tree cell with a provided text supplier manager.
	 */
	public CustomTreeCell(TreeItemTextSupplierManager<T> mgr) {
		this.supplierMgr = mgr;
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
			Function<T, String> supplier = supplierMgr.getSupplier(item.getClass(), getTreeItem());
			if (supplier == null)
				setText(item.toString());
			else
				setText(supplier.apply(item));
		}
	}

	/**
	 * @return the supplier
	 */
	protected TreeItemTextSupplierManager<T> getSupplier() {
		return supplierMgr;
	}

	/**
	 * @param supplier the supplier to set
	 */
	protected void setSupplier(TreeItemTextSupplierManager<T> supplier) {
		this.supplierMgr = supplier;
	}

}
