package com.github.sylordis.binocles.model.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.exceptions.ExporterException;
import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;

/**
 * Exports the model to a Yaml structure file.
 * 
 * @author sylordis
 *
 */
public class YamlFileExporter implements FileExporter {

	private final Logger logger = LogManager.getLogger();

	@Override
	public void export(BinoclesModel model, File file) throws ExporterException, IOException {
		Map<String, Object> binoclesRoot = new LinkedHashMap<>();
		exportHeader(binoclesRoot);
		List<Object> nomenclaturesRoot = new ArrayList<Object>();
		exportNomenclatures(nomenclaturesRoot, model.getNomenclatures());
		binoclesRoot.put("nomenclatures", nomenclaturesRoot);
		List<Object> libraryRoot = new ArrayList<Object>();
		exportBooks(libraryRoot, model.getBooks());
		binoclesRoot.put("library", libraryRoot);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			Map<String, Object> root = Map.of("binocles", binoclesRoot);
			DumperOptions yamlOptions = new DumperOptions();
			yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			Yaml yaml = new Yaml(yamlOptions);
			yaml.dump(root, writer);
		}
	}

	public void exportHeader(Map<String, Object> root) {
		root.put("version", "0.1.0-SNAPSHOT"); // TODO load version number
	}

	public void exportBooks(List<Object> root, Set<Book> books) {
		for (Book book : books) {
			logger.debug("Exporting book {}", book.getId());
			Map<String, Object> bookMap = new LinkedHashMap<>();
			// Book header
			bookMap.put("title", book.getTitle());
			bookMap.put("nomenclature", book.getNomenclature() != null ? book.getNomenclature().getId() : "");
			// Metadata
			Map<String, Object> metadatas = new TreeMap<>();
			metadatas.putAll(book.getMetadata());
			bookMap.put("metadata", metadatas);
			// Text
			bookMap.put("synopsis", book.getSynopsis());
			bookMap.put("globalcomment", book.getGlobalComment());
			// Chapters
			List<Object> chapters = new ArrayList<>();
			bookMap.put("chapters", chapters);
			exportChapters(chapters, book.getChapters());
			root.add(bookMap);
		}
	}

	public void exportChapters(List<Object> root, List<Chapter> chapters) {
		for (Chapter chapter : chapters) {
			logger.debug("Exporting chapter {}", chapter.getId());
			Map<String, Object> chapterMap = new LinkedHashMap<>();
			// Title
			chapterMap.put("title", chapter.getTitle());
			// Content
			chapterMap.put("content", chapter.getText());
			// Comments & Global
			chapterMap.put("globalcomment", chapter.getGlobalComment());
			List<Object> chapterComments = new ArrayList<>();
			exportComments(chapterComments, chapter.getComments());
			chapterMap.put("comments", chapterComments);
			root.add(chapterMap);
		}
	}

	public void exportComments(List<Object> root, List<Comment> comments) {
		logger.debug("Exporting comments");
		for (Comment comment : comments) {
			Map<String, Object> commentMap = new LinkedHashMap<>();
			commentMap.put("type", comment.getType().getId());
			Map<String, Object> rangeMap = new LinkedHashMap<>();
			rangeMap.put("start", comment.getStartIndex());
			rangeMap.put("end", comment.getEndIndex());
			commentMap.put("range", rangeMap);
			Map<String, Object> fieldsMap = new LinkedHashMap<>();
			fieldsMap.putAll(comment.getFields());
			commentMap.put("fields", fieldsMap);
			root.add(commentMap);
		}
	}

	public void exportNomenclatures(List<Object> root, Set<Nomenclature> nomenclatures) {
		for (Nomenclature nomenclature : nomenclatures) {
			logger.debug("Exporting nomenclature {}", nomenclature.getId());
			Map<String, Object> nomenclatureMap = new LinkedHashMap<>();
			nomenclatureMap.put("name", nomenclature.getName());
			List<Object> typesList = new ArrayList<>();
			exportCommentTypes(typesList, nomenclature.getTypes());
			nomenclatureMap.put("types", typesList);
			root.add(nomenclatureMap);
		}
	}

	public void exportCommentTypes(List<Object> root, List<CommentType> commentTypes) {
		logger.debug("Exporting comment types");
		for (CommentType type : commentTypes) {
			Map<String,Object> typeMap = new LinkedHashMap<>();
			typeMap.put("name", type.getName());
			typeMap.put("description", type.getDescription());
			// Fields
			Map<String,Object> fieldsMap = new HashMap<>();
			fieldsMap.putAll(type.getFields());
			typeMap.put("fields", fieldsMap);
			// Styles
			Map<String,Object> stylesMap = new HashMap<>();
			stylesMap.putAll(type.getStyles());
			typeMap.put("styles", stylesMap);
			root.add(typeMap);
		}
	}

}
