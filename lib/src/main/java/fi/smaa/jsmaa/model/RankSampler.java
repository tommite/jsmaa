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
package fi.smaa.jsmaa.model;

import java.util.List;

import fi.smaa.common.RandomUtil;

public class RankSampler {

	transient private double[] tmparr;
	transient private double[] samplearr;
	private List<Integer> ranks;
	private RandomUtil random = new RandomUtil();

	public RankSampler(List<Integer> ranks) {
		this.ranks = ranks;
	}

	public double[] sampleWeights() {
		initArrays();
		random.createSumToOneSorted(tmparr);
		
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
