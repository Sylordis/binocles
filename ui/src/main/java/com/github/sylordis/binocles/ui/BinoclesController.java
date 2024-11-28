package com.github.sylordis.binocles.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.github.sylordis.binocles.model.io.BinoclesIOFactory.IOOperation;
import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.DefaultNomenclature;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.review.NomenclatureItem;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.model.text.ReviewableContent;
import com.github.sylordis.binocles.ui.alerts.ReviewElementDeletionConfirmationAlert;
import com.github.sylordis.binocles.ui.alerts.TextElementDeletionConfirmationAlert;
import com.github.sylordis.binocles.ui.components.BookTreeRoot;
import com.github.sylordis.binocles.ui.components.Controller;
import com.github.sylordis.binocles.ui.components.CustomTreeCell;
import com.github.sylordis.binocles.ui.components.NomenclatureTreeRoot;
import com.github.sylordis.binocles.ui.dialogs.AboutDialog;
import com.github.sylordis.binocles.ui.dialogs.BookDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.ChapterDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.CommentTypeDetailsDialog;
import com.github.sylordis.binocles.ui.dialogs.NomenclatureDetailsDialog;
import com.github.sylordis.binocles.ui.doa.ChapterPropertiesAnswer;
import com.github.sylordis.binocles.ui.doa.CommentTypePropertiesAnswer;
import com.github.sylordis.binocles.ui.functional.TreeVarClickEventHandler;
import com.github.sylordis.binocles.ui.javafxutils.Browser;
import com.github.sylordis.binocles.ui.javafxutils.TreeViewUtils;
import com.github.sylordis.binocles.ui.settings.BinoclesUIConfiguration;
import com.github.sylordis.binocles.ui.settings.BinoclesUIConstants;
import com.github.sylordis.binocles.ui.views.BinoclesTabPane;
import com.github.sylordis.binocles.ui.views.BookView;
import com.github.sylordis.binocles.ui.views.ChapterView;
import com.github.sylordis.binocles.ui.views.CommentTypeView;
import com.github.sylordis.binocles.ui.views.NomenclatureView;
import com.github.sylordis.binocles.ui.views.WelcomeView;
import com.github.sylordis.binocles.utils.exceptions.ExportException;
import com.github.sylordis.binocles.utils.exceptions.ImportException;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;
import com.github.sylordis.binocles.utils.io.FileExporter;
import com.github.sylordis.binocles.utils.io.FileImporter;
import com.google.common.base.Objects;

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
import javafx.scene.control.Menu;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the application.
 * 
 * @author sylordis
 *
 */
public class BinoclesController implements Initializable, Controller {

	@FXML
	private TreeView<ReviewableContent> booksTree;
	@FXML
	private TreeView<NomenclatureItem> nomenclaturesTree;

