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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAATRI;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class LeftTreeModelSMAATRITest {
	
	private LeftTreeModelSMAATRI treeModel;
	private SMAATRIModel smaaModel;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private OutrankingCriterion crit1 = new OutrankingCriterion("crit1", true, 
			new Interval(0.0, 0.0), new Interval(1.0, 1.0));
	private Alternative cat1 = new Alternative("prof");
	
	@Before
	public void setUp() {
		smaaModel = createSmaaModel();
		treeModel = new LeftTreeModelSMAATRI(smaaModel);
	}
	
	private SMAATRIModel createSmaaModel() {
		SMAATRIModel model = new SMAATRIModel("model");
		model.addAlternative(alt1);
		model.addAlternative(alt2);
		model.addCriterion(crit1);
		model.addCategory(cat1);
		return model;
	}
	
	private Object getResultsNode() {
		return treeModel.getChild(treeModel.getRoot(), 4);
	}	
	
	private Object getCategoriesNode() {
		return treeModel.getChild(treeModel.getRoot(), 2);		
	}
	
	private Object getPreferencesNode() {
		return treeModel.getChild(treeModel.getRoot(), 3);		
	}
	
	
	@Test
	public void testGetChildOfRoot() {
		assertEquals("Categories", treeModel.getChild(treeModel.getRoot(), 2));
		assertEquals("Results", treeModel.getChild(treeModel.getRoot(), 4));
		assertEquals("Preferences", treeModel.getChild(treeModel.getRoot(), 3));
	}
	
	@Test
	public void testNumChildrenRoot() {
		assertEquals(5, treeModel.getChildCount(treeModel.getRoot()));				
	}
	
	@Test
	public void testCategoriesNodes() {
		assertEquals(1, treeModel.getChildCount(getCategoriesNode()));
		assertEquals(0, treeModel.getChildCount(cat1));
		assertTrue(treeModel.isLeaf(cat1));
		assertFalse(treeModel.isLeaf(getCategoriesNode()));
	}	
	
	@Test
	public void testEmptyAltsCrits() {
		smaaModel = new SMAATRIModel("model");
		treeModel = new LeftTreeModelSMAATRI(smaaModel);
		
		assertFalse(treeModel.isLeaf(getCategoriesNode()));
		assertEquals(0, treeModel.getChildCount(getCategoriesNode()));	
	}
	
	@Test
	public void testGetIndexOfChild() {
		assertEquals(2, treeModel.getIndexOfChild(treeModel.getRoot(), getCategoriesNode()));
		assertEquals(4, treeModel.getIndexOfChild(treeModel.getRoot(), getResultsNode()));
		assertEquals(3, treeModel.getIndexOfChild(treeModel.getRoot(), getPreferencesNode()));
		assertEquals(0, treeModel.getIndexOfChild(getCategoriesNode(), cat1));
	}
	
	@Test
	public void testGetChildrenCategories() {
		assertEquals(cat1, treeModel.getChild(getCategoriesNode(), 0));
	}	
	
	@Test
	public void testResultsNode() {
		assertEquals(1, treeModel.getChildCount(getResultsNode()));
		assertEquals(treeModel.getCatAccNode(), treeModel.getChild(getResultsNode(), 0));
	}
	
	@Test
	public void testCatAccNode() {
		assertTrue(treeModel.isLeaf(treeModel.getCatAccNode()));
	}
}
