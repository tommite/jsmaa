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
import fi.smaa.jsmaa.model.Alternative;

public class AlternativeList {

	private List<Alternative> alts;

	public AlternativeList(List<Alternative> alts) {
		this.alts = alts;
	}
	
	public AlternativeList() {
		alts = new ArrayList<Alternative>();
	}
	
	public List<Alternative> getList() {
		return alts;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<AlternativeList> XML = new XMLFormat<AlternativeList>(AlternativeList.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public AlternativeList newInstance(Class<AlternativeList> cls, InputElement ie) throws XMLStreamException {
			return new AlternativeList();
		}
		@Override
		public void read(InputElement ie, AlternativeList list) throws XMLStreamException {
			while (ie.hasNext()) {
				Alternative a = ie.get("alternative", Alternative.class);
				list.alts.add(a);
			}
		}
		@Override
		public void write(AlternativeList list, OutputElement oe) throws XMLStreamException {
			for (Alternative a : list.alts) {
				oe.add(a, "alternative", Alternative.class);
			}
		}		
	};			
}
