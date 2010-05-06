package fi.smaa.jsmaa.model;

import java.util.List;

import fi.smaa.common.RandomUtil;

public class RankSampler {

	transient private double[] tmparr;
	transient private double[] samplearr;
	private List<Integer> ranks;
	
	public RankSampler(List<Integer> ranks) {
		this.ranks = ranks;
	}

	public double[] sampleWeights() {
		initArrays();
		RandomUtil.createSumToOneSorted(tmparr);
		
		for (int i=0;i<samplearr.length;i++) {
			Integer r = ranks.get(i);
			samplearr[i] = tmparr[tmparr.length - r];
		}
		return samplearr;
	}
	
	private void initArrays() {
		if (tmparr == null) {
			tmparr = new double[ranks.size()];
		}
		if (samplearr == null) {
			samplearr = new double[ranks.size()];			
		}
	}
}
