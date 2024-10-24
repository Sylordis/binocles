package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.html.CSS;

import com.github.sylordis.binocles.ui.javafxutils.StyleUtilsFX;
import com.github.sylordis.binocles.utils.MapUtils;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * UI component that acts as style editor as a read-only text editor to setup styling of a text,
 * displaying the result in a text element.
 */
public class StyleEditor extends VBox implements Initializable {

	@FXML
	private ToggleButton controlButtonBold;
	@FXML
	private ToggleButton controlButtonItalic;
	@FXML
	private ToggleButton controlButtonUnderline;
	@FXML
	private ToggleButton controlButtonStrikethrough;
	@FXML
	private ToggleButton controlButtonColorPickerBackground;
	@FXML
	private ColorPicker controlColorPickerBackground;
	@FXML
	private ToggleButton controlButtonColorPickerForeground;
	@FXML
	private ColorPicker controlColorPickerForeground;
	@FXML
	private TextFlow fieldTextFlow;
	@FXML
	private ToolBar toolbar;
	/**
	 * Inner Textflow's text.
	 */
	@FXML
	private Text fieldText;

	/**
	 * Constructs a new style editor.
	 */
	public StyleEditor() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("style_editor.fxml"));
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
		ColorPreviewBox box = new ColorPreviewBox(18);
		box.initialize(location, resources);
		box.foregroundColorProperty().bind(Bindings.createObjectBinding(
		        () -> getColorIfSelected(controlColorPickerForeground, controlButtonColorPickerForeground),
		        controlButtonColorPickerForeground.selectedProperty(), controlColorPickerForeground.valueProperty()));
		box.backgroundColorProperty().bind(Bindings.createObjectBinding(
		        () -> getColorIfSelected(controlColorPickerBackground, controlButtonColorPickerBackground),
		        controlButtonColorPickerBackground.selectedProperty(), controlColorPickerBackground.valueProperty()));
		toolbar.getItems().add(box);
	}

	private Paint getColorIfSelected(ColorPicker picker, ToggleButton selector) {
		Paint paint = null;
		if (selector.isSelected()) {
			paint = picker.valueProperty().orElse(null).getValue();
		}
		return paint;
	}
	
	/**
	 * Sets the content of the style editor, e.g. the text displayed.
	 * 
	 * @param content
	 */
	public void setText(String content) {
		this.fieldText.setText(content);
	}

	@FXML
	public void updateTextStyle() {
		Font currentFont = fieldText.getFont();
		FontWeight fontWeight = controlButtonBold.isSelected() ? FontWeight.BOLD : FontWeight.NORMAL;
		FontPosture fontPosture = controlButtonItalic.isSelected() ? FontPosture.ITALIC : FontPosture.REGULAR;
		fieldText.setFont(Font.font(currentFont.getFamily(), fontWeight, fontPosture, currentFont.getSize()));
		fieldText.setUnderline(controlButtonUnderline.isSelected());
		fieldText.setStrikethrough(controlButtonStrikethrough.isSelected());
		fieldText.setFill(controlButtonColorPickerForeground.isSelected() ? controlColorPickerForeground.getValue()
		        : Color.BLACK);
		BackgroundFill whiteFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, new Insets(0, 0, 0, 0));
		// TODO set background only on text
		if (controlButtonColorPickerBackground.isSelected()) {
			fieldTextFlow
			        .setBackground(new Background(whiteFill, new BackgroundFill(controlColorPickerBackground.getValue(),
			                CornerRadii.EMPTY, fieldTextFlow.getInsets())));
		} else {
			fieldTextFlow.setBackground(new Background(whiteFill));
		}
	}

	/**
	 * Sets the current state of the style editor.
	 * 
	 * @param styles list of styles with string keys
	 */
	public void loadStringStyles(Map<String, String> styles) {
		controlButtonBold.setSelected(hasStyleAndValue(styles, CSS.Attribute.FONT_WEIGHT, "bold"));
		controlButtonItalic.setSelected(hasStyleAndValue(styles, CSS.Attribute.FONT_STYLE, "italic"));
		controlButtonUnderline.setSelected(hasStyleAndValue(styles, CSS.Attribute.TEXT_DECORATION, "underline"));
		controlButtonStrikethrough.setSelected(hasStyleAndValue(styles, CSS.Attribute.TEXT_DECORATION, "line-through"));
		controlButtonColorPickerForeground.setSelected(hasStyleAndValue(styles, CSS.Attribute.COLOR, null));
		if (hasStyleAndValue(styles, CSS.Attribute.COLOR, null)) {
			controlColorPickerForeground.setValue(Color.web(styles.get(CSS.Attribute.COLOR.toString())));
		}
		controlButtonColorPickerBackground.setSelected(hasStyleAndValue(styles, CSS.Attribute.BACKGROUND_COLOR, null));
		if (hasStyleAndValue(styles, CSS.Attribute.BACKGROUND_COLOR, null)) {
			controlColorPickerBackground.setValue(Color.web(styles.get(CSS.Attribute.BACKGROUND_COLOR.toString())));
		}
		updateTextStyle();
	}

	/**
	 * Sets the current state of the style editor.
	 * 
	 * @param styles list of styles
	 */
	public void loadStyles(Map<CSS.Attribute, String> styles) {
		loadStringStyles(styles.entrySet().stream().collect(Collectors.toMap(Object::toString, Map.Entry::getValue)));
	}

	/**
	 * Checks if given map has a given CSS attribute and if it matches a value.
	 * 
	 * @param styles map of styles
	 * @param attr   CSS attribute to look for in keys
	 * @param value  value to match if it is contained in the actual value, provide null if value has
	 *               not to be matched
	 * @return true if the map contains the desired key and the searched value is null or is contained
	 *         in the entry
	 */
	private boolean hasStyleAndValue(Map<String, String> styles, CSS.Attribute attr, String value) {
		return MapUtils.containsKeyAndValue(styles, attr, value, Object::toString, String::contains);
	}

	/**
	 * Gets the style configured in this editor in a dictionary of CSS properties and their respective
	 * values. Properties not set or defaulting will not be present in the dictionary.
	 * 
	 * @return a dictionary of non-default CSS properties and their values
	 */
	public Map<CSS.Attribute, String> getCSSStyles() {
		Map<CSS.Attribute, String> css = new HashMap<>();
		if (controlButtonBold.isSelected())
			css.put(CSS.Attribute.FONT_WEIGHT, "bold");
		if (controlButtonItalic.isSelected())
			css.put(CSS.Attribute.FONT_STYLE, "italic");
		if (controlButtonUnderline.isSelected() || controlButtonStrikethrough.isSelected()) {
			Set<String> values = new HashSet<>();
			if (controlButtonUnderline.isSelected())
				values.add("underline");
			if (controlButtonStrikethrough.isSelected())
				values.add("line-through");
			css.put(CSS.Attribute.TEXT_DECORATION, String.join(" ", values));
		}
		if (controlButtonColorPickerForeground.isSelected())
			css.put(CSS.Attribute.COLOR, StyleUtilsFX.toHexString(controlColorPickerForeground.getValue()));
		if (controlButtonColorPickerBackground.isSelected())
			css.put(CSS.Attribute.BACKGROUND_COLOR, StyleUtilsFX.toHexString(controlColorPickerBackground.getValue()));
		return css;
	}

}
