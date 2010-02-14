package fi.smaa.jsmaa.gui.jfreechart.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.data.DomainOrder;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.jfreechart.LambdaRankAcceptabilityDataset;
import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset.Rank;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.simulator.SMAACEAResults;

public class LambdaRankAcceptabilityDatasetTest {

	private LambdaRankAcceptabilityDataset dataset;
	private List<Alternative> alts;
	private SMAACEAResults results;
	private Alternative a1;
	private Alternative a2;
	private Rank r1;
	private Rank r2;
	private ArrayList<Rank> ranks;

	@Before
	public void setUp() {
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		alts = new ArrayList<Alternative>();
		alts.add(a1);
		alts.add(a2);
		
		ScaleCriterion c1 = new ScaleCriterion("c1");
		ScaleCriterion c2 = new ScaleCriterion("c1");
		
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.add(c1);
		crit.add(c2);
		
		r1 = new Rank(0);
		r2 = new Rank(1);
		ranks = new ArrayList<Rank>();
		ranks.add(r1);
		ranks.add(r2);
		
		double[] weights = new double[]{1.0, 2.0};
		Integer[] ranksHits = new Integer[] { 1, 0 };
		Integer[] ranksHits2 = new Integer[] { 0, 1 };
		
		results = new SMAACEAResults(alts, 1);
		
		results.update(ranksHits, weights, 1.0);		
		results.update(ranksHits2, weights, 2.0);		
		
		dataset = new LambdaRankAcceptabilityDataset(results, a1);
	}
	
	@Test
	public void testGetDomainOrder() {
		assertEquals(DomainOrder.NONE, dataset.getDomainOrder());
	}
	
	@Test
	public void testGetItemCount() {
		assertEquals(results.getLambdaAcceptabilities().keySet().size(), dataset.getItemCount(0));
	}
	
	@Test
	public void testGetX() {
		assertEquals(new ArrayList<Double>(results.getLambdaAcceptabilities().keySet()).get(0), dataset.getX(0, 0));
		assertEquals(new ArrayList<Double>(results.getLambdaAcceptabilities().keySet()).get(0), dataset.getX(1, 0));
	}	
	
	@Test
	public void testGetY() {
		Map<Alternative, List<Double>> l1accs = results.getLambdaAcceptabilities().get(1.0).getRankAcceptabilities();
		assertEquals(l1accs.get(a1).get(0), dataset.getY(0, 0));
		assertEquals(l1accs.get(a1).get(1), dataset.getY(1, 0));
		
		Map<Alternative, List<Double>> l2accs = results.getLambdaAcceptabilities().get(2.0).getRankAcceptabilities();
		assertEquals(l2accs.get(a1).get(0), dataset.getY(0, 1));
		assertEquals(l2accs.get(a1).get(1), dataset.getY(1, 1));
	}
	
	@Test
	public void testGetSeriesCount() {
		assertEquals(results.getAlternatives().size(), dataset.getSeriesCount());
	}
	
	@Test
	public void testGetSeriesKey() {
		for (int i=0;i<results.getAlternatives().size();i++) {
			assertEquals("Rank "+(i+1), dataset.getSeriesKey(i));
		}
	}
	
	@Test
	public void testIndexOf() {
		assertEquals(0, dataset.indexOf("Rank 1"));
		assertEquals(1, dataset.indexOf("Rank 2"));
		assertEquals(-1, dataset.indexOf("Rank 3"));
		assertEquals(-1, dataset.indexOf("Rank 0"));
		assertEquals(-1, dataset.indexOf("Rank 0x"));
		assertEquals(-1, dataset.indexOf("Rank0x"));
		assertEquals(-1, dataset.indexOf("Ran"));
	}
}
