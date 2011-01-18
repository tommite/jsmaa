package fi.smaa.jsmaa.model;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class RelativeLogitNormalMeasurement extends RelativeGaussianMeasurementBase {
	private static final long serialVersionUID = 8466978166600150834L;
	public RelativeLogitNormalMeasurement(ReferenceableGaussianMeasurement baseline, GaussianMeasurement relative) {
		super(baseline, relative);
	}
	
	public RelativeLogitNormalMeasurement(ReferenceableGaussianMeasurement baseline) {
		this(baseline, new GaussianMeasurement());
	}
	
	private RelativeLogitNormalMeasurement() {
		this(new ReferenceableGaussianMeasurement());
	}
	
	@Override
	protected LogitNormalMeasurement getAbsolute() {
		return (new LogitNormalMeasurement(getAbsoluteMean(), getAbsoluteStdDev()));
	}
	
	@Override
	protected RelativeGaussianMeasurementBase newInstance() {
		return new RelativeLogitNormalMeasurement();
	}

	protected static final XMLFormat<RelativeLogitNormalMeasurement> XML = new XMLFormat<RelativeLogitNormalMeasurement>(RelativeLogitNormalMeasurement.class) {
		@Override
		public RelativeLogitNormalMeasurement newInstance(Class<RelativeLogitNormalMeasurement> cls, InputElement xml) {
			return new RelativeLogitNormalMeasurement();
		};
		@Override
		public boolean isReferenceable() {
			return RelativeGaussianMeasurementBase.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, RelativeLogitNormalMeasurement meas) throws XMLStreamException {
			RelativeGaussianMeasurementBase.XML.read(ie, meas);
		}
		@Override
		public void write(RelativeLogitNormalMeasurement meas, OutputElement oe) throws XMLStreamException {
			RelativeGaussianMeasurementBase.XML.write(meas, oe);
		}
	};
}
