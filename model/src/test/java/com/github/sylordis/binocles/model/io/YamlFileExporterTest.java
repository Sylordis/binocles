package com.github.sylordis.binocles.model.io;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;

import com.github.sylordis.binocles.model.BinoclesConfiguration;
import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.testing.utils.TestFileUtils;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.utils.MapUtils;
import com.github.sylordis.binocles.utils.exceptions.ExportException;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;

/**
 * Tests for the YAML export structure. Since the exporter creates a whole java structure with
 * {@link Map}s and {@link List}s, this test class will primarily focus on testing the structure
 * before writing to the file.
 */
class YamlFileExporterTest {

	private YamlFileExporter exporter;
	@TempDir
	File tempDir;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		exporter = new YamlFileExporter();
	}

	@Test
	void testYamlFileExporter() {
		assertNotNull(exporter);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#export(com.github.sylordis.binocles.model.BinoclesModel, java.io.File)}.
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ExportException
	 */
	@Test
	@Tag("output")
	@Tag("functional")
	@Tag("files")
	void testExport(TestInfo testInfo) throws URISyntaxException, FileNotFoundException, IOException, ExportException {
		final String basename = TestFileUtils.fileBasenameForTest(YamlFileExporterTest.class, testInfo);
		File expectedTemp = File.createTempFile(basename + "-expected_temp.yaml", null, tempDir);
		File expected = File.createTempFile(basename + "-expected.yaml", null, tempDir);
		File result = File.createTempFile(basename + "-result.yaml", null, tempDir);
		TestFileUtils.fillFileWithSamples(YamlFileExporterTest.class, expectedTemp,
		        "samples" + File.separator + basename + ".yaml");
		TestFileUtils.sed(expectedTemp, expected,
		        Map.of("<version>", BinoclesConfiguration.getInstance().getVersion()));
		expectedTemp.delete();
		BinoclesModel model = new BinoclesModel();
		Nomenclature nom1 = new Nomenclature("My Nomenclature");
		Nomenclature nom2 = new Nomenclature("High colour");
		Nomenclature nom3 = new Nomenclature("Empty");
		CommentType type11 = new CommentType("typography", "Typo in the text");
		CommentType type12 = new CommentType("comment", "Typical comment");
		type11.setFields(MapUtils.createVariable(new LinkedHashMap<>(), "value", "Correction value", "reason",
		        "Reason of the typography"));
		type12.setFields(Map.of("text", "Text of the comment"));
		nom1.setTypes(List.of(type11, type12));
		model.setNomenclatures(List.of(nom1, nom2, nom3));
		Book book1 = new Book("The First Book");
		Book book2 = new Book("Another book");
		model.setBooks(List.of(book1, book2));
		book1.setNomenclature(nom3);
		book1.setSynopsis("This is the first book ever");
		Chapter chapter11 = new Chapter("The First Chapter",
		        "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
		Chapter chapter12 = new Chapter("The Second Chapter");
		book1.setChapters(List.of(chapter11, chapter12));
		book2.setNomenclature(nom1);
		book2.setSynopsis("This is the second book");
		exporter.export(model, result);
		assertTrue(Files.exists(result.toPath()));
		assertLinesMatch(Files.readAllLines(expected.toPath()), Files.readAllLines(result.toPath()));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#export(com.github.sylordis.binocles.model.BinoclesModel, java.io.File)}.
	 * 
	 * @throws IOException
	 * @throws ExportException
	 */
	@Test
	@Tag("output")
	@Tag("functional")
	@Tag("files")
	void testExport_Empty(TestInfo testInfo) throws ExportException, IOException {
		File result = new File(tempDir, testInfo.getTestMethod().get().getName() + ".yaml");
		exporter.export(new BinoclesModel(), result);
		List<String> lines = Arrays.asList("binocles:",
		        "  version: " + BinoclesConfiguration.getInstance().getVersion(), "  nomenclatures: []",
		        "  library: []");
		assertAll(() -> assertTrue(Files.exists(result.toPath())),
		        () -> assertLinesMatch(lines, Files.readAllLines(result.toPath())));
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportHeader(java.util.Map)}.
	 */
	@Test
	void testExportHeader() {
		Map<String, Object> root = new HashMap<>();
		exporter.exportHeader(root);
		assertTrue(root.containsKey("version"));
		assertFalse(((String) root.get("version")).isBlank());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportBooks(java.util.List, java.util.Set)}.
	 * @throws UniqueIDException 
	 */
	@Test
	void testExportBooks_OneNoNomenclature() throws UniqueIDException {
		final String title = "The Chrysalids";
		final String synopsis = "Now everyone fights.";
		final String generalComment = "Third opus of the trilogy.";
		Book book1 = new Book(title, synopsis);
		book1.setGeneralComment(generalComment);
		final Map<String, String> metadata = Map.of("author", "John Wyndham");
		book1.setMetadata(metadata);
		List<Book> books = List.of(book1);
		final String chap_title = "Chapter 1";
		final String chap_content = "When I was quite small I would sometimes dream of a city - which was strange because it began before I even knew what a city was. But this city, clustered on the curve of a big blue bay, would come into my mind. I could see the streets, and the buildings that lined them, the waterfront, even boats in the harbour; yet, walking, I had never seen the sea, or a boat ...";
		Chapter chapter1 = new Chapter(chap_title, chap_content);
		book1.addChapter(chapter1);
		List<Object> root = new ArrayList<>();
		exporter.exportBooks(root, books);
		List<Object> expected = List.of(expectedBookMap(title, "", metadata, synopsis, generalComment,
		        List.of(expectedChapterMap(chap_title, chap_content, "", new ArrayList<>()))));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportBooks(java.util.List, java.util.Set)}.
	 */
	@Test
	void testExportBooks_Multiple() {
		final String[] titles = { "The Fellowship of the Ring", "The Two Towers", "The Return of the King" };
		final String[] synopsises = { "Bilbo Baggins celebrates his birthday and leaves the Ring to Frodo, his heir.",
		        " party of Orcs sent by Saruman and Sauron attack the Fellowship.",
		        "Sauron sends a great army against Gondor." };
		final String[] generalComments = { "First of the trilogy", "Second of the trilogy", "Third of the trilogy" };
		final String[] nomenclatureNames = { "Attentive", "Default", "" };
		final Nomenclature[] nomz = { new Nomenclature(nomenclatureNames[0]), new Nomenclature(nomenclatureNames[1]),
		        null };
		final Map<String, String> metadata1 = Map.of("author", "J.R.R. Tolkien", "publication", "1954");
		final Map<String, String> metadata2 = Map.of("author", "J.R.R. Tolkien", "publication", "1954");
		final Map<String, String> metadata3 = Map.of("author", "J.R.R. Tolkien", "publication", "1955");
		Book book1 = new Book(titles[0], synopsises[0]);
		book1.setNomenclature(nomz[0]);
		book1.setMetadata(metadata1);
		book1.setGeneralComment(generalComments[0]);
		Book book2 = new Book(titles[1], synopsises[1]);
		book2.setNomenclature(nomz[1]);
		book2.setMetadata(metadata2);
		book2.setGeneralComment(generalComments[1]);
		Book book3 = new Book(titles[2], synopsises[2]);
		book3.setNomenclature(nomz[2]);
		book3.setMetadata(metadata3);
		book3.setGeneralComment(generalComments[2]);
		List<Book> books = List.of(book1, book2, book3);
		List<Object> root = new ArrayList<>();
		exporter.exportBooks(root, books);
		List<Object> expected = List.of(
		        expectedBookMap(titles[0], nomz[0].getId(), metadata1, synopsises[0], generalComments[0],
		                new ArrayList<>()),
		        expectedBookMap(titles[1], nomz[1].getId(), metadata2, synopsises[1], generalComments[1],
		                new ArrayList<>()),
		        expectedBookMap(titles[2], "", metadata3, synopsises[2], generalComments[2], new ArrayList<>()));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportBooks(java.util.List, java.util.Set)}.
	 */
	@Test
	void testExportBooks_Empty() {
		List<Object> root = new ArrayList<>();
		exporter.exportBooks(root, new ArrayList<>());
		assertTrue(root.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportChapters(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportChapters_One() {
		final String title = "Chapter 1 - The Period";
		final String text = "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of Light, it was the season of Darkness, it was the spring of hope, it was the winter of despair, we had everything before us, we had nothing before us, we were all going direct to Heaven, we were all going direct the other way—in short, the period was so far like the present period, that some of its noisiest authorities insisted on its being received, for good or for evil, in the superlative degree of comparison only.";
		final String generalComment = "Several pairs of contrasting words in the opening lines have been interpreted to illustrate the dichotomous climate of the social disparities between the French bourgeoisie and aristocracy around the time of French Revolution.";
		final String commentType_name = "Opening";
		final String comment_text = "Dickens lived in London. In his book A Tale of Two Cities, based on the French Revolution, we see that he really could not write a tale of two cities. He was a resident of just one city: London.";
		final String comment_text2 = "Sydney Carton and Charles Darnay may bear importantly on Dickens's personal life. The plot hinges on the near-perfect resemblance between";
		Chapter chapter1 = new Chapter(title, text);
		chapter1.setGeneralComment(generalComment);
		List<Chapter> chapters = List.of(chapter1);
		final int[] starts = { 0, 11 };
		final int[] ends = { 10, 100 };
		final CommentType type = new CommentType(commentType_name);
		final Comment comment1 = new Comment(type, starts[0], ends[0]);
		final Comment comment2 = new Comment(type, starts[1], ends[1]);
		final Map<String, String> fields1 = Map.of("content", comment_text);
		final Map<String, String> fields2 = Map.of("content", comment_text2);
		comment1.setFields(fields1);
		comment2.setFields(fields2);
		chapter1.getComments().addAll(Arrays.asList(comment1, comment2));
		List<Object> root = new ArrayList<>();
		exporter.exportChapters(root, chapters);
		List<Object> expected = List.of(expectedChapterMap(title, text, generalComment,
		        List.of(expectedCommentMap(type.getId(), starts[0], ends[0], fields1),
		                expectedCommentMap(type.getId(), starts[1], ends[1], fields2))));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportChapters(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportChapters_Multiple() {
		final String[] titles = { "Chapter: I - The snake that ate the elephant",
		        "Chapter: II - The acquaintance with the little prince.", "Chapter: III - Fallen from the sky." };
		final String[] texts = { """
		        Once when I was six years old I saw a magnificent picture in a book, called
		        True Stories from Nature, about the primeval forest. It was a picture of a boa
		        constrictor in the act of swallowing an animal. Here is a copy of the drawing.
		        		        """, """
		        So I lived my life alone, without anyone that I could really talk to, until I had an
		        accident with my plane in the Desert of Sahara, six years ago. Something
		        was broken in my engine. And as I had with me neither a mechanic nor any
		        passengers, I set myself to attempt the difficult repairs all alone. It was a
		        question of life or death for me: I had scarcely enough drinking water to last a
		        week.""", """
		        It took me a long time to learn where he came from. The little prince, who
		        asked me so many questions, never seemed to hear the ones I asked him. It
		        was from words dropped by chance that, little by little, everything was
		        revealed to me.""" };
		final String[] generalComments = {
		        "The grownups have lost their imagination and ability to see the drawing for the truth it contains in the narrator's eyes—they can only jump to the most obvious conclusion that the hat-shaped creature is a hat. ",
		        "",
		        "If the grownups do not pass the pilot's boa constrictor test—and none of them do—the pilot dismisses their ability to see and appreciate the important things in life, like stars. Instead, grownups are more interested in the tangible, practical realities of everyday life, missing (or dismissing as pointless) the beauty and wonder. " };
		Chapter chapter1 = new Chapter(titles[0], texts[0]);
		chapter1.setGeneralComment(generalComments[0]);
		Chapter chapter2 = new Chapter(titles[1], texts[1]);
		chapter2.setGeneralComment(generalComments[1]);
		Chapter chapter3 = new Chapter(titles[2], texts[2]);
		chapter3.setGeneralComment(generalComments[2]);
		List<Chapter> chapters = List.of(chapter1, chapter2, chapter3);
		List<Object> root = new ArrayList<>();
		exporter.exportChapters(root, chapters);
		List<Object> expected = List.of(expectedChapterMap(titles[0], texts[0], generalComments[0], new ArrayList<>()),
		        expectedChapterMap(titles[1], texts[1], generalComments[1], new ArrayList<>()),
		        expectedChapterMap(titles[2], texts[2], generalComments[2], new ArrayList<>()));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportChapters(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportChapters_Empty() {
		List<Object> root = new ArrayList<>();
		exporter.exportChapters(root, new ArrayList<>());
		assertTrue(root.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportComments(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportComments_OneNoType() {
		final Map<String, String> fields = Map.of("content", "This is amazing", "Type", "Metal");
		Comment comment = new Comment(null, 1, 34);
		comment.setFields(fields);
		List<Comment> comments = List.of(comment);
		List<Object> root = new ArrayList<>();
		exporter.exportComments(root, comments);
		List<Object> expected = List.of(expectedCommentMap("", 1, 34, fields));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportComments(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportComments_Multiple() {
		final Map<String, String> fields1 = Map.of("Content", "bubblebuh", "REASON", "kwak");
		final Map<String, String> fields2 = Map.of("content", "This is not right", "correction", "abc not cba");
		final Map<String, String> fields3 = Map.of("content", "I think there's a mistake here");
		final Map<String, String> fields4 = Map.of("Is", "", "this", "", "real", "no");
		final String[] typesNames = { "Single line", "Multiline" };
		final int[] starts = { 0, 1, 200, 30 };
		final int[] ends = { 1, 30, 400, 40 };
		CommentType type1 = new CommentType(typesNames[0]);
		CommentType type2 = new CommentType(typesNames[1]);
		Comment comment1 = new Comment(type1, starts[0], ends[0], fields1);
		Comment comment2 = new Comment(type1, starts[1], ends[1], fields2);
		Comment comment3 = new Comment(null, starts[2], ends[2], fields3);
		Comment comment4 = new Comment(type2, starts[3], ends[3], fields4);
		List<Comment> comments = Arrays.asList(comment1, comment2, comment3, comment4);
		List<Object> root = new ArrayList<>();
		exporter.exportComments(root, comments);
		List<Object> expected = List.of(expectedCommentMap(type1.getId(), starts[0], ends[0], fields1),
		        expectedCommentMap(type1.getId(), starts[1], ends[1], fields2),
		        expectedCommentMap("", starts[2], ends[2], fields3),
		        expectedCommentMap(type2.getId(), starts[3], ends[3], fields4));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportComments(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportComments_Empty() {
		List<Object> root = new ArrayList<>();
		exporter.exportComments(root, new ArrayList<>());
		assertTrue(root.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportNomenclatures(java.util.List, java.util.Set)}.
	 */
	@Test
	void testExportNomenclatures_One() {
		final String name = "The Great Nomenclature";
		final String ctName = "My comnment type";
		final String ctDescription = "This is an example of a comment type";
		List<Nomenclature> nomenclatures = new ArrayList<>();
		Nomenclature nom1 = new Nomenclature(name);
		nomenclatures.add(nom1);
		CommentType type1 = new CommentType(ctName, ctDescription);
		final Map<String, String> fields1 = Map.of("Value", "Main content of this comment");
		final Map<String, String> styles1 = Map.of("font-style", "italic");
		type1.setFields(fields1);
		type1.setStyles(styles1);
		nom1.setTypes(List.of(type1));
		List<Object> root = new ArrayList<>();
		exporter.exportNomenclatures(root, nomenclatures);
		List<Object> expected = List.of(expectedNomenclatureMap(name,
		        List.of(expectedCommentTypeMap(ctName, ctDescription, fields1, styles1))));
		assertEquals(expected, root);

	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportNomenclatures(java.util.List, java.util.Set)}.
	 */
	@Test
	void testExportNomenclatures_Multiple() {
		final String[] nom_names = { "Forever After", "The Last Wish" };
		final String[] type_names = { "Directive", "Advisory", "Interpretive" };
		final String[] type_descriptions = { "Rewrite the sentences or gives direction about what to do", "",
		        "Shows an interpretation of the wording" };
		final String[] fields_names = { "What to do instead", "Example", "Advice", "Interpretation" };
		final String[] fields_descriptions = { "Direction to take", "Example of rewriting", "Advice on the content",
		        "How the reader can interpret this" };
		final String[] styles_names = { "font-weight", "color", "text-decoration", "font-style" };
		final String[] styles_descriptions = { "bold", "red", "dashed", "italic" };
		List<Object> root = new ArrayList<>();
		List<Nomenclature> nomenclatures = new ArrayList<>();
		Nomenclature nom1 = new Nomenclature(nom_names[0]);
		Nomenclature nom2 = new Nomenclature(nom_names[1]);
		nomenclatures.add(nom1);
		nomenclatures.add(nom2);
		CommentType type1 = new CommentType(type_names[0], type_descriptions[0]);
		CommentType type2 = new CommentType(type_names[1], type_descriptions[1]);
		CommentType type3 = new CommentType(type_names[2], type_descriptions[2]);
		final Map<String, String> fields1 = Map.of(fields_names[0], fields_descriptions[0], fields_names[1],
		        fields_descriptions[1]);
		final Map<String, String> fields2 = Map.of(fields_names[2], fields_descriptions[2]);
		final Map<String, String> fields3 = Map.of(fields_names[3], fields_descriptions[3]);
		final Map<String, String> styles1 = Map.of(styles_names[0], styles_descriptions[0], styles_names[1],
		        styles_descriptions[1]);
		final Map<String, String> styles2 = Map.of(styles_names[2], styles_descriptions[2]);
		final Map<String, String> styles3 = Map.of(styles_names[3], styles_descriptions[3]);
		type1.setFields(fields1);
		type1.setStyles(styles1);
		type2.setFields(fields2);
		type2.setStyles(styles2);
		type3.setFields(fields3);
		type3.setStyles(styles3);
		nom1.setTypes(List.of(type1));
		nom2.setTypes(List.of(type2, type3));
		exporter.exportNomenclatures(root, nomenclatures);
		List<Object> expected = List.of(
		        expectedNomenclatureMap(nom_names[0],
		                List.of(expectedCommentTypeMap(type_names[0], type_descriptions[0], fields1, styles1))),
		        expectedNomenclatureMap(nom_names[1],
		                List.of(expectedCommentTypeMap(type_names[1], type_descriptions[1], fields2, styles2),
		                        expectedCommentTypeMap(type_names[2], type_descriptions[2], fields3, styles3))));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportNomenclatures(java.util.List, java.util.Set)}.
	 */
	@Test
	void testExportNomenclatures_Empty() {
		List<Object> root = new ArrayList<>();
		exporter.exportNomenclatures(root, new ArrayList<>());
		assertTrue(root.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportCommentTypes(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportCommentTypes_One() {
		final ArrayList<CommentType> types = new ArrayList<>();
		final String name = "My comnment type";
		final String description = "This is an example of a comment type";
		CommentType type1 = new CommentType(name, description);
		final Map<String, String> fields1 = Map.of("Value", "Main content of this comment");
		final Map<String, String> styles1 = Map.of("font-style", "italic");
		type1.setFields(fields1);
		type1.setStyles(styles1);
		types.add(type1);
		List<Object> root = new ArrayList<>();
		exporter.exportCommentTypes(root, types);
		List<Object> expected = List.of(expectedCommentTypeMap(name, description, fields1, styles1));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportCommentTypes(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportCommentTypes_Multiple() {
		final String[] names = { "Directive", "Advisory", "Interpretive" };
		final String[] descriptions = { "Rewrite the sentences or gives direction about what to do", "Gives advices",
		        "Shows an interpretation of the wording" };
		List<Object> root = new ArrayList<>();
		final ArrayList<CommentType> types = new ArrayList<>();
		CommentType type1 = new CommentType(names[0], descriptions[0]);
		CommentType type2 = new CommentType(names[1], descriptions[1]);
		CommentType type3 = new CommentType(names[2], descriptions[2]);
		final Map<String, String> fields1 = Map.of("What to do instead", "Direction to take", "Example",
		        "Example of rewriting");
		final Map<String, String> fields2 = Map.of("Advice", "Advice on the content");
		final Map<String, String> fields3 = Map.of("Interpretation", "How the reader can interpret this");
		final Map<String, String> styles1 = Map.of("font-weight", "bold", "color", "red");
		final Map<String, String> styles2 = Map.of("text-decoration", "dashed");
		final Map<String, String> styles3 = Map.of("font-style", "italic");
		type1.setFields(fields1);
		type1.setStyles(styles1);
		type2.setFields(fields2);
		type2.setStyles(styles2);
		type3.setFields(fields3);
		type3.setStyles(styles3);
		types.add(type1);
		types.add(type2);
		types.add(type3);
		exporter.exportCommentTypes(root, types);
		List<Object> expected = List.of(expectedCommentTypeMap(names[0], descriptions[0], fields1, styles1),
		        expectedCommentTypeMap(names[1], descriptions[1], fields2, styles2),
		        expectedCommentTypeMap(names[2], descriptions[2], fields3, styles3));
		assertEquals(expected, root);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.binocles.model.io.YamlFileExporter#exportCommentTypes(java.util.List, java.util.List)}.
	 */
	@Test
	void testExportCommentTypes_Empty() {
		List<Object> root = new ArrayList<>();
		exporter.exportCommentTypes(root, new ArrayList<>());
		assertTrue(root.isEmpty());
	}

	/**
	 * Defines the hard expectation map for a book object in a normalised fashion.
	 * 
	 * @param title
	 * @param nomenclature
	 * @param metadata
	 * @param synopsis
	 * @param generalComment
	 * @param chapters
	 * @return
	 */
	private Map<String, Object> expectedBookMap(String title, String nomenclature, Map<String, String> metadata,
	        String synopsis, String generalComment, List<Object> chapters) {
		return MapUtils.create(new LinkedHashMap<String, Object>(),
		        new String[] { "title", "nomenclature", "metadata", "synopsis", "globalcomment", "description", "chapters" },
		        new Object[] { title, nomenclature, new TreeMap<>(metadata), synopsis, generalComment, "", chapters });
	}

	/**
	 * Defines the hard expectation map for a chapter object in a normalised fashion.
	 * 
	 * @param title
	 * @param content
	 * @param globalComment
	 * @param comments
	 * @return
	 */
	private Map<String, Object> expectedChapterMap(String title, String content, String globalComment,
	        List<Object> comments) {
		return MapUtils.create(new LinkedHashMap<String, Object>(),
		        new String[] { "title", "content", "globalcomment", "comments" },
		        new Object[] { title, content, globalComment, comments });
	}

	/**
	 * Defines the hard expectation map for a comment object in a normalised fashion.
	 * 
	 * @param type
	 * @param start
	 * @param end
	 * @param fields
	 * @return
	 */
	private Map<String, Object> expectedCommentMap(String type, int start, int end, Map<String, String> fields) {
		Map<String, Object> range = MapUtils.create(new LinkedHashMap<>(), new String[] { "start", "end" },
		        new Integer[] { start, end });
		return MapUtils.create(new LinkedHashMap<String, Object>(), new String[] { "type", "range", "fields" },
		        new Object[] { type, range, fields });
	}

	/**
	 * Defines the hard expectation map for a comment type object in a normalised fashion.
	 * 
	 * @param name
	 * @param description
	 * @param fields
	 * @param styles
	 * @return
	 */
	private Map<String, Object> expectedCommentTypeMap(String name, String description, Map<String, String> fields,
	        Map<String, String> styles) {
		return MapUtils.create(new LinkedHashMap<String, Object>(),
		        new String[] { "name", "description", "fields", "styles" },
		        new Object[] { name, description, fields, styles });
	}

	/**
	 * Defines the hard expectation map for a nomenclature object in a normalised fashion.
	 * 
	 * @param name
	 * @param commentTypes
	 * @return
	 */
	private Map<String, Object> expectedNomenclatureMap(String name, List<Object> commentTypes) {
		return MapUtils.create(new LinkedHashMap<String, Object>(), new String[] { "name", "types" },
		        new Object[] { name, commentTypes });
	}

}
