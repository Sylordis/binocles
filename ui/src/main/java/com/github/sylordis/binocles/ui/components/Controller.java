package com.github.sylordis.binocles.ui.components;

/**
 * Sets a contract on controllers so they would be able to contact the parent controller.
 */
public interface Controller {

	/**
	 * Gets the parent controller if it exists.
	 * 
	 * Default returns null.
	 * 
	 * @return the parent controller or null if none was set.
	 */
	default Controller getParentController() {
		return null;
	}

	/**
	 * Sets the parent controller.
	 * 
	 * It is empty by default.
	 * 
	 * @param parent parent controller to set
	 */
	default void setParentController(Controller parent) {
		// Nothing to do.
	}

	/**
	 * When called from the child, this method checks data in the controller that a child changed and
	 * something might have to change in the parent.
	 * 
	 * It is empty by default.
	 */
	default void childNotify() {
		// Nothing to do.
	}

}
