package com.github.sylordis.binocle.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.sylordis.binocle.model.review.ReviewableContent;
import com.github.sylordis.binocle.ui.components.ReviewTreeRoot;
import com.github.sylordis.binocle.ui.dialogs.NewBookDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Controller for the application.
 * 
 * @author sylordis
 *
 */
public class BinocleController implements Initializable {

	@FXML
	private TreeView<ReviewableContent> reviewTree;

	@FXML
	public void createNewBookAction(ActionEvent event) {
		NewBookDialog dialog = new NewBookDialog();
		dialog.build();
		dialog.launch();
//		if (title != null && !title.isEmpty()) {
//			TreeItem<ReviewableContent> book = new TreeItem<>(new Book(title), AppIcons.createImageViewFromConfig(AppIcons.ICON_BOOK));
//			book.setExpanded(true);
//			reviewTree.getRoot().getChildren().add(book);
//		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Main ");
		TreeItem<ReviewableContent> treeRoot = new TreeItem<>(new ReviewTreeRoot());
		reviewTree.setRoot(treeRoot);
		reviewTree.setShowRoot(false);
	}

}
