package fi.smaa.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.Criterion;

public class CriterionTest {
	
	private Criterion criterion;
	
	private Criterion createInstance() {
		return  new Criterion() {
			public void sample(double[] target) {
			}
			public String getTypeLabel() {
				return null;
			}
			protected void updateMeasurements() {
			}
			public String measurementsToString() {
				return null;
			}			
		};
		
	}
	
	@Before
	public void setUp() {
		criterion = createInstance();
		criterion.setName("nameCrit");
	}
	
	@Test
	public void testConstructor() {
		Criterion c = createInstance();
		c.setName("c");
		assertEquals("c", c.getName());
	}
	
	@Test
	public void testSetName() {
		JUnitUtil.testSetter(criterion, Criterion.PROPERTY_NAME, "nameCrit", "name");
	}

	
	@Test
	public void testToString() {
		criterion.setName("crit");
		assertEquals("crit", criterion.toString());
	}
	
	@Test
	public void testSetAlternatives() {
		List<Alternative> list = new ArrayList<Alternative>();
		list.add(new Alternative("alt"));
		JUnitUtil.testSetter(criterion, Criterion.PROPERTY_ALTERNATIVES, new ArrayList<Alternative>(), list);
	}

}
