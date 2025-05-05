package com.github.sylordis.binocles.ui.wizards;


import com.github.sylordis.binocles.model.BinoclesModel;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

class RenderExportRenderStylePane extends AbstractWizardPane {

	private RadioButton buttonStyle1;
	private RadioButton buttonStyle2;
	
	public RenderExportRenderStylePane(BinoclesModel model) {
		super(model);
	}

	@Override
	protected void build() {
		Label labelStyles = new Label("Choose a render style:");
		ToggleGroup radioGroup = new ToggleGroup();
		buttonStyle1 = new RadioButton("Comments after each paragraph");
		buttonStyle1.setToggleGroup(radioGroup);
		buttonStyle2 = new RadioButton("Comments at the end");
		buttonStyle2.setToggleGroup(radioGroup);
		VBox radioContainerPane = new VBox();
		radioContainerPane.getChildren().addAll(buttonStyle1, buttonStyle2);
		// Add to grid
		int row = 0;
		getGridPane().addRow(row, labelStyles);
		getGridPane().addRow(++row, radioContainerPane);
	}
	
}