/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class CriterionXMLFormatTest {

	@Test
	public void testMarshallOutrankingCriterion() throws XMLStreamException {
		OutrankingCriterion crit = new OutrankingCriterion("crit", false, new ExactMeasurement(1.0), new Interval(1.0, 2.0));
		OutrankingCriterion nCrit = (OutrankingCriterion) XMLHelper.fromXml(XMLHelper.toXml(crit, OutrankingCriterion.class));
		
		assertEquals(crit.getName(), nCrit.getName());
		assertEquals(crit.getAscending(), nCrit.getAscending());
		assertEquals(crit.getIndifMeasurement(), nCrit.getIndifMeasurement());
		assertEquals(crit.getPrefMeasurement(), nCrit.getPrefMeasurement());
	}
	
	@Test
	public void testMarshallOrdinalCriterion() throws XMLStreamException {
		OrdinalCriterion crit = new OrdinalCriterion("crit");
		OrdinalCriterion nCrit = (OrdinalCriterion) XMLHelper.fromXml(XMLHelper.toXml(crit, OrdinalCriterion.class));
		assertEquals(crit.getName(), nCrit.getName());
	}
	
	@Test
	public void testMarshallCardinalCriterion() throws XMLStreamException {
		ScaleCriterion crit = new ScaleCriterion("crit", false);
		ScaleCriterion nCrit = (ScaleCriterion) XMLHelper.fromXml(XMLHelper.toXml(crit, ScaleCriterion.class));
		
		assertEquals(crit.getName(), nCrit.getName());
		assertEquals(crit.getAscending(), nCrit.getAscending());
	}
}
