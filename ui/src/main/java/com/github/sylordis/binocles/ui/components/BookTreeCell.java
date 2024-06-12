package com.github.sylordis.binocles.ui.components;

import java.util.function.Function;

import com.github.sylordis.binocles.model.text.ReviewableContent;
import com.github.sylordis.binocles.ui.AppIcons;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;

/**
 * Tree cell renderer for the review tree.
 * 
 * @author sylordis
 *
 */
public class BookTreeCell extends TreeCell<ReviewableContent> {

	/**
	 * Text supplier for a tree item.
	 */
	private Function<ReviewableContent, String> textSupplier;

	/**
	 * Constructs a tree cell with default string supplier being outputting the title.
	 * {@link ReviewableContent#toDetailedString()}/
	 */
	public BookTreeCell() {
		this(c -> c.getTitle());
	}

	/**
	 * Creates a new tree cell with provided string supplier for items.
	 * 
	 * @param textSupplier
	 */
	public BookTreeCell(Function<ReviewableContent, String> textSupplier) {
		super();
		this.textSupplier = textSupplier;
	}

	@Override
	protected void updateItem(ReviewableContent item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			setText("");
			setGraphic(null);
		} else {
			Image img = AppIcons.getImageForType(item.getClass());
			if (img != null)
				setGraphic(AppIcons.createImageViewFromConfig(img));
			setText(textSupplier.apply(item));
		}
	}

	/**
	 * @return the textSupplier
	 */
	public Function<ReviewableContent, String> getTextSupplier() {
		return textSupplier;
	}

	/**
	 * @param textSupplier the textSupplier to set
	 */
	public void setTextSupplier(Function<ReviewableContent, String> textSupplier) {
		this.textSupplier = textSupplier;
	}

}