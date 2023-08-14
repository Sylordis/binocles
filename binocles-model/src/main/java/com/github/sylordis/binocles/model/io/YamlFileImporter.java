package com.github.sylordis.binocles.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.exceptions.ImporterException;
import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.utils.Identifiable;
import com.github.sylordis.binocles.utils.MapUtils;
import com.github.sylordis.binocles.utils.yaml.YAMLType;
import com.github.sylordis.binocles.utils.yaml.YAMLUtils;

/**
 * Imports a Yaml structure file.
 * 
 * @author sylordis
 *
 */
public final class YamlFileImporter implements FileImporter<BinoclesModel> {

	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();

	/**
	 * Imports a given YAML file to create all required entries. This method returns a populated model,
	 * that can be used however one sees fit.
	 * 
	 * @param yamlFile File to load
	 * @return a model populated from the file
	 * @throws ImporterException
	 * @throws IOException
	 * @Throws ImporterException
	 */
	public BinoclesModel load(File file) throws ImporterException, IOException {
		logger.info("Loading YAML file {}", file);
		BinoclesModel model = new BinoclesModel();
		InputStream inputStream = new FileInputStream(file);
		Yaml yaml = new Yaml();
		Map<String, Object> data = yaml.load(inputStream);
		if (data.containsKey("binocles")) {
			// Get nomenclatures
			Map<String, Object> root = YAMLUtils.get("binocles", data);
			if (YAMLUtils.checkChildType(root, "nomenclatures", YAMLType.LIST)) {
				List<Nomenclature> nomenclatures = loadNomenclaturesFromYAML(YAMLUtils.list("nomenclatures", root));
				logger.info("Imported {} nomenclatures", nomenclatures.size());
				model.setNomenclatures(nomenclatures);
			}
			// Get books
			if (YAMLUtils.checkChildType(root, "library", YAMLType.LIST)) {
				List<Book> books = loadBooksFromYAML(YAMLUtils.list("library", root));
				logger.info("Imported {} books", books.size());
				model.setBooks(books);
			}
			// TODO check comments consistency:
			// - range compared to chapter range
			// - fields existence & values
		} else {
			String message = "YAML import error: no proper root 'binocles' can be found";
			logger.error(message);
			throw new ImporterException(message);
		}
		return model;
	}

	/**
	 * Load book objects from a YAML list.
	 * 
	 * @param list
	 * @param nomenclatures
	 * @return
	 */
	private List<Book> loadBooksFromYAML(List<Object> list) {
		List<Book> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String title = YAMLUtils.strValue("title", data);
			Book book = new Book(title);
			// Normal fields
			String synopsis = YAMLUtils.strValue("synopsis", data);
			String description = YAMLUtils.strValue("description", data);
			String generalComment = YAMLUtils.strValue("generalcomment", data);
			book.setSynopsis(synopsis);
			book.setDescription(description);
			book.setGeneralComment(generalComment);
			// Metadata
			if (data.containsKey("metadata")) {
				Map<String, String> metadata = MapUtils.convertMap(YAMLUtils.get("metadata", data), Map.Entry::getKey,
				        e -> (String) e.getValue());
				book.setMetadata(metadata);
			}
			// Chapters
			if (YAMLUtils.checkChildType(data, "chapters", YAMLType.LIST)) {
				List<Chapter> chapters = loadChaptersFromYAML(YAMLUtils.list("chapters", data), book);
				book.setChapters(chapters);
			}
			result.add(book);
		}
		return result;
	}

	/**
	 * Load chapters objects from a YAML list.
	 * 
	 * @param list
	 * @param nomenclatures
	 * @return
	 */
	private List<Chapter> loadChaptersFromYAML(List<Object> list, Book book) {
		List<Chapter> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String title = YAMLUtils.strValue("title", data);
			Chapter chapter = new Chapter(title);
			// Normal fields
			String content = YAMLUtils.strValue("content", data);
			String generalComment = YAMLUtils.strValue("generalcomment", data);
			chapter.setText(content);
			chapter.setGeneralComment(generalComment);
			// Comments
			if (YAMLUtils.checkChildType(data, "comments", YAMLType.LIST)) {
				List<Comment> comments = loadCommentsFromYAML(YAMLUtils.list("comments", data), book);
				chapter.setComments(comments);
			}
			result.add(chapter);
		}
		return result;
	}

	/**
	 * Load comments objects from a YAML list.
	 * 
	 * @param list
	 * @param nomenclatures
	 * @return
	 */
	private List<Comment> loadCommentsFromYAML(List<Object> list, Book book) {
		List<Comment> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			// Range
			Map<String, Object> rangeData = YAMLUtils.get("range", data);
			int rangeStart = Integer.parseInt((String) rangeData.get("start"));
			int rangeEnd = Integer.parseInt((String) rangeData.get("end"));
			// Type
			String commentTypeName = Identifiable.formatId(YAMLUtils.strValue("type", data));
			Nomenclature nomenclature = book.getNomenclature();
			CommentType type = null;
			if (null != nomenclature) {
				type = nomenclature.getTypes().stream().filter(t -> t.getId().equals(commentTypeName)).findFirst()
				        .get();
			}
			// Instance
			Comment comment = new Comment(type, rangeStart, rangeEnd);
			// fields
			if (data.containsKey("fields")) {
				Map<String, String> fields = MapUtils.convertMap(YAMLUtils.get("fields", data), Map.Entry::getKey,
				        e -> (String) e.getValue());
				comment.setFields(fields);
			}
		}
		return result;
	}

	/**
	 * Load comment types objects from a YAML list.
	 * 
	 * @param list
	 * @return
	 */
	private List<CommentType> loadCommentTypesFromYAML(List<Object> list) {
		List<CommentType> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String name = YAMLUtils.strValue("name", data);
			String description = YAMLUtils.strValue("description", data);
			CommentType type = new CommentType(name, description);
			// Set fields
			if (data.containsKey("fields")) {
				Map<String, String> fields = MapUtils.convertMap(YAMLUtils.get("fields", data), Map.Entry::getKey,
				        e -> (String) e.getValue());
				type.setFields(fields);
			}
			// Set styles
			if (data.containsKey("styles")) {
				Map<String, String> styles = MapUtils.convertMap(YAMLUtils.get("styles", data), Map.Entry::getKey,
				        e -> (String) e.getValue());
				type.editStyles(styles);
			}
			result.add(type);
		}
		return result;
	}

	/**
	 * Create nomenclature objects from a YAML list.
	 * 
	 * @param list
	 * @return
	 */
	private List<Nomenclature> loadNomenclaturesFromYAML(List<Object> list) {
		List<Nomenclature> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String name = YAMLUtils.strValue("name", data);
			Nomenclature nom = new Nomenclature(name);
			if (YAMLUtils.checkChildType(data, "types", YAMLType.LIST)) {
				List<CommentType> types = loadCommentTypesFromYAML(YAMLUtils.list("types", data));
				nom.setTypes(types);
			}
			result.add(nom);
		}
		return result;
	}

}
