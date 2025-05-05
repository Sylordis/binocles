package com.github.sylordis.binocles.ui.wizards;

import org.controlsfx.dialog.Wizard.LinearFlow;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.text.Chapter;

/**
 * Wizard for render export.
 */
public class RenderExportWizard extends AbstractWizard<Void> {

	/**
	 * Wizard pane for chapter choice.
	 */
	private RenderExportChapterChoicePane chapterChoicePane;
	/**
	 * Wizard pane for render style choice.
	 */
	private RenderExportRenderStylePane renderStylePane;
	/**
	 * Wizard pane for conclusion.
	 */
	private RenderExportConclusionPane conclusionPane;
	/**
	 * Chapter under use.
	 */
	private Chapter chapter;

	/**
	 * Builds a new render export wizard.
	 * 
	 * @param model   Model to get data from
	 * @param chapter Chapter that might have been preselected
	 * @param owner   Owner of the wizard
	 */
	public RenderExportWizard(Object owner, BinoclesModel model, Chapter chapter) {
		super(owner, "Render export", model);
		this.chapter = chapter;
	}

	@Override
	protected void build() {
		this.chapterChoicePane = new RenderExportChapterChoicePane(getModel(), chapter);
		this.renderStylePane = new RenderExportRenderStylePane(getModel());
		this.conclusionPane = new RenderExportConclusionPane(getModel());
		addPanes(chapterChoicePane, renderStylePane, conclusionPane);
		getWizard().setFlow(new LinearFlow(chapterChoicePane, renderStylePane, conclusionPane));
	}

}
