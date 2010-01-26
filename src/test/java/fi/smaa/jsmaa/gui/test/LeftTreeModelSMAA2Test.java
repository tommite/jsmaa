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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAA2;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.SMAA2Model;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class LeftTreeModelSMAA2Test {
	
	private LeftTreeModelSMAA2 treeModel;
	private SMAA2Model smaaModel;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private ScaleCriterion crit1 = new ScaleCriterion("crit1");
	
	@Before
	public void setUp() {
		smaaModel = createSmaaModel();
		treeModel = new LeftTreeModelSMAA2(smaaModel);
	}
	
	private SMAA2Model createSmaaModel() {
		SMAA2Model model = new SMAA2Model("model");
		model.addAlternative(alt1);
		model.addAlternative(alt2);
		model.addCriterion(crit1);
		return model;
	}
	
	@Test
	public void testGetRoot() {
		assertNotNull(treeModel.getRoot());
	}

	@Test
	public void testChildOfRoot() {
		assertEquals("Alternatives", treeModel.getChild(treeModel.getRoot(), 0));
		assertEquals("Criteria", treeModel.getChild(treeModel.getRoot(), 1));
		assertEquals("Preferences", treeModel.getChild(treeModel.getRoot(), 2));		
		assertEquals("Results", treeModel.getChild(treeModel.getRoot(), 3));
	}
	
	@Test
	public void testNumChildrenRoot() {
		assertEquals(4, treeModel.getChildCount(treeModel.getRoot()));		
	}
	
	private Object getAlternativeNode() {
		return treeModel.getChild(treeModel.getRoot(), 0);
	}
	
	private Object getCriteriaNode() {
		return treeModel.getChild(treeModel.getRoot(), 1);
	}
	
	private Object getPreferencesNode() {
		return treeModel.getChild(treeModel.getRoot(), 2);
	}
	
	private Object getResultsNode() {
		return treeModel.getChild(treeModel.getRoot(), 3);
	}
	
	private Object getRankAccNode() {
		return treeModel.getChild(getResultsNode(), 0);
	}
	
	private Object getCentralWeightNode() {
		return treeModel.getChild(getResultsNode(), 1);
	}
	
	@Test
	public void testAlternativeNodes() {
		assertEquals(2, treeModel.getChildCount(getAlternativeNode()));
		assertEquals(0, treeModel.getChildCount(alt1));
		assertTrue(treeModel.isLeaf(alt1));
		assertFalse(treeModel.isLeaf(getAlternativeNode()));
	}
	
	@Test
	public void testCriteriaNodes() {
		assertEquals(1, treeModel.getChildCount(getCriteriaNode()));
		assertEquals(0, treeModel.getChildCount(crit1));
		assertTrue(treeModel.isLeaf(crit1));
		assertFalse(treeModel.isLeaf(getCriteriaNode()));
	}
	
	@Test
	public void testResultsNodes() {
		assertEquals(2, treeModel.getChildCount(getResultsNode()));
		assertEquals(0, treeModel.getChildCount(getRankAccNode()));
		assertEquals(0, treeModel.getChildCount(getCentralWeightNode()));
		
		assertTrue(treeModel.isLeaf(getRankAccNode()));
		assertTrue(treeModel.isLeaf(getCentralWeightNode()));
		assertFalse(treeModel.isLeaf(getResultsNode()));
	}
	
	@Test
	public void testPreferenceNodes() {
		assertTrue(treeModel.isLeaf(getPreferencesNode()));
	}
	
	@Test
	public void testEmptyAltsCrits() {
		smaaModel = new SMAA2Model("model");
		treeModel = new LeftTreeModelSMAA2(smaaModel);
		
		assertFalse(treeModel.isLeaf(getAlternativeNode()));
		assertFalse(treeModel.isLeaf(getCriteriaNode()));
		assertFalse(treeModel.isLeaf(treeModel.getRoot()));
		assertEquals(0, treeModel.getChildCount(getAlternativeNode()));	
	}
	
	@Test
	public void testGetIndexOfChild() {
		assertEquals(0, treeModel.getIndexOfChild(treeModel.getRoot(), getAlternativeNode()));
		assertEquals(1, treeModel.getIndexOfChild(treeModel.getRoot(), getCriteriaNode()));
		assertEquals(2, treeModel.getIndexOfChild(treeModel.getRoot(), getPreferencesNode()));
		assertEquals(3, treeModel.getIndexOfChild(treeModel.getRoot(), getResultsNode()));
		
		assertEquals(0, treeModel.getIndexOfChild(getAlternativeNode(), alt1));
		assertEquals(1, treeModel.getIndexOfChild(getAlternativeNode(), alt2));
		
		assertEquals(0, treeModel.getIndexOfChild(getCriteriaNode(), crit1));
		
		assertEquals(0, treeModel.getIndexOfChild(getResultsNode(), getRankAccNode()));
		assertEquals(1, treeModel.getIndexOfChild(getResultsNode(), getCentralWeightNode()));
	}

	@Test
	public void testGetChildrenAlternatives() {
		assertEquals(alt1, treeModel.getChild(getAlternativeNode(), 0));
	}

	@Test
	public void testGetChildrenCriteria() {
		assertEquals(crit1, treeModel.getChild(getCriteriaNode(), 0));
	}
	
	@Test
	public void testGetChildrenResults() {
		assertEquals(getRankAccNode(), treeModel.getChild(getResultsNode(), 0));
		assertEquals(getCentralWeightNode(), treeModel.getChild(getResultsNode(), 1));
	}
}
