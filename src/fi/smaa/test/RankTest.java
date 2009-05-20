package fi.smaa.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.rug.escher.common.JUnitUtil;

import org.junit.Test;

import fi.smaa.Rank;

public class RankTest {

	@Test
	public void testSetRank() {
		JUnitUtil.testSetter(new Rank(1), Rank.PROPERTY_RANK, 1, new Integer(2));
	}
	
	@Test
	public void testEquals() {
		assertTrue(new Rank(2).equals(new Rank(2)));
		assertFalse(new Rank(1).equals(new Rank(2)));
		assertFalse(new Rank(1).equals(null));
		assertFalse(new Rank(1).equals("blaa"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testErrorConstructor() {
		new Rank(0);
	}
}
