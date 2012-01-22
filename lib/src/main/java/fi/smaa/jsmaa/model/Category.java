package fi.smaa.jsmaa.model;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class Category extends Alternative {
	
	private static final long serialVersionUID = 8243454311923512118L;

	public Category(String name) {
		super(name);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Category) {
			return super.equals(other);
		}
		return false;
	}
	
	@Override
	public Category deepCopy() {
		Category a = new Category(getName());
		return a;
	}
		
	@SuppressWarnings("unused")
	private static final XMLFormat<Category> XML = new XMLFormat<Category>(Category.class) {		
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public Category newInstance(Class<Category> cls, InputElement ie) throws XMLStreamException {
			return new Category(ie.getAttribute("name", ""));
		}
		@Override
		public void read(InputElement ie, Category alt) throws XMLStreamException {
			alt.setName(ie.getAttribute("name", ""));
		}
		@Override
		public void write(Category alt, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("name", alt.getName());
		}		
	};	

}
