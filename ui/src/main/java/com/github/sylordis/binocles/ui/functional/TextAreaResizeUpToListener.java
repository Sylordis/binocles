package com.github.sylordis.binocles.ui.functional;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class TextAreaResizeUpToListener implements ChangeListener<String> {

	public static final int MAX_HEIGHT_INFINITE = -1;
	SimpleIntegerProperty count = new SimpleIntegerProperty(5);

	private TextArea textArea;
	private int maxHeight;

	public TextAreaResizeUpToListener(TextArea textArea) {
		this(textArea, MAX_HEIGHT_INFINITE);
	}

	public TextAreaResizeUpToListener(TextArea textArea, int maxHeight) {
		this.textArea = textArea;
		this.maxHeight = maxHeight;
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		textArea.setPrefHeight(textArea.lookup(".text").getLayoutBounds().getHeight());
//		textArea.setPrefHeight(textArea.lookup(".text").getLayoutBounds().getHeight());
//		if (newValue != null) {
//			textArea.applyCss();
//            Node text = textArea.lookup(".text");
//            textArea.prefHeightProperty().bind(new DoubleBinding() {
//				
//				@Override
//				protected double computeValue() {
//					return 2+text.getBoundsInLocal().getHeight();
//				}
//			});
//		}
	}
}
