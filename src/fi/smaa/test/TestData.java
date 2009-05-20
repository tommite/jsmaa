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

import fi.smaa.Alternative;
import fi.smaa.GaussianCriterion;
import fi.smaa.OrdinalCriterion;
import fi.smaa.SMAAModel;
import fi.smaa.UniformCriterion;

public class TestData {
	
	public SMAAModel model;
	public Alternative alt1;
	public Alternative alt2;
	public OrdinalCriterion crit1;	
	public UniformCriterion crit2;	
	public GaussianCriterion crit3;	

	public TestData() {
		model = new SMAAModel("model");
		alt1 = new Alternative("alt1");		
		alt2 = new Alternative("alt2");
		crit1 = new OrdinalCriterion("ordinal");
		crit2 = new UniformCriterion("uniform");
		crit3 = new GaussianCriterion("gaussian");
		
		model.addAlternative(alt1);
		model.addAlternative(alt2);
		model.addCriterion(crit1);
		model.addCriterion(crit2);
		model.addCriterion(crit3);
	}
}
