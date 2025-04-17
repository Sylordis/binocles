package com.github.sylordis.binocles.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.dialog.Wizard;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.decorators.BookDecorator;
import com.github.sylordis.binocles.model.decorators.ChapterDecorator;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.model.text.ReviewableContent;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.components.BookTreeRoot;
import com.github.sylordis.binocles.ui.doa.TreeCellTextSupplierIdentifier.CellExpansion;
import com.github.sylordis.binocles.ui.functional.CustomTreeStringConverter;
import com.github.sylordis.binocles.ui.javafxutils.TreeItemTextSupplierManager;
import com.github.sylordis.binocles.ui.javafxutils.TreeViewUtils;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.util.StringConverter;

/**
 * Wizard pane to select a chapter.
 */
class RenderExportChapterChoicePane extends AbstractWizardPane {

	/**
	 * Currently selected chapters.
	 */
	private List<Chapter> chapter;
	/**
	 * Tree view for books and chapters selection.
	 */
	private TreeView<ReviewableContent> fieldTree;

	public RenderExportChapterChoicePane(BinoclesModel model, Chapter chapter) {
		super(model);
		this.chapter = new ArrayList<>();
	}

	@Override
	protected void build() {
		// Create components
		CheckBoxTreeItem<ReviewableContent> root = new CheckBoxTreeItem<>(new BookTreeRoot());
		fieldTree = new TreeView<ReviewableContent>(root);
		fillAndConfigureTree(fieldTree);
		fieldTree.setShowRoot(false);
		Label labelTree = new Label("Select a chapter:");
		// TODO Auto-generated method stub
		// Add components
		addFormFeedback();
		getGridPane().addRow(1, labelTree);
		getGridPane().addRow(2, fieldTree);
		// Set up behaviours
		fieldTree.setMinWidth(300);
	}

	private void fillAndConfigureTree(TreeView<ReviewableContent> fieldTree2) {
		for (Book book : getModel().getBooks()) {
			TreeItem<ReviewableContent> bookNode = new CheckBoxTreeItem<ReviewableContent>(book, AppIcons.getFor(book));
			fieldTree.getRoot().getChildren().add(bookNode);
			// Add chapters to book
			for (Chapter chapter : book.getChapters())
				bookNode.getChildren().add(new CheckBoxTreeItem<ReviewableContent>(chapter, AppIcons.getFor(chapter)));
		}
		// Text manager
		TreeItemTextSupplierManager<ReviewableContent> mgr = new TreeItemTextSupplierManager<ReviewableContent>()
		        .decorate(Book.class, CellExpansion.COLLAPSED,
		                new BookDecorator().thenTitle().thenChapterCountWithText())
		        .decorate(Book.class, CellExpansion.EXPANDED, new BookDecorator().thenTitle())
		        .decorate(Chapter.class, new ChapterDecorator().thenTitle().thenCommentsCountWithText());
		// Converter + set factory
		StringConverter<TreeItem<ReviewableContent>> converter = new CustomTreeStringConverter<ReviewableContent>(mgr);
		fieldTree.setCellFactory(
		        CheckBoxTreeCell.forTreeView(TreeViewUtils.getCheckBoxTreeCellDefaultCallback(), converter));
	}

	@Override
	public void onEnteringPage(Wizard wizard) {
		if (!chapter.isEmpty()) {

			// TODO
		}
	}

	@Override
	public void onExitingPage(Wizard wizard) {
		chapter.clear();
		for (TreeItem<ReviewableContent> item : fieldTree.getSelectionModel().getSelectedItems()) {
			if (item.getValue() instanceof Chapter)
				chapter.add((Chapter) item.getValue());
		}
	}

	/**
	 * @return the chapter
	 */
	protected List<Chapter> getChapter() {
		return chapter;
	}

	/**
	 * @param chapter the chapter to set
	 */
	protected void setChapter(List<Chapter> chapter) {
		this.chapter = chapter;
	}

}