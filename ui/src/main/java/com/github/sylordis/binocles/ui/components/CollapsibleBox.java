package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.sylordis.binocles.ui.AppIcons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CollapsibleBox extends VBox implements Initializable {

	/**
	 * Buttons available for the box.
	 */
	public enum ButtonTypes {
		EDIT,
		DELETE;

		public long getStatusFlagValue() {
			return 1 << this.ordinal();
		}
	}

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
	 * The expansion/collapse state of the box. True is expanded, False is collapsed.
	 */
	private BooleanProperty expanded;

	/**
	 * Creates an empty collapsible box.
	 */
	public CollapsibleBox() {
		this.expanded = new SimpleBooleanProperty(true);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("collapsible_box.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
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

	@FXML
	public void editAction() {
		// Nothing to do
	}

	@FXML
	public void deleteAction() {
		// Nothing to do
	}

	/**
	 * Sets the background of the title.
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
