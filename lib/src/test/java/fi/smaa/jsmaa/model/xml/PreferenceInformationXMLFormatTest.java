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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javolution.xml.stream.XMLStreamException;

import org.drugis.common.JUnitUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class PreferenceInformationXMLFormatTest {
	
	private List<Criterion> crit;
	private Criterion c1;
	private Criterion c2;

	@Before
	public void setUp() {
		crit = new ArrayList<Criterion>();
		c1 = new ScaleCriterion("c");
		c2 = new ScaleCriterion("c2");	
		crit.add(c1);
		crit.add(c2);
	}

	@Test
	public void testMarshallCardinalPreferenceInformation() throws XMLStreamException {
		CardinalPreferenceInformation pref = new CardinalPreferenceInformation(crit);
		pref.setMeasurement(c1, new ExactMeasurement(1.0));
		pref.setMeasurement(c2, new GaussianMeasurement(1.0, 1.1));
		
		CardinalPreferenceInformation npref = (CardinalPreferenceInformation) XMLHelper.fromXml(
				XMLHelper.toXml(pref, CardinalPreferenceInformation.class));
		
		assertEquals(2, npref.getCriteria().size());
		
		Criterion nc1 = null;
		Criterion nc2 = null;
		
		if (npref.getCriteria().get(0).getName().equals("c")) {
			nc1 = npref.getCriteria().get(0);
			nc2 = npref.getCriteria().get(1);	
		} else {
			nc1 = npref.getCriteria().get(1);
			nc2 = npref.getCriteria().get(0);						
		}
		
		assertEquals(c1.getName(), nc1.getName());
		assertEquals(c2.getName(), nc2.getName());
		
		assertEquals(new ExactMeasurement(1.0), npref.getMeasurement(nc1));
		assertEquals(new GaussianMeasurement(1.0, 1.1), npref.getMeasurement(nc2));
		
		PropertyChangeListener mock = JUnitUtil.mockListener(npref, CardinalPreferenceInformation.PREFERENCES, null, null);
		npref.addPropertyChangeListener(mock);
		((ExactMeasurement) npref.getMeasurement(npref.getCriteria().get(0))).setValue(2.0);
		EasyMock.verify(mock);		
	}
	
	@Test
	public void testMarshallOrdinalPreferenceInformation() throws XMLStreamException {
		OrdinalPreferenceInformation pref = new OrdinalPreferenceInformation(crit);
		
		OrdinalPreferenceInformation npref = (OrdinalPreferenceInformation) XMLHelper.fromXml(
				XMLHelper.toXml(pref, OrdinalPreferenceInformation.class));
		
		assertEquals(2, npref.getCriteria().size());
		
		Criterion nc1 = null;
		Criterion nc2 = null;
		
		if (npref.getCriteria().get(0).getName().equals("c")) {
			nc1 = npref.getCriteria().get(0);
			nc2 = npref.getCriteria().get(1);			
		} else {
			nc1 = npref.getCriteria().get(1);
			nc2 = npref.getCriteria().get(0);						
		}
		
		assertEquals(c1.getName(), nc1.getName());
		assertEquals(c2.getName(), nc2.getName());
		
		assertEquals(new Rank(1), npref.getMeasurement(nc1));
		assertEquals(new Rank(2), npref.getMeasurement(nc2));		
		
		PropertyChangeListener mock = JUnitUtil.mockMultipleCallListener(npref, OrdinalPreferenceInformation.PREFERENCES, null, null);
		npref.addPropertyChangeListener(mock);
		npref.getMeasurement(npref.getCriteria().get(0)).setRank(2);
		EasyMock.verify(mock);
	}	
}
