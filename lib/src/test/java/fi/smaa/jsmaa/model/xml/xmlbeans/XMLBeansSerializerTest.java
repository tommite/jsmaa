package fi.smaa.jsmaa.model.xml.xmlbeans;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class XMLBeansSerializerTest {
	
	private SMAATRIModel model;
	private XMLBeansSerializer serializer;
	private ExactMeasurement a1crit1meas;
	private Interval a2crit1meas;
	private GaussianMeasurement cat1crit1meas;
	private SMAATRIModel newModel;

	@Before
	public void setUp() throws Exception {
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a2");
		Alternative cat1 = new Alternative("c1");
		Alternative cat2 = new Alternative("c2");				
		OutrankingCriterion crit1 = new OutrankingCriterion("crit1", false, 
				new ExactMeasurement(1.0), new ExactMeasurement(2.0));
		
		model = new SMAATRIModel("mymodel");
		model.addAlternative(a1);
		model.addAlternative(a2);
		model.addCategory(cat1);
		model.addCategory(cat2);
		model.addCriterion(crit1);

		a1crit1meas = new ExactMeasurement(20.0);
		a2crit1meas = new Interval(-10.0, 35.5);
		
		model.setMeasurement(crit1, a1, a1crit1meas);
		model.setMeasurement(crit1, a2, a2crit1meas);

		cat1crit1meas = new GaussianMeasurement(0.1, 0.2);
		model.setCategoryUpperBound(crit1, cat1, cat1crit1meas);
		
		serializer = new XMLBeansSerializer();
		newModel = serializer.deSerialize(serializer.serialize(model));
	}
	
	@Test
	public void testSerializeAlternatives() throws Exception {		
		assertEquals(2, newModel.getAlternatives().size());
		assertEquals("a1", newModel.getAlternatives().get(0).getName());
		assertEquals("a2", newModel.getAlternatives().get(1).getName());		
	}
	
	@Test
	public void testSerializeCategories() throws Exception {
		assertEquals(2, newModel.getCategories().size());
		assertEquals("c1", newModel.getCategories().get(0).getName());
		assertEquals("c2", newModel.getCategories().get(1).getName());		
	}
	
	@Test
	public void testSerializeCriteria() throws Exception {
		assertEquals(1, newModel.getCriteria().size());
		OutrankingCriterion c = (OutrankingCriterion) newModel.getCriteria().get(0);
		assertEquals(false, c.getAscending());
		assertEquals("crit1", c.getName());
		assertEquals(new ExactMeasurement(1.0), c.getIndifMeasurement());
		assertEquals(new ExactMeasurement(2.0), c.getPrefMeasurement());
	}
	
	@Test
	public void testSerializeAlternativePerformances() throws Exception{
		Criterion c1 = newModel.getCriteria().get(0);
		
		assertEquals(a1crit1meas, newModel.getMeasurement(c1, newModel.getAlternatives().get(0)));
		assertEquals(a2crit1meas, newModel.getMeasurement(c1, newModel.getAlternatives().get(1)));
	}
	
	@Test
	public void testSerializeProfilePerformances() throws Exception{
		System.out.println(serializer.serialize(model));		
		OutrankingCriterion c1 = (OutrankingCriterion) newModel.getCriteria().get(0);
		assertEquals(cat1crit1meas, newModel.getCategoryUpperBound(c1, newModel.getCategories().get(0)));
	}	
	
}
