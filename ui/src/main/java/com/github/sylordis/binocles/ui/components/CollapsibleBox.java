package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.github.sylordis.binocles.ui.AppIcons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Collapsible box as a javafx component, extremely similar to Title Pane, but customisable and with
 * an available toolbar.
 * 
 * If you override {@link #initialize(URL, ResourceBundle)} make super to call for the super implementation first.
 */
public class CollapsibleBox extends VBox implements Initializable {

	// TODO Action type management, being able to setup any wanted action with their icons, proposing a
	// default set.
	@FXML
	private Button buttonCollapse;
	@FXML
	private Button buttonEdit;
	@FXML
	private Button buttonDelete;
	@FXML
	private Text boxTitle;
	@FXML
	private HBox toolbar;
	@FXML
	private HBox boxTitleContainer;
	@FXML
	private VBox mainContent;

	/**
	 * List of tools for the toolbar.
	 */
	private List<Button> tools;
	/**
	 * Custom listener for expansion state changes, in addition to the existing one.
	 */
	private ChangeListener<? super Boolean> expansionCustomListener;

	/**
	 * The expansion/collapse state of the box. True is expanded, False is collapsed.
	 */
	private BooleanProperty expanded;

	/**
	 * Creates an empty collapsible box.
	 */
	public CollapsibleBox() {
		this.expanded = new SimpleBooleanProperty(true);
		this.tools = new ArrayList<>();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("collapsible_box.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Creates a collapsible box with toolbar buttons.
	 * 
	 * @param tools all buttons to add to the toolbar, they should already be configured
	 */
	public CollapsibleBox(Button... tools) {
		this();
		this.tools.addAll(Arrays.asList(tools));
	}

	/**
	 * Creates a collapsible box with a title and toolbar options.
	 * 
	 * @param title title of the box
	 * @param tools all buttons to add to the toolbar, they should already be configured
	 */
	public CollapsibleBox(String title, Button... tools) {
		this(tools);
		boxTitle.setText(title);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Make it so being not visible removes the management by the layout
		mainContent.managedProperty().bind(mainContent.visibleProperty());
		// Set listeners
		expanded.addListener((s, o, n) -> {
			mainContent.setVisible(n);
			buttonCollapse.setGraphic(
			        AppIcons.createImageView(n ? AppIcons.ICON_ARROW_DOWN : AppIcons.ICON_ARROW_RIGHT, 8, 8));
		});
		if (expansionCustomListener != null) {
			expanded.addListener(expansionCustomListener);
		}
		// Add all buttons
		setToolbar(this.tools);
	}

	/**
	 * Removes and sets a new set of tools in the toolbar.
	 * 
	 * @param tools pre-configured buttons
	 */
	public void setToolbar(Iterable<Button> tools) {
		toolbar.getChildren().clear();
		for (Button button : tools) {
			if (button != null) {
				toolbar.getChildren().add(button);
			}
		}
	}

	/**
	 * Removes and sets a new set of tools in the toolbar.
	 * 
	 * @param tools pre-configured buttons
	 */
	public void setToolbar(Button... tools) {
		setToolbar(Arrays.asList(tools));
	}

	/**
	 * Sets and update the content for the box. If content is already set, a call to
	 * {@link #clearContent()} is in order.
	 */
	public void updateContent() {
		// Nothing here
	}

	/**
	 * Clears the content of the box in order to be updated.
	 */
	public void clearContent() {
		mainContent.getChildren().clear();
	}

	/**
	 * Checks the expansion status of the box.
	 * 
	 * @return
	 */
	public boolean isExpanded() {
		return expanded.get();
	}

	/**
	 * Toggles the expansion status.
	 */
	@FXML
	public void toggleExpanded() {
		this.expanded.set(!this.expanded.getValue());
	}

	/**
	 * Triggers all actions required to edit the current item managed by the box.
	 */
	@FXML
	public void editAction() {
		// To be overridden if such action is available.
	}

	/**
	 * Triggers all actions required to delete the current box.
	 */
	@FXML
	public void deleteAction() {
		// To be overridden if such action is available.
	}

	/**
	 * Sets the background of the title.
	 * 
	 * @param background
	 */
	public void setTitleBackground(Background background) {
		boxTitleContainer.setBackground(background);
	}

	/**
	 * Sets the expanded status of this comment box.
	 * 
	 * @param expanded
	 */
	public void setExpanded(boolean expanded) {
		this.expanded.set(expanded);
	}

	/**
	 * Sets the title's text.
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		boxTitle.setText(text);
	}

	/**
	 * @return the main content container
	 */
	public VBox getMainContent() {
		return mainContent;
	}

	/**
	 * @return the title container
	 */
	public HBox getTitleContainer() {
		return boxTitleContainer;
	}

}
