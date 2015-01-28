/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import fi.smaa.common.RandomUtil;

public class RelativeGaussianCriterionMeasurement extends AbstractObservable implements CriterionMeasurement {
	private static final String PROPERTY_RELATIVE_MEASUREMENT = "relativeMeasurement";
	private static final String PROPERTY_BASELINE_MEASUREMENT = "baselineMeasurement";
	
	private final MultivariateGaussianCriterionMeasurement relative;
	private final GaussianMeasurement baseline;

	public RelativeGaussianCriterionMeasurement(MultivariateGaussianCriterionMeasurement relative, GaussianMeasurement baseline) {
		this.relative = relative;
		this.baseline = baseline;
		relative.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange(PROPERTY_RELATIVE_MEASUREMENT, null, RelativeGaussianCriterionMeasurement.this.relative);
			}
		});
		baseline.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange(PROPERTY_BASELINE_MEASUREMENT, null, RelativeGaussianCriterionMeasurement.this.baseline);
			}
		});
	}
	
	public MultivariateGaussianCriterionMeasurement getRelativeMeasurement() {
		return this.relative;
	}
	
	public GaussianMeasurement getBaselineMeasurement() {
		return this.baseline;
	}

	@Override
	public List<Alternative> getAlternatives() {
		return relative.getAlternatives();
	}

	@Override
	public void addAlternative(Alternative alt) {
		relative.addAlternative(alt);
	}

	@Override
	public void deleteAlternative(Alternative alt) {
		relative.deleteAlternative(alt);
	}

	@Override
	public void reorderAlternatives(List<Alternative> alts) {
		relative.reorderAlternatives(alts);
	}

	@Override
	public void sample(RandomUtil random, double[][] target, int criterionIndex) {
		relative.sample(random, target, criterionIndex);
		double theta = baseline.sample(random);
		for(int i = 0; i < getAlternatives().size(); ++i) { 
			target[criterionIndex][i] += theta;
		}
	}

	@Override
	public Interval getRange() {
		Interval i1 = relative.getRange();
		Interval i2 = baseline.getRange();
		return new Interval(i1.getStart() + i2.getStart(), i1.getEnd() + i2.getEnd());
	}

	@Override
	public RelativeGaussianCriterionMeasurement deepCopy(List<Alternative> alts) {
		return new RelativeGaussianCriterionMeasurement(relative.deepCopy(alts), baseline.deepCopy());
	}
}
