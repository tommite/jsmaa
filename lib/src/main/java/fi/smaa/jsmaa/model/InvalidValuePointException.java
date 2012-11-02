package fi.smaa.jsmaa.model;

@SuppressWarnings("serial")
public class InvalidValuePointException extends Exception {

	public InvalidValuePointException(String reason) {
		super(reason);
	}
}
