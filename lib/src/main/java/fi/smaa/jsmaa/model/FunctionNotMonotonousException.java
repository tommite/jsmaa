package fi.smaa.jsmaa.model;

@SuppressWarnings("serial")
public class FunctionNotMonotonousException extends InvalidValuePointException {

	public FunctionNotMonotonousException(String reason) {
		super(reason);
	}
}
