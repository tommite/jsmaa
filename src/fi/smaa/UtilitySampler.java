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

package fi.smaa;

import fi.smaa.common.RandomUtil;

public class UtilitySampler {
	private int numAlts;
	double[] tmparr;
	
	public UtilitySampler(int numAlts) {
		this.numAlts = numAlts;
		tmparr = new double[numAlts];			
	}
	
	@SuppressWarnings("unchecked")
	public void sample(Criterion crit, double[] target) {
		if (crit instanceof GaussianCriterion) {
			sample((GaussianCriterion) crit, target);
			if (crit instanceof LogNormalCriterion) {
				exponentiateVector(target);
			}
		} else if (crit instanceof OrdinalCriterion) {
			sample((OrdinalCriterion) crit, target);
		} else if (crit instanceof UniformCriterion) {
			sample((UniformCriterion) crit, target);
		} else {
			throw new IllegalArgumentException("Unknown criterion type");
		}		
	}
	
	private void exponentiateVector(double[] target) {
		for (int i=0;i<target.length;i++) {
			target[i] = Math.exp(target[i]);
		}
	}

	public void sample(GaussianCriterion crit, double[] target) {
		assert(target.length == numAlts);
		
		for (int i = 0 ; i < crit.getAlternatives().size();i++) {
			Alternative a = crit.getAlternatives().get(i);			
			target[i] = RandomUtil.createGaussian(crit.getMeasurements().get(a).getMean(),
					crit.getMeasurements().get(a).getStDev());
		}
	}

	public void sample(OrdinalCriterion c, double[] target) {
		assert(numAlts == target.length);		
		
		RandomUtil.createSumToOneSorted(tmparr);
		
		for (int i=0;i<c.getAlternatives().size();i++) {
			Rank rank = c.getMeasurements().get(c.getAlternatives().get(i));
			target[i] = tmparr[tmparr.length - rank.getRank()];
		}
	}

	public void sample(UniformCriterion c, double[] target) {
		assert(target.length == numAlts);

		for (int i=0;i<c.getAlternatives().size();i++) {
			Alternative a = c.getAlternatives().get(i);
			double intMin = c.getMeasurements().get(a).getStart();
			double intMax = c.getMeasurements().get(a).getEnd();
			double diff = intMax - intMin;
			target[i] = intMin + (RandomUtil.createUnif01() * diff);
		}
	}

}
