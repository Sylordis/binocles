package com.github.sylordis.binocles.ui.components;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.ui.javafxutils.FXBindUtils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * This class allows to display colour boxes for either one or two colours, with a foreground and
 * background one.<br/>
 * <br/>
 * The former will be displayed on top of the latter, but the full box will display only one colour
 * if only one is set.<br/>
 * <br/>
 * If you need to render two different colours side by side, just use two {@link ColorPreviewBox}
 * side by side with each one colour.
 */
public class ColorPreviewBox extends StackPane implements Initializable {

	private static float REDUCTION_IF_MULTIPLE = 0.75f;
	private final Logger logger = LogManager.getLogger();

	/**
	 * Size property of the box on all sides (min, pref and max).
	 */
	private DoubleProperty size;
	/**
	 * Dynamic property for rectangle sizes.
	 */
	private DoubleProperty rectangleSize;
	/**
	 * Paint property for the foreground box.
	 */
	private ObjectProperty<Paint> foregroundColor;
	/**
	 * Paint property for the background colour.
	 */
	private ObjectProperty<Paint> backgroundColor;

	/**
	 * UI component for foreground colour.
	 */
	private Rectangle foregroundRectangle;
	/**
	 * UI component for background colour.
	 */
	private Rectangle backgroundRectangle;

	/**
	 * Builds a colour preview box with no set colours to preview.
	 * 
	 * @param size maximum size of the box
	 */
	public ColorPreviewBox(double size) {
		this(size, null, null);
	}

	/**
	 * Builds a colour preview box with only one colour to preview.
	 * 
	 * @param size  maximum size of the box
	 * @param color colour to preview
	 */
	public ColorPreviewBox(double size, Paint color) {
		this(size, color, null);
	}

	/**
	 * Builds a colour preview box with two colours to preview, a foreground and a background one.
	 * 
	 * @param size       maximum size of the box
	 * @param foreground colour to preview in the foreground (can be null)
	 * @param background colour to preview in the background (can be null)
	 */
	public ColorPreviewBox(double size, Paint foreground, Paint background) {
		super();
		this.size = new SimpleDoubleProperty(size);
		this.rectangleSize = new SimpleDoubleProperty(size);
		this.foregroundColor = new SimpleObjectProperty<Paint>();
		this.backgroundColor = new SimpleObjectProperty<Paint>();
		this.foregroundColor.set(foreground);
		this.backgroundColor.set(background);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXBindUtils.bindDimensions(this, size);
		this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		foregroundRectangle = new Rectangle();
		backgroundRectangle = new Rectangle();
		foregroundRectangle.fillProperty().bind(foregroundColor.orElse(Color.TRANSPARENT));
		backgroundRectangle.fillProperty().bind(backgroundColor.orElse(Color.TRANSPARENT));
		FXBindUtils.bindDimensions(foregroundRectangle, rectangleSize);
		FXBindUtils.bindDimensions(backgroundRectangle, rectangleSize);
		rectangleSize.bind(
		        Bindings.createDoubleBinding(this::calculateNewRectangleSize, foregroundColor, backgroundColor, size));
		backgroundRectangle.xProperty().bind(size.subtract(rectangleSize));
		backgroundRectangle.yProperty().bind(size.subtract(rectangleSize));
		this.getChildren().addAll(foregroundRectangle, backgroundRectangle);
		// Debug
		foregroundRectangle.widthProperty()
		        .addListener((o, c, n) -> logger.debug("fg: {}x{} on ({},{})", foregroundRectangle.getWidth(),
		                foregroundRectangle.getHeight(), foregroundRectangle.getX(), foregroundRectangle.getY()));
		backgroundRectangle.yProperty()
		        .addListener((o, c, n) -> logger.debug("bg: {}x{} on ({},{})", backgroundRectangle.getWidth(),
		                backgroundRectangle.getHeight(), backgroundRectangle.getX(), backgroundRectangle.getY()));
	}

	private double calculateNewRectangleSize() {
		return size.get()
		        * (foregroundColor.isNotNull().get() && backgroundColor.isNotNull().get() ? REDUCTION_IF_MULTIPLE
		                : 1.0f);
	}

	/**
	 * @return the size
	 */
	public double getSize() {
		return size.get();
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size.set(size);
	}

	/**
	 * 
	 * @return the size property
	 */
	public DoubleProperty sizeProperty() {
		return size;
	}

	/**
	 * @return the foregroundColor
	 */
	public Paint getForegroundColor() {
		return foregroundColor.get();
	}

	/**
	 * @param foregroundColor the foregroundColor to set
	 */
	public void setForegroundColor(Paint foregroundColor) {
		this.foregroundColor.set(foregroundColor);
	}

	/**
	 * 
	 * @return the foregroundColor property
	 */
	public ObjectProperty<Paint> foregroundColorProperty() {
		return this.foregroundColor;
	}

	/**
	 * @return the backgroundColor
	 */
	public Paint getBackgroundColor() {
		return backgroundColor.get();
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Paint backgroundColor) {
		this.backgroundColor.set(backgroundColor);
	}

	/**
	 * 
	 * @return the backgroundColor property
	 */
	public ObjectProperty<Paint> backgroundColorProperty() {
		return this.backgroundColor;
	}

}
