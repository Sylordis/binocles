package com.github.sylordis.binocles.ui.javafxutils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public final class FXFormatUtils {

	/**
	 * Binds a node to its parent's boundaries, calling all anchor setters of {@link AnchorPane}.
	 * 
	 * @param node node to bind
	 * @see AnchorPane#setBottomAnchor(Node, Double)
	 * @see AnchorPane#setLeftAnchor(Node, Double)
	 * @see AnchorPane#setRightAnchor(Node, Double)
	 * @see AnchorPane#setTopAnchor(Node, Double)
	 */
	public static void bindToParent(Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private FXFormatUtils() {
		// Nothing to see here
	}

}
