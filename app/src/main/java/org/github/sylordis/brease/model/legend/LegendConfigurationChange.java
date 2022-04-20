package org.github.sylordis.brease.model.legend;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.github.sylordis.brease.model.Comment;

import com.google.common.base.Preconditions;

/**
 * Change of legend configuration, holding information for each comment change from/to, field
 * modifications and new legend configuration.
 *
 * @author Sylordis
 *
 */
public class LegendConfigurationChange {

	/**
	 * Next legend configuration to replace the current one.
	 */
	private LegendConfiguration nextConfiguration;
	/**
	 * Map of all type conversions, with old type => type conversion. This map should never be null.
	 */
	private Map<LegendConfigurationType, LegendConfigurationTypeChange> conversions;

	/**
	 * Creates an empty legend configuration change.
	 */
	public LegendConfigurationChange() {
		this(null);
	}

	/**
	 * Creates a legend configuration change with a determined next legend configuration, but without
	 * any conversions.
	 *
	 * @param next legend configuration to be applied
	 */
	public LegendConfigurationChange(LegendConfiguration next) {
		this(next, null);
	}

	/**
	 * @param next     legend configuration to be applied
	 * @param conversions conversions to apply
	 */
	public LegendConfigurationChange(LegendConfiguration next,
			Map<LegendConfigurationType, LegendConfigurationTypeChange> conversions) {
		this.nextConfiguration = next;
		this.conversions = new HashMap<>();
		if (conversions != null)
			this.conversions.putAll(conversions);
	}

	/**
	 * Changes a comment according to its current conversions.
	 *
	 * @param c
	 * @return
	 */
	public boolean changeComment(Comment c) {
		boolean changed = false;
		if (this.nextConfiguration != null && hasTypeChangeFor(c.getType())) {
			this.conversions.get(c.getType()).accept(c);
			changed = true;
		}
		return changed;
	}

	/**
	 * @return the newConfiguration
	 */
	public LegendConfiguration getNextConfiguration() {
		return this.nextConfiguration;
	}

	/**
	 * @param nextConfiguration the next configuration to set
	 */
	public void setNextConfiguration(LegendConfiguration nextConfiguration) {
		this.nextConfiguration = nextConfiguration;
	}

	/**
	 * @return the list of conversions as unmodifiable object
	 */
	public Map<LegendConfigurationType, LegendConfigurationTypeChange> getTypeConversions() {
		return Collections.unmodifiableMap(this.conversions);
	}

	/**
	 * Adds a new type conversion.
	 *
	 * @param from   type to apply this conversion to
	 * @param change conversion to apply
	 * @throws NullPointerException if any of the arguments is null
	 */
	public void addTypeConversion(LegendConfigurationType from, LegendConfigurationTypeChange change) {
		Preconditions.checkNotNull(from, "Cannot add a conversion without a base type");
		Preconditions.checkNotNull(change, "Cannot add a conversion without the actual conversion");
		this.conversions.put(from, change);
	}

	/**
	 * Replaces all conversions by the provided ones. This method is null-safe.
	 *
	 * @param conversions to put instead of actual ones
	 */
	public void setTypeConversions(Map<LegendConfigurationType, LegendConfigurationTypeChange> conversions) {
		this.conversions.clear();
		if (conversions != null)
			this.conversions.putAll(conversions);
	}

	/**
	 * Removes a conversion from the collection of conversions.
	 *
	 * @param from type conversion to remove in the change
	 * @return the element that was removed, null otherwise
	 */
	public LegendConfigurationTypeChange removeTypeConversion(LegendConfigurationType from) {
		return this.conversions.remove(from);
	}

	/**
	 * Checks if the configuration change is configured for a certain type.
	 *
	 * @param type
	 * @return
	 */
	public boolean hasTypeChangeFor(LegendConfigurationType type) {
		return this.conversions.containsKey(type);
	}

}
