package com.github.sylordis.binocles.ui.doa;

import javafx.scene.control.TreeItem;

public record TreeCellTextSupplierIdentifier<T>(Class<? extends T> type, CellExpansion state) {

	/**
	 * Enum to denote of a current node's expansion state.
	 */
	public enum CellExpansion {
		/**
		 * A collapsed tree cell.
		 */
		COLLAPSED,
		/**
		 * An expanded tree cell.
		 */
		EXPANDED,
		/**
		 * A cell in any of its expansion state.
		 */
		ANY
	}

	/**
	 * Matches the exact expansion state of a tree item compared to this one.
	 * 
	 * @param state
	 * @return
	 */
	public boolean matchExactExpansionState(TreeItem<?> item) {
		return item != null && ((state == CellExpansion.COLLAPSED && !item.isExpanded())
		        || (state == CellExpansion.EXPANDED && item.isExpanded()));
	}
}
