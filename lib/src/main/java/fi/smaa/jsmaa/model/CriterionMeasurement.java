/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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

import com.jgoodies.binding.beans.Observable;

import fi.smaa.common.RandomUtil;

public interface CriterionMeasurement extends Observable {
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
	
	public CriterionMeasurement deepCopy(List<Alternative> alts);
}
