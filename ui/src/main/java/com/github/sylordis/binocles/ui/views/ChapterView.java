package com.github.sylordis.binocles.ui.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.ui.javafxutils.FXFormatUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Pane component for a chapter.
 */
public class ChapterView extends AnchorPane implements Initializable, BinoclesTabPane {

	private Chapter chapter;

	/**
	 * Text area for chapter content.
	 */
	private StyleClassedTextArea chapterContent;
	/**
	 * Scroll pane for chapter content.
	 */
	private VirtualizedScrollPane<StyleClassedTextArea> chapterContentScrollPane;

	@FXML
	private Text chapterTitle;
	@FXML
	private Text bookTitle;
	@FXML
	private AnchorPane chapterContentZone;
	@FXML
	private VBox chapterCommentBox;
	@FXML
	private VBox chapterZoneBox;

	public ChapterView(Chapter chapter) {
		super();
		this.chapter = chapter;
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
		// TODO book title
		chapterTitle.setText(chapter.getTitle());
		// Chapter content
		chapterContent = new StyleClassedTextArea();
		chapterContent.setEditable(false);
		chapterContent.setStyle(null);
		chapterContent.setWrapText(true);
		chapterContent.replaceText(chapter.getText());
		chapterContent.prefHeightProperty().bind(chapterZoneBox.heightProperty());
		// Chapter content scroll pane
		chapterContentScrollPane = new VirtualizedScrollPane<>(chapterContent);
		chapterContentScrollPane.setPadding(new Insets(5, 5, 5, 5));
		chapterContentScrollPane.setPickOnBounds(true);
		FXFormatUtils.bindToParent(chapterContentScrollPane);
		chapterContentZone.getChildren().add(chapterContentScrollPane);
	}

	@Override
	public Object getItem() {
		return chapter;
	}

}
