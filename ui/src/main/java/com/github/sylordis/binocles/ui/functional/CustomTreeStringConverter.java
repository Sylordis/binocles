package com.github.sylordis.binocles.ui.functional;

import java.util.function.Function;

import com.github.sylordis.binocles.ui.javafxutils.TreeItemTextSupplierManager;

import javafx.scene.control.TreeItem;
import javafx.util.StringConverter;

public class CustomTreeStringConverter<T> extends StringConverter<TreeItem<T>> {

	private TreeItemTextSupplierManager<T> supplierMgr;

	public CustomTreeStringConverter() {
		supplierMgr = new TreeItemTextSupplierManager<T>();
	}

	public CustomTreeStringConverter(TreeItemTextSupplierManager<T> mgr) {
		supplierMgr = mgr;
	}

	@Override
	public String toString(TreeItem<T> object) {
		String ret;
		Class<?> type = object.getValue().getClass();
		Function<T, String> supplier = supplierMgr.getSupplier(type, object);
		if (supplier == null)
			ret = object.getValue().toString();
		else
			ret = supplier.apply(object.getValue());
		return ret;
	}

	@Override
	public TreeItem<T> fromString(String string) {
		return null;
	}

	/**
	 * @return the supplierMgr
	 */
	protected TreeItemTextSupplierManager<T> getSupplierMgr() {
		return supplierMgr;
	}

	/**
	 * @param supplierMgr the supplierMgr to set
	 */
	protected void setSupplierMgr(TreeItemTextSupplierManager<T> supplierMgr) {
		this.supplierMgr = supplierMgr;
	}

}
