package fi.smaa.jsmaa.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RankSamplerTest {

	private RankSampler rs;
	private Integer r2;
	private Integer r1;

	@Before
	public void setUp() {
		List<Integer> list = new ArrayList<Integer>();
		r1 = new Integer(1);
		r2 = new Integer(2);
		list.add(r1);
		list.add(r2);		
		rs = new RankSampler(list);
	}
	
	@Test
	public void testSampleWeights() {
		double[] w = rs.sampleWeights();
		
		assertEquals(2, w.length);
		assertTrue(w[0] > w[1]);

		r1 = new Integer(2);
		r2 = new Integer(1);
		List<Integer> list = new ArrayList<Integer>();
		list.add(r1);
		list.add(r2);
		rs = new RankSampler(list);
		
		w = rs.sampleWeights();
		assertTrue(w[0] < w[1]);
	}
}
