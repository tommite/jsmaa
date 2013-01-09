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
package fi.smaa.jsmaa.model;

import org.drugis.common.stat.Statistics;

import fi.smaa.common.RandomUtil;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class RelativeLogitNormalMeasurement extends RelativeGaussianMeasurementBase {
	private static final long serialVersionUID = 8466978166600150834L;
	public RelativeLogitNormalMeasurement(BaselineGaussianMeasurement baseline, GaussianMeasurement relative) {
		super(baseline, relative);
	}
	
	public RelativeLogitNormalMeasurement(BaselineGaussianMeasurement baseline) {
		this(baseline, new GaussianMeasurement());
	}
	
	private RelativeLogitNormalMeasurement() {
		this(new BaselineGaussianMeasurement());
	}
	
	@Override
	public double sample(RandomUtil random) {
		return Statistics.ilogit(super.sample(random));
	}
	
	private LogitNormalMeasurement getAbsolute() {
		return (new LogitNormalMeasurement(getAbsoluteMean(), getAbsoluteStdDev()));
	}

	@Override
	public Interval getRange() {
		return getAbsolute().getRange();
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
