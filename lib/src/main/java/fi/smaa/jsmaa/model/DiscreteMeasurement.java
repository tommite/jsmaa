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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.xml.Point2DList;

public class DiscreteMeasurement extends CardinalMeasurement implements
		List<Point2D> {

	private final String PROPERTY_DISCRETEPOINTS = "discretepoints";
	private static final long serialVersionUID = -1319270048369903772L;

	private List<Point2D> discretePoints;
	private double totalProbability;

	public DiscreteMeasurement(List<Point2D> discretePoints)
			throws PointOutsideIntervalException {
		this.discretePoints = discretePoints;
		double total = 0.0;
		for (Point2D point : discretePoints) {
			total += point.getY();
		}
		totalProbability = total;
		if (totalProbability > 1.0)
			throw new PointOutsideIntervalException("Point outside interval!");
	}

	public DiscreteMeasurement() {
		discretePoints = new ArrayList<Point2D>();
		this.totalProbability = 0.0;
	}

	@Override
	public Measurement deepCopy() {
		DiscreteMeasurement dm = new DiscreteMeasurement();
		for (Point2D point : discretePoints) {
			dm.add(point.deepCopy());
		}
		return dm;
	}

	@Override
	public Interval getRange() {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (Point2D point : discretePoints) {
			min = Math.min(min, point.getX());
			max = Math.max(max, point.getX());
		}
		return new Interval(min, max);
	}

	@Override
	public double sample(RandomUtil random) {
		if (totalProbability < 1.0) {
			throw new InvalidIntervalException();
		}
		double probability = random.createUnif01();
		double total = 0.0;
		for (Point2D point : discretePoints) {
			total += point.getY();
			if (total >= probability) {
				return point.getX();
			}
		}
		return Double.NaN; // it will never get here
	}

	public List<Point2D> getList() {
		return discretePoints;
	}

	public double getTotalProbability() {
		return totalProbability;
	}

	private boolean checkFeasibility(Collection<? extends Point2D> points) {
		for (Point2D point : points) {
			if (!checkFeasibility(point))
				return false;
		}
		return true;
	}

	private boolean checkFeasibility(Point2D point) {
		if (totalProbability + point.getY() > 1.0)
			return false;
		else
			return true;
	}

	@Override
	public boolean add(Point2D point) {
		if (!checkFeasibility(point))
			return false;
		discretePoints.add(point);
		totalProbability += point.getY();
		firePropertyChange(PROPERTY_DISCRETEPOINTS, null, null);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Point2D> points) {
		for (Point2D point : points) {
			if (!this.add(point))
				return false;
		}
		return true;
	}

	@Override
	public void clear() {
		discretePoints = new ArrayList<Point2D>();
		this.totalProbability = 0.0;

	}

	@Override
	public boolean contains(Object o) {
		return discretePoints.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> points) {
		return discretePoints.containsAll(points);
	}

	@Override
	public boolean isEmpty() {
		return discretePoints.isEmpty();
	}

	@Override
	public Iterator<Point2D> iterator() {
		return discretePoints.iterator();
	}

	@Override
	public boolean remove(Object o) {
		if (discretePoints.remove(o)) {
			totalProbability -= ((Point2D) o).getX();
			firePropertyChange(PROPERTY_DISCRETEPOINTS, null, null);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> points) {
		for (Object point : points) {
			if (!this.remove(point))
				return false;
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> points) {
		for (Point2D point : discretePoints) {
			if (this.contains(point))
				continue;
			if (!this.remove(point))
				return false;
		}
		return true;
	}

	@Override
	public int size() {
		return discretePoints.size();
	}

	@Override
	public Object[] toArray() {
		return discretePoints.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return discretePoints.toArray(a);
	}

	@Override
	public void add(int index, Point2D element) {
		if (checkFeasibility(element)) {
			discretePoints.add(index, element);
			totalProbability += element.getY();
			firePropertyChange(PROPERTY_DISCRETEPOINTS, null, null);
		}

	}

	@Override
	public boolean addAll(int index, Collection<? extends Point2D> points) {
		if (!checkFeasibility(points))
			return false;
		discretePoints.addAll(index, points);
		return true;
	}

	@Override
	public Point2D get(int index) {
		return discretePoints.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return discretePoints.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return discretePoints.lastIndexOf(o);
	}

	@Override
	public ListIterator<Point2D> listIterator() {
		return discretePoints.listIterator();
	}

	@Override
	public ListIterator<Point2D> listIterator(int index) {
		return discretePoints.listIterator();
	}

	@Override
	public Point2D remove(int index) {
		Point2D point = discretePoints.remove(index);
		totalProbability -= point.getY();
		firePropertyChange(PROPERTY_DISCRETEPOINTS, null, null);
		return point;
	}

	@Override
	public Point2D set(int index, Point2D element) {
		Point2D old = this.get(index);
		if (totalProbability - old.getY() + element.getY() > 1.0)
			return null;
		totalProbability = totalProbability - old.getY() + element.getY();
		firePropertyChange(PROPERTY_DISCRETEPOINTS, null, null);
		return discretePoints.set(index, element);
	}

	@Override
	public List<Point2D> subList(int fromIndex, int toIndex) {
		return discretePoints.subList(fromIndex, toIndex);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		DiscreteMeasurement other = (DiscreteMeasurement) obj;
		if (this.size() != other.size())
			return false;
		if (!other.containsAll(discretePoints))
			return false;
		if (other.getTotalProbability() != totalProbability)
			return false;
		return true;
	}

	protected static final XMLFormat<DiscreteMeasurement> XML = new XMLFormat<DiscreteMeasurement>(
			DiscreteMeasurement.class) {
		@Override
		public DiscreteMeasurement newInstance(Class<DiscreteMeasurement> cls,
				InputElement ie) throws XMLStreamException {
			return new DiscreteMeasurement();
		}

		@Override
		public boolean isReferenceable() {
			return false;
		}

		@Override
		public void read(InputElement ie, DiscreteMeasurement meas)
				throws XMLStreamException {
			Point2DList pts = ie.get("points", Point2DList.class);
			if (pts == null)
				return;
			for (Point2D p : pts.getList()) {
				try {
					if (!meas.add(p))
						throw new InvalidValuePointException(
								"Invalid value point");
				} catch (InvalidValuePointException e) {
					throw new XMLStreamException(e);
				}
			}

		}

		@Override
		public void write(DiscreteMeasurement meas, OutputElement oe)
				throws XMLStreamException {
			oe.add(new Point2DList(meas.getList()), "points", Point2DList.class);
		}
	};
}
