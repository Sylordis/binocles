package com.github.sylordis.binocles.ui.javafxutils;

import javafx.scene.layout.GridPane;

/**
 * Utilities classes for {@link GridPane}.
 * 
 * @author sylordis
 */
public final class GridPaneUtils {

	/**
	 * Removes all children of the grid after a certain row index, included.
	 * 
	 * @param grid grid pane to remove children of
	 * @param row 
	 */
	public static void removeChildrenFromLine(GridPane grid, int row) {
		grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= row);
	}

}
