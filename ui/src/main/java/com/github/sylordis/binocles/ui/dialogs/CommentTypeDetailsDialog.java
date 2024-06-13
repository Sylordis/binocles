package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.CommentTypeField;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.components.CustomListCell;
import com.github.sylordis.binocles.ui.components.StyleEditor;
import com.github.sylordis.binocles.ui.doa.CommentTypePropertiesAnswer;
import com.github.sylordis.binocles.ui.listeners.ListenerValidator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Dialog to create a new or edit a book.
 * 
 * @author sylordis
 *
 */
public class CommentTypeDetailsDialog extends AbstractAnswerDialog<CommentTypePropertiesAnswer> {

	/**
	 * Nomenclature to add the comment type to.
	 */
	private Nomenclature nomenclature;

	/**
	 * Combo box for the nomenclature.
	 */
	private ComboBox<Nomenclature> fieldNomenclatureChoice;
	/**
	 * Text field for the comment type name.
	 */
	private TextField fieldName;
	/**
	 * Text field for the comment type description.
	 */
	private TextField fieldDescription;
	/**
	 * The list of metafields.
	 */
	private TableView<CommentTypeField> fieldMetafields;
	/**
	 * Data for the metafields table.
	 */
	private ObservableList<CommentTypeField> fieldMetaFieldsData;
	/**
	 * Button to add a new metafield to the type.
	 */
	private Button fieldMetaFieldsControlsAdd;
	/**
	 * Button to remove selected metafields of the type.
	 */
	private Button fieldMetaFieldsControlsDelete;
	/**
	 * Text field to specify the field's name.
	 */
	private TextField fieldMetaFieldsControlsName;
	/**
	 * Text field to specify the field's description.
	 */
	private TextField fieldMetaFieldsControlsDescription;
	/**
	 * Style editor for the comments of this type.
	 */
	private StyleEditor fieldStyle;

	/**
	 * User feedback.
	 */
	private Text formFeedback;

	// Form validity controls and feedback

	private boolean nomenclatureChoiceValid = false;
	private String nomenclatureChoiceFeedback = "";
	private boolean nameValid = false;
	private String nameFeedback = "";
	private boolean fieldNameValid = false;
	private boolean fieldsValid = false;
	private String fieldsFeedback = "";

	/**
	 * Creates a new book creation dialog.
	 * 
	 * @param model
	 * @param currentNomenclature
	 */
	public CommentTypeDetailsDialog(BinoclesModel model, Nomenclature nomenclature) {
		super("Create a new comment type", model);
		setIcon(AppIcons.ICON_COMMENT_TYPE);
		setHeader("Please fill in the name of the new comment type and the nomenclature it belongs too.");
		this.nomenclature = nomenclature;
	}

