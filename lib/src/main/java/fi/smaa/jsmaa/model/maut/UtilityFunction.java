/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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
package fi.smaa.jsmaa.model.maut;

import java.util.Iterator;
import java.util.List;

import fi.smaa.jsmaa.model.Point2D;
import fi.smaa.jsmaa.model.ScaleCriterion;


public class UtilityFunction {

	public static double utility(ScaleCriterion crit, double val) {

		List<Point2D> pts = crit.getValuePoints();
		Iterator<Point2D> it = pts.iterator();
		Point2D prevPt = it.next();
		while(it.hasNext()) {
			Point2D nextPt = it.next();
			if (nextPt.getX() >= val || !it.hasNext()) {
				return interpolate(prevPt, nextPt, val);
			}

			prevPt = nextPt;
		}
		throw new IllegalArgumentException("outside scale");
	}

	private static double interpolate(Point2D prevPt, Point2D nextPt, double val) {
		double ivalX = nextPt.getX() - prevPt.getX();
		double ivalY = nextPt.getY() - prevPt.getY();

		double over = val - prevPt.getX();

		return prevPt.getY() + ((over / ivalX) * ivalY);
	}
}
