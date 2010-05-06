package fi.smaa.jsmaa.model.xml;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Measurement;

public class CriterionMeasurementPair {
	
	private Criterion criterion;
	private Measurement measurement;

	public CriterionMeasurementPair(Criterion c, Measurement m) {
		this.criterion = c;
		this.measurement = m;
	}
	
	public Criterion getCriterion() {
		return criterion;
	}
	
	public Measurement getMeasurement() {
		return measurement;
	}
	
	@Override
	public String toString() {
		return "Criterion=["+criterion+"], Measurement=["+measurement+"]";
	}
	
	protected static final XMLFormat<CriterionMeasurementPair> XML = new XMLFormat<CriterionMeasurementPair>(CriterionMeasurementPair.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public CriterionMeasurementPair newInstance(Class<CriterionMeasurementPair> cls, InputElement ie) throws XMLStreamException {
			return new CriterionMeasurementPair(null, null);
		}
		@Override
		public void read(InputElement ie, CriterionMeasurementPair pair) throws XMLStreamException {
			pair.criterion = ie.get("criterion");
			pair.measurement = ie.get("value");
		}
		@Override
		public void write(CriterionMeasurementPair pair, OutputElement oe) throws XMLStreamException {
			oe.add(pair.criterion, "criterion");
			oe.add(pair.measurement, "value");	
		}
	};	
}
