/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;

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
		RandomUtil random = RandomUtil.createWithFixedSeed();
		double[] w = rs.sampleWeights(random);
		
		assertEquals(2, w.length);
		assertTrue(w[0] > w[1]);

		r1 = new Integer(2);
		r2 = new Integer(1);
		List<Integer> list = new ArrayList<Integer>();
		list.add(r1);
		list.add(r2);
		rs = new RankSampler(list);
		
		w = rs.sampleWeights(random);
		assertTrue(w[0] < w[1]);
	}
}
