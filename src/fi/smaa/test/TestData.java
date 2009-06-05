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

package fi.smaa.test;

import java.util.HashMap;
import java.util.Map;

import fi.smaa.Alternative;
import fi.smaa.AlternativeExistsException;
import fi.smaa.AbstractCriterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.GaussianMeasurement;
import fi.smaa.OrdinalCriterion;
import fi.smaa.Rank;
import fi.smaa.SMAAModel;
import fi.smaa.UniformCriterion;
import fi.smaa.common.Interval;

public class TestData {
	
	public SMAAModel model;
	public Alternative alt1;
	public Alternative alt2;
	public OrdinalCriterion crit1;	
	public UniformCriterion crit2;	
	public AbstractCriterion<GaussianMeasurement> crit3;	
	public Map<Alternative, Rank> ranks;
	public Map<Alternative, Interval> intervals;
	public Map<Alternative, GaussianMeasurement> gaussianMeasurements;

	public TestData() {
		model = new SMAAModel("model");
		alt1 = new Alternative("alt1");		
		alt2 = new Alternative("alt2");
		crit1 = new OrdinalCriterion("ordinal");
		crit2 = new UniformCriterion("uniform");
		crit3 = new GaussianCriterion("gaussian");
		
		ranks = new HashMap<Alternative, Rank>();
		ranks.put(alt1, new Rank(1));
		ranks.put(alt2, new Rank(2));
		
		intervals = new HashMap<Alternative, Interval>();
		intervals.put(alt1, new Interval(0.0, 5.0));
		intervals.put(alt2, new Interval(-1.0, 2.0));
		
		gaussianMeasurements = new HashMap<Alternative, GaussianMeasurement>();
		gaussianMeasurements.put(alt1, new GaussianMeasurement(0.5, 3.0));
		gaussianMeasurements.put(alt2, new GaussianMeasurement(1.5, 2.0));
		
		
		try {
			model.addAlternative(alt1);
			model.addAlternative(alt2);			
		} catch (AlternativeExistsException e) {
			e.printStackTrace();
		}
		model.addCriterion(crit1);
		model.addCriterion(crit2);
		model.addCriterion(crit3);
	}
}
