package fi.smaa.jsmaa.model;

import java.util.List;

import org.drugis.common.stat.Statistics;

import fi.smaa.common.RandomUtil;

public class RelativeLogitGaussianCriterionMeasurement implements CriterionMeasurement {
	private final RelativeGaussianCriterionMeasurement nested;

	public RelativeLogitGaussianCriterionMeasurement(RelativeGaussianCriterionMeasurement m) {
		this.nested = m;	
	}
	
	public RelativeGaussianCriterionMeasurement getGaussianMeasurement() {
		return nested;
	}

	@Override
	public List<Alternative> getAlternatives() {
		return nested.getAlternatives();
	}

	@Override
	public void addAlternative(Alternative alt) {
		nested.addAlternative(alt);
	}

	@Override
	public void deleteAlternative(Alternative alt) {
		nested.deleteAlternative(alt);
	}

	@Override
	public void reorderAlternatives(List<Alternative> alts) {
		nested.reorderAlternatives(alts);
	}

	@Override
	public void sample(RandomUtil random, double[][] target, int criterionIndex) {
		nested.sample(random, target, criterionIndex);
		for(int i = 0; i < getAlternatives().size(); ++i) {
			target[criterionIndex][i] = Statistics.ilogit(target[criterionIndex][i]);
		}
	}

	@Override
	public Interval getRange() {
		return new Interval(0.0, 1.0);
	}

	@Override
	public RelativeLogitGaussianCriterionMeasurement deepCopy(List<Alternative> alts) {
		return new RelativeLogitGaussianCriterionMeasurement(nested.deepCopy(alts));
	}
	
}
