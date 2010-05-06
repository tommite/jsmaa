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
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import com.jgoodies.binding.beans.Observable;

public abstract class Criterion extends AbstractEntity implements Observable, DeepCopiable<Criterion>, Comparable<Criterion>, NamedObject, XMLSerializable {
	
	private static final long serialVersionUID = 6926477036734383519L;
	protected String name;
	public final static String PROPERTY_TYPELABEL = "typeLabel";

	public abstract String getTypeLabel();
	
	protected Criterion(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);		
	}

	public String getName() {
		return name;
	}
	
	public int compareTo(Criterion o) {
		return name.compareTo(o.getName());
	}
	
	protected static final XMLFormat<Criterion> XML = new XMLFormat<Criterion>(Criterion.class) {
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public void read(InputElement ie, Criterion crit) throws XMLStreamException {
			crit.name = ie.getAttribute("name", "");
		}
		@Override
		public void write(Criterion crit, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("name", crit.getName());
		}
	};
}