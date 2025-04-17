package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.decorators.BookDecorator;
import com.github.sylordis.binocles.model.decorators.ChapterDecorator;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.model.text.ReviewableContent;
import com.github.sylordis.binocles.ui.components.CustomTreeCell;
import com.github.sylordis.binocles.ui.doa.TreeCellTextSupplierIdentifier.CellExpansion;
import com.github.sylordis.binocles.ui.javafxutils.TreeItemTextSupplierManager;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Alert for deletion confirmation of a text element.
 */
public class TextElementDeletionConfirmationAlert extends Alert {

	/**
	 * Label of the tree.
	 */
	private Label label;
	/**
	 * Content tree to display what is about to be deleted.
	 */
	private TreeView<ReviewableContent> tree;

	/**
	 * Creates a new text element deletion confirmation alert.
	 */
	public TextElementDeletionConfirmationAlert() {
		super(AlertType.CONFIRMATION);
		this.setContentText("Are you sure you want to delete the following items?");
		build();
	}

	/**
	 * Sets up and configures all visual elements of the expended content.
	 */
	private void build() {
		label = new Label("This is what you'll be deleting:");
		tree = new TreeView<ReviewableContent>();
		tree.setCellFactory(p -> {
			return new CustomTreeCell<ReviewableContent>(new TreeItemTextSupplierManager<ReviewableContent>()
			        .decorate(Book.class, CellExpansion.COLLAPSED,
			                new BookDecorator().thenTitle().thenChapterCountWithText())
			        .decorate(Book.class, CellExpansion.EXPANDED, new BookDecorator().thenTitle())
			        .decorate(Chapter.class, new ChapterDecorator().thenTitle().thenCommentsCountWithText()));
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
	public void setTreeRoot(ReviewableContent rootItem) {
		if (null != tree.getRoot())
			tree.getRoot().getChildren().clear();
		if (rootItem != null) {
			TreeItem<ReviewableContent> root = new TreeItem<ReviewableContent>(rootItem);
			tree.setRoot(root);
			if (rootItem.hasChildren()) {
				root.setExpanded(true);
				rootItem.getChildren().forEach(c -> {
					TreeItem<ReviewableContent> item = new TreeItem<>((ReviewableContent) c);
					root.getChildren().add(item);
				});
			}
		}
	}

}
