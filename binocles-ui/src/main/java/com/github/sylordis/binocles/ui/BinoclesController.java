package com.github.sylordis.binocles.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.exceptions.ExporterException;
import com.github.sylordis.binocles.model.exceptions.ImporterException;
import com.github.sylordis.binocles.model.exceptions.UniqueNameException;
import com.github.sylordis.binocles.model.io.FileExporter;
import com.github.sylordis.binocles.model.io.FileImporter;
import com.github.sylordis.binocles.model.io.IOFactory;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.review.NomenclatureItem;
import com.github.sylordis.binocles.model.review.ReviewableContent;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.ui.answers.CreateBookAnswer;
import com.github.sylordis.binocles.ui.answers.CreateChapterAnswer;
import com.github.sylordis.binocles.ui.components.NomenclatureTreeCell;
import com.github.sylordis.binocles.ui.components.NomenclatureTreeRoot;
import com.github.sylordis.binocles.ui.components.ReviewTreeCell;
import com.github.sylordis.binocles.ui.components.ReviewableContentTreeRoot;
import com.github.sylordis.binocles.ui.dialogs.AboutDialog;
import com.github.sylordis.binocles.ui.dialogs.CreateBookDialog;
import com.github.sylordis.binocles.ui.dialogs.CreateChapterDialog;
import com.github.sylordis.binocles.ui.javafxutils.Browser;
import com.github.sylordis.binocles.ui.javafxutils.TreeViewUtils;
import com.github.sylordis.binocles.ui.settings.BinoclesConfiguration;
import com.github.sylordis.binocles.ui.settings.BinoclesConstants;
import com.github.sylordis.binocles.utils.comparators.IdentifiableComparator;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the application.
 * 
 * @author sylordis
 *
 */
public class BinoclesController implements Initializable {

	@FXML
	private TreeView<ReviewableContent> bookTree;
	@FXML
	private TreeView<NomenclatureItem> nomenclatureTree;

	@FXML
	private MenuBar menuBar;

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
	private MenuItem menuReviewBookCreate;
	@FXML
	private MenuItem menuReviewChapterCreate;
	@FXML
	private MenuItem menuReviewNomenclatureCreate;
	@FXML
	private MenuItem menuReviewCommentTypeCreate;

	@FXML
	private MenuItem menuHelpDocumentation;
	@FXML
	private MenuItem menuHelpAbout;

	@FXML
	private MenuItem reviewTreeMenuEdit;
	@FXML
	private MenuItem reviewTreeMenuDelete;

	@FXML
	private Button toolbarOpen;
	@FXML
	private Button toolbarSave;
	@FXML
	private Button toolbarSaveAs;
	@FXML
	private Button toolbarExportStruct;
	@FXML
	private Button toolbarExportRender;
	@FXML
	private Button toolbarCreateBook;
	@FXML
	private Button toolbarCreateChapter;

	@FXML
	private VBox textZoneVBox;
	@FXML
	private Text textZoneChapterTitle;
	@FXML
	private Text textZoneMessages;
	/**
	 * Text zone for chapter content.
	 */
	private StyleClassedTextArea textZoneChapterContent;
	/**
	 * Scroll pane for chapter content.
	 */
	private VirtualizedScrollPane<StyleClassedTextArea> textZoneChapterContentScrollPane;

