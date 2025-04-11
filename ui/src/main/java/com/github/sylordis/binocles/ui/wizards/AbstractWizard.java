package com.github.sylordis.binocles.ui.wizards;

import java.util.Optional;
import java.util.function.Supplier;

import org.controlsfx.dialog.Wizard;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.ui.contracts.Displayable;

import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

/**
 * 
 */
public abstract class AbstractWizard<R> implements Displayable<Optional<R>> {

	/**
	 * Model affected by the dialog.
	 */
	private BinoclesModel model;
	/**
	 * Local Wizard
	 */
	private Wizard wizard;
	/**
	 * Title of the wizard.
	 */
	private String title;
	/**
	 * Icon of the dialog, automatically configured if provided.
	 */
	private Image icon;
	/**
	 * Supplier for the result.
	 */
	private Supplier<R> resultSupplier;

	/**
	 * Creates a new custom wizard.
	 * 
	 * @param model
	 */
	public AbstractWizard(String title, BinoclesModel model) {
		super();
		this.title = title;
		this.model = model;
	}

	protected abstract void build();

	/**
	 * Set up the wizard, display it and wait for user action. It is the last method to be called.
	 * 
	 * @return
	 */
	@Override
	public Optional<R> display() {
		wizard = new Wizard();
		wizard.setTitle(title);
		
		build();
		
		Optional<R> result = Optional.empty();
		Optional<ButtonType> answer = wizard.showAndWait();
		if (answer.isPresent() && resultSupplier != null)
			result = Optional.of(resultSupplier.get());
		return result; 
	}
	
	/**
	 * @return the model
	 */
	protected BinoclesModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	protected void setModel(BinoclesModel model) {
		this.model = model;
	}

	/**
	 * @return the wizard
	 */
	protected Wizard getWizard() {
		return wizard;
	}

	/**
	 * @param wizard the wizard to set
	 */
	protected void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Image icon) {
		this.icon = icon;
	}

}
