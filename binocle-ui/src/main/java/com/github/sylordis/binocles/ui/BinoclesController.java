package com.github.sylordis.binocle.ui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocle.model.BinocleModel;
import com.github.sylordis.binocle.model.exceptions.UniqueNameException;
import com.github.sylordis.binocle.model.review.Nomenclature;
import com.github.sylordis.binocle.model.review.ReviewableContent;
import com.github.sylordis.binocle.model.text.Book;
import com.github.sylordis.binocle.model.text.Chapter;
import com.github.sylordis.binocle.ui.answers.CreateBookAnswer;
import com.github.sylordis.binocle.ui.answers.CreateChapterAnswer;
import com.github.sylordis.binocle.ui.components.NomenclatureTreeRoot;
import com.github.sylordis.binocle.ui.components.ReviewTreeRoot;
import com.github.sylordis.binocle.ui.dialogs.CreateBookDialog;
import com.github.sylordis.binocle.ui.dialogs.CreateChapterDialog;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
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
	private TreeView<Nomenclature> nomenclatureTree;

	@FXML
	private MenuItem menuFileNew;
	@FXML
	private MenuItem menuFileOpen;
	@FXML
	private MenuItem menuFileSave;
	@FXML
	private MenuItem menuFileSaveAs;
	@FXML
	private MenuItem menuFileExportStructural;
	@FXML
	private MenuItem menuFileExportRender;
	@FXML
	private MenuItem menuFileQuit;

	@FXML
	private MenuItem menuReviewNewBook;
	@FXML
	private MenuItem menuReviewNewChapter;

	@FXML
	private Button navOpen;
	@FXML
	private Button navSave;
	@FXML
	private Button navSaveAs;
	@FXML
	private Button navExportStruct;
	@FXML
	private Button navExportRender;
	@FXML
	private Button navCreateBook;
	@FXML
	private Button navCreateChapter;

	@FXML
	private TextFlow textZoneChapterContent;
	@FXML
	private Text textZoneChapterTitle;

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Current model managed by the controller.
	 */
	private BinocleModel model;

	@FXML
	public void menuQuitAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Are you sure you want to quit?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
			Platform.exit();
	}

	@FXML
	public void createBookAction(ActionEvent event) {
		CreateBookDialog dialog = new CreateBookDialog(model);
		Optional<CreateBookAnswer> answer = dialog.display();
		// Check answer is valid
		if (answer.isPresent()) {
			String title = answer.get().title();
			try {
				Book book = new Book(title);
				// Set nomenclature if present
				if (answer.get().nomenclature().isPresent()) {
					Nomenclature nom = answer.get().nomenclature().get();
					book.setNomenclature(nom);
				}
				model.addBook(book);
				TreeItem<ReviewableContent> bookItem = new TreeItem<>(book,
				        AppIcons.createImageViewFromConfig(AppIcons.ICON_BOOK));
				bookItem.setExpanded(true);
				reviewTree.getRoot().getChildren().add(bookItem);
				reviewTree.getRoot().getChildren()
				        .sort((s1, s2) -> s1.getValue().getId().compareTo(s2.getValue().getId()));
			} catch (UniqueNameException e) {
				logger.error(e);
			}
		}
	}

	@FXML
	public void createChapterAction(ActionEvent event) {
		// Get book first
		TreeItem<ReviewableContent> treeSelected = reviewTree.getSelectionModel().getSelectedItem();
		if (null != treeSelected) {
			TreeItem<ReviewableContent> treeBookParent = Book.class.equals(treeSelected.getValue().getClass())
			        ? treeSelected
			        : treeSelected.getParent();
			Book currentBook = (Book) treeBookParent.getValue();
			CreateChapterDialog dialog = new CreateChapterDialog(model, currentBook);
			Optional<CreateChapterAnswer> answer = dialog.display();
			if (answer.isPresent()) {
				logger.info("Created a chapter '{}'", answer.get().title());
				Chapter chapter = new Chapter(answer.get().title(), answer.get().content());
				currentBook.addChapter(chapter);
				TreeItem<ReviewableContent> chapterTreeItem = new TreeItem<>(chapter);
				treeBookParent.getChildren().add(chapterTreeItem);
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.debug("Controller init");
		// Initialise model
		model = new BinocleModel();
		// Initialise the tree for reviews
		TreeItem<ReviewableContent> reviewsTreeRoot = new TreeItem<>(new ReviewTreeRoot());
		reviewTree.setRoot(reviewsTreeRoot);
		reviewTree.setShowRoot(false);
		// Initialise the tree for nomenclatures
		TreeItem<Nomenclature> nomenclaturesTreeRoot = new TreeItem<>(new NomenclatureTreeRoot());
		nomenclatureTree.setRoot(nomenclaturesTreeRoot);
		nomenclatureTree.setShowRoot(false);
		// Set trees change listener
		// TODO Tree item renderers
		reviewTree.getSelectionModel().selectedItemProperty()
		        .addListener(new ChangeListener<TreeItem<ReviewableContent>>() {

			        @Override
			        public void changed(ObservableValue<? extends TreeItem<ReviewableContent>> observable,
			                TreeItem<ReviewableContent> oldValue, TreeItem<ReviewableContent> newValue) {
				        logger.debug("Tree selected: old {} / new {} {}", oldValue,
				                null != newValue ? newValue.getValue().getClass() : "none", newValue);
				        menuReviewNewChapter.setDisable(null == newValue);
				        navCreateChapter.setDisable(null == newValue);
				        setChapterContent(newValue);
			        }

			        public void setChapterContent(TreeItem<ReviewableContent> newValue) {
				        if (Chapter.class.equals(newValue.getValue().getClass())) {
					        Chapter chapter = (Chapter) newValue.getValue();
					        textZoneChapterTitle.setText(chapter.getTitle());
					        textZoneChapterContent.getChildren().setAll(new Text(chapter.getText()));
				        } else if (Book.class.equals(newValue.getValue().getClass())) {
				        	Book book = (Book) newValue.getValue();
				        	textZoneChapterTitle.setText(book.getTitle());
				        } else {
					        textZoneChapterTitle.setText("");
					        textZoneChapterContent.getChildren()
					                .setAll(new Text("Please select/create a chapter in the review tree."));
				        }
			        }
		        });
	}

}