	@FXML
	private VBox commentZoneVBox;

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Current model managed by the controller.
	 */
	private BinoclesModel model;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.debug("Controller init");
		// Initialise model
		model = new BinoclesModel();
		// Initialise the tree for reviews
		TreeItem<ReviewableContent> reviewsTreeRoot = new TreeItem<>(new ReviewableContentTreeRoot());
		bookTree.setRoot(reviewsTreeRoot);
		bookTree.setCellFactory(p -> {
			return new ReviewTreeCell();
		});
		// Initialise the tree for nomenclatures
		TreeItem<NomenclatureItem> nomenclaturesTreeRoot = new TreeItem<>(new NomenclatureTreeRoot());
		nomenclatureTree.setRoot(nomenclaturesTreeRoot);
		nomenclatureTree.setCellFactory(p -> {
			return new NomenclatureTreeCell();
		});
		// Set trees change listener
		bookTree.getSelectionModel().selectedItemProperty()
		        .addListener(new ChangeListener<TreeItem<ReviewableContent>>() {

			        @Override
			        public void changed(ObservableValue<? extends TreeItem<ReviewableContent>> observable,
			                TreeItem<ReviewableContent> oldValue, TreeItem<ReviewableContent> newValue) {
				        setDisplayZoneContent(newValue);
			        }
		        });
		// Text zone messages
		textZoneMessages.setVisible(true);
		textZoneMessages.setText("Please select/create a chapter in the review tree.");
		// Text zone chapter content
		textZoneChapterContent = new StyleClassedTextArea();
		textZoneChapterContent.setEditable(false);
		textZoneChapterContent.setStyle(null);
		textZoneChapterContent.setWrapText(true);
		textZoneChapterContent.setVisible(false);
		textZoneChapterContent.replaceText("");
		// Text zone chapter content scroll pane
		textZoneChapterContentScrollPane = new VirtualizedScrollPane<>(textZoneChapterContent);
		textZoneVBox.getChildren().add(textZoneChapterContentScrollPane);
		textZoneChapterContentScrollPane.setPickOnBounds(true);
		textZoneChapterContent.prefHeightProperty().bind(textZoneVBox.heightProperty());
	}

	/**
	 * Creates a new book by opening a {@link CreateBookDialog}.
	 * 
	 * @param event
	 */
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
				// Add new book to model and front-end
				model.addBook(book);
				TreeItem<ReviewableContent> bookItem = new TreeItem<>(book);
				bookItem.setExpanded(true);
				bookTree.getRoot().getChildren().add(bookItem);
				bookTree.getRoot().getChildren()
				        .sort((s1, s2) -> new IdentifiableComparator().compare(s1.getValue(), s2.getValue()));
				bookTree.getSelectionModel().select(bookItem);
			} catch (UniqueNameException e) {
				logger.error(e);
			}
			setButtonsStatus();
		}
	}

	/**
	 * Creates a new chapter in a book.
	 * 
	 * @param event
	 */
	@FXML
	public void createChapterAction(ActionEvent event) {
		// Get currently selected item
		TreeItem<ReviewableContent> treeSelected = bookTree.getSelectionModel().getSelectedItem();
		Book currentBook = null;
		if (null != treeSelected) {
			// Get current book
			TreeItem<ReviewableContent> currentTreeBookParent = Book.class.equals(treeSelected.getValue().getClass())
			        ? treeSelected
			        : treeSelected.getParent();
			currentBook = (Book) currentTreeBookParent.getValue();
		}
		// Create dialog
		CreateChapterDialog dialog = new CreateChapterDialog(model, currentBook);
		Optional<CreateChapterAnswer> answer = dialog.display();
		if (answer.isPresent()) {
			Book bookParent = answer.get().parent();
			logger.info("Created chapter '{}' in '{}'", answer.get().title(), bookParent.getTitle());
			Chapter chapter = new Chapter(answer.get().title(), answer.get().content());
			bookParent.addChapter(chapter);
			TreeItem<ReviewableContent> chapterTreeItem = new TreeItem<>(chapter);
			TreeItem<ReviewableContent> currentBookParent = TreeViewUtils.getTreeViewItem(bookTree.getRoot(),
			        bookParent);
			currentBookParent.getChildren().add(chapterTreeItem);
			currentBookParent.setExpanded(true);
			bookTree.getSelectionModel().select(chapterTreeItem);
		}
		setButtonsStatus();
	}

	@FXML
	public void deleteElementsAction(ActionEvent event) {
		// TODO Get all selected elements in the tree, open confirmation dialog.
		showNotImplementedAlert();
	}

	@FXML
	public void exitAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Are you sure you want to quit?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
			Platform.exit();
	}

	@FXML
	public void exportRenderAction(ActionEvent event) {
		// TODO
		showNotImplementedAlert();
	}
	
	@FXML
	public void exportStructuralAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(BinoclesConfiguration.getInstance().getFileFilters(true));
		fileChooser.setTitle("Export file");
		// Open file saver
		Stage stage = getStageFromSourceOrMenuBar(event);
		File file = fileChooser.showSaveDialog(stage);
		if (null != file) {
			FileExporter exporter = new IOFactory().getFileExporter(file);
			if (null != exporter) {
				try {
					exporter.export(this.model, file);
				} catch (IOException e) {
					logger.atError().withThrowable(e).log("Could not write to the selected file.");
				} catch (ExporterException e) {
					logger.atError().withThrowable(e).log("Could not export the selected file.");
				}
			}
		}
	}

	/**
	 * Gets current stage from the event source or from the menu bar.
	 * 
	 * @param event
	 * @return
	 */
	public Stage getStageFromSourceOrMenuBar(ActionEvent event) {
		Stage stage = null;
		try {
			Node node = (Node) event.getSource();
			stage = (Stage) node.getScene().getWindow();
		} catch (ClassCastException e) {
			stage = (Stage) menuBar.getScene().getWindow();
		}
		return stage;
	}

	@FXML
	public void openAboutAction(ActionEvent event) {
		new AboutDialog().display();
	}

	@FXML
	public void openDocumentationAction(ActionEvent event) {
		new Browser().open(BinoclesConstants.DOCUMENTATION_LINK);
	}

	@FXML
	public void openFileAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(BinoclesConfiguration.getInstance().getFileFilters(true));
		// Open file chooser
		Stage stage = getStageFromSourceOrMenuBar(event);
		File file = fileChooser.showOpenDialog(stage);
		// Null file means dialog was cancelled
		if (null != file) {
			FileImporter<BinoclesModel> importer = new IOFactory().getFileImporter(file);
			// No importer found
			if (null == importer) {
				Message msg = new StringFormattedMessage("Cannot open file '{}', file type is not managed.", file);
				logger.error(msg);
				showErrorAlert(msg.toString());
			} else {
				try {
					// Read file
					BinoclesModel importedModel = importer.load(file);
					// Replace model
					this.model = importedModel;
					// TODO (Later) Add to model: conflict management (same IDs?)
					rebuildTrees();
					// TODO Set disable statuses
				} catch (IOException e) {
					logger.atError().withThrowable(e).log("Could not read the selected file.");
				} catch (ImporterException e) {
					logger.atError().withThrowable(e).log("Could not import the selected file.");
				}
			}
		}
		setButtonsStatus();
	}

	public void setButtonsStatus() {
		// Affect UI
		if (model.hasBooks()) {
			menuReviewChapterCreate.setDisable(false);
			toolbarCreateChapter.setDisable(false);
		}
	}

	/**
	 * Sets the chapter content's value according to what is selected.
	 * 
	 * @param value current selected value
	 */
	public void setDisplayZoneContent(TreeItem<ReviewableContent> value) {
		textZoneChapterTitle.setText("");
		textZoneMessages.setText("");
		textZoneChapterContent.clear();
		if (Chapter.class.equals(value.getValue().getClass())) {
			Chapter chapter = (Chapter) value.getValue();
			textZoneChapterTitle.setText(chapter.getTitle());
			textZoneMessages.setVisible(false);
			textZoneMessages.setText("");
			textZoneChapterContent.setVisible(true);
			textZoneChapterContent.replaceText(chapter.getText());
		} else if (Book.class.equals(value.getValue().getClass())) {
			Book book = (Book) value.getValue();
			textZoneChapterTitle.setText(book.getTitle());
			textZoneChapterContent.clear();
			textZoneMessages.setVisible(true);
			textZoneMessages.setText("Please select/create a chapter in the review tree to display its content.");
			textZoneChapterContent.setVisible(false);
		} else {
			textZoneChapterTitle.setText("");
			textZoneMessages.setVisible(true);
			textZoneMessages.setText("Please select an item the review tree.");
			textZoneChapterContent.setVisible(false);
		}
	}

	/**
	 * Displays a small alert dialog that the feature is not implemented yet.
	 */
	private void showNotImplementedAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uhoh");
		alert.setHeaderText(null);
		alert.setContentText("It seems this feature is not implemented yet, please come back later =)");
		alert.showAndWait();
	}

	/**
	 * Shows a small error alert dialog.
	 * 
	 * @param string Message of the alert.
	 */
	private void showErrorAlert(String text) {
		showErrorAlert("Error", text);
	}

	/**
	 * Shows a small error alert dialog.
	 * 
	 * @param title Title of the alert.
	 * @param text  Message of the alert.
	 */
	private void showErrorAlert(String title, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}

	/**
	 * Rebuilds the trees according to model.
	 * 
	 * @see BinoclesController#rebuildBooksTree()
	 * @see #rebuildNomenclaturesTree()
	 */
	public void rebuildTrees() {
		rebuildBooksTree();
		rebuildNomenclaturesTree();
	}

	/**
	 * Rebuilds the book tree according to model.
	 */
	public void rebuildBooksTree() {
		// Remove all existing books
		bookTree.getRoot().getChildren().clear();
		// Add all books
		for (Book book : model.getBooks()) {
			TreeItem<ReviewableContent> bookNode = new TreeItem<>(book);
			bookTree.getRoot().getChildren().add(bookNode);
			// Add chapters to book
			for (Chapter chapter : book.getChapters()) {
				bookNode.getChildren().add(new TreeItem<ReviewableContent>(chapter));
			}
		}
	}

	/**
	 * Rebuilds the nomenclature tree according to model.
	 */
	public void rebuildNomenclaturesTree() {
		// Remove all existing nomenclatures
		nomenclatureTree.getRoot().getChildren().clear();
		// Add all nomenclatures
		for (Nomenclature nomenclature : model.getNomenclatures()) {
			TreeItem<NomenclatureItem> nomenclatureNode = new TreeItem<>(nomenclature);
			nomenclatureTree.getRoot().getChildren().add(nomenclatureNode);
			for (CommentType commentType : nomenclature.getTypes()) {
				nomenclatureNode.getChildren().add(new TreeItem<NomenclatureItem>(commentType));
			}
		}
	}

}
