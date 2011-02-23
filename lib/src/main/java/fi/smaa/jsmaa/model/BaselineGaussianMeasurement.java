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
public class BaselineGaussianMeasurement extends GaussianMeasurement {
	private static final long serialVersionUID = 5221976399470534972L;
	private Double d_sample = null;

	public BaselineGaussianMeasurement(Double mean, Double stdDev) {
		super(mean, stdDev);
	}
	
	public BaselineGaussianMeasurement() {
		super();
	}
	
	/**
	 * Advance to the next state.
	 */
	public void update() {
		d_sample = super.sample();
	}
	
	public double sample() {
		if (d_sample == null) {
			throw new IllegalStateException(getClass().getSimpleName() + " requires calling update() before sample()");
		}
		return d_sample;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other.getClass().equals(BaselineGaussianMeasurement.class))) {
			return false;
		}
		BaselineGaussianMeasurement go = (BaselineGaussianMeasurement) other;
		return valueEquals(go);
	}
	
	public BaselineGaussianMeasurement deepCopy() {
		return new BaselineGaussianMeasurement(getMean(), getStDev());
	}
	
	protected static final XMLFormat<BaselineGaussianMeasurement> XML = new XMLFormat<BaselineGaussianMeasurement>(BaselineGaussianMeasurement.class) {
		@Override
		public BaselineGaussianMeasurement newInstance(Class<BaselineGaussianMeasurement> cls, InputElement ie) throws XMLStreamException {
			return new BaselineGaussianMeasurement();
		}				
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public void read(InputElement ie, BaselineGaussianMeasurement meas) throws XMLStreamException {
			GaussianMeasurement.XML.read(ie, meas);	
		}
		@Override
		public void write(BaselineGaussianMeasurement meas, OutputElement oe) throws XMLStreamException {
			GaussianMeasurement.XML.write(meas, oe);
		}
	};
}
