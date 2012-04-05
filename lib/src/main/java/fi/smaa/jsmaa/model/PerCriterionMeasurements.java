package fi.smaa.jsmaa.model;

/**
 * Measurements that are independent between criteria, but dependent between alternatives.
 * @see Measurement. 
 */
public interface PerCriterionMeasurements {
	/**
	 * Set the measurement for this criterion.
	 */
	public CriterionMeasurement getCriterionMeasurement(Criterion c);
	
	/**
	 * Get the measurement for this criterion.
	 */
	public void setCriterionMeasurement(Criterion c, CriterionMeasurement m);
}
