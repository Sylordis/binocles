package com.github.sylordis.binocles.ui.components;

import java.util.function.Function;

import javafx.scene.control.ListCell;

/**
 * Custom cell for specific renders.
 * @author sylordis
 *
 * @param <T>
 */
public class CustomListCell<T> extends ListCell<T> {

	/**
	 * Provider fo
	 */
	private Function<T,String> textProvider;
	
	public CustomListCell(Function<T,String> textProvider) {
		this.textProvider = textProvider;
	}
	
	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (null == item || empty)
			setText("");
		else
			setText(textProvider.apply(item));
	}
	
}
