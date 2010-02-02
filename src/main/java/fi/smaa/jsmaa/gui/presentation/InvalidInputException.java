package fi.smaa.jsmaa.gui.presentation;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception {

	public InvalidInputException(String reason) {
		super(reason);
	}
}