	@FXML
	private Text footerLeftStatusText;
	@FXML
	private Text footerRightStatusText;

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
	private MenuItem menuFileImport;
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
	private Menu menuReviewCommentSub;

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
	private Button toolbarImport;
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
	 * User infos (directories, saving, etc).
	 */
	private BinoclesUserInteraction user;

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Current model managed by the controller.
	 */
	private BinoclesModel model;

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
			user.modelWasModified();
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
				user.modelWasModified();
			} catch (UniqueIDException e) {
				showErrorAlert("Unique ID error", "Books only accept unique id'd chapters.");
			}
		}
		setButtonsStatus();
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
			user.modelWasModified();
		}
	}

	/**
	 * Creates a new tab in the tabbed pane.
	 * 
	 * @param item the current tree item
	 */
	private void createNewTab(TreeItem<?> item) {
		Node node = null;
		String title = null;
		// Try to create node
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
				node = new NomenclatureView(this, (Nomenclature) content);
			} else if (content instanceof CommentType) {
				title = ((CommentType) content).getName();
				node = new CommentTypeView(this, (Nomenclature) item.getParent().getValue(), (CommentType) content);
			}
		}
		// If node was created, create the tab
		if (node != null && title != null) {
			logger.debug("Created new tab '{}'", title);
			Tab tab = new Tab(title, node);
			Image img = AppIcons.getImageForType(item.getValue().getClass());
			if (img != null)
				tab.setGraphic(AppIcons.createImageViewFromConfig(img));
			mainTabPane.getTabs().add(tab);
			mainTabPane.getSelectionModel().select(tab);
		} else {
			showNotImplementedAlert();
		}
	}

	@FXML
	public void createNomenclatureAction(ActionEvent event) {
		NomenclatureDetailsDialog dialog = new NomenclatureDetailsDialog(model);
		Optional<Nomenclature> answer = dialog.display();
		// Check answer is valid
		if (answer.isPresent()) {
			Nomenclature nomenclature = answer.get();
			try {
				model.addNomenclatureUnique(nomenclature);
				rebuildNomenclaturesTree();
				user.modelWasModified();
			} catch (UniqueIDException e) {
				logger.error(e);
			}
			setButtonsStatus();
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
		// TODO Multiple selection deletion
		TreeItem<NomenclatureItem> treeSelected = nomenclaturesTree.getSelectionModel().getSelectedItem();
		if (null != treeSelected) {
			// Check if in use
			List<Object> used = new ArrayList<>();
			Class<?> usedBy = null;
			if (treeSelected.getValue() instanceof Nomenclature) {
				used = model.getBooks().stream().filter(b -> treeSelected.getValue().equals(b.getNomenclature()))
				        .collect(Collectors.toList());
				usedBy = Book.class;
			} else if (treeSelected.getValue() instanceof CommentType) {
				used = model.getBooks().stream().flatMap(b -> b.getChapters().stream())
				        .flatMap(c -> c.getComments().stream()).filter(c -> treeSelected.getValue().equals(c.getType()))
				        .collect(Collectors.toList());
				usedBy = Comment.class;
			}
			if (!used.isEmpty()) {
				showErrorAlert("Delete error", String.format(
				        "The object(s) you are trying to delete are currently in use by %d %s%s.%n%nPlease perform a migration before deleting.",
				        used.size(), usedBy.getSimpleName(), used.size() > 1 ? "s" : ""));
			} else {
				ReviewElementDeletionConfirmationAlert alert = new ReviewElementDeletionConfirmationAlert();
				alert.setGraphic(AppIcons.createImageViewFromConfig(AppIcons.ICON_TRASH));
				alert.setTreeRoot(treeSelected.getValue());
				Optional<ButtonType> answer = alert.showAndWait();
				if (answer.isPresent() && answer.get().equals(ButtonType.OK)) {
					logger.info("Deleting '{}'", treeSelected.getValue());
					// Remove in model
					if (treeSelected.getValue() instanceof Nomenclature) {
						model.getNomenclatures().remove(treeSelected.getValue());
					} else if (treeSelected.getValue() instanceof CommentType) {
						treeSelected.getParent().getValue().getChildren().remove(treeSelected.getValue());
					}
					// Remove in tree
					treeSelected.getParent().getChildren().remove(treeSelected);
					user.modelWasModified();
				}
			}
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
		if (!booksTree.getSelectionModel().isEmpty()) {
			// TODO Multi deletion
			TreeItem<ReviewableContent> treeSelected = booksTree.getSelectionModel().getSelectedItem();
			TextElementDeletionConfirmationAlert alert = new TextElementDeletionConfirmationAlert();
			alert.setGraphic(AppIcons.createImageViewFromConfig(AppIcons.ICON_TRASH));
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
				user.modelWasModified();
			}
		} else {
			showErrorAlert("No element in the book tree is selected.");
		}
	}

	/**
	 * Opens a book edition dialog.
	 * 
	 * @param book item to edit
	 */
	public void editBook(Book book) {
		BookDetailsDialog dialog = new BookDetailsDialog(model, book);
		Optional<Book> answer = dialog.display();
		if (answer.isPresent()) {
			Optional<Tab> optTab = getTabWithItem(book);
			logger.info("Edited book '{}' => '{}'", book, answer.get());
			book.copy(answer.get());
			booksTree.refresh();
			// TODO Refresh book tab
			if (optTab.isPresent()) {
				optTab.get().setText(book.getTitle());
				((BookView) optTab.get().getContent()).updateControllerStatus(this);
			}
			user.modelWasModified();
		}
	}

	/**
	 * Opens a chapter edition dialog.
	 * 
	 * @param chapter item to edit
	 * @param book    book the chapter is part of
	 */
	public void editChapter(Chapter chapter, Book book) {
		ChapterDetailsDialog dialog = new ChapterDetailsDialog(model, book, chapter);
		Optional<ChapterPropertiesAnswer> answer = dialog.display();
		if (answer.isPresent()) {
			Optional<Tab> optTab = getTabWithItem(chapter);
			Chapter chapterModified = answer.get().chapter();
			logger.info("Edited chapter '{}' in '{}' => {}", chapter.getTitle(), book.getTitle(), chapterModified);
			chapter.copy(chapterModified);
			booksTree.refresh();
			if (optTab.isPresent()) {
				optTab.get().setText(chapter.getTitle());
				((ChapterView) optTab.get().getContent()).updateControllerStatus(this);
			}
			user.modelWasModified();
			// TODO Consolidate comments
		}
	}

	/**
	 * Opens a comment type edition dialog.
	 * 
	 * @param commentType  item to edit
	 * @param nomenclature nomenclature the comment type is part of
	 */
	public void editCommentType(CommentType commentType, Nomenclature nomenclature) {
		CommentTypeDetailsDialog dialog = new CommentTypeDetailsDialog(model, nomenclature, commentType);
		Optional<CommentTypePropertiesAnswer> answer = dialog.display();
		if (answer.isPresent()) {
			Optional<Tab> optTab = getTabWithItem(nomenclature);
			CommentType modified = answer.get().commentType();
			logger.info("Edited comment type '{}' in '{}' => {}", commentType.getName(), nomenclature.getName(),
			        modified);
			commentType.copy(modified);
			nomenclaturesTree.refresh();
			if (optTab.isPresent())
				((BinoclesTabPane) optTab.get().getContent()).updateControllerStatus(this);
			getTabViewsOfType(ChapterView.class, v -> Objects.equal(v.getBook().getNomenclature(), nomenclature))
			        .forEach(v -> v.updateControllerStatus(this));
			user.modelWasModified();
		}
	}

	/**
	 * Opens a nomenclature edition dialog.
	 * 
	 * @param nomenclature item to edit
	 */
	public void editNomenclature(Nomenclature nomenclature) {
		NomenclatureDetailsDialog dialog = new NomenclatureDetailsDialog(model, nomenclature);
		Optional<Nomenclature> answer = dialog.display();
		if (answer.isPresent()) {
			Optional<Tab> optTab = getTabWithItem(nomenclature);
			Nomenclature nomenclatureModified = answer.get();
			logger.info("Edited nomenclature '{}' => '{}'", nomenclature.getName(), answer.get());
			nomenclature.copy(nomenclatureModified);
			nomenclaturesTree.refresh();
			booksTree.refresh();
			if (optTab.isPresent()) {
				optTab.get().setText(nomenclature.getName());
				((NomenclatureView) optTab.get().getContent()).updateControllerStatus(this);
			}
			user.modelWasModified();
		}
	}

	@FXML
	public void editReviewElementAction(ActionEvent event) {
		TreeItem<NomenclatureItem> treeSelected = nomenclaturesTree.getSelectionModel().getSelectedItem();
		if (null != treeSelected) {
			if (treeSelected.getValue() instanceof Nomenclature)
				editNomenclature((Nomenclature) treeSelected.getValue());
			else if (treeSelected.getValue() instanceof CommentType)
				editCommentType((CommentType) treeSelected.getValue(),
				        (Nomenclature) treeSelected.getParent().getValue());
		}
	}

	@FXML
	public void editTextElementAction(ActionEvent event) {
		TreeItem<ReviewableContent> treeSelected = booksTree.getSelectionModel().getSelectedItem();
		if (null != treeSelected) {
			if (treeSelected.getValue() instanceof Chapter)
				editChapter((Chapter) treeSelected.getValue(), (Book) treeSelected.getParent().getValue());
			else if (treeSelected.getValue() instanceof Book)
				editBook((Book) treeSelected.getValue());
		}
	}

	@FXML
	public void exitAction(ActionEvent event) {
		Platform.exit();
		// TODO Add the following in the stop() from Application
		// https://stackoverflow.com/questions/26619566/javafx-stage-close-handler
		// https://stackoverflow.com/questions/26060859/javafx-getting-scene-from-a-controller
//		if (!user.isCurrentModelSaved()) {
//			Alert alert = new Alert(AlertType.CONFIRMATION);
//			alert.setHeaderText("You have unsaved changes. Are you sure you want to quit?");
//			Optional<ButtonType> result = alert.showAndWait();
//			if (result.get() == ButtonType.OK)
//				Platform.exit();
//		}
	}

	@FXML
	public void exportRenderAction(ActionEvent event) {
		// TODO Export given selection with a wizard
		showNotImplementedAlert();
	}

	@FXML
	public void exportStructuralAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
		        .addAll(BinoclesUIConfiguration.getInstance().getFileFilters(IOOperation.EXPORT_STRUCTURAL, true));
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
	 * Exports the current model to the given file.
	 * 
	 * @param file File to export to
	 * @return true if the export was a success, false otherwise
	 */
	public boolean exportToFile(File file) {
		boolean success = false;
		if (null != file) {
			try {
				FileExporter<BinoclesModel> exporter = new BinoclesIOFactory().getFileExporter(file);
				if (null != exporter) {
					exporter.export(this.model, file);
					success = true;
				}
			} catch (IOException e) {
				logger.atError().withThrowable(e).log("Could not write to the selected file.");
				showErrorAlert("Could not write to the selected file.");
			} catch (ExportException e) {
				logger.atError().withThrowable(e).log("Could not export the selected file.");
				showErrorAlert("Could not export the selected file.");
			}
		}
		return success;
	}

	/**
	 * 
	 * @return the current model
	 */
	public BinoclesModel getModel() {
		return model;
	}

	@Override
	public Controller getParentController() {
		return null;
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

	/**
	 * Gets a tab that is already opened that is holding/representing/containing a given item.
	 * 
	 * @param item item that the tab is holding
	 * @return an optional tab, it has a result if a tab could be found
	 */
	public Optional<Tab> getTabWithItem(Object o) {
		Optional<Tab> needle = mainTabPane.getTabs().stream()
		        .filter(t -> ((BinoclesTabPane) t.getContent()).getItem() != null
		                && ((BinoclesTabPane) t.getContent()).getItem().equals(o))
		        .findFirst();
		return needle;
	}

	/**
	 * Collects all open tabs that contain given view and returns their actual views.
	 * 
	 * @param <T>  type of the views
	 * @param type type of the views
	 * @return a list of views of the provided type.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BinoclesTabPane> List<T> getTabViewsOfType(Class<T> type, Predicate<T> filter) {
		Stream<T> stream = mainTabPane.getTabs().stream().filter(t -> t.getContent().getClass().equals(type))
		        .map(t -> (T) t.getContent());
		if (filter != null)
			stream = stream.filter(filter);
		List<T> result = stream.collect(Collectors.toList());
		logger.debug("Collecting all tabs of '{}' ({})", type.getSimpleName(), result.size());
		return result;
	}

	@FXML
	public void importAction(ActionEvent event) {
		// TODO
		showNotImplementedAlert("Import");
	}

	/**
	 * Opens a file and loads its content, replacing current content.
	 * 
	 * @param file    file to open
	 * @param replace if set to true, will replace the current model
	 */
	public void importFile(File file, boolean replace) {
		try {
			FileImporter<BinoclesModel> importer = new BinoclesIOFactory().getFileImporter(file);
			// No importer found
			if (null == importer) {
				Message msg = new StringFormattedMessage("Cannot open file '{}', file type is not managed.", file);
				logger.error(msg);
				showErrorAlert(msg.toString());
			} else {
				BinoclesModel importedModel = importer.load(file);
				if (replace) {
					this.model = importedModel;
					try {
						model.addNomenclatureUnique(new DefaultNomenclature());
					} catch (UniqueIDException e) {
						model.getNomenclatures().removeIf(n -> n.isDefaultNomenclature());
						model.getNomenclatures().add(new DefaultNomenclature());
						logger.atError().withThrowable(e).log("Error during import.");
						showErrorAlert(
						        "Nomenclature with name 'Default' already exists in the imported file. Import was successful but Default Nomenclature was replaced.");
					}
				} else {
					showNotImplementedAlert();
				}
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

	@FXML
	public void newAction(ActionEvent event) {
		logger.info("New model");
		this.model = new BinoclesModel();
		mainTabPane.getTabs().clear();
		rebuildTrees();
		setButtonsStatus();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.trace("Controller initialisation");
		user = new BinoclesUserInteraction();
		// Initialise model
		model = new BinoclesModel();
		model.getNomenclatures().add(new DefaultNomenclature());
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
		// Bindings & listeners
		user.currentSaveFileProperty().addListener((s, o, n) -> updateFooter());
		user.currentModelIsSavedProperty().addListener((s, o, n) -> updateFooter());
		user.lastSaveTimeProperty().addListener((s, o, n) -> updateFooter());
		// Set trees change listener
		booksTree.setOnMouseClicked(
		        TreeVarClickEventHandler.createDoubleClickHandler(booksTree, this::openTabItemAction));
		booksTree.getSelectionModel().selectedItemProperty()
		        .addListener((s, o, n) -> setTextElementsContextMenuStatus());
		nomenclaturesTree.setOnMouseClicked(
		        TreeVarClickEventHandler.createDoubleClickHandler(nomenclaturesTree, this::openTabItemAction));
		nomenclaturesTree.getSelectionModel().selectedItemProperty()
		        .addListener((s, o, n) -> setReviewElementsContextMenuStatus());
		// Set tabs change listener
		mainTabPane.getSelectionModel().selectedItemProperty().addListener((s, o, n) -> {
			if (n != null)
				((BinoclesTabPane) n.getContent()).updateControllerStatus(this);
		});
		// Other states configuration
		mainTabPane.setTabDragPolicy(TabDragPolicy.REORDER);
//		TODO nomenclaturesTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//		TODO booksTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Add welcome tab if necessary
		mainTabPane.getTabs().add(new Tab("Welcome", new WelcomeView()));
		logger.trace("Controller initialisation finished");
	}

	@FXML
	public void openAboutAction(ActionEvent event) {
		new AboutDialog().display();
	}

	@FXML
	public void openDocumentationAction(ActionEvent event) {
		new Browser().open(BinoclesUIConstants.DOCUMENTATION_LINK);
	}

	/**
	 * Opens a file and loads its content, replacing current content.
	 * 
	 * @param file file to open
	 */
	public void openFile(File file) {
		importFile(file, true);
		if (file.getName().endsWith("." + BinoclesIOFactory.SAVE_FILE_EXTENSION)) {
			logger.debug("binocles files opened! Setting save status.");
			user.setSaved(file);
		}
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
			// Close all tabs
			mainTabPane.getTabs().clear();
			openFile(file);
		}
		setButtonsStatus();
	}

	/**
	 * Opens a new tab from the books tree.
	 * 
	 * @param event
	 */
	public void openTabItemAction(TreeItem<?> item) {
		Optional<Tab> existingTab = getTabWithItem(item.getValue());
		if (existingTab.isPresent()) {
			logger.debug("Switch to tab '{}'", existingTab.get().getText());
			mainTabPane.getSelectionModel().select(existingTab.get());
		} else {
			createNewTab(item);
		}
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
	 * Saves the current model to given file.
	 * 
	 * @param file
	 */
	public void saveToFile(File file) {
		if (file != null && exportToFile(file)) {
			user.setSaved(file);
		}
	}

	@FXML
	public void saveAction(ActionEvent event) {
		if (!user.hasSaveFile()) {
			saveAsAction(event);
		} else {
			saveToFile(user.getCurrentSaveFile());
		}
	}

	@FXML
	public void saveAsAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
		        .addAll(BinoclesUIConfiguration.getInstance().getFileFilters(IOOperation.SAVE, true));
		fileChooser.setTitle("Save file");
		// Open file saver
		Stage stage = getStageFromSourceOrMenuBar(event);
		File file = fileChooser.showSaveDialog(stage);
		if (file != null) {
			saveToFile(file);
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
	 * Update method callable by user controllers.
	 */
	public void setModelChanged() { 
		user.modelWasModified();
	}
	
	@Override
	public void setParentController(Controller parent) {
		// Nothing to do here
	}

	/**
	 * Sets the review configuration tree's context menu items status according to current selection.
	 */
	public void setReviewElementsContextMenuStatus() {
		nomenclaturesTreeMenuDelete.setDisable(nomenclaturesTree.getSelectionModel().isEmpty());
		nomenclaturesTreeMenuEdit.setDisable(nomenclaturesTree.getSelectionModel().getSelectedIndices().size() != 1);
	}

	/**
	 * Sets the text elements tree's context menu items status according to current selection.
	 */
	public void setTextElementsContextMenuStatus() {
		booksTreeMenuDelete.setDisable(booksTree.getSelectionModel().isEmpty());
		booksTreeMenuEdit.setDisable(booksTree.getSelectionModel().getSelectedIndices().size() != 1);
	}

	/**
	 * Shows a small error alert with title "Error" and no header.
	 * 
	 * @param text Message of the alert
	 */
	private void showErrorAlert(String text) {
		showErrorAlert("Error", text);
	}

	/**
	 * Shows an error alert with no header.
	 * 
	 * @param title Title of the alert
	 * @param text  Message of the alert
	 */
	private void showErrorAlert(String title, String text) {
		showErrorAlert(title, null, text);
	}

	/**
	 * Shows an error alert dialog.
	 * 
	 * @param title  Title of the alert
	 * @param header Header text of the alert
	 * @param text   Message of the alert
	 */
	private void showErrorAlert(String title, String header, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}

	/**
	 * Shows an error alert with no header and a long message in a non-editable text area.
	 * 
	 * @param title Title of the alert
	 * @param text  Long message of the alert, appended after a label
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
	 * Displays a small alert dialog that a feature is not implemented yet.
	 */
	public void showNotImplementedAlert() {
		showNotImplementedAlert(null);
	}

	/**
	 * Displays a small alert dialog that a feature is not implemented yet.
	 * 
	 * @param feature the feature name, null is no precision
	 */
	public void showNotImplementedAlert(String feature) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Uhoh");
		alert.setHeaderText(null);
		if (feature != null) {
			alert.setContentText(String
			        .format("It seems the feature '%s' is not implemented yet, please come back later =)", feature));
		} else {
			alert.setContentText("It seems this feature is not implemented yet, please come back later =)");
		}
		alert.showAndWait();
	}

	/**
	 * Updates the footer informations according to user information.
	 */
	public void updateFooter() {
		logger.debug("Updating footer: saved?{} time={} file={}", user.isCurrentModelSaved(), user.getLastSaveTime(),
		        user.getCurrentSaveFile());
		if (!user.isCurrentModelSaved() && user.getCurrentSaveFile() == null) {
			footerLeftStatusText.setText("Unsaved");
		} else {
			String msg = user.isCurrentModelSaved() ? "Saved" : "Modified";
			if (user.getLastSaveTime() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				msg = msg.concat(String.format(" (last save: %s)", user.getLastSaveTime().format(formatter)));
			}
			footerLeftStatusText.setText(msg);
		}
		footerRightStatusText
		        .setText(user.hasSaveFile() ? user.getCurrentSaveFile().getAbsolutePath() : "No save file");
	}
}
