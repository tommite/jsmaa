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

package fi.smaa.jsmaa.simulator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.JUnitUtil;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

public class SMAATRIResultsTest {

	private SMAATRIResults results;
	
	private Integer[] firstSecondCat;
	private Integer[] secondThirdCat;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private List<Alternative> alts;
	private List<Alternative> cats;
	
	@Before
	public void setUp()  {
		firstSecondCat = new Integer[]{0, 1};
		secondThirdCat = new Integer[]{1, 2};
		alts = new ArrayList<Alternative>();
		cats = new ArrayList<Alternative>();
		cats.add(new Alternative("cat1"));
		cats.add(new Alternative("cat2"));
		cats.add(new Alternative("cat3"));
		alts.add(alt1);
		alts.add(alt2);
		results = new SMAATRIResults(alts, cats, 10);
	}
	
	private void do10Hits() {		
		for (int i=0;i<5;i++) {
			results.update(firstSecondCat);
		}
		for (int i=0;i<5;i++) {
			results.update(secondThirdCat);
		}
	}
	
	@Test
	public void testCorrectCategoryAcceptabilities() {
		do10Hits();

		List<Double> ra1 = results.getCategoryAcceptabilities().get(alt1);
		List<Double> ra2 = results.getCategoryAcceptabilities().get(alt2);

		assertEquals(0.5, ra1.get(0), 0.00001);
		assertEquals(0.5, ra1.get(1), 0.00001);
		assertEquals(0.0, ra1.get(2), 0.00001);

		assertEquals(0.0, ra2.get(0), 0.00001);
		assertEquals(0.5, ra2.get(1), 0.00001);
		assertEquals(0.5, ra2.get(2), 0.00001);

	}
	
	@Test
	public void testReset() {
		do10Hits();
		
		results.reset();

		Map<Alternative, List<Double>> raccs = results.getCategoryAcceptabilities();
		for (List<Double> list : raccs.values()) {
			for (Double d : list) {
				assertTrue(d.equals(Double.NaN));
			}
		}
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
