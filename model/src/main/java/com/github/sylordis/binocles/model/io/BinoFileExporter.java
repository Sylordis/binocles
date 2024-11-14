package com.github.sylordis.binocles.model.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.utils.exceptions.ExportException;
import com.github.sylordis.binocles.utils.io.FileExporter;

public class BinoFileExporter implements FileExporter<BinoclesModel> {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	@Override
	public void export(BinoclesModel model, File file) throws ExportException, IOException {
		logger.debug("Export model to '{}'", file);
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(model);
            logger.info("Model saved to '{}", file);
        }
	}

}
