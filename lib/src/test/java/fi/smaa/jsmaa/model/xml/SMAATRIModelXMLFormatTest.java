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

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Category;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRIModelXMLFormatTest {

	@Test
	public void testMarshallSMAATRIModel() throws XMLStreamException {
		Category cat1 = new Category("cat1");
		Category cat2 = new Category("cat2");
		OutrankingCriterion oc = new OutrankingCriterion("crit", false, new ExactMeasurement(1.0), new ExactMeasurement(2.0));
		
		SMAATRIModel mod = new SMAATRIModel("model");
		mod.setRule(false);
		mod.addCategory(cat1);
		mod.addCategory(cat2);
		mod.addCriterion(oc);
		mod.getLambda().setEnd(0.9);
		mod.getLambda().setStart(0.7);
		
		mod.setCategoryUpperBound(oc, cat1, new Interval(0.0, 1.0));
		
		SMAATRIModel nmod = XMLHelper.fromXml(XMLHelper.toXml(mod, SMAATRIModel.class));
		
		assertEquals(2, nmod.getCategories().size());
		assertEquals(cat1.getName(), nmod.getCategories().get(0).getName());
		assertEquals(cat2.getName(), nmod.getCategories().get(1).getName());
		
		assertEquals(false, nmod.getRule());
		assertEquals(new Interval(0.0, 1.0), nmod.getCategoryUpperBound((OutrankingCriterion) nmod.getCriteria().get(0), nmod.getCategories().get(0)));
		assertEquals(new Interval(0.7, 0.9), nmod.getLambda());
	}
}
