package fi.smaa.jsmaa.gui.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.LeftTreeTransferHandler;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAA2;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class LeftTreeTransferHandlerTest {
	
	private SMAATRIModel smaaModel;
	private OutrankingCriterion crit1;
	private OutrankingCriterion crit2;
	private Alternative a1;
	private Alternative a2;
	private Alternative cat1;
	private Alternative cat2;
	private LeftTreeTransferHandler handler;

	@Before
	public void setUp() {
		smaaModel = new SMAATRIModel("name");
		crit1 = new OutrankingCriterion("o1", true, new ExactMeasurement(0.0), new ExactMeasurement(1.0));
		crit2 = new OutrankingCriterion("o2", true, new ExactMeasurement(0.0), new ExactMeasurement(1.0));
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		cat1 = new Alternative("c1");
		cat2 = new Alternative("c2");
		
		smaaModel.addAlternative(a1);
		smaaModel.addAlternative(a2);
		
		smaaModel.addCategory(cat1);
		smaaModel.addCategory(cat2);
		
		smaaModel.addCriterion(crit1);
		smaaModel.addCriterion(crit2);
		
		LeftTreeModelSMAA2 treeModel = new LeftTreeModelSMAA2(smaaModel);
		handler = new LeftTreeTransferHandler(treeModel, smaaModel);
	}

	@Test
	public void testMoveCriterion() {
		handler.moveCriterion(crit2, 0);
		assertEquals(crit2, smaaModel.getCriteria().get(0));
		assertEquals(crit1, smaaModel.getCriteria().get(1));
	}	
	
	@Test
	public void testMoveAlternative() {
		handler.moveAlternative(a2, 0);
		assertEquals(a2, smaaModel.getAlternatives().get(0));
		assertEquals(a1, smaaModel.getAlternatives().get(1));
	}
	
	@Test
	public void testMoveCategory() {
		handler.moveCategory(cat2, 0);
		assertEquals(cat2, smaaModel.getCategories().get(0));
		assertEquals(cat1, smaaModel.getCategories().get(1));
	}
}
