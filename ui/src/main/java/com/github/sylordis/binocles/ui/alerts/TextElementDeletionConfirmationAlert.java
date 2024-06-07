package com.github.sylordis.binocles.ui.alerts;

import com.github.sylordis.binocles.model.review.ReviewableContent;
import com.github.sylordis.binocles.ui.components.BookTreeCell;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class TextElementDeletionConfirmationAlert extends Alert {

	private Label label;
	private TreeView<ReviewableContent> tree;

	public TextElementDeletionConfirmationAlert() {
		super(AlertType.CONFIRMATION);
		this.setContentText("Are you sure you want to delete this item?");
		build();
	}

	private void build() {
		label = new Label("This is what you'll be deleting:");
		tree = new TreeView<ReviewableContent>();
		tree.setCellFactory(p -> {
			return new BookTreeCell();
		});

		tree.setMaxWidth(Double.MAX_VALUE);
		tree.setMaxHeight(Double.MAX_VALUE);
		GridPane grid = new GridPane();
		grid.setMaxWidth(Double.MAX_VALUE);
		GridPane.setVgrow(tree, Priority.ALWAYS);
		GridPane.setHgrow(tree, Priority.ALWAYS);
		grid.addRow(0, label);
		grid.addRow(1, tree);

		this.getDialogPane().setExpandableContent(grid);
	}

	public void setTreeRoot(ReviewableContent rootItem) {
		if (null != tree.getRoot())
			tree.getRoot().getChildren().clear();
		if (rootItem != null) {
			TreeItem<ReviewableContent> root = new TreeItem<ReviewableContent>(rootItem);
			tree.setRoot(root);
			if (rootItem.hasChildren()) {
				rootItem.getChildren().forEach(c -> {
					TreeItem<ReviewableContent> item = new TreeItem<>(c);
					root.getChildren().add(item);
				});
			}
		}
	}
}
