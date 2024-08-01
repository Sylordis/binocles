package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.Nomenclature;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This component is the visual element destined to show comments and be collapsible. If the comment is too long, it
 * will display an ellipsis when collapsed.
 */
public class CommentBox extends TitledPane implements Initializable {

	@FXML
	private VBox mainPane;
//	private final String DEFAULT_ELLIPSIS_STRING = "...";
//	private final int MAX_TEXTFLOW_HEIGHT = 100;

	private Comment comment;
	private Nomenclature defaultNomenclature;
//	private int heightEllipsis;

	private final Logger logger = LogManager.getLogger();

	/**
	 * Creates a new Comment box, providing a default nomenclature in case the comment doesn't have any.
	 * 
	 * @param comment
	 * @param defaultNomenclature
	 */
	public CommentBox(Comment comment, Nomenclature defaultNomenclature) {
		this.comment = comment;
		this.defaultNomenclature = defaultNomenclature;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("comment_box.fxml"));
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
		if (comment.getType() == null)
			this.setText(defaultNomenclature.getTypes().get(0).getName());
		else
			this.setText(comment.getType().getName());
		if (comment.getFields().size() == 1) {
			Entry<String, String> entry = comment.getFields().entrySet().iterator().next();
			Text text = new Text(entry.getValue());
			TextFlow flow = new TextFlow(text);
			flow.getStyleClass().add("comment-entry");
			mainPane.getChildren().add(flow);
		} else {
			for (Entry<String, String> field : comment.getFields().entrySet()) {
				Label label = new Label(field.getKey() + ": ");
				Text text = new Text(field.getValue());
				TextFlow flow = new TextFlow(label, text);
				flow.getStyleClass().add("comment-entry");
				mainPane.getChildren().add(flow);
			}
		}
		// Set comment box style
		final Set<Node> node = this.lookupAll(".title");
		logger.debug("Setting comment box style: nodes = {}", node.size());
		node.forEach(n -> n.setStyle("-fx-background-color: #99FF99;"));
		// TODO set max height with ellipsis
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

	/**
	 * @return the defaultNomenclature
	 */
	public Nomenclature getDefaultNomenclature() {
		return defaultNomenclature;
	}

	/**
	 * @param defaultNomenclature the defaultNomenclature to set
	 */
	public void setDefaultNomenclature(Nomenclature defaultNomenclature) {
		this.defaultNomenclature = defaultNomenclature;
	}

}
