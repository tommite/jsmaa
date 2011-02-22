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
import javolution.xml.stream.XMLStreamException;

public class ScaleCriterion extends CardinalCriterion {
	
	private static final long serialVersionUID = 306783908162696324L;
	public final static String PROPERTY_SCALE = "scale";
	private Interval scale = new Interval(0.0, 0.0);
	
	public ScaleCriterion(String name, Boolean ascending) {
		super(name, ascending);
	}
	
	public ScaleCriterion(String name) {
		super(name, true);
	}

	public Interval getScale() {
		return scale;
	}
	
	public void setScale(Interval scale) {
		Interval oldVal = this.scale;
		this.scale = scale;
		firePropertyChange(PROPERTY_SCALE, oldVal, this.scale);
	}
	
	@Override
	public String getTypeLabel() {
		return "Cardinal";
	}

	public ScaleCriterion deepCopy() {
		ScaleCriterion c = new ScaleCriterion(name, ascending);
		c.setScale(scale);
		return c;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<ScaleCriterion> XML = new XMLFormat<ScaleCriterion>(ScaleCriterion.class) {
		@Override
		public ScaleCriterion newInstance(Class<ScaleCriterion> cls, InputElement ie) throws XMLStreamException {
			return new ScaleCriterion(ie.<String>getAttribute("name", ""));
		}		
		@Override
		public boolean isReferenceable() {
			return CardinalCriterion.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, ScaleCriterion crit) throws XMLStreamException {
			CardinalCriterion.XML.read(ie, crit);
		}

		@Override
		public void write(ScaleCriterion crit, OutputElement oe) throws XMLStreamException {
			CardinalCriterion.XML.write(crit, oe);
		}		
	};
}
