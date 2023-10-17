package com.github.sylordis.binocles.utils;

import java.io.File;

public final class FileUtils {

	// Private constructor to prevent instantiation
	private FileUtils() {
		// Nothing to do here
	}

	/**
	 * Returns the extension of a file or null if it doesn't have any or if the file is null
	 * 
	 * @param file The file to get the extension from
	 * @return
	 */
	public static String getExtension(File file) {
		String ext = null;
		if (file != null) {
			int extPos = file.getName().indexOf(".");
			if (extPos >= 0) {
				ext = file.getName().substring(extPos + 1).toLowerCase();
			}
		}
		return ext;
	}
}
