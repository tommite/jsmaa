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
