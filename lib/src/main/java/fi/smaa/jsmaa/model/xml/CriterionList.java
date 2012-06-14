/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
import fi.smaa.jsmaa.model.Criterion;

public class CriterionList {

	private List<Criterion> crit;

	public CriterionList(List<Criterion> crit) {
		this.crit = crit;
	}
	
	public CriterionList() {
		crit = new ArrayList<Criterion>();
	}
	
	public List<Criterion> getList() {
		return crit;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<CriterionList> XML = new XMLFormat<CriterionList>(CriterionList.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public CriterionList newInstance(Class<CriterionList> cls, InputElement ie) throws XMLStreamException {
			return new CriterionList();
		}
		@Override
		public void read(InputElement ie, CriterionList list) throws XMLStreamException {
			while (ie.hasNext()) {
				Criterion c = ie.get("criterion");
				list.crit.add(c);
			}
		}
		@Override
		public void write(CriterionList list, OutputElement oe) throws XMLStreamException {
			for (Criterion c : list.crit) {
				oe.add(c, "criterion");
			}
		}		
	};			
}
