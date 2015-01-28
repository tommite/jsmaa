/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import java.util.ArrayList;

import javolution.xml.stream.XMLStreamException;
import junit.framework.Assert;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.DiscreteMeasurement;
import fi.smaa.jsmaa.model.Point2D;
import fi.smaa.jsmaa.model.PointOutsideIntervalException;

public class DiscreteMeasurementXMLFormatTest {

	@Test
	public void testDiscreteMeasurement() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(4, 0.5));
		points.add(new Point2D(5, 0.5));
		DiscreteMeasurement dm = null;
		try {
			dm = new DiscreteMeasurement(points);
		} catch (PointOutsideIntervalException e) {
			Assert.fail(e.getMessage());
		}

		DiscreteMeasurement nm = null;
		try {
			nm = (DiscreteMeasurement) XMLHelper.fromXml(XMLHelper.toXml(dm,
					DiscreteMeasurement.class));
		} catch (XMLStreamException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(dm, nm);
	}

}
