package com.github.sylordis.binocles.ui.components;

import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.AppIcons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This component is the visual element destined to show comments and be collapsible. If the comment
 * is too long, it will display an ellipsis when collapsed.
 */
public class CommentBox extends VBox implements Initializable {

//	private final String DEFAULT_ELLIPSIS_STRING = "...";
//	private final int MAX_TEXTFLOW_HEIGHT = 100;

	@FXML
	private Button buttonCollapse;
	@FXML
	private Text boxTitle;
	@FXML
	private HBox toolbar;
	@FXML
	private VBox mainContent;

	private BooleanProperty expanded;

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
		this.expanded = new SimpleBooleanProperty(true);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("comment_box2.fxml"));
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
			boxTitle.setText(defaultNomenclature.getTypes().get(0).getName());
		else {
			boxTitle.setText(comment.getType().getName());
		}
		if (comment.getFields().size() == 1) {
			Entry<String, String> entry = comment.getFields().entrySet().iterator().next();
			Text text = new Text(entry.getValue());
			TextFlow flow = new TextFlow(text);
			flow.getStyleClass().add("comment-entry");
			mainContent.getChildren().add(flow);
		} else {
			for (Entry<String, String> field : comment.getFields().entrySet()) {
				Label label = new Label(field.getKey() + ": ");
				Text text = new Text(field.getValue());
				TextFlow flow = new TextFlow(label, text);
				flow.getStyleClass().add("comment-entry");
				mainContent.getChildren().add(flow);
			}
		}
		// Set listeners
		expanded.addListener((s, o, n) -> {
			mainContent.setVisible(n);
			buttonCollapse.setGraphic(AppIcons.createImageView(n ? AppIcons.ICON_ARROW_DOWN : AppIcons.ICON_ARROW_RIGHT, 8, 8));
		});
//		mainPane.setOpaqueInsets(new Insets(0, 0, 5, 0));
		// TODO Set comment box style
//		if (comment.getType() != null) {
//			String colourHex = comment.getType().getStyles().get("color");
//			logger.debug("colour: {}", colourHex);
//			if (colourHex != null) {
//				Color colour = Color.web(colourHex).deriveColor(1.0, 1.0, 1.9, 1.0);
//				BackgroundFill bgfill = new BackgroundFill(colour, CornerRadii.EMPTY, mainPane.getInsets());
//				// TODO THE FUCK WITH THE MARGIN
//				setBackground(new Background(bgfill));
//				backgroundProperty().set(new Background(bgfill));
//				mainPane.setBackground(new Background(bgfill));
//				mainPane.backgroundProperty().set(new Background(bgfill));
//			}
//		}
//		final Set<Node> node = this.lookupAll(".title");
//		logger.debug("Setting comment box style: nodes = {}", node.size());
//		node.forEach(n -> n.setStyle("-fx-background-color: #99FF99;"));
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

	/**
	 * Sets the expanded status of this comment box.
	 * 
	 * @param expanded
	 */
	public void setExpanded(boolean expanded) {
		this.expanded.set(expanded);
	}

	/**
	 * Toggles the expansion status.
	 */
	@FXML
	public void toggleExpanded() {
		this.expanded.set(!this.expanded.getValue());
	}

}
