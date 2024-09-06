package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.richtext.InlineCssTextArea;

import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.BinoclesController;
import com.github.sylordis.binocles.ui.components.CommentBox;
import com.github.sylordis.binocles.ui.components.Controller;
import com.github.sylordis.binocles.ui.dialogs.CommentDetailsDialog;
import com.github.sylordis.binocles.ui.functional.CommentBoxComparator;
import com.github.sylordis.binocles.ui.javafxutils.StyleUtilsFX;
import com.github.sylordis.binocles.utils.StyleUtils;
import com.github.sylordis.binocles.utils.StyleUtils.CSSBlockStyle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * Pane component for a chapter.
 */
public class ChapterView extends BorderPane implements Initializable, BinoclesTabPane, Controller {

	/**
	 * Current chapter for this view.
	 */
	private Chapter chapter;
	/**
	 * Book the chapter belong's to.
	 */
	private Book book;
	/**
	 * Parent controller.
	 */
	private BinoclesController mainController;
	/**
	 * List of all comment boxes.
	 */
	private Set<CommentBox> commentBoxes;
	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();
	/**
	 * Text flow for the comment area when no comments are present.
	 */
	private TextFlow defaultCommentsFlow;

	/**
	 * Text area for chapter content.
	 */
	private InlineCssTextArea chapterContent;

	@FXML
	private Button toolbarCreateComment;
	@FXML
	private HBox chapterHeader;
	@FXML
	private Text chapterTitle;
	@FXML
	private Text bookTitle;
	@FXML
	private ScrollPane mainScrollPane;
	@FXML
	private HBox chapterContentContainer;
	@FXML
	private Region chapterContentSeparator;
	@FXML
	private VBox commentBoxContainer;
	@FXML
	private Button expandCollapseButton;

	/**
	 * Value of this boolean is modified when using the {@link #toggleCommentBoxesExpansion()}. It is
	 * true when all comments were expanded, false when all comments were collapsed.
	 */
	private SimpleBooleanProperty expandedCommentsState;

	/**
	 * Creates a new chapter view, loading the associated FXML.
	 * 
	 * @param mainController main controller to reflect on some changes
	 * @param book           book containing the chapter
	 * @param chapter        chapter under review for this view
	 */
	public ChapterView(BinoclesController mainController, Book book, Chapter chapter) {
		super();
		this.mainController = mainController;
		this.book = book;
		this.chapter = chapter;
		this.expandedCommentsState = new SimpleBooleanProperty(true);
		commentBoxes = new TreeSet<>(new CommentBoxComparator());
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chapter_view.fxml"));
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
		// Default
		Text defaultText = new Text("No comments for now.");
		defaultText.getStyleClass().addAll("text-note");
		defaultCommentsFlow = new TextFlow(defaultText);
		defaultCommentsFlow.setTextAlignment(TextAlignment.CENTER);
		// Titles
		chapterTitle.setText(chapter.getTitle());
		bookTitle.setText(book.getTitle());
		// Chapter content
		chapterContent = new InlineCssTextArea();
		chapterContent.setEditable(false);
		chapterContent.setWrapText(true);
		chapterContent.setAutoHeight(true);
		chapterContent.getStyleClass().addAll("main-text-area");
		chapterContent.replaceText(chapter.getText());
		// Add components and layout
		chapterContentContainer.getChildren().add(0, chapterContent);
		HBox.setHgrow(chapterContent, Priority.ALWAYS);
		// Comment boxes
		if (chapter.getComments().isEmpty())
			commentBoxContainer.getChildren().add(defaultCommentsFlow);
		else {
			chapter.getComments().forEach(this::createCommentBoxAndApplyStyle);
		}
		// Listeners
		chapterContent.selectionProperty()
		        .addListener((s, o, n) -> toolbarCreateComment.setDisable(n == null || n.getLength() == 0));
		chapterContent.addEventFilter(ScrollEvent.ANY, SE -> {
			ScrollEvent.fireEvent(chapterContentContainer, SE);
			SE.consume();
		});
		expandedCommentsState.addListener((s, o, n) -> expandCollapseButton
		        .setGraphic(AppIcons.createImageViewFromConfig(n ? AppIcons.ICON_COLLAPSE : AppIcons.ICON_EXPAND)));
	}

	@FXML
	public void createCommentAction(ActionEvent event) {
		IndexRange selectRange = chapterContent.getSelection();
		CommentDetailsDialog dialog = new CommentDetailsDialog(mainController.getModel(), book, chapter,
		        selectRange.getStart(), selectRange.getEnd());
		Optional<Comment> answer = dialog.display();
		if (answer.isPresent()) {
			if (commentBoxes.isEmpty())
				commentBoxContainer.getChildren().remove(defaultCommentsFlow);
			chapter.addComment(answer.get());
			logger.info("New comment on {}: {}", chapter, answer.get());
			createCommentBoxAndApplyStyle(answer.get());
		}
	}

