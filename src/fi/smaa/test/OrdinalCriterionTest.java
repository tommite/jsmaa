package fi.smaa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.OrdinalCriterion;
import fi.smaa.Rank;

public class OrdinalCriterionTest {
	
	private OrdinalCriterion crit;
	private List<Alternative> alts;
	private Map<Alternative, Rank> rankMap;
	
	@Before
	public void setUp() {
		crit = new OrdinalCriterion("Crit");
		alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
		rankMap = new HashMap<Alternative, Rank>();
		rankMap.put(alts.get(0), new Rank(2));
		rankMap.put(alts.get(1), new Rank(1));
	}
	
	@Test
	public void testSetRanks() {
		Map<Alternative, Rank> map = new HashMap<Alternative, Rank>();
		JUnitUtil.testSetter(crit, OrdinalCriterion.PROPERTY_RANKS, map, rankMap);
	}

	@Test
	public void testSample() {
		crit.setAlternatives(alts);
		crit.setRanks(rankMap);		
		double[] tgt = new double[2];
		crit.sample(tgt);
		assertTrue(tgt[0] < tgt[1]);
	}
	
	@Test
	public void testGetTypeLabel() {
		assertEquals("Ordinal", crit.getTypeLabel());
	}

	@Test
	public void testSetAlternatives() {
		List<Alternative> list = new ArrayList<Alternative>();
		list.add(new Alternative("alt"));
		JUnitUtil.testSetter(crit, Criterion.PROPERTY_ALTERNATIVES, new ArrayList<Alternative>(), list);
	}
	
	@Test
	public void testDeleteAlternativeShiftsRanksDown() {
		Alternative newAlt = new Alternative("alt3"); 
		alts.add(newAlt);
		rankMap.put(newAlt, new Rank(3));
		crit.setAlternatives(alts);
		crit.setRanks(rankMap);
		ArrayList<Alternative> nAlts = new ArrayList<Alternative>();
		nAlts.add(alts.get(0));
		nAlts.add(newAlt);
		crit.setAlternatives(nAlts);
		Map<Alternative, Rank> ranks = crit.getRanks();
		assertEquals(2, ranks.size());
		assertEquals(new Rank(1), ranks.get(alts.get(0)));
		assertEquals(new Rank(2), ranks.get(newAlt));
		
		
	}
}
