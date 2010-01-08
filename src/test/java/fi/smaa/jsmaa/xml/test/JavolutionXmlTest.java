package fi.smaa.jsmaa.xml.test;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.xml.JSMAABindingv1;

public class JavolutionXmlTest {
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testMarshalAlternative() throws XMLStreamException {
		Alternative a = new Alternative("a");
		
		String xml = JSMAABindingv1.toXml(a);
		
		Alternative na = (Alternative) JSMAABindingv1.fromXml(xml);
		
		assertEquals("a", na.getName());
	}

}
