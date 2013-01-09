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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.drugis.common.beans.AbstractObservable;
import org.drugis.common.stat.Statistics;

import fi.smaa.common.RandomUtil;

public class RelativeLogitGaussianCriterionMeasurement extends AbstractObservable implements CriterionMeasurement {
	private static final String PROPERTY_NESTED_MEASUREMENT = "gaussianMeasurement";
	
	private final RelativeGaussianCriterionMeasurement nested;

	public RelativeLogitGaussianCriterionMeasurement(RelativeGaussianCriterionMeasurement m) {
		this.nested = m;	
		nested.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange(PROPERTY_NESTED_MEASUREMENT, null, RelativeLogitGaussianCriterionMeasurement.this.nested);
			}
		});
	}
	
	public RelativeGaussianCriterionMeasurement getGaussianMeasurement() {
		return nested;
	}

	@Override
	public List<Alternative> getAlternatives() {
		return nested.getAlternatives();
	}

	@Override
	public void addAlternative(Alternative alt) {
		nested.addAlternative(alt);
	}

	@Override
	public void deleteAlternative(Alternative alt) {
		nested.deleteAlternative(alt);
	}

	@Override
	public void reorderAlternatives(List<Alternative> alts) {
		nested.reorderAlternatives(alts);
	}

	@Override
	public void sample(RandomUtil random, double[][] target, int criterionIndex) {
		nested.sample(random, target, criterionIndex);
		for(int i = 0; i < getAlternatives().size(); ++i) {
			target[criterionIndex][i] = Statistics.ilogit(target[criterionIndex][i]);
		}
	}

	@Override
	public Interval getRange() {
		return new Interval(0.0, 1.0);
	}

	@Override
	public RelativeLogitGaussianCriterionMeasurement deepCopy(List<Alternative> alts) {
		return new RelativeLogitGaussianCriterionMeasurement(nested.deepCopy(alts));
	}
	
}
