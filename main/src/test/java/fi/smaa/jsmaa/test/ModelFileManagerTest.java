package fi.smaa.jsmaa.test;

import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeListener;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.ModelFileManager;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class ModelFileManagerTest {

	private ModelFileManager mgr;
	private SMAAModel model;

	@Before
	public void setUp() {
		mgr = new ModelFileManager();
		model = new SMAAModel("mod");		
	}
	
	@Test
	public void testSetModel() {
		mgr.setSaved(false);
		JUnitUtil.testSetter(mgr, ModelFileManager.PROPERTY_MODEL, null, model);
		assertTrue(mgr.getSaved());
	}
	
	@Test
	public void testSetSaved() {
		assertFalse(mgr.getTitle().contains(new String("*")));		
		JUnitUtil.testSetter(mgr, ModelFileManager.PROPERTY_MODELSAVED, true, false);
		assertTrue(mgr.getTitle().contains(new String("*")));
	}	
	
	@Test
	public void testModelChangeSetsUnsaved() {
		ScaleCriterion c = new ScaleCriterion("x", true);
		model.addCriterion(c);
		mgr.setModel(model);
		
		PropertyChangeListener mock = JUnitUtil.mockListener(mgr, ModelFileManager.PROPERTY_MODELSAVED, true, false);
		mgr.addPropertyChangeListener(mock);
		c.setAscending(false);
		verify(mock);
	}
	
	@Test
	public void testModelNameSetsUnsaved() {
		mgr.setModel(model);
		
		PropertyChangeListener mock = JUnitUtil.mockListener(mgr, ModelFileManager.PROPERTY_MODELSAVED, true, false);
		mgr.addPropertyChangeListener(mock);
		model.setName("new");
		verify(mock);
	}	
}
