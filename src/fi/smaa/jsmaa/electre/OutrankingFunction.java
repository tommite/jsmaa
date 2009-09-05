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

package fi.smaa.jsmaa.electre;

import fi.smaa.jsmaa.model.OutrankingCriterion;

public class OutrankingFunction {

	public static double concordance(OutrankingCriterion crit, double outranker, double outranked) {
		double diff = outranked - outranker;
		if (!crit.getAscending()) {
			diff = 0.0 - diff;
		}
		if (diff >= crit.getPreferenceThreshold()) {
			return 0.0;
		} else if (diff <= crit.getIndifferenceThreshold()) {
			return 1.0;
		} else {	
			return (diff - crit.getIndifferenceThreshold()) / 
				(crit.getPreferenceThreshold() - crit.getIndifferenceThreshold()); 
		}
	}		
}
