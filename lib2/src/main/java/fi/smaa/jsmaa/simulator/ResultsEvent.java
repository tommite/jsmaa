package fi.smaa.jsmaa.simulator;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ResultsEvent extends EventObject {

	private Exception e;

	public ResultsEvent(Object source) {
		super(source);
	}
	
	public ResultsEvent(Object source, Exception e) {
		super(source);
		this.e = e;
	}
	
	public Exception getException() {
		return e;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof ResultsEvent;
	}
}
