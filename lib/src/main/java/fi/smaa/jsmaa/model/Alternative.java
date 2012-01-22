/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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

public class Alternative extends AbstractEntity implements Comparable<Alternative>, NamedObject, DeepCopiable<Alternative>, XMLSerializable {
	private static final long serialVersionUID = -3443177440566082791L;
	private String name;
	public final static String PROPERTY_MEASUREMENTS = "measurements";
	
	public Alternative(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		String oldVal = this.name;
		this.name = _name;
		firePropertyChange(PROPERTY_NAME, oldVal, this.name);
	}
				
	@Override
	public String toString() {
		return name;
	}

	public Alternative deepCopy() {
		Alternative a = new Alternative(name);
		return a;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Alternative) {
			Alternative a2 = (Alternative) other;
			return name.equals(a2.getName());
		}
		return false;
			
	}

	public int compareTo(Alternative o) {
		return (name.compareTo(o.getName()));
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<Alternative> XML = new XMLFormat<Alternative>(Alternative.class) {		
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public Alternative newInstance(Class<Alternative> cls, InputElement ie) throws XMLStreamException {
			return new Alternative(ie.getAttribute("name", ""));
		}
		@Override
		public void read(InputElement ie, Alternative alt) throws XMLStreamException {
			alt.name = ie.getAttribute("name", "");
		}
		@Override
		public void write(Alternative alt, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("name", alt.getName());
		}		
	};	
}