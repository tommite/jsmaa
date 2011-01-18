package fi.smaa.jsmaa.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class RelativeLogitNormalMeasurement extends CardinalMeasurement {
	private static final long serialVersionUID = 8466978166600150834L;
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
	
	public RelativeLogitNormalMeasurement(ReferenceableGaussianMeasurement baseline, GaussianMeasurement relative) {
		d_baseline = baseline;
		d_baseline.addPropertyChangeListener(nestedListener);
		d_relative = relative;
		d_relative.addPropertyChangeListener(nestedListener);
	}
	
	public RelativeLogitNormalMeasurement(ReferenceableGaussianMeasurement baseline) {
		this(baseline, new GaussianMeasurement());
	}
	
	private RelativeLogitNormalMeasurement() {
		this(new ReferenceableGaussianMeasurement());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(RelativeLogitNormalMeasurement.class)) {
			return false;
		}
		RelativeLogitNormalMeasurement m = (RelativeLogitNormalMeasurement)obj;
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

	private LogitNormalMeasurement getAbsolute() {
		return (new LogitNormalMeasurement(getAbsoluteMean(), getAbsoluteStdDev()));
	}

	private Double getAbsoluteStdDev() {
		double s1 = d_baseline.getStDev();
		double s2 = d_relative.getStDev();
		return Math.sqrt(s1 * s1 + s2 * s2);
	}

	private double getAbsoluteMean() {
		return d_baseline.getMean() + d_relative.getMean();
	}

	@Override
	public double sample() {
		return getAbsolute().sample();
	}

	public RelativeLogitNormalMeasurement deepCopy() {
		return new RelativeLogitNormalMeasurement(getBaseline().deepCopy(), getRelative().deepCopy());
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
	
	protected static final XMLFormat<RelativeLogitNormalMeasurement> XML = new XMLFormat<RelativeLogitNormalMeasurement>(RelativeLogitNormalMeasurement.class) {
		@Override
		public RelativeLogitNormalMeasurement newInstance(Class<RelativeLogitNormalMeasurement> cls, InputElement ie) throws XMLStreamException {
			return new RelativeLogitNormalMeasurement();
		}				
		@Override
		public boolean isReferenceable() {
			return GaussianMeasurement.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, RelativeLogitNormalMeasurement meas) throws XMLStreamException {
			meas.setBaseline(ie.get(PROPERTY_BASELINE, ReferenceableGaussianMeasurement.class));
			meas.setRelative(ie.get(PROPERTY_RELATIVE, GaussianMeasurement.class));
		}
		@Override
		public void write(RelativeLogitNormalMeasurement meas, OutputElement oe) throws XMLStreamException {
			oe.add(meas.getBaseline(), PROPERTY_BASELINE, ReferenceableGaussianMeasurement.class);
			oe.add(meas.getRelative(), PROPERTY_RELATIVE, GaussianMeasurement.class);
		}
	};
}
