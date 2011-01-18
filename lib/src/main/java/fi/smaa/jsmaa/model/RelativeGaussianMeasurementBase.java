package fi.smaa.jsmaa.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public abstract class RelativeGaussianMeasurementBase extends CardinalMeasurement {
	private static final long serialVersionUID = -4052031951942599415L;

	public static final String PROPERTY_BASELINE = "baseline";

	public static final String PROPERTY_RELATIVE = "relative";
	private ReferenceableGaussianMeasurement d_baseline;
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
			ReferenceableGaussianMeasurement baseline,
			GaussianMeasurement relative) {
		d_baseline = baseline;
		d_baseline.addPropertyChangeListener(nestedListener);
		d_relative = relative;
		d_relative.addPropertyChangeListener(nestedListener);
	}

	protected abstract CardinalMeasurement getAbsolute();

	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		RelativeGaussianMeasurementBase m = (RelativeGaussianMeasurementBase)obj;
		return m.getBaseline().equals(getBaseline()) && m.getRelative().equals(getRelative());
	}

	@Override
	public String toString() {
		return "ilogit(" + d_baseline.toString() + " + " + d_relative.toString() + ")";
	}

	@Override
	public Interval getRange() {
		return getAbsolute().getRange();
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
	public double sample() {
		return getAbsolute().sample();
	}

	public ReferenceableGaussianMeasurement getBaseline() {
		return d_baseline;
	}

	private void setBaseline(ReferenceableGaussianMeasurement m) {
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
			meas.setBaseline(ie.get(PROPERTY_BASELINE, ReferenceableGaussianMeasurement.class));
			meas.setRelative(ie.get(PROPERTY_RELATIVE, GaussianMeasurement.class));
		}
		@Override
		public void write(RelativeGaussianMeasurementBase meas, OutputElement oe) throws XMLStreamException {
			oe.add(meas.getBaseline(), PROPERTY_BASELINE, ReferenceableGaussianMeasurement.class);
			oe.add(meas.getRelative(), PROPERTY_RELATIVE, GaussianMeasurement.class);
		}
	};
}