/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

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

package fi.smaa.jsmaa.model;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class OrdinalCriterion extends Criterion {
	
	private static final long serialVersionUID = -1153156807411990038L;

	public OrdinalCriterion(String name) {
		super(name);
	}

	public OrdinalCriterion deepCopy() {
		return new OrdinalCriterion(name);
	}

	@Override
	public String getTypeLabel() {
		return "Ordinal";
	}
	
	protected static final XMLFormat<OrdinalCriterion> XML = new XMLFormat<OrdinalCriterion>(OrdinalCriterion.class) {
		@Override
		public OrdinalCriterion newInstance(Class<OrdinalCriterion> cls, InputElement ie) throws XMLStreamException {
			return new OrdinalCriterion(ie.getAttribute("name").toString());
		}		
		@Override
		public boolean isReferenceable() {
			return Criterion.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, OrdinalCriterion crit) throws XMLStreamException {
			Criterion.XML.read(ie, crit);
		}

		@Override
		public void write(OrdinalCriterion crit, OutputElement oe) throws XMLStreamException {
			Criterion.XML.write(crit, oe);
		}		
	};
}