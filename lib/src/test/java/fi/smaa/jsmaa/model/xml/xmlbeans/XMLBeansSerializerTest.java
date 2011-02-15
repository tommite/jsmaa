package fi.smaa.jsmaa.model.xml.xmlbeans;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class XMLBeansSerializerTest {
	
	private SMAATRIModel model;
	private XMLBeansSerializer serializer;

	@Before
	public void setUp() {
		model = new SMAATRIModel("mymodel");
		model.addAlternative(new Alternative("a1"));
		model.addAlternative(new Alternative("a2"));
		model.addCategory(new Alternative("c1"));
		model.addCategory(new Alternative("c2"));
		model.addCriterion(new OutrankingCriterion("crit1", false, 
				new ExactMeasurement(1.0), new ExactMeasurement(2.0)));
		
		serializer = new XMLBeansSerializer();
	}
	
	@Test
	public void testSerializeAlternatives() throws Exception {
		SMAATRIModel newModel = serializer.deSerialize(serializer.serialize(model));
		
		assertEquals(2, newModel.getAlternatives().size());
		assertEquals("a1", newModel.getAlternatives().get(0).getName());
		assertEquals("a2", newModel.getAlternatives().get(1).getName());		
	}
	
	@Test
	public void testSerializeCategories() throws Exception {
		SMAATRIModel newModel = serializer.deSerialize(serializer.serialize(model));
		
		assertEquals(2, newModel.getCategories().size());
		assertEquals("c1", newModel.getCategories().get(0).getName());
		assertEquals("c2", newModel.getCategories().get(1).getName());		
	}
	
	@Test
	public void testSerializeCriteria() throws Exception {
		SMAATRIModel newModel = serializer.deSerialize(serializer.serialize(model));
		
		assertEquals(1, newModel.getCriteria().size());
		OutrankingCriterion c = (OutrankingCriterion) newModel.getCriteria().get(0);
		assertEquals(false, c.getAscending());
		assertEquals("crit1", c.getName());
		assertEquals(new ExactMeasurement(1.0), c.getIndifMeasurement());
		assertEquals(new ExactMeasurement(2.0), c.getPrefMeasurement());
	}
	
}
