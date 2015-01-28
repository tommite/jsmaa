/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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
package fi.smaa.jsmaa.simulator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class SMAA2ResultsTest {
	
	private SMAA2Results results;
	
	private double[] weights1;
	private double[] weights2;
	private int[] firstFirst;
	private int[] secondFirst;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private ScaleCriterion c1 = new ScaleCriterion("c1");
	private ScaleCriterion c2 = new ScaleCriterion("c2");
	private ScaleCriterion c3 = new ScaleCriterion("c3");
	private List<Alternative> alts;
	private List<Criterion> crit;	
	
	@Before
	public void setUp()  {
		weights1 = new double[]{0.0, 1.0, 0.0};	
		weights2 = new double[]{1.0, 0.0, 0.0};
		firstFirst = new int[]{0, 1};
		secondFirst = new int[]{1, 0};
		alts = new ArrayList<Alternative>();
		crit = new ArrayList<Criterion>();
		alts.add(alt1);
		alts.add(alt2);
		crit.add(c1);
		crit.add(c2);
		crit.add(c3);
		
		results = new SMAA2Results(alts, crit, 10);
	}
	
	@Test
	public void testConfidenceFactors() {
		boolean[] hit = new boolean[2];
		for (int i=0;i<10;i++) {
			results.confidenceUpdate(hit);
		}
		hit[0] = true;		
		for (int i=0;i<10;i++) {
			results.confidenceUpdate(hit);
		}
		hit[1] = true;
		for (int i=0;i<10;i++) {
			results.confidenceUpdate(hit);
		}
		
		Map<Alternative, Double> cf = results.getConfidenceFactors();
		assertEquals(0.66, cf.get(alt1), 0.02);
		assertEquals(0.33, cf.get(alt2), 0.02);
	}
	
	@Test
	public void test10HitsCWs() {
		do10Hits();
		
		Map<Criterion, Double> cw1 = results.getCentralWeightVectors().get(alt1);
		Map<Criterion, Double> cw2 = results.getCentralWeightVectors().get(alt2);
				
		assertEquals(0.0, cw1.get(c1), 0.0001);
		assertEquals(1.0, cw1.get(c2), 0.0001);
		assertEquals(0.0, cw1.get(c3), 0.0001);
		
		assertEquals(1.0, cw2.get(c1), 0.0001);
		assertEquals(0.0, cw2.get(c2), 0.0001);
		assertEquals(0.0, cw2.get(c3), 0.0001);
	}
	
	@Test
	public void testEqualsCWs() {
		int[] ranks = new int[]{0, 0};
		
		for (int i=0;i<=10;i++) {
			double third = 1.0 / 3.0;
			double w1 = third * (i/5.0);
			double w3 = third * ((10-i)/5.0);
			double[] weights = new double[]{w1, third, w3};
			results.update(ranks, weights);
		}
		
		Map<Criterion, Double> cw1 = results.getCentralWeightVectors().get(alt1);
		Map<Criterion, Double> cw2 = results.getCentralWeightVectors().get(alt2);
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
		secondFirst = new int[] { 0, 1 };
		do10Hits();

		List<Double> ra1 = results.getRankAcceptabilities().get(alt1);
		List<Double> ra2 = results.getRankAcceptabilities().get(alt2);

		assertEquals(1.0, ra1.get(0), 0.00001);
		assertEquals(0.0, ra1.get(1), 0.00001);

		assertEquals(0.0, ra2.get(0), 0.00001);
		assertEquals(1.0, ra2.get(1), 0.00001);

	}
	
	@Test
	public void testReset() {
		do10Hits();
		
		Map<Criterion, Double> cw1 = results.getCentralWeightVectors().get(alt1);
		assertFalse(cw1.get(c1).equals(Double.NaN));
		
		results.reset();
		cw1 = results.getCentralWeightVectors().get(alt2);		
		
		Map<Alternative, List<Double>> raccs = results.getRankAcceptabilities();
		for (List<Double> list : raccs.values()) {
			for (Double d : list) {
				assertTrue(d.equals(Double.NaN));
			}
		}
		assertTrue(cw1.get(c1).equals(Double.NaN));
		assertTrue(cw1.get(c2).equals(Double.NaN));
		assertTrue(cw1.get(c3).equals(Double.NaN));
	}

	
	@Test
	public void testListenerFiresCorrectAmount() {
		do10Hits();
		SMAAResultsListener mock = createMock(SMAAResultsListener.class);
		mock.resultsChanged((ResultsEvent) JUnitUtil.eqEventObject(new ResultsEvent(results)));
		replay(mock);
		results.addResultsListener(mock);
		do10Hits();
		verify(mock);
	}
	
}
