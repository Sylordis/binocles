package com.github.sylordis.binocles.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FileUtilsTest {

	@ParameterizedTest
	@CsvSource({"/etc/file.yaml,yaml", "/usr/bin/config.json,json", "/usr,", ","})
	void testGetExtension(String input, String expected) {
		File file = null;
		if (input != null)
			file = new File(input);
		assertEquals(expected, FileUtils.getExtension(file));
	}

}
