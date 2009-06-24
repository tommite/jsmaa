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

package fi.smaa.jsmaa.maut;

import java.util.List;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;

public class UtilitySampler {
	private List<Alternative> alts;
	private SMAAModel m;
	
	public UtilitySampler(SMAAModel m, List<Alternative> alts) {
		assert(alts.size() > 0);
		this.alts = alts;
		this.m = m;
	}
	
	public void sample(Criterion crit, double[] target) {
		if (crit instanceof CardinalCriterion) {
			sample((CardinalCriterion) crit, target);
		} else {
			throw new IllegalArgumentException("Unknown criterion type");
		}		
	}


	public void sample(CardinalCriterion c, double[] target) {
		assert(target.length == alts.size());

		for (int i=0;i<alts.size();i++) {
			
			target[i] = m.getMeasurement(c, alts.get(i)).sample();
		}
	}

}
