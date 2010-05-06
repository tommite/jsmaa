package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRIModelXMLFormatTest {

	@Test
	public void testMarshallSMAATRIModel() throws XMLStreamException {
		Alternative cat1 = new Alternative("cat1");
		Alternative cat2 = new Alternative("cat2");
		OutrankingCriterion oc = new OutrankingCriterion("crit", false, new ExactMeasurement(1.0), new ExactMeasurement(2.0));
		
		SMAATRIModel mod = new SMAATRIModel("model");
		mod.setRule(false);
		mod.addCategory(cat1);
		mod.addCategory(cat2);
		mod.addCriterion(oc);
		mod.getLambda().setEnd(0.9);
		mod.getLambda().setStart(0.7);
		
		mod.setCategoryUpperBound(oc, cat1, new Interval(0.0, 1.0));
		
		SMAATRIModel nmod = XMLHelper.fromXml(XMLHelper.toXml(mod, SMAATRIModel.class));
		
		assertEquals(2, nmod.getCategories().size());
		assertEquals(cat1.getName(), nmod.getCategories().get(0).getName());
		assertEquals(cat2.getName(), nmod.getCategories().get(1).getName());
		
		assertEquals(false, nmod.getRule());
		assertEquals(new Interval(0.0, 1.0), nmod.getCategoryUpperBound((OutrankingCriterion) nmod.getCriteria().get(0), nmod.getCategories().get(0)));
		assertEquals(new Interval(0.7, 0.9), nmod.getLambda());
	}
}
