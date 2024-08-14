package com.github.sylordis.binocles.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.Comment;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.model.text.Book;
import com.github.sylordis.binocles.model.text.Chapter;
import com.github.sylordis.binocles.utils.Identifiable;
import com.github.sylordis.binocles.utils.MapUtils;
import com.github.sylordis.binocles.utils.exceptions.ImportException;
import com.github.sylordis.binocles.utils.exceptions.UniqueIDException;
import com.github.sylordis.binocles.utils.io.FileImporter;
import com.github.sylordis.binocles.utils.yaml.YAMLType;
import com.github.sylordis.binocles.utils.yaml.YAMLUtils;

/**
 * Imports a Yaml structure file.
 * 
 * @author sylordis
 *
 */
public final class YamlFileImporter implements FileImporter<BinoclesModel> {

	// TODO Import YAML keys in proper order
	
	/**
	 * Class logger.
	 */
	private final Logger logger = LogManager.getLogger();
	/**
	 * Constant for storing the nomenclature id temporarily until proper linking.
	 */
	public final static String NOMENCLATURE_BINOCLES_KEY = "_binocles_nomenclature";

	private BinoclesModel model;

	/**
	 * Imports a given YAML file to create all required entries. This method returns a populated model,
	 * that can be used however one sees fit.
	 * 
	 * @param yamlFile File to load
	 * @return a model populated from the file
	 * @throws ImportException
	 * @throws IOException
	 * @Throws ImporterException
	 */
	public BinoclesModel load(File file) throws ImportException, IOException {
		logger.info("Loading YAML file {}", file);
		model = new BinoclesModel();
		try {
			InputStream inputStream = new FileInputStream(file);
			Yaml yaml = new Yaml();
			Map<String, Object> data = yaml.load(inputStream);
			if (data.containsKey("binocles")) {
				// Get nomenclatures
				Map<String, Object> root = YAMLUtils.get("binocles", data);
				if (YAMLUtils.checkChildType(root, "nomenclatures", YAMLType.LIST)) {
					List<Nomenclature> nomenclatures = loadNomenclaturesFromYAML(YAMLUtils.list("nomenclatures", root));
					logger.info("Imported {} nomenclatures", nomenclatures.size());
					try {
						model.setNomenclaturesUnique(nomenclatures);
					} catch (UniqueIDException e) {
						// TODO To not interrupt the import but resolve import conflicts at the end
						throw new ImportException(e);
					}
				}
				// Get books
				if (YAMLUtils.checkChildType(root, "library", YAMLType.LIST)) {
					List<Book> books = loadBooksFromYAML(YAMLUtils.list("library", root));
					logger.info("Imported {} books", books.size());
					try {
						model.setBooksUnique(books);
					} catch (UniqueIDException e) {
						// TODO To not interrupt the import but resolve import conflicts at the end
						throw new ImportException(e);
					}
				}
				// TODO check comments consistency:
				// - range compared to chapter range
				// - fields existence & values
			} else {
				String message = "YAML import error: no proper root 'binocles' can be found";
				logger.error(message);
				throw new ImportException(message);
			}
		} catch (ParserException e) {
			throw new ImportException(e);
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
	public List<Book> loadBooksFromYAML(List<Object> list) {
		List<Book> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String title = YAMLUtils.strValue("title", data);
			Book book = new Book(title);
			// Normal fields
			String synopsis = YAMLUtils.strValue("synopsis", data);
			String generalComment = YAMLUtils.strValue("globalcomment", data);
			String description = YAMLUtils.strValue("description", data);
			book.setSynopsis(synopsis);
			book.setGeneralComment(generalComment);
			book.setDescription(description);
			logger.info("Created book '{}'", book.getTitle());
			Map<String, String> metadata = new HashMap<>();
			// Nomenclature, store in metadata for later
			String nomenclature = YAMLUtils.strValue("nomenclature", data);
			if (nomenclature != null && !nomenclature.isEmpty()) {
				book.setNomenclature(model.getNomenclature(YAMLUtils.strValue("nomenclature", data)));
				logger.info("'{}' linked with nomenclature '{}'", book.getTitle(), book.getNomenclature().getName());
			} else {
				book.setNomenclature(model.getDefaultNomenclature());
			}
			// Metadata
			if (data.containsKey("metadata")) {
				metadata.putAll(MapUtils.convertMap(YAMLUtils.get("metadata", data), Map.Entry::getKey,
				        this::convertValueType));
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
	 * Converts the value of an entry to a string.
	 * 
	 * @param e
	 * @return
	 */
	public String convertValueType(Entry<String, Object> e) {
		Class<?> valueType = e.getValue().getClass();
		String returnValue = null;
		if (valueType.equals(Date.class)) {
			returnValue = new SimpleDateFormat("yyyy-MM-dd").format((Date) e.getValue());
		} else if (Number.class.isAssignableFrom(valueType)) {
			returnValue = "" + e.getValue();
		} else {
			returnValue = (String) e.getValue();
		}
		return returnValue;
	}

	/**
	 * Load chapters objects from a YAML list.
	 * 
	 * @param list
	 * @param nomenclatures
	 * @return
	 */
	public List<Chapter> loadChaptersFromYAML(List<Object> list, Book book) {
		List<Chapter> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String title = YAMLUtils.strValue("title", data);
			// Normal fields
			String content = YAMLUtils.strValue("content", data);
			String generalComment = YAMLUtils.strValue("generalcomment", data);
			Chapter chapter = new Chapter(title, content);
			chapter.setGeneralComment(generalComment);
			// Comments
			if (YAMLUtils.checkChildType(data, "comments", YAMLType.LIST)) {
				List<Comment> comments = loadCommentsFromYAML(YAMLUtils.list("comments", data), book);
				chapter.setComments(comments);
				logger.debug("{} comments loaded", comments.size());
			}
			logger.info("Imported chapter '{}'", chapter.getTitle());
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
	public List<Comment> loadCommentsFromYAML(List<Object> list, Book book) {
		List<Comment> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			// Range
			Map<String, Object> rangeData = YAMLUtils.get("range", data);
			int rangeStart = (int) rangeData.get("start");
			int rangeEnd = (int) rangeData.get("end");
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
				        e -> convertValueType(e));
				comment.setFields(fields);
			}
			result.add(comment);
		}
		return result;
	}

	/**
	 * Load comment types objects from a YAML list.
	 * 
	 * @param list
	 * @return
	 */
	public List<CommentType> loadCommentTypesFromYAML(List<Object> list) {
		List<CommentType> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String name = YAMLUtils.strValue("name", data);
			String description = YAMLUtils.strValue("description", data);
			CommentType type = new CommentType(name, description);
			// Set fields
			if (data.containsKey("fields")) {
				Map<String, String> fields = MapUtils.convertMap(YAMLUtils.get("fields", data), Map.Entry::getKey,
				        e -> convertValueType(e));
				logger.debug("Fields found: {}", fields);
				type.setFields(fields);
			}
			if (data.containsKey("large")) {
				logger.debug("Setting large fields");
				YAMLUtils.list("large", data).forEach(l -> type.getFields().get(l.toString()).setIsLongText(true));
			}
			// Set styles
			if (data.containsKey("styles")) {
				Map<String, String> styles = MapUtils.convertMap(YAMLUtils.get("styles", data), Map.Entry::getKey,
				        e -> convertValueType(e));
				logger.debug("Style found: {}", styles);
				type.editStyles(styles);
			}
			result.add(type);
			logger.info("Imported comment type '{}', fields: {}", type.getName(), type.getFields().keySet());
		}
		return result;
	}

	/**
	 * Create nomenclature objects from a YAML list.
	 * 
	 * @param list
	 * @return
	 */
	public List<Nomenclature> loadNomenclaturesFromYAML(List<Object> list) {
		List<Nomenclature> result = new ArrayList<>();
		for (Object o : list) {
			Map<String, Object> data = YAMLUtils.toNode(o);
			String name = YAMLUtils.strValue("name", data);
			Nomenclature nom = new Nomenclature(name);
			logger.info("Created nomenclature '{}'", nom.getName());
			if (YAMLUtils.checkChildType(data, "types", YAMLType.LIST)) {
				List<CommentType> types = loadCommentTypesFromYAML(YAMLUtils.list("types", data));
				nom.setTypes(types);
			}
			result.add(nom);
		}
		return result;
	}

}
