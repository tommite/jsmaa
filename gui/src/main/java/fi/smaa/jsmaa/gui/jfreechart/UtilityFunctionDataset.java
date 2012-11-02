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
package fi.smaa.jsmaa.gui.jfreechart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.AbstractXYDataset;

import fi.smaa.jsmaa.model.ScaleCriterion;

@SuppressWarnings("serial")
public class UtilityFunctionDataset extends AbstractXYDataset implements PropertyChangeListener {
	
	private ScaleCriterion crit;

	public UtilityFunctionDataset(ScaleCriterion crit) {
		this.crit = crit;
		crit.addPropertyChangeListener(this);
	}
	
	public ScaleCriterion getCriterion() {
		return crit;
	}

	@Override
	public int getSeriesCount() {
		return 1;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Comparable getSeriesKey(int index) {
		if (index == 0) {
			return "value";
		}
		throw new IndexOutOfBoundsException("index " + index);
	}

	public int getItemCount(int index) {
		if (index == 0) {
			return crit.getValuePoints().size();
		}
		throw new IndexOutOfBoundsException("index " + index);
	}

	public Number getX(int series, int item) {
		if (series == 0) {
			return crit.getValuePoints().get(item).getX();
		}
		throw new IndexOutOfBoundsException("series " + series + " item " + item);
	}

	public Number getY(int series, int item) {
		if (series == 0) {
			return crit.getValuePoints().get(item).getY();
		}
		throw new IndexOutOfBoundsException("series " + series + " item " + item);
	}

	public void propertyChange(PropertyChangeEvent ev) {
		notifyListeners(new DatasetChangeEvent(this, this));
	}
}
