package com.github.sylordis.binocles.ui.views;

import com.github.sylordis.binocles.ui.BinoclesController;

/**
 * 
 * @param <T> The item represented by this tab
 */
public interface BinoclesTabPane {

	/**
	 * Gets the item represented by this tab.
	 * 
	 * @return
	 */
	Object getItem();

	/**
	 * Method called by the controller when switching tabs in order to update its status.
	 * 
	 * @param controller
	 */
	default void updateControllerStatus(BinoclesController controller) {
		// Nothing to do by default
	}

}
