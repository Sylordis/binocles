package com.github.sylordis.binocles.ui.dialogs;

import com.github.sylordis.binocles.model.BinoclesModel;
import com.github.sylordis.binocles.model.review.CommentType;
import com.github.sylordis.binocles.model.review.CommentTypeField;
import com.github.sylordis.binocles.model.review.Nomenclature;
import com.github.sylordis.binocles.ui.AppIcons;
import com.github.sylordis.binocles.ui.components.CustomListCell;
import com.github.sylordis.binocles.ui.components.StyleEditor;
import com.github.sylordis.binocles.ui.doa.CommentTypePropertiesAnswer;
import com.github.sylordis.binocles.ui.functional.ListenerValidator;
import com.github.sylordis.binocles.utils.contracts.Identifiable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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
	 * Current item being edited.
	 */
	private CommentType commentType;

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
	 * Checkbox field to specify if the field is a long one.
	 */
	private CheckBox fieldMetaFieldsControlsIsLong;
	/**
	 * Style editor for the comments of this type.
	 */
	private StyleEditor fieldStyle;

	// Form validity controls and feedback

	private boolean nomenclatureChoiceValid = false;
	private String nomenclatureChoiceFeedback = "";
	private boolean nameValid = false;
	private String nameFeedback = "";
	private boolean fieldNameValid = false;
	private boolean fieldsValid = false;
	private String fieldsFeedback = "";

	/**
	 * Creates a new comment type creation dialog.
	 * 
	 * @param model        current model being edited for other data collection
	 * @param nomenclature possible current parent
	 */
	public CommentTypeDetailsDialog(BinoclesModel model, Nomenclature nomenclature) {
		this(model, nomenclature, null, "Create a new comment type");
	}

	/**
	 * Creates a new comment type edition dialog.
	 * 
	 * @param model        current model being edited for other data collection
	 * @param nomenclature current parent
	 * @param type         comment type being edited
	 */
	public CommentTypeDetailsDialog(BinoclesModel model, Nomenclature nomenclature, CommentType type) {
		this(model, nomenclature, type, "Editing comment type '" + type.getName() + "'");
	}

	/**
	 * Creates a new comment type edition/creation dialog.
	 * 
	 * @param model        current model being edited for other data collection
	 * @param nomenclature current parent
	 * @param type         comment type being edited
	 * @param title        title of the dialog
	 */
	private CommentTypeDetailsDialog(BinoclesModel model, Nomenclature nomenclature, CommentType type, String title) {
		super(title, model);
		setIcon(AppIcons.ICON_COMMENT_TYPE);
		setHeader("Please fill in the name of the comment type in the chosen nomenclature.");
		this.nomenclature = nomenclature;
		this.commentType = type;
	}

	@Override
	public void build() {
		// Nomenclature field
		Label labelNomenclature = new Label("Nomenclature");
		fieldNomenclatureChoice = new ComboBox<>(FXCollections.observableArrayList(getModel().getNomenclatures(true)));
		// Comment type name field
		Label labelCommentTypeName = new Label("Name");
		fieldName = new TextField();
		// Comment type name description
		Label labelCommentTypeDescription = new Label("Description (optional)");
		fieldDescription = new TextField();
		// Metafields field
		fieldMetaFieldsData = FXCollections.observableArrayList();
		fieldMetaFieldsData.add(new CommentTypeField("text", "Body text of the comment"));
		fieldsValid = !fieldMetaFieldsData.isEmpty(); // if metafields are not empty, then it is valid by default
		fieldMetafields = new TableView<>(fieldMetaFieldsData);
		fieldMetafields.setPlaceholder(new Label("No fields set"));
		TableColumn<CommentTypeField, String> tableFieldNameColumn = new TableColumn<>("Name");
		tableFieldNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//		tableFieldNameColumn.setCellFactory(c -> EditCell.createStringEditCell());
		TableColumn<CommentTypeField, String> tableFieldDescriptionColumn = new TableColumn<>("Description");
		tableFieldDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
//		tableFieldDescriptionColumn.setCellFactory(c -> EditCell.createStringEditCell());
		TableColumn<CommentTypeField, String> tableFieldIsLongColumn = new TableColumn<>("Long?");
		tableFieldIsLongColumn.setCellValueFactory(new PropertyValueFactory<>("isLongText"));
//		tableFieldIsLongColumn.setCellFactory(CheckBoxTableCell
//		        .forTableColumn(i -> new SimpleBooleanProperty(fieldMetaFieldsData.get(i).getIsLongText()), false));
		fieldMetafields.getColumns().add(tableFieldNameColumn);
		fieldMetafields.getColumns().add(tableFieldDescriptionColumn);
		fieldMetafields.getColumns().add(tableFieldIsLongColumn);
//		fieldMetafields.setEditable(true);
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
		fieldMetaFieldsControlsIsLong = new CheckBox("Long?");
		HBox fieldCommentTypeFieldsControlsBottom = new HBox();
		fieldCommentTypeFieldsControlsBottom.getStyleClass().add("controls");
		fieldMetaFieldsControlsAdd = new Button("Add");
		fieldCommentTypeFieldsControlsBottom.alignmentProperty().set(Pos.CENTER_RIGHT);
		fieldCommentTypeFieldsControlsBottom.getChildren().addAll(fieldMetaFieldsControlsName,
		        fieldMetaFieldsControlsDescription, fieldMetaFieldsControlsIsLong, new Separator(Orientation.VERTICAL),
		        fieldMetaFieldsControlsAdd);
		// Style field
		Label labelCommentTypeStyle = new Label("Comment style:");
		fieldStyle = new StyleEditor();
		// Set dialog components
		if (commentType != null) {
			fieldNomenclatureChoice.getSelectionModel().select(nomenclature);
			fieldNomenclatureChoice.setDisable(true);
			fieldName.setText(commentType.getName());
			fieldDescription.setText(commentType.getDescription());
			fieldMetaFieldsData.clear();
			fieldMetaFieldsData.addAll(commentType.getFields().values());
			fieldStyle.loadStringStyles(commentType.getStyles());
		}
		addFormFeedback();
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
		                (o, n) -> Identifiable.checkNewNameUniquenessValidityAmongParent(n, nomenclature, commentType,
		                        (nom, s) -> nom.hasCommentType(s)))
		        .feed(s -> nameFeedback = s).onEither(b -> nameValid = b).andThen(this::updateFormStatus);
		fieldName.textProperty().addListener(commentTypeNameUIValidator);
		ListenerValidator<Nomenclature> nomenclatureParentUIValidator = new ListenerValidator<Nomenclature>()
		        .validIf("You have to pick a nomenclature to add this comment type to.", (o, n) -> null != n)
		        .feed(s -> nomenclatureChoiceFeedback = s).onEither(b -> {
			        nomenclatureChoiceValid = b;
			        this.nomenclature = fieldNomenclatureChoice.getSelectionModel().getSelectedItem();
		        }).andThen(this::updateFormStatus);
		fieldMetafields.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<CommentTypeField>() {

			@Override
			public void onChanged(Change<? extends CommentTypeField> c) {
				fieldMetaFieldsControlsDelete
				        .setDisable(fieldMetafields.getSelectionModel().getSelectedIndices().isEmpty());
			}
		});
		fieldNomenclatureChoice.valueProperty().addListener(nomenclatureParentUIValidator);
		fieldMetaFieldsControlsName.textProperty().addListener((o, c, n) -> {
			if (fieldMetaFieldsData.stream().anyMatch(t -> t.getName().equals(n))) {
				fieldMetaFieldsControlsAdd.setText("Edit");
			} else {
				fieldMetaFieldsControlsAdd.setText("Add");
			}
		});
		fieldMetaFieldsControlsAdd.setOnAction(e -> addMetafieldEntry());
		fieldMetaFieldsControlsDelete.setOnAction(e -> deleteMetafieldEntry());
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
	 * Deletes a field from the table.
	 */
	private void deleteMetafieldEntry() {
		fieldMetaFieldsData.removeAll(fieldMetafields.getSelectionModel().getSelectedItems());
		if (fieldMetaFieldsData.isEmpty()) {
			fieldsFeedback = "You need at least one field.";
			fieldsValid = false;
		} else {
			fieldsFeedback = "";
			fieldsValid = true;
		}
		updateFormStatus();
		fieldMetafields.refresh();
	}

	/**
	 * Adds a new field in the table view, resetting the text fields and the button. If an existing
	 * field has the same name, it will only update its description.
	 */
	private void addMetafieldEntry() {
		String name = fieldMetaFieldsControlsName.getText();
		String description = fieldMetaFieldsControlsDescription.getText();
		boolean isLong = fieldMetaFieldsControlsIsLong.isSelected();
		CommentTypeField field = getField(name);
		if (null == field) {
			fieldMetaFieldsData.add(new CommentTypeField(name, description, isLong));
		} else {
			field.setDescription(description);
			field.setIsLongText(isLong);
			fieldMetafields.refresh();
		}
		fieldMetaFieldsControlsName.setText("");
		fieldMetaFieldsControlsDescription.setText("");
		fieldMetaFieldsControlsIsLong.setSelected(false);
		fieldMetaFieldsControlsAdd.setDisable(true);
		fieldsFeedback = "";
		fieldsValid = true;
		updateFormStatus();
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
			commentType.setFields(fieldMetaFieldsData);
			fieldStyle.getCSSStyles().forEach((p, v) -> commentType.setStyle(p.toString(), v));
			answer = new CommentTypePropertiesAnswer(nomenclature, commentType);
		}
		return answer;
	}

}
