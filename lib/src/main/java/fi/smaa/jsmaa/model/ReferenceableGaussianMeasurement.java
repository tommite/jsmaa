/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 * A Gaussian measurement that may be referenced from other parts of the XML. 
 */
public class ReferenceableGaussianMeasurement extends GaussianMeasurement {
	private static final long serialVersionUID = 5221976399470534972L;

	public ReferenceableGaussianMeasurement(Double mean, Double stdDev) {
		super(mean, stdDev);
	}
	
	public ReferenceableGaussianMeasurement() {
		super();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other.getClass().equals(ReferenceableGaussianMeasurement.class))) {
			return false;
		}
		ReferenceableGaussianMeasurement go = (ReferenceableGaussianMeasurement) other;
		return valueEquals(go);
	}
	
	public ReferenceableGaussianMeasurement deepCopy() {
		return new ReferenceableGaussianMeasurement(getMean(), getStDev());
	}
	
	protected static final XMLFormat<ReferenceableGaussianMeasurement> XML = new XMLFormat<ReferenceableGaussianMeasurement>(ReferenceableGaussianMeasurement.class) {
		@Override
		public ReferenceableGaussianMeasurement newInstance(Class<ReferenceableGaussianMeasurement> cls, InputElement ie) throws XMLStreamException {
			return new ReferenceableGaussianMeasurement();
		}				
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public void read(InputElement ie, ReferenceableGaussianMeasurement meas) throws XMLStreamException {
			GaussianMeasurement.XML.read(ie, meas);	
		}
		@Override
		public void write(ReferenceableGaussianMeasurement meas, OutputElement oe) throws XMLStreamException {
			GaussianMeasurement.XML.write(meas, oe);
		}
	};
}
