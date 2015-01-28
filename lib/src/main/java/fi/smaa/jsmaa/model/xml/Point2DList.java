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
import fi.smaa.jsmaa.model.Point2D;

public class Point2DList {

	private List<Point2D> pts;

	public Point2DList(List<Point2D> pts) {
		this.pts = pts;
	}
	
	public Point2DList() {
		pts = new ArrayList<Point2D>();
	}
	
	public List<Point2D> getList() {
		return pts;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<Point2DList> XML = new XMLFormat<Point2DList>(Point2DList.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public Point2DList newInstance(Class<Point2DList> cls, InputElement ie) throws XMLStreamException {
			return new Point2DList();
		}
		@Override
		public void read(InputElement ie, Point2DList list) throws XMLStreamException {
			while (ie.hasNext()) {
				Point2D p = ie.get("point", Point2D.class);
				list.pts.add(p);
			}
		}
		@Override
		public void write(Point2DList list, OutputElement oe) throws XMLStreamException {
			for (Point2D p : list.pts) {
				oe.add(p, "point", Point2D.class);
			}
		}		
	};			
}
