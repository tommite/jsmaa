package fi.smaa.jsmaa.model;

/**
 * Fully independent measurements. Each (criterion, alternative) pair has an independent Measurement.
 * @see Measurement. 
 */
public interface IndependentMeasurements extends FullJointMeasurements {
	/**
	 * Set the measurement for this (criterion, alternative) pair.
	 */
	public void setMeasurement(Criterion crit, Alternative alt, Measurement meas);
	
	/**
	 * Get the measurement for this (criterion, alternative) pair.
	 */
	public Measurement getMeasurement(Criterion crit, Alternative alt);
}