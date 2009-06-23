/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

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

package fi.smaa.jsmaa.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.CriterionTypeChooser;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.AlternativeExistsException;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogNormalMeasurement;
import fi.smaa.jsmaa.model.NoSuchAlternativeException;
import fi.smaa.jsmaa.model.NoSuchCriterionException;
import fi.smaa.jsmaa.model.SMAAModel;

public class CriterionTypeChooserTest {

	CriterionTypeChooser chooser;
	private CardinalCriterion crit;
	private ImpactMatrix matrix;
	private Alternative alt;
	private SMAAModel model;
	
	@Before
	public void setUp() throws NoSuchAlternativeException, NoSuchCriterionException, AlternativeExistsException {
		setupModel();
		chooser = new CriterionTypeChooser(matrix, alt, crit);
	}
	
	@Test
	public void testConstructor() throws Exception {
		assertEquals(0, chooser.getSelectedIndex());
		matrix.setMeasurement(crit, alt, new GaussianMeasurement());
		chooser = new CriterionTypeChooser(matrix, alt, crit);
		assertEquals(1, chooser.getSelectedIndex());
	}
	
	@Test
	public void testChangesMeasurementInModel() throws Exception {
		chooser.setSelectedIndex(1);
		assertTrue(matrix.getMeasurement(crit, alt) instanceof GaussianMeasurement);		
		chooser.setSelectedIndex(2);
		assertTrue(matrix.getMeasurement(crit, alt) instanceof LogNormalMeasurement);	
	}
	
	private void setupModel() throws AlternativeExistsException, NoSuchAlternativeException, NoSuchCriterionException {
		model = new SMAAModel("model");
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a2");
		CardinalCriterion c1 = new CardinalCriterion("c1");
		CardinalCriterion c2 = new CardinalCriterion("c2");
		model.addAlternative(a1);
		model.addAlternative(a2);
		model.addCriterion(c1);
		model.addCriterion(c2);
		model.getImpactMatrix().setMeasurement(c1, a1, new Interval(0.0, 6.0));
		alt = a1;
		crit = c1;
		matrix = model.getImpactMatrix();
	}		
}
