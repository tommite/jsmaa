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
