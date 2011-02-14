package fi.smaa.common;

import java.io.StringReader;

import javolution.io.AppendableWriter;
import javolution.text.TextBuilder;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.xml.JSMAABinding;
import fi.smaa.jsmaa.model.xml.NonStringReferenceResolver;

public class XMLHelper {

	public static <T> String toXml(T obj, Class<T> cls) throws XMLStreamException {
		XMLObjectWriter writer = null;
		try {
			TextBuilder xml = TextBuilder.newInstance();
			AppendableWriter out = new AppendableWriter().setOutput(xml);
			writer = XMLObjectWriter.newInstance(out);
			writer.setBinding(new JSMAABinding());
			writer.setReferenceResolver(new NonStringReferenceResolver());		
			writer.setIndentation("\t");
			writer.write(obj, cls.getCanonicalName(), cls);
			writer.flush();		
			return xml.toString();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	

	public static <T> T fromXml(String xml) throws XMLStreamException {
		XMLObjectReader reader = null;
		try {
			StringReader sreader = new StringReader(xml);
			reader = XMLObjectReader.newInstance(sreader);
			reader.setBinding(new JSMAABinding());
			reader.setReferenceResolver(new NonStringReferenceResolver());
			return reader.<T>read();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

}
