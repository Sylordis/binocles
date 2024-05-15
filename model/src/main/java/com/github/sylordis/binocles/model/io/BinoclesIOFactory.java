package com.github.sylordis.binocles.model.io;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.utils.io.IOFactory;

/**
 * IOFactory for Binocles.
 */
public class BinoclesIOFactory extends IOFactory<BinoclesModel> {

	@Override
	protected void initImporters() {
		getImporters().put("bino", YamlFileImporter.class);
		getImporters().put("yaml", YamlFileImporter.class);
	}

	@Override
	protected void initExporters() {
		getExporters().put("bino", YamlFileExporter.class);
		getExporters().put("yaml", YamlFileExporter.class);
	}

}
