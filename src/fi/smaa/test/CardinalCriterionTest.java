package fi.smaa.test;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.CardinalCriterion;
import fi.smaa.common.Interval;

public class CardinalCriterionTest {
	
	private CardinalCriterion criterion;
	
	@Before
	public void setUp() {
		criterion = new CardinalCriterion() {
			public void sample(double[] target) {
			}
			public String getTypeLabel() {
				return null;
			}
			public Interval getScale() {
				return new Interval(0.0, 1.0);
			}
			protected void updateMeasurements() {
			}
			public String measurementsToString() {
				return null;
			}
		};
		criterion.setName("name");
	}	

	@Test
	public void testSetAscending() {
		JUnitUtil.testSetter(criterion, CardinalCriterion.PROPERTY_ASCENDING, true, false);
	}

}
