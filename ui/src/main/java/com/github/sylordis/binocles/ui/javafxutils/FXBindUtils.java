package com.github.sylordis.binocles.ui.javafxutils;

import static com.github.sylordis.binocles.utils.Flags.ifAllDo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public final class FXBindUtils {

	/**
	 * Flags for binding dimensions.
	 */
	public final class DimensionBind {
		public static final int NONE = 0x00;
		public static final int HEIGHT = 0x01;
		public static final int WIDTH = 0x10;
		public static final int ALL = HEIGHT | WIDTH;
	}

	/**
	 * Flags for binding size types.
	 */
	public final class SizeBind {
		public static final int NONE = 0x000;
		public static final int MIN = 0x001;
		public static final int PREF = 0x010;
		public static final int MAX = 0x100;
		public static final int ALL = MIN | PREF | MAX;
	}

	/**
	 * Binds a node to its parent's boundaries, calling all anchor setters of {@link AnchorPane}.
	 * 
	 * @param node node to bind
	 * @see AnchorPane#setBottomAnchor(Node, Double)
	 * @see AnchorPane#setLeftAnchor(Node, Double)
	 * @see AnchorPane#setRightAnchor(Node, Double)
	 * @see AnchorPane#setTopAnchor(Node, Double)
	 */
	public static void bindToParent(Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
	}

	/**
	 * Binds the visibility and the management properties of a node to a boolean observable.
	 * 
	 * @param node  node to bind
	 * @param value boolean value to be bound to
	 */
	public static void bindVisibility(Node node, ObservableValue<? extends Boolean> value) {
		node.visibleProperty().bind(value);
		node.managedProperty().bind(value);
	}

	/**
	 * Takes a node and binds the requested sizes and dimensions to the given sizes. The result of the
	 * binding depends on the actual class or super class of the {@link Node}:
	 * 
	 * <table>
	 * <tr>
	 * <th>Region</th>
	 * <td>Can bind minimum, preferred and maximum sizes on any or both dimensions.</td>
	 * </tr>
	 * <tr>
	 * <th>Rectangle</th>
	 * <td>Can bind any or both dimensions, but the size doesn't matter.</td>
	 * </tr>
	 * </table>
	 * 
	 * @param node       Node to bind dimensions of
	 * @param size       Size to bind the node to
	 * @param dimensions Dimensions of the node to bind, from {@link DimensionBind} constants.
	 * @param sizes      Sizes of the node to bind, from {@link DimensionBind} constants.
	 */
	public static void bindDimensions(Node node, DoubleProperty size, int dimensions, int sizes) {
		if (node instanceof Region) {
			Region region = (Region) node;
			ifAllDo(() -> region.minHeightProperty().bind(size), sizes, SizeBind.MIN, dimensions,
			        DimensionBind.WIDTH);
			ifAllDo(() -> region.minWidthProperty().bind(size), sizes, SizeBind.MIN, dimensions,
			        DimensionBind.HEIGHT);
			ifAllDo(() -> region.prefHeightProperty().bind(size), sizes, SizeBind.PREF, dimensions,
			        DimensionBind.WIDTH);
			ifAllDo(() -> region.prefWidthProperty().bind(size), sizes, SizeBind.PREF, dimensions,
			        DimensionBind.HEIGHT);
			ifAllDo(() -> region.maxHeightProperty().bind(size), sizes, SizeBind.MAX, dimensions,
			        DimensionBind.WIDTH);
			ifAllDo(() -> region.maxWidthProperty().bind(size), sizes, SizeBind.MAX, dimensions,
			        DimensionBind.HEIGHT);
		} else if (node instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) node;
			ifAllDo(() -> rectangle.heightProperty().bind(size), sizes, dimensions, DimensionBind.HEIGHT);
			ifAllDo(() -> rectangle.widthProperty().bind(size), sizes, dimensions, DimensionBind.WIDTH);
		}
	}

	/**
	 * Shortcut for {@link #bindDimensions(Node, DoubleProperty, int, int)} on all size types.
	 * 
	 * @param node       Node to bind dimensions of
	 * @param size       Size to bind the node to
	 * @param dimensions Dimensions of the node to bind, from {@link DimensionBind} constants.
	 */
	public static void bindDimensions(Node node, DoubleProperty size, int dimensions) {
		bindDimensions(node, size, dimensions, SizeBind.ALL);
	}

	/**
	 * Shortcut for {@link #bindDimensions(Node, DoubleProperty, int, int)} on all sizes and dimensions
	 * if applicable.
	 * 
	 * @param node Node to bind dimensions of
	 * @param size Size to bind the node to
	 */
	public static void bindDimensions(Node node, DoubleProperty size) {
		bindDimensions(node, size, DimensionBind.ALL, SizeBind.ALL);
	}

	// Private constructor to prevent instantiation.
	private FXBindUtils() {
		// Nothing to see here
	}

}
