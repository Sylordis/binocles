package com.github.sylordis.binocles.ui.wizards;

import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Wizard<R> {

	private Dialog<R> dialog;
	private StackPane stackPane;

	public Wizard() {
		dialog = new Dialog<>();
	}

	public Optional<R> showAndWait() {
		return dialog.showAndWait();
	}

	public void setOwner(Object o) {
		Window window = null;
		if (o instanceof Window) {
			window = (Window) o;
		} else if (o instanceof Node) {
			window = ((Node) o).getScene().getWindow();
		}

		dialog.initOwner(window);
	}

	public Dialog<R> getDialog() {
		return dialog;
	}

	public void setDialog(Dialog<R> dialog) {
		this.dialog = dialog;
	}

	public void setIcon(Image image) {
		Stage stage = (Stage) this.dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(image);
	}
}
