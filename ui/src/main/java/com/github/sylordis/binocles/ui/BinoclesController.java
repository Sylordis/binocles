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
import com.github.sylordis.binocles.model.io.BinoclesIOFactory;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.DefaultNomenclature;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.review.NomenclatureItem;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.model.text.ReviewableContent;
import com.github.sylordis.binocles.ui.alerts.TextElementDeletionConfirmationAlert;
import com.github.sylordis.binocles.ui.components.BookTreeCell;
import com.github.sylordis.binocles.ui.components.BookTreeRoot;
import com.github.sylordis.binocles.ui.components.NomenclatureTreeCell;
import com.github.sylordis.binocles.ui.components.NomenclatureTreeRoot;
import com.github.sylordis.binocles.ui.dialogs.AboutDialog;
import com.github.sylordis.binocles.ui.dialogs.BookDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.ChapterDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.CommentTypeDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.NomenclatureDetailsDialog;
import com.github.sylordis.binocles.ui.doa.ChapterPropertiesAnswer;
import com.github.sylordis.binocles.ui.doa.CommentTypePropertiesAnswer;
import com.github.sylordis.binocles.ui.javafxutils.Browser;
import com.github.sylordis.binocles.ui.javafxutils.TreeViewUtils;
import com.github.sylordis.binocles.ui.settings.BinoclesUIConfiguration;
import com.github.sylordis.binocles.ui.settings.BinoclesUIConstants;
import com.github.sylordis.binocles.utils.exceptions.ExportException;
import com.github.sylordis.binocles.utils.exceptions.ImportException;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;
import com.github.sylordis.binocles.utils.io.FileExporter;
import com.github.sylordis.binocles.utils.io.FileImporter;

