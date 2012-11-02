package fi.smaa.jsmaa.model;

@SuppressWarnings("serial")
public class PointOutsideIntervalException extends InvalidValuePointException {
	
	public PointOutsideIntervalException(String reason) {
		super(reason);
	}
	
}
