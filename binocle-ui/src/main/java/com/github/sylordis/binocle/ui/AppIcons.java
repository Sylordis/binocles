package com.github.sylordis.binocle.ui;

import com.github.sylordis.binocle.model.review.CommentType;
import com.github.sylordis.binocle.model.review.Nomenclature;
import com.github.sylordis.binocle.model.text.Book;
import com.github.sylordis.binocle.model.text.Chapter;
import com.github.sylordis.binocle.ui.settings.BinocleConfiguration;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Holds the icons configuration.
 * 
 * @author sylordis
 *
 */
public final class AppIcons {

	/**
	 * Icon for the software.
	 */
	public static final Image ICON_SOFTWARE = new Image(AppIcons.class.getResourceAsStream("img/ink.png"));

	/**
	 * Icon for {@link Book}.
	 */
	public static final Image ICON_BOOK = new Image(AppIcons.class.getResourceAsStream("img/book.png"));

	/**
	 * Icon for {@link Chapter}.
	 */
	public static final Image ICON_CHAPTER = new Image(AppIcons.class.getResourceAsStream("img/chapter.png"));

	/**
	 * Icon for {@link Nomenclature}/Nomenclature.
	 */
	public static final Image ICON_NOMENCLATURE = new Image(AppIcons.class.getResourceAsStream("img/options.png"));

	/**
	 * Icon for {@link CommentType}.
	 */
	public static final Image ICON_COMMENT_TYPE = new Image(AppIcons.class.getResourceAsStream("img/typography.png"));

	/**
	 * Creates an image view based on the current display size of the {@link BinocleConfiguration}.
	 * @param image Image to wrap into a view
	 * @return
	 * @see BinocleConfiguration#getDisplaySize
	 */
	public static final ImageView createImageViewFromConfig(Image image) {
		ImageView view = new ImageView(image);
		view.setPreserveRatio(true);
		view.setFitWidth(BinocleConfiguration.getInstance().getDisplaySize().getIconMaxSize());
		view.setFitHeight(BinocleConfiguration.getInstance().getDisplaySize().getIconMaxSize());
		return view;
	}
}
