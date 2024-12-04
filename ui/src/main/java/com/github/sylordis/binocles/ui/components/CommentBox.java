package com.github.sylordis.binocles.ui.components;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.views.ChapterView;

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
//	private int heightEllipsis;

	/**
	 * 
	 */
	private Controller parentController;
	/**
	 * Comment held by this box.
	 */
	private Comment comment;
	/**
	 * Default nomenclature if the comment has none.
	 */
	private Nomenclature defaultNomenclature;

	/**
	 * Local logger.
	 */
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
		// Title & get nomenclature fields for reference
		List<String> nomenclatureFields = new ArrayList<>();
		if (comment.getType() == null) {
			setTitle(defaultNomenclature.getTypes().get(0).getName());
			nomenclatureFields.addAll(defaultNomenclature.getTypes().get(0).getFields().keySet());
		} else {
			setTitle(comment.getType().getName());
			nomenclatureFields.addAll(comment.getType().getFields().keySet());
		}
		// Text content
		getMainContent().getChildren().clear();
		if (nomenclatureFields.size() == 1) {
			Text text = createTextField(comment.getFields().get(nomenclatureFields.get(0)));
			TextFlow flow = new TextFlow(text);
			flow.getStyleClass().add("comment-entry");
			getMainContent().getChildren().add(flow);
		} else {
			for (String field : nomenclatureFields) {
				Label label = new Label(field + ": ");
				Text textField = createTextField(comment.getFields().get(field));
				TextFlow flow = new TextFlow(label, textField);
				flow.getStyleClass().add("comment-entry");
				getMainContent().getChildren().add(flow);
			}
		}
		// Set comment box style
		applyCommentTypeStyle();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		ToolbarButton edit = new ToolbarButton(AppIcons.ICON_PENCIL, "Edit this comment");
		edit.setOnAction(e -> editAction());
		ToolbarButton delete = new ToolbarButton(AppIcons.ICON_TRASH, "Delete this comment");
		delete.setOnAction(e -> deleteAction());
		setToolbar(edit, delete);
	}

	/**
	 * Creates a text field with the provided text, or a note saying that the content is empty.
	 * 
	 * @param text
	 * @return
	 */
	private Text createTextField(String text) {
		Text textField = new Text();
		if (text == null || text.isBlank()) {
			textField.setText("<not provided>");
			textField.getStyleClass().addAll("text-note", "mixed-bg");
		} else
			textField.setText(text);
		return textField;
	}

	/**
	 * Gets a type's color CSS property.
	 * 
	 * @param type
	 * @return
	 */
	private String getTypeStyleColor(CommentType type) {
		return type.getStyles().get("color");
	}

	/**
	 * Sets the box style according to the comment type.
	 */
	private void applyCommentTypeStyle() {
		String colorHex = getTypeStyleColor(defaultNomenclature.getTypes().get(0));
		if (comment.getType() != null) {
			String commentColorHex = getTypeStyleColor(comment.getType());
			if (colorHex != null)
				colorHex = commentColorHex;
		}
		setBoxStyle(colorHex);
	}

	/**
	 * 
	 * @param colorHex
	 */
	private void setBoxStyle(String colorHex) {
		Color colour = Color.web(colorHex);
		Color mainBgColor = deriveMainBgColor(colour);
		Color titleBgColor = deriveTitleBgColor(colour);
		Color borderColor = deriveBorderColor(colour);
		logger.debug("Setting colours: type={}, main bg={}, title bg={}, border={}", colorHex, mainBgColor,
		        titleBgColor, borderColor);
		BackgroundFill bgfill = new BackgroundFill(mainBgColor, CornerRadii.EMPTY, this.getInsets());
		BackgroundFill titleBgFill = new BackgroundFill(titleBgColor, CornerRadii.EMPTY,
		        getTitleContainer().getInsets());
		setBackground(new Background(bgfill));
		setTitleBackground(new Background(titleBgFill));
		setBorder(new Border(
		        new BorderStroke(borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	}

	/**
	 * Sets a default style for the comment box.
	 */
	public void setDefaultBoxStyle() {
		setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		setTitleBackground(new Background(new BackgroundFill(Color.web("#DDD"), CornerRadii.EMPTY, Insets.EMPTY)));
		setBorder(new Border(new BorderStroke(Color.web("#dcdcdc"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
		        BorderWidths.DEFAULT)));
	}

	/**
	 * @param colour
	 * @return
	 */
	private Color deriveBorderColor(Color colour) {
		return colour.desaturate().desaturate().brighter();
	}

	/**
	 * @param colour
	 * @return
	 */
	private Color deriveTitleBgColor(Color colour) {
		return colour.desaturate().brighter();
	}

	/**
	 * @param colour
	 * @return
	 */
	private Color deriveMainBgColor(Color colour) {
		return deriveBorderColor(colour).brighter();
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

	@Override
	public void editAction() {
		Optional<Comment> answer = ((ChapterView) parentController).editCommentAction(this.comment);
		if (answer.isPresent()) {
			this.comment = answer.get();
			clearContent();
			updateContent();
		}
	}

	@Override
	public void deleteAction() {
		((ChapterView) parentController).deleteCommentAction(comment);
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
