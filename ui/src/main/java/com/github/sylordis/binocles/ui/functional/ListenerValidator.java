package com.github.sylordis.binocles.ui.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * A ListenerValidator checks the input provided and validates it with a list of conditions that can
 * trigger error messages. The validator can be setup in one line by using the one-liner chained
 * methods.<br/>
 * <br/>
 * 
 * One-liners: {@link #validIf(String, BiFunction)}, {@link #feed(Consumer)},
 * {@link #feed(Consumer, Function)}, {@link #feedDefault(String)}, {@link #onValid(Consumer)},
 * {@link #onInvalid(Consumer)}, {@link #onEither(Consumer)}, {@link #andThen(Runnable)}.
 * 
 * @author sylordis
 *
 * @param <T> type of the value handled by this validator
 */
public class ListenerValidator<T> implements ChangeListener<T> {

	/**
	 * List of conditions to fulfil for the field to be valid.
	 */
	private final Map<String, BiFunction<T, T, Boolean>> validityConditions;
	/**
	 * Action when validation is valid.
	 */
	private BiConsumer<Object, Boolean> actionWhenValid;
	/**
	 * Action when validation is invalid.
	 */
	private BiConsumer<Object, Boolean> actionWhenInvalid;
	/**
	 * Feedback writing when triggered.
	 */
	private BiConsumer<Object, String> feedbackConsumer;
	/**
	 * All raised error messages. This list should never be null.
	 */
	private List<String> errorMessages;
	/**
	 * Action to run when verification has been triggered.
	 */
	private Runnable postAction;
	/**
	 * Basic text when there's no error. Default is {@link #FEEDBACK_DEFAULT}.
	 */
	private String feedbackDefault = FEEDBACK_DEFAULT;
	/**
	 * Object this validator is for. Is used to inject data for a specific field.
	 */
	private Object fieldFor;

	/**
	 * Default configured value for the feedback.
	 */
	public final static String FEEDBACK_DEFAULT = "";

	/**
	 * Behaviour for feedback.
	 * 
	 * @author sylordis
	 *
	 */
	public final class FeedbackBehaviour {

		/**
		 * Gets the first entry of the list.
		 */
		public static final Function<List<String>, String> FIRST_ONLY = s -> s.get(0);
		/**
		 * Gets the last entry of the list.
		 */
		public static final Function<List<String>, String> LAST_ONLY = s -> s.get(s.size() - 1);
		/**
		 * Joins all strings with a new line.
		 * 
		 * @see String#join(CharSequence, Iterable)
		 */
		public static final Function<List<String>, String> AGGREGATE_NEWLINE = s -> s.stream()
		        .filter(StringUtils::isNotBlank).collect(Collectors.joining("\n"));
		/**
		 * Joins all strings with a semi-column.
		 * 
		 * @see String#join(CharSequence, Iterable)
		 */
		public static final Function<List<String>, String> AGGREGATE_SEMICOLON = s -> s.stream()
		        .filter(StringUtils::isNotBlank).collect(Collectors.joining("; "));
		/**
		 * Default behaviour.
		 * 
		 * @see #AGGREGATE_NEWLINE
		 */
		public static final Function<List<String>, String> DEFAULT = AGGREGATE_NEWLINE;

	}

	/**
	 * Behaviour for feedback. Default is {@link FeedbackBehaviour#AGGREGATE_NEWLINE}.
	 * 
	 * @see FeedbackBehaviour
	 */
	private Function<List<String>, String> feedbackBehaviour = FeedbackBehaviour.DEFAULT;

	/**
	 * Constructs a new empty validator.
	 */
	public ListenerValidator() {
		this.validityConditions = new HashMap<>();
		this.errorMessages = new ArrayList<>();
	}

	@Override
	public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
		boolean valid = true;
		// Prepare error message collection
		this.getErrorMessages().clear();
		// Go through all conditions
		var conditions = getValidityConditions().entrySet();
		for (var condition : conditions) {
			// True if no error is spotted
			boolean conditionValid = condition.getValue().apply(oldValue, newValue);
			// Set the global validity (true as long as no false is encountered)
			valid = valid && conditionValid;
			// Propagate error message
			if (!conditionValid)
				this.getErrorMessages().add(condition.getKey());
		}
		// Act on final result
		if (valid) {
			triggerWhenValid(valid);
		} else {
			triggerWhenInvalid(valid);
		}
		triggerFeedback();
		triggerPostAction();
	}

	/**
	 * Triggers the action when input is invalid, if set.
	 * 
	 * @param valid
	 */
	public void triggerWhenInvalid(boolean valid) {
		if (null != actionWhenInvalid)
			actionWhenInvalid.accept(fieldFor, valid);
	}

	/**
	 * Triggers the action when input is invalid, if set.
	 * 
	 * @param valid
	 */
	public void triggerWhenValid(boolean valid) {
		if (null != actionWhenValid)
			actionWhenValid.accept(fieldFor, valid);
	}

	/**
	 * Triggers the feedback provider if set.
	 * 
	 * @param txt
	 */
	public void triggerFeedback() {
		if (null != feedbackConsumer) {
			if (this.errorMessages.isEmpty())
				feedbackConsumer.accept(fieldFor, feedbackDefault);
			else
				feedbackConsumer.accept(fieldFor, feedbackBehaviour.apply(errorMessages));
		}
	}

	/**
	 * Adds an error condition to this validator.
	 * 
	 * @param error             message to display in case of error
	 * @param validityCondition function to validate the input, input is old input / new input.
	 */
	public void addValiditionCondition(String error, BiFunction<T, T, Boolean> validityCondition) {
		if (null != error && null != validityCondition)
			this.validityConditions.put(error, validityCondition);
	}

	/**
	 * @return the onValid
	 */
	public BiConsumer<Object, Boolean> getActionWhenValid() {
		return actionWhenValid;
	}

	/**
	 * @param whenValid the onValid to set
	 */
	public void setActionWhenValid(BiConsumer<Object, Boolean> whenValid) {
		this.actionWhenValid = whenValid;
	}

	/**
	 * @return the whenInvalid
	 */
	public BiConsumer<Object, Boolean> getActionWhenInvalid() {
		return actionWhenInvalid;
	}

	/**
	 * @param whenInvalid the onInvalid to set
	 */
	public void setActionWhenInvalid(BiConsumer<Object, Boolean> whenInvalid) {
		this.actionWhenInvalid = whenInvalid;
	}

	/**
	 * @return the feedback consumer
	 */
	public BiConsumer<Object, String> getFeedbackConsumer() {
		return feedbackConsumer;
	}

	/**
	 * @param consumer the feedback consumer to set
	 */
	public void setFeedbackConsumer(BiConsumer<Object, String> consumer) {
		this.feedbackConsumer = consumer;
	}

	/**
	 * @return the fieldFor
	 */
	protected Object getFieldFor() {
		return fieldFor;
	}

	/**
	 * @param fieldFor the fieldFor to set
	 */
	protected void setFieldFor(Object fieldFor) {
		this.fieldFor = fieldFor;
	}

	/**
	 * @return the feed default value
	 */
	public String getFeedbackDefault() {
		return feedbackDefault;
	}

	/**
	 * @param feedDefault the feed default value to set
	 */
	public void setFeedbackDefault(String feedDefault) {
		if (null == feedDefault)
			this.feedbackDefault = FEEDBACK_DEFAULT;
		else
			this.feedbackDefault = feedDefault;
	}

	/**
	 * @return the validity conditions
	 */
	public Map<String, BiFunction<T, T, Boolean>> getValidityConditions() {
		return validityConditions;
	}

	/**
	 * Replaces all previously set validity conditions.
	 * 
	 * @param conditions
	 */
	public void setValidityConditions(Map<String, BiFunction<T, T, Boolean>> conditions) {
		this.validityConditions.clear();
		if (conditions != null)
			this.validityConditions.putAll(conditions);
	}

	/**
	 * @return the postAction
	 */
	public Runnable getPostAction() {
		return postAction;
	}

	/**
	 * @param postAction the postAction to set
	 */
	public void setPostAction(Runnable postAction) {
		this.postAction = postAction;
	}

	/**
	 * Triggers the action post validation.
	 */
	public void triggerPostAction() {
		if (null != this.postAction)
			postAction.run();
	}

	/**
	 * @return the errorMessages
	 */
	public Collection<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages.clear();
		if (null != errorMessages)
			this.errorMessages.addAll(errorMessages);
	}

	/**
	 * @return the feedbackBehaviour
	 */
	public Function<List<String>, String> getFeedbackBehaviour() {
		return feedbackBehaviour;
	}

	/**
	 * Sets the feedback behaviour aggregator. If null, resets it to default.
	 * 
	 * @param feedbackBehaviour the feedbackBehaviour to set
	 */
	public void setFeedbackBehaviour(Function<List<String>, String> feedbackBehaviour) {
		if (null != feedbackBehaviour)
			this.feedbackBehaviour = feedbackBehaviour;
		else
			this.feedbackBehaviour = FeedbackBehaviour.AGGREGATE_NEWLINE;
	}

	public ListenerValidator<T> link(Object o) {
		this.fieldFor = o;
		return this;
	}
	
	/**
	 * Adds a new condition to this validator. Multiple conditions can be added.
	 * 
	 * @param errorText Error feedback to be displayed when invalid
	 * @param validator Validity condition
	 * @return itself for chain configuration
	 * @see #addValiditionCondition(String, BiFunction)
	 */
	public ListenerValidator<T> validIf(String errorText, BiFunction<T, T, Boolean> validator) {
		addValiditionCondition(errorText, validator);
		return this;
	}

	/**
	 * Sets the action when this validator is valid.
	 * 
	 * @param action
	 * @return itself for chain configuration
	 * @see #setActionWhenValid(Consumer)
	 */
	public ListenerValidator<T> onValid(BiConsumer<Object,Boolean> action) {
		setActionWhenValid(action);
		return this;
	}

	/**
	 * Sets the action when this validator is invalid.
	 * 
	 * @param action
	 * @return itself for chain configuration
	 * @see #setActionWhenInvalid(Consumer)
	 */
	public ListenerValidator<T> onInvalid(BiConsumer<Object,Boolean> action) {
		setActionWhenInvalid(action);
		return this;
	}

	/**
	 * Sets both actions when this validator is valid or invalid. Use this if both actions are the same
	 * and trigger a similar behaviour set on the validator output.
	 * 
	 * @param action
	 * @return itself for chain configuration
	 * @see #setActionWhenInvalid(Consumer)
	 * @see #setActionWhenValid(Consumer)
	 */
	public ListenerValidator<T> onEither(BiConsumer<Object,Boolean> action) {
		setActionWhenInvalid(action);
		setActionWhenValid(action);
		return this;
	}

	/**
	 * Sets the feedback consumer.
	 * 
	 * @param feedback
	 * @return itself for chain configuration
	 * @see #setFeedbackConsumer(Consumer)
	 */
	public ListenerValidator<T> feed(BiConsumer<Object,String> feedback) {
		setFeedbackConsumer(feedback);
		return this;
	}

	/**
	 * Sets the feedback consumer and changes the behaviour in case multiple error messages are raised.
	 * 
	 * @param feedback
	 * @param behaviour behaviour in case of multiple error messages
	 * @return itself for chain configuration
	 * @see FeedbackBehaviour
	 */
	public ListenerValidator<T> feed(BiConsumer<Object,String> feedback, Function<List<String>, String> behaviour) {
		setFeedbackConsumer(feedback);
		setFeedbackBehaviour(behaviour);
		return this;
	}

	/**
	 * Sets the post action.
	 * 
	 * @param post
	 * @return itself for chain configuration
	 * @see #setPostAction(Runnable)
	 */
	public ListenerValidator<T> andThen(Runnable post) {
		setPostAction(post);
		return this;
	}

	/**
	 * Sets the idle text for the feedback. Setting it has no effect if no feedback consumer is
	 * provided.
	 * 
	 * @param idle
	 * @return
	 * @see #setFeedbackDefault(String)
	 */
	public ListenerValidator<T> feedDefault(String idle) {
		setFeedbackDefault(idle);
		return this;
	}

}
