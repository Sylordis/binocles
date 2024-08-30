package com.github.sylordis.binocles.ui.components;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.views.ChapterView;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This component is the visual element destined to show comments and be collapsible. If the comment
 * is too long, it will display an ellipsis when collapsed.
 */
public class CommentBox extends CollapsibleBox implements Controller {

//	private final String DEFAULT_ELLIPSIS_STRING = "...";
//	private final int MAX_TEXTFLOW_HEIGHT = 100;

	private Controller parentController;

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
		super();
		this.comment = comment;
		this.defaultNomenclature = defaultNomenclature;
		updateContent();
	}

	@Override
	public void updateContent() {
		// Title
		if (comment.getType() == null)
			setTitle(defaultNomenclature.getTypes().get(0).getName());
		else {
			setTitle(comment.getType().getName());
		}
		// Text content
		if (comment.getFields().size() == 1) {
			Entry<String, String> entry = comment.getFields().entrySet().iterator().next();
			Text text = new Text(entry.getValue());
			TextFlow flow = new TextFlow(text);
			flow.getStyleClass().add("comment-entry");
			getMainContent().getChildren().add(flow);
		} else {
			for (Entry<String, String> field : comment.getFields().entrySet()) {
				Label label = new Label(field.getKey() + ": ");
				Text text = new Text(field.getValue());
				TextFlow flow = new TextFlow(label, text);
				flow.getStyleClass().add("comment-entry");
				getMainContent().getChildren().add(flow);
			}
		}
		// Set comment box style
		setBoxStyle();
	}

	/**
	 * Sets the box style according to the comment type.
	 */
	private void setBoxStyle() {
		if (comment.getType() != null) {
			String colourHex = comment.getType().getStyles().get("color");
			logger.debug("colour: {}", colourHex);
			if (colourHex != null) {
				Color colour = Color.web(colourHex);
				Color mainBgColor = colour.desaturate().desaturate().brighter().brighter();
				Color titleBgColor = colour.desaturate().brighter();
				Color borderColor = colour.desaturate().desaturate().brighter();
				BackgroundFill bgfill = new BackgroundFill(mainBgColor, CornerRadii.EMPTY, this.getInsets());
				BackgroundFill titleBgFill = new BackgroundFill(titleBgColor, CornerRadii.EMPTY,
				        getTitleContainer().getInsets());
				setBackground(new Background(bgfill));
				setTitleBackground(new Background(titleBgFill));
				setBorder(new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				        BorderWidths.DEFAULT)));
			} else
				setDefaultBoxStyle();
		} else
			setDefaultBoxStyle();
	}

	/**
	 * Sets a default style for the comment box.
	 */
	private void setDefaultBoxStyle() {
		setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		setTitleBackground(new Background(new BackgroundFill(Color.web("#DDD"), CornerRadii.EMPTY, Insets.EMPTY)));
		setBorder(new Border(new BorderStroke(Color.web("#dcdcdc"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
		        BorderWidths.DEFAULT)));
	}

	@Override
	public void toggleExpanded() {
		super.toggleExpanded();
		parentController.childNotify();
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

	@FXML
	@Override
	public void editAction() {
		((ChapterView) parentController).editComment(comment);
		clearContent();
		updateContent();
	}

	@FXML
	@Override
	public void deleteAction() {
		((ChapterView) parentController).deleteComment(comment);
	}

	@Override
	public Controller getParentController() {
		return this.parentController;
	}

	@Override
	public void setParentController(Controller parent) {
		this.parentController = parent;
	}

}
