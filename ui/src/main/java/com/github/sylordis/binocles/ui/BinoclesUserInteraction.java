package com.github.sylordis.binocles.ui;

import java.io.File;
import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BinoclesUserInteraction {

	/**
	 * Current file used for save.
	 */
	private SimpleObjectProperty<File> currentSaveFileProperty = new SimpleObjectProperty<File>();
	/**
	 * Property to determine if something in the model changed. False = not saved, true = saved (i.e. no
	 * modification happened since last save).
	 */
	private BooleanProperty currentModelIsSavedProperty = new SimpleBooleanProperty(false);
	/**
	 * Last time the model was saved.
	 */
	private SimpleObjectProperty<LocalDateTime> lastSaveTimeProperty = new SimpleObjectProperty<LocalDateTime>();

	/**
	 * 
	 * @return the current save file (or null if none)
	 */
	public File getCurrentSaveFile() {
		return currentSaveFileProperty.get();
	}

	/**
	 * @return the currentFileProperty
	 */
	public SimpleObjectProperty<File> currentSaveFileProperty() {
		return currentSaveFileProperty;
	}

	/**
	 * @param file the current save file to set
	 */
	public void setCurrentSaveFile(File file) {
		this.currentSaveFileProperty.set(file);
	}

	/**
	 * Checks if a save file is recorded.
	 * 
	 * @return
	 */
	public boolean hasSaveFile() {
		return this.currentSaveFileProperty.isNotNull().get();
	}

	/**
	 * 
	 * @return if the current model is saved
	 */
	public boolean isCurrentModelSaved() {
		return currentModelIsSavedProperty.get();
	}
	
	/**
	 * @return the currentModelIsSaved property
	 */
	public BooleanProperty currentModelIsSavedProperty() {
		return currentModelIsSavedProperty;
	}

	/**
	 * Sets the current model as saved.
	 */
	public void modelWasSaved() {
		this.currentModelIsSavedProperty.set(true);
	}

	/**
	 * Sets the current model as modified compared to previous save.
	 */
	public void modelWasModified() {
		this.currentModelIsSavedProperty.set(false);
	}

	/**
	 * @return the lastSaveTimeProperty
	 */
	public SimpleObjectProperty<LocalDateTime> lastSaveTimeProperty() {
		return lastSaveTimeProperty;
	}

	/**
	 * @param dt the lastSavedTime to set
	 */
	public void setLastSavedTime(LocalDateTime dt) {
		this.lastSaveTimeProperty.set(dt);
	}

	/**
	 * 
	 * @return the last save time
	 */
	public LocalDateTime getLastSaveTime() {
		return this.lastSaveTimeProperty.get();
	}

	/**
	 * Sets the last save time to now.
	 */
	public void setSaved() {
		this.lastSaveTimeProperty.set(LocalDateTime.now());
	}

	/**
	 * Sets the last save time to now.
	 */
	public void setSaved(File file) {
		this.currentSaveFileProperty.set(file);
		this.currentModelIsSavedProperty.set(true);
		this.lastSaveTimeProperty.set(LocalDateTime.now());
	}

}
