package com.github.sylordis.binocles.ui.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * A ListenerValidator checks the input provided and validates it with a list of conditions that can
 * trigger error messages. The validator can be setup in one line by using the one-liner chained
 * methods.<br/><br/>
 * 
 * One-liners: {@link #error(String, BiFunction)}, {@link #feed(Consumer)},
 * {@link #feed(Consumer, Function)}, {@link #feedDefault(String)}, {@link #onValid(Consumer)}, {@link #onInvalid(Consumer)},
 * {@link #onEither(Consumer)}, {@link #andThen(Runnable)}.
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
	private Consumer<Boolean> actionWhenValid;
	/**
	 * Action when validation is invalid.
	 */
	private Consumer<Boolean> actionWhenInvalid;
	/**
	 * Feedback writing when triggered.
	 */
	private Consumer<String> feedbackConsumer;
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
		public static final Function<List<String>, String> AGGREGATE = s -> String.join("\n", s);
		/**
		 * Default behaviour.
		 * 
		 * @see #AGGREGATE
		 */
		public static final Function<List<String>, String> DEFAULT = AGGREGATE;

	}

	/**
	 * Behaviour for feedback. Default is {@link FeedbackBehaviour#AGGREGATE}.
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
		this.errorMessages.clear();
		// Go through all conditions
		var errors = validityConditions.entrySet();
		for (var error : errors) {
			// True if an error is spotted
			boolean conditionValid = error.getValue().apply(oldValue, newValue);
			// Set the validity (set to false if at least one of the conditions are met)
			valid = valid && conditionValid;
			// Propagate error message
			if (!conditionValid)
				this.errorMessages.add(error.getKey());
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
			actionWhenInvalid.accept(valid);
	}

	/**
	 * Triggers the action when input is invalid, if set.
	 * 
	 * @param valid
	 */
	public void triggerWhenValid(boolean valid) {
		if (null != actionWhenValid)
			actionWhenValid.accept(valid);
	}

	/**
	 * Triggers the feedback provider if set.
	 * 
	 * @param txt
	 */
	public void triggerFeedback() {
		if (null != feedbackConsumer) {
			if (this.errorMessages.isEmpty())
				feedbackConsumer.accept(feedbackDefault);
			else
				feedbackConsumer.accept(feedbackBehaviour.apply(errorMessages));
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
	public Consumer<Boolean> getActionWhenValid() {
		return actionWhenValid;
	}

	/**
	 * @param whenValid the onValid to set
	 */
	public void setActionWhenValid(Consumer<Boolean> whenValid) {
		this.actionWhenValid = whenValid;
	}

	/**
	 * @return the whenInvalid
	 */
	public Consumer<Boolean> getActionWhenInvalid() {
		return actionWhenInvalid;
	}

	/**
	 * @param whenInvalid the onInvalid to set
	 */
	public void setActionWhenInvalid(Consumer<Boolean> whenInvalid) {
		this.actionWhenInvalid = whenInvalid;
	}

	/**
	 * @return the feedback consumer
	 */
	public Consumer<String> getFeedbackConsumer() {
		return feedbackConsumer;
	}

	/**
	 * @param consumer the feedback consumer to set
	 */
	public void setFeedbackConsumer(Consumer<String> consumer) {
		this.feedbackConsumer = consumer;
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
			this.feedbackBehaviour = FeedbackBehaviour.AGGREGATE;
	}

	/**
	 * Adds a new error to this validator.
	 * 
	 * @param text      Error feedback to be displayed when invalid
	 * @param validator Validity condition
	 * @return itself for chain configuration
	 */
	public ListenerValidator<T> error(String text, BiFunction<T, T, Boolean> validator) {
		addValiditionCondition(text, validator);
		return this;
	}

	/**
	 * Sets the action when this validator is valid.
	 * 
	 * @param action
	 * @return itself for chain configuration
	 */
	public ListenerValidator<T> onValid(Consumer<Boolean> action) {
		setActionWhenValid(action);
		return this;
	}

	/**
	 * Sets the action when this validator is invalid.
	 * 
	 * @param action
	 * @return itself for chain configuration
	 */
	public ListenerValidator<T> onInvalid(Consumer<Boolean> action) {
		setActionWhenInvalid(action);
		return this;
	}

	/**
	 * Sets both actions when this validator is valid or invalid. Use this if both actions are the same
	 * and trigger a similar behaviour set on the validator output.
	 * 
	 * @param action
	 * @return itself for chain configuration
	 */
	public ListenerValidator<T> onEither(Consumer<Boolean> action) {
		setActionWhenInvalid(action);
		setActionWhenValid(action);
		return this;
	}

	/**
	 * Sets the feedback consumer.
	 * 
	 * @param feedback
	 * @return itself for chain configuration
	 */
	public ListenerValidator<T> feed(Consumer<String> feedback) {
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
	public ListenerValidator<T> feed(Consumer<String> feedback, Function<List<String>, String> behaviour) {
		setFeedbackConsumer(feedback);
		setFeedbackBehaviour(behaviour);
		return this;
	}

	/**
	 * Sets the post action.
	 * 
	 * @param post
	 * @return itself for chain configuration
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
	 */
	public ListenerValidator<T> feedDefault(String idle) {
		setFeedbackDefault(idle);
		return this;
	}
}
