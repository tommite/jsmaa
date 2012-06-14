/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
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
	 * @param crit Criteria to use in the copy.
	 * @param alts Alternatives to use in the copy.
	 * @return A deep copy.
	 */
	public FullJointMeasurements deepCopy(List<Criterion> crit, List<Alternative> alts);

	/**
	 * Listen to changes in the structure and measurements.
	 */
	public void addListener(ImpactMatrixListener l);
	
	/**
	 * Stop listening to changes in the structure and measurements.
	 */
	public void removeListener(ImpactMatrixListener l);
}