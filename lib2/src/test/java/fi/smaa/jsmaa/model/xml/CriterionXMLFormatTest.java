package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class CriterionXMLFormatTest {

	@Test
	public void testMarshallOutrankingCriterion() throws XMLStreamException {
		OutrankingCriterion crit = new OutrankingCriterion("crit", false, new ExactMeasurement(1.0), new Interval(1.0, 2.0));
		OutrankingCriterion nCrit = (OutrankingCriterion) XMLHelper.fromXml(XMLHelper.toXml(crit, OutrankingCriterion.class));
		
		assertEquals(crit.getName(), nCrit.getName());
		assertEquals(crit.getAscending(), nCrit.getAscending());
		assertEquals(crit.getIndifMeasurement(), nCrit.getIndifMeasurement());
		assertEquals(crit.getPrefMeasurement(), nCrit.getPrefMeasurement());
	}
	
	@Test
	public void testMarshallOrdinalCriterion() throws XMLStreamException {
		OrdinalCriterion crit = new OrdinalCriterion("crit");
		OrdinalCriterion nCrit = (OrdinalCriterion) XMLHelper.fromXml(XMLHelper.toXml(crit, OrdinalCriterion.class));
		assertEquals(crit.getName(), nCrit.getName());
	}
	
	@Test
	public void testMarshallCardinalCriterion() throws XMLStreamException {
		ScaleCriterion crit = new ScaleCriterion("crit", false);
		ScaleCriterion nCrit = (ScaleCriterion) XMLHelper.fromXml(XMLHelper.toXml(crit, ScaleCriterion.class));
		
		assertEquals(crit.getName(), nCrit.getName());
		assertEquals(crit.getAscending(), nCrit.getAscending());
	}
}
