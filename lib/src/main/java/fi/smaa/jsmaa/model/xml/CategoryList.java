/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
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
