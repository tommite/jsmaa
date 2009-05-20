package fi.smaa.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.smaa.UtilIndexPair;

public class UtilIndexPairTest {

	@Test
	public void testCompare() {
		UtilIndexPair p1 = new UtilIndexPair(0, 0.0);
		UtilIndexPair p2 = new UtilIndexPair(0, -1.0);
		UtilIndexPair p3 = new UtilIndexPair(0, 1.0);
		UtilIndexPair p4 = new UtilIndexPair(1, 0.0);
		
		assertEquals(-1, p1.compareTo(p2));
		assertEquals(1, p1.compareTo(p3));
		assertEquals(0, p1.compareTo(p4));
	}
}
