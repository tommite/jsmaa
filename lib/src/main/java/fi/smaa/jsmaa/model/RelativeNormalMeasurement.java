package fi.smaa.jsmaa.model;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class RelativeNormalMeasurement extends RelativeGaussianMeasurementBase {
	private static final long serialVersionUID = 4535236276314126215L;

	public RelativeNormalMeasurement(ReferenceableGaussianMeasurement baseline, GaussianMeasurement relative) {
		super(baseline, relative);
	}
	
	public RelativeNormalMeasurement(ReferenceableGaussianMeasurement baseline) {
		this(baseline, new GaussianMeasurement());
	}
	
	private RelativeNormalMeasurement() {
		this(new ReferenceableGaussianMeasurement());
	}
	
	@Override
	protected GaussianMeasurement getAbsolute() {
		return (new GaussianMeasurement(getAbsoluteMean(), getAbsoluteStdDev()));
	}

	public RelativeNormalMeasurement deepCopy() {
		return new RelativeNormalMeasurement(getBaseline().deepCopy(), getRelative().deepCopy());
	}
	
	protected static final XMLFormat<RelativeNormalMeasurement> XML = new XMLFormat<RelativeNormalMeasurement>(RelativeNormalMeasurement.class) {
		@Override
		public RelativeNormalMeasurement newInstance(Class<RelativeNormalMeasurement> cls, InputElement xml) {
			return new RelativeNormalMeasurement();
		};
		@Override
		public boolean isReferenceable() {
			return RelativeGaussianMeasurementBase.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, RelativeNormalMeasurement meas) throws XMLStreamException {
			RelativeGaussianMeasurementBase.XML.read(ie, meas);
		}
		@Override
		public void write(RelativeNormalMeasurement meas, OutputElement oe) throws XMLStreamException {
			RelativeGaussianMeasurementBase.XML.write(meas, oe);
		}
	};
}
