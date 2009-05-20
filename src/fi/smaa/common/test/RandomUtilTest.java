package fi.smaa.common.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.smaa.common.RandomUtil;

public class RandomUtilTest {

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
}
