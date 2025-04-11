package com.github.sylordis.binocles.ui.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class automates the feedback and the validation of forms.
 */
public class FormValidationControl {

	/**
	 * All suppliers to gather feedback when needed.
	 * 
	 * @see #combineFeedbacks()
	 */
	private List<Supplier<String>> feedbackCollectors;
	/**
	 * Suppliers that check the current dialog validity. A field is valid when its validator returns
	 * true.
	 */
	private List<Supplier<Boolean>> validators;
	
	/**
	 * Creates a new FormUserControl.
	 */
	public FormValidationControl() {
		feedbackCollectors = new ArrayList<>();
		validators = new ArrayList<>();
	}

	/**
	 * Checks if all validators set in this user control and returns a final answer.
	 * 
	 * @return true if all validators are valid, false otherwise
	 */
	public boolean checkValidity() {
		boolean validity = true;
		for (Supplier<Boolean> validator : validators) {
			validity = validity && validator.get();
		}
		return validity;
	}

	/**
	 * Combines all feedback from the collectors and consumes it.
	 * 
	 * @param consumer
	 */
	public void combineAndProcessFeedback(Consumer<String> consumer) {
		StringBuilder collect = new StringBuilder();
		for (Supplier<String> s : feedbackCollectors) {
			if (!collect.isEmpty())
				collect.append("\n");
			collect.append(s.get());
		}
		consumer.accept(collect.toString());
	}

	/**
	 * @return the feedbackCollectors
	 */
	public List<Supplier<String>> getFeedbackCollectors() {
		return feedbackCollectors;
	}

	/**
	 * @param feedbackCollectors the feedbackCollectors to set
	 */
	public void setFeedbackCollectors(List<Supplier<String>> feedbackCollectors) {
		this.feedbackCollectors = feedbackCollectors;
	}

	/**
	 * Adds a new feedback collector.
	 * 
	 * @param collector
	 */
	public void addFeedbackCollector(Supplier<String> collector) {
		feedbackCollectors.add(collector);
	}

	/**
	 * Adds multiple feedback collectors.
	 * 
	 * @param collectors
	 */
	public void addFeedbackCollectors(Collection<? extends Supplier<String>> collectors) {
		feedbackCollectors.addAll(collectors);
	}

	/**
	 * Adds multiple feedback collectors.
	 * 
	 * @param collectors
	 */
	@SafeVarargs
	public final void addFeedbackCollectors(Supplier<String>... collectors) {
		feedbackCollectors.addAll(Arrays.asList(collectors));
	}
	
	/**
	 * @return the formValidators
	 */
	public List<Supplier<Boolean>> getFormValidators() {
		return validators;
	}

	/**
	 * @param validators the formValidators to set
	 */
	public void setFormValidators(List<Supplier<Boolean>> validators) {
		this.validators = validators;
	}

	/**
	 * Adds multiple validators to this form user control.
	 * 
	 * @param validators
	 */
	@SafeVarargs
	public final void addFormValidators(Supplier<Boolean>... validators) {
		this.validators.addAll(Arrays.asList(validators));
	}

	/**
	 * Adds a new validator to this form user control.
	 * 
	 * @param validator
	 */
	public void addFormValidator(Supplier<Boolean> validator) {
		validators.add(validator);
	}

	/**
	 * Adds multiple validators to this form user control.
	 * 
	 * @param validators
	 */
	public void addFormValidators(Collection<? extends Supplier<Boolean>> validators) {
		this.validators.addAll(validators);
	}

}
