package fi.smaa.jsmaa.model.xml;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Measurement;

public class CriterionAlternativeMeasurement extends CriterionMeasurementPair {

	private Alternative alternative;

	public CriterionAlternativeMeasurement(Alternative a, Criterion c, Measurement m) {
		super(c, m);
		this.alternative = a;
	}
		
	public Alternative getAlternative() {
		return alternative;
	}
	
	@Override
	public String toString() {
		return "Alternative=["+alternative+"], "+super.toString();
	}

	@SuppressWarnings("unused")
	private static final XMLFormat<CriterionAlternativeMeasurement> XML 
		= new XMLFormat<CriterionAlternativeMeasurement>(CriterionAlternativeMeasurement.class) {		
		@Override
		public boolean isReferenceable() {
			return CriterionMeasurementPair.XML.isReferenceable();
		}
		@Override
		public CriterionAlternativeMeasurement newInstance(Class<CriterionAlternativeMeasurement> cls, InputElement ie) throws XMLStreamException {
			return new CriterionAlternativeMeasurement(null, null, null);
		}
		@Override
		public void read(InputElement ie, CriterionAlternativeMeasurement pair) throws XMLStreamException {
			CriterionMeasurementPair.XML.read(ie, pair);
			pair.alternative = ie.get("alternative", Alternative.class);
		}
		@Override
		public void write(CriterionAlternativeMeasurement pair, OutputElement oe) throws XMLStreamException {
			CriterionMeasurementPair.XML.write(pair, oe);			
			oe.add(pair.alternative, "alternative", Alternative.class);
		}
	};	

}
