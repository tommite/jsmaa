/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
