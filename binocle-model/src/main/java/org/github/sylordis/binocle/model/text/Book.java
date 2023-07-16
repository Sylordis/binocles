package org.github.sylordis.binocle.model.text;

import java.util.ArrayList;
import java.util.List;

import org.github.sylordis.binocle.model.legend.LegendConfiguration;
import org.github.sylordis.binocle.model.review.ReviewableContent;

/**
 * Represents a book that can contain several chapters. Books will be the base entity that is
 * managed by Binocle. Each Chapter will have their own comments but their configuration is managed
 * at the Book level.
 * 
 * @author sylordis
 *
 */
public class Book extends ReviewableContent {

	/**
	 * Title of the book.
	 */
	private String title;
	/**
	 * List of chapters structuring the books. This list cannot be null.
	 */
	private List<Chapter> chapters;
	/**
	 * Book synopsis.
	 */
	private String synopsis;
	/**
	 * Book description, whatever the author wants to put there.
	 */
	private String description;
	/**
	 * Current nomenclature for the review.
	 */
	private LegendConfiguration nomenclature;

	/**
	 * Creates a new Book with a title.
	 * 
	 * @param title
	 */
	public Book(String title) {
		this(title, "", "");
	}

	/**
	 * @param title
	 * @param chapters
	 * @param synopsis
	 * @param description
	 */
	public Book(String title, String synopsis, String description) {
		super();
		this.title = title;
		this.chapters = new ArrayList<>();
		this.synopsis = synopsis;
		this.description = description;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Creates a new empty chapter. The title will be created automatically based on the amount of
	 * already existing chapters.
	 */
	public void createChapter() {
		Chapter chapter = new Chapter("Chapter " + this.chapters.size());
		this.chapters.add(chapter);
	}

	/**
	 * 
	 * @param chapter
	 */
	public void addChapter(Chapter chapter) {
		this.chapters.add(chapter);
	}
	
	/**
	 * @return the chapters
	 */
	public List<Chapter> getChapters() {
		return chapters;
	}

	/**
	 * @param chapters the chapters to set
	 */
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	/**
	 * @return the synopsis
	 */
	public String getSynopsis() {
		return synopsis;
	}

	/**
	 * @param synopsis the synopsis to set
	 */
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the nomenclature.
	 * @return
	 */
	public LegendConfiguration getNomenclature() {
		return nomenclature;
	}

	/**
	 * Replaces the nomenclature the hard way. All existing comments on this entity and its chapters will be assigned the Orphan type.
	 * @param nomenclature
	 */
	public void setNomenclature(LegendConfiguration nomenclature) {
		this.nomenclature = nomenclature;
	}

}
