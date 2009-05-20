package fi.smaa.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.SMAAModel;
import fi.smaa.gui.LeftTreeModel;

public class LeftTreeModelTest {
	
	private LeftTreeModel treeModel;
	private SMAAModel smaaModel;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private Criterion crit1 = new GaussianCriterion("crit1");
	
	@Before
	public void setUp() {
		smaaModel = createSmaaModel();
		treeModel = new LeftTreeModel(smaaModel);
	}
	
	private SMAAModel createSmaaModel() {
		SMAAModel model = new SMAAModel();
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
		assertEquals("Results", treeModel.getChild(treeModel.getRoot(), 2));
	}
	
	@Test
	public void testNumChildrenRoot() {
		assertEquals(3, treeModel.getChildCount(treeModel.getRoot()));		
	}
	
	private Object getAlternativeNode() {
		return treeModel.getChild(treeModel.getRoot(), 0);
	}
	
	private Object getCriteriaNode() {
		return treeModel.getChild(treeModel.getRoot(), 1);
	}
	
	private Object getResultsNode() {
		return treeModel.getChild(treeModel.getRoot(), 2);
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
	public void testEmptyAltsCrits() {
		smaaModel = new SMAAModel("model");
		treeModel = new LeftTreeModel(smaaModel);
		
		assertFalse(treeModel.isLeaf(getAlternativeNode()));
		assertFalse(treeModel.isLeaf(getCriteriaNode()));
		assertFalse(treeModel.isLeaf(treeModel.getRoot()));
		assertEquals(0, treeModel.getChildCount(getAlternativeNode()));	
	}
	
	@Test
	public void testGetIndexOfChild() {
		assertEquals(0, treeModel.getIndexOfChild(treeModel.getRoot(), getAlternativeNode()));
		assertEquals(1, treeModel.getIndexOfChild(treeModel.getRoot(), getCriteriaNode()));
		
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
