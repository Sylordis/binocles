package com.github.sylordis.binocles.ui.wizards;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.ui.contracts.Displayable;

import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Abstract class serving as base for all future Wizards, either with a return type or none. This
 * class manages the base structure of a wizard, as icon, title and link to the model. Everything
 * content related to the wizard has to implement either {@link WizardPane} or the custom version
 * {@link AbstractWizardPane}.<br/>
 * <br/>
 * 
 * To call for the wizard, only use the {@link #display()} method.<br/>
 * </br>
 * 
 * If the return type is not <code>Void</code>, you'll have to set a {@link #resultSupplier} which
 * will be called upon finishing with the wizard, otherwise it will return {@link Optional#empty()}.
 * 
 * @param R return type, set to <code>Void</code> for no return value.
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
	 * Set of all panes of the wizard, strictly used for controls and pre-actions.
	 */
	private Set<AbstractWizardPane> panes;
	/**
	 * Owner of the wizard.
	 */
	private Object owner;

	/**
	 * Creates a new custom wizard.
	 * 
	 * @param owner Owner of the wizard
	 * @param title Title of the wizard window
	 * @param model
	 */
	public AbstractWizard(Object owner, String title, BinoclesModel model) {
		super();
		this.title = title;
		this.model = model;
		this.panes = new HashSet<AbstractWizardPane>();
	}

	/**
	 * Creates a new custom wizard with no owner.
	 * 
	 * @param model
	 */
	public AbstractWizard(String title, BinoclesModel model) {
		this(null, title, model);
	}

	/**
	 * Builds and sets up the content of the wizard.<br/>
	 * Don't forget to:
	 * <ul>
	 * <li>Add the panes to this wizard with {@link #addPane(AbstractWizardPane)} or
	 * {@link #addPanes(AbstractWizardPane...)}.</li>
	 * <li>Set the flow with <code>{@link #getWizard()}.setFlow([..])</code>.</li>
	 * </ul>
	 */
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
		if (null != icon) {
			Stage dialogStage = (Stage) wizard.getDialog().getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(icon);
		}

		build();
		for (AbstractWizardPane pane : panes)
			pane.construct();

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

	/**
	 * @return the title
	 */
	protected String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	protected void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the resultSupplier
	 */
	protected Supplier<R> getResultSupplier() {
		return resultSupplier;
	}

	/**
	 * @param resultSupplier the resultSupplier to set
	 */
	protected void setResultSupplier(Supplier<R> resultSupplier) {
		this.resultSupplier = resultSupplier;
	}

	/**
	 * Adds a pane to this wizard.<br/>
	 * <b>WARNING:</b> this does not influence the flow of the wizard but just allows basic operations
	 * like {@link AbstractWizardPane#build()}.
	 * 
	 * @param pane
	 */
	protected void addPane(AbstractWizardPane pane) {
		this.panes.add(pane);
	}

	/**
	 * Adds panes to this wizard.<br/>
	 * <b>WARNING:</b> this does not influence the flow of the wizard but just allows basic operations
	 * like {@link AbstractWizardPane#build()}.
	 * 
	 * @param panes
	 */
	protected void addPanes(AbstractWizardPane... panes) {
		this.panes.addAll(Arrays.asList(panes));
	}

	public Set<AbstractWizardPane> getPanes() {
		return panes;
	}

	public void setPanes(Set<AbstractWizardPane> panes) {
		this.panes = panes;
	}

	public Object getOwner() {
		return owner;
	}

	public void setOwner(Object owner) {
		this.owner = owner;
	}
	
}