	@Override
	public void build() {
		// Nomenclature field
		Label labelNomenclature = new Label("Nomenclature:");
		fieldNomenclatureChoice = new ComboBox<>(FXCollections.observableArrayList(getModel().getNomenclatures(true)));
		// Comment type name field
		Label labelCommentTypeName = new Label("Name:");
		fieldName = new TextField();
		// Comment type name description
		Label labelCommentTypeDescription = new Label("Description (optional):");
		fieldDescription = new TextField();
		// Feedback field
		formFeedback = new Text("");
		formFeedback.getStyleClass().add("text-danger");
		// Metafields field
		fieldMetaFieldsData = FXCollections.observableArrayList();
		fieldMetaFieldsData.add(new CommentTypeField("text", "Body text of the comment"));
		fieldsValid = true; // if metafields are not empty, then it is valid by default
		fieldMetafields = new TableView<>(fieldMetaFieldsData);
		fieldMetafields.setPlaceholder(new Label("No fields set"));
		TableColumn<CommentTypeField, String> tableFieldNameColumn = new TableColumn<>("Name");
		tableFieldNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<CommentTypeField, String> tableFieldDescriptionColumn = new TableColumn<>("Description");
		tableFieldDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		fieldMetafields.getColumns().add(tableFieldNameColumn);
		fieldMetafields.getColumns().add(tableFieldDescriptionColumn);
		Label labelCommentTypeFields = new Label("Fields:");
		// Metafields field top controls
		HBox fieldCommentTypeFieldsControlsTop = new HBox();
		fieldCommentTypeFieldsControlsTop.alignmentProperty().set(Pos.CENTER_RIGHT);
		fieldMetaFieldsControlsDelete = new Button("Delete");
		fieldCommentTypeFieldsControlsTop.getChildren().add(fieldMetaFieldsControlsDelete);
		// Metafields field bottom controls
		fieldMetaFieldsControlsName = new TextField();
		fieldMetaFieldsControlsName.promptTextProperty().set("Field name");
		fieldMetaFieldsControlsDescription = new TextField();
		fieldMetaFieldsControlsDescription.promptTextProperty().set("Field description");
		HBox fieldCommentTypeFieldsControlsBottom = new HBox();
		fieldMetaFieldsControlsAdd = new Button("Add");
		fieldCommentTypeFieldsControlsBottom.alignmentProperty().set(Pos.CENTER_RIGHT);
		fieldCommentTypeFieldsControlsBottom.getChildren().addAll(fieldMetaFieldsControlsName,
		        fieldMetaFieldsControlsDescription, fieldMetaFieldsControlsAdd);
		// Style field
		Label labelCommentTypeStyle = new Label("Comment style:");
		fieldStyle = new StyleEditor();
		// Set dialog components
		getGridPane().addRow(0, formFeedback);
		GridPane.setColumnSpan(formFeedback, GridPane.REMAINING);
		getGridPane().addRow(1, labelNomenclature, fieldNomenclatureChoice);
		getGridPane().addRow(2, labelCommentTypeName, fieldName);
		getGridPane().addRow(3, labelCommentTypeDescription, fieldDescription);
		getGridPane().addRow(4, labelCommentTypeFields, fieldCommentTypeFieldsControlsTop);
		getGridPane().addRow(5, fieldMetafields);
		GridPane.setColumnSpan(fieldMetafields, GridPane.REMAINING);
		getGridPane().addRow(6, fieldCommentTypeFieldsControlsBottom);
		GridPane.setColumnSpan(fieldCommentTypeFieldsControlsBottom, GridPane.REMAINING);
		getGridPane().addRow(7, labelCommentTypeStyle);
		getGridPane().addRow(8, fieldStyle);
		GridPane.setColumnSpan(fieldStyle, GridPane.REMAINING);
		// Set up listeners
		ListenerValidator<String> commentTypeNameUIValidator = new ListenerValidator<String>()
		        .validIf("Comment type name can't be blank or empty.", (o, n) -> !n.isBlank())
		        .validIf(
		                "Comment type with the same name already exists (case insensitive) in the selected nomenclature.",
		                (o, n) -> this.nomenclature == null || !this.nomenclature.hasCommentType(n))
		        .feed(s -> nameFeedback = s).onEither(b -> nameValid = b).andThen(this::postActions);
		fieldName.textProperty().addListener(commentTypeNameUIValidator);
		ListenerValidator<Nomenclature> nomenclatureParentUIValidator = new ListenerValidator<Nomenclature>()
		        .validIf("You have to pick a nomenclature to add this comment type to.", (o, n) -> null != n)
		        .feed(s -> nomenclatureChoiceFeedback = s).onEither(b -> {
			        nomenclatureChoiceValid = b;
			        this.nomenclature = fieldNomenclatureChoice.getSelectionModel().getSelectedItem();
		        }).andThen(this::postActions);
		fieldMetafields.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<CommentTypeField>() {

			@Override
			public void onChanged(Change<? extends CommentTypeField> c) {
				fieldMetaFieldsControlsDelete
				        .setDisable(fieldMetafields.getSelectionModel().getSelectedIndices().isEmpty());
			}
		});
		fieldNomenclatureChoice.valueProperty().addListener(nomenclatureParentUIValidator);
		fieldMetaFieldsControlsAdd.setOnAction(e -> {
			addNewField();
			fieldsFeedback = "";
			fieldsValid = true;
			postActions();
		});
		fieldMetaFieldsControlsDelete.setOnAction(e -> {
			fieldMetaFieldsData.removeAll(fieldMetafields.getSelectionModel().getSelectedItems());
			if (fieldMetaFieldsData.isEmpty()) {
				fieldsFeedback = "You need at least one field.";
				fieldsValid = false;
			} else {
				fieldsFeedback = "";
				fieldsValid = true;
			}
			postActions();
			fieldMetafields.refresh();
		});
		ListenerValidator<String> fieldNameUIValidator = new ListenerValidator<String>()
		        .validIf("Field name can't be blank or empty.", (o, n) -> !n.isBlank())
		        .onEither(b -> fieldNameValid = b)
		        .andThen(() -> fieldMetaFieldsControlsAdd.setDisable(!fieldNameValid));
		fieldMetaFieldsControlsName.textProperty().addListener(fieldNameUIValidator);
		// Set feedback collectors & form validators
		addFeedbackCollector(() -> nameFeedback);
		addFeedbackCollector(() -> nomenclatureChoiceFeedback);
		addFeedbackCollector(() -> fieldsFeedback);
		addFormValidator(() -> nameValid);
		addFormValidator(() -> nomenclatureChoiceValid);
		addFormValidator(() -> fieldsValid);
		// Set up components status
		fieldNomenclatureChoice.setButtonCell(new CustomListCell<Nomenclature>(n -> n.getName()));
		fieldNomenclatureChoice.setCellFactory(p -> {
			return new CustomListCell<Nomenclature>(n -> n.getName());
		});
		if (this.nomenclature != null) {
			fieldNomenclatureChoice.getSelectionModel().select(this.nomenclature);
		} else {
			// Force nomenclature choice validity and feedback if not provided
			nomenclatureParentUIValidator.changed(null, null, null);
		}
		commentTypeNameUIValidator.changed(null, null, fieldName.getText());
		tableFieldNameColumn.setPrefWidth(100);
		tableFieldDescriptionColumn.setPrefWidth(250);
		fieldMetafields.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		fieldMetafields.setFixedCellSize(25);
		fieldMetafields.setPrefHeight(5 * fieldMetafields.getFixedCellSize() + 30);
		fieldMetaFieldsControlsAdd.setDisable(true);
		fieldMetaFieldsControlsDelete.setDisable(true);
		// Focus on book name field
		Platform.runLater(() -> fieldName.requestFocus());
	}

	/**
	 * Adds a new field in the table view, resetting the text fields and the button. If an existing
	 * field has the same name, it will only update its description.
	 */
	public void addNewField() {
		String name = fieldMetaFieldsControlsName.getText();
		String description = fieldMetaFieldsControlsDescription.getText();
		CommentTypeField field = getField(name);
		if (null == field) {
			fieldMetaFieldsData.add(new CommentTypeField(name, description));
		} else {
			field.setDescription(description);
			fieldMetafields.refresh();
		}
		fieldMetaFieldsControlsName.setText("");
		fieldMetaFieldsControlsDescription.setText("");
		fieldMetaFieldsControlsAdd.setDisable(true);
	}

	/**
	 * Gets a comment type field from the list based on the name.
	 * 
	 * @param name
	 * @return the comment type field with the given name or null if it doesn't exist
	 */
	public CommentTypeField getField(String name) {
		CommentTypeField answer = null;
		for (CommentTypeField field : fieldMetaFieldsData) {
			if (field.getName().equals(name)) {
				answer = field;
				break;
			}
		}
		return answer;
	}

	@Override
	public CommentTypePropertiesAnswer convertResult(ButtonType button) {
		CommentTypePropertiesAnswer answer = null;
		if (button.equals(getConfirmButton())) {
			// Create fields map from fields list
			Nomenclature nomenclature = fieldNomenclatureChoice.getSelectionModel().getSelectedItem();
			CommentType commentType = new CommentType(fieldName.getText().trim(), fieldDescription.getText().trim());
			fieldMetaFieldsData.forEach(f -> commentType.setField(f.getName(), f.getDescription()));
			fieldStyle.getCSSStyles().forEach((p, v) -> commentType.setStyle(p.toString(), v));
			answer = new CommentTypePropertiesAnswer(nomenclature, commentType);
		}
		return answer;
	}

	/**
	 * Action to run post validation step.
	 */
	public void postActions() {
		setConfirmButtonDisabledOnValidity();
		combineAndProcessFeedback(formFeedback::setText);
		sizeToScene();
	}

}
