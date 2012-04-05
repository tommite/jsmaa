package fi.smaa.jsmaa.model;

import java.util.List;

import fi.smaa.common.RandomUtil;

/**
 * A full joint distribution over the criteria x alternatives.
 */
public interface FullJointMeasurements {
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
	 * Gets the criteria.
	 * @return the criteria. Never a null.
	 */
	public List<Criterion> getCriteria();

	/**
	 * Adds an alternative. If alternative already exists, does nothing.
	 * @param c
	 */
	public void addCriterion(Criterion c);

	/**
	 * Deletes a criterion. If criterion doesn't exist, does nothing.
	 * @param c Criterion to delete
	 */
	public void deleteCriterion(Criterion c);

	/**
	 * Reorder the list of criteria.
	 * @param crits The new order.
	 */
	public void reorderCriteria(List<Criterion> crits);

	/**
	 * Sample from the full joint distribution of measurements.
	 * The sample is written to the target array, where the rows correspond to criteria and the columns to alternatives. 
	 * @param target Target array, criteria x alternatives.
	 */
	public void sample(RandomUtil random, double[][] target);
	
	/**
	 * Get the range for criteria measurements on the given criterion.
	 * @return An interval, or null if the criterion is not cardinal.
	 * @see CardinalMeasurement
	 */
	public abstract Interval getRange(Criterion crit);

	/**
	 * Make a deep copy, replacing the alternatives and criteria with their equals in the given lists.
	 * @param alts Alternatives to use in the copy.
	 * @param crit Criteria to use in the copy.
	 * @return A deep copy.
	 */
	public FullJointMeasurements deepCopy(List<Alternative> alts, List<Criterion> crit);

	/**
	 * Listen to changes in the structure and measurements.
	 */
	public void addListener(ImpactMatrixListener l);
	
	/**
	 * Stop listening to changes in the structure and measurements.
	 */
	public void removeListener(ImpactMatrixListener l);
}