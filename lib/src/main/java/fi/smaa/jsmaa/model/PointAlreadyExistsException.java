package fi.smaa.jsmaa.model;

@SuppressWarnings("serial")
public class PointAlreadyExistsException extends InvalidValuePointException {

	public PointAlreadyExistsException(String reason) {
		super(reason);
	}
}
