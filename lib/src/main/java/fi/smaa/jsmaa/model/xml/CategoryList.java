package fi.smaa.jsmaa.model.xml;

import java.util.ArrayList;
import java.util.List;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Category;

public class CategoryList {
	private List<Category> alts;

	public CategoryList(List<Category> alts) {
		this.alts = alts;
	}
	
	public CategoryList() {
		alts = new ArrayList<Category>();
	}
	
	public List<Category> getList() {
		return alts;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<CategoryList> XML = new XMLFormat<CategoryList>(CategoryList.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public CategoryList newInstance(Class<CategoryList> cls, InputElement ie) throws XMLStreamException {
			return new CategoryList();
		}
		@Override
		public void read(InputElement ie, CategoryList list) throws XMLStreamException {
			while (ie.hasNext()) {
				Category a = ie.get("category", Category.class);
				list.alts.add(a);
			}
		}
		@Override
		public void write(CategoryList list, OutputElement oe) throws XMLStreamException {
			for (Category a : list.alts) {
				oe.add(a, "category", Category.class);
			}
		}		
	};
}
