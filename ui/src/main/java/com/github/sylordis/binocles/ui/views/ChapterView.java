package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.richtext.StyleClassedTextArea;

import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.ui.BinoclesController;
import com.github.sylordis.binocles.ui.components.CommentBox;
import com.github.sylordis.binocles.ui.dialogs.CommentDetailsDialog;
import com.github.sylordis.binocles.ui.functional.CommentBoxComparator;
import com.github.sylordis.binocles.ui.javafxutils.StyleUtils;
import com.github.sylordis.binocles.ui.javafxutils.StyleUtils.CSSType;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ScrollPane;
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
public class ChapterView extends BorderPane implements Initializable, BinoclesTabPane {

	private Chapter chapter;
	private Book book;
	private BinoclesController mainController;
	private Set<CommentBox> commentBoxes;
	private final Logger logger = LogManager.getLogger();
	private TextFlow defaultFlow;

	/**
	 * Text area for chapter content.
	 */
	private StyleClassedTextArea chapterContent;
	/**
	 * Scroll pane for chapter content.
	 */
//	private VirtualizedScrollPane<StyleClassedTextArea> chapterContentScrollPane;

	private Comparator<Node> commentBoxComparator;

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

	public ChapterView(BinoclesController mainController, Book book, Chapter chapter) {
		super();
		this.mainController = mainController;
		this.book = book;
		this.chapter = chapter;
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
		defaultFlow = new TextFlow(defaultText);
		defaultFlow.setTextAlignment(TextAlignment.CENTER);
		// Titles
		chapterTitle.setText(chapter.getTitle());
		bookTitle.setText(book.getTitle());
		// Chapter content
		chapterContent = new StyleClassedTextArea();
		chapterContent.setEditable(false);
		chapterContent.setStyle(null);
		chapterContent.setWrapText(true);
		chapterContent.setAutoHeight(true);
		chapterContent.setStyleClass(0, chapter.getText().length(), "style-justified");
		chapterContent.getStyleClass().addAll("main-text-area");
		chapterContent.replaceText(chapter.getText());
		// Add components and layout
		chapterContentContainer.getChildren().add(0, chapterContent);
		HBox.setHgrow(chapterContent, Priority.ALWAYS);
		// Comment boxes
		if (chapter.getComments().isEmpty())
			commentBoxContainer.getChildren().add(defaultFlow);
		else {
			chapter.getComments().forEach(this::createCommentBox);
		}
		// Listeners
		chapterContent.selectionProperty()
		        .addListener((s, o, n) -> toolbarCreateComment.setDisable(n == null || n.getLength() == 0));
		chapterContent.addEventFilter(ScrollEvent.ANY, SE -> {
			ScrollEvent.fireEvent(chapterContentContainer, SE);
			SE.consume();
		});
	}

	@FXML
	public void createCommentAction(ActionEvent event) {
		IndexRange selectRange = chapterContent.getSelection();
		CommentDetailsDialog dialog = new CommentDetailsDialog(mainController.getModel(), book, chapter,
		        selectRange.getStart(), selectRange.getEnd());
		Optional<Comment> answer = dialog.display();
		if (answer.isPresent()) {
			if (commentBoxes.isEmpty())
				commentBoxContainer.getChildren().remove(defaultFlow);
			chapter.addComment(answer.get());
			logger.info("New comment on {}: {}", chapter, answer.get());
			createCommentBox(answer.get());
			// TODO Apply comment style on text
			logger.debug("Comment boxes: {}", commentBoxContainer.getChildren().size());
		}
	}

	/**
	 * Creates a comment box in the comment panel, applies the style of said comment's type to the text.
	 * 
	 * @param comment
	 */
	private void createCommentBox(Comment comment) {
		Nomenclature defaultNomenclature = mainController.getModel().getDefaultNomenclature();
		CommentBox cbox = new CommentBox(comment, defaultNomenclature);
		commentBoxes.add(cbox);
		commentBoxContainer.getChildren().add(cbox);
		CommentType type = comment.getType();
		if (type == null)
			type = defaultNomenclature.getTypes().get(0);
		Collection<String> styles = type.getStyles().entrySet().stream()
		        .map(e -> StyleUtils.convertCSStoTypeStyle(e.getKey(), e.getValue(), CSSType.RichTextFX))
		        .collect(Collectors.toList());
		logger.debug("Setting style: {}, style={}", comment, styles);
		chapterContent.setStyle(comment.getStartIndex(), comment.getEndIndex(), styles);
		// TODO Set text styles
		// TODO Set listeners for hovering and going away to draw and remove lines
	}

	private void sortCommentBoxes() {
		ObservableList<Node> boxes = FXCollections.observableArrayList(commentBoxContainer.getChildren());
		FXCollections.sort(boxes, commentBoxComparator);
		commentBoxContainer.getChildren().clear();
		commentBoxContainer.getChildren().addAll(boxes);
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
	public StyleClassedTextArea getChapterContentZone() {
		return chapterContent;
	}

}
