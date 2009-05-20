package fi.smaa;


public class UtilityFunction {
	
	public static double utility(CardinalCriterion crit, double val) {
		boolean asc = crit.getAscending();
		double overMin = val - crit.getScale().getStart();
		
		double utility = overMin / crit.getScale().getLength();
		if (!asc) {
			utility = 1.0 - utility;
		}
		return utility;
	}
}
