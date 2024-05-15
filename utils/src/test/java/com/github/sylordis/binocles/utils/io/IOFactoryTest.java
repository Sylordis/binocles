package com.github.sylordis.binocles.utils.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.sylordis.binocles.utils.exceptions.ExportException;
import com.github.sylordis.binocles.utils.exceptions.ImportException;

/**
 * 
 */
class IOFactoryTest {

	/**
	 * Object under test.
	 */
	private IOFactory<Object> factory;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		factory = new IOFactoryImpl();
	}

	@Test
	void testIOFactory() {
		assertNotNull(factory);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)}.
	 * 
	 * @throws ImportException
	 */
	@Test
	void testGetFileImporter() throws ImportException {
		factory.getImporters().put("xml", XMLImporter.class);
		FileImporter<Object> importer = factory.getFileImporter(new File("testy.xml"));
		assertNotNull(importer);
		assertEquals(XMLImporter.class, importer.getClass());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)} when
	 * there's no importer available for the file type.
	 * 
	 * @throws ExportException
	 */
	@Test
	void testGetFileImporter_NotAvailable() throws ImportException {
		assertThrows(ImportException.class, () -> factory.getFileImporter(new File("testy.xml")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)} when the
	 * file has no extension.
	 * 
	 * @throws ExportException
	 */
	@Test
	void testGetFileImporter_NoExtension() throws ImportException {
		assertNull(factory.getFileImporter(new File("testy")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)} when the
	 * file is null.
	 * 
	 * @throws ExportException
	 */
	@Test
	void testGetFileImporter_Null() throws ImportException {
		assertNull(factory.getFileImporter(null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)} when the
	 * call triggers an exception.
	 */
	@Test
	void testGetFileImporter_NoSuchMethodException() throws ImportException {
		factory.getImporters().put("nsm", NoSuchMethodImporter.class);
		assertThrows(ImportException.class, () -> factory.getFileImporter(new File("hello.nsm")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)} when the
	 * call triggers an exception.
	 */
	@Test
	void testGetFileImporter_IllegalAccessException() throws ImportException {
		factory.getImporters().put("ia", IllegalAccessImporter.class);
		assertThrows(ImportException.class, () -> factory.getFileImporter(new File("bad.ia")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileExporter(java.io.File)}.
	 */
	@Test
	void testGetFileExporter() throws ExportException {
		factory.getExporters().put("abc", ABCExporter.class);
		FileExporter<Object> exporter = factory.getFileExporter(new File("testy.abc"));
		assertNotNull(exporter);
		assertEquals(ABCExporter.class, exporter.getClass());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileExporter(java.io.File)}.
	 * 
	 * @throws ExportException
	 */
	@Test
	void testGetFileExporter_NotAvailable() throws ExportException {
		assertThrows(ExportException.class, () -> factory.getFileExporter(new File("myfile.abcd")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileExporter(java.io.File)} when the
	 * file is null.
	 * 
	 * @throws ExportException
	 */
	@Test
	void testGetFileExporter_Null() throws ExportException {
		assertNull(factory.getFileExporter(null));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)} when the
	 * call triggers an exception.
	 */
	@Test
	void testGetFileExporter_NoSuchMethodException() throws ImportException {
		factory.getExporters().put("nsm", NoSuchMethodExporter.class);
		assertThrows(ExportException.class, () -> factory.getFileExporter(new File("export.nsm")));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.utils.io.IOFactory#getFileImporter(java.io.File)} when the
	 * call triggers an exception.
	 */
	@Test
	void testGetFileExporter_IllegalAccessException() throws ImportException {
		factory.getExporters().put("ia", IllegalAccessExporter.class);
		assertThrows(ExportException.class, () -> factory.getFileExporter(new File("bad.ia")));
	}

	@Test
	void testGetImporters() {
		assertNotNull(factory.getImporters());
		assertTrue(factory.getImporters().isEmpty());
	}

	@Test
	void testGetExporters() {
		assertNotNull(factory.getExporters());
		assertTrue(factory.getExporters().isEmpty());
	}

	/**
	 * Test class
	 */
	class IOFactoryImpl extends IOFactory<Object> {

		@Override
		protected void initImporters() {
			// Nothing
		}

		@Override
		protected void initExporters() {
			// Nothing
		}

	}

	/**
	 * Test class for importer.
	 */
	public static class XMLImporter implements FileImporter<Object> {

		public XMLImporter() {

		}

		@Override
		public Object load(File file) throws ImportException, IOException {
			return null;
		}

	}

	/**
	 * Test class for exporters.
	 */
	public static class ABCExporter implements FileExporter<Object> {

		@Override
		public void export(Object o, File file) throws ExportException, IOException {
			// Nothing to do
		}
	}

	class NoSuchMethodImporter implements FileImporter<Object> {
		
		@Override
		public Object load(File file) throws ImportException, IOException {
			return null;
		}
		
	}
	
	public static class IllegalAccessImporter implements FileImporter<Object> {

		private IllegalAccessImporter() {}
		
		@Override
		public Object load(File file) throws ImportException, IOException {
			return null;
		}
		
	}

	class NoSuchMethodExporter implements FileExporter<Object> {

		@Override
		public void export(Object o, File file) throws ExportException, IOException {
			// Nothing to do
		}
		
	}
	
	public static class IllegalAccessExporter implements FileExporter<Object> {

		private IllegalAccessExporter() {}

		@Override
		public void export(Object o, File file) throws ExportException, IOException {
			// Nothing to do			
		}
		
	}
	
}
