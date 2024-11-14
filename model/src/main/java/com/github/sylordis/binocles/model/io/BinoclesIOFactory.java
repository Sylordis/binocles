package com.github.sylordis.binocles.model.io;

import java.util.Map;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.utils.io.IOFactory;

/**
 * IOFactory for Binocles.
 */
public class BinoclesIOFactory extends IOFactory<BinoclesModel> {

	/**
	 * Input/Output operations.
	 */
	public class IOOperation {
		public static final int OPEN = 0x00001;
		public static final int SAVE = 0x00010;
		public static final int IMPORT = 0x00100;
		public static final int EXPORT_STRUCTURAL = 0x01000;
		public static final int EXPORT_RENDER = 0x10000;
		public static final int ALL = 0x11111;
	}

	/**
	 * Official extension for save files.
	 */
	public static final String SAVE_FILE_EXTENSION = "bino";
	/**
	 * Official extension for save files to be used in a File Chooser.
	 */
	public static final String SAVE_FILE_EXTENSION_FILE_CHOOSER = "*." + SAVE_FILE_EXTENSION;

	/**
	 * Official description for save files.
	 */
	public static final String SAVE_FILE_DESCRIPTION = "Binocles file";

	/**
	 * File extensions for structural exports, with dictionary "Extension => Description"
	 */
	public static Map<String, String[]> EXPORT_STRUCTURAL_FORMATS_EXTENSIONS = Map.of( //
	        "YAML file", new String[] { "*.yaml" });

	@Override
	protected void initImporters() {
		getImporters().put(SAVE_FILE_EXTENSION, BinoFileImporter.class);
		getImporters().put("yaml", YamlFileImporter.class);
	}

	@Override
	protected void initExporters() {
		getExporters().put(SAVE_FILE_EXTENSION, BinoFileExporter.class);
		getExporters().put("yaml", YamlFileExporter.class);
	}

}