import javafx.application.Platform;
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
	private TreeView<ReviewableContent> booksTree;
	@FXML
	private TreeView<NomenclatureItem> nomenclaturesTree;

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
	private MenuItem nomenclaturesTreeMenuNewNomenclature;
	@FXML
	private MenuItem nomenclaturesTreeMenuNewCommentType;
	@FXML
	private MenuItem nomenclaturesTreeMenuEdit;
	@FXML
	private MenuItem nomenclaturesTreeMenuDelete;

	@FXML
	private MenuItem booksTreeMenuNewBook;
	@FXML
	private MenuItem booksTreeMenuNewChapter;
	@FXML
	private MenuItem booksTreeMenuEdit;
	@FXML
	private MenuItem booksTreeMenuDelete;

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
	private Button toolbarCreateNomenclature;
	@FXML
	private Button toolbarCreateCommentType;

	@FXML
	private Button toolbarTextCreateComment;

	@FXML
	private VBox textZoneVBox;
	@FXML
	private Text textZoneBookTitle;
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
	@FXML
	private Text commentZoneTitle;
	@FXML
	private VBox commentZoneVBoxInner;
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
		logger.trace("Controller initialisation");
		// Initialise model
		model = new BinoclesModel();
		try {
			model.addNomenclature(new DefaultNomenclature());
		} catch (UniqueIDException e) {
			// First nomenclature added, cannot have exception
		}
		// Initialise the tree for text items
		TreeItem<ReviewableContent> textTreeRoot = new TreeItem<>(new BookTreeRoot());
		booksTree.setRoot(textTreeRoot);
		booksTree.setCellFactory(p -> {
			return new BookTreeCell();
		});
		rebuildBooksTree();
		// Initialise the tree for nomenclatures
		TreeItem<NomenclatureItem> nomenclaturesTreeRoot = new TreeItem<>(new NomenclatureTreeRoot());
		nomenclaturesTree.setRoot(nomenclaturesTreeRoot);
		nomenclaturesTree.setCellFactory(p -> {
			return new NomenclatureTreeCell();
		});
		rebuildNomenclaturesTree();
		// Set trees change listener
		booksTree.getSelectionModel().selectedItemProperty().addListener((s, o, n) -> setDisplayZoneContent(n));
		booksTree.getSelectionModel().selectedItemProperty().addListener((s,o,n) -> setTextElementsContextMenuStatus());
		nomenclaturesTree.getSelectionModel().selectedItemProperty().addListener((s,o,n) -> setReviewElementsContextMenuStatus());
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
		textZoneChapterContent.selectionProperty()
		        .addListener((s, o, n) -> toolbarTextCreateComment.setDisable(n == null || n.getLength() == 0));
		// Text zone chapter content scroll pane
		textZoneChapterContentScrollPane = new VirtualizedScrollPane<>(textZoneChapterContent);
		textZoneVBox.getChildren().add(textZoneChapterContentScrollPane);
		textZoneChapterContentScrollPane.setPickOnBounds(true);
		textZoneChapterContent.prefHeightProperty().bind(textZoneVBox.heightProperty());
		logger.trace("Controller initialisation finished");
	}

	/**
	 * Creates a new book by opening a {@link BookDetailsDialog}.
	 * 
	 * @param event
	 */
	@FXML
	public void createBookAction(ActionEvent event) {
		BookDetailsDialog dialog = new BookDetailsDialog(model);
		Optional<Book> answer = dialog.display();
		// Check answer is valid
		if (answer.isPresent()) {
			try {
				// Add new book to model and front-end
				model.addBook(answer.get());
				rebuildBooksTree();
			} catch (UniqueIDException e) {
				logger.error(e);
			}
			setButtonsStatus();
		}
	}

	@FXML
	public void editBookAction(ActionEvent event) {
		// TODO
	}

	/**
	 * Creates a new chapter in a book.
	 * 
	 * @param event
	 */
	@FXML
	public void createChapterAction(ActionEvent event) {
		// Get currently selected item
		TreeItem<ReviewableContent> treeSelected = booksTree.getSelectionModel().getSelectedItem();
		Book currentBook = null;
		if (null != treeSelected) {
			// Get current book
			TreeItem<ReviewableContent> currentTreeBookParent = Book.class.equals(treeSelected.getValue().getClass())
			        ? treeSelected
			        : treeSelected.getParent();
			currentBook = (Book) currentTreeBookParent.getValue();
		}
		// Create dialog
		ChapterDetailsDialog dialog = new ChapterDetailsDialog(model, currentBook);
		Optional<ChapterPropertiesAnswer> answer = dialog.display();
		if (answer.isPresent()) {
			Book bookParent = answer.get().parent();
			Chapter chapter = answer.get().chapter();
			bookParent.addChapter(chapter);
			logger.info("Created chapter '{}' in '{}'", chapter.getTitle(), bookParent.getTitle());
			TreeItem<ReviewableContent> chapterTreeItem = new TreeItem<>(chapter);
			TreeItem<ReviewableContent> currentBookParent = TreeViewUtils.getTreeViewItem(booksTree.getRoot(),
			        bookParent);
			currentBookParent.getChildren().add(chapterTreeItem);
			currentBookParent.setExpanded(true);
			booksTree.getSelectionModel().select(chapterTreeItem);
		}
		setButtonsStatus();
	}

	@FXML
	public void createNomenclatureAction(ActionEvent event) {
		NomenclatureDetailsDialog dialog = new NomenclatureDetailsDialog(model);
		Optional<String> answer = dialog.display();
		// Check answer is valid
		if (answer.isPresent()) {
			String title = answer.get();
			try {
				Nomenclature nomenclature = new Nomenclature(title);
				// Add new book to model and front-end
				model.addNomenclature(nomenclature);
				rebuildNomenclaturesTree();
			} catch (UniqueIDException e) {
				logger.error(e);
			}
			setButtonsStatus();
		}
	}

	@FXML
	public void createCommentTypeAction(ActionEvent event) {
		// Get currently selected item
		TreeItem<NomenclatureItem> treeSelected = nomenclaturesTree.getSelectionModel().getSelectedItem();
		Nomenclature currentNomenclature = null;
		if (null != treeSelected) {
			// Get current book
			TreeItem<NomenclatureItem> currentTreeNomenclatureParent = Nomenclature.class
			        .equals(treeSelected.getValue().getClass()) ? treeSelected : treeSelected.getParent();
			currentNomenclature = (Nomenclature) currentTreeNomenclatureParent.getValue();
		}
		CommentTypeDetailsDialog dialog = new CommentTypeDetailsDialog(model, currentNomenclature);
		Optional<CommentTypePropertiesAnswer> answer = dialog.display();
		if (answer.isPresent()) {
			Nomenclature nomenclature = answer.get().nomenclature();
			CommentType type = answer.get().commentType();
			nomenclature.getTypes().add(type);
			logger.info("Created comment type '{}', added to '{}'", type.getName(), nomenclature.getName());
			logger.debug("  fields: {}", type.getFields().keySet());
			logger.debug("  styles:");
			type.getStyles().forEach((k, v) -> logger.debug("    {}: {}", k, v));
			TreeItem<NomenclatureItem> commentTypeTreeItem = new TreeItem<>(type);
			TreeItem<NomenclatureItem> currentBookParent = TreeViewUtils.getTreeViewItem(nomenclaturesTree.getRoot(),
			        nomenclature);
			currentBookParent.getChildren().add(commentTypeTreeItem);
			currentBookParent.setExpanded(true);
			nomenclaturesTree.getSelectionModel().select(commentTypeTreeItem);
		}
	}

	@FXML
	public void createCommentAction(ActionEvent event) {
		// TODO
		showNotImplementedAlert();
	}

	/**
	 * This action is triggered when the user prompts to delete one or multiple text elements (Book
	 * and/or Chapter).
	 * 
	 * @param event
	 */
	@FXML
	public void deleteTextElementsAction(ActionEvent event) {
		TreeItem<ReviewableContent> treeSelected = booksTree.getSelectionModel().getSelectedItem();
		if (null != treeSelected) {
			TextElementDeletionConfirmationAlert alert = new TextElementDeletionConfirmationAlert();
			alert.setTreeRoot(treeSelected.getValue());
			Optional<ButtonType> answer = alert.showAndWait();
			if (answer.isPresent() && answer.get().equals(ButtonType.OK)) {
				logger.info("Deleting {}", treeSelected.getValue());
				if (treeSelected.getValue() instanceof Book) {
					model.getBooks().remove(treeSelected.getValue());
				} else if (treeSelected.getValue() instanceof Chapter) {
					treeSelected.getParent().getValue().getChildren().remove(treeSelected.getValue());
				}
				rebuildBooksTree();
			}
		} else {
			showErrorAlert("No element in the book tree is selected.");
		}
	}

	/**
	 * This action is triggered when the user prompts to delete one or multiple review elements
	 * (Nomenclature and/or Comment Type).
	 * 
	 * @param event
	 */
	@FXML
	public void deleteReviewElementsAction(ActionEvent event) {
		// TODO Get all selected elements in the tree, open confirmation dialog.
		showNotImplementedAlert();
	}

	@FXML
	public void editReviewElementAction(ActionEvent event) {
		// TODO Open dialog and then modify element.
		showNotImplementedAlert();
	}

	@FXML
	public void editTextElementAction(ActionEvent event) {
		// TODO Open dialog and then modify element.
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
		// TODO Export given selection with a wizard
		showNotImplementedAlert();
	}

	@FXML
	public void exportStructuralAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(BinoclesUIConfiguration.getInstance().getFileFilters(true));
		fileChooser.setTitle("Export file");
		// Open file saver
		Stage stage = getStageFromSourceOrMenuBar(event);
		File file = fileChooser.showSaveDialog(stage);
		if (null != file) {
			try {
				FileExporter<BinoclesModel> exporter = new BinoclesIOFactory().getFileExporter(file);
				if (null != exporter) {
					// TODO Export progress report (cancellable dialog with progress bar)
					exporter.export(this.model, file);
				}
			} catch (IOException e) {
				logger.atError().withThrowable(e).log("Could not write to the selected file.");
				showErrorAlert("Could not write to the selected file.");
			} catch (ExportException e) {
				logger.atError().withThrowable(e).log("Could not export the selected file.");
				showErrorAlert("Could not export the selected file.");
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
		new Browser().open(BinoclesUIConstants.DOCUMENTATION_LINK);
	}

	@FXML
	public void openFileAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(BinoclesUIConfiguration.getInstance().getFileFilters(true));
		// Open file chooser
		Stage stage = getStageFromSourceOrMenuBar(event);
		File file = fileChooser.showOpenDialog(stage);
		// Null file means dialog was cancelled
		if (null != file) {
			// TODO Import progress report (cancellable dialog with progress bar)
			openFile(file);
		}
		setButtonsStatus();
	}

	/**
	 * Opens a file and loads its content, replacing current content.
	 * 
	 * @param file
	 */
	public void openFile(File file) {
		try {
			FileImporter<BinoclesModel> importer = new BinoclesIOFactory().getFileImporter(file);
			// No importer found
			if (null == importer) {
				Message msg = new StringFormattedMessage("Cannot open file '{}', file type is not managed.", file);
				logger.error(msg);
				showErrorAlert(msg.toString());
			} else {
				// Read file
				BinoclesModel importedModel = importer.load(file);
				// Replace model
				this.model = importedModel;
				try {
					model.addNomenclature(new DefaultNomenclature());
				} catch (UniqueIDException e) {
					logger.atError().withThrowable(e).log("Error during import.");
					showErrorAlert(
					        "Nomenclature with name 'Default' already exists in the imported file. Import was successful but Default Nomenclature was replaced.");
				}
				// TODO (Later) Add to model: conflict management (same IDs?)
				rebuildTrees();
				setButtonsStatus();
			}
		} catch (IOException e) {
			logger.atError().withThrowable(e).log("Could not read the selected file.");
			showErrorAlert("Could not read the selected file.");
		} catch (ImportException e) {
			logger.atError().withThrowable(e).log("Could not import the selected file.");
			showErrorAlert("Could not import the selected file.");
		}
	}

	/**
	 * Changes the status of the buttons according to software state.
	 */
	public void setButtonsStatus() {
		menuReviewChapterCreate.setDisable(!model.hasBooks());
		toolbarCreateChapter.setDisable(!model.hasBooks());
		menuReviewCommentTypeCreate.setDisable(!model.hasCustomNomenclatures());
		toolbarCreateCommentType.setDisable(!model.hasCustomNomenclatures());
		booksTreeMenuNewChapter.setDisable(!model.hasBooks());
		nomenclaturesTreeMenuNewCommentType.setDisable(!model.hasCustomNomenclatures());
	}

	/**
	 * Sets the review configuration tree's context menu items status according to current selection.
	 */
	public void setReviewElementsContextMenuStatus() {
		nomenclaturesTreeMenuDelete.setDisable(nomenclaturesTree.getSelectionModel().isEmpty());
		nomenclaturesTreeMenuEdit.setDisable(nomenclaturesTree.getSelectionModel().isEmpty());
	}

	/**
	 * Sets the text elements tree's context menu items status according to current selection.
	 */
	public void setTextElementsContextMenuStatus() {
		booksTreeMenuDelete.setDisable(booksTree.getSelectionModel().isEmpty());
		booksTreeMenuEdit.setDisable(booksTree.getSelectionModel().isEmpty());
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
		if (value != null && Chapter.class.equals(value.getValue().getClass())) {
			Book book = (Book) value.getParent().getValue();
			textZoneBookTitle.setText(book.getTitle());
			Chapter chapter = (Chapter) value.getValue();
			textZoneChapterTitle.setVisible(true);
			textZoneChapterTitle.setText(chapter.getTitle());
			textZoneMessages.setVisible(false);
			textZoneMessages.setText("");
			textZoneChapterContent.setVisible(true);
			textZoneChapterContent.replaceText(chapter.getText());
			commentZoneVBox.setVisible(true);
			toolbarTextCreateComment.setVisible(true);
		} else if (value != null && Book.class.equals(value.getValue().getClass())) {
			Book book = (Book) value.getValue();
			textZoneBookTitle.setText(book.getTitle());
			textZoneChapterTitle.setVisible(false);
			textZoneChapterTitle.setText("");
			textZoneChapterContent.clear();
			textZoneMessages.setVisible(true);
			textZoneMessages.setText("Please select/create a chapter in the review tree to display its content.");
			textZoneChapterContent.setVisible(false);
			commentZoneVBox.setVisible(false);
			toolbarTextCreateComment.setVisible(false);
		} else {
			textZoneBookTitle.setText("");
			textZoneChapterTitle.setText("");
			textZoneMessages.setVisible(true);
			textZoneMessages.setText("Please select an item the review tree.");
			textZoneChapterContent.setVisible(false);
			commentZoneVBox.setVisible(false);
			toolbarTextCreateComment.setVisible(false);
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
		booksTree.getRoot().getChildren().clear();
		// TODO Order according to settings
		// Add all books
		for (Book book : model.getBooks()) {
			TreeItem<ReviewableContent> bookNode = new TreeItem<>(book);
			booksTree.getRoot().getChildren().add(bookNode);
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
		nomenclaturesTree.getRoot().getChildren().clear();
		// TODO Order according to settings
		// Add all nomenclatures
		for (Nomenclature nomenclature : model.getNomenclatures()) {
			if (!nomenclature.isDefaultNomenclature()) {
				TreeItem<NomenclatureItem> nomenclatureNode = new TreeItem<>(nomenclature);
				nomenclaturesTree.getRoot().getChildren().add(nomenclatureNode);
				for (CommentType commentType : nomenclature.getTypes()) {
					nomenclatureNode.getChildren().add(new TreeItem<NomenclatureItem>(commentType));
				}
			}
		}
	}

}
