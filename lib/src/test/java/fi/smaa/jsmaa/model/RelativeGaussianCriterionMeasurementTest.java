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
package fi.smaa.jsmaa.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;

public class RelativeGaussianCriterionMeasurementTest {
	private MultivariateGaussianCriterionMeasurement delta;
	private GaussianMeasurement baseline;
	private List<Alternative> alternatives;
	private RelativeGaussianCriterionMeasurement m;

	@Before
	public void setUp() {
		alternatives = new ArrayList<Alternative>(Arrays.asList(
				new Alternative("SuSE"), new Alternative("Mandriva"), new Alternative("Yellow Dog")));
		delta = new MultivariateGaussianCriterionMeasurement(alternatives);
		baseline = new GaussianMeasurement();
		m = new RelativeGaussianCriterionMeasurement(delta, baseline);
	}
	
	@Test
	public void testInitialization() {
		assertSame(delta, m.getRelativeMeasurement());
		assertSame(baseline, m.getBaselineMeasurement());
		assertEquals(alternatives, m.getAlternatives());
	}
	
	@Test
	public void testModifyAlternatives() {
		Alternative alt = new Alternative("Knoppix");
		alternatives.add(alt);
		assertFalse(alternatives.size() == m.getAlternatives().size());
		m.addAlternative(alt);
		assertEquals(alternatives, m.getAlternatives());
		assertEquals(alternatives, delta.getAlternatives());
		
		alternatives.remove(alt);
		m.deleteAlternative(alt);
		assertEquals(alternatives, m.getAlternatives());
		assertEquals(alternatives, delta.getAlternatives());

		Collections.sort(alternatives);
		m.reorderAlternatives(alternatives);
		assertEquals(alternatives, m.getAlternatives());
		assertEquals(alternatives, delta.getAlternatives());
	}
	
	@Test
	public void testSample() {
		delta.setMeanVector(new ArrayRealVector(new double[] { 14.0, 10.2, 3.6 }));
		delta.setCovarianceMatrix(MatrixUtils.createRealMatrix(3, 3));
		baseline.setMean(-2.0);
		baseline.setStDev(0.0);
		
		double[][] target = new double[2][3];
		m.sample(RandomUtil.createWithRandomSeed(), target, 1);
		assertArrayEquals(new double[3], target[0], 0.0);
		assertArrayEquals(new double[] { 12.0, 8.2, 1.6 }, target[1], 1e-12);
		
		baseline.setStDev(1.0);
		delta.setMeanVector(new ArrayRealVector(new double[3]));
		m.sample(RandomUtil.createWithRandomSeed(), target, 1);
		assertEquals(target[1][0], target[1][1], 0.0);
		assertEquals(target[1][0], target[1][2], 0.0);
	}
	
	@Test
	public void testRange() {
		delta.setMeanVector(new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 }));
		delta.setCovarianceMatrix(MatrixUtils.createRealDiagonalMatrix(new double[] { 14.0, 10.2, 3.6 }));
		baseline.setMean(-2.0);
		baseline.setStDev(2.5);
		
		assertEquals(delta.getRange().getStart() + baseline.getRange().getStart(), m.getRange().getStart(), 1e-7);
		assertEquals(delta.getRange().getEnd() + baseline.getRange().getEnd(), m.getRange().getEnd(), 1e-7);
	}
	
	@Test
	public void testDeepCopy() {
		List<Alternative> newAlts = new ArrayList<Alternative>();
		for (Alternative a : alternatives) {
			newAlts.add(a.deepCopy());
		}
		RelativeGaussianCriterionMeasurement clone = m.deepCopy(newAlts);
		assertEquals(newAlts, clone.getAlternatives());
		assertEquals(newAlts, clone.getRelativeMeasurement().getAlternatives());
		assertSame(newAlts.get(0), clone.getAlternatives().get(0));
		assertNotSame(m.getBaselineMeasurement(), clone.getBaselineMeasurement());
		assertNotSame(m.getRelativeMeasurement(), clone.getRelativeMeasurement());
	}
}
