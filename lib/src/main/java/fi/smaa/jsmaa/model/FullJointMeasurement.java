package fi.smaa.jsmaa.model;

import java.util.List;

import fi.smaa.common.RandomUtil;

public interface FullJointMeasurement {

	public void addListener(ImpactMatrixListener l);

	public void removeListener(ImpactMatrixListener l);

	/**
	 * Deletes an alternative. If alternative doesn't exist, does nothing.
	 * @param alt Alternative to delete.
	 */
	public void deleteAlternative(Alternative alt);

	/**
	 * Adds an alternative. If alternative already exists, does nothing.
	 * @param alt Alternative to add.
	 */
	public void addAlternative(Alternative alt);

	/**
	 * Deletes a criterion. If criterion doesn't exist, does nothing.
	 * @param c Criterion to delete
	 */
	public void deleteCriterion(Criterion c);

	/**
	 * Adds an alternative. If alternative already exists, does nothing.
	 * @param c
	 */
	public void addCriterion(Criterion c);

	/**
	 * Gets the alternatives.
	 * @return the alternatives. Never a null.
	 */
	public List<Alternative> getAlternatives();

	/**
	 * Gets the criteria.
	 * @return the criteria. Never a null.
	 */
	public List<Criterion> getCriteria();

	public FullJointMeasurement deepCopy(List<Alternative> alts, List<Criterion> crit);

	public void reorderAlternatives(List<Alternative> newAlts);

	public void reorderCriteria(List<Criterion> newCrit);

	public void sample(RandomUtil random, double[][] target);
}