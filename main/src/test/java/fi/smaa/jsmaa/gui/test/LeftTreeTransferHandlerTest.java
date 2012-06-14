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
package fi.smaa.jsmaa.gui.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.LeftTreeTransferHandler;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Category;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class LeftTreeTransferHandlerTest {
	
	private SMAATRIModel smaaModel;
	private OutrankingCriterion crit1;
	private OutrankingCriterion crit2;
	private Alternative a1;
	private Alternative a2;
	private Category cat1;
	private Category cat2;
	private LeftTreeTransferHandler handler;

	@Before
	public void setUp() {
		smaaModel = new SMAATRIModel("name");
		crit1 = new OutrankingCriterion("o1", true, new ExactMeasurement(0.0), new ExactMeasurement(1.0));
		crit2 = new OutrankingCriterion("o2", true, new ExactMeasurement(0.0), new ExactMeasurement(1.0));
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		cat1 = new Category("c1");
		cat2 = new Category("c2");
		
		smaaModel.addAlternative(a1);
		smaaModel.addAlternative(a2);
		
		smaaModel.addCategory(cat1);
		smaaModel.addCategory(cat2);
		
		smaaModel.addCriterion(crit1);
		smaaModel.addCriterion(crit2);
		
		LeftTreeModel treeModel = new LeftTreeModel(smaaModel);
		handler = new LeftTreeTransferHandler(treeModel, smaaModel);
	}

	@Test
	public void testMoveCriterion() {
		handler.moveCriterion(crit2, 0);
		assertEquals(crit2, smaaModel.getCriteria().get(0));
		assertEquals(crit1, smaaModel.getCriteria().get(1));
	}	
	
	@Test
	public void testMoveAlternative() {
		handler.moveAlternative(a2, 0);
		assertEquals(a2, smaaModel.getAlternatives().get(0));
		assertEquals(a1, smaaModel.getAlternatives().get(1));
	}
	
	@Test
	public void testMoveCategory() {
		handler.moveCategory(cat2, 0);
		assertEquals(cat2, smaaModel.getCategories().get(0));
		assertEquals(cat1, smaaModel.getCategories().get(1));
	}
}
