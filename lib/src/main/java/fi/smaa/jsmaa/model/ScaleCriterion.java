/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.xml.Point2DList;

public class ScaleCriterion extends CardinalCriterion {
	
	public static final double DOMAIN_MAX = 1.0;
	public static final double DOMAIN_MIN = 0.0;
	private static final long serialVersionUID = 306783908162696324L;
	public final static String PROPERTY_SCALE = "scale";
	public final static String PROPERTY_VALUEPOINTS = "valuePoints";
	private Interval scale;
	private List<Point2D> addedPoints = new ArrayList<Point2D>();
	
	public ScaleCriterion(String name, Boolean ascending) {
		super(name, ascending);
		setScale(new Interval(0.0, 0.0));
	}
	
	public ScaleCriterion(String name) {
		this(name, true);
	}	

	public Interval getScale() {
		return scale;
	}
	
	public void setScale(Interval scale) {
		Interval oldVal = this.scale;
		this.scale = scale;
		removePointsOutsideScale();
		firePropertyChange(PROPERTY_SCALE, oldVal, this.scale);
		firePropertyChange(PROPERTY_VALUEPOINTS, null, null);
	}
	
	private void removePointsOutsideScale() {
		Iterator<Point2D> it = addedPoints.iterator();
		while (it.hasNext()) {
			Point2D p = it.next();
			if (p.getX() <= scale.getStart() ||
					p.getX() >= scale.getEnd()) {
				it.remove();
			}
		}
		sortAddedPoints();
	}

	private void sortAddedPoints() {
		if (getAscending()) {
			Collections.sort(addedPoints, new PointsComparator(true));
		} else {
			Collections.sort(addedPoints, new PointsComparator(false));
		}
	}
	
	private class PointsComparator implements Comparator<Point2D> {
		
		private boolean asc;
		public PointsComparator(boolean asc) {
			this.asc = asc;
		}
		
		@Override
		public int compare(Point2D p1, Point2D p2) {
			int val = Double.compare(p1.getX(), p2.getX());
			if (asc) {
				return val;
			} else {
				return 1 - val;
			}
		}
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
			Interval scale = ie.get("scale", Interval.class);
			crit.setScale(scale);
			Point2DList pts = ie.get("points", Point2DList.class);
			for (Point2D p : pts.getList()) {
				try {
					crit.addValuePoint(p);
				} catch (InvalidValuePointException e) {
					throw new XMLStreamException(e);
				}
			}
		}

		@Override
		public void write(ScaleCriterion crit, OutputElement oe) throws XMLStreamException {
			CardinalCriterion.XML.write(crit, oe);
			oe.add(crit.getScale(), "scale", Interval.class);
			oe.add(new Point2DList(crit.addedPoints), "points", Point2DList.class);
		}		
	};

	public void addValuePoint(Point2D valuePoint) throws InvalidValuePointException {
		if (valuePoint.getX() <= scale.getStart() || valuePoint.getX() >= scale.getEnd()) {
			throw new PointOutsideIntervalException();
		}
		if (valuePoint.getY() <= DOMAIN_MIN || valuePoint.getY() >= DOMAIN_MAX) {
			throw new PointOutsideIntervalException();
		}
		for (Point2D p : addedPoints) {
			if (p.getX() == valuePoint.getX()) {
				throw new PointAlreadyExistsException();
			}
			if (getAscending()) {
				if (p.getX() < valuePoint.getX() && p.getY() > valuePoint.getY()) {
					throw new FunctionNotMonotonousException();
				}
			} else { // descending
				if (p.getX() > valuePoint.getX() && p.getY() > valuePoint.getY()) {
					throw new FunctionNotMonotonousException();
				}
			}
		}
		addedPoints.add(valuePoint);
		sortAddedPoints();
		firePropertyChange(PROPERTY_VALUEPOINTS, null, null);
	}

	private Point2D getStartPoint() {
		if (getAscending()) {
			return new Point2D(scale.getStart(), DOMAIN_MIN);
		} else {
			return new Point2D(scale.getStart(), DOMAIN_MAX);
		}
	}

	public List<Point2D> getValuePoints() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(getStartPoint());
		points.addAll(addedPoints);
		points.add(getEndPoint());

		return points;
	}

	private Point2D getEndPoint() {
		if (getAscending()) {
			return new Point2D(scale.getEnd(), DOMAIN_MAX);
		} else {
			return new Point2D(scale.getEnd(), DOMAIN_MIN);
		}
	}
	
	@Override
	public void setAscending(Boolean asc) {
		boolean changePts = asc != getAscending();
		if (changePts) {
			addedPoints.clear();
		}
		super.setAscending(asc);
		if (changePts) {
			firePropertyChange(PROPERTY_VALUEPOINTS, null, null);
		}
	}
}
