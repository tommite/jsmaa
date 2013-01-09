/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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

import java.util.ArrayList;
import java.util.List;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.IndependentMeasurements;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.RankSampler;

public class Sampler {
	private IndependentMeasurements m;
	private final RandomUtil random;
	
	public Sampler(IndependentMeasurements impactMatrix, RandomUtil random) {
		this.m = impactMatrix;
		this.random = random;
	}

	public void sample(Criterion crit, double[] target) {
		if (target.length != m.getAlternatives().size()) {
			throw new IllegalArgumentException();
		}
		
		if (crit instanceof CardinalCriterion) {
			sample((CardinalCriterion) crit, target);
		} else if (crit instanceof OrdinalCriterion) {
			sample((OrdinalCriterion) crit, target);
		} else {
			throw new IllegalArgumentException("Unknown criterion type");
		}		
	}
	
	private void sample (OrdinalCriterion o, double[] target) {
		List<Integer> ranks = new ArrayList<Integer>();
		for (int i=0;i<target.length;i++) {
			Integer r = ((Rank) m.getMeasurement(o, m.getAlternatives().get(i))).getRank();
			ranks.add(r);
		}
		
		RankSampler rs = new RankSampler(ranks);
		double[] w = rs.sampleWeights(random);
				
		for (int i=0;i<w.length;i++) {
			target[i] = w[i];
		}
	}

	private void sample(CardinalCriterion c, double[] target) {

		for (int i=0;i<target.length;i++) {
			CardinalMeasurement meas = (CardinalMeasurement) m.getMeasurement(c, m.getAlternatives().get(i));
			target[i] = meas.sample(random);
		}
	}

}
