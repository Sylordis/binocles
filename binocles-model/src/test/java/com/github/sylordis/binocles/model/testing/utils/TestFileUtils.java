package com.github.sylordis.binocles.model.testing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.junit.jupiter.api.TestInfo;

public class TestFileUtils {

	/**
	 * Creates the path for a file.
	 * 
	 * @param testClass
	 * @param info
	 * 
	 * @return
	 */
	public static String fileBasenameForTest(Class<?> testClass, TestInfo info) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(testClass.getSimpleName()).append("_").append(info.getTestMethod().get().getName());
		return sbuilder.toString();
	}

	/**
	 * Fills a file with all bytes from a sample stream.
	 * 
	 * @param testClass
	 * @param file
	 * @param samplesStream
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void fillFileWithSamples(Class<?> testClass, File file, String samplesStream)
	        throws IOException, FileNotFoundException {
		try (OutputStream outstream = new FileOutputStream(file);
		        InputStream instream = testClass.getClassLoader().getResourceAsStream(samplesStream)) {
			outstream.write(instream.readAllBytes());
		}
	}

	/**
	 * Fills a target file with the source content, to which string regex replacements were applied to
	 * each line.
	 * 
	 * @param source
	 * @param target
	 * @param replacements pattern => replacements
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void sed(File source, File target, Map<String, String> replacements)
	        throws IOException, FileNotFoundException {
		try (FileWriter fileWriter = new FileWriter(target);
		        BufferedReader fileReader = new BufferedReader(new FileReader(source))) {
			String line = null;
			while ((line = fileReader.readLine()) != null) {
				for (Map.Entry<String, String> entry : replacements.entrySet()) {
					line = line.replaceAll(entry.getKey(), entry.getValue());
				}
				fileWriter.write(line);
				fileWriter.write("\n");
			}
		}
	}

}
