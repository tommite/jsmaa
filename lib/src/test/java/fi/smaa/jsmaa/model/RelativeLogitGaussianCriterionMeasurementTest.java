/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.drugis.common.stat.Statistics;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;

public class RelativeLogitGaussianCriterionMeasurementTest {
	private MultivariateGaussianCriterionMeasurement delta;
	private GaussianMeasurement baseline;
	private List<Alternative> alternatives;
	private RelativeGaussianCriterionMeasurement relative;
	private RelativeLogitGaussianCriterionMeasurement m;

	@Before
	public void setUp() {
		alternatives = new ArrayList<Alternative>(Arrays.asList(
				new Alternative("SuSE"), new Alternative("Mandriva"), new Alternative("Yellow Dog")));
		delta = new MultivariateGaussianCriterionMeasurement(alternatives);
		baseline = new GaussianMeasurement();
		relative = new RelativeGaussianCriterionMeasurement(delta, baseline);
		m = new RelativeLogitGaussianCriterionMeasurement(relative);
	}
	
	@Test
	public void testInitialization() {
		assertEquals(alternatives, m.getAlternatives());
		assertSame(relative, m.getGaussianMeasurement());
	}
	
	@Test
	public void testRange() {
		assertEquals(new Interval(0.0, 1.0), m.getRange());
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
		assertArrayEquals(new double[] { Statistics.ilogit(12.0), Statistics.ilogit(8.2), Statistics.ilogit(1.6) }, target[1], 1e-12);
		
		baseline.setStDev(1.0);
		delta.setMeanVector(new ArrayRealVector(new double[3]));
		m.sample(RandomUtil.createWithRandomSeed(), target, 1);
		assertEquals(target[1][0], target[1][1], 0.0);
		assertEquals(target[1][0], target[1][2], 0.0);
	}
}
