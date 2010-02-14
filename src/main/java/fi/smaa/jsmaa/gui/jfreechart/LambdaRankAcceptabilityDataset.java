package fi.smaa.jsmaa.gui.jfreechart;

import java.util.ArrayList;

import org.jfree.data.DomainOrder;
import org.jfree.data.xy.XYDataset;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAACEAResults;

public class LambdaRankAcceptabilityDataset extends SMAADataSet<SMAACEAResults> implements XYDataset {

	private Alternative alt;

	/**
	 * @param results
	 * @param rank to plot
	 */
	public LambdaRankAcceptabilityDataset(SMAACEAResults results, Alternative alt) {
		super(results);
		this.alt = alt;
	}

	@Override
	public DomainOrder getDomainOrder() {
		return DomainOrder.NONE;
	}

	@Override
	public int getItemCount(int seriesIndex) {
		return results.getLambdaAcceptabilities().keySet().size();
	}

	@Override
	public Number getX(int series, int item) {
		return new Double(getXValue(series, item));
	}

	@Override
	public double getXValue(int series, int item) { // series are ranks, item = lambda
		return new ArrayList<Double>(results.getLambdaAcceptabilities().keySet()).get(item); 
	}

	@Override
	public Number getY(int series, int item) {
		return new Double(getYValue(series, item));
	}

	@Override
	public double getYValue(int series, int item) { // series are ranks, item = lambda
		Double lambda = new ArrayList<Double>(results.getLambdaAcceptabilities().keySet()).get(item);
		if (alt == null) {
			return 0.0;
		}
		return results.getLambdaAcceptabilities().get(lambda).getRankAcceptabilities().get(alt).get(series);
	}

	@Override
	public int getSeriesCount() {
		return results.getAlternatives().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable getSeriesKey(int index) {
		return "Rank " + (index+1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(Comparable obj) {
		if (obj instanceof String) {
			String sobj = (String) obj;
			if (sobj.length() < 6) {
				return -1;
			}
			if (sobj.substring(0, 4).equals("Rank")) {
				try {
					int index = Integer.parseInt(sobj.substring(5)) - 1;
					if (index < 0 || index >= results.getAlternatives().size()) {
						return -1;
					}
					return index;					
				} catch (Exception e) {
					return -1;
				}
			}
		}
		return -1;
	}
}
