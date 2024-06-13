package com.github.sylordis.binocles.ui.doa;

import javafx.scene.control.TreeItem;

public record TreeCellTextSupplierIdentifier<T>(Class<? extends T> type, CellExpansion state) {

	public enum CellExpansion {
		COLLAPSED,
		EXPANDED,
		ANY
	}

	/**
	 * Matches the exact expansion state of a tree item compared to this one.
	 * 
	 * @param state
	 * @return
	 */
	public boolean matchExactExpansionState(TreeItem<?> item) {
		return (state == CellExpansion.COLLAPSED && !item.isExpanded())
		        || (state == CellExpansion.EXPANDED && item.isExpanded());
	}
}
