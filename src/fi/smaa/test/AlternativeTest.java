package fi.smaa.test;

import static org.junit.Assert.assertEquals;
import nl.rug.escher.common.JUnitUtil;

import org.junit.Test;

import fi.smaa.Alternative;

public class AlternativeTest {
	
	@Test
	public void testSetName() {
		JUnitUtil.testSetter(new Alternative(), Alternative.PROPERTY_NAME, null, "hello");
	}
	
	@Test
	public void testConstructor() {
		Alternative a = new Alternative("test");
		assertEquals("test", a.getName());
	}
	
	@Test
	public void testToString() {
		Alternative a = new Alternative("alt");
		assertEquals("alt", a.toString());
	}

}