	/**
	 * Applies the styles of a comment, related to his comment type, unto the text for visual feedback.
	 * 
	 * @param comment
	 */
	private void applyCommentStyleOnText(Comment comment) {
		Nomenclature defaultNomenclature = mainController.getModel().getDefaultNomenclature();
		CommentType type = comment.getType();
		// Take default nomenclature if none is set
		if (type == null) {
			logger.debug("Getting default nomenclature");
			type = defaultNomenclature.getTypes().get(0);
		}
		// Set text styles
		String stylesToString = StyleUtils.createCSSBlock(type.getStyles(), CSSBlockStyle.INLINE, StyleUtilsFX.JAVA_FX);
		logger.debug("Setting style: {}, style={}, string='{}'", comment, type.getStyles(), stylesToString);
		chapterContent.setStyle(comment.getStartIndex(), comment.getEndIndex(), stylesToString);
		expandedCommentsState.set(true);
	}

	/**
	 * Applies all comments styling to the text.
	 */
	public void applyAllCommentsStylesOnText() {
		this.chapter.getComments().forEach(this::applyCommentStyleOnText);
	}

	/**
	 * Resets the style of the whole text.
	 */
	public void clearTextStyle() {
		chapterContent.clearStyle(0, chapter.getText().length());
	}

	/**
	 * Creates a comment box in the comment panel and applies the style of said comment's type to the
	 * text.
	 * 
	 * @param comment
	 */
	private void createCommentBoxAndApplyStyle(Comment comment) {
		Nomenclature defaultNomenclature = mainController.getModel().getDefaultNomenclature();
		CommentBox cbox = new CommentBox(comment, defaultNomenclature);
		cbox.setParentController(this);
		commentBoxes.add(cbox);
		commentBoxContainer.getChildren().setAll(commentBoxes);
		applyCommentStyleOnText(comment);
		// TODO Set listeners for hovering and going away to draw and remove lines
	}

	/**
	 * Toggle the expansion and collapsing of all comments. Please be aware that when comments are
	 * created, the overall expansion state will be considered as expanded.
	 */
	@FXML
	public void toggleCommentBoxesExpansion() {
		expandedCommentsState.set(!expandedCommentsState.get());
		commentBoxes.forEach(c -> c.setExpanded(expandedCommentsState.get()));
	}

	@Override
	public Object getItem() {
		return chapter;
	}

	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * @return the chapter
	 */
	public Chapter getChapter() {
		return chapter;
	}

	/**
	 * Gets the text editor.
	 * 
	 * @return
	 */
	public InlineCssTextArea getChapterContentZone() {
		return chapterContent;
	}

	@Override
	public Controller getParentController() {
		return mainController;
	}

	/**
	 * Opens the comment details dialog in order to edit a comment.
	 * 
	 * @param comment
	 * @return
	 */
	public Optional<Comment> editCommentAction(Comment comment) {
		CommentDetailsDialog dialog = new CommentDetailsDialog(mainController.getModel(), book, chapter, comment);
		Optional<Comment> answer = dialog.display();
		if (answer.isPresent()) {
			comment.copy(answer.get());
			clearTextStyle();
			this.chapter.getComments().forEach(this::applyCommentStyleOnText);
		}
		return answer;
	}

	/**
	 * Starts a confirmation dialog to delete a given comment.
	 * 
	 * @param comment
	 */
	public void deleteCommentAction(Comment comment) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setContentText("Are you sure you want to delete this comment?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			deleteComment(comment);
			// Add empty message if no comments
			if (commentBoxes.isEmpty())
				commentBoxContainer.getChildren().add(defaultCommentsFlow);
		}
	}

	/**
	 * Deletes a comment from the comments of the chapter.
	 * 
	 * @param comment
	 */
	private void deleteComment(Comment comment) {
		this.chapter.getComments().remove(comment);
		CommentBox box = commentBoxes.stream().filter(b -> b.getComment().equals(comment)).findFirst().get();
		this.commentBoxes.remove(box);
		clearTextStyle();
		applyAllCommentsStylesOnText();
	}

	@Override
	public void setParentController(Controller parent) {
		mainController = (BinoclesController) parent;
	}

	@Override
	public void childNotify() {
		// Check if all boxes are expanded or collapsed and modify the property accordingly. Nothing happens
		// if not all comment boxes have the same expansion status.
		if (commentBoxes.stream().allMatch(c -> c.isExpanded())) {
			expandedCommentsState.set(true);
		} else if (commentBoxes.stream().allMatch(c -> !c.isExpanded())) {
			expandedCommentsState.set(false);
		}
	}

}
