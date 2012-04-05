package fi.smaa.jsmaa.model;

import java.util.List;

import fi.smaa.common.RandomUtil;

public interface CriterionMeasurement {
	/**
	 * Gets the alternatives.
	 * @return the alternatives. Never a null.
	 */
	public List<Alternative> getAlternatives();

	/**
	 * Adds an alternative. If alternative already exists, does nothing.
	 * @param alt Alternative to add.
	 */
	public void addAlternative(Alternative alt);

	/**
	 * Deletes an alternative. If alternative doesn't exist, does nothing.
	 * @param alt Alternative to delete.
	 */
	public void deleteAlternative(Alternative alt);

	/**
	 * Reorder the list of alternatives.
	 * @param alts The new order.
	 */
	public void reorderAlternatives(List<Alternative> alts);
	
	/**
	 * Sample from the joint distribution over the alternatives.
	 * The sample is written to the target array, where the rows correspond to criteria and the columns to alternatives.
	 * @param target Target array, criteria x alternatives.
	 * @param criterionIndex Index of the row to write the samples to.
	 */
	public void sample(RandomUtil random, double target[][], int criterionIndex);
	
	/**
	 * Get the range for the criterion measurements.
	 * @return An interval, or null if the criterion is not cardinal.
	 * @see CardinalMeasurement
	 */
	public abstract Interval getRange();
}
