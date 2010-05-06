package fi.smaa.jsmaa.model.xml;

import javolution.xml.stream.XMLStreamException;

@SuppressWarnings("serial")
public class InvalidModelVersionException extends XMLStreamException {
	
	private int version;

	public InvalidModelVersionException(int version) {
		this.version = version;
	}
	
	public int getVersion() {
		return version;
	}

}
