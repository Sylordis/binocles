package com.github.sylordis.binocles.ui.functional;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

/**
 * Event handler to react on clicks on a tree item.
 */
public class TreeVarClickEventHandler implements EventHandler<MouseEvent> {

	/**
	 * Consumer to run when the event is triggered with the right amount of clicks.
	 */
	private final Consumer<TreeItem<?>> consumer;
	/**
	 * Tree managed by the event handler.
	 */
	private final TreeView<?> tree;

	private Supplier<Integer> clickNumberSupplier;

	/**
	 * Creates a new double click event handler.
	 * 
	 * @param tree tree managed by this event handler
	 * @param consumer tree item consumer, the one which was clicked and triggered this event handler
	 * @return
	 */
	public static final TreeVarClickEventHandler createDoubleClickHandler(TreeView<?> tree,
	        Consumer<TreeItem<?>> consumer) {
		return new TreeVarClickEventHandler(tree, consumer, 2);
	}

	/**
	 * Creates a single click event handler.
	 * 
	 * @param tree tree managed by this event handler
	 * @param consumer tree item consumer, the one which was clicked and triggered this event handler
	 * @return
	 */
	public static final TreeVarClickEventHandler createSingleClickHandler(TreeView<?> tree,
	        Consumer<TreeItem<?>> consumer) {
		return new TreeVarClickEventHandler(tree, consumer, 1);
	}

	/**
	 * Creates a new variable click event handler for trees.
	 * 
	 * @param tree tree managed by this event handler
	 * @param consumer tree item consumer, the one which was clicked and triggered this event handler
	 * @param nclicks supplier for the number of clicks needed to trigger this event handler
	 */
	public TreeVarClickEventHandler(TreeView<?> tree, Consumer<TreeItem<?>> consumer, Supplier<Integer> nclicks) {
		this.consumer = consumer;
		this.tree = tree;
		this.clickNumberSupplier = nclicks;
	}

	/**
	 * Creates a new variable click event handler for trees with a provided number of clicks.
	 * 
	 * @param tree tree managed by this event handler
	 * @param consumer tree item consumer, the one which was clicked and triggered this event handler
	 * @param nclicks number of clicks needed to trigger this event handler
	 */
	public TreeVarClickEventHandler(TreeView<?> tree, Consumer<TreeItem<?>> consumer, int nclicks) {
		this(tree, consumer, () -> nclicks);
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getClickCount() == clickNumberSupplier.get() && !tree.getSelectionModel().isEmpty()) {
			consumer.accept(tree.getSelectionModel().getSelectedItem());
		}
	}

}
