package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.sylordis.binocles.model.review.Comment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class CommentBox extends TitledPane implements Initializable {

	@FXML
	private AnchorPane mainPane;
	
	private Comment comment;

	public CommentBox(Comment comment) {
		this.comment = comment;
		this.setText("My Comment");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("comment_box.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * @return the comment
	 */
	public Comment getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(Comment comment) {
		this.comment = comment;
	}

}
