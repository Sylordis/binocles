package com.github.sylordis.binocle.ui.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TextListenerValidator implements ChangeListener<String> {

	/**
	 * List of conditions to fulfil.
	 */
	private final Map<String, BiFunction<String, String, Boolean>> faults;
	private Consumer<Boolean> actionOnValid;
	private Consumer<Boolean> actionOnInvalid;
	private Consumer<String> feedback;
	private String feedIdle = "";

	/**
	 * Constructs a new empty validator.
	 */
	public TextListenerValidator() {
		this.faults = new HashMap<>();
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		boolean valid = true;
		// Prepare error message collection
		StringBuilder rame = new StringBuilder();
		// Go through all conditions
		var errors = faults.entrySet();
		for (var error : errors) {
			// True if an error is spotted
			boolean checkForError = error.getValue().apply(oldValue, newValue);
			// Set the validity (set to false if at least one of the conditions are met)
			valid = valid && !checkForError;
			// Propagate error message
			if (checkForError) {
				if (!rame.isEmpty())
					rame.append("\n");
				rame.append(error.getKey());
			}
		}
		// Act on final result
		if (valid) {
			triggerOnValidAction(valid);
			triggerFeedback(feedIdle);
		} else {
			triggerOnInvalidAction(valid);
			triggerFeedback(rame.toString());
		}
	}

	public void triggerOnInvalidAction(boolean valid) {
		if (null != actionOnInvalid)
			actionOnInvalid.accept(valid);
	}

	public void triggerOnValidAction(boolean valid) {
		if (null != actionOnValid)
			actionOnValid.accept(valid);
	}

	public void triggerFeedback(String txt) {
		if (null != feedback)
			feedback.accept(txt);
	}

	public void addErrorCondition(String text, BiFunction<String, String, Boolean> condition) {
		this.faults.put(text, condition);
	}

	/**
	 * @return the onValid
	 */
	public Consumer<Boolean> getActionOnValid() {
		return actionOnValid;
	}

	/**
	 * @param onValid the onValid to set
	 */
	public void setActionOnValid(Consumer<Boolean> onValid) {
		this.actionOnValid = onValid;
	}

	/**
	 * @return the onInvalid
	 */
	public Consumer<Boolean> getActionOnInvalid() {
		return actionOnInvalid;
	}

	/**
	 * @param onInvalid the onInvalid to set
	 */
	public void setActionOnInvalid(Consumer<Boolean> onInvalid) {
		this.actionOnInvalid = onInvalid;
	}

	/**
	 * @return the feedback
	 */
	public Consumer<String> getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(Consumer<String> feedback) {
		this.feedback = feedback;
	}

	/**
	 * @return the feedIdle
	 */
	public String getFeedbackIdle() {
		return feedIdle;
	}

	/**
	 * @param feedIdle the feedIdle to set
	 */
	public void setFeedbackIdle(String feedIdle) {
		if (null == feedIdle)
			this.feedIdle = "";
		else
			this.feedIdle = feedIdle;
	}

	/**
	 * @return the error conditions
	 */
	public Map<String, BiFunction<String, String, Boolean>> getErrorConditions() {
		return faults;
	}

	/**
	 * Replaces all previously set error conditions.
	 * 
	 * @param faults
	 */
	public void setErrorConditions(Map<String, BiFunction<String, String, Boolean>> faults) {
		this.faults.clear();
		if (faults != null)
			this.faults.putAll(faults);
	}

	/**
	 * Adds a new error to this validator.
	 * 
	 * @param text      Error text
	 * @param condition Error condition
	 * @return itself for chain building
	 */
	public TextListenerValidator error(String text, BiFunction<String, String, Boolean> condition) {
		addErrorCondition(text, condition);
		return this;
	}

	/**
	 * Sets the action when this validator is valid.
	 * 
	 * @param action
	 * @return
	 */
	public TextListenerValidator onValid(Consumer<Boolean> action) {
		this.actionOnValid = action;
		return this;
	}

	/**
	 * Sets the action when this validator is invalid.
	 * 
	 * @param action
	 * @return
	 */
	public TextListenerValidator onInvalid(Consumer<Boolean> action) {
		this.actionOnInvalid = action;
		return this;
	}

	/**
	 * Sets both actions when this validator is valid or invalid. Use this if both actions are the same
	 * and trigger a similar behaviour set on the validator output.
	 * 
	 * @param action
	 * @return
	 */
	public TextListenerValidator onEither(Consumer<Boolean> action) {
		this.actionOnInvalid = action;
		this.actionOnValid = action;
		return this;
	}

	/**
	 * Sets the feedback consumer.
	 * @param feedback
	 * @return
	 */
	public TextListenerValidator feed(Consumer<String> feedback) {
		this.feedback = feedback;
		return this;
	}
}
