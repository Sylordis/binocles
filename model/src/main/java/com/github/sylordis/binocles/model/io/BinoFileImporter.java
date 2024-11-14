package com.github.sylordis.binocles.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.utils.exceptions.ImportException;
import com.github.sylordis.binocles.utils.io.FileImporter;

/**
 * Imports specific binocles files.
 * 
 * @author sylordis
 */
public class BinoFileImporter implements FileImporter<BinoclesModel> {

	@Override
	public BinoclesModel load(File file) throws ImportException, IOException {
		BinoclesModel model = null;
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
			model = (BinoclesModel) inputStream.readObject();
		} catch (ClassNotFoundException e) {
			throw new ImportException("File is unreadable or is corrupted", e);
		}
		return model;
	}

}
