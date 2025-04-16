package com.github.sylordis.binocles.ui.components;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class automates the feedback and the validation of forms.
 */
public class FormValidationControl {

	/**
	 * All field feedbacks.
	 */
	private Map<Object, String> feedbacks;
	/**
	 * All field validity statuses.
	 */
	private Map<Object, Boolean> validities;

	/**
	 * Creates a new FormUserControl.
	 */
	public FormValidationControl() {
		feedbacks = new LinkedHashMap<>();
		validities = new LinkedHashMap<>();
	}

	/**
	 * Registers one or multiple fields.
	 * 
	 * @param f
	 */
	public void register(Object... fields) {
		for (Object f : fields) {
			this.feedbacks.put(f, "");
			this.validities.put(f, false);
		}
	}

	/**
	 * Gets the feedback associated to a field.
	 * 
	 * @param field
	 * @return
	 */
	public String feedback(Object field) {
		return this.feedbacks.getOrDefault(field, "");
	}

	/**
	 * Sets the feedback associated to a field.
	 * 
	 * @param field
	 * @param feedback
	 * @return
	 */
	public void feedback(Object field, String feedback) {
		this.feedbacks.put(field, feedback);
	}

	/**
	 * Gets the validity of a field.
	 * 
	 * @param field
	 * @return
	 */
	public boolean valid(Object field) {
		return this.validities.getOrDefault(field, false);
	}

	/**
	 * Sets the validity of a field.
	 * 
	 * @param field
	 * @param validity
	 */
	public void valid(Object field, boolean validity) {
		this.validities.put(field, validity);
	}

	public void set(Object field, boolean validity, String feedback) {
		this.feedbacks.put(field, feedback);
		this.validities.put(field, validity);
	}

	/**
	 * Checks if all validators set in this user control and returns a final answer.
	 * 
	 * @return true if all validators are valid, false otherwise
	 */
	public boolean checkValidity() {
		return validities.values().stream().allMatch(b -> b);
	}

	/**
	 * Combines all feedback from the collectors and consumes it.
	 * 
	 * @param consumer
	 */
	public void combineAndProcessFeedback(Consumer<String> consumer) {
		StringBuilder collect = new StringBuilder();
		for (String s : feedbacks.values()) {
			if (s != null && !s.isEmpty()) {
				if (!collect.isEmpty())
					collect.append("\n");
				collect.append(s);
			}
		}
		consumer.accept(collect.toString());
	}

}
