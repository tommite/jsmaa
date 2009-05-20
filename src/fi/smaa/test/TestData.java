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
