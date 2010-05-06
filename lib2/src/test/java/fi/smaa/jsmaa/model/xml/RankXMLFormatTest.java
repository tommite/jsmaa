package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Rank;

public class RankXMLFormatTest {

	@Test
	public void testMarshalRank() throws XMLStreamException {
		Rank r = new Rank(3);
		Rank nr = (Rank) XMLHelper.fromXml(XMLHelper.toXml(r, Rank.class));
		assertEquals(r, nr);
	}
}
