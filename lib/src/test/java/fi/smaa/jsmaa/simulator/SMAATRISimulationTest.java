/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.smaa.jsmaa.simulator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drugis.common.JUnitUtil;
import org.drugis.common.threading.ThreadHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Category;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.IndependentMeasurements;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRISimulationTest {

	private SMAATRIModel model;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private OutrankingCriterion c1 = new OutrankingCriterion("c1", true, 
			new Interval(0.0, 0.0), new Interval(1.0, 1.0));
	private OutrankingCriterion c2 = new OutrankingCriterion("c2", true,
			new Interval(0.0, 0.0), new Interval(1.0, 1.0));
	private Category cat1 = new Category("cat1");
	private Category cat2 = new Category("cat2");
	private Set<Alternative> alts;
	private Set<Criterion> crit;		
	private List<Category> cats;
	
	@Before
	public void setUp() {
		alts = new HashSet<Alternative>();
		crit = new HashSet<Criterion>();
		cats = new ArrayList<Category>();
		alts.add(alt1);
		alts.add(alt2);
		crit.add(c1);
		crit.add(c2);
		cats.add(cat1);
		cats.add(cat2);
		model = new SMAATRIModel("model");
		model.addAlternative(alt1);
		model.addAlternative(alt2);
		model.addCriterion(c1);
		model.addCriterion(c2);
		model.addCategory(cat1);
		model.addCategory(cat2);
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c1, alt1, new ExactMeasurement(2.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c2, alt1, new ExactMeasurement(2.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c1, alt2, new ExactMeasurement(0.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c2, alt2, new ExactMeasurement(0.0));
		model.setCategoryUpperBound(c1, cat1, new ExactMeasurement(1.0));
		model.setCategoryUpperBound(c2, cat1, new ExactMeasurement(1.0));
		model.setRule(true);
	}
	
	@Test
	public void testOneCategory() throws InterruptedException {
		model.deleteCategory(cat1);
		
		SMAATRIResults res = runModel(model);
		
		Map<Alternative, List<Double>> accs = res.getCategoryAcceptabilities();		
		assertEquals(1.0, accs.get(alt1).get(0), 0.00001);
		assertEquals(1.0, accs.get(alt2).get(0), 0.00001);
	}

	private SMAATRIResults runModel(SMAATRIModel model) throws InterruptedException {
		SMAATRISimulation simulation = new SMAATRISimulation(model, RandomUtil.createWithFixedSeed(), 10000);
		ThreadHandler hand = ThreadHandler.getInstance();
		hand.scheduleTask(simulation.getTask());
		do {
			Thread.sleep(1);
		} while (!simulation.getTask().isFinished());

		return (SMAATRIResults) simulation.getResults();
	}
	
	@Test
	public void testCorrectResults() throws InterruptedException {		
		SMAATRIResults res = runModel(model);
		Map<Alternative, List<Double>> accs = res.getCategoryAcceptabilities();
		
		assertEquals(0.0, accs.get(alt1).get(0), 0.00001);
		assertEquals(1.0, accs.get(alt1).get(1), 0.00001);
		
		assertEquals(1.0, accs.get(alt2).get(0), 0.00001);
		assertEquals(0.0, accs.get(alt2).get(1), 0.00001);	
	}
	
	@Test
	public void testCorrectResultsPessimistic() throws InterruptedException {
		model.setRule(false);

		SMAATRIResults res = runModel(model);
		Map<Alternative, List<Double>> accs = res.getCategoryAcceptabilities();
		
		assertEquals(1.0, accs.get(alt1).get(0), 0.00001);
		assertEquals(0.0, accs.get(alt1).get(1), 0.00001);
		
		assertEquals(1.0, accs.get(alt2).get(0), 0.00001);
		assertEquals(0.0, accs.get(alt2).get(1), 0.00001);	
	}
	
	// ignore because mock doesnt match the event
	@Test
	@Ignore
	public void testInvalidUpperBoundsFire() throws InterruptedException {
		Category cat3 = new Category("cat3");
		model.addCategory(cat3);
		model.setCategoryUpperBound(c1, cat1, new ExactMeasurement(1.0));
		model.setCategoryUpperBound(c1, cat2, new ExactMeasurement(0.0));	
		
		SMAAResultsListener mock = createMock(SMAAResultsListener.class);
		
		SMAATRISimulation simulation = new SMAATRISimulation(model, RandomUtil.createWithFixedSeed(), 10000);
		ThreadHandler hand = ThreadHandler.getInstance();
		
		mock.resultsChanged((ResultsEvent) JUnitUtil.eqEventObject(new ResultsEvent(simulation.getResults(),
				new IterationException(""))));
		
		replay(mock);
		simulation.getResults().addResultsListener(mock);		
		hand.scheduleTask(simulation.getTask());
		do {
			Thread.sleep(1);
		} while (hand.getQueuedTasks() > 0);
		verify(mock);
	}

	// ignore because mock doesnt match the event
	@Test
	@Ignore
	public void testInvalidThresholdsFire() throws InterruptedException {
		c1.setIndifMeasurement(new ExactMeasurement(3.0));
		c1.setPrefMeasurement(new ExactMeasurement(2.0));
		
		SMAATRISimulation simulation = new SMAATRISimulation(model, RandomUtil.createWithFixedSeed(), 10000);
		ThreadHandler hand = ThreadHandler.getInstance();
		hand.scheduleTask(simulation.getTask());
		do {
			Thread.sleep(1);
		} while (hand.getQueuedTasks() > 0);
		
		SMAAResultsListener mock = createMock(SMAAResultsListener.class);
		mock.resultsChanged((ResultsEvent) JUnitUtil.eqEventObject(new ResultsEvent(simulation.getResults(),
				new IterationException(""))));
		
		replay(mock);
		simulation.getResults().addResultsListener(mock);		
		hand.scheduleTask(simulation.getTask());
		do {
			Thread.sleep(1);
		} while (hand.getQueuedTasks() > 0);		
		verify(mock);
	}
	
	@Test
	public void testOneCriterionZeroBoundUpperBound() throws InterruptedException {
		model.setCategoryUpperBound(c1, cat1, new ExactMeasurement(0.0));
		model.setCategoryUpperBound(c2, cat1, new ExactMeasurement(0.0));

		SMAATRIResults results = runModel(model);
		Thread.sleep(10);
		assertEquals(new Double(0.0), results.getCategoryAcceptabilities().get(alt1).get(0));
	}
}
