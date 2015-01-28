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
package fi.smaa.jsmaa.model;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class Point2D {
	private double x;
	private double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Point2D deepCopy() {
		return new Point2D(this.x, this.y);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Point2D) {
			Point2D po = (Point2D) o;
			return po.x == x && po.y == y;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}

	@SuppressWarnings("unused")
	private static final XMLFormat<Point2D> XML = new XMLFormat<Point2D>(Point2D.class) {
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public Point2D newInstance(Class<Point2D> cls, InputElement ie) throws XMLStreamException {
			return new Point2D(ie.getAttribute("x", 0.0), ie.getAttribute("y", 0.0));
		}
		@Override
		public void read(InputElement ie, Point2D pt) throws XMLStreamException {
			pt.x = ie.getAttribute("x", 0.0);
			pt.y = ie.getAttribute("y", 0.0);
		}

		@Override
		public void write(Point2D pt, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("x", pt.getX());
			oe.setAttribute("y", pt.getY());
		}
	};
}
