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
package fi.smaa.jsmaa.model;

import fi.smaa.common.RandomUtil;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;


public class LogNormalMeasurement extends GaussianMeasurement {
	
	private static final long serialVersionUID = -562511137486161262L;

	public LogNormalMeasurement(double mean, double stdev) {
		super(mean, stdev);
	}
	
	public LogNormalMeasurement() {
		super();
	}
	
	/**
	 * Derives range of 95% confidence interval.
	 */
	@Override
	public Interval getRange() {
		return new Interval(Math.exp(mean - (stDev * 1.96)),
				Math.exp(mean + (stDev * 1.96)));
	}	

	@Override
	public LogNormalMeasurement deepCopy() {
		return new LogNormalMeasurement(mean, stDev);
	}
	
	@Override
	public double sample(RandomUtil random) {
		return Math.exp(super.sample(random));
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof LogNormalMeasurement)) {
			return false;
		}
		return super.valueEquals((LogNormalMeasurement)other);
	}
	

	protected static final XMLFormat<LogNormalMeasurement> XML = new XMLFormat<LogNormalMeasurement>(LogNormalMeasurement.class) {
		@Override
		public LogNormalMeasurement newInstance(Class<LogNormalMeasurement> cls, InputElement ie) throws XMLStreamException {
			return new LogNormalMeasurement(ie.getAttribute("mean").toDouble(),
					ie.getAttribute("stdev").toDouble());
		}				
		@Override
		public boolean isReferenceable() {
			return GaussianMeasurement.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, LogNormalMeasurement meas) throws XMLStreamException {
			GaussianMeasurement.XML.read(ie, meas);			
		}
		@Override
		public void write(LogNormalMeasurement meas, OutputElement oe) throws XMLStreamException {
			GaussianMeasurement.XML.write(meas, oe);
		}
	};
	
}
