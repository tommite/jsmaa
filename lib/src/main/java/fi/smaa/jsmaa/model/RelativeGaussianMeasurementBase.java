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

import fi.smaa.common.RandomUtil;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public abstract class RelativeGaussianMeasurementBase extends CardinalMeasurement {
	private static final long serialVersionUID = -4052031951942599415L;

	public static final String PROPERTY_BASELINE = "baseline";

	public static final String PROPERTY_RELATIVE = "relative";
	private BaselineGaussianMeasurement d_baseline;
	private GaussianMeasurement d_relative;
	private PropertyChangeListener nestedListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getSource() == d_baseline) {
					firePropertyChange(PROPERTY_BASELINE, null, d_baseline);
				} else if (event.getSource() == d_relative) {
					firePropertyChange(PROPERTY_RELATIVE, null, d_relative);
				}
			}
		};

	public RelativeGaussianMeasurementBase(
			BaselineGaussianMeasurement baseline,
			GaussianMeasurement relative) {
		d_baseline = baseline;
		d_baseline.addPropertyChangeListener(nestedListener);
		d_relative = relative;
		d_relative.addPropertyChangeListener(nestedListener);
	}

	protected abstract RelativeGaussianMeasurementBase newInstance();

	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		RelativeGaussianMeasurementBase m = (RelativeGaussianMeasurementBase)obj;
		return m.getBaseline().equals(getBaseline()) && m.getRelative().equals(getRelative());
	}
	
	public RelativeGaussianMeasurementBase deepCopy() {
		RelativeGaussianMeasurementBase copy = newInstance();
		copy.setBaseline(getBaseline().deepCopy());
		copy.setRelative(getRelative().deepCopy());
		return copy;
	}

	@Override
	public String toString() {
		return "ilogit(" + d_baseline.toString() + " + " + d_relative.toString() + ")";
	}

	protected Double getAbsoluteStdDev() {
		double s1 = d_baseline.getStDev();
		double s2 = d_relative.getStDev();
		return Math.sqrt(s1 * s1 + s2 * s2);
	}

	protected double getAbsoluteMean() {
		return d_baseline.getMean() + d_relative.getMean();
	}

	@Override
	public double sample(RandomUtil random) {
		return getBaseline().sample(random) + getRelative().sample(random);
	}

	public BaselineGaussianMeasurement getBaseline() {
		return d_baseline;
	}

	void setBaseline(BaselineGaussianMeasurement m) {
		d_baseline.removePropertyChangeListener(nestedListener);
		d_baseline = m;
		d_relative.addPropertyChangeListener(nestedListener);
	}

	public GaussianMeasurement getRelative() {
		return d_relative;
	}

	private void setRelative(GaussianMeasurement m) {
		d_relative.removePropertyChangeListener(nestedListener);
		d_relative = m;
		d_relative.addPropertyChangeListener(nestedListener);
	}
	
	protected static final XMLFormat<RelativeGaussianMeasurementBase> XML = new XMLFormat<RelativeGaussianMeasurementBase>(RelativeGaussianMeasurementBase.class) {
		@Override
		public boolean isReferenceable() {
			return GaussianMeasurement.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, RelativeGaussianMeasurementBase meas) throws XMLStreamException {
			meas.setBaseline(ie.get(PROPERTY_BASELINE, BaselineGaussianMeasurement.class));
			meas.setRelative(ie.get(PROPERTY_RELATIVE, GaussianMeasurement.class));
		}
		@Override
		public void write(RelativeGaussianMeasurementBase meas, OutputElement oe) throws XMLStreamException {
			oe.add(meas.getBaseline(), PROPERTY_BASELINE, BaselineGaussianMeasurement.class);
			oe.add(meas.getRelative(), PROPERTY_RELATIVE, GaussianMeasurement.class);
		}
	};
}