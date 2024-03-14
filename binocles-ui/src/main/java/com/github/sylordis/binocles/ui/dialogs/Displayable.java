package com.github.sylordis.binocles.ui.dialogs;

import java.util.Optional;

/**
 * Interface for custom dialogs for default calls. Those classes contain, manage and affect their
 * own UI elements.
 * 
 * It is custom to create a protected method build() to call in the display. This build() method
 * will construct and setup all UI elements of the dialog.
 * 
 * @param R type of an answer that might be provided by the dialog. Set {@link Void} for dialogs
 *          without answers, use {@link Optional} for dialogs that can be cancelled and might return
 *          a null answer.
 */
public interface Displayable<R> {

	/**
	 * Builds the pane and displays it.
	 */
	R display();

}
