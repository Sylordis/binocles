package com.github.sylordis.binocles.ui.components;

import com.github.sylordis.binocles.model.review.ReviewableContent;
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
			setText(item.toString());
		}
	}

}