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
package fi.smaa.jsmaa;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Category;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class DefaultModels {

	public static SMAAModel getSMAA2Model() {
		SMAAModel model = new SMAAModel("SMAA-2 Model");
		addDefaultCriteria(model);
		addDefaultAlternatives(model);
		return model;
	}

	private static void addDefaultCriteria(SMAAModel model) {
		model.addCriterion(new ScaleCriterion("Criterion 1"));
		model.addCriterion(new ScaleCriterion("Criterion 2"));
	}
	
	private static void addDefaultOutrankingCriteria(SMAAModel model) {
		model.addCriterion(new OutrankingCriterion("Criterion 1", true, 
				new Interval(0.0, 0.0), new Interval(1.0, 1.0)));
		model.addCriterion(new OutrankingCriterion("Criterion 2", true, 
				new Interval(0.0, 0.0), new Interval(1.0, 1.0)));
	}	

	private static void addDefaultAlternatives(SMAAModel model) {
		Alternative a1 = new Alternative("Alternative 1");
		model.addAlternative(a1);
		Alternative a2 = new Alternative("Alternative 2");
		model.addAlternative(a2);			
		Alternative a3 = new Alternative("Alternative 3");
		model.addAlternative(a3);
		
		ImpactMatrix meas = (ImpactMatrix) model.getMeasurements();
		meas.setMeasurement(model.getCriteria().get(0), a1, new ExactMeasurement(10.0));
		meas.setMeasurement(model.getCriteria().get(1), a1, new ExactMeasurement(5.0));
		meas.setMeasurement(model.getCriteria().get(0), a2, new ExactMeasurement(7.0));
		meas.setMeasurement(model.getCriteria().get(1), a2, new Interval(3.0, 6.0));
		meas.setMeasurement(model.getCriteria().get(0), a3, new GaussianMeasurement(2.0, 0.3));
		meas.setMeasurement(model.getCriteria().get(1), a3, new Interval(3.0, 6.0));				
	}

	public static SMAATRIModel getSMAATRIModel() {
		SMAATRIModel model = new SMAATRIModel("SMAA-TRI Model");
		addDefaultOutrankingCriteria(model);
		addDefaultAlternatives(model);				
		model.addCategory(new Category("Category 1"));
		model.addCategory(new Category("Category 2"));
		
		return model;
	}

}
