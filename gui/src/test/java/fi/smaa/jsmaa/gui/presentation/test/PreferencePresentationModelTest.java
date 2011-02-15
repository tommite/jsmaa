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
package fi.smaa.jsmaa.gui.presentation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.MissingPreferenceInformation;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class PreferencePresentationModelTest {
	
	private PreferencePresentationModel pmodel;
	private SMAAModel model;
	private ScaleCriterion c1;
	private ScaleCriterion c2;
	
	@Before
	public void setUp() {
		model = new SMAAModel("model");
		
		c1 = new ScaleCriterion("c1");
		c2 = new ScaleCriterion("c2");
		model.addCriterion(c1);
		model.addCriterion(c2);
		pmodel = new PreferencePresentationModel(model, true);
	}
	
	@Test
	public void testGetModel() {
		ValueModel vModel = pmodel.getModel(PreferencePresentationModel.PREFERENCE_TYPE);
		assertEquals(PreferencePresentationModel.PreferenceType.MISSING, vModel.getValue());
	}

	@Test
	public void testSetPreferenceType() {
		JUnitUtil.testSetter(pmodel, PreferencePresentationModel.PREFERENCE_TYPE, pmodel.getPreferenceType(), PreferencePresentationModel.PreferenceType.ORDINAL);
		
		OrdinalPreferenceInformation pref = (OrdinalPreferenceInformation) model.getPreferenceInformation();		
		assertEquals(new Rank(1), pref.getRanks().get(0));
		assertEquals(new Rank(2), pref.getRanks().get(1));
		
		pmodel.setPreferenceType(PreferencePresentationModel.PreferenceType.MISSING);
		assertTrue(model.getPreferenceInformation().equals(new MissingPreferenceInformation(model.getCriteria().size())));
		
		pmodel.setPreferenceType(PreferencePresentationModel.PreferenceType.CARDINAL);
		CardinalPreferenceInformation card = (CardinalPreferenceInformation) model.getPreferenceInformation();
		assertTrue(card.getMeasurement(c1) instanceof CardinalMeasurement);
		assertTrue(card.getMeasurement(c2) instanceof CardinalMeasurement);
	}
	
	@Test
	public void testSetPreferenceTypeSetsOldPreferences() {
		pmodel.setPreferenceType(PreferencePresentationModel.PreferenceType.ORDINAL);
		OrdinalPreferenceInformation pref = (OrdinalPreferenceInformation) model.getPreferenceInformation();		
		pref.getRanks().get(0).setRank(2);
		
		pmodel.setPreferenceType(PreferencePresentationModel.PreferenceType.CARDINAL);
		pmodel.setPreferenceType(PreferencePresentationModel.PreferenceType.ORDINAL);

		pref = (OrdinalPreferenceInformation) model.getPreferenceInformation();		
		assertEquals(new Rank(2), pref.getRanks().get(0));
	
	}
}
