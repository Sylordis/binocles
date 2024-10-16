package com.github.sylordis.binocles.ui.alerts;

import com.github.sylordis.binocles.model.decorators.CommentTypeDecorator;
import com.github.sylordis.binocles.model.decorators.NomenclatureDecorator;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.review.NomenclatureItem;
import com.github.sylordis.binocles.ui.components.CustomTreeCell;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Alert for deletion confirmation of a text element.
 */
public class ReviewElementDeletionConfirmationAlert extends Alert {

	/**
	 * Label of the tree.
	 */
	private Label label;
	/**
	 * Content tree to display what is about to be deleted.
	 */
	private TreeView<NomenclatureItem> tree;

	/**
	 * Creates a new text element deletion confirmation alert.
	 */
	public ReviewElementDeletionConfirmationAlert() {
		super(AlertType.CONFIRMATION);
		this.setContentText("Are you sure you want to delete the following items?");
		build();
	}

	/**
	 * Sets up and configures all visual elements of the expended content.
	 */
	private void build() {
		label = new Label("This is what you'll be deleting:");
		tree = new TreeView<NomenclatureItem>();
		tree.setCellFactory(p -> {
			return new CustomTreeCell<NomenclatureItem>()
			        .decorate(Nomenclature.class, new NomenclatureDecorator().thenName())
			        .decorate(CommentType.class, new CommentTypeDecorator().thenName());
		});

		tree.setMaxWidth(Double.MAX_VALUE);
//		tree.setMaxHeight(Double.MAX_VALUE);
		GridPane grid = new GridPane();
		grid.setMaxWidth(Double.MAX_VALUE);
		GridPane.setVgrow(tree, Priority.ALWAYS);
		GridPane.setHgrow(tree, Priority.ALWAYS);
		grid.addRow(0, label);
		grid.addRow(1, tree);

		this.getDialogPane().setExpandableContent(grid);
		this.getDialogPane().setExpanded(true);
	}

	/**
	 * Sets the tree root and expand it if it has children.
	 * 
	 * @param rootItem
	 */
	public void setTreeRoot(NomenclatureItem rootItem) {
		if (null != tree.getRoot())
			tree.getRoot().getChildren().clear();
		if (rootItem != null) {
			TreeItem<NomenclatureItem> root = new TreeItem<NomenclatureItem>(rootItem);
			tree.setRoot(root);
			if (rootItem.hasChildren()) {
				root.setExpanded(true);
				rootItem.getChildren().forEach(c -> {
					TreeItem<NomenclatureItem> item = new TreeItem<>(c);
					root.getChildren().add(item);
				});
			}
		}
	}

}
