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

	@Override
	protected RelativeGaussianMeasurementBase newInstance() {
		return new RelativeNormalMeasurement();
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
