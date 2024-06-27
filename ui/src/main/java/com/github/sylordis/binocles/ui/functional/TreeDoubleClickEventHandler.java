package com.github.sylordis.binocles.ui.functional;

import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class TreeDoubleClickEventHandler implements EventHandler<MouseEvent> {

	private final Consumer<TreeItem<?>> consumer;
	private final TreeView<?> tree;

	public TreeDoubleClickEventHandler(TreeView<?> tree, Consumer<TreeItem<?>> consumer) {
		this.consumer = consumer;
		this.tree = tree;
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getClickCount() == 2 && !tree.getSelectionModel().isEmpty()) {
			consumer.accept(tree.getSelectionModel().getSelectedItem());
		}
	}

}
