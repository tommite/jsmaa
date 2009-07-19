/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

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

package fi.smaa.jsmaa.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.SMAASimulator;
import fi.smaa.jsmaa.SMAATRIResults;
import fi.smaa.jsmaa.SMAATRISimulationThread;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRISimulatorTest {

	private SMAATRIModel model;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private OutrankingCriterion c1 = new OutrankingCriterion("c1", true, 0.0, 1.0);
	private OutrankingCriterion c2 = new OutrankingCriterion("c2", true, 0.0, 1.0);
	private Alternative cat1 = new Alternative("cat1");
	private Alternative cat2 = new Alternative("cat2");
	private Set<Alternative> alts;
	private Set<Criterion> crit;		
	private List<Alternative> cats;
	
	@Before
	public void setUp() {
		alts = new HashSet<Alternative>();
		crit = new HashSet<Criterion>();
		cats = new ArrayList<Alternative>();
		alts.add(alt1);
		alts.add(alt2);
		crit.add(c1);
		crit.add(c2);
		cats.add(cat1);
		cats.add(cat2);
		model = new SMAATRIModel("model");
		model.setAlternatives(alts);
		model.setCriteria(crit);
		model.setCategories(cats);
	}
	
	@Test
	public void testConstructor() {
		SMAASimulator simulator = new SMAASimulator(model, new SMAATRISimulationThread(model, 100));		
		assertEquals(100, simulator.getTotalIterations().intValue());
	}
	
	@Test
	public void testCorrectResults() throws InterruptedException {
		SMAASimulator simulator = new SMAASimulator(model, new SMAATRISimulationThread(model, 10000));		
		simulator.restart();
		while (simulator.isRunning()) {
			Thread.sleep(10);
		}
		
		SMAATRIResults res = (SMAATRIResults) simulator.getResults();
		fail();
	}
	
}
