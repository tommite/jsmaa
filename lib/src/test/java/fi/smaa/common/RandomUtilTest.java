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

package fi.smaa.common;

import static org.junit.Assert.*;

import java.util.Date;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

import cern.jet.random.engine.DRand;

import fi.smaa.common.RandomUtil;

public class RandomUtilTest {
	
	@Before
	public void setUp() {
		RandomUtil.random = new DRand(new Date());
	}

	@Test
	public void testCreateSumToRand() {
		double[] dest = new double[3];
		
		RandomUtil.createSumToRand(dest, 3.0);
		
		double sum = 0.0;
		for (int i=0;i<dest.length;i++) {
			sum += dest[i];
		}
		assertEquals(3.0, sum, 0.0001);
	}
	
	@Test
	public void testCreateSumToRandSlow() {
		double[] destCount = new double[2];
		
		double[] tgt = new double[2];		
		
		int iters = 10000;
		for (int i=0;i<iters;i++) {
			RandomUtil.createSumToRand(tgt, 1.0);
			destCount[0] += tgt[0];
			destCount[1] += tgt[1];
		}
		destCount[0] /= (double) iters;
		destCount[1] /= (double) iters;
		
		assertEquals(0.5, destCount[0], 0.01);
		assertEquals(0.5, destCount[1], 0.01);
	}
	
	@Test
	public void testCreateUnif01() {
		double ran = RandomUtil.createUnif01();
		assertTrue(ran >= 0.0);
		assertTrue(ran <= 1.0);
	}
	
	@Test
	public void testCreateGaussian() {
		double gaus = RandomUtil.createGaussian(10000.0, 1.0);
		// quite unlikely that it's 0.0
		assertFalse(gaus == 0.0);
		
		gaus = RandomUtil.createGaussian(1.0, 0.0);
		assertEquals(1.0, gaus, 0.0001);
	}
	
	@Test
	public void testCreateUnifOnSphere() {
		RealVector point = RandomUtil.createUnifOnsphere(3);
		assertEquals(3, point.getDimension());
		assertEquals(1, point.getNorm(), 0.0000001);
	}
	
	@Test
	public void testCreateUnifFromSegment() {
		RealVector p1 = new ArrayRealVector(2);
		RealVector p2 = new ArrayRealVector(2);
		
		p1.setEntry(0, 1.0);
		p1.setEntry(1, 1.0);
		p2.setEntry(0, 2.0);
		p2.setEntry(1, 2.0);

		RealVector s = RandomUtil.createUnifFromSegment(p1, p2);
		assertTrue(s.getEntry(0) >= 1.0 && s.getEntry(0) <= 2.0);
		assertTrue(s.getEntry(1) >= -2.0 && s.getEntry(1) <= 2.0);
	}
}
