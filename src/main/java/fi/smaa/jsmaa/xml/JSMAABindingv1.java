package fi.smaa.jsmaa.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javolution.xml.QName;
import javolution.xml.XMLBinding;
import javolution.xml.XMLFormat;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Alternative;

public class JSMAABindingv1 extends XMLBinding {

	public JSMAABindingv1() {
		super();
		setAliases();
	}

	public static String toXml(Object obj) throws XMLStreamException {	     
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLObjectWriter outs = new XMLObjectWriter().setOutput(bos).setBinding(new JSMAABindingv1());

		outs.setIndentation("\t");
		outs.write(obj);
		outs.close();
		return bos.toString();
	}

	public static Object fromXml(String xml) throws XMLStreamException {	     
		StringReader reader = new StringReader(xml);
		XMLObjectReader outs = new XMLObjectReader().setInput(reader).setBinding(new JSMAABindingv1());
		return outs.read();
	}	

	private void setAliases() {
		setAlias(Alternative.class, parseAliasQName("Alternative"));
	}

	private QName parseAliasQName(String string) {
		return QName.valueOf("", string);
	}

	@SuppressWarnings("unchecked")
	protected XMLFormat getFormat(Class forClass)  {
		if (forClass.equals(Alternative.class)) {
			return new AlternativeXMLFormat();
		} else {
			return super.getFormat(forClass);
		}
	}		

	@SuppressWarnings("deprecation")
	public static class AlternativeXMLFormat extends XMLFormat<Alternative> {

		@Override
		public boolean isReferenceable() {
			return true;
		}

		@Override
		public Alternative newInstance(Class<Alternative> cls, InputElement ie) throws XMLStreamException {
			return new Alternative(ie.getAttribute("name", "unnamed"));
		}

		@Override
		public void read(InputElement ie, Alternative alt) throws XMLStreamException {
			alt.setName(ie.getAttribute("name", "unnamed"));
		}

		@Override
		public void write(Alternative alt, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("name", alt.getName());
		}		
	}
}
