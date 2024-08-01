package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.text.html.CSS;

import com.github.sylordis.binocles.ui.javafxutils.StyleUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	/**
	 * Inner Textflow's text.
	 */
	@FXML
	private Text fieldText;

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
		if (controlButtonColorPickerBackground.isSelected()) {
			fieldTextFlow
			        .setBackground(new Background(whiteFill, new BackgroundFill(controlColorPickerBackground.getValue(),
			                CornerRadii.EMPTY, fieldTextFlow.getInsets())));
		} else {
			fieldTextFlow.setBackground(new Background(whiteFill));
		}
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
			css.put(CSS.Attribute.TEXT_DECORATION, String.join("; ", values));
		}
		if (controlButtonColorPickerForeground.isSelected())
			css.put(CSS.Attribute.COLOR, StyleUtils.toHexString(controlColorPickerForeground.getValue()));
		if (controlButtonColorPickerBackground.isSelected())
			css.put(CSS.Attribute.BACKGROUND_COLOR, StyleUtils.toHexString(controlColorPickerBackground.getValue()));
		return css;
	}

}
