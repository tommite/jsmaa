package fi.smaa.jsmaa.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAACEA;
import fi.smaa.jsmaa.model.SMAACEAModel;

public class LeftTreeModelSMAACEATest {

	private LeftTreeModelSMAACEA model;

	@Before
	public void setUp() {
		SMAACEAModel ceaModel = new SMAACEAModel("cea");
		model = new LeftTreeModelSMAACEA(ceaModel); 
	}
	
	@Test
	public void testGetChild() {
		assertEquals(model.getDataNode(), model.getChild(model.getRoot(), LeftTreeModelSMAACEA.DATA));
	}
	
	@Test
	public void testGetChildCount() {
		assertEquals(4, model.getChildCount(model.getRoot()));
	}
	
	@Test
	public void testGetIndexOfChild() {
		assertEquals(LeftTreeModelSMAACEA.DATA, model.getIndexOfChild(model.getRoot(), model.getDataNode()));
	}
	
	@Test
	public void testIsLeaf() {
		assertTrue(model.isLeaf(model.getDataNode()));
	}
}
