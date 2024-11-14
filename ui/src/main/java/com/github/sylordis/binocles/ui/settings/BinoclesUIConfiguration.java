package com.github.sylordis.binocles.ui.settings;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.sylordis.binocles.model.BinoclesConfiguration;
import com.github.sylordis.binocles.model.io.BinoclesIOFactory;
import com.github.sylordis.binocles.model.io.BinoclesIOFactory.IOOperation;
import com.github.sylordis.binocles.utils.Flags;

import javafx.stage.FileChooser;

/**
 * Main configuration class for the UI. It uses the same file as
 * {@link BinoclesConfiguration#CFG_FILE}.
 * 
 * @author sylordis
 *
 */
public class BinoclesUIConfiguration {

	/**
	 * Display size.
	 */
	private DisplaySize displaySize;

	/**
	 * Holder for singleton instance.
	 * 
	 * @author sylordis
	 *
	 */
	private static class BinocleConfigurationHolder {
		/**
		 * The singleton instance.
		 */
		private static final BinoclesUIConfiguration instance = new BinoclesUIConfiguration();
	}

	/**
	 * Gets the singleton instance of the configuration.
	 * 
	 * @return
	 */
	public static BinoclesUIConfiguration getInstance() {
		return BinocleConfigurationHolder.instance;
	}

	/**
	 * Creates a new instance.
	 */
	private BinoclesUIConfiguration() {
		displaySize = DisplaySize.MEDIUM;
	}

	/**
	 * Gets all file filters for the software without a wild one.
	 * 
	 * @return
	 */
	public List<FileChooser.ExtensionFilter> getFileFilters() {
		return getFileFilters(IOOperation.ALL, false);
	}

	/**
	 * Gets all file filters for the software.
	 * 
	 * @param addWild Adds a wild option for all extensions.
	 * @return
	 */
	public List<FileChooser.ExtensionFilter> getFileFilters(boolean addWild) {
		return getFileFilters(IOOperation.ALL, addWild);
	}

	/**
	 * Gets the file filters for the software.
	 * 
	 * @param addWild Adds a wild option for all extensions.
	 * @return
	 */
	public List<FileChooser.ExtensionFilter> getFileFilters(int operations, boolean addWild) {
		Set<Map.Entry<String, String[]>> extensions = new HashSet<>();
		if (Flags.any(operations, IOOperation.OPEN, IOOperation.SAVE))
			extensions.add(new AbstractMap.SimpleEntry<String, String[]>(BinoclesIOFactory.SAVE_FILE_DESCRIPTION,
			        new String[] { BinoclesIOFactory.SAVE_FILE_EXTENSION_FILE_CHOOSER }));
		if (Flags.any(operations, IOOperation.IMPORT, IOOperation.EXPORT_STRUCTURAL))
			extensions.addAll(BinoclesIOFactory.EXPORT_STRUCTURAL_FORMATS_EXTENSIONS.entrySet());
		if (addWild)
			extensions.add(new AbstractMap.SimpleEntry<String, String[]>("All files", new String[] { "*.*" }));
		return extensions.stream().map(e -> new FileChooser.ExtensionFilter(e.getKey(), e.getValue())).toList();
	}

	/**
	 * @return the displaySize
	 */
	public DisplaySize getDisplaySize() {
		return displaySize;
	}

	/**
	 * @param displaySize the displaySize to set
	 */
	public void setDisplaySize(DisplaySize displaySize) {
		this.displaySize = displaySize;
	}

}
