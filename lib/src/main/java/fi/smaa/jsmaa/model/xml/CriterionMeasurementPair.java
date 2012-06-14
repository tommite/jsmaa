/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
