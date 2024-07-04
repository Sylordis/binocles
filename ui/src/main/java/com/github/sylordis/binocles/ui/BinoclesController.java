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

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.decorators.BookDecorator;
import com.github.sylordis.binocles.model.decorators.ChapterDecorator;
import com.github.sylordis.binocles.model.decorators.CommentTypeDecorator;
import com.github.sylordis.binocles.model.decorators.NomenclatureDecorator;
import com.github.sylordis.binocles.model.io.BinoclesIOFactory;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.DefaultNomenclature;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.review.NomenclatureItem;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.model.text.ReviewableContent;
import com.github.sylordis.binocles.ui.alerts.TextElementDeletionConfirmationAlert;
import com.github.sylordis.binocles.ui.components.BookTreeRoot;
import com.github.sylordis.binocles.ui.components.CustomTreeCell;
import com.github.sylordis.binocles.ui.components.NomenclatureTreeRoot;
import com.github.sylordis.binocles.ui.dialogs.AboutDialog;
import com.github.sylordis.binocles.ui.dialogs.BookDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.ChapterDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.CommentTypeDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.NomenclatureDetailsDialog;
import com.github.sylordis.binocles.ui.doa.ChapterPropertiesAnswer;
import com.github.sylordis.binocles.ui.doa.CommentTypePropertiesAnswer;
import com.github.sylordis.binocles.ui.functional.TreeDoubleClickEventHandler;
import com.github.sylordis.binocles.ui.javafxutils.Browser;
import com.github.sylordis.binocles.ui.javafxutils.TreeViewUtils;
import com.github.sylordis.binocles.ui.settings.BinoclesUIConfiguration;
import com.github.sylordis.binocles.ui.settings.BinoclesUIConstants;
import com.github.sylordis.binocles.ui.views.BinoclesTabPane;
import com.github.sylordis.binocles.ui.views.BookView;
import com.github.sylordis.binocles.ui.views.ChapterView;
import com.github.sylordis.binocles.ui.views.CommentTypeView;
import com.github.sylordis.binocles.ui.views.WelcomeView;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabDragPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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
	private SplitPane mainSplitPane;

	@FXML
	private TabPane mainTabPane;

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
			return new CustomTreeCell<ReviewableContent>()
			        .decorate(Book.class, new BookDecorator().thenTitle().thenNomenclature())
			        .decorate(Chapter.class, new ChapterDecorator().thenTitle().thenCommentsCount());
		});
		rebuildBooksTree();
		// Initialise the tree for nomenclatures
		TreeItem<NomenclatureItem> nomenclaturesTreeRoot = new TreeItem<>(new NomenclatureTreeRoot());
		nomenclaturesTree.setRoot(nomenclaturesTreeRoot);
		nomenclaturesTree.setCellFactory(p -> {
			return new CustomTreeCell<NomenclatureItem>()
			        .decorate(Nomenclature.class, new NomenclatureDecorator().thenName())
			        .decorate(CommentType.class, new CommentTypeDecorator().thenName());
		});
		rebuildNomenclaturesTree();
		// Set trees change listener
		booksTree.setOnMouseClicked(new TreeDoubleClickEventHandler(booksTree, this::openTabItemAction));
		booksTree.getSelectionModel().selectedItemProperty()
		        .addListener((s, o, n) -> setTextElementsContextMenuStatus());
		nomenclaturesTree
		        .setOnMouseClicked(new TreeDoubleClickEventHandler(nomenclaturesTree, this::openTabItemAction));
		nomenclaturesTree.getSelectionModel().selectedItemProperty()
		        .addListener((s, o, n) -> setReviewElementsContextMenuStatus());
		// Set tabs change listener
		mainTabPane.getSelectionModel().selectedItemProperty()
		        .addListener((s, o, n) -> ((BinoclesTabPane) n.getContent()).updateControllerStatus(this));
		// Other states configuration
		mainTabPane.setTabDragPolicy(TabDragPolicy.REORDER);
		// Add welcome tab if necessary
		mainTabPane.getTabs().add(new Tab("Welcome", new WelcomeView()));
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
		// TODO editBookAction
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
			try {
				Book bookParent = answer.get().parent();
				Chapter chapter = answer.get().chapter();
				bookParent.addChapter(chapter);
				logger.info("Created chapter '{}' in '{}'", chapter.getTitle(), bookParent.getTitle());
				TreeItem<ReviewableContent> chapterTreeItem = new TreeItem<>(chapter);
				TreeItem<ReviewableContent> currentBookParent = TreeViewUtils.getTreeViewItem(booksTree.getRoot(),
				        bookParent);
				currentBookParent.getChildren().add(chapterTreeItem);
				currentBookParent.setExpanded(true);
			} catch (UniqueIDException e) {
				showErrorAlert("Unique ID error", "Books only accept unique id'd chapters.");
			}
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
				logger.info("Deleting '{}'", treeSelected.getValue());
				// Remove in model
				if (treeSelected.getValue() instanceof Book) {
					model.getBooks().remove(treeSelected.getValue());
				} else if (treeSelected.getValue() instanceof Chapter) {
					treeSelected.getParent().getValue().getChildren().remove(treeSelected.getValue());
				}
				// Remove in tree
				treeSelected.getParent().getChildren().remove(treeSelected);
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
			// TODO Choose what to export
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

	/**
	 * Opens a new tab from the books tree.
	 * 
	 * @param event
	 */
	public void openTabItemAction(TreeItem<?> item) {
		// Check if tab with same item already open
		Optional<Tab> needle = mainTabPane.getTabs().stream()
		        .filter(t -> ((BinoclesTabPane) t.getContent()).getItem() != null
		                && ((BinoclesTabPane) t.getContent()).getItem().equals(item.getValue()))
		        .findFirst();
		if (needle.isPresent()) {
			logger.debug("Double click: present");
			// If so just switch to it
			mainTabPane.getSelectionModel().select(needle.get());
		} else {
			createNewTab(item);
		}
	}

	/**
	 * Creates a new tab in the tabbed pane.
	 * 
	 * @param object
	 */
	private void createNewTab(TreeItem<?> item) {
		Node node = null;
		String title = null;
		if (item.getValue() instanceof ReviewableContent) {
			ReviewableContent content = (ReviewableContent) item.getValue();
			title = content.getTitle();
			if (content instanceof Chapter) {
				node = new ChapterView(this, (Book) item.getParent().getValue(), (Chapter) content);
			} else if (content instanceof Book) {
				node = new BookView(this, (Book) content);
			}
		} else if (item.getValue() instanceof NomenclatureItem) {
			NomenclatureItem content = (NomenclatureItem) item.getValue();
			if (content instanceof Nomenclature) {
				title = ((Nomenclature) content).getName();
				// TODO create node
			} else if (content instanceof CommentType) {
				title = ((CommentType) content).getName();
				node = new CommentTypeView(this, (Nomenclature) item.getParent().getValue(), (CommentType) content);
			}
		}
		if (node != null && title != null) {
			Tab tab = new Tab(title, node);
			Image img = AppIcons.getImageForType(item.getValue().getClass());
			if (img != null)
				tab.setGraphic(AppIcons.createImageViewFromConfig(img));
			mainTabPane.getTabs().add(tab);
			mainTabPane.getSelectionModel().select(tab);
		}
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
			showLongErrorAlert("Error while opening file", "Could not import the selected file.", e.getMessage());
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
	 * Shows a big error alert dialog.
	 * 
	 * @param title Title of the alert.
	 * @param text  Message of the alert.
	 */
	private void showLongErrorAlert(String title, String text, String longText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(text);
		Label label = new Label("The error message was:");
		TextArea textArea = new TextArea(longText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxHeight(Double.MAX_VALUE);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.getDialogPane().setExpanded(true);
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
		// TODO Get all books that are expanded and expand them again after rebuilding
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

	/**
	 * 
	 * @return the current model
	 */
	public BinoclesModel getModel() {
		return model;
	}
}
