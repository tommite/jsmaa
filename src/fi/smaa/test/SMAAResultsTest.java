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

package fi.smaa.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.SMAAModel;
import fi.smaa.SMAAResults;
import fi.smaa.SMAAResultsListener;

public class SMAAResultsTest {
	
	private SMAAResults results;
	private SMAAModel model;
	private TestData testData;
	
	private double[] weights1;
	private double[] weights2;
	private Integer[] firstFirst;
	private Integer[] secondFirst;
	
	
	@Before
	public void setUp() {
		weights1 = new double[]{0.0, 1.0, 0.0};	
		weights2 = new double[]{1.0, 0.0, 0.0};
		firstFirst = new Integer[]{0, 1};
		secondFirst = new Integer[]{1, 0};		
		testData = new TestData();
		model = testData.model;
		results = new SMAAResults(model.getAlternatives(), model.getCriteria(), 10);
	}
	
	@Test
	public void test10HitsCWs() {
		do10Hits();
		
		List<Double> cw1 = results.getCentralWeightVectors().get(model.getAlternatives().get(0));
		List<Double> cw2 = results.getCentralWeightVectors().get(model.getAlternatives().get(1));
		
		List<Double> ex1 = Arrays.asList(new Double[]{0.0, 1.0, 0.0});
		List<Double> ex2 = Arrays.asList(new Double[]{1.0, 0.0, 0.0});		
		
		assertEquals(ex1, cw1);
		assertEquals(ex2, cw2);		
	}
	
	@Test
	public void testEqualsCWs() {
		Integer[] ranks = new Integer[]{0, 0};
		
		for (int i=0;i<=10;i++) {
			double third = 1.0 / 3.0;
			double w1 = third * (i/5.0);
			double w3 = third * ((10-i)/5.0);
			double[] weights = new double[]{w1, third, w3};
			results.update(ranks, weights);
		}
		
		List<Double> cw1 = results.getCentralWeightVectors().get(model.getAlternatives().get(0));
		List<Double> cw2 = results.getCentralWeightVectors().get(model.getAlternatives().get(1));
		assertTrue(cw1.equals(cw2));		
	}	

	private void do10Hits() {		
		for (int i=0;i<5;i++) {
			results.update(firstFirst, weights1);
		}
		for (int i=0;i<5;i++) {
			results.update(secondFirst, weights2);
		}
	}
	
	@Test
	public void testCorrectRankAcceptabilities() {
		secondFirst = new Integer[] { 0, 1 };
		do10Hits();

		List<Double> ra1 = results.getRankAcceptabilities().get(testData.alt1);
		List<Double> ra2 = results.getRankAcceptabilities().get(testData.alt2);

		assertEquals(1.0, ra1.get(0), 0.00001);
		assertEquals(0.0, ra1.get(1), 0.00001);

		assertEquals(0.0, ra2.get(0), 0.00001);
		assertEquals(1.0, ra2.get(1), 0.00001);

	}
	
	@Test
	public void testReset() {
		do10Hits();
		
		List<Double> cw1 = results.getCentralWeightVectors().get(model.getAlternatives().get(0));
		
		results.reset();
		
		Map<Alternative, List<Double>> raccs = results.getRankAcceptabilities();
		for (List<Double> list : raccs.values()) {
			for (Double d : list) {
				assertTrue(d.equals(Double.NaN));
			}
		}
		
		assertTrue(cw1.get(0).equals(Double.NaN));
		assertTrue(cw1.get(1).equals(Double.NaN));
		assertTrue(cw1.get(2).equals(Double.NaN));
	}

	
	@Test
	public void testListenerFiresCorrectAmount() {
		do10Hits();
		SMAAResultsListener mock = createMock(SMAAResultsListener.class);
		mock.resultsChanged();
		replay(mock);
		results.addResultsListener(mock);
		do10Hits();
		verify(mock);
	}
	
}
