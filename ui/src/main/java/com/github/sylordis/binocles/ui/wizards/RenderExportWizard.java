package com.github.sylordis.binocles.ui.wizards;

import org.controlsfx.dialog.Wizard.LinearFlow;

import com.github.sylordis.binocles.model.BinoclesModel;

public class RenderExportWizard extends AbstractWizard<Void> {

	private RenderExportChapterChoicePane chapterChoicePane;
	private RenderExportRenderStylePane renderStylePane;
	private RenderExportConclusionPane conclusionPane;

	public RenderExportWizard(String title, BinoclesModel model) {
		super("Render export", model);
	}

	@Override
	protected void build() {
		this.chapterChoicePane = new RenderExportChapterChoicePane(getModel());
		this.renderStylePane = new RenderExportRenderStylePane(getModel());
		this.conclusionPane = new RenderExportConclusionPane(getModel());
		getWizard().setFlow(new LinearFlow(chapterChoicePane, renderStylePane, conclusionPane));
		// TODO
	}

}
