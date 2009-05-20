package fi.smaa.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.SMAAModel;

public class SMAAModelTest {
	
	private SMAAModel model;
	
	@Before
	public void setUp() {
		model = new SMAAModel();
		model.setName("test");
	}

	@Test
	public void testSetName() {
		JUnitUtil.testSetter(model, SMAAModel.PROPERTY_NAME, "test", "modelName");
	}
	
	@Test
	public void testSetAlternatives() {
		List<Alternative> alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));

		JUnitUtil.testSetter(model, SMAAModel.PROPERTY_ALTERNATIVES, model.getAlternatives(), alts);
	}
	
	@Test
	public void testSetCriteria() {
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.add(new GaussianCriterion("c1"));
		crit.add(new GaussianCriterion("c2"));
		
		JUnitUtil.testSetter(model, SMAAModel.PROPERTY_CRITERIA, model.getCriteria(), crit);
	}
	
	@Test
	public void testAddAlternative() {
		Alternative a = new Alternative("alt");
		JUnitUtil.testAdder(model, SMAAModel.PROPERTY_ALTERNATIVES, "addAlternative", a);
	}
	
	@Test
	public void testToString() {
		SMAAModel mod = new SMAAModel("testModel");
		assertEquals("testModel", mod.toString());
	}
	
	@Test
	public void testAddCriterion() {
		Criterion c = new GaussianCriterion("gaus");
		JUnitUtil.testAdder(model, SMAAModel.PROPERTY_CRITERIA, "addCriterion", c);
	}
	
	@Test
	public void testDeleteAlternative() throws Exception {
		Alternative a = new Alternative("altToDelete");
		JUnitUtil.testDeleter(model, SMAAModel.PROPERTY_ALTERNATIVES, "deleteAlternative", a);
	}
}
