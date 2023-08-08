package com.github.sylordis.binocles.ui.components;

import com.github.sylordis.binocles.model.review.NomenclatureItem;
import com.github.sylordis.binocles.ui.AppIcons;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;

/**
 * Tree cell renderer for the review tree.
 * 
 * @author sylordis
 *
 */
public class NomenclatureTreeCell extends TreeCell<NomenclatureItem> {

	@Override
	protected void updateItem(NomenclatureItem item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			setText("");
		} else {
			Image img = AppIcons.getImageForType(item.getClass());
			if (img != null)
				setGraphic(AppIcons.createImageViewFromConfig(img));
			setText(item.toString());
		}
	}

}