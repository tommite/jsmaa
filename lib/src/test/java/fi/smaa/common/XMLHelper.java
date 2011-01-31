package fi.smaa.common;

import java.io.StringReader;

import javolution.io.AppendableWriter;
import javolution.text.TextBuilder;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.XMLReferenceResolver;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.xml.JSMAABinding;

public class XMLHelper {

	public static <T> String toXml(T obj, Class<T> cls) throws XMLStreamException {	     
	    TextBuilder xml = TextBuilder.newInstance();
	    AppendableWriter out = new AppendableWriter().setOutput(xml);
		XMLObjectWriter writer = new XMLObjectWriter().setOutput(out).setBinding(new JSMAABinding());
		writer.setReferenceResolver(new XMLReferenceResolver());		
		writer.setIndentation("\t");
		writer.write(obj, cls.getCanonicalName(), cls);
		writer.close();
		return xml.toString();
	}

	public static <T> T fromXml(String xml) throws XMLStreamException {	     
		StringReader sreader = new StringReader(xml);
		XMLObjectReader reader = new XMLObjectReader().setInput(sreader).setBinding(new JSMAABinding());
		reader.setReferenceResolver(new XMLReferenceResolver());
		try {
			T t = reader.<T>read();
			return t;
		} finally {
			reader.close();
		}
	}

}
