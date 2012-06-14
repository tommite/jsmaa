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

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;

public class CriterionMeasurementPairXMLFormatTest {

	@Test
	public void testMarshallCriterionMeasurementPair() throws XMLStreamException {
		Rank m = new Rank(2);
		OrdinalCriterion c = new OrdinalCriterion("c");
		CriterionMeasurementPair p = new CriterionMeasurementPair(c, m);
		
		CriterionMeasurementPair np = (CriterionMeasurementPair) XMLHelper.fromXml(XMLHelper.toXml(p, CriterionMeasurementPair.class));
		
		assertEquals(c.getName(), np.getCriterion().getName());
		assertEquals(m, np.getMeasurement());
	}
}
