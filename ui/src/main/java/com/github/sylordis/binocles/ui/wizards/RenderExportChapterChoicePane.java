package com.github.sylordis.binocles.ui.wizards;

import org.controlsfx.control.CheckTreeView;
import org.controlsfx.dialog.Wizard;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.decorators.BookDecorator;
import com.github.sylordis.binocles.model.decorators.ChapterDecorator;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.model.text.ReviewableContent;
import com.github.sylordis.binocles.ui.components.BookTreeRoot;
import com.github.sylordis.binocles.ui.components.CustomTreeCell;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;

/**
 * Wizard pane to select a chapter.
 */
class RenderExportChapterChoicePane extends AbstractWizardPane {

	private Chapter chapter;
	
	private CheckTreeView<ReviewableContent> fieldTree;
	
	public RenderExportChapterChoicePane(BinoclesModel model, Chapter chapter) {
		super(model);
		this.chapter = chapter;
	}

	@Override
	protected void build() {
		// Create components
		CheckBoxTreeItem<ReviewableContent> root = new CheckBoxTreeItem<>(new BookTreeRoot());
		fieldTree = new CheckTreeView<ReviewableContent>(root);
		for (Book book : getModel().getBooks()) {
			TreeItem<ReviewableContent> bookNode = new TreeItem<>(book);
			fieldTree.getRoot().getChildren().add(bookNode);
			// Add chapters to book
			for (Chapter chapter : book.getChapters())
				bookNode.getChildren().add(new CheckBoxTreeItem<ReviewableContent>(chapter));
		}
		fieldTree.setShowRoot(false);
		Label labelTree = new Label("Select a chapter:");
		// TODO Auto-generated method stub
		// Add components
		addFormFeedback();
		getGridPane().addRow(1, labelTree);
		getGridPane().addRow(2, fieldTree);
		// Set up behaviours
		fieldTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	@Override
	public void onEnteringPage(Wizard wizard) {
		// TODO Auto-generated method stub
		super.onEnteringPage(wizard);
	}

	@Override
	public void onExitingPage(Wizard wizard) {
		// TODO Auto-generated method stub
		super.onExitingPage(wizard);
	}

}